package com.wmaxlees.gregcolonies.apiimp.initializer;

import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityInitializer {
  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
      DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MOD_ID);

  static {
  }
}
