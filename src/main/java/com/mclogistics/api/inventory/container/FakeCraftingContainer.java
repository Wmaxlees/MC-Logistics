package com.mclogistics.api.inventory.container;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;

public class FakeCraftingContainer extends TransientCraftingContainer {
  public FakeCraftingContainer(AbstractContainerMenu abstractContainerMenu, int i, int j) {
    super(abstractContainerMenu, i, j);
  }

  @Override
  public int getMaxStackSize() {
    return 99;
  }
}
