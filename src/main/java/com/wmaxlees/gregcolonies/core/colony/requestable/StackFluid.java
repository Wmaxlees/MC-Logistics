package com.wmaxlees.gregcolonies.core.colony.requestable;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.minecolonies.api.util.ReflectionUtils;
import com.minecolonies.api.util.constant.TypeConstants;
import com.wmaxlees.gregcolonies.api.colony.requestsystem.requestable.IDeliverableFluid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class StackFluid implements IConcreteDeliverableFluid {
  protected FluidStack targetFluid;
  protected int milliBuckets;
  protected int minMilliBuckets;
  protected FluidStack result;

  private static final Set<TypeToken<?>> TYPE_TOKENS =
      ReflectionUtils.getSuperClasses(TypeToken.of(StackFluid.class)).stream()
          .filter(type -> !type.equals(TypeConstants.OBJECT))
          .collect(Collectors.toSet());

  public StackFluid(@NotNull FluidStack stack, final int milliBuckets, final int minMilliBuckets) {
    if (stack.isEmpty()) {
      throw new IllegalArgumentException("Cannot deliver empty Fluid Stack.");
    }

    this.targetFluid = stack.copy();
    this.milliBuckets = milliBuckets;
    this.minMilliBuckets = minMilliBuckets;
    this.result = FluidStack.EMPTY;
  }

  @Override
  public boolean matches(@NotNull FluidStack fluid) {
    return fluid.getFluid().getFluidType().equals(this.targetFluid.getFluid().getFluidType());
  }

  @Override
  public int getMilliBuckets() {
    return milliBuckets;
  }

  @Override
  public int getMinimumMilliBuckets() {
    return minMilliBuckets;
  }

  @Override
  public @NotNull FluidStack getResult() {
    return result;
  }

  @Override
  public void setResult(@NotNull FluidStack result) {
    this.result = result.copy();
  }

  @Override
  public IDeliverableFluid copyWithCount(int newMilliBuckets) {
    return new StackFluid(targetFluid, newMilliBuckets, minMilliBuckets);
  }

  @Override
  public boolean canBeResolvedByBuilding() {
    return false;
  }

  @Override
  public Set<TypeToken<?>> getSuperClasses() {
    return TYPE_TOKENS;
  }

  @Override
  public List<FluidStack> getRequestedItems() {
    return Lists.newArrayList(targetFluid);
  }
}
