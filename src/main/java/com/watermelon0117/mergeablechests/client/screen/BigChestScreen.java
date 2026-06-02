package com.watermelon0117.mergeablechests.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.watermelon0117.mergeablechests.network.PacketHandler;
import com.watermelon0117.mergeablechests.menu.BigChestMenu;
import com.watermelon0117.mergeablechests.network.BigChestScrollPacket;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class BigChestScreen extends AbstractContainerScreen<BigChestMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation("textures/gui/container/generic_54.png");
    private static final int SCROLL_TRACK_X = 170;
    private static final int SCROLL_TRACK_Y = 18;
    private static final int SCROLL_TRACK_WIDTH = 5;
    private static final int SCROLL_TRACK_HEIGHT = 108;
    private static final int SCROLL_THUMB_HEIGHT = 15;
    private boolean scrolling;
    private float scrollOffs;

    public BigChestScreen(BigChestMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageHeight = 114 + menu.getVisibleRows() * 18;
        inventoryLabelY = imageHeight - 94;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
        int chestAreaHeight = menu.getVisibleRows() * 18 + 17;
        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, chestAreaHeight);
        blit(poseStack, leftPos, topPos + chestAreaHeight, 0, 126, imageWidth, 96);
        renderScrollBar(poseStack);
    }

    private void renderScrollBar(PoseStack poseStack) {
        if (!isScrollBarActive()) {
            return;
        }

        int trackX = leftPos + SCROLL_TRACK_X;
        int trackY = topPos + SCROLL_TRACK_Y;
        fill(poseStack, trackX, trackY, trackX + SCROLL_TRACK_WIDTH, trackY + SCROLL_TRACK_HEIGHT, 0x66000000);

        int thumbY = getThumbY();
        fill(poseStack, trackX, thumbY, trackX + SCROLL_TRACK_WIDTH, thumbY + SCROLL_THUMB_HEIGHT, 0xFFE0E0E0);
        fill(poseStack, trackX + 1, thumbY + 1, trackX + SCROLL_TRACK_WIDTH - 1, thumbY + SCROLL_THUMB_HEIGHT - 1, 0xFF8A8A8A);
    }

    private int getThumbY() {
        return topPos + SCROLL_TRACK_Y + Math.round(scrollOffs * (SCROLL_TRACK_HEIGHT - SCROLL_THUMB_HEIGHT));
    }

    private boolean isScrollBarActive() {
        return menu.getMaxScrollRowOffset() > 0;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (!isScrollBarActive()) {
            return false;
        }

        int maxOffset = menu.getMaxScrollRowOffset();
        float step = (float) (delta / (double) maxOffset);
        setScrollOffs(Mth.clamp(scrollOffs - step, 0.0F, 1.0F));
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isScrollBarActive() && isMouseOverScrollBar(mouseX, mouseY)) {
            scrolling = isScrollBarActive();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (scrolling) {
            setScrollFromMouse(mouseY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            scrolling = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean isMouseOverScrollBar(double mouseX, double mouseY) {
        int trackX = leftPos + SCROLL_TRACK_X;
        int trackY = topPos + SCROLL_TRACK_Y;
        return mouseX >= trackX && mouseX < trackX + SCROLL_TRACK_WIDTH
                && mouseY >= trackY && mouseY < trackY + SCROLL_TRACK_HEIGHT;
    }

    private void setScrollFromMouse(double mouseY) {
        float progress = (float) ((mouseY - (topPos + SCROLL_TRACK_Y) - SCROLL_THUMB_HEIGHT / 2.0D)
                / (SCROLL_TRACK_HEIGHT - SCROLL_THUMB_HEIGHT));
        setScrollOffs(Mth.clamp(progress, 0.0F, 1.0F));
    }

    private void setScrollOffs(float scrollOffs) {
        int oldOffset = menu.getScrollRowOffset();
        this.scrollOffs = scrollOffs;
        menu.scrollTo(scrollOffs);
        if (oldOffset != menu.getScrollRowOffset()) {
            PacketHandler.sendToServer(new BigChestScrollPacket(menu.containerId, menu.getScrollRowOffset()));
        }
    }

}
