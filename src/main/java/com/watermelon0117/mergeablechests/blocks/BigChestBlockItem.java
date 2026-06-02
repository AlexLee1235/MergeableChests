package com.watermelon0117.mergeablechests.blocks;

import com.watermelon0117.mergeablechests.client.renderer.BigChestItemRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class BigChestBlockItem extends BlockItem {
    public BigChestBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(BigChestItemRenderer.createExtensions());
    }
}
