package com.mclogistics.core.colony.buildings.modules;

import com.google.common.collect.ImmutableList;
import com.mclogistics.api.crafting.FluidStorage;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IPersistentModule;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class FluidListModule extends AbstractBuildingModule implements IPersistentModule {
  /** Tag to store the fluid list. */
  private static final String TAG_FLUIDLIST = "fluidList";

  /** List of allowed fluids. */
  private ImmutableList<FluidStorage> fluidsAllowed = ImmutableList.of();

  /** List of default allowed fluids. */
  private ImmutableList<FluidStorage> defaultValues = ImmutableList.of();

  /** Unique id of this module. */
  private final String id;

  /**
   * Construct a new grouped fluidlist module with the unique list identifier.
   *
   * @param id the list id.
   */
  public FluidListModule(final String id) {
    super();
    this.id = id;
  }

  /**
   * Construct a new grouped fluidlist module with the unique list identifier and default values.
   *
   * @param id the list id.
   * @param defaultStacks the default values.
   */
  public FluidListModule(final String id, final FluidStorage... defaultStacks) {
    this(id);
    defaultValues = ImmutableList.copyOf(defaultStacks);
  }

  @Override
  public void deserializeNBT(CompoundTag compound) {
    if (compound.contains(id)) {
      compound = compound.getCompound(id);
    }

    final List<FluidStorage> allowedFluids = new ArrayList<>();
    final ListTag filterableList = compound.getList(TAG_FLUIDLIST, Tag.TAG_COMPOUND);
    for (int i = 0; i < filterableList.size(); ++i) {
      ;
      allowedFluids.add(
          new FluidStorage(FluidStack.loadFluidStackFromNBT(filterableList.getCompound(i)), 1000));
    }

    this.fluidsAllowed = ImmutableList.copyOf(allowedFluids);
  }

  @Override
  public void serializeNBT(final CompoundTag compound) {
    @NotNull final ListTag filteredFluids = new ListTag();
    for (@NotNull final FluidStorage fluid : fluidsAllowed) {
      @NotNull final CompoundTag fluidCompound = new CompoundTag();
      fluid.getFluidStack().writeToNBT(fluidCompound);
      filteredFluids.add(fluidCompound);
    }
    compound.put(TAG_FLUIDLIST, filteredFluids);
  }

  public void addFluid(final FluidStorage fluid) {
    if (!fluidsAllowed.contains(fluid)) {
      this.fluidsAllowed =
          ImmutableList.<FluidStorage>builder().addAll(fluidsAllowed).add(fluid).build();
      markDirty();
    }
  }

  public boolean isFluidInList(final FluidStorage fluid) {
    return fluidsAllowed.contains(fluid);
  }

  public void removeFluid(final FluidStorage fluid) {
    final List<FluidStorage> allowedFluids = new ArrayList<>(fluidsAllowed);
    allowedFluids.remove(fluid);
    this.fluidsAllowed = ImmutableList.copyOf(allowedFluids);
    markDirty();
  }

  public ImmutableList<FluidStorage> getList() {
    return fluidsAllowed;
  }

  public String getListIdentifier() {
    return this.id;
  }

  public void clearFluids() {
    fluidsAllowed = ImmutableList.of();
    markDirty();
  }

  public void resetToDefaults() {
    this.fluidsAllowed = ImmutableList.copyOf(defaultValues);
  }

  @Override
  public void serializeToView(@NotNull final FriendlyByteBuf buf) {
    buf.writeInt(fluidsAllowed.size());
    for (final FluidStorage fluid : fluidsAllowed) {
      buf.writeFluidStack(fluid.getFluidStack());
    }
  }

  public String getId() {
    return this.id;
  }
}
