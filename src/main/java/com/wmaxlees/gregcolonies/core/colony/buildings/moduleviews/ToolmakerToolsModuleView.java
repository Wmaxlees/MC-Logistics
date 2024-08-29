package com.wmaxlees.gregcolonies.core.colony.buildings.moduleviews;

import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModuleView;
import com.wmaxlees.gregcolonies.core.client.gui.modules.ToolmakerToolsModuleWindow;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class ToolmakerToolsModuleView extends AbstractBuildingModuleView {
  private List<ResourceLocation> keepStocked = new ArrayList<>();

  @Override
  public void deserialize(@NotNull final FriendlyByteBuf buf) {
    final int size = buf.readInt();
    keepStocked.clear();
    for (int i = 0; i < size; i++) {
      keepStocked.add(buf.readResourceLocation());
    }
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public BOWindow getWindow() {
    return new ToolmakerToolsModuleWindow(buildingView, this);
  }

  @Override
  public String getIcon() {
    return "info";
  }

  @Override
  public String getDesc() {
    return "com.minecolonies.gui.workerhuts.enchanter.workers";
  }
}
