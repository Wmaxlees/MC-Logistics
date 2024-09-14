package com.mclogistics.core.colony.requestsystem.requests;

import static com.mclogistics.api.util.constant.SerializationIdentifierConstants.COURIER_TANK_REQUEST_ID;

import com.google.common.reflect.TypeToken;
import com.mclogistics.core.colony.requestable.CourierTankRequestable;
import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.colony.requestsystem.request.IRequestFactory;
import com.minecolonies.api.colony.requestsystem.request.RequestState;
import com.minecolonies.api.colony.requestsystem.requester.IRequester;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.core.colony.requestsystem.requests.StandardRequestFactories;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public final class CourierTankRequestFactory
    implements IRequestFactory<CourierTankRequestable, CourierTankRequest> {
  @Override
  public CourierTankRequest getNewInstance(
      @NotNull CourierTankRequestable input,
      @NotNull IRequester location,
      @NotNull IToken<?> token,
      @NotNull RequestState initialState) {
    return new CourierTankRequest(location, token, input);
  }

  @Override
  public @NotNull TypeToken<? extends CourierTankRequest> getFactoryOutputType() {
    return TypeToken.of(CourierTankRequest.class);
  }

  @Override
  public @NotNull TypeToken<? extends CourierTankRequestable> getFactoryInputType() {
    return TypeToken.of(CourierTankRequestable.class);
  }

  @Override
  public short getSerializationId() {
    return COURIER_TANK_REQUEST_ID;
  }

  @Override
  public @NotNull CompoundTag serialize(
      @NotNull IFactoryController controller, @NotNull CourierTankRequest request) {
    return StandardRequestFactories.serializeToNBT(
        controller, request, CourierTankRequestable::serialize);
  }

  @Override
  public @NotNull CourierTankRequest deserialize(
      @NotNull IFactoryController controller, @NotNull CompoundTag nbt) throws Throwable {
    return StandardRequestFactories.deserializeFromNBT(
        controller,
        nbt,
        CourierTankRequestable::deserialize,
        (requested, token, requester, requestState) ->
            controller.getNewInstance(
                TypeToken.of(CourierTankRequest.class), requested, token, requester, requestState));
  }

  @Override
  public void serialize(
      @NotNull IFactoryController controller,
      @NotNull CourierTankRequest request,
      FriendlyByteBuf packetBuffer) {
    StandardRequestFactories.serializeToFriendlyByteBuf(
        controller, request, packetBuffer, CourierTankRequestable::serialize);
  }

  @Override
  public @NotNull CourierTankRequest deserialize(
      @NotNull IFactoryController controller, @NotNull FriendlyByteBuf buffer) throws Throwable {
    return StandardRequestFactories.deserializeFromFriendlyByteBuf(
        controller,
        buffer,
        CourierTankRequestable::deserialize,
        (requested, token, requester, requestState) ->
            controller.getNewInstance(
                TypeToken.of(CourierTankRequest.class), requested, token, requester, requestState));
  }
}
