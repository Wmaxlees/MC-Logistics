package com.wmaxlees.gregcolonies.core.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.FluidHandlerBlockEntity;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

public class TileEntityTank extends FluidHandlerBlockEntity implements MenuProvider {
  protected final int MAX_SIZE_MILLI_BUCKETS = 12000;

  public TileEntityTank(final BlockPos pos, final BlockState state) {
    super(GregColoniesTileEntities.TANK.get(), pos, state);
    this.tank = new Tank(MAX_SIZE_MILLI_BUCKETS);
  }

  @Override
  public Component getDisplayName() {
    return null;
  }

  @Override
  public @Nullable AbstractContainerMenu createMenu(
      int p_39954_, Inventory p_39955_, Player p_39956_) {
    return null;
  }

  private static class Tank extends FluidTank {
    public Tank(int capacity) {
      super(capacity);
    }
  }
}
