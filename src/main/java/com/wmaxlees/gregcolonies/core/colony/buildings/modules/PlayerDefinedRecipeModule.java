package com.wmaxlees.gregcolonies.core.colony.buildings.modules;

import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IPersistentModule;
import net.minecraft.world.item.ItemStack;

public class PlayerDefinedRecipeModule extends AbstractBuildingModule
    implements IPersistentModule, IBuildingModule {

  public static class PlayerDefinedRecipe {
    protected ItemStack inputItem;
    protected ItemStack outputItem;
  }
}
