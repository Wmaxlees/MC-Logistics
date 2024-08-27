package com.wmaxlees.gregcolonies.core.colony.buildings.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IBuildingEventsModule;
import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IPersistentModule;
import com.minecolonies.api.util.NBTUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import static com.wmaxlees.gregcolonies.api.util.constant.NbtTagConstants.*;

/**
 * The module that allows the toolmaker to select which tools to provide.
 */
public class ToolmakerWorkordersModule extends AbstractBuildingModule
        implements IBuildingModule, IPersistentModule, IBuildingEventsModule {
    private List<ResourceLocation> workorders = new ArrayList<>();

    @Override
    public void deserializeNBT(final CompoundTag compound) {
        workorders.clear();

        final ListTag filterableList = compound.getList(TAG_WORK_ORDERS_LIST, Tag.TAG_COMPOUND);
        for (int i = 0; i < filterableList.size(); ++i) {
            final CompoundTag rlTag = filterableList.getCompound(i);
            final ResourceLocation rl = new ResourceLocation(rlTag.getString(TAG_RESOURCE_NAMESPACE), rlTag.getString(TAG_RESOURCE_PATH));
            workorders.add(rl);
        }
    }

    @Override
    public void serializeNBT(final CompoundTag compound) {
        final ListTag items = new ListTag();
        for (final ResourceLocation item : workorders) {
            final CompoundTag rlTag = new CompoundTag();
            compound.putString(TAG_RESOURCE_NAMESPACE, item.getNamespace());
            compound.putString(TAG_RESOURCE_PATH, item.getPath());
            items.add(rlTag);
        }
        compound.put(TAG_WORK_ORDERS_LIST, items);
    }

    @Override
    public void serializeToView(@NotNull final FriendlyByteBuf buf) {
        buf.writeInt(workorders.size());
        for (final ResourceLocation item : workorders) {
            buf.writeResourceLocation(item);
        }
    }

    public Set<ResourceLocation> getWorkorders() {
        return new HashSet<ResourceLocation>(workorders);
    }
}
