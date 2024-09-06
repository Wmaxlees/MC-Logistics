package com.wmaxlees.gregcolonies.core.colony.buildings.moduleviews;

import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;
import com.mojang.logging.LogUtils;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.core.client.gui.modules.WindowSearchableListRecipes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

public class SearchableCraftingModuleView extends CraftingModuleView {
  // Directly reference a slf4j logger
  private static final Logger LOGGER = LogUtils.getLogger();

  @Override
  @OnlyIn(Dist.CLIENT)
  public BOWindow getWindow() {
    LOGGER.info("{}: Loading the correct GUI xml", Constants.MOD_ID);
    return new WindowSearchableListRecipes(
        buildingView, Constants.MOD_ID + ":gui/layouthuts/layoutsearchablelistrecipes.xml", this);
  }
}
