package com.wmaxlees.gregcolonies.data.recipe.generated;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.minecolonies.api.util.constant.BuildingConstants.MODULE_CUSTOM;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.item.tool.ToolHelper;
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

public class ToolRecipeHandler {
  // Directly reference a slf4j logger
  private static final Logger LOGGER = LogUtils.getLogger();

  public static void init(Consumer<FinishedRecipe> provider) {
    for (Material mat : GTCEuAPI.materialManager.getRegisteredMaterials()) {
      if (!mat.hasProperty(PropertyKey.TOOL)) {
        continue;
      }

      processTool(provider, mat);
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

  private static void addToolRecipe(
      Consumer<FinishedRecipe> provider,
      @NotNull Material material,
      @NotNull GTToolType tool,
      int treatedWoodRodCount,
      int metalRodCount,
      int screwCount,
      boolean requireFileTool) {
    Stream.Builder<ItemStorage> inputItems = Stream.builder();
    if (treatedWoodRodCount > 0) {
      Item treatedWoodRodItem =
          ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:treated_wood_rod"));
      if (treatedWoodRodItem == null) {
        return;
      }
      ItemStorage treatedWoodRods = new ItemStorage(new ItemStack(treatedWoodRodItem));
      treatedWoodRods.setAmount(treatedWoodRodCount);
      inputItems.add(treatedWoodRods);
    }

    if (metalRodCount > 0) {
      Item rodItem =
          ForgeRegistries.ITEMS.getValue(
              new ResourceLocation(material.getModid() + ":" + material.getName() + "_rod"));
      if (rodItem == null) {
        return;
      }
      ItemStorage rods = new ItemStorage(new ItemStack(rodItem));
      rods.setAmount(metalRodCount);
      inputItems.add(rods);
    }

    if (screwCount > 0) {
      Item screwItem =
          ForgeRegistries.ITEMS.getValue(
              new ResourceLocation(material.getModid() + ":" + material.getName() + "_screw"));
      if (screwItem == null) {
        return;
      }
      ItemStorage screws = new ItemStorage(new ItemStack(screwItem));
      screws.setAmount(screwCount);
      inputItems.add(screws);
    }

    ItemStack toolHeadStack = getToolHead(tool, material);
    if (toolHeadStack.isEmpty()) {
      return;
    }
    inputItems.add(new ItemStorage(toolHeadStack));

    ItemStack toolStack = ToolHelper.get(tool, material);
    if (toolStack.isEmpty()) {
      return;
    }

    CustomRecipeProvider.CustomRecipeBuilder builder =
        CustomRecipeProvider.CustomRecipeBuilder.create(
                ModJobs.TOOLMAKER_ID.getPath(),
                MODULE_CUSTOM,
                String.format("%s_%s", tool.name, material.getName()))
            .result(toolStack)
            .inputs(inputItems.build().collect(Collectors.toList()));

    if (requireFileTool) {
      builder.requiredTool(ToolType.FILE);
    }

    builder.build(provider);
  }

  private static void processTool(Consumer<FinishedRecipe> provider, Material material) {
    if (material.hasFlag(GENERATE_PLATE)) {
      addToolRecipe(provider, material, GTToolType.MINING_HAMMER, 1, 0, 0, true);

      addToolRecipe(provider, material, GTToolType.SPADE, 2, 0, 0, true);

      addToolRecipe(provider, material, GTToolType.SAW, 2, 0, 0, true);

      addToolRecipe(provider, material, GTToolType.AXE, 2, 0, 0, true);

      addToolRecipe(provider, material, GTToolType.HOE, 2, 0, 0, true);

      addToolRecipe(provider, material, GTToolType.PICKAXE, 2, 0, 0, true);

      addToolRecipe(provider, material, GTToolType.SCYTHE, 2, 0, 0, true);

      addToolRecipe(provider, material, GTToolType.SHOVEL, 2, 0, 0, true);

      addToolRecipe(provider, material, GTToolType.SWORD, 1, 0, 0, true);

      addToolRecipe(provider, material, GTToolType.HARD_HAMMER, 1, 0, 0, false);

      addToolRecipe(provider, material, GTToolType.FILE, 1, 0, 0, false);

      addToolRecipe(provider, material, GTToolType.KNIFE, 1, 0, 0, true);

      addToolRecipe(provider, material, GTToolType.WRENCH, 0, 0, 0, false);
    }
    if (material.hasFlag(GENERATE_ROD)) {
      if (material.hasFlag(GENERATE_PLATE)) {
        addToolRecipe(provider, material, GTToolType.BUTCHERY_KNIFE, 0, 1, 0, true);

        if (material.hasFlag(GENERATE_BOLT_SCREW)) {
          addToolRecipe(provider, material, GTToolType.WIRE_CUTTER, 0, 2, 1, true);
        }
      }

      addToolRecipe(provider, material, GTToolType.SCREWDRIVER, 1, 0, 0, true);

      addToolRecipe(provider, material, GTToolType.CROWBAR, 0, 0, 0, true);
    }
  }
}
