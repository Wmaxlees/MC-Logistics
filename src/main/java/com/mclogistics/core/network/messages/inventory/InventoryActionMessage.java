package com.mclogistics.core.network.messages.inventory;

import com.mclogistics.api.inventory.container.ContainerCraftingPlayerDefined;
import com.minecolonies.api.network.IMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class InventoryActionMessage implements IMessage {
  InventoryAction action;
  int slot;
  ItemStack slotItem;

  public InventoryActionMessage(InventoryAction action, int slot, ItemStack slotItem) {
    super();

    this.action = action;
    this.slot = slot;
    this.slotItem = slotItem;
  }

  public InventoryActionMessage(InventoryAction action, int slot) {
    this(action, slot, ItemStack.EMPTY);
  }

  public InventoryActionMessage() {
    super();
  }

  @Override
  public void toBytes(FriendlyByteBuf buf) {
    buf.writeEnum(action);
    buf.writeInt(slot);
    buf.writeItem(slotItem);
  }

  @Override
  public void fromBytes(FriendlyByteBuf buf) {
    action = buf.readEnum(InventoryAction.class);
    slot = buf.readInt();
    slotItem = buf.readItem();
  }

  @Override
  public void onExecute(NetworkEvent.Context ctxIn, boolean isLogicalServer) {
    if (!isLogicalServer) {
      return;
    }

    final ServerPlayer player = ctxIn.getSender();
    if (player == null) {
      return;
    }

    if (player.containerMenu instanceof ContainerCraftingPlayerDefined container) {
      container.doAction(action, slot, slotItem);
    }
  }
}
