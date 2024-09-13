package com.wmaxlees.gregcolonies.core.network.messages.server.colony.building;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.network.messages.server.AbstractBuildingServerMessage;
import com.wmaxlees.gregcolonies.api.crafting.FluidStorage;
import com.wmaxlees.gregcolonies.core.colony.buildings.modules.FluidListModule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

public class AssignFilterableFluidMessage extends AbstractBuildingServerMessage<AbstractBuilding> {

  private boolean assign;

  private FluidStorage fluid;

  private int id;

  public AssignFilterableFluidMessage() {
    super();
  }

  public AssignFilterableFluidMessage(
      final IBuildingView building, final int id, final FluidStorage fluid, final boolean assign) {
    super(building);
    this.assign = assign;
    this.fluid = fluid;
    this.id = id;
  }

  @Override
  public void fromBytesOverride(@NotNull final FriendlyByteBuf buf) {

    this.assign = buf.readBoolean();
    this.fluid = new FluidStorage(buf.readFluidStack(), 1000);
    this.id = buf.readInt();
  }

  @Override
  public void toBytesOverride(@NotNull final FriendlyByteBuf buf) {

    buf.writeBoolean(this.assign);
    buf.writeFluidStack(this.fluid.getFluidStack());
    buf.writeInt(this.id);
  }

  @Override
  public void onExecute(
      final NetworkEvent.Context ctxIn,
      final boolean isLogicalServer,
      final IColony colony,
      final AbstractBuilding building) {
    if (building.getModule(id) instanceof FluidListModule module) {
      if (assign) {
        module.addFluid(fluid);
      } else {
        module.removeFluid(fluid);
      }
    }
  }
}
