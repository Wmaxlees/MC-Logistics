package com.wmaxlees.gregcolonies.apiimp.initializer;

import com.mojang.logging.LogUtils;
import com.wmaxlees.gregcolonies.api.inventory.ModContainers;
import com.wmaxlees.gregcolonies.api.inventory.container.ContainerCraftingPlayerDefined;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.core.client.gui.containers.WindowPlayerDefinedCrafting;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GregColoniesModContainerInitializer {
  // Directly reference a slf4j logger
  private static final Logger LOGGER = LogUtils.getLogger();

  public static final DeferredRegister<MenuType<?>> CONTAINERS =
      DeferredRegister.create(ForgeRegistries.MENU_TYPES, Constants.MOD_ID);

  static {
    ModContainers.craftingPlayerDefined =
        CONTAINERS.register(
            "crafting_player_defined",
            () -> IForgeMenuType.create(ContainerCraftingPlayerDefined::fromFriendlyByteBuf));
  }

  @SubscribeEvent
  public static void clientSetup(final FMLClientSetupEvent event) {
    MenuScreens.register(
        ModContainers.craftingPlayerDefined.get(), WindowPlayerDefinedCrafting::new);
  }
}
