package com.mclogistics.core.colony.buildings.modules;

import com.mclogistics.api.items.ModItems;
import com.mclogistics.core.items.capabilities.CourierTankFluidHandler;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IPersistentModule;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javafx.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
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

  public int getMilliBucketsAvailableOf(Predicate<Fluid> predicate) {
    int available = 0;
    for (BlockPos tankPos : tanks) {
      BlockEntity tank = building.getColony().getWorld().getBlockEntity(tankPos);
      if (tank.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().isEmpty()) {
        continue;
      }

      IFluidHandler handler = tank.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get();
      for (int i = 0; i < handler.getTanks(); ++i) {
        FluidStack stack = handler.getFluidInTank(i);
        if (predicate.test(stack.getFluid())) {
          available += stack.getAmount();
        }
      }
    }

    return available;
  }

  /**
   * @return A pair of item stacks. The first is the full tanks. The second is the partial full tank
   *     with the remainder.
   */
  public Pair<ItemStack, ItemStack> tryDrainToCourierTank(Fluid fluid, int amount) {
    int remainingAmount = amount;
    for (BlockPos tankPos : tanks) {
      BlockEntity tank = building.getColony().getWorld().getBlockEntity(tankPos);
      if (tank.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().isEmpty()) {
        continue;
      }

      IFluidHandler handler = tank.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get();
      for (int i = 0; i < handler.getTanks(); ++i) {
        FluidStack stack = handler.getFluidInTank(i);
        if (fluid.isSame(stack.getFluid())) {
          remainingAmount -= stack.getAmount();
          if (remainingAmount <= 0) {
            break;
          }
        }
      }
    }

    if (remainingAmount > 0) {
      return null;
    }

    remainingAmount = amount;
    for (BlockPos tankPos : tanks) {
      BlockEntity tank = building.getColony().getWorld().getBlockEntity(tankPos);
      if (tank.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().isEmpty()) {
        continue;
      }

      IFluidHandler handler = tank.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get();
      for (int i = 0; i < handler.getTanks(); ++i) {
        FluidStack stack = handler.getFluidInTank(i);
        if (fluid.isSame(stack.getFluid())) {
          int amountToRemove = Math.min(remainingAmount, stack.getAmount());

          FluidStack toRemove = stack.copy();
          toRemove.setAmount(amountToRemove);

          handler.drain(toRemove, IFluidHandler.FluidAction.EXECUTE);

          remainingAmount -= stack.getAmount();
          if (remainingAmount <= 0) {
            break;
          }
        }
      }
    }

    int fullContainers =
        CourierTankFluidHandler.MAX_CAPACITY_BUCKETS * FluidType.BUCKET_VOLUME / amount;
    int remaining = CourierTankFluidHandler.MAX_CAPACITY_BUCKETS * FluidType.BUCKET_VOLUME % amount;

    FluidStack fullFluid =
        new FluidStack(
            fluid, CourierTankFluidHandler.MAX_CAPACITY_BUCKETS * FluidType.BUCKET_VOLUME);
    FluidStack partialFluid = new FluidStack(fluid, remaining);

    ItemStack fullTanks = new ItemStack(ModItems.courierTank.get());
    fullTanks
        .getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
        .resolve()
        .get()
        .fill(fullFluid, IFluidHandler.FluidAction.EXECUTE);
    fullTanks.setCount(fullContainers);

    ItemStack partialTank = new ItemStack(ModItems.courierTank.get());
    partialTank
        .getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
        .resolve()
        .get()
        .fill(partialFluid, IFluidHandler.FluidAction.EXECUTE);

    return new Pair<>(fullTanks, partialTank);
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
