package com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings;

import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.buildings.workerbuildings.IWareHouse;
import com.minecolonies.api.tileentities.AbstractTileEntityColonyBuilding;
import com.minecolonies.api.tileentities.AbstractTileEntityWareHouse;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.buildings.modules.CourierAssignmentModule;
import com.minecolonies.core.tileentities.TileEntityWareHouse;
import com.wmaxlees.gregcolonies.core.blocks.BlockGregColoniesTank;
import com.wmaxlees.gregcolonies.core.tileentities.TileEntityTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class BuildingFluidWarehouse extends AbstractBuilding implements IWareHouse {
  /** String describing the Warehouse. */
  private static final String FLUID_WAREHOUSE = "fluid_warehouse";

  /** Max level of the building. */
  private static final int MAX_LEVEL = 5;

  /** Max storage upgrades. */
  public static final int MAX_STORAGE_UPGRADE = 3;

  /**
   * Instantiates a new warehouse building.
   *
   * @param c the colony.
   * @param l the location
   */
  public BuildingFluidWarehouse(final IColony c, final BlockPos l) {
    super(c, l);
  }

  @Override
  public boolean canAccessWareHouse(final ICitizenData citizenData) {
    // TODO(Wmaxlees): Switch this to a fluid Courier once that exists
    return getFirstModuleOccurance(CourierAssignmentModule.class).hasAssignedCitizen(citizenData);
  }

  @Override
  public void upgradeContainers(Level world) {}

  @NotNull
  @Override
  public String getSchematicName() {
    return FLUID_WAREHOUSE;
  }

  @Override
  public boolean hasContainerPosition(final BlockPos inDimensionLocation) {
    return containerList.contains(inDimensionLocation)
        || getLocation().getInDimensionLocation().equals(inDimensionLocation);
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
      if (entity instanceof TileEntityTank) {
        // ((TileEntityTank) entity).setInWarehouse(true);
        //        while (((TileEntityRack) entity).getUpgradeSize()
        //            < getFirstModuleOccurance(WarehouseModule.class).getStorageUpgrade()) {
        //          ((TileEntityRack) entity).upgradeRackSize();
        //        }
      }
    }
    super.registerBlockPosition(block, pos, world);
  }

  @Override
  public AbstractTileEntityWareHouse getTileEntity() {
    final AbstractTileEntityColonyBuilding entity = super.getTileEntity();
    return !(entity instanceof TileEntityWareHouse) ? null : (AbstractTileEntityWareHouse) entity;
  }
}
