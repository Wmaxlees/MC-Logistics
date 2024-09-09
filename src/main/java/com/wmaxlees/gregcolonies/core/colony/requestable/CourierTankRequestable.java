package com.wmaxlees.gregcolonies.core.colony.requestable;

import com.google.common.reflect.TypeToken;
import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.api.util.ReflectionUtils;
import com.minecolonies.api.util.constant.TypeConstants;
import com.wmaxlees.gregcolonies.api.items.ModItems;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class CourierTankRequestable implements IDeliverable {
  private static final Set<TypeToken<?>> TYPE_TOKENS =
      ReflectionUtils.getSuperClasses(TypeToken.of(CourierTankRequestable.class)).stream()
          .filter(type -> !type.equals(TypeConstants.OBJECT))
          .collect(Collectors.toSet());

  ////// --------------------------- NBTConstants --------------------------- \\\\\\
  private static final String NBT_COUNT = "count";
  private static final String NBT_FLUID = "fluid";
  private static final String NBT_RESULT = "result";
  private static final String NBT_EXACT_AMOUNT = "exact_amount";
  ////// --------------------------- NBTConstants --------------------------- \\\\\\

  private final int count;
  private final Fluid fluid;
  private ItemStack result;
  private final boolean exactAmount;

  public CourierTankRequestable(final int count, Fluid fluid, final boolean exactAmount) {
    this.count = count;
    this.fluid = fluid;
    this.exactAmount = exactAmount;
  }

  @Override
  public boolean matches(@NotNull ItemStack stack) {
    if (stack.getItem() != ModItems.courierTank) {
      return false;
    }

    if (stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().isEmpty()) {
      return false;
    }

    IFluidHandler handler =
        stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().get();
    return handler.getFluidInTank(0).getFluid().isSame(fluid);
  }

  @Override
  public void setResult(@NotNull final ItemStack result) {
    this.result = result;
  }

  @Override
  public IDeliverable copyWithCount(final int newCount) {
    return new CourierTankRequestable(newCount, this.fluid, this.exactAmount);
  }

  @Override
  public int getCount() {
    return count;
  }

  @Override
  public int getMinimumCount() {
    return exactAmount ? count : 1;
  }

  @NotNull
  @Override
  public ItemStack getResult() {
    return result;
  }

  @Override
  public Set<TypeToken<?>> getSuperClasses() {
    return TYPE_TOKENS;
  }

  public Fluid getFluid() {
    return fluid;
  }

  public static CompoundTag serialize(
      final IFactoryController controller, final CourierTankRequestable object) {
    CompoundTag result = new CompoundTag();

    result.putInt(NBT_COUNT, object.getCount());
    result.putString(NBT_FLUID, ForgeRegistries.FLUIDS.getKey(object.getFluid()).toString());
    if (object.result != null) {
      result.put(NBT_RESULT, object.result.serializeNBT());
    }
    result.putBoolean(NBT_EXACT_AMOUNT, object.exactAmount);

    return result;
  }

  public static CourierTankRequestable deserialize(
      final IFactoryController controller, final CompoundTag tag) {
    final int count = tag.getInt(NBT_COUNT);
    final Fluid fluid =
        ForgeRegistries.FLUIDS.getValue(new ResourceLocation(tag.getString(NBT_FLUID)));
    final boolean exactAmount = tag.getBoolean(NBT_EXACT_AMOUNT);

    CourierTankRequestable requestable = new CourierTankRequestable(count, fluid, exactAmount);
    if (tag.contains(NBT_RESULT)) {
      requestable.result = ItemStackUtils.deserializeFromNBT(tag.getCompound(NBT_RESULT));
    }

    return requestable;
  }

  public static void serialize(
      final IFactoryController controller,
      final FriendlyByteBuf buffer,
      final CourierTankRequestable object) {
    buffer.writeInt(object.getCount());
    buffer.writeFluidStack(new FluidStack(object.getFluid(), 1000));
    buffer.writeItemStack(object.result != null ? object.result : ItemStack.EMPTY, false);
    buffer.writeBoolean(object.exactAmount);
  }

  public static CourierTankRequestable deserialize(
      final IFactoryController controller, final FriendlyByteBuf buffer) {
    final int count = buffer.readInt();
    final Fluid fluid = buffer.readFluidStack().getFluid();
    final ItemStack result = buffer.readItem();
    final boolean exactAmount = buffer.readBoolean();

    CourierTankRequestable requestable = new CourierTankRequestable(count, fluid, exactAmount);
    requestable.result = result;

    return requestable;
  }
}
