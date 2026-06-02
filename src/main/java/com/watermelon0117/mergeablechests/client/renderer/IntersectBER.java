package com.watermelon0117.mergeablechests.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.watermelon0117.mergeablechests.blockentities.IntersectBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public class IntersectBER implements BlockEntityRenderer<IntersectBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public IntersectBER(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(IntersectBlockEntity p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
        p_112309_.pushPose();
        Level level = p_112307_.getLevel();
        var entities = level.getEntities(null,
                new AABB(p_112307_.getBlockPos(), p_112307_.getBlockPos().offset(1, 1, 1)));

        if (!entities.isEmpty()) {
            float yo = (float) entities.get(0).yOld;
            float y = (float) entities.get(0).getY();
            y = yo + (y - yo) * p_112308_;
            p_112309_.scale(1, y - p_112307_.getBlockPos().getY(), 1);
        }
        context.getBlockRenderDispatcher().renderSingleBlock(Blocks.WHITE_CONCRETE.defaultBlockState(),
                p_112309_, p_112310_, p_112311_, p_112312_);
        p_112309_.popPose();
    }
}
