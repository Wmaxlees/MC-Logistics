package com.wmaxlees.gregcolonies.apiimp.initializer;

import com.mojang.logging.LogUtils;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.data.GregColoniesModels;
import com.wmaxlees.gregcolonies.data.GregColoniesRecipes;
import com.wmaxlees.gregcolonies.data.pack.GregColoniesDynamicDataPack;
import com.wmaxlees.gregcolonies.data.pack.GregColoniesDynamicResourcePack;
import com.wmaxlees.gregcolonies.data.pack.GregColoniesPackSource;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Constants.MOD_ID)
public class GregColoniesModResources {
  // Directly reference a slf4j logger
  private static final Logger LOGGER = LogUtils.getLogger();

  @SubscribeEvent
  public static void registerPackFinders(AddPackFindersEvent event) {
    LOGGER.info("Registering GregColonies pack finders.");
    if (event.getPackType() == PackType.CLIENT_RESOURCES) {
      LOGGER.info("Client side GregColonies resources loading.");
      // Clear old data
      GregColoniesDynamicResourcePack.clearClient();

      long startTime = System.currentTimeMillis();
      GregColoniesModels.modelAddition(GregColoniesDynamicResourcePack::addItemModel);
      LOGGER.info(
          "GregColonies Resource loading took {}ms", System.currentTimeMillis() - startTime);

      event.addRepositorySource(
          new GregColoniesPackSource(
              Constants.MOD_ID + "dynamic_assets",
              event.getPackType(),
              Pack.Position.BOTTOM,
              GregColoniesDynamicResourcePack::new));
    } else if (event.getPackType() == PackType.SERVER_DATA) {
      // Clear old data
      GregColoniesDynamicDataPack.clearServer();

      // Register recipes & unification data again
      long startTime = System.currentTimeMillis();
      GregColoniesRecipes.recipeAddition(GregColoniesDynamicDataPack::addRecipe);
      GregColoniesRecipes.toolmakerRecipeAddition(GregColoniesDynamicDataPack::addCustomRecipe);
      LOGGER.info("GregColonies Data loading took {}ms", System.currentTimeMillis() - startTime);

      event.addRepositorySource(
          new GregColoniesPackSource(
              Constants.MOD_ID + ":dynamic_data",
              event.getPackType(),
              Pack.Position.BOTTOM,
              GregColoniesDynamicDataPack::new));
    }
  }
}
