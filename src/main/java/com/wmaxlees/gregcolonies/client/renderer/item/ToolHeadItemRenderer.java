package com.wmaxlees.gregcolonies.client.renderer.item;

import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.wmaxlees.gregcolonies.data.pack.GregColoniesDynamicResourcePack;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class ToolHeadItemRenderer {
  private static final Set<ToolHeadItemRenderer> MODELS = new HashSet<>();

  public static void reinitModels() {
    for (ToolHeadItemRenderer model : MODELS) {
      GregColoniesDynamicResourcePack.addItemModel(
          ForgeRegistries.ITEMS.getKey(model.item),
          new DelegatedModel(model.toolType.modelLocation));
    }
  }

  private final Item item;
  private final GTToolType toolType;

  protected ToolHeadItemRenderer(Item item, GTToolType toolType) {
    this.item = item;
    this.toolType = toolType;
  }

  public static void create(Item item, GTToolType toolType) {
    MODELS.add(new ToolHeadItemRenderer(item, toolType));
  }
}
