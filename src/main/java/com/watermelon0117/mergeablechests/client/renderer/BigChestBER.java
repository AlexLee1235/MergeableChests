package com.watermelon0117.mergeablechests.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.watermelon0117.mergeablechests.blockentities.BigChestBlockEntity;
import com.watermelon0117.mergeablechests.blocks.BigChestBlock;
import com.watermelon0117.mergeablechests.blocks.ChestCaseBlock;
import com.watermelon0117.mergeablechests.blocks.ChestSideBlock;
import com.watermelon0117.mergeablechests.blocks.LidBlock;
import com.watermelon0117.mergeablechests.init.BlockInit;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BigChestBER implements BlockEntityRenderer<BigChestBlockEntity> {
    private final BlockEntityRendererProvider.Context context;
    private final RandomSource random = RandomSource.create();

    public BigChestBER(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(BigChestBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource,
                       int packedLight, int packedOverlay) {
        if (!be.hasLevel() || !be.isHeightRoot()) {
            return;
        }

        Level level = be.getLevel();
        BlockPos blockPos = be.getBlockPos();
        Direction dir = level.getBlockState(blockPos).getValue(BigChestBlock.FACING);
        renderBody(be, poseStack, bufferSource);

        poseStack.pushPose();
        float openness = be.getOpenNess(partialTick);
        openness = 1.0F - openness;
        openness = 1.0F - openness * openness * openness;
        openness *= (float) (Math.PI / 2.0F);
        if (dir == Direction.WEST || dir == Direction.NORTH) {
            poseStack.translate(-dir.step().x() * be.width() - 1.0 / 16,
                    be.height() - 7.0 / 16,
                    -dir.step().z() * be.depth() - 1.0 / 16);
            poseStack.mulPose(rotationAround(dir.getClockWise(), openness));
            poseStack.translate(dir.step().x() * be.width() + 1.0 / 16,
                    -(be.height() - 7.0 / 16),
                    dir.step().z() * be.depth() + 1.0 / 16);
        } else {
            poseStack.translate(1.0 / 16, be.height() - 7.0 / 16, 1.0 / 16);
            poseStack.mulPose(rotationAround(dir.getClockWise(), openness));
            poseStack.translate(-1.0 / 16, -(be.height() - 7.0 / 16), -1.0 / 16);
        }
        renderEntireCase(be, poseStack, bufferSource);
        renderLidLatch(be, dir, poseStack, bufferSource);
        poseStack.popPose();
    }

    private void renderEntireCase(BigChestBlockEntity be, PoseStack poseStack, MultiBufferSource bufferSource) {
        float width = be.width();
        float depth = be.depth();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < depth; j++) {
                int flag = 0;
                if (i == 0) flag |= 2;
                if (i == width - 1) flag |= 8;
                if (j == 0) flag |= 4;
                if (j == depth - 1) flag |= 1;
                renderCase(be, flag, Direction.NORTH, new Vector3f(i, be.height() - 1, j), poseStack, bufferSource);
            }
        }
    }

    private void renderCase(BigChestBlockEntity be, int flag, Direction dir, Vector3f pos, PoseStack poseStack,
                            MultiBufferSource bufferSource) {
        BlockState state = getCaseChest(flag).defaultBlockState()
                .setValue(ChestCaseBlock.LID, frontCaseLidPart(be, pos));
        Vector3f lightPos = new Vector3f(pos.x(), pos.y() + 1.0F, pos.z());
        renderRotatedBlockPart(be, state, dir, pos, poseStack, bufferSource,
                RenderType.solid(), 0.0D, 0.0D, 0.0D, lightPos);
    }

    private void renderLidLatch(BigChestBlockEntity be, Direction facing, PoseStack poseStack,
                                MultiBufferSource bufferSource) {
        float x;
        float z;
        if (facing.getAxis() == Direction.Axis.Z) {
            x = be.width() / 2.0F - 0.5F;
            z = facing == Direction.NORTH ? 0.0F : be.depth() - 1.0F;
        } else {
            x = facing == Direction.WEST ? 0.0F : be.width() - 1.0F;
            z = be.depth() / 2.0F - 0.5F;
        }

        Vector3f pos = new Vector3f(x, be.height() - 1.0F, z);
        Vector3f lightPos = new Vector3f(x, be.height() - 1.0F, z);
        BlockState state = BlockInit.LID.get().defaultBlockState().setValue(LidBlock.FACING, facing);
        renderBlockPart(be, state, pos, poseStack, bufferSource,
                RenderType.cutout(), 0.0D, 0.0D, 0.0D, lightPos);
    }

    private void renderBody(BigChestBlockEntity be, PoseStack poseStack, MultiBufferSource bufferSource) {
        float width = be.width();
        float depth = be.depth();
        float height = be.height();

        for (int i = 0; i < width; i++) {
            for (int k = 0; k < height; k++) {
                int flag = 0;
                if (i == 0) flag |= 2;
                if (i == width - 1) flag |= 8;
                if (k == 0) flag |= 4;
                if (k == height - 1) flag |= 1;
                renderSide(be, flag, Direction.SOUTH, new Vector3f(i, k, 0), poseStack, bufferSource);

                flag = 0;
                if (i == 0) flag |= 8;
                if (i == width - 1) flag |= 2;
                if (k == 0) flag |= 4;
                if (k == height - 1) flag |= 1;
                renderSide(be, flag, Direction.NORTH, new Vector3f(i, k, be.depth() - 1), poseStack, bufferSource);
            }
        }

        for (int j = 0; j < depth; j++) {
            for (int k = 0; k < height; k++) {
                int flag = 0;
                if (j == 0) flag |= 8;
                if (j == depth - 1) flag |= 2;
                if (k == 0) flag |= 4;
                if (k == height - 1) flag |= 1;
                renderSide(be, flag, Direction.EAST, new Vector3f(0, k, j), poseStack, bufferSource);

                flag = 0;
                if (j == 0) flag |= 2;
                if (j == depth - 1) flag |= 8;
                if (k == 0) flag |= 4;
                if (k == height - 1) flag |= 1;
                renderSide(be, flag, Direction.WEST, new Vector3f(be.width() - 1, k, j), poseStack, bufferSource);
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < depth; j++) {
                int flag = 0;
                if (i == 0) flag |= 2;
                if (i == width - 1) flag |= 8;
                if (j == 0) flag |= 4;
                if (j == depth - 1) flag |= 1;
                renderInsideTop(be, flag, Direction.NORTH, new Vector3f(i, be.height() - 1, j), poseStack, bufferSource);
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < depth; j++) {
                int flag = 0;
                if (i == 0) flag |= 2;
                if (i == width - 1) flag |= 8;
                if (j == 0) flag |= 4;
                if (j == depth - 1) flag |= 1;
                renderBottom(be, flag, new Vector3f(i, 0, j), poseStack, bufferSource);
            }
        }
    }

    private void renderInsideTop(BigChestBlockEntity be, int flag, Direction dir, Vector3f pos, PoseStack poseStack,
                                 MultiBufferSource bufferSource) {
        renderRotatedBlockPart(be, getInTopChest(flag).defaultBlockState(), dir, pos, poseStack, bufferSource,
                RenderType.cutout(), 0.0D, -6.0D / 16.0D, 0.0D);
    }

    private void renderBottom(BigChestBlockEntity be, int flag, Vector3f pos, PoseStack poseStack,
                              MultiBufferSource bufferSource) {
        renderBlockPart(be, getBottomChest(flag).defaultBlockState(), pos, poseStack, bufferSource,
                RenderType.cutout(), 0.0D, 0.0D, 0.0D);
    }

    private void renderSide(BigChestBlockEntity be, int flag, Direction dir, Vector3f pos, PoseStack poseStack,
                            MultiBufferSource bufferSource) {
        BlockState state = getSideChest(flag).defaultBlockState()
                .setValue(ChestSideBlock.FACING, dir.getOpposite())
                .setValue(ChestSideBlock.LID, frontBodyLidPart(be, dir, pos));
        renderBlockPart(be, state, pos, poseStack, bufferSource, RenderType.solid(), 0.0D, 0.0D, 0.0D);
    }

    private ChestSideBlock.LidPart frontBodyLidPart(BigChestBlockEntity be, Direction dir, Vector3f pos) {
        Level level = be.getLevel();
        Direction facing = level.getBlockState(be.getBlockPos()).getValue(BigChestBlock.FACING);
        if (dir.getOpposite() != facing || (int) pos.y() != (int) be.height() - 1) {
            return ChestSideBlock.LidPart.NORMAL;
        }

        int frontWidth = facing.getAxis() == Direction.Axis.Z ? (int) be.width() : (int) be.depth();
        int panelIndex = facing.getAxis() == Direction.Axis.Z ? (int) pos.x() : (int) pos.z();

        if (frontWidth % 2 == 1 && panelIndex == frontWidth / 2) {
            return ChestSideBlock.LidPart.MID;
        }

        if (frontWidth % 2 == 0) {
            int lowerCenter = frontWidth / 2 - 1;
            int upperCenter = frontWidth / 2;
            boolean invertIndexDirection = facing == Direction.SOUTH || facing == Direction.WEST;

            if (panelIndex == lowerCenter) {
                return invertIndexDirection ? ChestSideBlock.LidPart.RIGHT : ChestSideBlock.LidPart.LEFT;
            }
            if (panelIndex == upperCenter) {
                return invertIndexDirection ? ChestSideBlock.LidPart.LEFT : ChestSideBlock.LidPart.RIGHT;
            }
        }

        return ChestSideBlock.LidPart.NORMAL;
    }

    private ChestCaseBlock.LidPart frontCaseLidPart(BigChestBlockEntity be, Vector3f pos) {
        Level level = be.getLevel();
        Direction facing = level.getBlockState(be.getBlockPos()).getValue(BigChestBlock.FACING);
        int width = (int) be.width();
        int depth = (int) be.depth();
        int x = (int) pos.x();
        int z = (int) pos.z();
        int frontWidth;
        int panelIndex;
        Direction lidSide;

        switch (facing) {
            case NORTH -> {
                if (z != 0) {
                    return ChestCaseBlock.LidPart.NORMAL;
                }
                frontWidth = width;
                panelIndex = x;
                lidSide = Direction.SOUTH;
            }
            case SOUTH -> {
                if (z != depth - 1) {
                    return ChestCaseBlock.LidPart.NORMAL;
                }
                frontWidth = width;
                panelIndex = x;
                lidSide = Direction.NORTH;
            }
            case WEST -> {
                if (x != 0) {
                    return ChestCaseBlock.LidPart.NORMAL;
                }
                frontWidth = depth;
                panelIndex = z;
                lidSide = Direction.EAST;
            }
            case EAST -> {
                if (x != width - 1) {
                    return ChestCaseBlock.LidPart.NORMAL;
                }
                frontWidth = depth;
                panelIndex = z;
                lidSide = Direction.WEST;
            }
            default -> {
                return ChestCaseBlock.LidPart.NORMAL;
            }
        }

        if (frontWidth % 2 == 1 && panelIndex == frontWidth / 2) {
            return middleCaseLidPart(lidSide);
        }

        if (frontWidth % 2 == 0) {
            int lowerCenter = frontWidth / 2 - 1;
            int upperCenter = frontWidth / 2;
            boolean invertIndexDirection = facing == Direction.SOUTH || facing == Direction.WEST;

            if (panelIndex == lowerCenter) {
                return caseLidPart(lidSide, invertIndexDirection);
            }
            if (panelIndex == upperCenter) {
                return caseLidPart(lidSide, !invertIndexDirection);
            }
        }

        return ChestCaseBlock.LidPart.NORMAL;
    }

    private ChestCaseBlock.LidPart middleCaseLidPart(Direction side) {
        return switch (side) {
            case NORTH -> ChestCaseBlock.LidPart.MID_NORTH;
            case SOUTH -> ChestCaseBlock.LidPart.MID_SOUTH;
            case WEST -> ChestCaseBlock.LidPart.MID_WEST;
            case EAST -> ChestCaseBlock.LidPart.MID_EAST;
            default -> ChestCaseBlock.LidPart.NORMAL;
        };
    }

    private ChestCaseBlock.LidPart caseLidPart(Direction side, boolean right) {
        return switch (side) {
            case NORTH -> right ? ChestCaseBlock.LidPart.RIGHT_NORTH : ChestCaseBlock.LidPart.LEFT_NORTH;
            case SOUTH -> right ? ChestCaseBlock.LidPart.RIGHT_SOUTH : ChestCaseBlock.LidPart.LEFT_SOUTH;
            case WEST -> right ? ChestCaseBlock.LidPart.RIGHT_WEST : ChestCaseBlock.LidPart.LEFT_WEST;
            case EAST -> right ? ChestCaseBlock.LidPart.RIGHT_EAST : ChestCaseBlock.LidPart.LEFT_EAST;
            default -> ChestCaseBlock.LidPart.NORMAL;
        };
    }

    private void renderRotatedBlockPart(BigChestBlockEntity be, BlockState state, Direction dir, Vector3f pos,
                                        PoseStack poseStack, MultiBufferSource bufferSource, RenderType renderType,
                                        double offsetX, double offsetY, double offsetZ) {
        renderRotatedBlockPart(be, state, dir, pos, poseStack, bufferSource, renderType,
                offsetX, offsetY, offsetZ, pos);
    }

    private void renderRotatedBlockPart(BigChestBlockEntity be, BlockState state, Direction dir, Vector3f pos,
                                        PoseStack poseStack, MultiBufferSource bufferSource, RenderType renderType,
                                        double offsetX, double offsetY, double offsetZ, Vector3f lightPos) {
        poseStack.pushPose();
        poseStack.translate(pos.x() + offsetX, pos.y() + offsetY, pos.z() + offsetZ);
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-dir.toYRot()));
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        renderBlockPartAtCurrentPose(be, state, lightPos, poseStack, bufferSource, renderType);
        poseStack.popPose();
    }

    private void renderBlockPart(BigChestBlockEntity be, BlockState state, Vector3f pos, PoseStack poseStack,
                                 MultiBufferSource bufferSource, RenderType renderType,
                                 double offsetX, double offsetY, double offsetZ) {
        renderBlockPart(be, state, pos, poseStack, bufferSource, renderType,
                offsetX, offsetY, offsetZ, pos);
    }

    private void renderBlockPart(BigChestBlockEntity be, BlockState state, Vector3f pos, PoseStack poseStack,
                                 MultiBufferSource bufferSource, RenderType renderType,
                                 double offsetX, double offsetY, double offsetZ, Vector3f lightPos) {
        poseStack.pushPose();
        poseStack.translate(pos.x() + offsetX, pos.y() + offsetY, pos.z() + offsetZ);
        renderBlockPartAtCurrentPose(be, state, lightPos, poseStack, bufferSource, renderType);
        poseStack.popPose();
    }

    private void renderBlockPartAtCurrentPose(BigChestBlockEntity be, BlockState state, Vector3f lightPos,
                                              PoseStack poseStack, MultiBufferSource bufferSource,
                                              RenderType renderType) {
        Level level = be.getLevel();
        BlockPos piecePos = be.getBlockPos().offset((int) lightPos.x(), (int) lightPos.y(), (int) lightPos.z());
        context.getBlockRenderDispatcher().renderBatched(state, piecePos, level, poseStack,
                bufferSource.getBuffer(renderType), false, random, ModelData.EMPTY, renderType);
    }

    private Block getSideChest(int f) {
        return switch (f) {
            case 0 -> BlockInit.CHEST_SIDE_0.get();
            case 1 -> BlockInit.CHEST_SIDE_1.get();
            case 2 -> BlockInit.CHEST_SIDE_2.get();
            case 3 -> BlockInit.CHEST_SIDE_3.get();
            case 4 -> BlockInit.CHEST_SIDE_4.get();
            case 5 -> BlockInit.CHEST_SIDE_5.get();
            case 6 -> BlockInit.CHEST_SIDE_6.get();
            case 7 -> BlockInit.CHEST_SIDE_7.get();
            case 8 -> BlockInit.CHEST_SIDE_8.get();
            case 9 -> BlockInit.CHEST_SIDE_9.get();
            case 10 -> BlockInit.CHEST_SIDE_10.get();
            case 11 -> BlockInit.CHEST_SIDE_11.get();
            case 12 -> BlockInit.CHEST_SIDE_12.get();
            case 13 -> BlockInit.CHEST_SIDE_13.get();
            case 14 -> BlockInit.CHEST_SIDE_14.get();
            default -> BlockInit.CHEST_SIDE_15.get();
        };
    }

    private static Quaternionf rotationAround(Direction direction, float radians) {
        return new Quaternionf().fromAxisAngleRad(
                direction.getStepX(), direction.getStepY(), direction.getStepZ(), radians);
    }

    private Block getCaseChest(int f) {
        return switch (f) {
            case 0 -> BlockInit.CHEST_CASE_0.get();
            case 1 -> BlockInit.CHEST_CASE_1.get();
            case 2 -> BlockInit.CHEST_CASE_2.get();
            case 3 -> BlockInit.CHEST_CASE_3.get();
            case 4 -> BlockInit.CHEST_CASE_4.get();
            case 5 -> BlockInit.CHEST_CASE_5.get();
            case 6 -> BlockInit.CHEST_CASE_6.get();
            case 7 -> BlockInit.CHEST_CASE_7.get();
            case 8 -> BlockInit.CHEST_CASE_8.get();
            case 9 -> BlockInit.CHEST_CASE_9.get();
            case 10 -> BlockInit.CHEST_CASE_10.get();
            case 11 -> BlockInit.CHEST_CASE_11.get();
            case 12 -> BlockInit.CHEST_CASE_12.get();
            case 13 -> BlockInit.CHEST_CASE_13.get();
            case 14 -> BlockInit.CHEST_CASE_14.get();
            default -> BlockInit.CHEST_CASE_15.get();
        };
    }

    private Block getInTopChest(int f) {
        return switch (f) {
            case 0 -> BlockInit.CHEST_IN_TOP_0.get();
            case 1 -> BlockInit.CHEST_IN_TOP_1.get();
            case 2 -> BlockInit.CHEST_IN_TOP_2.get();
            case 3 -> BlockInit.CHEST_IN_TOP_3.get();
            case 4 -> BlockInit.CHEST_IN_TOP_4.get();
            case 5 -> BlockInit.CHEST_IN_TOP_5.get();
            case 6 -> BlockInit.CHEST_IN_TOP_6.get();
            case 7 -> BlockInit.CHEST_IN_TOP_7.get();
            case 8 -> BlockInit.CHEST_IN_TOP_8.get();
            case 9 -> BlockInit.CHEST_IN_TOP_9.get();
            case 10 -> BlockInit.CHEST_IN_TOP_10.get();
            case 11 -> BlockInit.CHEST_IN_TOP_11.get();
            case 12 -> BlockInit.CHEST_IN_TOP_12.get();
            case 13 -> BlockInit.CHEST_IN_TOP_13.get();
            case 14 -> BlockInit.CHEST_IN_TOP_14.get();
            default -> BlockInit.CHEST_IN_TOP_15.get();
        };
    }

    private Block getBottomChest(int f) {
        return switch (f) {
            case 0 -> BlockInit.CHEST_BOTTOM_0.get();
            case 1 -> BlockInit.CHEST_BOTTOM_1.get();
            case 2 -> BlockInit.CHEST_BOTTOM_2.get();
            case 3 -> BlockInit.CHEST_BOTTOM_3.get();
            case 4 -> BlockInit.CHEST_BOTTOM_4.get();
            case 5 -> BlockInit.CHEST_BOTTOM_5.get();
            case 6 -> BlockInit.CHEST_BOTTOM_6.get();
            case 7 -> BlockInit.CHEST_BOTTOM_7.get();
            case 8 -> BlockInit.CHEST_BOTTOM_8.get();
            case 9 -> BlockInit.CHEST_BOTTOM_9.get();
            case 10 -> BlockInit.CHEST_BOTTOM_10.get();
            case 11 -> BlockInit.CHEST_BOTTOM_11.get();
            case 12 -> BlockInit.CHEST_BOTTOM_12.get();
            case 13 -> BlockInit.CHEST_BOTTOM_13.get();
            case 14 -> BlockInit.CHEST_BOTTOM_14.get();
            default -> BlockInit.CHEST_BOTTOM_15.get();
        };
    }
}
