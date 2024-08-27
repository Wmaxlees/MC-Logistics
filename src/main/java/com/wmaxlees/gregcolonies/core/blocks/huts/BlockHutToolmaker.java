package com.wmaxlees.gregcolonies.core.blocks.huts;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.wmaxlees.gregcolonies.api.blocks.AbstractGregColoniesBlockHut;
import com.wmaxlees.gregcolonies.api.colony.buildings.ModBuildings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockHutToolmaker extends AbstractGregColoniesBlockHut<BlockHutToolmaker> {
    @NotNull
    @Override
    public String getHutName() {
        return "blockhuttoolmaker";
    }

    @Override
    public BuildingEntry getBuildingEntry() {
        return ModBuildings.toolmaker.get();
    }
}
