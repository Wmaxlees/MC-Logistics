package com.wmaxlees.gregcolonies.apiimp.initializer;

import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.api.util.constant.Logger;
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

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Constants.MOD_ID)
public class GregColoniesModResources {
  @SubscribeEvent
  public static void registerPackFinders(AddPackFindersEvent event) {
    if (event.getPackType() == PackType.CLIENT_RESOURCES) {
      // Clear old data
      GregColoniesDynamicResourcePack.clearClient();

      long startTime = System.currentTimeMillis();
      GregColoniesModels.modelAddition(GregColoniesDynamicResourcePack::addItemModel);
      Logger.InfoLog(
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
      Logger.InfoLog("GregColonies Data loading took {}ms", System.currentTimeMillis() - startTime);

      event.addRepositorySource(
          new GregColoniesPackSource(
              Constants.MOD_ID + ":dynamic_data",
              event.getPackType(),
              Pack.Position.BOTTOM,
              GregColoniesDynamicDataPack::new));
    }
  }
}
