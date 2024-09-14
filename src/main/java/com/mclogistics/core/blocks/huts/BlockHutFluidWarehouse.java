package com.mclogistics.core.blocks.huts;

import com.mclogistics.api.blocks.AbstractBlockHut;
import com.mclogistics.api.colony.buildings.ModBuildings;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import org.jetbrains.annotations.NotNull;

public class BlockHutFluidWarehouse extends AbstractBlockHut<BlockHutFluidWarehouse> {
  @NotNull
  @Override
  public String getHutName() {
    return "blockhutfluidwarehouse";
  }

  @Override
  public BuildingEntry getBuildingEntry() {
    return ModBuildings.fluidWarehouse.get();
  }
}
