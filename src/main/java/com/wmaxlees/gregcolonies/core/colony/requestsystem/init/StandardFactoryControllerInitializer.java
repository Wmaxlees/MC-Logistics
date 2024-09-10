package com.wmaxlees.gregcolonies.core.colony.requestsystem.init;

import com.minecolonies.api.colony.requestsystem.StandardFactoryController;
import com.wmaxlees.gregcolonies.core.colony.requestsystem.requests.CourierTankRequestFactory;
import com.wmaxlees.gregcolonies.core.colony.requestsystem.requests.CourierTanksRequestFactory;

public final class StandardFactoryControllerInitializer {

  /** Private constructor to hide the implicit public one. */
  private StandardFactoryControllerInitializer() {}

  public static void onPreInit() {
    StandardFactoryController.getInstance().registerNewFactory(new CourierTankRequestFactory());
    StandardFactoryController.getInstance().registerNewFactory(new CourierTanksRequestFactory());
  }
}
