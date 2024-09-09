package com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.buildings.workerbuildings.IBuildingDeliveryman;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

public class BuildingFluidCourier extends AbstractBuilding implements IBuildingDeliveryman {

  private static final String FLUID_COURIER = "fluid_courier";

  /**
   * Instantiates a new warehouse building.
   *
   * @param c the colony.
   * @param l the location
   */
  public BuildingFluidCourier(final IColony c, final BlockPos l) {
    super(c, l);
  }

  @NotNull
  @Override
  public String getSchematicName() {
    return FLUID_COURIER;
  }

  @Override
  public int getMaxBuildingLevel() {
    return 1;
  }
}
