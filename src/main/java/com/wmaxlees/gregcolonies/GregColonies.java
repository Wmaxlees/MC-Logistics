package com.wmaxlees.gregcolonies;

import com.mojang.logging.LogUtils;
import com.wmaxlees.gregcolonies.api.creativetab.ModCreativeTabs;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.apiimp.initializer.*;
import com.wmaxlees.gregcolonies.core.Network;
import com.wmaxlees.gregcolonies.core.event.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.NewRegistryEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(GregColonies.MODID)
public class GregColonies {
  // Define mod id in a common place for everything to reference
  public static final String MODID = Constants.MOD_ID;
  // Directly reference a slf4j logger
  private static final Logger LOGGER = LogUtils.getLogger();

  public GregColonies() {
    TileEntityInitializer.BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    GregColoniesModContainerInitializer.CONTAINERS.register(
        FMLJavaModLoadingContext.get().getModEventBus());
    GregColoniesModJobsInitializer jobsInitializer = new GregColoniesModJobsInitializer();
    jobsInitializer.RegisterJobs();
    GregColoniesModBuildingsInitializer buildingsInitializer =
        new GregColoniesModBuildingsInitializer();
    buildingsInitializer.RegisterBuildings();

    ModCreativeTabs.TAB_REG.register(FMLJavaModLoadingContext.get().getModEventBus());

    Mod.EventBusSubscriber.Bus.FORGE.bus().get().register(EventHandler.class);

    Mod.EventBusSubscriber.Bus.FORGE.bus().get().register(this.getClass());
    Mod.EventBusSubscriber.Bus.MOD.bus().get().register(this.getClass());
  }

  @SubscribeEvent
  public static void preInit(@NotNull final FMLCommonSetupEvent event) {
    Network.getNetwork().registerCommonMessages();
  }

  // You can use SubscribeEvent and let the Event Bus discover methods to call
  @SubscribeEvent
  public void onServerStarting(ServerStartingEvent event) {
    // Do something when the server starts
    LOGGER.info("HELLO from server starting");
  }

  @SubscribeEvent
  public static void registerNewRegistries(final NewRegistryEvent event) {
    LOGGER.info("Registering!!");
  }

  // You can use EventBusSubscriber to automatically register all static methods
  // in the class annotated with @SubscribeEvent
  @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
  public static class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
      // Some client setup code
      LOGGER.info("HELLO FROM CLIENT SETUP");
      LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
  }
}
