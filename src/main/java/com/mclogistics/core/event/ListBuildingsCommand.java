package com.mclogistics.core.event;

import com.minecolonies.api.MinecoloniesAPIProxy;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ListBuildingsCommand {
  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(Commands.literal("all_buildings").executes(ListBuildingsCommand::execute));
  }

  private static int execute(CommandContext<CommandSourceStack> command) {
    if (command.getSource().getEntity() instanceof Player player) {
      for (ResourceLocation item :
          MinecoloniesAPIProxy.getInstance().getBuildingRegistry().getKeys()) {
        player.sendSystemMessage(Component.literal(item.getNamespace() + ":" + item.getPath()));
      }
    }
    return Command.SINGLE_SUCCESS;
  }
}
