package com.wmaxlees.gregcolonies.core.colony.buildings.modules;

import com.minecolonies.api.entity.citizen.Skill;
import com.minecolonies.core.colony.buildings.modules.CraftingWorkerBuildingModule;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;
import com.minecolonies.core.colony.buildings.moduleviews.WorkerBuildingModuleView;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.wmaxlees.gregcolonies.api.colony.jobs.ModJobs;
import com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings.BuildingToolmaker;
import com.wmaxlees.gregcolonies.core.colony.buildings.modules.ToolmakerToolsModule;
import com.wmaxlees.gregcolonies.core.colony.buildings.moduleviews.ToolmakerToolsModuleView;
import com.wmaxlees.gregcolonies.core.colony.buildings.modules.ToolmakerWorkordersModule;
import com.wmaxlees.gregcolonies.core.colony.buildings.moduleviews.ToolmakerWorkordersModuleView;

public class BuildingModules {

        /**
         * Craftmanship
         */
        public static final BuildingEntry.ModuleProducer<CraftingWorkerBuildingModule, WorkerBuildingModuleView> TOOLMAKER_WORK = new BuildingEntry.ModuleProducer<>(
                        "toolmaker_work",
                        () -> new CraftingWorkerBuildingModule(ModJobs.toolmaker.get(), Skill.Strength, Skill.Focus,
                                        false,
                                        (b) -> 1, Skill.Strength, Skill.Focus),
                        () -> WorkerBuildingModuleView::new);
        public static final BuildingEntry.ModuleProducer<BuildingToolmaker.CraftingModule, CraftingModuleView> TOOLMAKER_CRAFT = new BuildingEntry.ModuleProducer<>(
                        "toolmaker_craft", () -> new BuildingToolmaker.CraftingModule(ModJobs.toolmaker.get()),
                        () -> CraftingModuleView::new);
        public static final BuildingEntry.ModuleProducer<ToolmakerWorkordersModule, ToolmakerWorkordersModuleView> TOOLMAKER_WORKORDERS = new BuildingEntry.ModuleProducer<>(
                        "toolmaker_workorders", ToolmakerWorkordersModule::new,
                        () -> ToolmakerWorkordersModuleView::new);
        public static final BuildingEntry.ModuleProducer<ToolmakerToolsModule, ToolmakerToolsModuleView> TOOLMAKER_TOOLS = new BuildingEntry.ModuleProducer<>(
                        "toolmaker_tools", ToolmakerToolsModule::new, () -> ToolmakerToolsModuleView::new);

}
