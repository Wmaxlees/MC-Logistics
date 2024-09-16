package com.mclogistics.core.entity.ai.workers.storage;

import static com.mclogistics.api.util.constant.TranslationConstant.*;
import static com.mclogistics.core.entity.ai.statemachine.states.AIWorkerState.*;
import static com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState.*;

import com.google.common.reflect.TypeToken;
import com.mclogistics.core.colony.buildings.modules.InventoryUserModule;
import com.mclogistics.core.colony.buildings.workerbuildings.BuildingItemWarehouse;
import com.mclogistics.core.colony.jobs.JobItemWarehouseManager;
import com.minecolonies.api.colony.interactionhandling.ChatPriority;
import com.minecolonies.api.colony.requestsystem.requestable.StackList;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.entity.ai.statemachine.AITarget;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.entity.citizen.VisibleCitizenStatus;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.api.util.constant.translation.RequestSystemTranslationConstants;
import com.minecolonies.core.colony.buildings.modules.ItemListModule;
import com.minecolonies.core.colony.interactionhandling.StandardInteraction;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIInteract;
import com.minecolonies.core.tileentities.TileEntityRack;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class EntityAIWorkItemWarehouseManager
    extends AbstractEntityAIInteract<JobItemWarehouseManager, BuildingItemWarehouse> {
  public static final String ITEM_WAREHOUSE_LIST = "item_warehouse_list";

  public EntityAIWorkItemWarehouseManager(@NotNull JobItemWarehouseManager job) {
    super(job);

    super.registerTargets(
        new AITarget(IDLE, START_WORKING, STANDARD_DELAY),
        new AITarget(START_WORKING, this::startWorking, 60),
        new AITarget(COLLECT_ITEMS_TO_STORE, this::collectItemsToStore, 60),
        new AITarget(STORE_ITEMS, this::storeItems, 60));
  }

  @Override
  public Class<BuildingItemWarehouse> getExpectedBuildingClass() {
    return BuildingItemWarehouse.class;
  }

  private IAIState startWorking() {
    if (walkToBuilding()) {
      return getState();
    }

    final ItemListModule itemListModule =
        building.getModuleMatching(
            ItemListModule.class, m -> m.getId().equals(ITEM_WAREHOUSE_LIST));
    worker.getCitizenData().setVisibleStatus(VisibleCitizenStatus.WORKING);

    final int itemsInBuilding =
        InventoryUtils.getCountFromBuilding(
            building, stack -> itemListModule.isItemInList(new ItemStorage(stack)));
    final int itemsInInv =
        InventoryUtils.getItemCountInItemHandler(
            (worker.getInventoryCitizen()),
            stack -> itemListModule.isItemInList(new ItemStorage(stack)));

    if (itemsInInv + itemsInBuilding <= 0) {
      requestItems();
    }

    if (itemsInBuilding > 0 && itemsInInv == 0) {
      needsCurrently = new Tuple<>(stack -> itemListModule.isItemInList(new ItemStorage(stack)), 1);
      return COLLECT_ITEMS_TO_STORE;
    }

    if (itemsInInv > 0) {
      return STORE_ITEMS;
    }

    return IDLE;
  }

  private IAIState collectItemsToStore() {
    final List<ItemStorage> allowedItems =
        building
            .getModuleMatching(ItemListModule.class, m -> m.getId().equals(ITEM_WAREHOUSE_LIST))
            .getList();

    if (allowedItems.isEmpty()) {
      if (worker.getCitizenData() != null) {
        worker
            .getCitizenData()
            .triggerInteraction(
                new StandardInteraction(
                    Component.translatable(ITEM_WAREHOUSE_MANAGER_NO_ITEMS),
                    ChatPriority.BLOCKING));
        return COLLECT_ITEMS_TO_STORE;
      }
    }

    if (walkTo == null) {
      final List<BlockPos> allContainers = building.getContainers();
      for (final BlockPos container : allContainers) {
        BlockEntity blockEntity = world.getBlockEntity(container);
        if (blockEntity instanceof TileEntityRack rack
            && rack.hasItemStack(stack -> allowedItems.contains(new ItemStorage(stack)))) {
          walkTo = container;
          break;
        }
      }

      if (walkTo == null) {
        return getStateAfterPickUp();
      }
    }

    if (walkToBlock(walkTo)) {
      return getState();
    }

    if (!getAllItemsToStoreFrom(walkTo)) {
      walkTo = null;
      return START_WORKING;
    }

    walkTo = null;
    return getStateAfterPickUp();
  }

  private boolean getAllItemsToStoreFrom(BlockPos containerPos) {
    Optional<IItemHandler> rackHandler = getItemHandlerFrom(containerPos);
    if (rackHandler.isEmpty()) {
      building.removeContainerPosition(containerPos);
      return false;
    }

    final List<ItemStorage> allowedItems =
        building
            .getModuleMatching(ItemListModule.class, m -> m.getId().equals(ITEM_WAREHOUSE_LIST))
            .getList();
    if (allowedItems.isEmpty()) {
      return false;
    }

    return InventoryUtils.transferItemStackIntoNextBestSlotInItemHandler(
        rackHandler.get(),
        stack -> allowedItems.contains(new ItemStorage(stack)),
        worker.getInventoryCitizen());
  }

  private void requestItems() {
    if (!building.hasWorkerOpenRequestsOfType(
            worker.getCitizenData().getId(), TypeToken.of(StackList.class))
        && !building.hasWorkerOpenRequestsFiltered(
            worker.getCitizenData().getId(),
            req ->
                req.getShortDisplayString()
                    .getSiblings()
                    .contains(
                        Component.translatable(
                            RequestSystemTranslationConstants.REQUEST_SYSTEM_STACK_LIST)))) {
      final List<ItemStorage> allowedItems =
          building
              .getModuleMatching(ItemListModule.class, m -> m.getId().equals(ITEM_WAREHOUSE_LIST))
              .getList();

      if (allowedItems.isEmpty()) {
        if (worker.getCitizenData() != null) {
          worker
              .getCitizenData()
              .triggerInteraction(
                  new StandardInteraction(
                      Component.translatable(ITEM_WAREHOUSE_MANAGER_NO_ITEMS),
                      ChatPriority.BLOCKING));
        }
      } else {
        worker
            .getCitizenData()
            .createRequestAsync(
                new StackList(
                    allowedItems.stream().map(ItemStorage::getItemStack).toList(),
                    "Item Warehouse",
                    Integer.MAX_VALUE,
                    1));
      }
    }
  }

  private IAIState storeItems() {
    final ItemListModule itemListModule =
        building.getModuleMatching(
            ItemListModule.class, m -> m.getId().equals(ITEM_WAREHOUSE_LIST));
    final int targetSlot =
        InventoryUtils.findFirstSlotInItemHandlerWith(
            (worker.getInventoryCitizen()),
            stack -> itemListModule.isItemInList(new ItemStorage(stack)));

    if (targetSlot == -1) {
      return START_WORKING;
    }

    final ItemStack targetStack = worker.getInventoryCitizen().getStackInSlot(targetSlot);

    if (walkTo == null) {
      return findChestForStackAndSetMoveTo(targetStack) ? getState() : START_WORKING;
    }

    if (walkToBlock(walkTo)) {
      return getState();
    }

    Optional<IItemHandler> chestHandler = getItemHandlerFrom(walkTo);
    if (chestHandler.isEmpty()) {
      walkTo = null;
      return START_WORKING;
    }

    List<Integer> slots =
        InventoryUtils.findAllSlotsInItemHandlerWith(
            worker.getInventoryCitizen(),
            stack -> itemListModule.isItemInList(new ItemStorage(stack)));
    for (final int slot : slots) {
      InventoryUtils.transferItemStackIntoNextBestSlotInItemHandler(
          worker.getItemHandlerCitizen(), slot, chestHandler.get());
    }

    walkTo = null;
    return START_WORKING;
  }

  private boolean findChestForStackAndSetMoveTo(ItemStack targetStack) {
    final InventoryUserModule inventoryUserModule =
        building.getFirstModuleOccurance(InventoryUserModule.class);

    for (BlockPos chestPos : inventoryUserModule.getChests()) {
      Optional<IItemHandler> chestHandler = getItemHandlerFrom(chestPos);
      if (chestHandler.isEmpty()) {
        continue;
      }

      if (!canItemStackBePlacedInHandler(chestHandler.get(), targetStack, true)) {
        continue;
      }

      walkTo = chestPos;
      return true;
    }

    return false;
  }

  protected boolean canItemStackBePlacedInHandler(
      IItemHandler itemHandler, ItemStack stack, boolean partialOkay) {
    if (partialOkay) {
      stack = stack.copy();
      stack.setCount(1);
    }
    for (int i = 0; i < itemHandler.getSlots(); i++) {
      if (itemHandler.insertItem(i, stack, true).isEmpty()) {
        return true;
      }
    }

    return false;
  }

  private Optional<IItemHandler> getItemHandlerFrom(BlockPos pos) {
    BlockEntity chest = building.getColony().getWorld().getBlockEntity(pos);

    if (chest == null) {
      return Optional.empty();
    }

    return chest.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
  }
}
