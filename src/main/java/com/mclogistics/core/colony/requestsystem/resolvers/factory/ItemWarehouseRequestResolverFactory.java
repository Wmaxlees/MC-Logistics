package com.mclogistics.core.colony.requestsystem.resolvers.factory;

import com.google.common.reflect.TypeToken;
import com.mclogistics.api.util.constant.SerializationIdentifierConstants;
import com.mclogistics.core.colony.requestsystem.resolvers.ItemWarehouseRequestResolver;
import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.colony.requestsystem.location.ILocation;
import com.minecolonies.api.colony.requestsystem.resolver.IRequestResolverFactory;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.util.constant.TypeConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public class ItemWarehouseRequestResolverFactory
    implements IRequestResolverFactory<ItemWarehouseRequestResolver> {
  ////// --------------------------- NBTConstants --------------------------- \\\\\\
  private static final String NBT_TOKEN = "Token";
  private static final String NBT_LOCATION = "Location";

  ////// --------------------------- NBTConstants --------------------------- \\\\\\

  @NotNull
  @Override
  public TypeToken<? extends ItemWarehouseRequestResolver> getFactoryOutputType() {
    return TypeToken.of(ItemWarehouseRequestResolver.class);
  }

  @NotNull
  @Override
  public TypeToken<? extends ILocation> getFactoryInputType() {
    return TypeConstants.ILOCATION;
  }

  @NotNull
  @Override
  public ItemWarehouseRequestResolver getNewInstance(
      @NotNull final IFactoryController factoryController,
      @NotNull final ILocation iLocation,
      @NotNull final Object... context)
      throws IllegalArgumentException {
    return new ItemWarehouseRequestResolver(
        iLocation, factoryController.getNewInstance(TypeConstants.ITOKEN));
  }

  @NotNull
  @Override
  public CompoundTag serialize(
      @NotNull final IFactoryController controller,
      @NotNull final ItemWarehouseRequestResolver warehouseRequestResolver) {
    final CompoundTag compound = new CompoundTag();
    compound.put(NBT_TOKEN, controller.serialize(warehouseRequestResolver.getId()));
    compound.put(NBT_LOCATION, controller.serialize(warehouseRequestResolver.getLocation()));
    return compound;
  }

  @NotNull
  @Override
  public ItemWarehouseRequestResolver deserialize(
      @NotNull final IFactoryController controller, @NotNull final CompoundTag nbt) {
    final IToken<?> token = controller.deserialize(nbt.getCompound(NBT_TOKEN));
    final ILocation location = controller.deserialize(nbt.getCompound(NBT_LOCATION));

    return new ItemWarehouseRequestResolver(location, token);
  }

  @Override
  public void serialize(
      IFactoryController controller,
      ItemWarehouseRequestResolver input,
      FriendlyByteBuf packetBuffer) {
    controller.serialize(packetBuffer, input.getId());
    controller.serialize(packetBuffer, input.getLocation());
  }

  @Override
  public ItemWarehouseRequestResolver deserialize(
      IFactoryController controller, FriendlyByteBuf buffer) throws Throwable {
    final IToken<?> token = controller.deserialize(buffer);
    final ILocation location = controller.deserialize(buffer);

    return new ItemWarehouseRequestResolver(location, token);
  }

  @Override
  public short getSerializationId() {
    return SerializationIdentifierConstants.ITEM_WAREHOUSE_REQUEST_RESOLVER_ID;
  }
}
