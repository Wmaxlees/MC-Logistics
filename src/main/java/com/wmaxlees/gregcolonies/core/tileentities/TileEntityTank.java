package com.wmaxlees.gregcolonies.core.tileentities;

import com.mojang.logging.LogUtils;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class TileEntityTank extends BlockEntity {
  // Directly reference a slf4j logger
  private static final Logger LOGGER = LogUtils.getLogger();

  protected final int MAX_SIZE_BUCKETS = 18;

  protected FluidTank tank =
      new FluidTank(MAX_SIZE_BUCKETS * FluidType.BUCKET_VOLUME) {
        @Override
        protected void onContentsChanged() {
          TileEntityTank.this.setChanged();
        }
      };

  public TileEntityTank(final BlockPos pos, final BlockState state) {
    super(GregColoniesTileEntities.TANK.get(), pos, state);
  }

  @Override
  public void saveAdditional(@NotNull CompoundTag data) {
    super.saveAdditional(data);
    if (!tank.isEmpty()) {
      tank.writeToNBT(data);
    }
  }

  @Override
  public void load(@NotNull CompoundTag data) {
    super.load(data);
    tank.readFromNBT(data);
  }

  public boolean onPlayerUse(Player player, InteractionHand hand) {
    return FluidUtil.interactWithFluidHandler(player, hand, tank);
  }

  public IFluidTank getTank() {
    return tank;
  }

  public IFluidHandler getFluidHandler() {
    return tank;
  }

  public void logContents() {
    LOGGER.info(
        "{}: Currently contains {}mb of {}",
        Constants.MOD_ID,
        tank.getFluidAmount(),
        tank.getFluid().getDisplayName().getString());
  }
}
