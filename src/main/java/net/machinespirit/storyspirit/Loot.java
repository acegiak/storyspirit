package net.machinespirit.storyspirit;

import net.machinespirit.storyspirit.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

class Loot{

	public static Map<ItemStack,Integer> loot;
	public static Map<ItemStack,Integer> named;;

    static{
        named = new HashMap<ItemStack, Integer>();
		named.put(new ItemStack(Material.IRON_SWORD), 12);
		named.put(new ItemStack(Material.GOLD_SWORD), 6);
		named.put(new ItemStack(Material.DIAMOND_SWORD), 3);
		named.put(new ItemStack(Material.IRON_AXE), 10);
		named.put(new ItemStack(Material.GOLD_AXE), 5);
		named.put(new ItemStack(Material.DIAMOND_AXE), 2);
		named.put(new ItemStack(Material.IRON_PICKAXE), 8);
		named.put(new ItemStack(Material.GOLD_PICKAXE), 4);
		named.put(new ItemStack(Material.DIAMOND_PICKAXE), 2);
		named.put(new ItemStack(Material.IRON_SPADE), 4);
		named.put(new ItemStack(Material.GOLD_SPADE), 2);
		named.put(new ItemStack(Material.DIAMOND_SPADE), 1);
		named.put(new ItemStack(Material.IRON_HOE), 4);
		named.put(new ItemStack(Material.GOLD_HOE), 2);
		named.put(new ItemStack(Material.DIAMOND_HOE), 1);
		named.put(new ItemStack(Material.LEATHER_HELMET), 4);
		named.put(new ItemStack(Material.IRON_HELMET), 4);
		named.put(new ItemStack(Material.GOLD_HELMET), 2);
		named.put(new ItemStack(Material.DIAMOND_HELMET), 1);
		named.put(new ItemStack(Material.LEATHER_LEGGINGS), 4);
		named.put(new ItemStack(Material.IRON_LEGGINGS), 4);
		named.put(new ItemStack(Material.GOLD_LEGGINGS), 2);
		named.put(new ItemStack(Material.DIAMOND_LEGGINGS), 1);
		named.put(new ItemStack(Material.LEATHER_BOOTS), 4);
		named.put(new ItemStack(Material.IRON_BOOTS), 4);
		named.put(new ItemStack(Material.GOLD_BOOTS), 2);
		named.put(new ItemStack(Material.DIAMOND_BOOTS), 1);
		named.put(new ItemStack(Material.LEATHER_CHESTPLATE), 4);
		named.put(new ItemStack(Material.IRON_CHESTPLATE), 4);
		named.put(new ItemStack(Material.GOLD_CHESTPLATE), 2);
		named.put(new ItemStack(Material.DIAMOND_CHESTPLATE), 1);
		named.put(new ItemStack(Material.BOW), 16);
		named.put(new ItemStack(Material.FISHING_ROD), 6);


        loot = new HashMap<ItemStack, Integer>();
		loot.put(new ItemStack(Material.IRON_INGOT,16), 8);
		loot.put(new ItemStack(Material.EMERALD,5), 8);
		loot.put(new ItemStack(Material.GOLD_INGOT,16), 8);
		loot.put(new ItemStack(Material.BREAD,16), 12);
		loot.put(new ItemStack(Material.GOLD_NUGGET,16), 8);
		loot.put(new ItemStack(Material.COOKED_BEEF,16), 8);
		loot.put(new ItemStack(Material.WHEAT,64), 8);
		loot.put(new ItemStack(Material.SADDLE,1), 8);
		loot.put(new ItemStack(Material.IRON_BARDING,1),2);
		loot.put(new ItemStack(Material.GOLD_BARDING,1), 2);
		loot.put(new ItemStack(Material.DIAMOND_BARDING,1), 2);
		loot.put(new ItemStack(Material.NAME_TAG,1), 16);
		loot.put(new ItemStack(Material.SLIME_BALL,1), 8);
		loot.put(new ItemStack(Material.TORCH,24), 10);
		loot.put(new ItemStack(Material.REDSTONE,16), 8);
		loot.put(new ItemStack(Material.MILK_BUCKET,1), 2);
		loot.put(new ItemStack(Material.WATER_BUCKET,1), 2);
		loot.put(new ItemStack(Material.ARROW,16), 10);
		loot.put(new ItemStack(Material.EMPTY_MAP,1), 8);
		loot.put(new ItemStack(Material.BED,1), 8);
		loot.put(new ItemStack(Material.BAKED_POTATO,16), 12);
		loot.put(new ItemStack(Material.SPIDER_EYE,5), 12);
		loot.put(new ItemStack(Material.MUSHROOM_SOUP,12), 12);
		loot.put(new ItemStack(Material.COOKED_MUTTON,12), 12);
        loot.put(new ItemStack(Material.COOKED_CHICKEN,12), 12);

    }

    public static List<ItemStack> getTreasure(){
        ArrayList<ItemStack> hold = new ArrayList<ItemStack>();
        int count = StorySpirit.random.nextInt(12);
        for(int i = 0;i< count;i++){
            hold.add(randomItem());
        }
        return hold;
    }

    public static ItemStack randomItem(){
        ItemStack s = null;

        if(StorySpirit.random.nextFloat()<0.15f){
            return randomNamed();
        }

        int count =0;
		for(ItemStack i : loot.keySet()){

            if(i != null && loot.get(i) != null){
            count += loot.get(i);
            }
        }
        int selection = StorySpirit.random.nextInt(Math.max(1, count));
        int fresh = 0;
        for (ItemStack stack : loot.keySet()) {

            if(stack != null && loot.get(stack) != null){
            fresh += loot.get(stack);
            if(selection < fresh){
                return new ItemStack(stack.getType(),Math.max(1,StorySpirit.random.nextInt(stack.getAmount())));
            }}
            
        }
        return new ItemStack(Material.WOOD_SWORD);
    }

    public static ItemStack randomNamed(){

        ItemStack s = null;
        int count =0;
        for(ItemStack i : named.keySet()){
            if(i != null && named.get(i) !=null){
                count += named.get(i);
            }else{
                System.out.println("no record in named for "+i.toString());
            }
        }
        int selection = StorySpirit.random.nextInt(Math.max(1, count));
        int fresh = 0;
        for (ItemStack stack : named.keySet()) {
            if(stack != null && named.get(stack) != null){
                fresh += named.get(stack);
                if(selection < fresh){
                    s = new ItemStack(stack.getType(),1);
                    break;
                }
            }
        }

        String lore = null;
        if(StorySpirit.random.nextFloat()<0.5 && DataLayer.db.opinions != null && DataLayer.db.opinions.size()>0){
            String villid = (String)DataLayer.db.opinions.keySet().toArray()[StorySpirit.random.nextInt(DataLayer.db.opinions.size())];
            String villiname = DataLayer.db.names.get(villid);
            String[] loreOptions = new String[]{"Property of "+villiname, villiname+"'s lost "+s.getType().toString().replace("_", " ").toLowerCase(),"Belongs to "+villiname};
            lore = loreOptions[StorySpirit.random.nextInt(loreOptions.length)];
            ItemMeta meta = s.getItemMeta();
            meta.setLore(Arrays.asList(new String[]{lore}));
        }


        if(s != null){
            if(StorySpirit.random.nextFloat()<0.5){
                enchantItem(s);
                
                itemTweak(s, Namer.name(), lore,new int[]{StorySpirit.random.nextInt(255),StorySpirit.random.nextInt(255), StorySpirit.random.nextInt(255)});
            }
            return s;
        }

        return null;
    }
    
    static void itemTweak(ItemStack item,String name,String lore,int[] color){
        ItemMeta meta = item.getItemMeta();
        if(name != null && !name.equals("")){meta.setDisplayName(name);}
        if(lore != null && !lore.equals("")){
            ArrayList<String> lorelist = new ArrayList<String>();
            lorelist.add(lore);
            meta.setLore(lorelist);
        }
        item.setItemMeta(meta);
        if(item.getType().equals(Material.LEATHER_BOOTS) || item.getType().equals(Material.LEATHER_HELMET) || item.getType().equals(Material.LEATHER_CHESTPLATE) || item.getType().equals(Material.LEATHER_LEGGINGS)){
            Color colour = Color.fromRGB(color[0],color[1],color[2]);
            LeatherArmorMeta lmeta = (LeatherArmorMeta)item.getItemMeta();
            lmeta.setColor(colour);
            item.setItemMeta(lmeta);
        }
    }
    static void enchantItem(ItemStack item){
        Enchantment e = null;
        int attempts = 0;
        for(int i = 0;i<StorySpirit.random.nextInt(3); i++){
            while(e == null || !e.canEnchantItem(item)){
                e = Enchantment.getById(StorySpirit.random.nextInt(63));
                attempts++;
                if(attempts > 64){
                    return;
                }
            }
            if(e.getMaxLevel()<1){
                item.addEnchantment(e, 1);
            }else{
                item.addEnchantment(e, StorySpirit.random.nextInt(e.getMaxLevel())+1);
            }
        }
    }
}