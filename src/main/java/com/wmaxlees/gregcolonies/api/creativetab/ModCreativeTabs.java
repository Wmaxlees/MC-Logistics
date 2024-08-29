package com.wmaxlees.gregcolonies.api.creativetab;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.wmaxlees.gregcolonies.api.blocks.ModBlocks;
import com.wmaxlees.gregcolonies.api.items.ModItems;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
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
          "gchuts",
          () ->
              new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 1)
                  .icon(() -> new ItemStack(ModBlocks.blockHutToolmaker))
                  .title(Component.translatable("gregcolonies.creativetab.huts"))
                  .displayItems(
                      (config, output) -> {
                        output.accept(ModBlocks.blockHutToolmaker);
                      })
                  .build());

  public static final RegistryObject<CreativeModeTab> GENERAL =
      TAB_REG.register(
          "mctoolheads",
          () ->
              new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 1)
                  .icon(() -> new ItemStack(ModBlocks.blockHutToolmaker))
                  .title(Component.translatable("gregcolonies.creativetab.toolheads"))
                  .displayItems(
                      (config, output) -> {
                        for (Material mat : GTCEuAPI.materialManager.getRegisteredMaterials()) {
                          if (!mat.hasProperty(PropertyKey.TOOL)) {
                            continue;
                          }

                          for (GTToolType type : GTToolType.getTypes().values()) {
                            ItemLike item = ModItems.TOOL_HEAD_ITEMS.get(mat, type);
                            if (item != null) {
                              output.accept(item);
                            }
                          }
                        }
                      })
                  .build());

  /** Private constructor to hide the implicit one. */
  private ModCreativeTabs() {
    /*
     * Intentionally left empty.
     */
  }
}
