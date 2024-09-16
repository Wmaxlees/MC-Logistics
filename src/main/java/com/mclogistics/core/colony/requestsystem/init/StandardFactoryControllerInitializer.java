package com.mclogistics.core.colony.requestsystem.init;

import com.mclogistics.core.colony.requestsystem.requests.CourierTankRequestFactory;
import com.mclogistics.core.colony.requestsystem.requests.CourierTanksRequestFactory;
import com.mclogistics.core.colony.requestsystem.resolvers.factory.FluidWarehouseRequestResolverFactory;
import com.mclogistics.core.colony.requestsystem.resolvers.factory.ItemWarehouseRequestResolverFactory;
import com.minecolonies.api.colony.requestsystem.StandardFactoryController;

public final class StandardFactoryControllerInitializer {

  /** Private constructor to hide the implicit public one. */
  private StandardFactoryControllerInitializer() {}

  public static void onPreInit() {
    StandardFactoryController.getInstance().registerNewFactory(new CourierTankRequestFactory());
    StandardFactoryController.getInstance().registerNewFactory(new CourierTanksRequestFactory());
    StandardFactoryController.getInstance()
        .registerNewFactory(new FluidWarehouseRequestResolverFactory());
    StandardFactoryController.getInstance()
        .registerNewFactory(new ItemWarehouseRequestResolverFactory());
  }
}
