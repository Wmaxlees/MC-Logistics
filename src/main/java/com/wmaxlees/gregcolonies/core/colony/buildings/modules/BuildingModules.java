package com.wmaxlees.gregcolonies.core.colony.buildings.modules;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.entity.citizen.Skill;
import com.minecolonies.core.colony.buildings.modules.CraftingWorkerBuildingModule;
import com.minecolonies.core.colony.buildings.moduleviews.ToolModuleView;
import com.minecolonies.core.colony.buildings.moduleviews.WorkerBuildingModuleView;
import com.wmaxlees.gregcolonies.api.colony.jobs.ModJobs;
import com.wmaxlees.gregcolonies.api.items.ModItems;
import com.wmaxlees.gregcolonies.core.colony.buildings.moduleviews.PlayerDefinedCraftingModuleView;
import com.wmaxlees.gregcolonies.core.colony.buildings.moduleviews.SearchableCraftingModuleView;
import com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings.BuildingToolPartSmith;
import com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings.BuildingToolmaker;

public class BuildingModules {

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
}
