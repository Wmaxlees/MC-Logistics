package com.wmaxlees.gregcolonies.core.items;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import net.minecraft.world.item.Item;

public class ItemToolHead extends Item {
    GTToolType toolType;
    Material toolMaterial;

    public ItemToolHead(final GTToolType type, final Material material, final Properties properties) {
        super(properties);

        this.toolType = type;
        this.toolMaterial = material;
    }
}
