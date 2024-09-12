package com.wmaxlees.gregcolonies.apiimp.initializer;

import com.wmaxlees.gregcolonies.api.items.ModItems;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.client.renderer.item.CourierTankItemRender;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Constants.MOD_ID)
public class GregColoniesModelProvider {

  @SubscribeEvent
  public static void registerItemColor(RegisterColorHandlersEvent.Item event) {
    event.register(new CourierTankItemRender(), ModItems.courierTank);
  }
}
