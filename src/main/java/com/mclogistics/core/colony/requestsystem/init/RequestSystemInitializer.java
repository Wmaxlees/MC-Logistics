package com.mclogistics.core.colony.requestsystem.init;

import com.mclogistics.core.colony.requestable.CourierTankRequestable;
import com.mclogistics.core.colony.requestable.CourierTanksRequestable;
import com.mclogistics.core.colony.requestsystem.requests.CourierTankRequest;
import com.mclogistics.core.colony.requestsystem.requests.CourierTanksRequest;
import com.minecolonies.api.colony.requestsystem.manager.RequestMappingHandler;

public class RequestSystemInitializer {

  public static void onPostInit() {
    RequestMappingHandler.registerRequestableTypeMapping(
        CourierTankRequestable.class, CourierTankRequest.class);
    RequestMappingHandler.registerRequestableTypeMapping(
        CourierTanksRequestable.class, CourierTanksRequest.class);
  }
}
