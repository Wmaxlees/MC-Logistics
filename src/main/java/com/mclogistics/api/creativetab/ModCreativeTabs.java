package com.mclogistics.api.creativetab;

import com.mclogistics.api.blocks.ModBlocks;
import com.mclogistics.api.items.ModItems;
import com.mclogistics.api.util.constant.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/** Class used to handle the creativeTab of minecolonies. */
@Mod.EventBusSubscriber
public class ModCreativeTabs {
  public static final DeferredRegister<CreativeModeTab> TAB_REG =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

  public static final RegistryObject<CreativeModeTab> HUTS =
      TAB_REG.register(
          "mclogisticshuts",
          () ->
              new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 1)
                  .icon(() -> new ItemStack(ModBlocks.blockHutMachinist))
                  .title(Component.translatable("mclogistics.creativetab.huts"))
                  .displayItems(
                      (config, output) -> {
                        output.accept(ModBlocks.blockHutMachinist);
                        output.accept(ModBlocks.blockHutFluidWarehouse);
                      })
                  .build());

  public static final RegistryObject<CreativeModeTab> GENERAL =
      TAB_REG.register(
          "mcfluids",
          () ->
              new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 1)
                  .icon(() -> new ItemStack(ModBlocks.blockHutMachinist))
                  .title(Component.translatable("mclogistics.creativetab.fluids"))
                  .displayItems(
                      (config, output) -> {
                        output.accept(ModItems.courierTank.get());
                      })
                  .build());

  /** Private constructor to hide the implicit one. */
  private ModCreativeTabs() {
    /*
     * Intentionally left empty.
     */
  }
}
