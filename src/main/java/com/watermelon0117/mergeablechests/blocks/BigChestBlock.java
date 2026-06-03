package com.watermelon0117.mergeablechests.blocks;

import com.watermelon0117.mergeablechests.blockentities.BigChestBlockEntity;
import com.watermelon0117.mergeablechests.init.BlockEntityInit;
import com.watermelon0117.mergeablechests.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BigChestBlock extends Block implements EntityBlock{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SINGLE_SHAPE = Block.box(1, 0, 1, 15, 14, 15);

    public BigChestBlock(Properties p_41383_) {
        super(p_41383_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(FACING);
    }
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
        return this.defaultBlockState().setValue(FACING, p_49820_.getHorizontalDirection().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return BlockEntityInit.BIG_CHEST_BE.get().create(p_153215_, p_153216_);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return getMergedShape(p_60555_, p_60556_, p_60557_);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getMergedShape(state, level, pos);
    }

    private static VoxelShape getMergedShape(BlockState state, BlockGetter level, BlockPos pos) {
        if (!(level instanceof LevelAccessor levelAccessor)) {
            return SINGLE_SHAPE;
        }

        BlockPos root = BigChestBlockEntity.findRoot(levelAccessor, pos);
        BlockState rootState = levelAccessor.getBlockState(root);
        if (!BigChestBlockEntity.same(state, rootState)) {
            return SINGLE_SHAPE;
        }

        int width = BigChestBlockEntity.width(levelAccessor, root);
        int depth = BigChestBlockEntity.depth(levelAccessor, root, width);
        int height = BigChestBlockEntity.height(levelAccessor, root, width, depth);
        int dx = pos.getX() - root.getX();
        int dy = pos.getY() - root.getY();
        int dz = pos.getZ() - root.getZ();

        if (width <= 0 || depth <= 0 || height <= 0
                || dx < 0 || dx >= width
                || dy < 0 || dy >= height
                || dz < 0 || dz >= depth) {
            return SINGLE_SHAPE;
        }

        double minX = dx == 0 ? 1.0D : 0.0D;
        double maxX = dx == width - 1 ? 15.0D : 16.0D;
        double minZ = dz == 0 ? 1.0D : 0.0D;
        double maxZ = dz == depth - 1 ? 15.0D : 16.0D;
        double maxY = dy == height - 1 ? 14.0D : 16.0D;

        return Block.box(minX, 0.0D, minZ, maxX, maxY, maxZ);
    }

    private static boolean isBlocked(Level level, BlockPos rootPos) {
        if (!level.getBlockState(rootPos).is(BlockInit.BIG_CHEST.get())) {
            return false;
        }

        int width = BigChestBlockEntity.width(level, rootPos);
        int depth = BigChestBlockEntity.depth(level, rootPos, width);
        int height = BigChestBlockEntity.height(level, rootPos, width, depth);
        if (width <= 0 || depth <= 0 || height <= 0) {
            return false;
        }

        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                BlockPos above = rootPos.offset(x, height, z);
                if (level.getBlockState(above).isRedstoneConductor(level, above)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if (p_60506_.getItemInHand(p_60507_).getItem() == BlockInit.BIG_CHEST_BLOCK_ITEM.get()) {
            return InteractionResult.PASS;
        }

        if (p_60504_.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!(p_60506_ instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }

        BlockPos rootPos = BigChestBlockEntity.findRoot(p_60504_, p_60505_);
        if (!(p_60504_.getBlockEntity(rootPos) instanceof BigChestBlockEntity rootEntity)) {
            return InteractionResult.PASS;
        }

        if (isBlocked(p_60504_, rootPos)) {
            return InteractionResult.CONSUME;
        }

        List<BigChestBlockEntity> blocks = BigChestBlockEntity.collectMergedBlocks(p_60504_, rootPos);
        if (blocks.isEmpty()) {
            return InteractionResult.PASS;
        }

        NetworkHooks.openScreen(serverPlayer,
                new net.minecraft.world.SimpleMenuProvider((containerId, inventory, player) ->
                        new com.watermelon0117.mergeablechests.menu.BigChestMenu(containerId, inventory, blocks, rootEntity, rootPos),
                        Component.translatable("container.mergeablechests.big_chest")),
                buffer -> {
                    buffer.writeBlockPos(rootPos);
                    buffer.writeVarInt(blocks.size());
                });
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!oldState.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BigChestBlockEntity bigChestBlockEntity) {
                Containers.dropContents(level, pos, bigChestBlockEntity);
                bigChestBlockEntity.clearContent();
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BigChestBlockEntity bigChestBlockEntity) {
            bigChestBlockEntity.recheckOpen();
        }
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
        super.triggerEvent(state, level, pos, id, param);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(id, param);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        if (!level.isClientSide || p_153214_ != BlockEntityInit.BIG_CHEST_BE.get()) {
            return null;
        }

        return (tickerLevel, pos, state, blockEntity) ->
                BigChestBlockEntity.lidAnimateTick(tickerLevel, pos, state, (BigChestBlockEntity) blockEntity);
    }
}
