package com.watermelon0117.mergeablechests.blockentities;

import com.watermelon0117.mergeablechests.blocks.BigChestBlock;
import com.watermelon0117.mergeablechests.init.BlockEntityInit;
import com.watermelon0117.mergeablechests.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BigChestBlockEntity extends BlockEntity implements Container {
    private static final int EVENT_SET_OPEN_COUNT = 1;
    public static final int SLOT_COUNT = 27;
    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new InvWrapper(this));
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            playSound(level, pos, SoundEvents.CHEST_OPEN);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            playSound(level, pos, SoundEvents.CHEST_CLOSE);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int oldCount, int newCount) {
            signalOpenCount(level, pos, state, oldCount, newCount);
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            AbstractContainerMenu menu = player.containerMenu;
            return menu instanceof com.watermelon0117.mergeablechests.menu.BigChestMenu bigChestMenu
                    && bigChestMenu.isRoot(BigChestBlockEntity.this);
        }
    };
    private final ChestLidController chestLidController = new ChestLidController();
    public float angle = 0;

    public BigChestBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityInit.BIG_CHEST_BE.get(), p_155229_, p_155230_);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(),
                this.getBlockPos().getX() + 10, this.getBlockPos().getY() + 10, this.getBlockPos().getZ() + 10);
    }

    public static void lidAnimateTick(Level level, BlockPos pos, BlockState state, BigChestBlockEntity blockEntity) {
        blockEntity.chestLidController.tickLid();
    }

    private void read(CompoundTag nbt) {
        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, items);
    }

    private CompoundTag write(CompoundTag nbt) {
        ContainerHelper.saveAllItems(nbt, items);
        return nbt;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        read(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        write(nbt);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return write(new CompoundTag());
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == EVENT_SET_OPEN_COUNT) {
            chestLidController.shouldBeOpen(type > 0);
            return true;
        }
        return super.triggerEvent(id, type);
    }

    public float getOpenNess(float partialTick) {
        return chestLidController.getOpenness(partialTick);
    }

    public void startOpen(Player player) {
        if (!remove && !player.isSpectator() && level != null) {
            openersCounter.incrementOpeners(player, level, worldPosition, getBlockState());
        }
    }

    public void stopOpen(Player player) {
        if (!remove && !player.isSpectator() && level != null) {
            openersCounter.decrementOpeners(player, level, worldPosition, getBlockState());
        }
    }

    public void recheckOpen() {
        if (!remove && level != null) {
            openersCounter.recheckOpeners(level, worldPosition, getBlockState());
        }
    }

    private static void playSound(Level level, BlockPos pos, SoundEvent sound) {
        level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int oldCount, int newCount) {
        level.blockEvent(pos, state.getBlock(), EVENT_SET_OPEN_COUNT, newCount);
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = ContainerHelper.removeItem(items, slot, amount);
        if (!stack.isEmpty()) {
            setChanged();
        }
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        if (level == null || level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D,
                worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
        if (!remove && cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        itemHandler = LazyOptional.of(() -> new InvWrapper(this));
    }

    public boolean same(BlockState blockState) {
        return same(blockState, this.getBlockState());
    }

    public static boolean same(BlockState blockState, BlockState blockState2) {
        return blockState.is(BlockInit.BIG_CHEST.get()) && blockState2.is(BlockInit.BIG_CHEST.get())
                && blockState.getValue(BigChestBlock.FACING).equals(blockState2.getValue(BigChestBlock.FACING));
    }

    public int width() {
        return width(level, getBlockPos());
    }

    public static int width(LevelAccessor level, BlockPos blockPos) {
        if (!level.getBlockState(blockPos).is(BlockInit.BIG_CHEST.get())) return 0;
        var pos = blockPos;
        int w = 0;
        for (int i = 0; i < 128; i++) {
            if (same(level.getBlockState(pos), level.getBlockState(blockPos)))
                w++;
            else
                break;

            blockPos = blockPos.offset(1, 0, 0);
        }
        return w;
    }

    public int depth() {
        return depth(level, getBlockPos(), width());
    }

    public static int depth(LevelAccessor level, BlockPos blockPos, int w) {
        if (!level.getBlockState(blockPos).is(BlockInit.BIG_CHEST.get())) return 0;
        var pos = blockPos;
        int d = 0;
        for (int i = 0; i < 128; i++) {
            if (same(level.getBlockState(blockPos), level.getBlockState(pos)) &&
                    isWidthRoot(level, blockPos) && width(level, blockPos) == w)
                d++;
            else
                break;
            blockPos = blockPos.offset(0, 0, 1);
        }
        return d;
    }

    public int height() {
        return height(level, getBlockPos(), width(), depth());
    }

    public static int height(LevelAccessor level, BlockPos blockPos, int w, int d) {
        if (!level.getBlockState(blockPos).is(BlockInit.BIG_CHEST.get())) return 0;
        var pos = blockPos;
        int h = 0;
        for (int i = 0; i < 128; i++) {
            if (same(level.getBlockState(blockPos), level.getBlockState(pos)) &&
                    isWidthRoot(level, blockPos) && width(level, blockPos) == w &&
                    isDepthRoot(level, blockPos) && depth(level, blockPos, w) == d)
                h++;
            else
                break;
            blockPos = blockPos.offset(0, 1, 0);
        }
        return h;
    }

    public boolean isWidthRoot() {
        return isWidthRoot(level, getBlockPos());
    }

    public static boolean isWidthRoot(LevelAccessor level, BlockPos blockPos) {
        if (!level.getBlockState(blockPos).is(BlockInit.BIG_CHEST.get())) return false;
        return !same(level.getBlockState(blockPos.offset(-1, 0, 0)),
                level.getBlockState(blockPos));
    }

    public boolean isDepthRoot() {
        return isDepthRoot(level, getBlockPos());
    }

    public static boolean isDepthRoot(LevelAccessor level, BlockPos blockPos) {
        if (!level.getBlockState(blockPos).is(BlockInit.BIG_CHEST.get())) return false;

        if (!isWidthRoot(level, blockPos))
            return false;
        if (!isWidthRoot(level, blockPos.offset(0, 0, -1)))
            return true;
        return !same(level.getBlockState(blockPos), level.getBlockState(blockPos.offset(0, 0, -1))) ||
                width(level, blockPos) != width(level, blockPos.offset(0, 0, -1));

    }

    public boolean isHeightRoot() {
        return isHeightRoot(level, getBlockPos());
    }

    public static boolean isHeightRoot(LevelAccessor level, BlockPos blockPos) {
        if (!level.getBlockState(blockPos).is(BlockInit.BIG_CHEST.get())) return false;
        int w = width(level, blockPos);
        if (!isDepthRoot(level, blockPos))
            return false;
        if (!isDepthRoot(level, blockPos.offset(0, -1, 0)))
            return true;
        return !same(level.getBlockState(blockPos), level.getBlockState(blockPos.offset(0, -1, 0))) ||
                depth(level, blockPos, w) != depth(level, blockPos.offset(0, -1, 0), w);

    }

    public static BlockPos findRoot(LevelAccessor level, BlockPos pos) {
        if (!level.getBlockState(pos).is(BlockInit.BIG_CHEST.get())) {
            return pos;
        }

        BlockPos root = pos;
        for (int i = 0; i < 128; i++) {
            BlockPos next = root.offset(-1, 0, 0);
            if (same(level.getBlockState(next), level.getBlockState(pos))) {
                root = next;
            } else {
                break;
            }
        }

        int w = width(level, root);
        for (int i = 0; i < 128; i++) {
            BlockPos next = root.offset(0, 0, -1);
            if (isWidthRoot(level, next) && same(level.getBlockState(next), level.getBlockState(pos))
                    && width(level, next) == w) {
                root = next;
            } else {
                break;
            }
        }

        int d = depth(level, root, w);
        for (int i = 0; i < 128; i++) {
            BlockPos next = root.offset(0, -1, 0);
            if (isDepthRoot(level, next) && same(level.getBlockState(next), level.getBlockState(pos))
                    && width(level, next) == w && depth(level, next, w) == d) {
                root = next;
            } else {
                break;
            }
        }

        return root;
    }

    public static List<BigChestBlockEntity> collectMergedBlocks(LevelAccessor level, BlockPos pos) {
        List<BigChestBlockEntity> blocks = new ArrayList<>();
        BlockPos root = findRoot(level, pos);
        BlockState rootState = level.getBlockState(root);
        if (!rootState.is(BlockInit.BIG_CHEST.get())) {
            return blocks;
        }

        int w = width(level, root);
        int d = depth(level, root, w);
        int h = height(level, root, w, d);
        for (int y = 0; y < h; y++) {
            for (int z = 0; z < d; z++) {
                for (int x = 0; x < w; x++) {
                    BlockPos blockPos = root.offset(x, y, z);
                    if (same(level.getBlockState(blockPos), rootState)
                            && level.getBlockEntity(blockPos) instanceof BigChestBlockEntity bigChestBlockEntity) {
                        blocks.add(bigChestBlockEntity);
                    }
                }
            }
        }
        return blocks;
    }
}
