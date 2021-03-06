package net.machinespirit.storyspirit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.machinespirit.storyspirit.Namer;
import net.machinespirit.storyspirit.StorySpirit;


public class Character {

static String[] helmnames = new String[]{"Helm","Hat","Cap","Crown","Helmet","Wig"};
static String[] shirtnames = new String[]{"Shirt","Chestplate","Coat","Jumper","Tunic"};
static String[] pantnames = new String[]{"Pants","Trousers","Leggings","Socks","Chaps","Greaves"};
static String[] shoenames = new String[]{"Shoes","Boots","Sandals","Cleats","Feet","Socks"};
static String[] toolnames = new String[]{"Weapon","Tool","Prize","Thing","Toy","Relic","Piece"};





public static void spawn(World world, Location location, String type){
    spawn(world, location, type,StorySpirit.random.nextInt(18));
}

public static void spawn(World world, Location location, String type, Integer points){
        EntityType entityType = null;
        if(type == null || type.equals("")){
            entityType = (EntityType)select(new EntityType[]{EntityType.CHICKEN,EntityType.PIG,EntityType.COW,EntityType.SHEEP,EntityType.CHICKEN,EntityType.PIG,EntityType.COW,EntityType.SHEEP,EntityType.RABBIT,EntityType.WOLF,EntityType.OCELOT,EntityType.HORSE,EntityType.VILLAGER,EntityType.MUSHROOM_COW,EntityType.ZOMBIE,EntityType.SKELETON,EntityType.ZOMBIE,EntityType.SKELETON,EntityType.PIG_ZOMBIE,EntityType.WITHER_SKULL,EntityType.WITCH},points);
        }else if(type.equals("friend")){
            entityType = (EntityType)select(new EntityType[]{EntityType.CHICKEN,EntityType.PIG,EntityType.COW,EntityType.SHEEP,EntityType.CHICKEN,EntityType.PIG,EntityType.COW,EntityType.SHEEP,EntityType.RABBIT,EntityType.WOLF,EntityType.OCELOT,EntityType.HORSE,EntityType.VILLAGER,EntityType.MUSHROOM_COW},points);
        }else if (type.equals("foe")){
            entityType = (EntityType)select(new EntityType[]{EntityType.ZOMBIE,EntityType.SKELETON,EntityType.ZOMBIE,EntityType.SKELETON,EntityType.PIG_ZOMBIE,EntityType.WITCH},points);
        }else{
            entityType = EntityType.fromName(type);
        }
        spawn(world, location, entityType, points);
}


public static void spawn(World world, Location location,EntityType entityType,Integer points){

    LivingEntity entity = (LivingEntity)world.spawnEntity(location, entityType);
    convert(entity, points,Namer.name());
}

public static void convert(LivingEntity entity ,Integer points, String name){
    int startingPoints = points;

    Boolean questTarget = false;

    float modifier = 2;

    if(points > 16){
        name += " the "+Namer.random(Namer.titles);
        modifier = 16;
    }else if(points > 12){
        name += " the "+Namer.random(Namer.adjectives);
        modifier = 8;
    }else if(points > 10){
        name  = Namer.random(Namer.adjectives)+" "+name;
        modifier = 4;
    }


    entity.setMaxHealth(entity.getMaxHealth()*modifier);
    entity.setHealth(entity.getHealth()*modifier);

    entity.setRemoveWhenFarAway(false);

    entity.setCanPickupItems(true);
    entity.setCustomName(name);
    entity.setCustomNameVisible(true);


    ItemStack tool = null;
    if(entity.getType().equals(EntityType.SKELETON)){
        tool = new ItemStack(Material.BOW);
    }
    if(entity.getType().equals(EntityType.ZOMBIE) || entity.getType().equals(EntityType.WITHER_SKULL) || entity.getType().equals(EntityType.PIG_ZOMBIE)){
        tool = new ItemStack((Material) select(new Material[]{Material.WOODEN_AXE,Material.WOODEN_SWORD,Material.IRON_AXE,Material.IRON_SWORD,Material.GOLDEN_AXE,Material.GOLDEN_SWORD,Material.DIAMOND_SWORD},points));
    }
    ItemStack shirt = new ItemStack((Material) select(new Material[]{Material.LEATHER_CHESTPLATE,Material.LEATHER_CHESTPLATE,Material.GOLDEN_CHESTPLATE,Material.IRON_CHESTPLATE,Material.CHAINMAIL_CHESTPLATE,Material.DIAMOND_CHESTPLATE},points));
    ItemStack head = new ItemStack((Material) select(new Material[]{Material.LEATHER_HELMET,Material.LEATHER_HELMET,Material.GOLDEN_HELMET,Material.IRON_HELMET,Material.CHAINMAIL_HELMET,Material.DIAMOND_HELMET},points));
    ItemStack pants = new ItemStack((Material) select(new Material[]{Material.LEATHER_LEGGINGS,Material.LEATHER_LEGGINGS,Material.GOLDEN_LEGGINGS,Material.IRON_LEGGINGS,Material.CHAINMAIL_LEGGINGS,Material.DIAMOND_LEGGINGS},points));
    ItemStack shoes = new ItemStack((Material) select(new Material[]{Material.LEATHER_BOOTS,Material.LEATHER_BOOTS,Material.GOLDEN_BOOTS,Material.IRON_BOOTS,Material.CHAINMAIL_BOOTS,Material.DIAMOND_BOOTS},points));
    
    int[] color1 = new int[]{StorySpirit.random.nextInt(255),StorySpirit.random.nextInt(255),StorySpirit.random.nextInt(255)};
    int[] color2 = new int[]{StorySpirit.random.nextInt(255),StorySpirit.random.nextInt(255),StorySpirit.random.nextInt(255)};

    
    Loot.itemTweak(head,StorySpirit.random.nextInt(8)>1?null:Namer.name(),StorySpirit.random.nextBoolean()?Namer.random(helmnames)+" of "+name:name+"'s "+Namer.random(helmnames),StorySpirit.random.nextBoolean()?color1:color2);
    Loot.itemTweak(shirt,StorySpirit.random.nextInt(8)>1?null:Namer.name(),StorySpirit.random.nextBoolean()?Namer.random(shirtnames)+" of "+name:name+"'s "+Namer.random(shirtnames),StorySpirit.random.nextBoolean()?color1:color2);
    Loot.itemTweak(pants,StorySpirit.random.nextInt(8)>1?null:Namer.name(),StorySpirit.random.nextBoolean()?Namer.random(pantnames)+" of "+name:name+"'s "+Namer.random(pantnames),StorySpirit.random.nextBoolean()?color1:color2);
    Loot.itemTweak(shoes,StorySpirit.random.nextInt(12)>1?null:Namer.name(),StorySpirit.random.nextBoolean()?Namer.random(shoenames)+" of "+name:name+"'s "+Namer.random(shoenames),StorySpirit.random.nextBoolean()?color1:color2);
    if(tool!=null){
        Loot.itemTweak(tool,StorySpirit.random.nextInt(5)<3?null:Namer.name(),StorySpirit.random.nextBoolean()?Namer.random(toolnames)+" of "+name:name+"'s "+Namer.random(toolnames),StorySpirit.random.nextBoolean()?color1:color2);
    }



    while(points > 0){
        if(StorySpirit.random.nextInt(10)>points){
            break;
        }
        Enchantment e = null;
        while(e == null){
            e = Enchantment.values()[StorySpirit.random.nextInt(Enchantment.values().length)];
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
        ((LivingEntity)entity).getEquipment().setHelmetDropChance(0.35f);
        ((LivingEntity)entity).getEquipment().setChestplateDropChance(0.35f);
        ((LivingEntity)entity).getEquipment().setLeggingsDropChance(0.35f);
        ((LivingEntity)entity).getEquipment().setBootsDropChance(0.35f);
        ((LivingEntity)entity).getEquipment().setItemInHandDropChance(0.35f);
    }

    if(entity instanceof Creeper){
        Creeper creep = (Creeper)entity;
        creep.setExplosionRadius((int) (creep.getExplosionRadius()*modifier));
    }
    if(entity instanceof Slime){
        Slime slime = (Slime)entity;
        slime.setSize((int) (slime.getSize()*modifier*0.5));
    }
    if(entity instanceof MagmaCube){
        MagmaCube magmaCube = (MagmaCube)entity;
        magmaCube.setSize((int) (magmaCube.getSize()*modifier*0.5));
    }
    if(entity instanceof Witch){
        if(StorySpirit.random.nextBoolean()){
        entity.addScoreboardTag("nice");
        }
    }


    if(entity instanceof Pig || entity instanceof Cow || entity instanceof Sheep || entity instanceof Llama || entity instanceof Rabbit || entity instanceof Horse || entity instanceof Squid || entity instanceof Chicken || entity instanceof Bat || entity instanceof Wolf || entity instanceof Ocelot || entity instanceof Parrot){
        ArrayList<PotionEffectType> types = new ArrayList<PotionEffectType>(Arrays.asList(PotionEffectType.DAMAGE_RESISTANCE,PotionEffectType.FAST_DIGGING,PotionEffectType.HEAL,PotionEffectType.FIRE_RESISTANCE,PotionEffectType.GLOWING,PotionEffectType.HEALTH_BOOST,PotionEffectType.INVISIBILITY,PotionEffectType.JUMP,PotionEffectType.LEVITATION,PotionEffectType.INCREASE_DAMAGE,PotionEffectType.REGENERATION,PotionEffectType.NIGHT_VISION,PotionEffectType.LUCK,PotionEffectType.SPEED,PotionEffectType.WATER_BREATHING, PotionEffectType.BLINDNESS,PotionEffectType.SLOW,PotionEffectType.CONFUSION));
        LivingEntity friend = (LivingEntity)entity;
        if(DataLayer.getBlessing(friend) == null){
            DataLayer.setBlessing(friend, types.get(StorySpirit.random.nextInt(types.size())));
        }
        DataLayer.setLost(entity);
        friend.addScoreboardTag("friend");
    }else if(!(entity instanceof Villager) && startingPoints > 10){
        ((LivingEntity)entity).addScoreboardTag("foe");
        DataLayer.setBoss(entity);
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


public static void villiconvert(Villager v){
    //if(Character.characters.containsKey(v.getPersistentID())){
    if(v.isCustomNameVisible()){
        
    }else{
        v.setCustomName(Namer.name());
        v.setCustomNameVisible(true);
    }
}

// public static float villiOpinion(Villager v, Player p){
//     if(v.getMetadata(arg0))
// }


}