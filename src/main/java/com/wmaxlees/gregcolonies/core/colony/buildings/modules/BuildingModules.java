package com.wmaxlees.gregcolonies.core.colony.buildings.modules;

import static com.wmaxlees.gregcolonies.api.util.constant.ItemListConstants.*;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.entity.citizen.Skill;
import com.minecolonies.core.colony.buildings.modules.CraftingWorkerBuildingModule;
import com.minecolonies.core.colony.buildings.moduleviews.ToolModuleView;
import com.minecolonies.core.colony.buildings.moduleviews.WorkerBuildingModuleView;
import com.wmaxlees.gregcolonies.api.colony.jobs.ModJobs;
import com.wmaxlees.gregcolonies.api.crafting.FluidStorage;
import com.wmaxlees.gregcolonies.api.items.ModItems;
import com.wmaxlees.gregcolonies.api.util.constant.translation.RequestSystemTranslatableConstants;
import com.wmaxlees.gregcolonies.core.colony.buildings.moduleviews.FluidListModuleView;
import com.wmaxlees.gregcolonies.core.colony.buildings.moduleviews.InventoryUserModuleView;
import com.wmaxlees.gregcolonies.core.colony.buildings.moduleviews.PlayerDefinedCraftingModuleView;
import com.wmaxlees.gregcolonies.core.colony.buildings.moduleviews.SearchableCraftingModuleView;
import com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings.BuildingToolPartSmith;
import com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings.BuildingToolmaker;
import java.util.HashSet;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class BuildingModules {

  /** Global */
  public static final BuildingEntry.ModuleProducer<InventoryUserModule, InventoryUserModuleView>
      INVENTORY_USER =
          new BuildingEntry.ModuleProducer<>(
              "inventory_user", InventoryUserModule::new, () -> InventoryUserModuleView::new);

  public static final BuildingEntry.ModuleProducer<IBuildingModule, ToolModuleView>
      TANK_SELECTOR_TOOL =
          new BuildingEntry.ModuleProducer<>(
              "fluid_inventory_tool",
              null,
              () -> () -> new ToolModuleView(ModItems.scepterTankInventory));

  /** Craftmanship */
  public static final BuildingEntry.ModuleProducer<
          CraftingWorkerBuildingModule, WorkerBuildingModuleView>
      TOOLMAKER_WORK =
          new BuildingEntry.ModuleProducer<>(
              "toolmaker_work",
              () ->
                  new CraftingWorkerBuildingModule(
                      ModJobs.toolmaker.get(),
                      Skill.Strength,
                      Skill.Focus,
                      false,
                      (b) -> 1,
                      Skill.Strength,
                      Skill.Focus),
              () -> WorkerBuildingModuleView::new);

  public static final BuildingEntry.ModuleProducer<
          BuildingToolmaker.CraftingModule, SearchableCraftingModuleView>
      TOOLMAKER_CRAFT =
          new BuildingEntry.ModuleProducer<>(
              "toolmaker_craft",
              () -> new BuildingToolmaker.CraftingModule(ModJobs.toolmaker.get()),
              () -> SearchableCraftingModuleView::new);

  public static final BuildingEntry.ModuleProducer<
          CraftingWorkerBuildingModule, WorkerBuildingModuleView>
      TOOLPARTSMITH_WORK =
          new BuildingEntry.ModuleProducer<>(
              "toolpartsmith_work",
              () ->
                  new CraftingWorkerBuildingModule(
                      ModJobs.toolpartsmith.get(),
                      Skill.Strength,
                      Skill.Focus,
                      false,
                      (b) -> 1,
                      Skill.Strength,
                      Skill.Focus),
              () -> WorkerBuildingModuleView::new);
  public static final BuildingEntry.ModuleProducer<
          BuildingToolPartSmith.CraftingModule, SearchableCraftingModuleView>
      TOOLPARTSMITH_CRAFT =
          new BuildingEntry.ModuleProducer<>(
              "toolpartsmith_craft",
              () -> new BuildingToolPartSmith.CraftingModule(ModJobs.toolpartsmith.get()),
              () -> SearchableCraftingModuleView::new);

  public static final BuildingEntry.ModuleProducer<
          CraftingWorkerBuildingModule, WorkerBuildingModuleView>
      MACHINIST_WORK =
          new BuildingEntry.ModuleProducer<>(
              "machinist_work",
              () ->
                  new CraftingWorkerBuildingModule(
                      ModJobs.machinist.get(),
                      Skill.Strength,
                      Skill.Focus,
                      false,
                      (b) -> 1,
                      Skill.Strength,
                      Skill.Focus),
              () -> WorkerBuildingModuleView::new);
  public static final BuildingEntry.ModuleProducer<
          PlayerDefinedCraftingModule, PlayerDefinedCraftingModuleView>
      MACHINIST_CRAFT =
          new BuildingEntry.ModuleProducer<>(
              "machinist_craft",
              () -> new PlayerDefinedCraftingModule(ModJobs.machinist.get()),
              () -> PlayerDefinedCraftingModuleView::new);
  public static final BuildingEntry.ModuleProducer<IBuildingModule, ToolModuleView>
      MACHINIST_INPUT_TOOL =
          new BuildingEntry.ModuleProducer<>(
              "machinist_input_tool",
              null,
              () -> () -> new ToolModuleView(ModItems.scepterMachinistInput));
  public static final BuildingEntry.ModuleProducer<IBuildingModule, ToolModuleView>
      MACHINIST_OUTPUT_TOOL =
          new BuildingEntry.ModuleProducer<>(
              "machinist_output_tool",
              null,
              () -> () -> new ToolModuleView(ModItems.scepterMachinistOutput));

  /** Storage */
  public static final BuildingEntry.ModuleProducer<IBuildingModule, WorkerBuildingModuleView>
      FLUID_WAREHOUSE_WORK =
          new BuildingEntry.ModuleProducer<>(
              "fluid_warehouse_work",
              () ->
                  new CraftingWorkerBuildingModule(
                      ModJobs.fluidwarehousemanager.get(),
                      Skill.Strength,
                      Skill.Focus,
                      false,
                      (b) -> 1,
                      Skill.Strength,
                      Skill.Focus),
              () -> WorkerBuildingModuleView::new);

  public static final BuildingEntry.ModuleProducer<FluidListModule, FluidListModuleView>
      FLUID_LIST_COURIER_TANKS =
          new BuildingEntry.ModuleProducer<>(
              "fluidlist_courier_tank",
              () -> new FluidListModule(ITEM_LIST_COURIER_TANKS),
              () ->
                  () ->
                      new FluidListModuleView(
                          ITEM_LIST_COURIER_TANKS,
                          RequestSystemTranslatableConstants.REQUEST_TYPE_COURIER_TANKS,
                          true,
                          (buildingView) ->
                              new HashSet<>(
                                  ForgeRegistries.FLUIDS.getValues().stream()
                                      .map(
                                          fluid ->
                                              new FluidStorage(new FluidStack(fluid, 1000), 1000))
                                      .toList())));
}
