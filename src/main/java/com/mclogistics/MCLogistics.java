package com.mclogistics;

import com.mclogistics.api.colony.buildings.ModBuildings;
import com.mclogistics.api.colony.jobs.ModJobs;
import com.mclogistics.api.creativetab.ModCreativeTabs;
import com.mclogistics.api.inventory.ModContainers;
import com.mclogistics.api.items.ModItems;
import com.mclogistics.api.util.constant.Constants;
import com.mclogistics.core.Network;
import com.mclogistics.core.colony.requestsystem.init.RequestSystemInitializer;
import com.mclogistics.core.colony.requestsystem.init.StandardFactoryControllerInitializer;
import com.mclogistics.core.event.EventHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

@Mod(MCLogistics.MODID)
public class MCLogistics {
  // Define mod id in a common place for everything to reference
  public static final String MODID = Constants.MOD_ID;

  public MCLogistics() {
    ModItems.DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    ModBuildings.DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    ModJobs.DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    ModContainers.DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());

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

  @SubscribeEvent
  public static void onLoadComplete(final FMLLoadCompleteEvent event) {
    RequestSystemInitializer.onPostInit();
  }
}
