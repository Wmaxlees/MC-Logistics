package com.wmaxlees.gregcolonies.core.blocks;

import com.minecolonies.api.blocks.AbstractBlockMinecolonies;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.IColonyManager;
import com.minecolonies.api.colony.permissions.Action;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.core.tileentities.TileEntityTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockGregColoniesTank extends AbstractBlockMinecolonies<BlockGregColoniesTank>
    implements EntityBlock {
  /** The hardness this block has. */
  private static final float BLOCK_HARDNESS = 10.0F;

  /** This blocks name. */
  private static final String BLOCK_NAME = "blockgregcoloniestank";

  /** The resistance this block has. */
  private static final float RESISTANCE = Float.POSITIVE_INFINITY;

  public BlockGregColoniesTank() {
    super(
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .sound(SoundType.WOOD)
            .strength(BLOCK_HARDNESS, RESISTANCE));
    this.registerDefaultState(
        this.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
  }

  @Override
  public ResourceLocation getRegistryName() {
    return new ResourceLocation(Constants.MOD_ID, BLOCK_NAME);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(final BlockPlaceContext context) {
    if (context.getPlayer() != null) {
      return defaultBlockState()
          .setValue(
              HorizontalDirectionalBlock.FACING, context.getPlayer().getDirection().getOpposite());
    }
    return super.getStateForPlacement(context);
  }

  @Override
  public @NotNull InteractionResult use(
      final @NotNull BlockState state,
      final @NotNull Level worldIn,
      final @NotNull BlockPos pos,
      final @NotNull Player player,
      final @NotNull InteractionHand hand,
      final @NotNull BlockHitResult ray) {
    final IColony colony = IColonyManager.getInstance().getColonyByPosFromWorld(worldIn, pos);
    final BlockEntity tileEntity = worldIn.getBlockEntity(pos);

    if ((colony == null || colony.getPermissions().hasPermission(player, Action.ACCESS_HUTS))
        && tileEntity instanceof TileEntityTank tileEntityTank) {
      if (!worldIn.isClientSide) {
        tileEntityTank.onPlayerUse(player, hand);
      }

      return InteractionResult.SUCCESS;
    }
    return InteractionResult.FAIL;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(
      @NotNull final BlockPos blockPos, @NotNull final BlockState blockState) {
    return new TileEntityTank(blockPos, blockState);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(HorizontalDirectionalBlock.FACING);
  }
}
