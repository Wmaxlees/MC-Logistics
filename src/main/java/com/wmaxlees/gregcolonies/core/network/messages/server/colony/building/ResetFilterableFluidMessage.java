package com.wmaxlees.gregcolonies.core.network.messages.server.colony.building;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.network.messages.server.AbstractBuildingServerMessage;
import com.wmaxlees.gregcolonies.core.colony.buildings.modules.FluidListModule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

public class ResetFilterableFluidMessage extends AbstractBuildingServerMessage<AbstractBuilding> {
  private int id;

  public ResetFilterableFluidMessage() {
    super();
  }

  public ResetFilterableFluidMessage(final IBuildingView building, final int id) {
    super(building);
    this.id = id;
  }

  @Override
  public void fromBytesOverride(@NotNull final FriendlyByteBuf buf) {
    this.id = buf.readInt();
  }

  @Override
  public void toBytesOverride(@NotNull final FriendlyByteBuf buf) {
    buf.writeInt(this.id);
  }

  @Override
  public void onExecute(
      final NetworkEvent.Context ctxIn,
      final boolean isLogicalServer,
      final IColony colony,
      final AbstractBuilding building) {
    if (building.getModule(id) instanceof FluidListModule module) {
      module.resetToDefaults();
    }
  }
}
