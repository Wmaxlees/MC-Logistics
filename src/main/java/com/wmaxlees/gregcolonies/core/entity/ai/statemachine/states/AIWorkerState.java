package com.wmaxlees.gregcolonies.core.entity.ai.statemachine.states;

import com.minecolonies.api.entity.ai.statemachine.states.IAIState;

public enum AIWorkerState implements IAIState {
  /*
  ###TOOLMAKER###
   */
  /** this is the state when the crafter is creating a tool. */
  CRAFT_TOOL(true),

  /*
  ###MACHINIST###
   */
  /** For inserting items */
  INSERT_ITEMS(true),
  /** For retrieving the results. */
  RETRIEVE_RESULTS(true);

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
