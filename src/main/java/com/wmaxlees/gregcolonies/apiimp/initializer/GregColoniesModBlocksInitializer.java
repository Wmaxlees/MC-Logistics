package com.wmaxlees.gregcolonies.apiimp.initializer;

import com.wmaxlees.gregcolonies.api.blocks.ModBlocks;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.core.blocks.huts.BlockHutFluidWarehouse;
import com.wmaxlees.gregcolonies.core.blocks.huts.BlockHutMachinist;
import com.wmaxlees.gregcolonies.core.blocks.huts.BlockHutToolPartSmith;
import com.wmaxlees.gregcolonies.core.blocks.huts.BlockHutToolmaker;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GregColoniesModBlocksInitializer {
  private GregColoniesModBlocksInitializer() {
    throw new IllegalStateException(
        "Tried to initialize: ModBlockInitializer but this is a Utility class.");
  }

  @SubscribeEvent
  public static void registerBlocks(RegisterEvent event) {
    if (event.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS)) {
      init(event.getForgeRegistry());
    }
  }

  /**
   * Initializes {@link ModBlocks} with the block instances.
   *
   * @param registry The registry to register the new blocks.
   */
  public static void init(final IForgeRegistry<Block> registry) {
    ModBlocks.blockHutToolPartSmith = new BlockHutToolPartSmith().registerBlock(registry);
    ModBlocks.blockHutToolmaker = new BlockHutToolmaker().registerBlock(registry);
    ModBlocks.blockHutMachinist = new BlockHutMachinist().registerBlock(registry);
    ModBlocks.blockHutFluidWarehouse = new BlockHutFluidWarehouse().registerBlock(registry);
  }

  @SubscribeEvent
  public static void registerItems(RegisterEvent event) {
    if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
      registerBlockItem(event.getForgeRegistry());
    }
  }

  /**
   * Initializes the registry with the relevant item produced by the relevant blocks.
   *
   * @param registry The item registry to add the items too.
   */
  public static void registerBlockItem(final IForgeRegistry<Item> registry) {
    ModBlocks.blockHutToolPartSmith.registerBlockItem(registry, new Item.Properties());
    ModBlocks.blockHutToolmaker.registerBlockItem(registry, new Item.Properties());
    ModBlocks.blockHutMachinist.registerBlockItem(registry, new Item.Properties());
    ModBlocks.blockHutFluidWarehouse.registerBlockItem(registry, new Item.Properties());
  }
}
