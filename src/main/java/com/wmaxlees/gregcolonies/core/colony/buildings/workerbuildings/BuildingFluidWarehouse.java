package com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.wmaxlees.gregcolonies.api.items.ModItems;
import com.wmaxlees.gregcolonies.core.blocks.BlockGregColoniesTank;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
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

    keepX.put(stack -> stack.is(ModItems.courierTank), new Tuple<>(Integer.MAX_VALUE, true));
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
  public void registerBlockPosition(
      @NotNull final Block block, @NotNull final BlockPos pos, @NotNull final Level world) {
    if (block instanceof BlockGregColoniesTank) {
      final BlockEntity entity = world.getBlockEntity(pos);
      //      if (entity instanceof TileEntityTank) {
      //         ((TileEntityTank) entity).setInWarehouse(true);
      //                while (((TileEntityRack) entity).getUpgradeSize()
      //                    < getFirstModuleOccurance(WarehouseModule.class).getStorageUpgrade()) {
      //                  ((TileEntityRack) entity).upgradeRackSize();
      //                }
      //      }
    }
    super.registerBlockPosition(block, pos, world);
  }
}
