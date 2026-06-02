package com.watermelon0117.mergeablechests.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ChestCaseBlock extends Block {
    public static final EnumProperty<LidPart> LID = EnumProperty.create("lid", LidPart.class);

    public ChestCaseBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LID, LidPart.NORMAL));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LID);
    }

    public enum LidPart implements StringRepresentable {
        NORMAL("normal"),
        MID_NORTH("mid_north"),
        MID_SOUTH("mid_south"),
        MID_EAST("mid_east"),
        MID_WEST("mid_west"),
        LEFT_NORTH("left_north"),
        LEFT_SOUTH("left_south"),
        LEFT_EAST("left_east"),
        LEFT_WEST("left_west"),
        RIGHT_NORTH("right_north"),
        RIGHT_SOUTH("right_south"),
        RIGHT_EAST("right_east"),
        RIGHT_WEST("right_west");

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
