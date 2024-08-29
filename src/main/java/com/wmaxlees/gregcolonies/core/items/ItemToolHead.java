package com.wmaxlees.gregcolonies.core.items;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.lowdragmc.lowdraglib.Platform;
import com.wmaxlees.gregcolonies.client.renderer.item.ToolHeadItemRenderer;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemToolHead extends Item {
  GTToolType toolType;
  Material toolMaterial;

  public ItemToolHead(final GTToolType type, final Material material, final Properties properties) {
    super(properties);

    this.toolType = type;
    this.toolMaterial = material;

    if (Platform.isClient()) {
      ToolHeadItemRenderer.create(this, toolType);
    }
  }

  Material getMaterial() {
    return toolMaterial;
  }

  public static class ToolHeadItemColor implements ItemColor {
    @Override
    public int getColor(ItemStack itemStack, int index) {
      if (itemStack.getItem() instanceof ItemToolHead item) {
        Material material = item.getMaterial();
        // TODO switch around main and secondary color once new textures are added
        return switch (index) {
          case 0, -101 -> {
            //            if (item.getToolClasses(itemStack).contains(GTToolType.CROWBAR)) {
            //              if (itemStack.hasTag() && getToolTag(itemStack).contains(TINT_COLOR_KEY,
            // Tag.TAG_INT)) {
            //                yield getToolTag(itemStack).getInt(TINT_COLOR_KEY);
            //              }
            //            }
            yield -1;
          }
          case 1, -111 -> material.getMaterialARGB();
          case 2, -121 -> {
            if (material.getMaterialSecondaryARGB() != -1) {
              yield material.getMaterialSecondaryARGB();
            } else {
              yield material.getMaterialARGB();
            }
          }
          default -> -1;
        };
      }
      return -1;
    }
  }
}
