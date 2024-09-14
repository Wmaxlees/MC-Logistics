package com.mclogistics.core.colony.buildings.moduleviews;

import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModuleView;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public class InventoryUserModuleView extends AbstractBuildingModuleView {

  List<BlockPos> chests = new ArrayList<>();
  List<BlockPos> tanks = new ArrayList<>();

  @Override
  public void deserialize(@NotNull FriendlyByteBuf buf) {
    int chestSize = buf.readInt();
    for (int i = 0; i < chestSize; i++) {
      chests.add(buf.readBlockPos());
    }

    int tankSize = buf.readInt();
    for (int i = 0; i < tankSize; i++) {
      tanks.add(buf.readBlockPos());
    }
  }

  @Override
  public BOWindow getWindow() {
    return null;
  }

  @Override
  public String getIcon() {
    return "";
  }

  @Override
  public String getDesc() {
    return "";
  }

  public List<BlockPos> getTanks() {
    return tanks;
  }

  public List<BlockPos> getChests() {
    return chests;
  }
}
