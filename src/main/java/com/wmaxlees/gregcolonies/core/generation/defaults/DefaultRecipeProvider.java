package com.wmaxlees.gregcolonies.core.generation.defaults;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.wmaxlees.gregcolonies.api.blocks.ModBlocks;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import static com.ldtteam.structurize.items.ModItems.buildTool;

/**
 * Datagen for standard crafting recipes
 */
public class DefaultRecipeProvider extends RecipeProvider {

    public DefaultRecipeProvider(final PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull final Consumer<FinishedRecipe> consumer) {
        buildHutRecipes(consumer);
    }

    private void buildHutRecipes(@NotNull final Consumer<FinishedRecipe> consumer) {
        registerHutRecipe1(consumer, ModBlocks.blockHutToolmaker,
                ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:bronze_ingot")));
    }

    private static InventoryChangeTrigger.TriggerInstance hasAllOf(ItemLike... items) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(items);
    }

    private static InventoryChangeTrigger.TriggerInstance hasAllOf(ItemPredicate... predicates) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(predicates);
    }

    /**
     * Standard hut block recipe pattern, using build tool and one unique item
     * surrounded by planks.
     * 
     * @param consumer the recipe consumer.
     * @param output   the resulting hut block.
     * @param input    the unique input item.
     */
    private static void registerHutRecipe1(@NotNull final Consumer<FinishedRecipe> consumer,
            @NotNull final ItemLike output,
            @NotNull final ItemLike input) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output)
                .pattern("XTX")
                .pattern("XBX")
                .pattern("XXX")
                .define('X', ItemTags.PLANKS)
                .define('B', input)
                .define('T', buildTool.get())
                .unlockedBy("has_items", hasAllOf(buildTool.get(), input))
                .save(consumer);
    }
}
