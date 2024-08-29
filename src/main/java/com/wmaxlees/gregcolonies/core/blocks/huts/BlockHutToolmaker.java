package com.wmaxlees.gregcolonies.core.blocks.huts;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.wmaxlees.gregcolonies.api.blocks.AbstractGregColoniesBlockHut;
import com.wmaxlees.gregcolonies.api.colony.buildings.ModBuildings;
import org.jetbrains.annotations.NotNull;

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
