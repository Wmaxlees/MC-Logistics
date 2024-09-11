package com.wmaxlees.gregcolonies.core.colony.buildings.modules;

import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IPersistentModule;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public class InventoryUserModule extends AbstractBuildingModule implements IPersistentModule {
  private static final String NBT_TANKS = "tanks";
  private static final String NBT_CHESTS = "chests";
  private static final String NBT_POS = "pos";

  List<BlockPos> chests = new ArrayList<>();
  List<BlockPos> tanks = new ArrayList<>();

  @Override
  public void deserializeNBT(final CompoundTag compound) {
    final ListTag tankTagList = compound.getList(NBT_TANKS, Tag.TAG_COMPOUND);
    for (int i = 0; i < tankTagList.size(); ++i) {
      if (tankTagList.getCompound(i).contains(NBT_POS)) {
        tanks.add(NbtUtils.readBlockPos(tankTagList.getCompound(i).getCompound(NBT_POS)));
      }
    }

    final ListTag chestTagList = compound.getList(NBT_CHESTS, Tag.TAG_COMPOUND);
    for (int i = 0; i < chestTagList.size(); ++i) {
      if (chestTagList.getCompound(i).contains(NBT_POS)) {
        chests.add(NbtUtils.readBlockPos(chestTagList.getCompound(i).getCompound(NBT_POS)));
      }
    }
  }

  @Override
  public void serializeNBT(final CompoundTag compound) {
    @NotNull final ListTag tankTagList = new ListTag();
    for (@NotNull final BlockPos entry : tanks) {
      @NotNull final CompoundTag tankCompound = new CompoundTag();
      tankCompound.put(NBT_POS, NbtUtils.writeBlockPos(entry));
      tankTagList.add(tankCompound);
    }
    compound.put(NBT_TANKS, tankTagList);

    @NotNull final ListTag chestTagList = new ListTag();
    for (@NotNull final BlockPos entry : chests) {
      @NotNull final CompoundTag chestCompound = new CompoundTag();
      chestCompound.put(NBT_POS, NbtUtils.writeBlockPos(entry));
      chestTagList.add(chestCompound);
    }
    compound.put(NBT_CHESTS, chestTagList);
  }

  @Override
  public void serializeToView(@NotNull final FriendlyByteBuf buf) {
    buf.writeInt(chests.size());
    for (@NotNull final BlockPos entry : chests) {
      buf.writeBlockPos(entry);
    }

    buf.writeInt(tanks.size());
    for (@NotNull final BlockPos entry : tanks) {
      buf.writeBlockPos(entry);
    }
  }

  public void toggleFluidLocation(BlockPos pos) {
    if (tanks.contains(pos)) {
      tanks.remove(pos);
    } else {
      tanks.add(pos);
    }
  }

  public List<BlockPos> getTanks() {
    return tanks;
  }

  public void toggleItemLocation(BlockPos pos) {
    if (chests.contains(pos)) {
      chests.remove(pos);
    } else {
      chests.add(pos);
    }
  }

  public List<BlockPos> getChests() {
    return chests;
  }
}
