package com.wmaxlees.gregcolonies.core.colony.requestable;

import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import net.minecraft.world.level.material.Fluid;

public interface IFluidRequestable extends IDeliverable {
  boolean fluidMatch(final Fluid fluid);

  String fluidDescName();
}
