package com.wmaxlees.gregcolonies.api.util;

import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public final class FluidStackUtils {
  private FluidStackUtils() {}

  public static IFluidHandler getFluidHandlerOrNull(ICapabilityProvider entity) {
    if (entity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().isPresent()) {
      return entity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get();
    }

    if (entity.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().isPresent()) {
      return entity.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().get();
    }

    return null;
  }

  public static int tryTransferMaxAmount(@NotNull IFluidHandler from, @NotNull IFluidHandler to) {
    if (from.getFluidInTank(0) == FluidStack.EMPTY) {
      return 0;
    }

    if (!to.isFluidValid(0, from.getFluidInTank(0))) {
      return 0;
    }

    int availableSpace = to.getTankCapacity(0) - to.getFluidInTank(0).getAmount();
    if (availableSpace <= 0) {
      return 0;
    }

    int maxToTransfer = from.getFluidInTank(0).getAmount();
    int transferAmount = Math.min(maxToTransfer, availableSpace);
    if (transferAmount <= 0) {
      return 0;
    }

    FluidStack transfer = from.drain(transferAmount, IFluidHandler.FluidAction.EXECUTE);
    to.fill(transfer, IFluidHandler.FluidAction.EXECUTE);

    return transfer.getAmount();
  }
}
