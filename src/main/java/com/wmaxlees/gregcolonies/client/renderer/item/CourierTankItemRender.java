package com.wmaxlees.gregcolonies.client.renderer.item;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

public class CourierTankItemRender implements ItemColor {
  @Override
  public int getColor(@NotNull ItemStack stack, int tintIndex) {
    if (tintIndex != 1) {
      return 0xFFFFFFFF;
    }
    if (stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().isEmpty()) {
      return 0xFFFFFFFF;
    }
    IFluidHandlerItem cap =
        stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().get();
    FluidStack fluidStack = cap.getFluidInTank(0);
    if (fluidStack.getFluid() != Fluids.EMPTY) {
      return IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack);
    }
    return 0xFFFFFFFF;
  }
}
