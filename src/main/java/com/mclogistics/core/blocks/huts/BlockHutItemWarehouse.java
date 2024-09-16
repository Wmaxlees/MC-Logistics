package com.mclogistics.core.blocks.huts;

import com.mclogistics.api.blocks.AbstractBlockHut;
import com.mclogistics.api.colony.buildings.ModBuildings;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import org.jetbrains.annotations.NotNull;

public class BlockHutItemWarehouse extends AbstractBlockHut<BlockHutItemWarehouse> {
  @NotNull
  @Override
  public String getHutName() {
    return "blockhutitemwarehouse";
  }

  @Override
  public BuildingEntry getBuildingEntry() {
    return ModBuildings.itemWarehouse.get();
  }
}
