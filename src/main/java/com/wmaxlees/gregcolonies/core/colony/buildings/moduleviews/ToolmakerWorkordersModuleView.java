package com.wmaxlees.gregcolonies.core.colony.buildings.moduleviews;

import java.util.List;
import java.util.ArrayList;

import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModuleView;
import com.wmaxlees.gregcolonies.core.client.gui.modules.ToolmakerWorkordersModuleWindow;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class ToolmakerWorkordersModuleView extends AbstractBuildingModuleView {
    private List<ResourceLocation> workOrders = new ArrayList<>();

    @Override
    public void deserialize(@NotNull final FriendlyByteBuf buf) {
        final int sizeWorkOrders = buf.readInt();
        workOrders.clear();
        for (int i = 0; i < sizeWorkOrders; i++) {
            workOrders.add(buf.readResourceLocation());
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public BOWindow getWindow() {
        return new ToolmakerWorkordersModuleWindow(buildingView, this);
    }

    @Override
    public String getIcon() {
        return "scepter";
    }

    @Override
    public String getDesc() {
        return "com.minecolonies.gui.workerhuts.enchanter.workers";
    }
}
