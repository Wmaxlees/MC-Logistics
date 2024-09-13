package com.wmaxlees.gregcolonies.core.colony.requestsystem.init;

import com.minecolonies.api.colony.requestsystem.manager.RequestMappingHandler;
import com.wmaxlees.gregcolonies.core.colony.requestable.CourierTankRequestable;
import com.wmaxlees.gregcolonies.core.colony.requestable.CourierTanksRequestable;
import com.wmaxlees.gregcolonies.core.colony.requestsystem.requests.CourierTankRequest;
import com.wmaxlees.gregcolonies.core.colony.requestsystem.requests.CourierTanksRequest;

public class RequestSystemInitializer {

  public static void onPostInit() {
    RequestMappingHandler.registerRequestableTypeMapping(
        CourierTankRequestable.class, CourierTankRequest.class);
    RequestMappingHandler.registerRequestableTypeMapping(
        CourierTanksRequestable.class, CourierTanksRequest.class);
  }
}
