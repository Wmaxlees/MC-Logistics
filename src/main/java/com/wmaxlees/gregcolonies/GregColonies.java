package com.wmaxlees.gregcolonies;

import com.wmaxlees.gregcolonies.api.creativetab.ModCreativeTabs;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.apiimp.initializer.*;
import com.wmaxlees.gregcolonies.core.Network;
import com.wmaxlees.gregcolonies.core.colony.requestsystem.init.RequestSystemInitializer;
import com.wmaxlees.gregcolonies.core.colony.requestsystem.init.StandardFactoryControllerInitializer;
import com.wmaxlees.gregcolonies.core.event.EventHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.NewRegistryEvent;
import org.jetbrains.annotations.NotNull;

@Mod(GregColonies.MODID)
public class GregColonies {
  // Define mod id in a common place for everything to reference
  public static final String MODID = Constants.MOD_ID;

  public GregColonies() {
    TileEntityInitializer.BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    GregColoniesModBuildingsInitializer.DEFERRED_REGISTER.register(
        FMLJavaModLoadingContext.get().getModEventBus());
    GregColoniesModJobsInitializer.DEFERRED_REGISTER.register(
        FMLJavaModLoadingContext.get().getModEventBus());
    GregColoniesModContainerInitializer.CONTAINERS.register(
        FMLJavaModLoadingContext.get().getModEventBus());

    ModCreativeTabs.TAB_REG.register(FMLJavaModLoadingContext.get().getModEventBus());

    Mod.EventBusSubscriber.Bus.FORGE.bus().get().register(EventHandler.class);

    Mod.EventBusSubscriber.Bus.FORGE.bus().get().register(this.getClass());
    Mod.EventBusSubscriber.Bus.MOD.bus().get().register(this.getClass());
  }

  @SubscribeEvent
  public static void preInit(@NotNull final FMLCommonSetupEvent event) {
    Network.getNetwork().registerCommonMessages();

    StandardFactoryControllerInitializer.onPreInit();
  }

  // You can use SubscribeEvent and let the Event Bus discover methods to call
  @SubscribeEvent
  public void onServerStarting(ServerStartingEvent event) {}

  @SubscribeEvent
  public static void registerNewRegistries(final NewRegistryEvent event) {}

  // You can use EventBusSubscriber to automatically register all static methods
  // in the class annotated with @SubscribeEvent
  @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
  public static class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {}
  }

  @SubscribeEvent
  public static void onLoadComplete(final FMLLoadCompleteEvent event) {
    RequestSystemInitializer.onPostInit();
  }
}
