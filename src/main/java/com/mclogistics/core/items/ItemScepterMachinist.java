package com.mclogistics.core.items;

import static com.minecolonies.api.util.constant.NbtTagConstants.TAG_ID;
import static com.minecolonies.api.util.constant.NbtTagConstants.TAG_POS;

import com.mclogistics.core.colony.buildings.workerbuildings.BuildingMachinist;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.IColonyManager;
import com.minecolonies.api.colony.IColonyView;
import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.items.IBlockOverlayItem;
import com.minecolonies.api.util.BlockPosUtil;
import com.minecolonies.core.Network;
import com.minecolonies.core.items.AbstractItemMinecolonies;
import com.minecolonies.core.network.messages.client.colony.ColonyViewBuildingViewMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;

public abstract class ItemScepterMachinist extends AbstractItemMinecolonies
    implements IBlockOverlayItem {
  private static final int HUT_OVERLAY = 0xFFFF0000;
  private static final int INPUT_OVERLAY = 0xFFFFFF00;
  private static final int OUTPUT_OVERLAY = 0xFF00FF00;

  public ItemScepterMachinist(final String name, final Properties properties) {
    super(name, properties.stacksTo(1));
  }

  @Override
  public InteractionResult useOn(final UseOnContext useContext) {
    // if server world, do nothing
    if (useContext.getLevel().isClientSide) {
      return InteractionResult.FAIL;
    }

    final Player player = useContext.getPlayer();

    final ItemStack scepter = useContext.getPlayer().getItemInHand(useContext.getHand());
    final CompoundTag compound = scepter.getOrCreateTag();

    final IColony colony =
        IColonyManager.getInstance()
            .getColonyByWorld(compound.getInt(TAG_ID), useContext.getLevel());
    final BlockPos hutPos = BlockPosUtil.read(compound, TAG_POS);
    final IBuilding hut = colony.getBuildingManager().getBuilding(hutPos);
    final BuildingMachinist building = (BuildingMachinist) hut;

    BlockEntity blockEntity = useContext.getLevel().getBlockEntity(useContext.getClickedPos());
    if (blockEntity == null) {
      player.getInventory().removeItemNoUpdate(player.getInventory().selected);
      return super.useOn(useContext);
    }

    if (blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
      handleBlockAssign(building, useContext.getClickedPos());
      Network.getNetwork()
          .sendToPlayer(new ColonyViewBuildingViewMessage(building), (ServerPlayer) player);
    }

    return super.useOn(useContext);
  }

  @NotNull
  @Override
  public List<OverlayBox> getOverlayBoxes(
      @NotNull final Level world, @NotNull final Player player, @NotNull ItemStack stack) {
    final CompoundTag compound = stack.getOrCreateTag();
    final IColonyView colony =
        IColonyManager.getInstance().getColonyView(compound.getInt(TAG_ID), world.dimension());
    final BlockPos pos = BlockPosUtil.read(compound, TAG_POS);

    if (colony != null && colony.getBuilding(pos) instanceof final BuildingMachinist.View hut) {
      final List<OverlayBox> overlays = new ArrayList<>();

      overlays.add(new OverlayBox(new AABB(pos), HUT_OVERLAY, 0.02f, true));

      overlays.add(new OverlayBox(new AABB(hut.getInputLocation()), INPUT_OVERLAY, 0.02f, true));
      overlays.add(new OverlayBox(new AABB(hut.getOutputLocation()), OUTPUT_OVERLAY, 0.02f, true));

      return overlays;
    }

    return Collections.emptyList();
  }

  protected abstract void handleBlockAssign(BuildingMachinist building, final BlockPos blockPos);

  public static class Input extends ItemScepterMachinist {
    public Input(final Properties properties) {
      super("sceptermachinistinput", properties);
    }

    @Override
    protected void handleBlockAssign(BuildingMachinist building, BlockPos blockPos) {
      building.setInputLocation(blockPos);
    }
  }

  public static class Output extends ItemScepterMachinist {
    public Output(final Properties properties) {
      super("sceptermachinistoutput", properties);
    }

    @Override
    protected void handleBlockAssign(BuildingMachinist building, BlockPos blockPos) {
      building.setOutputLocation(blockPos);
    }
  }
}
