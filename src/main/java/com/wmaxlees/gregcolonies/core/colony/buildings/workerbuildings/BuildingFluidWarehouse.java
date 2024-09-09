package com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.wmaxlees.gregcolonies.api.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import org.jetbrains.annotations.NotNull;

public class BuildingFluidWarehouse extends AbstractBuilding {
  /** String describing the Warehouse. */
  private static final String FLUID_WAREHOUSE = "fluid_warehouse";

  /** Max level of the building. */
  private static final int MAX_LEVEL = 1;

  /**
   * Instantiates a new warehouse building.
   *
   * @param c the colony.
   * @param l the location
   */
  public BuildingFluidWarehouse(final IColony c, final BlockPos l) {
    super(c, l);

    keepX.put(
        stack -> stack.getItem() == ModItems.courierTank, new Tuple<>(Integer.MAX_VALUE, false));
  }

  @NotNull
  @Override
  public String getSchematicName() {
    return FLUID_WAREHOUSE;
  }

  @Override
  public int getMaxBuildingLevel() {
    return MAX_LEVEL;
  }
}
