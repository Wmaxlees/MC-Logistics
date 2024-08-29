package com.wmaxlees.gregcolonies.api.blocks;

import com.minecolonies.api.blocks.AbstractBlockHut;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractGregColoniesBlockHut<B extends AbstractBlockHut<B>>
    extends AbstractBlockHut<B> {
  /**
   * Get the registry name from the block hut.
   *
   * @return the key.
   */
  @Override
  public ResourceLocation getRegistryName() {
    return new ResourceLocation(Constants.MOD_ID, getHutName());
  }
}
