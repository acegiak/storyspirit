package net.machinespirit.storyspirit;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

class Loot{

    static ArrayList<ItemStack> options;

    public static ItemStack randomLootItem(){
        options = new ArrayList<ItemStack>();
        options.add(new ItemStack(Material.BAKED_POTATO));
        options.add(new ItemStack(Material.GOLD_AXE));
        options.add(new ItemStack(Material.CAKE));
        return options.get(StorySpirit.random.nextInt(options.size()));
    }

}