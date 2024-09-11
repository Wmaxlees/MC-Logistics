package com.wmaxlees.gregcolonies.core.entity.ai.workers.storage;

import static com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState.*;
import static com.wmaxlees.gregcolonies.api.util.constant.ItemListConstants.*;
import static com.wmaxlees.gregcolonies.api.util.constant.TranslationConstant.*;
import static com.wmaxlees.gregcolonies.core.entity.ai.statemachine.states.AIWorkerState.*;

import com.google.common.reflect.TypeToken;
import com.minecolonies.api.colony.interactionhandling.ChatPriority;
import com.minecolonies.api.entity.ai.statemachine.AITarget;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.entity.citizen.VisibleCitizenStatus;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.core.colony.interactionhandling.StandardInteraction;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIInteract;
import com.wmaxlees.gregcolonies.api.crafting.FluidStorage;
import com.wmaxlees.gregcolonies.api.items.ModItems;
import com.wmaxlees.gregcolonies.api.util.constant.translation.RequestSystemTranslatableConstants;
import com.wmaxlees.gregcolonies.core.colony.buildings.modules.FluidListModule;
import com.wmaxlees.gregcolonies.core.colony.buildings.modules.InventoryUserModule;
import com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings.BuildingFluidWarehouse;
import com.wmaxlees.gregcolonies.core.colony.jobs.JobFluidWarehouseManager;
import com.wmaxlees.gregcolonies.core.colony.requestable.CourierTanksRequestable;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class EntityAIWorkFluidWarehouseManager
    extends AbstractEntityAIInteract<JobFluidWarehouseManager, BuildingFluidWarehouse> {
  /**
   * Sets up some important skeleton stuff for every ai.
   *
   * @param job the job class
   */
  public EntityAIWorkFluidWarehouseManager(@NotNull JobFluidWarehouseManager job) {
    super(job);

    super.registerTargets(
        new AITarget(IDLE, START_WORKING, STANDARD_DELAY),
        new AITarget(START_WORKING, this::startWorking, 60),
        new AITarget(MOVE_TO_TANK_TO_EMPTY, this::moveToTankToEmpty, 60),
        new AITarget(EMPTY_TANK, this::emptyTank, 60));
  }

  @Override
  public Class<BuildingFluidWarehouse> getExpectedBuildingClass() {
    return BuildingFluidWarehouse.class;
  }

  private IAIState startWorking() {
    if (walkToBuilding()) {
      return getState();
    }

    final FluidListModule fluidListModule =
        building.getModuleMatching(
            FluidListModule.class, m -> m.getId().equals(ITEM_LIST_COURIER_TANKS));
    worker.getCitizenData().setVisibleStatus(VisibleCitizenStatus.WORKING);

    final int courierTanksInBuilding =
        InventoryUtils.getCountFromBuilding(
            building, stack -> stack.getItem() == ModItems.courierTank);
    final int courierTanksInInv =
        InventoryUtils.getItemCountInItemHandler(
            (worker.getInventoryCitizen()), stack -> stack.getItem() == ModItems.courierTank);

    if (courierTanksInInv + courierTanksInBuilding <= 0) {
      requestFluids();
    }

    if (courierTanksInBuilding > 0 && courierTanksInInv == 0) {
      needsCurrently = new Tuple<>(stack -> stack.getItem() == ModItems.courierTank, 1);
      return GATHERING_REQUIRED_MATERIALS;
    }

    if (courierTanksInInv > 0) {
      return MOVE_TO_TANK_TO_EMPTY;
    }

    return IDLE;
  }

  private void requestFluids() {
    if (!building.hasWorkerOpenRequestsOfType(
            worker.getCitizenData().getId(), TypeToken.of(CourierTanksRequestable.class))
        && !building.hasWorkerOpenRequestsFiltered(
            worker.getCitizenData().getId(),
            req ->
                req.getShortDisplayString()
                    .getSiblings()
                    .contains(
                        Component.translatable(
                            RequestSystemTranslatableConstants.REQUEST_TYPE_COURIER_TANKS)))) {
      final List<FluidStorage> allowedFluids =
          building
              .getModuleMatching(
                  FluidListModule.class, m -> m.getId().equals(ITEM_LIST_COURIER_TANKS))
              .getList();

      final List<Fluid> requests =
          ForgeRegistries.FLUIDS.getValues().stream()
              .filter(
                  fluid ->
                      !allowedFluids.contains(new FluidStorage(new FluidStack(fluid, 1000), 1000)))
              .toList();

      if (requests.isEmpty()) {
        if (worker.getCitizenData() != null) {
          worker
              .getCitizenData()
              .triggerInteraction(
                  new StandardInteraction(
                      Component.translatable(FLUID_WAREHOUSE_MANAGER_NO_FLUIDS),
                      ChatPriority.BLOCKING));
        }
      } else {
        worker
            .getCitizenData()
            .createRequestAsync(new CourierTanksRequestable(Integer.MAX_VALUE, requests, false));
      }
    }
  }

  private IAIState emptyTank() {
    final int courierTanksInInv =
        InventoryUtils.getItemCountInItemHandler(
            (worker.getInventoryCitizen()), stack -> stack.getItem() == ModItems.courierTank);

    if (courierTanksInInv <= 0) {
      return START_WORKING;
    }

    if (FluidUtil.getFluidContained(
            worker.getInventoryCitizen().getHeldItem(InteractionHand.MAIN_HAND))
        .isEmpty()) {
      InventoryUtils.removeStackFromItemHandler(
          worker.getItemHandlerCitizen(),
          worker.getInventoryCitizen().getHeldItem(InteractionHand.MAIN_HAND),
          1);
      return START_WORKING;
    }

    BlockPos tankTarget = walkTo;
    BlockEntity tank = worker.level().getBlockEntity(tankTarget);
    if (tank.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().isEmpty()) {
      return START_WORKING;
    }

    IFluidHandler tankHandler = tank.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get();
    FluidStack tankFluid = tankHandler.getFluidInTank(0);

    IFluidHandler heldHandler =
        FluidUtil.getFluidHandler(
                worker.getInventoryCitizen().getHeldItem(InteractionHand.MAIN_HAND))
            .resolve()
            .get();

    if (!tankFluid.getFluid().isSame(heldHandler.getFluidInTank(0).getFluid())
        && !tankFluid.isEmpty()) {
      return START_WORKING;
    }

    FluidActionResult result =
        FluidUtil.tryEmptyContainer(
            worker.getInventoryCitizen().getHeldItem(InteractionHand.MAIN_HAND),
            tankHandler,
            Integer.MAX_VALUE,
            null,
            true);

    if (result
        .getResult()
        .getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
        .resolve()
        .isEmpty()) {
      InventoryUtils.removeStackFromItemHandler(
          worker.getItemHandlerCitizen(),
          worker.getInventoryCitizen().getHeldItem(InteractionHand.MAIN_HAND),
          1);
      return START_WORKING;
    }

    IFluidHandler resultHandler =
        result.getResult().getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().get();
    heldHandler.getFluidInTank(0).setAmount(resultHandler.getFluidInTank(0).getAmount());

    if (heldHandler.getFluidInTank(0).getAmount() > 0) {
      return MOVE_TO_TANK_TO_EMPTY;
    } else {
      InventoryUtils.removeStackFromItemHandler(
          worker.getItemHandlerCitizen(),
          worker.getInventoryCitizen().getHeldItem(InteractionHand.MAIN_HAND),
          1);
      return START_WORKING;
    }
  }

  private IAIState moveToTankToEmpty() {
    final int courierTanksInInv =
        InventoryUtils.getItemCountInItemHandler(
            (worker.getInventoryCitizen()), stack -> stack.getItem() == ModItems.courierTank);

    if (courierTanksInInv <= 0) {
      return START_WORKING;
    }

    int targetSlot = -1;
    for (int i = 0; i < worker.getInventoryCitizen().getSlots(); ++i) {
      ItemStack stack = worker.getInventoryCitizen().getStackInSlot(i);
      if (stack.is(ModItems.courierTank)) {
        targetSlot = i;
        worker.getInventoryCitizen().setHeldItem(InteractionHand.MAIN_HAND, i);
        break;
      }
    }

    if (targetSlot == -1) {
      return START_WORKING;
    }

    InventoryUserModule inventoryUserModule =
        building.getFirstModuleOccurance(InventoryUserModule.class);

    if (FluidUtil.getFluidContained(
            worker.getInventoryCitizen().getHeldItem(InteractionHand.MAIN_HAND))
        .isEmpty()) {
      InventoryUtils.removeStackFromItemHandler(
          worker.getItemHandlerCitizen(),
          worker.getInventoryCitizen().getHeldItem(InteractionHand.MAIN_HAND),
          1);
      return START_WORKING;
    }

    Fluid heldFluid =
        FluidUtil.getFluidContained(
                worker.getInventoryCitizen().getHeldItem(InteractionHand.MAIN_HAND))
            .get()
            .getFluid();

    for (BlockPos tankPos : inventoryUserModule.getTanks()) {
      BlockEntity tank = worker.level().getBlockEntity(tankPos);
      if (tank == null) {
        continue;
      }

      if (tank.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().isEmpty()) {
        continue;
      }

      IFluidHandler handler = tank.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get();
      FluidStack tankFluid = handler.getFluidInTank(0);

      if (!tankFluid.getFluid().isSame(heldFluid) && !tankFluid.isEmpty()) {
        continue;
      }

      setWalkTo(tankPos);
      return EMPTY_TANK;
    }

    worker
        .getCitizenData()
        .triggerInteraction(
            new StandardInteraction(
                Component.translatable(FLUID_WAREHOUSE_MANAGER_NO_AVAILABLE_TANKS),
                ChatPriority.BLOCKING));

    return MOVE_TO_TANK_TO_EMPTY;
  }
}
