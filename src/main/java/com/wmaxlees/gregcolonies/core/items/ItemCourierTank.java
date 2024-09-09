package com.wmaxlees.gregcolonies.core.items;

import com.wmaxlees.gregcolonies.core.items.capabilities.CourierTankFluidHandler;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import static com.wmaxlees.gregcolonies.api.util.constant.NbtTagConstants.*;

public class ItemCourierTank extends Item {
  public ItemCourierTank(Properties properties) {
    super(properties);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
    return new CourierTankFluidHandler(stack);
  }

  @Override
  public void appendHoverText(
      @NotNull ItemStack stack,
      @Nullable Level level,
      @NotNull List<Component> tooltip,
      @NotNull TooltipFlag flag) {
    int amount = getFluidAmount(stack);
    Fluid fluid = getFluidType(stack);
    if (fluid == Fluids.EMPTY || amount == 0) {
      tooltip.add(Component.literal("Empty"));
      return;
    }

    tooltip.add(
        Component.literal(
            "Contains: "
                + amount
                + "mb "
                + fluid.getFluidType().getDescription().getString()));
  }

  private static Fluid getFluidType(ItemStack stack) {
    CompoundTag stackTags = stack.getTag();
    if (stackTags == null) {
      return Fluids.EMPTY;
    }

    ResourceLocation fluidType = new ResourceLocation(stackTags.getString(TAG_FLUID_TYPE));
    return ForgeRegistries.FLUIDS.getValue(fluidType);
  }

  private static int getFluidAmount(ItemStack stack) {
    CompoundTag stackTags = stack.getTag();
    if (stackTags == null) {
      return 0;
    }

    return stackTags.getInt(TAG_FLUID_AMOUNT);
  }
}
