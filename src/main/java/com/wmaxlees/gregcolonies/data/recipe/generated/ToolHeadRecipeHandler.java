package com.wmaxlees.gregcolonies.data.recipe.generated;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.minecolonies.api.util.constant.BuildingConstants.MODULE_CUSTOM;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.core.generation.CustomRecipeProvider;
import com.mojang.logging.LogUtils;
import com.wmaxlees.gregcolonies.api.colony.jobs.ModJobs;
import com.wmaxlees.gregcolonies.api.items.ModItems;
import com.wmaxlees.gregcolonies.api.util.constant.ToolType;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class ToolHeadRecipeHandler {
  // Directly reference a slf4j logger
  private static final Logger LOGGER = LogUtils.getLogger();

  public static void init(Consumer<FinishedRecipe> provider) {
    for (Material mat : GTCEuAPI.materialManager.getRegisteredMaterials()) {
      if (!mat.hasProperty(PropertyKey.TOOL)) {
        continue;
      }

      processToolHead(provider, mat);
    }
  }

  private static ItemStack getToolHead(GTToolType toolType, Material material) {
    if (material.hasProperty(PropertyKey.TOOL)) {
      var entry = ModItems.TOOL_HEAD_ITEMS.get(material, toolType);
      if (entry != null) {
        return new ItemStack(entry);
      }
    }
    return ItemStack.EMPTY;
  }

  private static void addToolHeadRecipe(
      Consumer<FinishedRecipe> provider,
      @NotNull Material material,
      @NotNull GTToolType tool,
      int plateCount,
      int ingotCount,
      int rodCount,
      boolean requireHammerTool) {
    Stream.Builder<ItemStorage> inputItems = Stream.builder();
    if (plateCount > 0) {
      Item plateItem =
          ForgeRegistries.ITEMS.getValue(
              new ResourceLocation(material.getModid() + ":" + material.getName() + "_plate"));
      if (plateItem == null) {
        return;
      }
      ItemStorage plates = new ItemStorage(new ItemStack(plateItem));
      plates.setAmount(plateCount);
      inputItems.add(plates);
    }

    if (ingotCount > 0) {
      Item ingotItem =
          ForgeRegistries.ITEMS.getValue(
              new ResourceLocation(material.getModid() + ":" + material.getName() + "_ingot"));
      if (ingotItem == null) {
        return;
      }
      ItemStorage ingots = new ItemStorage(new ItemStack(ingotItem));
      ingots.setAmount(ingotCount);
      inputItems.add(ingots);
    }

    if (rodCount > 0) {
      Item rodItem =
          ForgeRegistries.ITEMS.getValue(
              new ResourceLocation(material.getModid() + ":" + material.getName() + "_rod"));
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

    CustomRecipeProvider.CustomRecipeBuilder builder =
        CustomRecipeProvider.CustomRecipeBuilder.create(
                ModJobs.TOOL_PART_SMITH_ID.getPath(),
                MODULE_CUSTOM,
                String.format("%s_%s_head", tool.name, material.getName()))
            .result(toolHeadStack)
            .inputs(inputItems.build().collect(Collectors.toList()));

    if (requireHammerTool) {
      builder.requiredTool(ToolType.HAMMER);
    }

    builder.build(provider);
  }

  private static void processToolHead(Consumer<FinishedRecipe> provider, Material material) {
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
