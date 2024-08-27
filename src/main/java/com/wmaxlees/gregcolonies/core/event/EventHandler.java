package com.wmaxlees.gregcolonies.core.event;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {
    @SubscribeEvent
    public static void onCommandsRegister(final RegisterCommandsEvent event) {
        ListBuildingsCommand.register(event.getDispatcher());
        ListJobsCommand.register(event.getDispatcher());
    }
}
