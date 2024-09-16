package com.mclogistics.api.blocks;

import com.mclogistics.api.util.constant.Constants;
import com.mclogistics.core.blocks.huts.BlockHutFluidWarehouse;
import com.mclogistics.core.blocks.huts.BlockHutItemWarehouse;
import com.mclogistics.core.blocks.huts.BlockHutMachinist;
import com.minecolonies.api.blocks.AbstractBlockHut;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModBlocks {
  public static AbstractBlockHut<? extends AbstractBlockHut<?>> blockHutMachinist;
  public static AbstractBlockHut<? extends AbstractBlockHut<?>> blockHutFluidWarehouse;
  public static AbstractBlockHut<? extends AbstractBlockHut<?>> blockHutItemWarehouse;

  private ModBlocks() {
    throw new IllegalStateException(
        "Tried to initialize: ModBlockInitializer but this is a Utility class.");
  }

  @SubscribeEvent
  public static void registerBlocks(RegisterEvent event) {
    if (event.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS)) {
      blockHutMachinist = new BlockHutMachinist().registerBlock(event.getForgeRegistry());
      blockHutFluidWarehouse = new BlockHutFluidWarehouse().registerBlock(event.getForgeRegistry());
      blockHutItemWarehouse = new BlockHutItemWarehouse().registerBlock(event.getForgeRegistry());
    }
  }

  @SubscribeEvent
  public static void registerItems(RegisterEvent event) {
    if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
      blockHutMachinist.registerBlockItem(event.getForgeRegistry(), new Item.Properties());
      blockHutFluidWarehouse.registerBlockItem(event.getForgeRegistry(), new Item.Properties());
      blockHutItemWarehouse.registerBlockItem(event.getForgeRegistry(), new Item.Properties());
    }
  }
}
