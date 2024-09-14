package com.mclogistics.core.colony.buildings.moduleviews;

import com.ldtteam.blockui.views.BOWindow;
import com.mclogistics.api.util.constant.Constants;
import com.mclogistics.core.Network;
import com.mclogistics.core.network.messages.server.colony.building.OpenMachinistCraftingGUIMessage;
import com.minecolonies.core.client.gui.modules.WindowListRecipes;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;
import com.minecolonies.core.colony.buildings.views.AbstractBuildingView;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PlayerDefinedCraftingModuleView extends CraftingModuleView {
  @Override
  public void openCraftingGUI() {
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
    return new WindowListRecipes(
        buildingView,
        Constants.MINECOLONIES_MOD_ID + ":gui/layouthuts/layoutlistrecipes.xml",
        this);
  }
}
