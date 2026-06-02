package com.watermelon0117.mergeablechests.blocks;

import com.watermelon0117.mergeablechests.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class IntersectBlock extends Block implements EntityBlock {
    public IntersectBlock(Properties p_41383_) {
        super(p_41383_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return BlockEntityInit.INTERSECT_BE.get().create(p_153215_, p_153216_);
    }
    @Override
    public RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

}