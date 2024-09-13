package com.wmaxlees.gregcolonies.core.colony.requestsystem.requests;

import com.minecolonies.api.colony.requestsystem.requester.IRequester;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.core.colony.requestsystem.requests.AbstractRequest;
import com.wmaxlees.gregcolonies.core.colony.requestable.CourierTanksRequestable;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class CourierTanksRequest extends AbstractRequest<CourierTanksRequestable> {
  protected CourierTanksRequest(
      @NotNull IRequester requester,
      @NotNull IToken<?> token,
      @NotNull CourierTanksRequestable requested) {
    super(requester, token, requested);
  }

  @Override
  public @NotNull Component getShortDisplayString() {
    return Component.literal("Courier Tanks: " + getRequest().fluidDescName());
  }
}
