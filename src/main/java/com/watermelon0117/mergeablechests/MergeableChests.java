package com.watermelon0117.mergeablechests;

import com.watermelon0117.mergeablechests.init.*;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(MergeableChests.MODID)
public class MergeableChests {
    public static final String MODID="mergeablechests";

    public MergeableChests(FMLJavaModLoadingContext context){
	IEventBus bus = context.getModEventBus();

        BlockInit.BLOCKS.register(bus);
        BlockInit.BLOCK_ITEMS.register(bus);
        ItemInit.ITEMS.register(bus);
        EntityInit.ENTITIES.register(bus);
        BlockEntityInit.BLOCK_ENTITIES.register(bus);
        MenuInit.MENU_TYPES.register(bus);
        RecipeInit.RECIPE_SERIALIZERS.register(bus);
        FluidInit.FLUID_TYPES.register(bus);
        FluidInit.FLUIDS.register(bus);
        bus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (CreativeModeTabs.FUNCTIONAL_BLOCKS.equals(event.getTabKey())) {
            event.accept(BlockInit.BIG_CHEST_BLOCK_ITEM);
        }
    }
}
