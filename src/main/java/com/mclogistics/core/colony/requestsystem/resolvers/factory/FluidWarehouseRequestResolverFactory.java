package com.mclogistics.core.colony.requestsystem.resolvers.factory;

import com.google.common.reflect.TypeToken;
import com.mclogistics.api.util.constant.SerializationIdentifierConstants;
import com.mclogistics.core.colony.requestsystem.resolvers.FluidWarehouseRequestResolver;
import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.colony.requestsystem.location.ILocation;
import com.minecolonies.api.colony.requestsystem.resolver.IRequestResolverFactory;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.util.constant.TypeConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public class FluidWarehouseRequestResolverFactory
    implements IRequestResolverFactory<FluidWarehouseRequestResolver> {
  ////// --------------------------- NBTConstants --------------------------- \\\\\\
  private static final String NBT_TOKEN = "Token";
  private static final String NBT_LOCATION = "Location";

  ////// --------------------------- NBTConstants --------------------------- \\\\\\

  @NotNull
  @Override
  public TypeToken<? extends FluidWarehouseRequestResolver> getFactoryOutputType() {
    return TypeToken.of(FluidWarehouseRequestResolver.class);
  }

  @NotNull
  @Override
  public TypeToken<? extends ILocation> getFactoryInputType() {
    return TypeConstants.ILOCATION;
  }

  @NotNull
  @Override
  public FluidWarehouseRequestResolver getNewInstance(
      @NotNull final IFactoryController factoryController,
      @NotNull final ILocation iLocation,
      @NotNull final Object... context)
      throws IllegalArgumentException {
    return new FluidWarehouseRequestResolver(
        iLocation, factoryController.getNewInstance(TypeConstants.ITOKEN));
  }

  @NotNull
  @Override
  public CompoundTag serialize(
      @NotNull final IFactoryController controller,
      @NotNull final FluidWarehouseRequestResolver warehouseRequestResolver) {
    final CompoundTag compound = new CompoundTag();
    compound.put(NBT_TOKEN, controller.serialize(warehouseRequestResolver.getId()));
    compound.put(NBT_LOCATION, controller.serialize(warehouseRequestResolver.getLocation()));
    return compound;
  }

  @NotNull
  @Override
  public FluidWarehouseRequestResolver deserialize(
      @NotNull final IFactoryController controller, @NotNull final CompoundTag nbt) {
    final IToken<?> token = controller.deserialize(nbt.getCompound(NBT_TOKEN));
    final ILocation location = controller.deserialize(nbt.getCompound(NBT_LOCATION));

    return new FluidWarehouseRequestResolver(location, token);
  }

  @Override
  public void serialize(
      IFactoryController controller,
      FluidWarehouseRequestResolver input,
      FriendlyByteBuf packetBuffer) {
    controller.serialize(packetBuffer, input.getId());
    controller.serialize(packetBuffer, input.getLocation());
  }

  @Override
  public FluidWarehouseRequestResolver deserialize(
      IFactoryController controller, FriendlyByteBuf buffer) throws Throwable {
    final IToken<?> token = controller.deserialize(buffer);
    final ILocation location = controller.deserialize(buffer);

    return new FluidWarehouseRequestResolver(location, token);
  }

  @Override
  public short getSerializationId() {
    return SerializationIdentifierConstants.FLUID_WAREHOUSE_REQUEST_RESOLVER_ID;
  }
}
