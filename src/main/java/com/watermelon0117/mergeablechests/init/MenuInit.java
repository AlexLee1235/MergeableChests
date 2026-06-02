package com.watermelon0117.mergeablechests.init;

import com.watermelon0117.mergeablechests.MergeableChests;
import com.watermelon0117.mergeablechests.menu.BigChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuInit {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MergeableChests.MODID);

    public static final RegistryObject<MenuType<BigChestMenu>> BIG_CHEST_MENU = MENU_TYPES.register("big_chest",
            () -> IForgeMenuType.create(BigChestMenu::new));
}