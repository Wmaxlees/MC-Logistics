package com.wmaxlees.gregcolonies.core.generation.defaults;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.wmaxlees.gregcolonies.core.generation.defaults.workers.DefaultToolmakerCraftingProvider;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GatherDataHandler {
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * This method is for adding datagenerators. this does not run during normal
     * client operations, only during building.
     *
     * @param event event sent when you run the "runData" gradle task
     */
    @SubscribeEvent
    public static void dataGeneratorSetup(final GatherDataEvent event) {
        LOGGER.info("Adding GregColonies generated data.");
        final DataGenerator generator = event.getGenerator();

        generator.addProvider(event.includeServer(), new DefaultRecipeProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new DefaultToolmakerCraftingProvider(generator.getPackOutput()));
    }
}
