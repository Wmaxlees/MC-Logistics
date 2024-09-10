package com.wmaxlees.gregcolonies.core.tileentities;

import com.wmaxlees.gregcolonies.api.util.constant.Logger;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public class TileEntityTank extends BlockEntity {
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
    Logger.InfoLog(
        "Currently contains {}mb of {}",
        tank.getFluidAmount(),
        tank.getFluid().getDisplayName().getString());
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
      return ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, LazyOptional.of(() -> tank));
    }
    return super.getCapability(cap);
  }
}
