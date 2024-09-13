package com.wmaxlees.gregcolonies.core.colony.requestsystem.requests;

import static com.wmaxlees.gregcolonies.api.util.constant.SerializationIdentifierConstants.COURIER_TANKS_REQUEST_ID;

import com.google.common.reflect.TypeToken;
import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.colony.requestsystem.request.IRequestFactory;
import com.minecolonies.api.colony.requestsystem.request.RequestState;
import com.minecolonies.api.colony.requestsystem.requester.IRequester;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.core.colony.requestsystem.requests.StandardRequestFactories;
import com.wmaxlees.gregcolonies.core.colony.requestable.CourierTanksRequestable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public final class CourierTanksRequestFactory
    implements IRequestFactory<CourierTanksRequestable, CourierTanksRequest> {
  @Override
  public CourierTanksRequest getNewInstance(
      @NotNull CourierTanksRequestable input,
      @NotNull IRequester location,
      @NotNull IToken<?> token,
      @NotNull RequestState initialState) {
    return new CourierTanksRequest(location, token, input);
  }

  @Override
  public @NotNull TypeToken<? extends CourierTanksRequest> getFactoryOutputType() {
    return TypeToken.of(CourierTanksRequest.class);
  }

  @Override
  public @NotNull TypeToken<? extends CourierTanksRequestable> getFactoryInputType() {
    return TypeToken.of(CourierTanksRequestable.class);
  }

  @Override
  public short getSerializationId() {
    return COURIER_TANKS_REQUEST_ID;
  }

  @Override
  public @NotNull CompoundTag serialize(
      @NotNull IFactoryController controller, @NotNull CourierTanksRequest request) {
    return StandardRequestFactories.serializeToNBT(
        controller, request, CourierTanksRequestable::serialize);
  }

  @Override
  public @NotNull CourierTanksRequest deserialize(
      @NotNull IFactoryController controller, @NotNull CompoundTag nbt) throws Throwable {
    return StandardRequestFactories.deserializeFromNBT(
        controller,
        nbt,
        CourierTanksRequestable::deserialize,
        (requested, token, requester, requestState) ->
            controller.getNewInstance(
                TypeToken.of(CourierTanksRequest.class),
                requested,
                token,
                requester,
                requestState));
  }

  @Override
  public void serialize(
      @NotNull IFactoryController controller,
      @NotNull CourierTanksRequest request,
      FriendlyByteBuf packetBuffer) {
    StandardRequestFactories.serializeToFriendlyByteBuf(
        controller, request, packetBuffer, CourierTanksRequestable::serialize);
  }

  @Override
  public @NotNull CourierTanksRequest deserialize(
      @NotNull IFactoryController controller, @NotNull FriendlyByteBuf buffer) throws Throwable {
    return StandardRequestFactories.deserializeFromFriendlyByteBuf(
        controller,
        buffer,
        CourierTanksRequestable::deserialize,
        (requested, token, requester, requestState) ->
            controller.getNewInstance(
                TypeToken.of(CourierTanksRequest.class),
                requested,
                token,
                requester,
                requestState));
  }
}
