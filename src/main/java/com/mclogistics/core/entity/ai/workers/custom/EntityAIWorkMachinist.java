package com.mclogistics.core.entity.ai.workers.custom;

import static com.mclogistics.api.util.constant.TranslationConstant.*;
import static com.mclogistics.core.entity.ai.statemachine.states.AIWorkerState.INSERT_ITEMS;
import static com.mclogistics.core.entity.ai.statemachine.states.AIWorkerState.RETRIEVE_RESULTS;
import static com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState.*;

import com.mclogistics.api.items.ModItems;
import com.mclogistics.core.colony.buildings.workerbuildings.BuildingMachinist;
import com.mclogistics.core.colony.jobs.JobMachinist;
import com.minecolonies.api.colony.interactionhandling.ChatPriority;
import com.minecolonies.api.colony.requestsystem.request.RequestState;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.entity.ai.statemachine.AITarget;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.core.colony.interactionhandling.StandardInteraction;
import com.minecolonies.core.entity.ai.workers.crafting.AbstractEntityAICrafting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class EntityAIWorkMachinist
    extends AbstractEntityAICrafting<JobMachinist, BuildingMachinist> {
  /**
   * Sets up some important skeleton stuff for every ai.
   *
   * @param job the job class
   */
  public EntityAIWorkMachinist(@NotNull JobMachinist job) {
    super(job);
    super.registerTargets(
        new AITarget(INSERT_ITEMS, this::putItems, STANDARD_DELAY),
        new AITarget(RETRIEVE_RESULTS, this::retrieveResults, STANDARD_DELAY));
  }

  @Override
  public Class<BuildingMachinist> getExpectedBuildingClass() {
    return BuildingMachinist.class;
  }

  @Override
  protected IAIState craft() {
    if (currentRecipeStorage == null || job.getCurrentTask() == null) {
      return START_WORKING;
    }

    if (currentRequest == null && job.getCurrentTask() != null) {
      return GET_RECIPE;
    }

    if (walkToBuilding()) {
      return getState();
    }

    currentRequest = job.getCurrentTask();

    if (currentRequest != null
        && (currentRequest.getState() == RequestState.CANCELLED
            || currentRequest.getState() == RequestState.FAILED)) {
      currentRequest = null;
      incrementActionsDone(getActionRewardForCraftingSuccess());
      currentRecipeStorage = null;
      return START_WORKING;
    }

    if (!checkInputOkay()) {
      return START_WORKING;
    }

    if (!checkOutputOkay()) {
      return START_WORKING;
    }

    final IAIState check = checkForItems(currentRecipeStorage);
    if (check == CRAFT) {
      return INSERT_ITEMS;
    } else {
      currentRequest = null;
      job.finishRequest(false);
      incrementActionsDoneAndDecSaturation();
      resetValues();
    }

    return getState();
  }

  protected IAIState putItems() {
    if (currentRequest == null) {
      return START_WORKING;
    }

    if (!checkInputOkay()) {
      return START_WORKING;
    }

    if (!checkOutputOkay()) {
      return START_WORKING;
    }

    if (checkForItems(currentRecipeStorage) != CRAFT) {
      return START_WORKING;
    }

    removeRequiredItemsFromWorker();
    addRequiredItemsToInput();

    return RETRIEVE_RESULTS;
  }

  protected IAIState retrieveResults() {
    if (!checkOutputOkay()) {
      return START_WORKING;
    }

    if (!checkResultInOutput()) {
      return RETRIEVE_RESULTS;
    }

    List<ItemStack> removedItems = removeItemsFromOutput();
    for (ItemStack removedItem : removedItems) {
      InventoryUtils.transferItemStackIntoNextBestSlotInItemHandler(
          removedItem, worker.getInventoryCitizen());
    }
    removedItems = removeFluidsFromOutput();
    for (ItemStack removedItem : removedItems) {
      InventoryUtils.transferItemStackIntoNextBestSlotInItemHandler(
          removedItem, worker.getInventoryCitizen());
    }

    currentRequest.addDelivery(currentRecipeStorage.getPrimaryOutput());
    job.setCraftCounter(job.getCraftCounter() + 1);
    if (job.getCraftCounter() >= job.getMaxCraftingCount()) {
      incrementActionsDone(getActionRewardForCraftingSuccess());

      currentRecipeStorage = null;
      resetValues();

      if (inventoryNeedsDump()
          && job.getMaxCraftingCount() == 0
          && job.getProgress() == 0
          && job.getCraftCounter() == 0
          && currentRequest != null) {
        worker
            .getCitizenExperienceHandler()
            .addExperience(currentRequest.getRequest().getCount() / 2.0);
      }
      return INVENTORY_FULL;
    } else {
      job.setProgress(0);
      return GET_RECIPE;
    }
  }

  private void removeRequiredItemsFromWorker() {
    for (final ItemStorage storage : currentRecipeStorage.getCleanedInput()) {
      final ItemStack stack = storage.getItemStack();
      int amountNeeded = storage.getAmount();

      if (amountNeeded == 0) {
        break;
      }

      IItemHandler handler = worker.getItemHandlerCitizen();

      int slotOfStack =
          InventoryUtils.findFirstSlotInItemHandlerNotEmptyWith(
              handler,
              itemStack ->
                  !ItemStackUtils.isEmpty(itemStack)
                      && ItemStackUtils.compareItemStacksIgnoreStackSize(
                          itemStack, stack, false, !storage.ignoreNBT()));

      while (slotOfStack != -1 && amountNeeded > 0) {
        final int count = ItemStackUtils.getSize(handler.getStackInSlot(slotOfStack));
        final ItemStack extractedStack =
            handler.extractItem(slotOfStack, amountNeeded, false).copy();

        // This prevents the AI and for that matter the server from getting stuck in case of an
        // emergency.
        // Deletes some items, but hey.
        if (ItemStackUtils.isEmpty(extractedStack)) {
          handler.insertItem(slotOfStack, extractedStack, false);
          return;
        }

        amountNeeded -= count;
        if (amountNeeded > 0) {
          slotOfStack =
              InventoryUtils.findFirstSlotInItemHandlerNotEmptyWith(
                  handler,
                  itemStack ->
                      !ItemStackUtils.isEmpty(itemStack)
                          && ItemStackUtils.compareItemStacksIgnoreStackSize(
                              itemStack, stack, false, !storage.ignoreNBT()));
        }
      }

      // stop looping handlers if we have what we need
      if (amountNeeded <= 0) {
        break;
      }
    }
  }

  private boolean addRequiredItemsToInput() {
    BlockEntity inputBlockEntity = worker.level().getBlockEntity(building.getInputLocation());
    IItemHandler inputItemHandler =
        inputBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();

    for (final ItemStorage storage : currentRecipeStorage.getCleanedInput()) {
      if (!InventoryUtils.transferItemStackIntoNextBestSlotInItemHandler(
          storage.getItemStack(), inputItemHandler)) {
        return false;
      }
    }

    return true;
  }

  private boolean checkResultInOutput() {
    BlockEntity outputBlockEntity = worker.level().getBlockEntity(building.getOutputLocation());
    IItemHandler outputItemHandler =
        outputBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();

    int remaining = currentRecipeStorage.getPrimaryOutput().getCount();
    for (int i = 0; i < outputItemHandler.getSlots(); ++i) {
      if (outputItemHandler
          .getStackInSlot(i)
          .is(currentRecipeStorage.getPrimaryOutput().getItem())) {
        remaining -= outputItemHandler.getStackInSlot(i).getCount();
        if (remaining <= 0) {
          return true;
        }
      }
    }

    return false;
  }

  private List<ItemStack> removeItemsFromOutput() {
    List<ItemStack> result = new ArrayList<>();

    BlockEntity outputBlockEntity = worker.level().getBlockEntity(building.getOutputLocation());
    IItemHandler outputItemHandler =
        outputBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();

    for (int i = 0; i < outputItemHandler.getSlots(); ++i) {
      ItemStack stack = outputItemHandler.getStackInSlot(i);
      result.add(outputItemHandler.extractItem(i, stack.getCount(), false));
    }

    return result;
  }

  private List<ItemStack> removeFluidsFromOutput() {
    List<ItemStack> result = new ArrayList<>();

    BlockEntity outputBlockEntity = worker.level().getBlockEntity(building.getOutputLocation());
    if (outputBlockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().isEmpty()) {
      return result;
    }
    IFluidHandler outputFluidHandler =
        outputBlockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get();
    for (int i = 0; i < outputFluidHandler.getTanks(); ++i) {
      FluidStack stack = outputFluidHandler.getFluidInTank(i);
      stack = outputFluidHandler.drain(stack, IFluidHandler.FluidAction.EXECUTE);

      int remaining = stack.getAmount();
      while (remaining > 0) {
        ItemStack container = new ItemStack(ModItems.courierTank.get());
        IFluidHandlerItem itemContainer = FluidUtil.getFluidHandler(container).resolve().get();
        int amount = itemContainer.fill(stack, IFluidHandler.FluidAction.EXECUTE);
        remaining -= amount;
        result.add(container);
      }
    }

    return result;
  }

  private boolean checkInputOkay() {
    StorageState inputState =
        checkTargetIsOkay(
            building.getInputLocation(), currentRecipeStorage.getCleanedInput().size(), true);
    if (inputState != StorageState.STORAGE_OKAY) {
      worker
          .getCitizenData()
          .triggerInteraction(
              new StandardInteraction(
                  Component.translatable(MACHINIST_HAS_NO_INPUT), ChatPriority.BLOCKING));
      return false;
    }

    return true;
  }

  private boolean checkOutputOkay() {
    StorageState outputState = checkTargetIsOkay(building.getOutputLocation(), 1, false);
    if (outputState != StorageState.STORAGE_OKAY) {
      worker
          .getCitizenData()
          .triggerInteraction(
              new StandardInteraction(
                  Component.translatable(MACHINIST_HAS_NO_OUTPUT), ChatPriority.BLOCKING));
      return false;
    }

    return true;
  }

  private enum StorageState {
    STORAGE_OKAY,
    NO_STORAGE_DEFINED,
    NO_BLOCK_ENTITY,
    NO_STORAGE,
    STORAGE_TOO_SMALL,
    STORAGE_HAS_CONTENTS,
  }

  private StorageState checkTargetIsOkay(BlockPos position, int requiredSize, boolean mustBeEmpty) {
    if (position.equals(BlockPos.ZERO)) {
      return StorageState.NO_STORAGE_DEFINED;
    }

    BlockEntity blockEntity;
    blockEntity = worker.level().getBlockEntity(position);
    if (blockEntity == null) {
      return StorageState.NO_BLOCK_ENTITY;
    }

    if (blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().isEmpty()) {
      return StorageState.NO_STORAGE;
    }

    IItemHandler itemHandler =
        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();

    if (itemHandler.getSlots() < requiredSize) {
      return StorageState.STORAGE_TOO_SMALL;
    }

    if (mustBeEmpty) {
      for (int i = 0; i < itemHandler.getSlots(); ++i) {
        if (!itemHandler.getStackInSlot(i).isEmpty()) {
          return StorageState.STORAGE_HAS_CONTENTS;
        }
      }
    }

    return StorageState.STORAGE_OKAY;
  }
}
