package com.watermelon0117.mergeablechests.init;

import com.watermelon0117.mergeablechests.MergeableChests;
import com.watermelon0117.mergeablechests.blockentities.BigChestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MergeableChests.MODID);

    public static final RegistryObject<BlockEntityType<BigChestBlockEntity>> BIG_CHEST_BE = BLOCK_ENTITIES.register("big_chest",
            () -> BlockEntityType.Builder.of(BigChestBlockEntity::new, BlockInit.BIG_CHEST.get()).build(null));
}
