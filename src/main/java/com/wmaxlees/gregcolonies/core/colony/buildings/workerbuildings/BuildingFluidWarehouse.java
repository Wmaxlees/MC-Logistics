package com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.requestsystem.resolver.IRequestResolver;
import com.minecolonies.api.util.constant.TypeConstants;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.wmaxlees.gregcolonies.api.items.ModItems;
import com.wmaxlees.gregcolonies.core.colony.requestsystem.resolvers.FluidWarehouseRequestResolver;
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

  @Override
  public ImmutableCollection<IRequestResolver<?>> createResolvers() {
    final ImmutableCollection<IRequestResolver<?>> supers = super.createResolvers();
    final ImmutableList.Builder<IRequestResolver<?>> builder = ImmutableList.builder();

    builder.addAll(supers);
    builder.add(
        new FluidWarehouseRequestResolver(
            getRequester().getLocation(),
            getColony()
                .getRequestManager()
                .getFactoryController()
                .getNewInstance(TypeConstants.ITOKEN)));

    return builder.build();
  }
}
