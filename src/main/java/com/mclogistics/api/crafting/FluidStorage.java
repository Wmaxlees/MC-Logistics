package com.mclogistics.api.crafting;

import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class FluidStorage {
  private final FluidStack stack;
  private int amount;

  public FluidStorage(@NotNull final FluidStack stack, final int amount) {
    this.stack = stack;
    this.amount = amount;
  }

  /**
   * Get the itemStack from this itemStorage.
   *
   * @return the stack.
   */
  public FluidStack getFluidStack() {
    return stack;
  }

  /**
   * Getter for the quantity.
   *
   * @return the amount.
   */
  public int getAmount() {
    return this.amount;
  }

  /**
   * Setter for the quantity.
   *
   * @param amount the amount.
   */
  public void setAmount(final int amount) {
    this.amount = amount;
  }

  @Override
  public String toString() {
    final FluidStack stack = this.stack.copy();
    stack.setAmount(this.amount);
    return stack.toString();
  }

  /**
   * Is this an empty ItemStorage
   *
   * @return true if empty
   */
  public boolean isEmpty() {
    return stack.isEmpty() || amount <= 0;
  }

  /**
   * Make a copy of the ItemStorage
   *
   * @return a copy
   */
  public FluidStorage copy() {
    return new FluidStorage(stack.copy(), amount);
  }
}
