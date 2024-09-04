package com.wmaxlees.gregcolonies.core.network.messages.server.colony.building;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.core.colony.buildings.modules.AbstractCraftingBuildingModule;
import com.minecolonies.core.colony.buildings.views.AbstractBuildingView;
import com.minecolonies.core.network.messages.server.AbstractBuildingServerMessage;
import com.mojang.logging.LogUtils;
import com.wmaxlees.gregcolonies.api.inventory.container.ContainerCraftingPlayerDefined;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class OpenMachinistCraftingGUIMessage extends AbstractBuildingServerMessage<IBuilding> {
  // Directly reference a slf4j logger
  private static final Logger LOGGER = LogUtils.getLogger();

  /** The type of container. */
  private int id;

  /** Empty public constructor. */
  public OpenMachinistCraftingGUIMessage() {
    super();
  }

  /**
   * Creates an open inventory message for a building.
   *
   * @param id the string id.
   * @param building {@link AbstractBuildingView}
   */
  public OpenMachinistCraftingGUIMessage(
      @NotNull final AbstractBuildingView building, final int id) {
    super(building);
    this.id = id;
  }

  @Override
  public void fromBytesOverride(@NotNull final FriendlyByteBuf buf) {
    this.id = buf.readInt();
  }

  @Override
  public void toBytesOverride(@NotNull final FriendlyByteBuf buf) {
    buf.writeInt(id);
  }

  @Override
  protected void onExecute(
      final NetworkEvent.Context ctxIn,
      final boolean isLogicalServer,
      final IColony colony,
      final IBuilding building) {
    LOGGER.info("{}: Loading MachinistCraftingGUI.", Constants.MOD_ID);

    final ServerPlayer player = ctxIn.getSender();
    if (player == null) {
      return;
    }

    if (building.getModule(id) instanceof AbstractCraftingBuildingModule module) {
      LOGGER.info(
          "{}: OpenMachinistCraftingGUIMessage was called and is calling OpenScreen.",
          Constants.MOD_ID);
      net.minecraftforge.network.NetworkHooks.openScreen(
          player,
          new MenuProvider() {
            @NotNull
            @Override
            public Component getDisplayName() {
              return Component.literal("Crafting GUI");
            }

            @NotNull
            @Override
            public AbstractContainerMenu createMenu(
                final int id, @NotNull final Inventory inv, @NotNull final Player player) {
              return new ContainerCraftingPlayerDefined(
                  id, inv, building.getID(), module.getProducer().getRuntimeID());
            }
          },
          buffer ->
              new FriendlyByteBuf(
                  buffer
                      .writeBlockPos(building.getID())
                      .writeInt(module.getProducer().getRuntimeID())));
    }
  }
}
