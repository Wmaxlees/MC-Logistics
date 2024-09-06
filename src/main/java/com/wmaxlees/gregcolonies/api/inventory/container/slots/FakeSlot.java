package com.wmaxlees.gregcolonies.api.inventory.container.slots;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FakeSlot extends Slot {
  public FakeSlot(Container container, int i, int j, int k) {
    super(container, i, j, k);
  }

  @Override
  public void onTake(final @NotNull Player playerIn, final @NotNull ItemStack stack) {
    /* Intentionally blank */
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return false;
  }

  @Override
  public void set(ItemStack stack) {
    if (!stack.isEmpty()) {
      stack = stack.copy();
    }

    super.set(stack);
  }

  @Override
  public boolean mayPickup(Player player) {
    return false;
  }

  @Override
  public int getMaxStackSize() {
    return 99;
  }
}
