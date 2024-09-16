package com.mclogistics.core.colony.buildings.workerbuildings;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.mclogistics.core.colony.requestsystem.resolvers.ItemWarehouseRequestResolver;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.requestsystem.resolver.IRequestResolver;
import com.minecolonies.api.util.constant.TypeConstants;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

public class BuildingItemWarehouse extends AbstractBuilding {
  private static final String ITEM_WAREHOUSE = "item_warehouse";

  private static final int MAX_LEVEL = 1;

  public BuildingItemWarehouse(final IColony c, final BlockPos l) {
    super(c, l);
  }

  @NotNull
  @Override
  public String getSchematicName() {
    return ITEM_WAREHOUSE;
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
        new ItemWarehouseRequestResolver(
            getRequester().getLocation(),
            getColony()
                .getRequestManager()
                .getFactoryController()
                .getNewInstance(TypeConstants.ITOKEN)));

    return builder.build();
  }
}
