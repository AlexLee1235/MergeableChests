package com.watermelon0117.mergeablechests.init;

import com.watermelon0117.mergeablechests.MergeableChests;
import com.watermelon0117.mergeablechests.blocks.BigChestBlock;
import com.watermelon0117.mergeablechests.blocks.BigChestBlockItem;
import com.watermelon0117.mergeablechests.blocks.ChestCaseBlock;
import com.watermelon0117.mergeablechests.blocks.ChestSideBlock;
import com.watermelon0117.mergeablechests.blocks.LidBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MergeableChests.MODID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MergeableChests.MODID);

    public static final RegistryObject<Block> BIG_CHEST = BLOCKS.register("big_chest",
            () -> new BigChestBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD).strength(2.5F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<BlockItem> BIG_CHEST_BLOCK_ITEM = BLOCK_ITEMS.register("big_chest",
            () -> new BigChestBlockItem(BIG_CHEST.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Block> LID = BLOCKS.register("lid",
            () -> new LidBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().noCollission()));
    public static final RegistryObject<Block> CHEST_0 = BLOCKS.register("chest_0",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_1 = BLOCKS.register("chest_1",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_2 = BLOCKS.register("chest_2",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_3 = BLOCKS.register("chest_3",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_4 = BLOCKS.register("chest_4",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_5 = BLOCKS.register("chest_5",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_6 = BLOCKS.register("chest_6",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_7 = BLOCKS.register("chest_7",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_8 = BLOCKS.register("chest_8",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_9 = BLOCKS.register("chest_9",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_10 = BLOCKS.register("chest_10",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_11 = BLOCKS.register("chest_11",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_12 = BLOCKS.register("chest_12",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_13 = BLOCKS.register("chest_13",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_14 = BLOCKS.register("chest_14",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_15 = BLOCKS.register("chest_15",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_SIDE_0 = BLOCKS.register("chest_side_0",
            () -> new ChestSideBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

    public static final RegistryObject<Block> CHEST_SIDE_1 = BLOCKS.register("chest_side_1",
            () -> new ChestSideBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

    public static final RegistryObject<Block> CHEST_SIDE_2 = BLOCKS.register("chest_side_2",
            () -> new ChestSideBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

    public static final RegistryObject<Block> CHEST_SIDE_3 = BLOCKS.register("chest_side_3",
            () -> new ChestSideBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

    public static final RegistryObject<Block> CHEST_SIDE_4 = BLOCKS.register("chest_side_4",
            () -> new ChestSideBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

    public static final RegistryObject<Block> CHEST_SIDE_5 = BLOCKS.register("chest_side_5",
            () -> new ChestSideBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

    public static final RegistryObject<Block> CHEST_SIDE_6 = BLOCKS.register("chest_side_6",
            () -> new ChestSideBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

    public static final RegistryObject<Block> CHEST_SIDE_7 = BLOCKS.register("chest_side_7",
            () -> new ChestSideBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

    public static final RegistryObject<Block> CHEST_SIDE_8 = BLOCKS.register("chest_side_8",
            () -> new ChestSideBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

    public static final RegistryObject<Block> CHEST_SIDE_9 = BLOCKS.register("chest_side_9",
            () -> new ChestSideBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

    public static final RegistryObject<Block> CHEST_SIDE_10 = BLOCKS.register("chest_side_10",
            () -> new ChestSideBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

    public static final RegistryObject<Block> CHEST_SIDE_11 = BLOCKS.register("chest_side_11",
            () -> new ChestSideBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

    public static final RegistryObject<Block> CHEST_SIDE_12 = BLOCKS.register("chest_side_12",
            () -> new ChestSideBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

    public static final RegistryObject<Block> CHEST_SIDE_13 = BLOCKS.register("chest_side_13",
            () -> new ChestSideBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

    public static final RegistryObject<Block> CHEST_SIDE_14 = BLOCKS.register("chest_side_14",
            () -> new ChestSideBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

    public static final RegistryObject<Block> CHEST_SIDE_15 = BLOCKS.register("chest_side_15",
            () -> new ChestSideBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

    public static final RegistryObject<Block> CHEST_IN_TOP_0 = BLOCKS.register("chest_in_top_0",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_IN_TOP_1 = BLOCKS.register("chest_in_top_1",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_IN_TOP_2 = BLOCKS.register("chest_in_top_2",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_IN_TOP_3 = BLOCKS.register("chest_in_top_3",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_IN_TOP_4 = BLOCKS.register("chest_in_top_4",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_IN_TOP_5 = BLOCKS.register("chest_in_top_5",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_IN_TOP_6 = BLOCKS.register("chest_in_top_6",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_IN_TOP_7 = BLOCKS.register("chest_in_top_7",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_IN_TOP_8 = BLOCKS.register("chest_in_top_8",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_IN_TOP_9 = BLOCKS.register("chest_in_top_9",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_IN_TOP_10 = BLOCKS.register("chest_in_top_10",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_IN_TOP_11 = BLOCKS.register("chest_in_top_11",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_IN_TOP_12 = BLOCKS.register("chest_in_top_12",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_IN_TOP_13 = BLOCKS.register("chest_in_top_13",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_IN_TOP_14 = BLOCKS.register("chest_in_top_14",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_IN_TOP_15 = BLOCKS.register("chest_in_top_15",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_CASE_0 = BLOCKS.register("chest_case_0",
            () -> new ChestCaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_CASE_1 = BLOCKS.register("chest_case_1",
            () -> new ChestCaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_CASE_2 = BLOCKS.register("chest_case_2",
            () -> new ChestCaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_CASE_3 = BLOCKS.register("chest_case_3",
            () -> new ChestCaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_CASE_4 = BLOCKS.register("chest_case_4",
            () -> new ChestCaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_CASE_5 = BLOCKS.register("chest_case_5",
            () -> new ChestCaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_CASE_6 = BLOCKS.register("chest_case_6",
            () -> new ChestCaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_CASE_7 = BLOCKS.register("chest_case_7",
            () -> new ChestCaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_CASE_8 = BLOCKS.register("chest_case_8",
            () -> new ChestCaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_CASE_9 = BLOCKS.register("chest_case_9",
            () -> new ChestCaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_CASE_10 = BLOCKS.register("chest_case_10",
            () -> new ChestCaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_CASE_11 = BLOCKS.register("chest_case_11",
            () -> new ChestCaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_CASE_12 = BLOCKS.register("chest_case_12",
            () -> new ChestCaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_CASE_13 = BLOCKS.register("chest_case_13",
            () -> new ChestCaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_CASE_14 = BLOCKS.register("chest_case_14",
            () -> new ChestCaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_CASE_15 = BLOCKS.register("chest_case_15",
            () -> new ChestCaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_BOTTOM_0 = BLOCKS.register("chest_bottom_0",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_BOTTOM_1 = BLOCKS.register("chest_bottom_1",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_BOTTOM_2 = BLOCKS.register("chest_bottom_2",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_BOTTOM_3 = BLOCKS.register("chest_bottom_3",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_BOTTOM_4 = BLOCKS.register("chest_bottom_4",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_BOTTOM_5 = BLOCKS.register("chest_bottom_5",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_BOTTOM_6 = BLOCKS.register("chest_bottom_6",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_BOTTOM_7 = BLOCKS.register("chest_bottom_7",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_BOTTOM_8 = BLOCKS.register("chest_bottom_8",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_BOTTOM_9 = BLOCKS.register("chest_bottom_9",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_BOTTOM_10 = BLOCKS.register("chest_bottom_10",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_BOTTOM_11 = BLOCKS.register("chest_bottom_11",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_BOTTOM_12 = BLOCKS.register("chest_bottom_12",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_BOTTOM_13 = BLOCKS.register("chest_bottom_13",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_BOTTOM_14 = BLOCKS.register("chest_bottom_14",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> CHEST_BOTTOM_15 = BLOCKS.register("chest_bottom_15",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

}
