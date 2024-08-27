package com.wmaxlees.gregcolonies.core.data;

import com.wmaxlees.gregcolonies.core.data.tools.ToolHeadRecipes;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public class RecipeProvider {

    public static void recipeAddition(Consumer<FinishedRecipe> originalConsumer) {
        ToolHeadRecipes.init(originalConsumer);
    }
}
