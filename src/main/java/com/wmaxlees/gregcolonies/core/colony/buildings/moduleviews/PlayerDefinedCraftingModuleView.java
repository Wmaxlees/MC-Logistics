package com.wmaxlees.gregcolonies.core.colony.buildings.moduleviews;

import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.core.client.gui.modules.WindowListRecipes;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;
import com.minecolonies.core.colony.buildings.views.AbstractBuildingView;
import com.mojang.logging.LogUtils;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.core.Network;
import com.wmaxlees.gregcolonies.core.network.messages.server.colony.building.OpenMachinistCraftingGUIMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

public class PlayerDefinedCraftingModuleView extends CraftingModuleView {
  // Directly reference a slf4j logger
  private static final Logger LOGGER = LogUtils.getLogger();

  @Override
  public void openCraftingGUI() {
    LOGGER.info("{}: Calling openCraftingGUI", Constants.MOD_ID);
    final BlockPos pos = buildingView.getPosition();
    Minecraft.getInstance()
        .player
        .openMenu((MenuProvider) Minecraft.getInstance().level.getBlockEntity(pos));
    Network.getNetwork()
        .sendToServer(
            new OpenMachinistCraftingGUIMessage(
                (AbstractBuildingView) buildingView, this.getProducer().getRuntimeID()));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public BOWindow getWindow() {
    LOGGER.info("{}: Calling getWindow", Constants.MOD_ID);
    return new WindowListRecipes(
        buildingView,
        Constants.MINECOLONIES_MOD_ID + ":gui/layouthuts/layoutlistrecipes.xml",
        this);
  }
}
