package com.wmaxlees.gregcolonies.apiimp.initializer;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.apiimp.initializer.ModBuildingsInitializer;
import com.minecolonies.core.colony.buildings.views.EmptyView;
import com.mojang.logging.LogUtils;
import com.wmaxlees.gregcolonies.api.blocks.ModBlocks;
import com.wmaxlees.gregcolonies.api.colony.buildings.ModBuildings;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings.BuildingToolmaker;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;

import static com.minecolonies.core.colony.buildings.modules.BuildingModules.*;
import static com.wmaxlees.gregcolonies.core.colony.buildings.modules.BuildingModules.*;

import org.slf4j.Logger;

public final class GregColoniesModBuildingsInitializer {
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public void RegisterBuildings() {
        LOGGER.info("Initializing GregColonies buildings.");

        ModBuildings.toolmaker = ModBuildingsInitializer.DEFERRED_REGISTER.register(ModBuildings.TOOLMAKER_ID,
                () -> new BuildingEntry.Builder()
                        .setBuildingBlock(ModBlocks.blockHutToolmaker)
                        .setBuildingProducer(BuildingToolmaker::new)
                        .setBuildingViewProducer(() -> EmptyView::new)
                        .setRegistryName(new ResourceLocation("minecolonies", ModBuildings.TOOLMAKER_ID))
                        .addBuildingModuleProducer(TOOLMAKER_WORK)
                        .addBuildingModuleProducer(TOOLMAKER_WORKORDERS)
                        .addBuildingModuleProducer(TOOLMAKER_TOOLS)
                        .addBuildingModuleProducer(TOOLMAKER_CRAFT)
                        .addBuildingModuleProducer(MIN_STOCK)
                        .createBuildingEntry());
    }
}
