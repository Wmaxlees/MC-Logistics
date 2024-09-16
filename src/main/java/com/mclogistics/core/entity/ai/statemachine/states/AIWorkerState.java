package com.mclogistics.core.entity.ai.statemachine.states;

import com.minecolonies.api.entity.ai.statemachine.states.IAIState;

public enum AIWorkerState implements IAIState {
  /*
  ###MACHINIST###
   */
  /** For inserting items */
  INSERT_ITEMS(true),
  /** For retrieving the results. */
  RETRIEVE_RESULTS(true),

  /*
  ###FLUID WAREHOUSE MANAGER
   */
  /** Moving to a tank to empty. */
  MOVE_TO_TANK_TO_EMPTY(true),
  /** Emptying the courier tank. */
  EMPTY_TANK(true),

  /*
  ###ITEM WAREHOUSE MANAGER
   */
  STORE_ITEMS(true),
  COLLECT_ITEMS_TO_STORE(true);

  /** Is it okay to eat. */
  private boolean isOkayToEat;

  /**
   * Create a new one.
   *
   * @param okayToEat if okay.
   */
  AIWorkerState(final boolean okayToEat) {
    this.isOkayToEat = okayToEat;
  }

  /**
   * Method to check if it is okay.
   *
   * @return true if so.
   */
  public boolean isOkayToEat() {
    return isOkayToEat;
  }
}
