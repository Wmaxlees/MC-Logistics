package com.wmaxlees.gregcolonies.core.colony.buildings.modules;

import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IModuleWithExternalBlocks;
import com.minecolonies.api.colony.buildings.modules.IPersistentModule;
import com.wmaxlees.gregcolonies.core.blocks.BlockGregColoniesTank;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TankUserModule extends AbstractBuildingModule
    implements IPersistentModule, IModuleWithExternalBlocks {
  private static final String NBT_TANKS = "tanks";
  private static final String NBT_POS = "pos";

  List<BlockPos> tanks = new ArrayList<>();

  @Override
  public void onBlockPlacedInBuilding(
      @NotNull final BlockState blockState,
      @NotNull final BlockPos pos,
      @NotNull final Level world) {
    if (blockState.getBlock() instanceof BlockGregColoniesTank && !tanks.contains(pos)) {
      tanks.add(pos);
    }
  }

  @Override
  public List<BlockPos> getRegisteredBlocks() {
    return tanks;
  }

  @Override
  public void deserializeNBT(final CompoundTag compound) {
    final ListTag tankTagList = compound.getList(NBT_TANKS, Tag.TAG_COMPOUND);
    for (int i = 0; i < tankTagList.size(); ++i) {
      if (tankTagList.getCompound(i).contains(NBT_POS)) {
        tanks.add(NbtUtils.readBlockPos(tankTagList.getCompound(i).getCompound(NBT_POS)));
      }
    }
  }

  @Override
  public void serializeNBT(final CompoundTag compound) {
    @NotNull final ListTag tankTagList = new ListTag();
    for (@NotNull final BlockPos entry : tanks) {
      @NotNull final CompoundTag furnaceCompound = new CompoundTag();
      furnaceCompound.put(NBT_POS, NbtUtils.writeBlockPos(entry));
      tankTagList.add(furnaceCompound);
    }
    compound.put(NBT_TANKS, tankTagList);
  }
}
