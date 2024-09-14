package com.mclogistics.api.items;

import com.mclogistics.api.util.constant.Constants;
import com.mclogistics.client.renderer.item.CourierTankItemRender;
import com.mclogistics.core.items.ItemCourierTank;
import com.mclogistics.core.items.ItemScepterInventorySelector;
import com.mclogistics.core.items.ItemScepterMachinist;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Constants.MOD_ID)
public final class ModItems {
  public static final DeferredRegister<Item> DEFERRED_REGISTER =
      DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

  public static final RegistryObject<Item> courierTank;
  public static final RegistryObject<Item> scepterInventoryChest;
  public static final RegistryObject<Item> scepterInventoryTank;
  public static final RegistryObject<Item> scepterMachinistInput;
  public static final RegistryObject<Item> scepterMachinistOutput;

  static {
    courierTank =
        DEFERRED_REGISTER.register(
            "courier_tank", () -> new ItemCourierTank(new Item.Properties()));
    scepterInventoryChest =
        DEFERRED_REGISTER.register(
            "scepter_inventory_chest",
            () -> new ItemScepterInventorySelector.Item(new Item.Properties()));
    scepterInventoryTank =
        DEFERRED_REGISTER.register(
            "scepter_inventory_tank",
            () -> new ItemScepterInventorySelector.Fluid(new Item.Properties()));
    scepterMachinistInput =
        DEFERRED_REGISTER.register(
            "scepter_machinist_input", () -> new ItemScepterMachinist.Input(new Item.Properties()));
    scepterMachinistOutput =
        DEFERRED_REGISTER.register(
            "scepter_machinist_output",
            () -> new ItemScepterMachinist.Output(new Item.Properties()));
  }

  @SubscribeEvent
  public static void registerItemColor(RegisterColorHandlersEvent.Item event) {
    event.register(new CourierTankItemRender(), courierTank.get());
  }

  private ModItems() {}
}
