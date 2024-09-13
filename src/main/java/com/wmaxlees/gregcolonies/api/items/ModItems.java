package com.wmaxlees.gregcolonies.api.items;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.wmaxlees.gregcolonies.core.items.ItemCourierTank;
import com.wmaxlees.gregcolonies.core.items.ItemScepterInventorySelector;
import com.wmaxlees.gregcolonies.core.items.ItemScepterMachinist;
import com.wmaxlees.gregcolonies.core.items.ItemToolHead;
import net.minecraft.world.item.Item;

public final class ModItems {
  public static final Table<Material, GTToolType, Item> TOOL_HEAD_ITEMS =
      ArrayTable.create(
          GTCEuAPI.materialManager.getRegisteredMaterials().stream()
              .filter(mat -> mat.hasProperty(PropertyKey.TOOL))
              .toList(),
          GTToolType.getTypes().values().stream().toList());

  public static Item scepterMachinistInput;
  public static Item scepterMachinistOutput;
  public static Item scepterTankInventory;
  public static Item scepterChestInventory;

  public static Item courierTank;

  static {
    for (Material mat : GTCEuAPI.materialManager.getRegisteredMaterials()) {
      if (!mat.hasProperty(PropertyKey.TOOL)) {
        continue;
      }

      for (GTToolType type : GTToolType.getTypes().values()) {
        TOOL_HEAD_ITEMS.put(mat, type, new ItemToolHead(type, mat, new Item.Properties()));
      }
    }

    scepterMachinistInput = new ItemScepterMachinist.Input(new Item.Properties());
    scepterMachinistOutput = new ItemScepterMachinist.Output(new Item.Properties());
    scepterTankInventory = new ItemScepterInventorySelector.Fluid(new Item.Properties());
    scepterChestInventory = new ItemScepterInventorySelector.Item(new Item.Properties());

    courierTank = new ItemCourierTank(new Item.Properties());
  }

  private ModItems() {}
}
