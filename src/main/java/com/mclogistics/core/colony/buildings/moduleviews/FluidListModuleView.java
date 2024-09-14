package com.mclogistics.core.colony.buildings.moduleviews;

import com.ldtteam.blockui.views.BOWindow;
import com.mclogistics.api.crafting.FluidStorage;
import com.mclogistics.api.util.constant.Constants;
import com.mclogistics.core.Network;
import com.mclogistics.core.client.gui.modules.WindowFluidList;
import com.mclogistics.core.network.messages.server.colony.building.AssignFilterableFluidMessage;
import com.mclogistics.core.network.messages.server.colony.building.ResetFilterableFluidMessage;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModuleView;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class FluidListModuleView extends AbstractBuildingModuleView {
  private final List<FluidStorage> listsOfFluids = new ArrayList<>();

  private final String id;

  private final Function<IBuildingView, Set<FluidStorage>> allFluids;

  private final boolean inverted;

  private final String desc;

  public FluidListModuleView(
      final String id,
      final String desc,
      final boolean inverted,
      final Function<IBuildingView, Set<FluidStorage>> allFluids) {
    super();
    this.id = id;
    this.desc = desc;
    this.inverted = inverted;
    this.allFluids = allFluids;
  }

  public void addFluid(final FluidStorage fluid) {
    Network.getNetwork()
        .sendToServer(
            new AssignFilterableFluidMessage(
                this.buildingView, getProducer().getRuntimeID(), fluid, true));
    listsOfFluids.add(fluid);
  }

  public boolean isAllowedFluid(final FluidStorage fluid) {
    return listsOfFluids.contains(fluid);
  }

  public int getSize() {
    return listsOfFluids.size();
  }

  public void removeFluid(final FluidStorage fluid) {
    Network.getNetwork()
        .sendToServer(
            new AssignFilterableFluidMessage(
                this.buildingView, getProducer().getRuntimeID(), fluid, false));
    listsOfFluids.remove(fluid);
  }

  public String getId() {
    return id;
  }

  public Function<IBuildingView, Set<FluidStorage>> getAllFluids() {
    return allFluids;
  }

  public boolean isInverted() {
    return inverted;
  }

  public void clearFluids() {
    Network.getNetwork()
        .sendToServer(
            new ResetFilterableFluidMessage(this.buildingView, getProducer().getRuntimeID()));
    listsOfFluids.clear();
  }

  @Override
  public String getDesc() {
    return desc;
  }

  @Override
  public void deserialize(@NotNull final FriendlyByteBuf buf) {
    listsOfFluids.clear();
    final int size = buf.readInt();

    for (int j = 0; j < size; j++) {
      listsOfFluids.add(new FluidStorage(buf.readFluidStack(), 1000));
    }
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public BOWindow getWindow() {
    return new WindowFluidList(
        Constants.MINECOLONIES_MOD_ID + ":gui/layouthuts/layoutfilterablelist.xml",
        buildingView,
        this);
  }

  @Override
  public String getIcon() {
    return this.getId();
  }
}
