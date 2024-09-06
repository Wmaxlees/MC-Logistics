package com.wmaxlees.gregcolonies.core.colony.requestable;

import com.wmaxlees.gregcolonies.api.colony.requestsystem.requestable.IDeliverableFluid;
import java.util.List;
import net.minecraftforge.fluids.FluidStack;

public interface IConcreteDeliverableFluid extends IDeliverableFluid {
  /**
   * Get a list of concrete fluids requested by this deliverable
   *
   * @return
   */
  List<FluidStack> getRequestedItems();
}
