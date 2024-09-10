package com.wmaxlees.gregcolonies.core.colony.requestsystem.requests;

import com.minecolonies.api.colony.requestsystem.requester.IRequester;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.core.colony.requestsystem.requests.AbstractRequest;
import com.wmaxlees.gregcolonies.core.colony.requestable.CourierTanksRequestable;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

public class CourierTanksRequest extends AbstractRequest<CourierTanksRequestable> {
  List<Fluid> fluids;

  protected CourierTanksRequest(
      @NotNull IRequester requester,
      @NotNull IToken<?> token,
      @NotNull CourierTanksRequestable requested) {
    super(requester, token, requested);

    this.fluids = requested.getFluids();
  }

  @Override
  public @NotNull Component getShortDisplayString() {
    return Component.literal("Courier Tanks: Multiple");
  }
}
