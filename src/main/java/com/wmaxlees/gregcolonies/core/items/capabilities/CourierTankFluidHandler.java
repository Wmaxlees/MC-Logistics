package com.wmaxlees.gregcolonies.core.items.capabilities;

import static com.wmaxlees.gregcolonies.api.util.constant.NbtTagConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class CourierTankFluidHandler implements IFluidHandlerItem, ICapabilityProvider {
  private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);

  private static final int MAX_TANKS = 1;

  private static final int MAX_CAPACITY = 6;

  private final List<FluidStack> tankStacks =
      Stream.generate(() -> FluidStack.EMPTY)
          .limit(MAX_TANKS)
          .collect(Collectors.toCollection(ArrayList::new));

  private final ItemStack container;

  public CourierTankFluidHandler(ItemStack container) {
    this.container = container;

    loadFromNBT();
  }

  @Override
  @Nonnull
  public <T> LazyOptional<T> getCapability(
      @Nonnull Capability<T> capability, @Nullable Direction facing) {
    if (capability == ForgeCapabilities.FLUID_HANDLER_ITEM) {
      return ForgeCapabilities.FLUID_HANDLER_ITEM.orEmpty(capability, holder);
    }

    return LazyOptional.empty();
  }

  @Override
  public @NotNull ItemStack getContainer() {
    return container;
  }

  @Override
  public int getTanks() {
    return MAX_TANKS;
  }

  @Override
  public @NotNull FluidStack getFluidInTank(int tank) {
    if (tank >= MAX_TANKS) {
      return FluidStack.EMPTY;
    }

    return tankStacks.get(tank);
  }

  @Override
  public int getTankCapacity(int tank) {
    if (tank >= MAX_TANKS) {
      return -1;
    }

    return MAX_CAPACITY * FluidType.BUCKET_VOLUME;
  }

  @Override
  public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
    if (tank >= MAX_TANKS) {
      return false;
    }

    return tankStacks.get(tank).isEmpty() || tankStacks.get(tank).isFluidEqual(stack);
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    if (resource.isEmpty()) {
      return 0;
    }

    for (int i = 0; i < MAX_TANKS; ++i) {
      if (isFluidValid(i, resource)) {
        int amount = Math.min(resource.getAmount(), getRemainingCapacity(i));

        if (action.execute()) {
          if (tankStacks.get(i).isEmpty()) {
            tankStacks.set(i, resource.copy());
            tankStacks.get(i).setAmount(amount);
          } else {
            tankStacks.get(i).grow(amount);
          }

          updateNBT();
        }

        return amount;
      }
    }

    return 0;
  }

  private int getRemainingCapacity(int tank) {
    if (tank >= MAX_TANKS) {
      return 0;
    }

    return (MAX_CAPACITY * FluidType.BUCKET_VOLUME) - tankStacks.get(tank).getAmount();
  }

  @Override
  public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
    if (resource.isEmpty()) {
      return resource;
    }

    for (int i = 0; i < MAX_TANKS; ++i) {
      if (!tankStacks.get(i).isFluidEqual(resource)) {
        continue;
      }

      int amount = Math.min(resource.getAmount(), tankStacks.get(i).getAmount());

      FluidStack result = resource.copy();
      result.setAmount(amount);

      if (action.execute()) {
        tankStacks.get(i).shrink(amount);
        if (tankStacks.get(i).getAmount() == 0) {
          tankStacks.set(i, FluidStack.EMPTY);
        }

        updateNBT();
      }

      return result;
    }

    return FluidStack.EMPTY;
  }

  @Override
  public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
    for (int i = 0; i < MAX_TANKS; ++i) {
      if (tankStacks.get(i).isFluidEqual(FluidStack.EMPTY)) {
        continue;
      }

      int amount = Math.min(maxDrain, tankStacks.get(i).getAmount());

      FluidStack result = tankStacks.get(i).copy();
      result.setAmount(amount);

      if (action.execute()) {
        tankStacks.get(i).shrink(amount);
        if (tankStacks.get(i).getAmount() == 0) {
          tankStacks.set(i, FluidStack.EMPTY);
        }

        updateNBT();
      }

      return result;
    }

    return FluidStack.EMPTY;
  }

  private void updateNBT() {
    CompoundTag tag = new CompoundTag();
    tag.putInt(TAG_FLUID_AMOUNT, tankStacks.get(0).getAmount());
    tag.putString(
        TAG_FLUID_TYPE, ForgeRegistries.FLUIDS.getKey(tankStacks.get(0).getFluid()).toString());
    container.setTag(tag);
  }

  private void loadFromNBT() {
    CompoundTag tag = container.getTag();
    int fluidAmount = tag.getInt(TAG_FLUID_AMOUNT);
    ResourceLocation fluidType = new ResourceLocation(tag.getString(TAG_FLUID_TYPE));

    tankStacks.set(0, new FluidStack(ForgeRegistries.FLUIDS.getValue(fluidType), fluidAmount));
  }
}
