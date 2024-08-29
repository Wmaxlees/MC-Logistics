package com.wmaxlees.gregcolonies.data;

import com.google.gson.JsonElement;
import com.wmaxlees.gregcolonies.data.model.generated.ToolHeadModelHandler;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;

public class GregColoniesModels {
  /*
   * Called on resource reload in-game.
   *
   * These methods are meant for recipes that cannot be reasonably changed by a Datapack,
   * such as "X Ingot -> 2 X Rods" types of recipes, that follow a pattern for many recipes.
   *
   * This should also be used for recipes that need
   * to respond to a config option in ConfigHolder.
   */
  public static void modelAddition(BiConsumer<ResourceLocation, JsonElement> originalConsumer) {
    ToolHeadModelHandler.init(originalConsumer);
  }
}
