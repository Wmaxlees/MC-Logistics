package com.wmaxlees.gregcolonies.api.colony.requestsystem.requestable;

import com.minecolonies.api.colony.requestsystem.requestable.IRetryable;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public interface IDeliverableFluid extends IRetryable {
  boolean matches(@NotNull FluidStack fluid);

  int getMilliBuckets();

  int getMinimumMilliBuckets();

  @NotNull
  FluidStack getResult();

  void setResult(@NotNull FluidStack result);

  IDeliverableFluid copyWithCount(int newMilliBuckets);

  boolean canBeResolvedByBuilding();
}
