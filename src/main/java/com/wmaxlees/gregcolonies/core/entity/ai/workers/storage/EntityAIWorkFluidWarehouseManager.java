package com.wmaxlees.gregcolonies.core.entity.ai.workers.storage;

import static com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState.*;
import static com.wmaxlees.gregcolonies.api.util.constant.ItemListConstants.*;
import static com.wmaxlees.gregcolonies.api.util.constant.TranslationConstant.*;

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
import com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings.BuildingFluidWarehouse;
import com.wmaxlees.gregcolonies.core.colony.jobs.JobFluidWarehouseManager;
import com.wmaxlees.gregcolonies.core.colony.requestable.CourierTanksRequestable;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
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
        new AITarget(START_WORKING, this::startWorking, 60));
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

    return IDLE;
  }

  public void requestFluids() {
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
}
