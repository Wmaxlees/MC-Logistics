package com.wmaxlees.gregcolonies.core.generation.defaults.workers;

import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.core.generation.CustomRecipeProvider;
import com.wmaxlees.gregcolonies.api.colony.jobs.ModJobs;
import com.wmaxlees.gregcolonies.core.generation.defaults.Materials;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import static com.minecolonies.api.util.constant.BuildingConstants.MODULE_CUSTOM;

public class DefaultToolmakerCraftingProvider extends CustomRecipeProvider {
    public static final String TOOLMAKER = ModJobs.TOOLMAKER_ID.getPath();

    public DefaultToolmakerCraftingProvider(@NotNull final PackOutput packOutput) {
        super(packOutput);
    }

    @NotNull
    @Override
    public String getName() {
        return "ToolmakerCraftingProvider";
    }

    @Override
    protected void registerRecipes(@NotNull final Consumer<FinishedRecipe> consumer) {
        for (int matLevel = 0; matLevel < Materials.TOOL_MATERIALS.length; ++matLevel) {
            for (String matName : Materials.TOOL_MATERIALS[matLevel]) {
                LOGGER.info("Registering " + matName);
                // Axe
//                CustomRecipeProvider.CustomRecipeBuilder.create(TOOLMAKER, MODULE_CUSTOM, matName + "_axe")
//                        .inputs(Stream.of(
//                                new ItemStorage(new ItemStack(ForgeRegistries.ITEMS
//                                        .getValue(new ResourceLocation("gtceu:" + matName + "_ingot")))),
//                                new ItemStorage(new ItemStack(ForgeRegistries.ITEMS
//                                        .getValue(new ResourceLocation("gtceu:" + matName + "_plate")))),
//                                new ItemStorage(new ItemStack(ForgeRegistries.ITEMS
//                                        .getValue(new ResourceLocation("gtceu:treated_wood_rod")))))
//                                .collect(Collectors.toList()))
//                        .result(new ItemStack(
//                                ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:" + matName + "_axe"))))
//                        .minBuildingLevel(matLevel)
//                        .build(consumer);
            }
        }
    }
}
