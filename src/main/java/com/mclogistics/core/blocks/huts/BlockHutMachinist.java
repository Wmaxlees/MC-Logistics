package com.mclogistics.core.blocks.huts;

import com.mclogistics.api.blocks.AbstractBlockHut;
import com.mclogistics.api.colony.buildings.ModBuildings;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import org.jetbrains.annotations.NotNull;

public class BlockHutMachinist extends AbstractBlockHut<BlockHutMachinist> {
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
