package com.wmaxlees.gregcolonies.api.items;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.wmaxlees.gregcolonies.core.items.ItemToolHead;
import net.minecraft.world.item.Item;

public final class ModItems {
    public final static Table<Material, GTToolType, ItemProviderEntry<ItemToolHead>> TOOL_HEAD_ITEMS = ArrayTable.create(
            GTCEuAPI.materialManager.getRegisteredMaterials().stream().filter(mat -> mat.hasProperty(PropertyKey.TOOL))
                    .toList(),
            GTToolType.getTypes().values().stream().toList());

    private ModItems() {}
}
