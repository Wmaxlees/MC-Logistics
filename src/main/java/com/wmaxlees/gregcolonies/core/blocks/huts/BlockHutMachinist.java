package com.wmaxlees.gregcolonies.core.blocks.huts;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.wmaxlees.gregcolonies.api.blocks.AbstractGregColoniesBlockHut;
import com.wmaxlees.gregcolonies.api.colony.buildings.ModBuildings;
import org.jetbrains.annotations.NotNull;

public class BlockHutMachinist extends AbstractGregColoniesBlockHut<BlockHutMachinist> {
  @NotNull
  @Override
  public String getHutName() {
    return "blockhutmachinist";
  }

  @Override
  public BuildingEntry getBuildingEntry() {
    return ModBuildings.machinist.get();
  }
}
