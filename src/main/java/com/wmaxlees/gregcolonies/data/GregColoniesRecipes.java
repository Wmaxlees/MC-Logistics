package com.wmaxlees.gregcolonies.data;

import com.wmaxlees.gregcolonies.data.recipe.generated.ToolHeadRecipeHandler;
import com.wmaxlees.gregcolonies.data.recipe.generated.ToolRecipeHandler;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;

public class GregColoniesRecipes {
  /*
   * Called on resource reload in-game.
   *
   * These methods are meant for recipes that cannot be reasonably changed by a Datapack,
   * such as "X Ingot -> 2 X Rods" types of recipes, that follow a pattern for many recipes.
   *
   * This should also be used for recipes that need
   * to respond to a config option in ConfigHolder.
   */
  public static void recipeAddition(Consumer<FinishedRecipe> originalConsumer) {}

  public static void toolmakerRecipeAddition(Consumer<FinishedRecipe> originalConsumer) {
    ToolHeadRecipeHandler.init(originalConsumer);
    ToolRecipeHandler.init(originalConsumer);
  }
}
