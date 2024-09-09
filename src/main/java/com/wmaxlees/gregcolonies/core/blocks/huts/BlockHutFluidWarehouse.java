package com.wmaxlees.gregcolonies.core.blocks.huts;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.wmaxlees.gregcolonies.api.blocks.AbstractGregColoniesBlockHut;
import com.wmaxlees.gregcolonies.api.colony.buildings.ModBuildings;
import org.jetbrains.annotations.NotNull;

public class BlockHutFluidWarehouse extends AbstractGregColoniesBlockHut<BlockHutFluidWarehouse> {
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
