package com.wmaxlees.gregcolonies.core.colony.requestable;

import com.google.common.reflect.TypeToken;
import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.api.util.NBTUtils;
import com.minecolonies.api.util.ReflectionUtils;
import com.minecolonies.api.util.constant.TypeConstants;
import com.wmaxlees.gregcolonies.api.items.ModItems;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class CourierTanksRequestable implements IDeliverable {
  private static final Set<TypeToken<?>> TYPE_TOKENS =
      ReflectionUtils.getSuperClasses(TypeToken.of(CourierTanksRequestable.class)).stream()
          .filter(type -> !type.equals(TypeConstants.OBJECT))
          .collect(Collectors.toSet());

  ////// --------------------------- NBTConstants --------------------------- \\\\\\
  private static final String NBT_COUNT = "count";
  private static final String NBT_FLUIDS = "fluids";
  private static final String NBT_FLUID_RESOURCE_LOCATION = "fluid_resource_location";
  private static final String NBT_RESULT = "result";
  private static final String NBT_EXACT_AMOUNT = "exact_amount";
  ////// --------------------------- NBTConstants --------------------------- \\\\\\

  private final int count;
  private final List<Fluid> fluids;
  private ItemStack result;
  private final boolean exactAmount;

  public CourierTanksRequestable(
      final int count, final List<Fluid> fluids, final boolean exactAmount) {
    this.count = count;
    this.fluids = fluids;
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

    return fluids.contains(handler.getFluidInTank(0).getFluid());
  }

  @Override
  public void setResult(@NotNull final ItemStack result) {
    this.result = result;
  }

  @Override
  public IDeliverable copyWithCount(final int newCount) {
    return new CourierTanksRequestable(newCount, this.fluids, this.exactAmount);
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

  public List<Fluid> getFluids() {
    return fluids;
  }

  public static CompoundTag serialize(
      final IFactoryController controller, final CourierTanksRequestable object) {
    CompoundTag result = new CompoundTag();

    result.put(
        NBT_FLUIDS,
        object.fluids.stream()
            .map(
                fluid -> {
                  CompoundTag tag = new CompoundTag();
                  tag.putString(
                      NBT_FLUID_RESOURCE_LOCATION, ForgeRegistries.FLUIDS.getKey(fluid).toString());
                  return tag;
                })
            .collect(NBTUtils.toListNBT()));
    if (object.result != null) {
      result.put(NBT_RESULT, object.result.serializeNBT());
    }
    result.putBoolean(NBT_EXACT_AMOUNT, object.exactAmount);

    return result;
  }

  public static CourierTanksRequestable deserialize(
      final IFactoryController controller, final CompoundTag tag) {
    final int count = tag.getInt(NBT_COUNT);
    final List<Fluid> fluids = new ArrayList<>();
    final ListTag fluidsTagList = tag.getList(NBT_FLUIDS, Tag.TAG_COMPOUND);
    for (int i = 0; i < fluidsTagList.size(); ++i) {
      final CompoundTag fluidCompound = fluidsTagList.getCompound(i);
      final Fluid fluid =
          ForgeRegistries.FLUIDS.getValue(
              new ResourceLocation(fluidCompound.getString(NBT_FLUID_RESOURCE_LOCATION)));
      fluids.add(fluid);
    }
    final boolean exactAmount = tag.getBoolean(NBT_EXACT_AMOUNT);

    CourierTanksRequestable requestable = new CourierTanksRequestable(count, fluids, exactAmount);
    if (tag.contains(NBT_RESULT)) {
      requestable.result = ItemStackUtils.deserializeFromNBT(tag.getCompound(NBT_RESULT));
    }

    return requestable;
  }

  public static void serialize(
      final IFactoryController controller,
      final FriendlyByteBuf buffer,
      final CourierTanksRequestable object) {
    buffer.writeInt(object.getCount());
    buffer.writeInt(object.getFluids().size());
    object.fluids.forEach(fluid -> buffer.writeFluidStack(new FluidStack(fluid, 1000)));
    buffer.writeItemStack(object.result != null ? object.result : ItemStack.EMPTY, false);
    buffer.writeBoolean(object.exactAmount);
  }

  public static CourierTanksRequestable deserialize(
      final IFactoryController controller, final FriendlyByteBuf buffer) {
    final int count = buffer.readInt();
    final List<Fluid> fluids = new ArrayList<>();
    int size = buffer.readInt();
    for (int i = 0; i < size; ++i) {
      fluids.add(buffer.readFluidStack().getFluid());
    }
    final ItemStack result = buffer.readItem();
    final boolean exactAmount = buffer.readBoolean();

    CourierTanksRequestable requestable = new CourierTanksRequestable(count, fluids, exactAmount);
    requestable.result = result;

    return requestable;
  }
}
