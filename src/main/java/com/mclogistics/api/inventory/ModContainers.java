package com.mclogistics.api.inventory;

import com.mclogistics.api.inventory.container.ContainerCraftingPlayerDefined;
import com.mclogistics.api.util.constant.Constants;
import com.mclogistics.core.client.gui.containers.WindowPlayerDefinedCrafting;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModContainers {
  public static RegistryObject<MenuType<ContainerCraftingPlayerDefined>> craftingPlayerDefined;

  public static final DeferredRegister<MenuType<?>> DEFERRED_REGISTER =
      DeferredRegister.create(ForgeRegistries.MENU_TYPES, Constants.MOD_ID);

  static {
    ModContainers.craftingPlayerDefined =
        DEFERRED_REGISTER.register(
            "crafting_player_defined",
            () -> IForgeMenuType.create(ContainerCraftingPlayerDefined::fromFriendlyByteBuf));
  }

  @SubscribeEvent
  public static void clientSetup(final FMLClientSetupEvent event) {
    MenuScreens.register(
        ModContainers.craftingPlayerDefined.get(), WindowPlayerDefinedCrafting::new);
  }
}
