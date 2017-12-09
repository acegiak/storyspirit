package net.machinespirit.storyspirit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import java.util.ArrayList;
import java.util.List;
import net.machinespirit.storyspirit.Namer;
import net.machinespirit.storyspirit.StorySpirit;


public class Character {

static String[] helmnames = new String[]{"Helm","Hat","Cap","Crown","Helmet","Wig"};
static String[] shirtnames = new String[]{"Shirt","Chestplate","Coat","Jumper","Tunic"};
static String[] pantnames = new String[]{"Pants","Trousers","Leggings","Socks","Chaps","Greaves"};
static String[] shoenames = new String[]{"Shoes","Boots","Sandals","Cleats","Feet","Socks"};
static String[] toolnames = new String[]{"Weapon","Tool","Prize","Thing","Toy","Relic","Piece"};





public static void spawn(World world, Location location, String type){
    spawn(world, location, type,StorySpirit.random.nextInt(10));
}

public static void spawn(World world, Location location, String type, Integer points){
        EntityType entityType = null;
        if(type == null || type.equals("")){
            entityType = (EntityType)select(new EntityType[]{EntityType.CHICKEN,EntityType.PIG,EntityType.COW,EntityType.SHEEP,EntityType.CHICKEN,EntityType.PIG,EntityType.COW,EntityType.SHEEP,EntityType.RABBIT,EntityType.WOLF,EntityType.OCELOT,EntityType.HORSE,EntityType.VILLAGER,EntityType.MUSHROOM_COW,EntityType.ZOMBIE,EntityType.SKELETON,EntityType.ZOMBIE,EntityType.SKELETON,EntityType.PIG_ZOMBIE,EntityType.WITHER_SKULL,EntityType.WITCH},points);
        }else if(type.equals("friend")){
            entityType = (EntityType)select(new EntityType[]{EntityType.CHICKEN,EntityType.PIG,EntityType.COW,EntityType.SHEEP,EntityType.CHICKEN,EntityType.PIG,EntityType.COW,EntityType.SHEEP,EntityType.RABBIT,EntityType.WOLF,EntityType.OCELOT,EntityType.HORSE,EntityType.VILLAGER,EntityType.MUSHROOM_COW},points);
        }else if (type.equals("foe")){
            points = StorySpirit.random.nextInt(18);
            entityType = (EntityType)select(new EntityType[]{EntityType.ZOMBIE,EntityType.SKELETON,EntityType.ZOMBIE,EntityType.SKELETON,EntityType.PIG_ZOMBIE,EntityType.WITCH},points);
        }else{
            entityType = EntityType.fromName(type);
        }
        spawn(world, location, entityType, points);
}

public static void spawn(World world, Location location,EntityType entityType,Integer points){
    Entity entity = world.spawnEntity(location, entityType);

    String name =Namer.name();
    if(points > 16){
        name += " the "+Namer.random(Namer.titles);
    }else if(points > 12){
        name += " the "+Namer.random(Namer.adjectives);
    }else if(points > 10){
        name  = Namer.random(Namer.adjectives)+" "+name;
    }
    entity.setCustomName(name);
    entity.setCustomNameVisible(true);


    ItemStack tool = null;
    if(entityType== EntityType.SKELETON){
        tool = new ItemStack(Material.BOW);
    }
    if(entityType==EntityType.ZOMBIE || entityType==EntityType.WITHER_SKULL || entityType==EntityType.PIG_ZOMBIE){
        tool = new ItemStack((Material) select(new Material[]{Material.WOOD_AXE,Material.WOOD_SWORD,Material.IRON_AXE,Material.IRON_SWORD,Material.GOLD_AXE,Material.GOLD_SWORD,Material.DIAMOND_SWORD},points));
    }
    ItemStack shirt = new ItemStack((Material) select(new Material[]{Material.LEATHER_CHESTPLATE,Material.LEATHER_CHESTPLATE,Material.GOLD_CHESTPLATE,Material.IRON_CHESTPLATE,Material.CHAINMAIL_CHESTPLATE,Material.DIAMOND_CHESTPLATE},points));
    ItemStack head = new ItemStack((Material) select(new Material[]{Material.LEATHER_HELMET,Material.LEATHER_HELMET,Material.GOLD_HELMET,Material.IRON_HELMET,Material.CHAINMAIL_HELMET,Material.DIAMOND_HELMET},points));
    ItemStack pants = new ItemStack((Material) select(new Material[]{Material.LEATHER_LEGGINGS,Material.LEATHER_LEGGINGS,Material.GOLD_LEGGINGS,Material.IRON_LEGGINGS,Material.CHAINMAIL_LEGGINGS,Material.DIAMOND_LEGGINGS},points));
    ItemStack shoes = new ItemStack((Material) select(new Material[]{Material.LEATHER_BOOTS,Material.LEATHER_BOOTS,Material.GOLD_BOOTS,Material.IRON_BOOTS,Material.CHAINMAIL_BOOTS,Material.DIAMOND_BOOTS},points));
    
    int[] color1 = new int[]{StorySpirit.random.nextInt(255),StorySpirit.random.nextInt(255),StorySpirit.random.nextInt(255)};
    int[] color2 = new int[]{StorySpirit.random.nextInt(255),StorySpirit.random.nextInt(255),StorySpirit.random.nextInt(255)};

    
    itemTweak(head,StorySpirit.random.nextInt(8)>1?null:Namer.name(),StorySpirit.random.nextBoolean()?Namer.random(helmnames)+" of "+name:name+"'s "+Namer.random(helmnames),StorySpirit.random.nextBoolean()?color1:color2);
    itemTweak(shirt,StorySpirit.random.nextInt(8)>1?null:Namer.name(),StorySpirit.random.nextBoolean()?Namer.random(shirtnames)+" of "+name:name+"'s "+Namer.random(shirtnames),StorySpirit.random.nextBoolean()?color1:color2);
    itemTweak(pants,StorySpirit.random.nextInt(8)>1?null:Namer.name(),StorySpirit.random.nextBoolean()?Namer.random(pantnames)+" of "+name:name+"'s "+Namer.random(pantnames),StorySpirit.random.nextBoolean()?color1:color2);
    itemTweak(shoes,StorySpirit.random.nextInt(12)>1?null:Namer.name(),StorySpirit.random.nextBoolean()?Namer.random(shoenames)+" of "+name:name+"'s "+Namer.random(shoenames),StorySpirit.random.nextBoolean()?color1:color2);
    if(tool!=null){
    itemTweak(tool,StorySpirit.random.nextInt(5)<3?null:Namer.name(),StorySpirit.random.nextBoolean()?Namer.random(toolnames)+" of "+name:name+"'s "+Namer.random(toolnames),StorySpirit.random.nextBoolean()?color1:color2);
    }



    while(points > 0){
        if(StorySpirit.random.nextInt(10)>points){
            break;
        }
        Enchantment e = null;
        while(e == null){
            e = Enchantment.getById(StorySpirit.random.nextInt(63));
        }
        int l = 0;
        switch(StorySpirit.random.nextInt(5)){
        case 0:
            if(head != null && e.canEnchantItem(head)){
                l = StorySpirit.random.nextInt(e.getMaxLevel())+1;
                head.addEnchantment(e, l);
            }
            break;
        case 1:
            if(shirt != null && e.canEnchantItem(shirt)){
                l = StorySpirit.random.nextInt(e.getMaxLevel())+1;
                shirt.addEnchantment(e, l);
            }
            break;
        case 2:
            if(pants != null && e.canEnchantItem(pants)){
                l = StorySpirit.random.nextInt(e.getMaxLevel())+1;
                pants.addEnchantment(e, l);
            }
            break;
        case 3:
            if(shoes != null && e.canEnchantItem(shoes)){
                l = StorySpirit.random.nextInt(e.getMaxLevel())+1;
                shoes.addEnchantment(e, l);
            }
            break;
        case 4:
            if(tool != null && e.canEnchantItem(tool)){
                l = StorySpirit.random.nextInt(e.getMaxLevel())+1;
                tool.addEnchantment(e, l);
            }
            break;
        }
        points -= l;
    }

    if(entity instanceof LivingEntity){
        ((LivingEntity)entity).getEquipment().setHelmet(head);
        ((LivingEntity)entity).getEquipment().setChestplate(shirt);
        ((LivingEntity)entity).getEquipment().setLeggings(pants);
        ((LivingEntity)entity).getEquipment().setBoots(shoes);
        ((LivingEntity)entity).getEquipment().setItemInHand(tool);
        ((LivingEntity)entity).getEquipment().setHelmetDropChance(0.3f);
        ((LivingEntity)entity).getEquipment().setChestplateDropChance(0.3f);
        ((LivingEntity)entity).getEquipment().setLeggingsDropChance(0.3f);
        ((LivingEntity)entity).getEquipment().setBootsDropChance(0.3f);
        ((LivingEntity)entity).getEquipment().setItemInHandDropChance(0.3f);
    }

}

static Object select(Object[] things, Integer points){
    int acc = 0;
    for(int i = things.length-1;i >= 0; i--){
        acc = 2^i;
        if(StorySpirit.random.nextInt(acc+2)<1 && points > i){
            points -= i;
            return things[i];
        }
    }
    return things[0];
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



}