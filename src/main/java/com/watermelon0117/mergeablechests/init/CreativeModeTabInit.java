package com.watermelon0117.mergeablechests.init;

import com.watermelon0117.mergeablechests.MergeableChests;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModeTabInit {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MergeableChests.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + MergeableChests.MODID))
                    .icon(() -> Items.CRAFTING_TABLE.getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(BlockInit.BIG_CHEST_BLOCK_ITEM.get());
                    })
                    .build());
}
