package com.mclogistics.core.network.messages.inventory;

public enum InventoryAction {
  // standard vanilla mechanics.
  PICKUP_OR_SET_DOWN,
  SPLIT_OR_PLACE_SINGLE,
  CREATIVE_DUPLICATE,
  SHIFT_CLICK,

  // extra
  MOVE_REGION,
  PICKUP_SINGLE,
  ROLL_UP,
  ROLL_DOWN,
  LACE_SINGLE,
}
