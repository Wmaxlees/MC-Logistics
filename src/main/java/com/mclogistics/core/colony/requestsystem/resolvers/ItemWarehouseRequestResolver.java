package com.mclogistics.core.colony.requestsystem.resolvers;

import static com.minecolonies.api.colony.requestsystem.requestable.deliveryman.AbstractDeliverymanRequestable.getDefaultDeliveryPriority;
import static com.minecolonies.api.util.constant.RSConstants.CONST_WAREHOUSE_RESOLVER_PRIORITY;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.mclogistics.api.colony.buildings.ModBuildings;
import com.mclogistics.api.util.constant.TranslationConstant;
import com.mclogistics.core.colony.buildings.modules.InventoryUserModule;
import com.mclogistics.core.colony.buildings.workerbuildings.BuildingItemWarehouse;
import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.colony.requestsystem.location.ILocation;
import com.minecolonies.api.colony.requestsystem.manager.IRequestManager;
import com.minecolonies.api.colony.requestsystem.request.IRequest;
import com.minecolonies.api.colony.requestsystem.request.RequestState;
import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import com.minecolonies.api.colony.requestsystem.requestable.INonExhaustiveDeliverable;
import com.minecolonies.api.colony.requestsystem.requestable.deliveryman.Delivery;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.util.*;
import com.minecolonies.api.util.constant.TypeConstants;
import com.minecolonies.core.colony.Colony;
import com.minecolonies.core.colony.requestsystem.resolvers.core.AbstractRequestResolver;
import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemWarehouseRequestResolver extends AbstractRequestResolver<IDeliverable> {
  public ItemWarehouseRequestResolver(
      @NotNull final ILocation location, @NotNull final IToken<?> token) {
    super(location, token);
  }

  @Override
  public TypeToken<? extends IDeliverable> getRequestType() {
    return TypeConstants.DELIVERABLE;
  }

  @Override
  public boolean canResolveRequest(
      @NotNull final IRequestManager manager,
      final IRequest<? extends IDeliverable> requestToCheck) {
    if (requestToCheck.getRequester().getLocation().equals(getLocation())) {
      // Don't fulfill its own requests
      return false;
    }

    if (!manager.getColony().getWorld().isClientSide) {
      final Colony colony = (Colony) manager.getColony();

      if (!isRequestChainValid(manager, requestToCheck, requestToCheck)) {
        return false;
      }

      try {
        final List<BuildingItemWarehouse> wareHouses = new ArrayList<>();
        for (final Map.Entry<BlockPos, IBuilding> building :
            colony.getBuildingManager().getBuildings().entrySet()) {
          if (building.getValue().getBuildingType() == ModBuildings.itemWarehouse.get()) {
            wareHouses.add((BuildingItemWarehouse) building.getValue());
          }
        }
        return internalCanResolve(wareHouses, requestToCheck);
      } catch (Exception e) {
        Log.getLogger().error(e);
      }
    }
    return false;
  }

  @Nullable
  @Override
  public List<IToken<?>> attemptResolveRequest(
      @NotNull final IRequestManager manager,
      @NotNull final IRequest<? extends IDeliverable> request) {
    if (manager.getColony().getWorld().isClientSide) {
      return Lists.newArrayList();
    }

    if (!(manager.getColony() instanceof Colony)) {
      return Lists.newArrayList();
    }

    final Colony colony = (Colony) manager.getColony();

    final BuildingItemWarehouse warehouse =
        (BuildingItemWarehouse)
            colony.getBuildingManager().getBuilding(getLocation().getInDimensionLocation());

    final int totalRequested = request.getRequest().getCount();
    int totalAvailable = 0;
    if (request.getRequest() instanceof INonExhaustiveDeliverable) {
      totalAvailable -= ((INonExhaustiveDeliverable) request.getRequest()).getLeftOver();
    }

    final InventoryUserModule module = warehouse.getFirstModuleOccurance(InventoryUserModule.class);
    final List<Tuple<ItemStack, BlockPos>> inv =
        module.getMatchingItemStacks(itemStack -> request.getRequest().matches(itemStack));
    for (final Tuple<ItemStack, BlockPos> stack : inv) {
      if (!stack.getA().isEmpty()) {
        totalAvailable += stack.getA().getCount();
      }
    }

    if (totalAvailable >= totalRequested
        || totalAvailable >= request.getRequest().getMinimumCount()) {
      return Lists.newArrayList();
    }

    if (totalAvailable < 0) {
      totalAvailable = 0;
    }

    final int totalRemainingRequired = totalRequested - totalAvailable;
    return Lists.newArrayList(
        manager.createRequest(this, request.getRequest().copyWithCount(totalRemainingRequired)));
  }

  @Override
  public void resolveRequest(
      @NotNull final IRequestManager manager,
      @NotNull final IRequest<? extends IDeliverable> request) {
    manager.updateRequestState(request.getId(), RequestState.RESOLVED);
  }

  @Nullable
  @Override
  public List<IRequest<?>> getFollowupRequestForCompletion(
      @NotNull final IRequestManager manager,
      @NotNull final IRequest<? extends IDeliverable> completedRequest) {
    if (manager.getColony().getWorld().isClientSide) {
      return null;
    }

    final Colony colony = (Colony) manager.getColony();
    final BuildingItemWarehouse warehouse =
        (BuildingItemWarehouse)
            colony.getBuildingManager().getBuilding(getLocation().getInDimensionLocation());

    List<IRequest<?>> deliveries = Lists.newArrayList();
    int remainingCount = completedRequest.getRequest().getCount();

    final Map<ItemStorage, Integer> storages = new HashMap<>();

    final int keep =
        completedRequest.getRequest() instanceof INonExhaustiveDeliverable
            ? ((INonExhaustiveDeliverable) completedRequest.getRequest()).getLeftOver()
            : 0;

    final InventoryUserModule module = warehouse.getFirstModuleOccurance(InventoryUserModule.class);
    final List<Tuple<ItemStack, BlockPos>> targetStacks =
        module.getMatchingItemStacks(itemStack -> completedRequest.getRequest().matches(itemStack));
    for (final Tuple<ItemStack, BlockPos> tuple : targetStacks) {
      if (ItemStackUtils.isEmpty(tuple.getA())) {
        continue;
      }

      int leftOver = tuple.getA().getCount();
      if (keep > 0) {
        int kept = storages.getOrDefault(new ItemStorage(tuple.getA()), 0);
        if (kept < keep) {
          if (leftOver + kept <= keep) {
            storages.put(
                new ItemStorage(tuple.getA()),
                storages.getOrDefault(new ItemStorage(tuple.getA()), 0) + tuple.getA().getCount());
            continue;
          }
          int toKeep = (leftOver + kept) - keep;
          leftOver -= toKeep;
          storages.put(
              new ItemStorage(tuple.getA()),
              storages.getOrDefault(new ItemStorage(tuple.getA()), 0) + toKeep);
        }
      }

      int count = Math.min(remainingCount, leftOver);
      final ItemStack matchingStack = tuple.getA().copy();
      matchingStack.setCount(count);

      completedRequest.addDelivery(matchingStack);

      final ILocation itemStackLocation =
          manager
              .getFactoryController()
              .getNewInstance(TypeConstants.ILOCATION, tuple.getB(), colony.getDimension());

      final Delivery delivery =
          new Delivery(
              itemStackLocation,
              completedRequest.getRequester().getLocation(),
              matchingStack,
              getDefaultDeliveryPriority(true));

      final IToken<?> requestToken = manager.createRequest(this, delivery);
      deliveries.add(manager.getRequestForToken(requestToken));
      remainingCount -= count;
      if (remainingCount <= 0) {
        break;
      }
    }

    return deliveries.isEmpty() ? null : deliveries;
  }

  @Override
  public void onAssignedRequestBeingCancelled(
      @NotNull final IRequestManager manager,
      @NotNull final IRequest<? extends IDeliverable> request) {}

  @Override
  public void onAssignedRequestCancelled(
      @NotNull final IRequestManager manager,
      @NotNull final IRequest<? extends IDeliverable> request) {}

  @Override
  public void onRequestedRequestComplete(
      @NotNull final IRequestManager manager, @NotNull final IRequest<?> request) {}

  @Override
  public void onRequestedRequestCancelled(
      @NotNull final IRequestManager manager, @NotNull final IRequest<?> request) {}

  @NotNull
  @Override
  public MutableComponent getRequesterDisplayName(
      @NotNull final IRequestManager manager, @NotNull final IRequest<?> request) {
    return Component.translatable(TranslationConstant.BUILDING_ITEM_WAREHOUSE_NAME);
  }

  @Override
  public int getPriority() {
    return CONST_WAREHOUSE_RESOLVER_PRIORITY;
  }

  private boolean internalCanResolve(
      List<BuildingItemWarehouse> wareHouses, IRequest<? extends IDeliverable> requestToCheck) {
    int totalCount = 0;
    for (final BuildingItemWarehouse warehouse : wareHouses) {
      for (@NotNull final BlockPos pos : warehouse.getContainers()) {
        if (WorldUtil.isBlockLoaded(warehouse.getColony().getWorld(), pos)) {
          final BlockEntity entity = warehouse.getColony().getWorld().getBlockEntity(pos);
          final Optional<IItemHandler> handler =
              entity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
          if (handler.isPresent()) {
            totalCount +=
                InventoryUtils.getItemCountInItemHandler(
                    handler.get(), itemStack -> requestToCheck.getRequest().matches(itemStack));
            if (totalCount >= requestToCheck.getRequest().getMinimumCount()) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  public boolean isRequestChainValid(
      @NotNull final IRequestManager manager,
      final IRequest<?> requestToCheck,
      final IRequest<?> initialRequest) {
    if (!requestToCheck.hasParent()) {
      return true;
    }

    if (requestToCheck.equals(initialRequest)) {
      return false;
    }

    final IRequest<?> parentRequest = manager.getRequestForToken(requestToCheck.getParent());

    // Should not happen but just to be sure.
    if (parentRequest == null) {
      return true;
    }

    return isRequestChainValid(manager, parentRequest, initialRequest);
  }
}
