package com.wmaxlees.gregcolonies.apiimp.initializer;

import com.wmaxlees.gregcolonies.api.blocks.ModBlocks;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.core.tileentities.GregColoniesTileEntities;
import com.wmaxlees.gregcolonies.core.tileentities.TileEntityTank;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityInitializer {
  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
      DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MOD_ID);

  static {
    GregColoniesTileEntities.TANK =
        BLOCK_ENTITIES.register(
            "tank",
            () -> BlockEntityType.Builder.of(TileEntityTank::new, ModBlocks.blockTank).build(null));
  }
}
