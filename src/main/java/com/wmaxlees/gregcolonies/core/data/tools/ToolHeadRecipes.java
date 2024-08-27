package com.wmaxlees.gregcolonies.core.data.tools;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.ToolProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.core.generation.CustomRecipeProvider;
import com.wmaxlees.gregcolonies.api.colony.jobs.ModJobs;
import com.wmaxlees.gregcolonies.api.items.ModItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.minecolonies.api.util.constant.BuildingConstants.MODULE_CRAFTING;

public class ToolHeadRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        TagPrefix.plate.executeHandler(provider, PropertyKey.TOOL, ToolHeadRecipes::processToolHead);
    }

    public static ItemStack getToolHead(GTToolType toolType, Material material) {
        if (material.hasProperty(PropertyKey.TOOL)) {
            var entry = ModItems.TOOL_HEAD_ITEMS.get(material, toolType);
            if (entry != null) {
                return new ItemStack(entry.get());
            }
        }
        return ItemStack.EMPTY;
    }

    public static void addToolHeadRecipe(Consumer<FinishedRecipe> provider, @NotNull Material material, @NotNull GTToolType tool, int plateCount, int ingotCount, int rodCount, boolean requireHammerTool) {
        Stream.Builder<ItemStorage> inputItems = Stream.builder();
        if (plateCount > 0) {
            Item plateItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(material.getModid() + ":" + material.getName() + "_plate"));
            if (plateItem == null) {
                return;
            }
            ItemStorage plates = new ItemStorage(new ItemStack(plateItem));
            plates.setAmount(plateCount);
            inputItems.add(plates);
        }

        if (ingotCount > 0) {
            Item ingotItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(material.getModid() + ":" + material.getName() + "_ingot"));
            if (ingotItem == null) {
                return;
            }
            ItemStorage ingots = new ItemStorage(new ItemStack(ingotItem));
            ingots.setAmount(ingotCount);
            inputItems.add(ingots);
        }

        if (rodCount > 0) {
            Item rodItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(material.getModid() + ":" + material.getName() + "_rod"));
            if (rodItem == null) {
                return;
            }
            ItemStorage rods = new ItemStorage(new ItemStack(rodItem));
            rods.setAmount(rodCount);
            inputItems.add(rods);
        }

        ItemStack toolHeadStack = getToolHead(tool, material);
        if (toolHeadStack.isEmpty()) {
            return;
        }

        CustomRecipeProvider.CustomRecipeBuilder.create(ModJobs.TOOLMAKER_ID.getPath(), MODULE_CRAFTING, String.format("%s_%s_head", tool.name, material.getName())).result(toolHeadStack).inputs(inputItems.build().collect(Collectors.toList())).build(provider);
    }

    private static void processToolHead(TagPrefix prefix, Material material, ToolProperty property, Consumer<FinishedRecipe> provider) {
        UnificationEntry plate = new UnificationEntry(TagPrefix.plate, material);
        UnificationEntry ingot = new UnificationEntry(material.hasProperty(PropertyKey.GEM) ? TagPrefix.gem : TagPrefix.ingot, material);

        if (material.hasFlag(GENERATE_PLATE)) {
            addToolHeadRecipe(provider, material, GTToolType.MINING_HAMMER, 6, 0, 0, true);

            addToolHeadRecipe(provider, material, GTToolType.SPADE, 3, 0, 0, true);

            addToolHeadRecipe(provider, material, GTToolType.SAW, 2, 0, 0, true);

            addToolHeadRecipe(provider, material, GTToolType.AXE, 2, 1, 0, true);

            addToolHeadRecipe(provider, material, GTToolType.HOE, 1, 1, 0, true);

            addToolHeadRecipe(provider, material, GTToolType.PICKAXE, 1, 2, 0, true);

            addToolHeadRecipe(provider, material, GTToolType.SCYTHE, 2, 1, 0, true);

            addToolHeadRecipe(provider, material, GTToolType.SHOVEL, 1, 0, 0, true);

            addToolHeadRecipe(provider, material, GTToolType.SWORD, 2, 0, 0, true);

            addToolHeadRecipe(provider, material, GTToolType.HARD_HAMMER, 0, 6, 0, false);

            addToolHeadRecipe(provider, material, GTToolType.FILE, 2, 0, 0, false);

            addToolHeadRecipe(provider, material, GTToolType.KNIFE, 1, 0, 0, true);

            addToolHeadRecipe(provider, material, GTToolType.WRENCH, 4, 0, 0, true);
        }

        if (material.hasFlag(GENERATE_ROD)) {
            if (material.hasFlag(GENERATE_PLATE)) {
                addToolHeadRecipe(provider, material, GTToolType.BUTCHERY_KNIFE, 4, 0, 0, true);

                if (material.hasFlag(GENERATE_BOLT_SCREW)) {
                    addToolHeadRecipe(provider, material, GTToolType.WIRE_CUTTER, 3, 0, 0, true);
                }
            }

            addToolHeadRecipe(provider, material, GTToolType.SCREWDRIVER, 0, 0, 2, true);

            addToolHeadRecipe(provider, material, GTToolType.CROWBAR, 0, 0, 3, true);
        }
    }
}
