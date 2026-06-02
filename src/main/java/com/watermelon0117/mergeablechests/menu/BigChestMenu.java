package com.watermelon0117.mergeablechests.menu;

import com.watermelon0117.mergeablechests.blockentities.BigChestBlockEntity;
import com.watermelon0117.mergeablechests.init.MenuInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BigChestMenu extends AbstractContainerMenu {
    public static final int COLUMNS = 9;
    private static final int SINGLE_BLOCK_VISIBLE_ROWS = 3;
    private static final int MERGED_VISIBLE_ROWS = 6;

    private final MergedBigChestContainer chestContainer;
    private final BlockPos rootPos;
    private final Player player;
    private final int visibleRows;
    private final int visibleSlots;
    private final SimpleContainer visibleContainer;
    private int scrollRowOffset;

    public BigChestMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(containerId, playerInventory, ClientContainerData.read(buffer));
    }

    public BigChestMenu(int containerId, Inventory playerInventory, List<BigChestBlockEntity> blocks,
                        BigChestBlockEntity root, BlockPos rootPos) {
        this(containerId, playerInventory, MergedBigChestContainer.server(blocks, root), rootPos);
    }

    public BigChestMenu(int containerId, Inventory playerInventory, List<BigChestBlockEntity> blocks, BlockPos rootPos) {
        this(containerId, playerInventory,
                MergedBigChestContainer.server(blocks, blocks.isEmpty() ? null : blocks.get(0)), rootPos);
    }

    private BigChestMenu(int containerId, Inventory playerInventory, ClientContainerData data) {
        this(containerId, playerInventory, data.container, data.rootPos);
    }

    private BigChestMenu(int containerId, Inventory playerInventory, MergedBigChestContainer chestContainer, BlockPos rootPos) {
        super(MenuInit.BIG_CHEST_MENU.get(), containerId);
        this.chestContainer = chestContainer;
        this.rootPos = rootPos;
        this.player = playerInventory.player;
        this.visibleRows = getVisibleRowsForBlockCount(chestContainer.getBlockCount());
        this.visibleSlots = COLUMNS * visibleRows;
        this.visibleContainer = new SimpleContainer(visibleSlots);
        addChestSlots();
        addPlayerSlots(playerInventory);
        refreshVisibleSlots();
        this.chestContainer.startOpen(playerInventory.player);
    }

    private static int getVisibleRowsForBlockCount(int blockCount) {
        return blockCount <= 1 ? SINGLE_BLOCK_VISIBLE_ROWS : MERGED_VISIBLE_ROWS;
    }

    private static MergedBigChestContainer createClientContainer(int blockCount) {
        List<Container> containers = new ArrayList<>();
        for (int i = 0; i < blockCount; i++) {
            containers.add(new SimpleContainer(BigChestBlockEntity.SLOT_COUNT));
        }
        return MergedBigChestContainer.client(containers);
    }

    private void addChestSlots() {
        for (int row = 0; row < visibleRows; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                int visibleSlot = column + row * COLUMNS;
                addSlot(new BigChestVisibleSlot(this, visibleContainer, visibleSlot, 8 + column * 18, 18 + row * 18));
            }
        }
    }

    private void addPlayerSlots(Inventory playerInventory) {
        int playerInventoryOffset = (visibleRows - 4) * 18;
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                addSlot(new Slot(playerInventory, column + row * COLUMNS + COLUMNS,
                        8 + column * 18, 103 + row * 18 + playerInventoryOffset));
            }
        }

        for (int column = 0; column < COLUMNS; column++) {
            addSlot(new Slot(playerInventory, column, 8 + column * 18, 161 + playerInventoryOffset));
        }
    }

    public int getGlobalSlotForVisible(int visibleSlot) {
        return scrollRowOffset * COLUMNS + visibleSlot;
    }

    public int getScrollRowOffset() {
        return scrollRowOffset;
    }

    public int getVisibleRows() {
        return visibleRows;
    }

    public int getVisibleSlots() {
        return visibleSlots;
    }

    public int getMaxScrollRowOffset() {
        return Math.max(0, Mth.ceil(chestContainer.getContainerSize() / (float) COLUMNS) - visibleRows);
    }

    public void setScrollRowOffset(int rowOffset) {
        int clamped = Mth.clamp(rowOffset, 0, getMaxScrollRowOffset());
        if (scrollRowOffset != clamped) {
            scrollRowOffset = clamped;
            refreshVisibleSlots();
            if (!player.level().isClientSide) {
                broadcastFullState();
            }
        }
    }

    public void scrollTo(float scrollOffset) {
        int maxRows = getMaxScrollRowOffset();
        int rowOffset = (int) ((double) (scrollOffset * (float) maxRows) + 0.5D);
        setScrollRowOffset(rowOffset);
    }

    private void refreshVisibleSlots() {
        for (int visibleSlot = 0; visibleSlot < visibleSlots; visibleSlot++) {
            int globalSlot = getGlobalSlotForVisible(visibleSlot);
            if (globalSlot >= 0 && globalSlot < chestContainer.getContainerSize()) {
                visibleContainer.setItem(visibleSlot, chestContainer.getItem(globalSlot));
            } else {
                visibleContainer.setItem(visibleSlot, ItemStack.EMPTY);
            }
        }
    }

    public boolean isRoot(BigChestBlockEntity blockEntity) {
        return rootPos.equals(blockEntity.getBlockPos());
    }

    @Override
    public boolean stillValid(Player player) {
        return chestContainer.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (!stillValid(player)) {
            return ItemStack.EMPTY;
        }

        ItemStack copiedStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            copiedStack = stack.copy();
            if (index < visibleSlots) {
                if (!moveItemStackTo(stack, visibleSlots, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackToChest(stack)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == copiedStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }
        return copiedStack;
    }

    private boolean moveItemStackToChest(ItemStack stack) {
        boolean changed = false;
        for (int slot = 0; slot < chestContainer.getContainerSize() && !stack.isEmpty(); slot++) {
            if (!chestContainer.canPlaceItem(slot, stack)) {
                continue;
            }

            ItemStack target = chestContainer.getItem(slot);
            if (!target.isEmpty() && ItemStack.isSameItemSameTags(target, stack)) {
                int max = Math.min(chestContainer.getMaxStackSize(), target.getMaxStackSize());
                int moveCount = Math.min(stack.getCount(), max - target.getCount());
                if (moveCount > 0) {
                    stack.shrink(moveCount);
                    target.grow(moveCount);
                    chestContainer.setItem(slot, target);
                    changed = true;
                }
            }
        }

        for (int slot = 0; slot < chestContainer.getContainerSize() && !stack.isEmpty(); slot++) {
            if (!chestContainer.canPlaceItem(slot, stack)) {
                continue;
            }

            ItemStack target = chestContainer.getItem(slot);
            if (target.isEmpty()) {
                int moveCount = Math.min(stack.getCount(), Math.min(chestContainer.getMaxStackSize(), stack.getMaxStackSize()));
                ItemStack moved = stack.split(moveCount);
                chestContainer.setItem(slot, moved);
                changed = true;
            }
        }

        if (changed) {
            chestContainer.setChanged();
            refreshVisibleSlots();
        }
        return changed;
    }

    @Override
    public void broadcastChanges() {
        refreshVisibleSlots();
        super.broadcastChanges();
    }

    @Override
    public void broadcastFullState() {
        refreshVisibleSlots();
        super.broadcastFullState();
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        chestContainer.stopOpen(player);
    }

    private static class BigChestVisibleSlot extends Slot {
        private final BigChestMenu menu;
        private final int visibleSlot;

        private BigChestVisibleSlot(BigChestMenu menu, Container container, int visibleSlot, int x, int y) {
            super(container, visibleSlot, x, y);
            this.menu = menu;
            this.visibleSlot = visibleSlot;
        }

        private int getGlobalSlot() {
            return menu.getGlobalSlotForVisible(visibleSlot);
        }

        @Override
        public boolean isActive() {
            return getGlobalSlot() < menu.chestContainer.getContainerSize();
        }

        @Override
        public boolean hasItem() {
            return isActive() && super.hasItem();
        }

        @Override
        public ItemStack getItem() {
            return isActive() ? super.getItem() : ItemStack.EMPTY;
        }

        @Override
        public void set(ItemStack stack) {
            if (isActive()) {
                menu.chestContainer.setItem(getGlobalSlot(), stack);
                super.set(stack);
            }
        }

        @Override
        public ItemStack remove(int amount) {
            if (!isActive()) {
                return ItemStack.EMPTY;
            }

            ItemStack removed = menu.chestContainer.removeItem(getGlobalSlot(), amount);
            super.set(menu.chestContainer.getItem(getGlobalSlot()));
            return removed;
        }

        @Override
        public void setChanged() {
            if (isActive()) {
                menu.chestContainer.setChanged();
            }
            super.setChanged();
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return isActive() && menu.chestContainer.canPlaceItem(getGlobalSlot(), stack);
        }

        @Override
        public boolean mayPickup(Player player) {
            return isActive() && menu.chestContainer.stillValid(player);
        }
    }

    private static class MergedBigChestContainer implements Container {
        private final List<? extends Container> containers;
        private final BigChestBlockEntity root;

        private MergedBigChestContainer(List<? extends Container> containers, BigChestBlockEntity root) {
            this.containers = containers;
            this.root = root;
        }

        private static MergedBigChestContainer server(List<BigChestBlockEntity> blocks, BigChestBlockEntity root) {
            return new MergedBigChestContainer(blocks, root);
        }

        private static MergedBigChestContainer client(List<Container> containers) {
            return new MergedBigChestContainer(containers, null);
        }

        private int getBlockCount() {
            return containers.size();
        }

        @Override
        public int getContainerSize() {
            return containers.size() * BigChestBlockEntity.SLOT_COUNT;
        }

        @Override
        public boolean isEmpty() {
            for (int i = 0; i < getContainerSize(); i++) {
                if (!getItem(i).isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public ItemStack getItem(int slot) {
            Container container = getContainer(slot);
            return container == null ? ItemStack.EMPTY : container.getItem(getLocalSlot(slot));
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            Container container = getContainer(slot);
            return container == null ? ItemStack.EMPTY : container.removeItem(getLocalSlot(slot), amount);
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            Container container = getContainer(slot);
            return container == null ? ItemStack.EMPTY : container.removeItemNoUpdate(getLocalSlot(slot));
        }

        @Override
        public void setItem(int slot, ItemStack stack) {
            Container container = getContainer(slot);
            if (container != null) {
                container.setItem(getLocalSlot(slot), stack);
            }
        }

        @Override
        public void setChanged() {
            for (Container container : containers) {
                container.setChanged();
            }
        }

        @Override
        public boolean stillValid(Player player) {
            if (root == null) {
                return true;
            }

            boolean closeEnough = false;
            var rootState = root.getBlockState();
            for (Container container : containers) {
                if (container instanceof BigChestBlockEntity blockEntity) {
                    if (!isLiveMergedBlock(blockEntity, rootState)) {
                        return false;
                    }

                    BlockPos blockPos = blockEntity.getBlockPos();
                    if (player.distanceToSqr(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D,
                            blockPos.getZ() + 0.5D) <= 64.0D) {
                        closeEnough = true;
                    }
                } else if (!container.stillValid(player)) {
                    return false;
                }
            }
            return closeEnough;
        }

        private boolean isLiveMergedBlock(BigChestBlockEntity blockEntity, net.minecraft.world.level.block.state.BlockState rootState) {
            var level = blockEntity.getLevel();
            return level != null
                    && level.getBlockEntity(blockEntity.getBlockPos()) == blockEntity
                    && BigChestBlockEntity.same(level.getBlockState(blockEntity.getBlockPos()), rootState);
        }

        @Override
        public void startOpen(Player player) {
            if (root != null) {
                root.startOpen(player);
            }
        }

        @Override
        public void stopOpen(Player player) {
            if (root != null) {
                root.stopOpen(player);
            }
        }

        @Override
        public boolean canPlaceItem(int slot, ItemStack stack) {
            Container container = getContainer(slot);
            return container != null && container.canPlaceItem(getLocalSlot(slot), stack);
        }

        @Override
        public void clearContent() {
            for (Container container : containers) {
                container.clearContent();
            }
        }

        private Container getContainer(int slot) {
            if (slot < 0 || slot >= getContainerSize()) {
                return null;
            }
            return containers.get(slot / BigChestBlockEntity.SLOT_COUNT);
        }

        private int getLocalSlot(int slot) {
            return slot % BigChestBlockEntity.SLOT_COUNT;
        }
    }

    private static class ClientContainerData {
        private final MergedBigChestContainer container;
        private final BlockPos rootPos;

        private ClientContainerData(MergedBigChestContainer container, BlockPos rootPos) {
            this.container = container;
            this.rootPos = rootPos;
        }

        private static ClientContainerData read(FriendlyByteBuf buffer) {
            BlockPos rootPos = buffer.readBlockPos();
            int blockCount = buffer.readVarInt();
            return new ClientContainerData(createClientContainer(blockCount), rootPos);
        }
    }
}
