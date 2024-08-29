package com.wmaxlees.gregcolonies.core.compatibility.jei;

import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@mezz.jei.api.JeiPlugin
public class JEIPlugin implements IModPlugin {

  @NotNull
  @Override
  public ResourceLocation getPluginUid() {
    return new ResourceLocation(Constants.MOD_ID);
  }

  @Override
  public void registerRecipes(@NotNull final IRecipeRegistration registration) {}
}
