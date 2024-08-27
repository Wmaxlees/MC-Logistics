package com.wmaxlees.gregcolonies.core.colony.buildings.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IBuildingEventsModule;
import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IPersistentModule;
import com.minecolonies.api.util.NBTUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
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
public class ToolmakerToolsModule extends AbstractBuildingModule
        implements IBuildingModule, IPersistentModule, IBuildingEventsModule {
    private Map<ResourceLocation, Boolean> keepStocked = new HashMap<>();

    public static ResourceLocation[] tools = new ResourceLocation[] {
            // new ResourceLocation("gtceu:iron_axe"),
            // new ResourceLocation("gtceu:iron_hoe"),
    };

    @Override
    public void deserializeNBT(final CompoundTag compound) {
        keepStocked.clear();

        NBTUtils.streamCompound(compound.getList(TAG_KEEP_STOCKED_LIST, Tag.TAG_COMPOUND))
                .map(this::deserializeKeepStocked)
                .forEach(t -> keepStocked.put(t.getA(), t.getB()));
    }

    @Override
    public void serializeNBT(final CompoundTag compound) {
        compound.put(TAG_KEEP_STOCKED_LIST,
                keepStocked.entrySet().stream().map(this::serializeKeepStocked).collect(NBTUtils.toListNBT()));
    }

    @Override
    public void serializeToView(@NotNull final FriendlyByteBuf buf) {
        buf.writeInt(keepStocked.size());
        for (final ResourceLocation item : keepStocked.keySet()) {
            buf.writeResourceLocation(item);
        }
    }

    /**
     * Helper to deserialize a keepstocked info from nbt.
     *
     * @param nbtTagCompound the compound to deserialize from.
     * @return the resulting ItemStack/boolean tuple.
     */
    private Tuple<ResourceLocation, Boolean> deserializeKeepStocked(final CompoundTag nbtTagCompound) {
        final ResourceLocation item = new ResourceLocation(nbtTagCompound.getString(TAG_ITEM));
        final boolean ks = nbtTagCompound.getBoolean(TAG_KEEP_STOCKED);
        return new Tuple<>(item, ks);
    }

    /**
     * Serialize the keepstocked info.
     * 
     * @param entry the entry to serialize.
     * @return the resulting compound.
     */
    private CompoundTag serializeKeepStocked(final Map.Entry<ResourceLocation, Boolean> entry) {
        final CompoundTag compound = new CompoundTag();
        compound.putString(TAG_ITEM, entry.getKey().toString());
        compound.putBoolean(TAG_KEEP_STOCKED, entry.getValue());
        return compound;
    }

    public Set<ResourceLocation> getToolsToKeepStocked() {
        return keepStocked.keySet();
    }
}
