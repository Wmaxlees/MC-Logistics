package com.wmaxlees.gregcolonies.apiimp.initializer;

import com.mojang.logging.LogUtils;
import com.wmaxlees.gregcolonies.api.items.ModItems;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.core.items.ItemToolHead;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GregColoniesModItemsInitializer {
  // Directly reference a slf4j logger
  private static final Logger LOGGER = LogUtils.getLogger();

  @SubscribeEvent
  public static void registerColors(RegisterColorHandlersEvent.Item event) {
    LOGGER.info("Registering GregColony colors");

    ModItems.TOOL_HEAD_ITEMS
        .rowMap()
        .forEach(
            (material, row) -> {
              row.forEach(
                  (toolType, item) -> {
                    ItemToolHead toolHead = (ItemToolHead) item;
                    if (toolHead == null) {
                      return;
                    }
                    event.register(new ItemToolHead.ToolHeadItemColor(), item);
                  });
            });
  }

  @SubscribeEvent
  public static void registerItems(RegisterEvent event) {
    if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
      LOGGER.info("Registering GregColony items");
      IForgeRegistry<Item> registry = event.getForgeRegistry();

      ModItems.TOOL_HEAD_ITEMS
          .rowMap()
          .forEach(
              (material, row) -> {
                row.forEach(
                    (toolType, item) -> {
                      LOGGER.info(
                          "Attempting to register item: "
                              + material.getName()
                              + " "
                              + toolType.name);
                      LOGGER.info("itemProvider: " + item);
                      if (item != null) {
                        registry.register(material.getName() + "_" + toolType.name + "_head", item);
                      }
                    });
              });
    }
  }
}
