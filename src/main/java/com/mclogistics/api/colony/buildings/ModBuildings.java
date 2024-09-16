package com.mclogistics.api.colony.buildings;

import static com.mclogistics.core.colony.buildings.modules.BuildingModules.*;
import static com.mclogistics.core.colony.buildings.modules.BuildingModules.TANK_SELECTOR_TOOL;

import com.mclogistics.api.blocks.ModBlocks;
import com.mclogistics.api.util.constant.Constants;
import com.mclogistics.core.colony.buildings.workerbuildings.BuildingFluidWarehouse;
import com.mclogistics.core.colony.buildings.workerbuildings.BuildingItemWarehouse;
import com.mclogistics.core.colony.buildings.workerbuildings.BuildingMachinist;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.core.colony.buildings.views.EmptyView;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModBuildings {
  public static final String MACHINIST_ID = "machinist";
  public static final String FLUID_WAREHOUSE_ID = "fluidwarehouse";
  public static final String ITEM_WAREHOUSE_ID = "itemwarehouse";
  public static RegistryObject<BuildingEntry> machinist;
  public static RegistryObject<BuildingEntry> fluidWarehouse;
  public static RegistryObject<BuildingEntry> itemWarehouse;

  public static final DeferredRegister<BuildingEntry> DEFERRED_REGISTER =
      DeferredRegister.create(
          new ResourceLocation(Constants.MINECOLONIES_MOD_ID, "buildings"), Constants.MOD_ID);

  static {
    ModBuildings.machinist =
        DEFERRED_REGISTER.register(
            ModBuildings.MACHINIST_ID,
            () ->
                new BuildingEntry.Builder()
                    .setBuildingBlock(ModBlocks.blockHutMachinist)
                    .setBuildingProducer(BuildingMachinist::new)
                    .setBuildingViewProducer(() -> BuildingMachinist.View::new)
                    .setRegistryName(
                        new ResourceLocation(Constants.MOD_ID, ModBuildings.MACHINIST_ID))
                    .addBuildingModuleProducer(MACHINIST_WORK)
                    .addBuildingModuleProducer(MACHINIST_CRAFT)
                    .addBuildingModuleProducer(MACHINIST_INPUT_TOOL)
                    .addBuildingModuleProducer(MACHINIST_OUTPUT_TOOL)
                    .createBuildingEntry());

    ModBuildings.fluidWarehouse =
        DEFERRED_REGISTER.register(
            ModBuildings.FLUID_WAREHOUSE_ID,
            () ->
                new BuildingEntry.Builder()
                    .setBuildingBlock(ModBlocks.blockHutFluidWarehouse)
                    .setBuildingProducer(BuildingFluidWarehouse::new)
                    .setBuildingViewProducer(() -> EmptyView::new)
                    .setRegistryName(
                        new ResourceLocation(Constants.MOD_ID, ModBuildings.FLUID_WAREHOUSE_ID))
                    .addBuildingModuleProducer(FLUID_WAREHOUSE_WORK)
                    .addBuildingModuleProducer(FLUID_LIST_COURIER_TANKS)
                    .addBuildingModuleProducer(INVENTORY_USER)
                    .addBuildingModuleProducer(TANK_SELECTOR_TOOL)
                    .createBuildingEntry());

    ModBuildings.itemWarehouse =
        DEFERRED_REGISTER.register(
            ModBuildings.ITEM_WAREHOUSE_ID,
            () ->
                new BuildingEntry.Builder()
                    .setBuildingBlock(ModBlocks.blockHutItemWarehouse)
                    .setBuildingProducer(BuildingItemWarehouse::new)
                    .setBuildingViewProducer(() -> EmptyView::new)
                    .setRegistryName(
                        new ResourceLocation(Constants.MOD_ID, ModBuildings.ITEM_WAREHOUSE_ID))
                    .addBuildingModuleProducer(ITEM_WAREHOUSE_WORK)
                    .addBuildingModuleProducer(ITEM_WAREHOUSE_LIST)
                    .addBuildingModuleProducer(INVENTORY_USER)
                    .addBuildingModuleProducer(CHEST_SELECTOR_TOOL)
                    .createBuildingEntry());
  }

  private ModBuildings() {
    throw new IllegalStateException(
        "Tried to initialize: ModBuildings but this is a Utility class.");
  }
}
