package com.mclogistics.api.blocks;

import com.mclogistics.api.util.constant.Constants;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractBlockHut<B extends com.minecolonies.api.blocks.AbstractBlockHut<B>>
    extends com.minecolonies.api.blocks.AbstractBlockHut<B> {
  @Override
  public ResourceLocation getRegistryName() {
    return new ResourceLocation(Constants.MOD_ID, getHutName());
  }
}
