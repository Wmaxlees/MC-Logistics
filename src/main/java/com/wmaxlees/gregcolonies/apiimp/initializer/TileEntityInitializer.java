package com.wmaxlees.gregcolonies.apiimp.initializer;

import com.wmaxlees.gregcolonies.api.blocks.ModBlocks;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.core.tileentities.GregColoniesTileEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityInitializer {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
            .create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MOD_ID);

    static {
        // GregColoniesTileEntities.TOOLMAKER = BLOCK_ENTITIES.register("toolmaker",
        //         () -> BlockEntityType.Builder.of(TileEntityToolmaker::new, ModBlocks.blockHutToolmaker).build(null));
    }
}
