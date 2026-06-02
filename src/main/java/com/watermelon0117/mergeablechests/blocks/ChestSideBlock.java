package com.watermelon0117.mergeablechests.blocks;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ChestSideBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<LidPart> LID = EnumProperty.create("lid", LidPart.class);

    public ChestSideBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LID, LidPart.NORMAL));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LID);
    }

    public enum LidPart implements StringRepresentable {
        NORMAL("normal"),
        MID("mid"),
        LEFT("left"),
        RIGHT("right");

        private final String name;

        LidPart(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
