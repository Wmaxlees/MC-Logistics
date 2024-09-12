package com.wmaxlees.gregcolonies.core.colony.requestsystem.resolvers;

import static com.minecolonies.api.colony.requestsystem.requestable.deliveryman.AbstractDeliverymanRequestable.getDefaultDeliveryPriority;
import static com.minecolonies.api.util.constant.RSConstants.CONST_WAREHOUSE_RESOLVER_PRIORITY;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.colony.requestsystem.location.ILocation;
import com.minecolonies.api.colony.requestsystem.manager.IRequestManager;
import com.minecolonies.api.colony.requestsystem.request.IRequest;
import com.minecolonies.api.colony.requestsystem.request.RequestState;
import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import com.minecolonies.api.colony.requestsystem.requestable.deliveryman.Delivery;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.WorldUtil;
import com.minecolonies.api.util.constant.TypeConstants;
import com.minecolonies.core.colony.Colony;
import com.minecolonies.core.colony.requestsystem.resolvers.core.AbstractRequestResolver;
import com.minecolonies.core.tileentities.TileEntityRack;
import com.wmaxlees.gregcolonies.api.colony.buildings.ModBuildings;
import com.wmaxlees.gregcolonies.api.util.constant.TranslationConstant;
import com.wmaxlees.gregcolonies.core.colony.buildings.modules.InventoryUserModule;
import com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings.BuildingFluidWarehouse;
import com.wmaxlees.gregcolonies.core.colony.requestable.CourierTankRequestable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidWarehouseRequestResolver extends AbstractRequestResolver<IDeliverable> {
  public FluidWarehouseRequestResolver(
      @NotNull final ILocation location, @NotNull final IToken<?> token) {
    super(location, token);
  }

  protected boolean internalCanResolve(
      final Level level,
      final List<BuildingFluidWarehouse> fluidWarehouses,
      final IRequest<? extends IDeliverable> requestToCheck) {
    // Right now, we only support single fluid requests
    if (!(requestToCheck.getRequest() instanceof CourierTankRequestable)) {
      return false;
    }

    final CourierTankRequestable courierTankRequestable =
        (CourierTankRequestable) requestToCheck.getRequest();

    int remainingRequestedMillibuckets = courierTankRequestable.getCount();
    for (BuildingFluidWarehouse fluidWarehouse : fluidWarehouses) {
      InventoryUserModule invModule =
          fluidWarehouse.getFirstModuleOccurance(InventoryUserModule.class);
      if (invModule == null) {
        continue;
      }
      remainingRequestedMillibuckets -=
          invModule.getMilliBucketsAvailableOf(courierTankRequestable::fluidMatch);

      if (remainingRequestedMillibuckets <= 0) {
        return true;
      }
    }

    return false;
  }

  @Override
  public TypeToken<? extends IDeliverable> getRequestType() {
    return TypeConstants.DELIVERABLE;
  }

  @Override
  public boolean canResolveRequest(
      @NotNull IRequestManager manager, IRequest<? extends IDeliverable> requestToCheck) {
    if (manager.getColony().getWorld().isClientSide) {
      return false;
    }

    final Colony colony = (Colony) manager.getColony();
    if (colony
            .getBuildingManager()
            .getBuilding(requestToCheck.getRequester().getLocation().getInDimensionLocation())
            .getBuildingType()
        == ModBuildings.fluidWarehouse.get()) {
      return false;
    }

    final IBuilding fluidWarehouse =
        colony.getBuildingManager().getBuilding(getLocation().getInDimensionLocation());
    if (fluidWarehouse == null) {
      return false;
    }

    if (requestHasLoop(manager, requestToCheck, requestToCheck)) {
      return false;
    }

    final List<BuildingFluidWarehouse> wareHouses = new ArrayList<>();
    for (final Map.Entry<BlockPos, IBuilding> building :
        colony.getBuildingManager().getBuildings().entrySet()) {
      if (building.getValue().getBuildingType() == ModBuildings.fluidWarehouse.get()) {
        wareHouses.add((BuildingFluidWarehouse) building.getValue());
      }
    }
    return internalCanResolve(colony.getWorld(), wareHouses, requestToCheck);
  }

  @Override
  public @Nullable List<IToken<?>> attemptResolveRequest(
      @NotNull IRequestManager manager, @NotNull IRequest<? extends IDeliverable> request) {
    if (manager.getColony().getWorld().isClientSide) {
      return List.of();
    }

    if (!(manager.getColony() instanceof Colony)) {
      return List.of();
    }

    // Right now, we only support single fluid requests
    if (!(request.getRequest() instanceof CourierTankRequestable)) {
      return List.of();
    }

    final CourierTankRequestable courierTankRequestable =
        (CourierTankRequestable) request.getRequest();
    final Colony colony = (Colony) manager.getColony();
    if (!(colony.getBuildingManager().getBuilding(getLocation().getInDimensionLocation())
        instanceof BuildingFluidWarehouse)) {
      return List.of();
    }

    final BuildingFluidWarehouse fluidWarehouse =
        (BuildingFluidWarehouse)
            colony.getBuildingManager().getBuilding(getLocation().getInDimensionLocation());

    final int totalRequestedMillibuckets = courierTankRequestable.getCount();
    InventoryUserModule invModule =
        fluidWarehouse.getFirstModuleOccurance(InventoryUserModule.class);
    if (invModule == null) {
      return List.of();
    }

    final int totalAvailableMilliBuckets =
        invModule.getMilliBucketsAvailableOf(courierTankRequestable::fluidMatch);

    if (totalAvailableMilliBuckets <= 0) {
      return List.of();
    }

    final int totalRemainingRequired =
        Math.max(0, totalRequestedMillibuckets - totalAvailableMilliBuckets);
    return Lists.newArrayList(
        manager.createRequest(this, request.getRequest().copyWithCount(totalRemainingRequired)));
  }

  @Override
  public void resolveRequest(
      @NotNull final IRequestManager manager,
      @NotNull final IRequest<? extends IDeliverable> request) {
    manager.updateRequestState(request.getId(), RequestState.RESOLVED);
  }

  @Override
  public List<IRequest<?>> getFollowupRequestForCompletion(
      @NotNull final IRequestManager manager,
      @NotNull final IRequest<? extends IDeliverable> request) {
    if (manager.getColony().getWorld().isClientSide) {
      return null;
    }

    if (!(manager.getColony() instanceof Colony)) {
      return null;
    }

    // Right now, we only support single fluid requests
    if (!(request.getRequest() instanceof CourierTankRequestable)) {
      return null;
    }

    final CourierTankRequestable courierTankRequestable =
        (CourierTankRequestable) request.getRequest();
    final Colony colony = (Colony) manager.getColony();
    if (!(colony.getBuildingManager().getBuilding(getLocation().getInDimensionLocation())
        instanceof BuildingFluidWarehouse)) {
      return null;
    }

    final BuildingFluidWarehouse fluidWarehouse =
        (BuildingFluidWarehouse)
            colony.getBuildingManager().getBuilding(getLocation().getInDimensionLocation());

    final int totalRequestedMillibuckets = courierTankRequestable.getCount();
    InventoryUserModule invModule =
        fluidWarehouse.getFirstModuleOccurance(InventoryUserModule.class);
    if (invModule == null) {
      return null;
    }

    final int totalAvailableMilliBuckets =
        invModule.getMilliBucketsAvailableOf(courierTankRequestable::fluidMatch);

    if (totalAvailableMilliBuckets <= 0) {
      return null;
    }

    Pair<ItemStack, ItemStack> filledTanks =
        invModule.tryDrainToCourierTank(
            courierTankRequestable.getFluid(), totalRequestedMillibuckets);

    BlockPos fullPlaced = BlockPos.ZERO;
    BlockPos partialPlaced = BlockPos.ZERO;
    for (@NotNull final BlockPos pos : fluidWarehouse.getContainers()) {
      if (WorldUtil.isBlockLoaded(fluidWarehouse.getColony().getWorld(), pos)) {
        final BlockEntity entity = fluidWarehouse.getColony().getWorld().getBlockEntity(pos);
        if (entity instanceof final TileEntityRack rack) {
          if (fullPlaced.equals(BlockPos.ZERO)
              && InventoryUtils.addItemStackToItemHandler(
                  rack.getInventory(), filledTanks.getKey())) {
            fullPlaced = pos;
          }

          if (partialPlaced.equals(BlockPos.ZERO)
              && InventoryUtils.addItemStackToItemHandler(
                  rack.getInventory(), filledTanks.getValue())) {
            partialPlaced = pos;
          }
        }
      }
    }

    request.addDelivery(filledTanks.getKey());
    request.addDelivery(filledTanks.getValue());

    final ILocation fullLocation =
        manager
            .getFactoryController()
            .getNewInstance(
                TypeConstants.ILOCATION, fullPlaced, fluidWarehouse.getColony().getWorld());
    final ILocation partialLocation =
        manager
            .getFactoryController()
            .getNewInstance(
                TypeConstants.ILOCATION, partialPlaced, fluidWarehouse.getColony().getWorld());

    final Delivery fullDelivery =
        new Delivery(
            fullLocation,
            request.getRequester().getLocation(),
            filledTanks.getKey(),
            getDefaultDeliveryPriority(true));
    final Delivery partialDelivery =
        new Delivery(
            partialLocation,
            request.getRequester().getLocation(),
            filledTanks.getValue(),
            getDefaultDeliveryPriority(true));

    final IToken<?> fullRequestToken = manager.createRequest(this, fullDelivery);
    final IToken<?> partialRequestToken = manager.createRequest(this, partialDelivery);

    return List.of(
        manager.getRequestForToken(fullRequestToken),
        manager.getRequestForToken(partialRequestToken));
  }

  public boolean requestHasLoop(
      @NotNull final IRequestManager manager,
      @NotNull final IRequest<?> requestToCheck,
      final IRequest<?> initialRequest) {
    if (!requestToCheck.hasParent()) {
      return false;
    }

    if (requestToCheck.equals(initialRequest)) {
      return true;
    }

    final IRequest<?> parentRequest = manager.getRequestForToken(requestToCheck.getParent());

    // Should not happen but just to be sure.
    if (parentRequest == null) {
      return false;
    }

    return requestHasLoop(manager, parentRequest, initialRequest);
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
    return Component.translatable(TranslationConstant.BUILDING_FLUID_WAREHOUSE_NAME);
  }

  @Override
  public int getPriority() {
    return CONST_WAREHOUSE_RESOLVER_PRIORITY;
  }
}
