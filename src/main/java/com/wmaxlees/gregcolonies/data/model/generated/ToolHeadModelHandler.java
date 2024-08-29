package com.wmaxlees.gregcolonies.data.model.generated;

import com.google.gson.*;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.mojang.logging.LogUtils;
import com.wmaxlees.gregcolonies.api.items.ModItems;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;

public class ToolHeadModelHandler {
  // Directly reference a slf4j logger
  private static final Logger LOGGER = LogUtils.getLogger();

  public static void init(BiConsumer<ResourceLocation, JsonElement> provider) {
    for (Material mat : GTCEuAPI.materialManager.getRegisteredMaterials()) {
      if (!mat.hasProperty(PropertyKey.TOOL)) {
        continue;
      }

      for (GTToolType type : GTToolType.getTypes().values()) {
        processToolHead(provider, mat, type);
      }
    }
  }

  private static void processToolHead(
      BiConsumer<ResourceLocation, JsonElement> provider, Material material, GTToolType type) {
    Item item = ModItems.TOOL_HEAD_ITEMS.get(material, type);

    String layer0 = "item/tools/" + type.name;

    JsonObject json = new JsonObject();
    json.addProperty("parent", "item/generated");
    JsonObject textures = new JsonObject();
    textures.addProperty("layer0", "gtceu:" + layer0);
    json.add("textures", textures);

    provider.accept(
        new ResourceLocation(Constants.MOD_ID, material.getName() + "_" + type.name + "_head"),
        json);
  }
}
