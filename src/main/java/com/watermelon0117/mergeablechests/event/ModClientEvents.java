package com.watermelon0117.mergeablechests.event;

import com.watermelon0117.mergeablechests.MergeableChests;
import com.watermelon0117.mergeablechests.client.renderer.BigChestBER;
import com.watermelon0117.mergeablechests.client.screen.BigChestScreen;
import com.watermelon0117.mergeablechests.init.BlockEntityInit;
import com.watermelon0117.mergeablechests.init.MenuInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MergeableChests.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(MenuInit.BIG_CHEST_MENU.get(), BigChestScreen::new));
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityInit.BIG_CHEST_BE.get(), BigChestBER::new);
    }
}
