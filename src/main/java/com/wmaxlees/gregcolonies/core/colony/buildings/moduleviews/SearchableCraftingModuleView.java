package com.wmaxlees.gregcolonies.core.colony.buildings.moduleviews;

import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.core.client.gui.modules.SearchableWindowListRecipes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SearchableCraftingModuleView extends CraftingModuleView {
  @Override
  @OnlyIn(Dist.CLIENT)
  public BOWindow getWindow() {
    return new SearchableWindowListRecipes(
        buildingView, Constants.MOD_ID + ":gui/layouthuts/layoutsearchablelistrecipes.xml", this);
  }
}
