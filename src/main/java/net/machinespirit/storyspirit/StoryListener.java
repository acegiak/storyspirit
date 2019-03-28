package net.machinespirit.storyspirit;


import java.awt.SystemTray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.text.html.parser.Entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.reflect.ClassPath.ResourceInfo;

public class StoryListener implements Listener
{
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event)
    {
        if(StorySpirit.random.nextFloat() < 1f/150f || (event.getEntity() instanceof Witch && StorySpirit.random.nextFloat()<0.1f)){
            event.setCancelled(true);
            Character.spawn(event.getLocation().getWorld(), event.getLocation(),event.getEntityType(),StorySpirit.random.nextInt(18));
        }
        if(event.getEntity() instanceof Villager){
            Character.villiconvert((Villager)event.getEntity());
        }
    }
    
    @EventHandler
    public void onWorldInit(WorldInitEvent event)
    {
        World world = event.getWorld();
        world.getPopulators().add(new DungeonPopulator());

    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event){

        System.out.println("player interacted!");
        if(event.getRightClicked() instanceof Villager){
            System.out.println("villiclicked");
            Character.villiconvert((Villager)event.getRightClicked());
            if(event.getPlayer().isSneaking()){
                event.setCancelled(true);
                Boolean delivered = false;
                try{
                    if(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(0).contains(event.getRightClicked().getCustomName())){
                        Quest.complete(event.getPlayer(), "delivered "+event.getPlayer().getItemInHand().getItemMeta().getDisplayName()+(event.getPlayer().getItemInHand().getItemMeta().getDisplayName().length()>10?"":" to "+event.getRightClicked().getCustomName()));
                        event.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                        delivered = true;
                    }
                }catch(NullPointerException e){
                    
                }
                if(!delivered){
                    event.getPlayer().sendMessage(Conversation.talk((Villager) event.getRightClicked(), event.getPlayer()));
                }
            }
        }
        if(event.getRightClicked().getScoreboardTags().contains("nice")&&event.getRightClicked() instanceof Witch){
            Merchant m = Bukkit.createMerchant(event.getRightClicked().getCustomName());

            Material[] ingredients = new Material[]{Material.SPIDER_EYE,Material.LILY_PAD,Material.POISONOUS_POTATO,Material.SLIME_BALL,Material.APPLE,Material.VINE,Material.RED_MUSHROOM,Material.BONE,Material.ENDER_PEARL,Material.FERMENTED_SPIDER_EYE,Material.BROWN_MUSHROOM,Material.ROTTEN_FLESH,Material.MUTTON,Material.BEETROOT,Material.PORKCHOP,Material.BEEF,Material.FLINT,Material.BLAZE_ROD,Material.CLAY_BALL,Material.COAL,Material.WHEAT_SEEDS,Material.WHEAT,Material.POTATO,Material.LEGACY_RAW_FISH,Material.SNOWBALL,Material.REDSTONE,Material.STICK,Material.FLINT,Material.WHEAT,Material.PAPER,Material.STRING,Material.SUGAR_CANE,Material.SUGAR,Material.QUARTZ,Material.LEATHER,Material.LAVA_BUCKET,Material.BREAD,Material.MILK_BUCKET,Material.GOLD_NUGGET,Material.GHAST_TEAR,Material.EGG,Material.WHITE_WOOL,Material.RABBIT_FOOT,Material.FEATHER,Material.RABBIT_HIDE,Material.FLOWER_POT};
            Material[] offered = new Material[]{Material.LEATHER_LEGGINGS,Material.LEATHER_BOOTS,Material.APPLE,Material.BEETROOT_SOUP,Material.EMERALD,Material.PAINTING,Material.CAULDRON,Material.ENDER_EYE,Material.GOLDEN_APPLE,Material.GOLDEN_CARROT,Material.BUCKET,Material.FLOWER_POT,Material.OAK_BOAT,Material.WRITABLE_BOOK,Material.BOOKSHELF,Material.COOKIE,Material.CAKE,Material.BEETROOT_SOUP,Material.CARROT_ON_A_STICK,Material.FIREWORK_ROCKET,Material.JUKEBOX,Material.MAP,Material.LEAD,Material.GLOWSTONE,Material.MOSSY_COBBLESTONE,Material.PUMPKIN_PIE,Material.RABBIT_STEW,Material.SEA_LANTERN,Material.SHIELD,Material.WHITE_BANNER,Material.TOTEM_OF_UNDYING};
            List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();

            if(DataLayer.witchInventories.containsKey(event.getRightClicked().getUniqueId())){
                recipes = DataLayer.witchInventories.get(event.getRightClicked().getUniqueId());
            }else{
                int count = StorySpirit.random.nextInt(7)+1;
                for(int i = 0;i<count;i++){
                    ItemStack item = null;
                    float choicespace = StorySpirit.random.nextFloat();
                    int cost =1;
                    int reward = 1;
                    int r2 = 1;
                    while(StorySpirit.random.nextBoolean()){
                        if(cost ==1){cost =0;}
                        cost +=3;
                    }
                    while(StorySpirit.random.nextBoolean()){
                        if(reward ==1){reward =0;}
                        reward +=3;
                    }
                    while(StorySpirit.random.nextBoolean()){
                        if(r2 ==1){r2 =0;}
                        r2 +=3;
                    }
                    if(choicespace<0.8f){
                        item = new ItemStack(Material.LEGACY_POTION, StorySpirit.random.nextInt(reward), (short) (8193+StorySpirit.random.nextInt(13)));
                    }else if(choicespace<0.95f){
                        item = new ItemStack(offered[StorySpirit.random.nextInt(offered.length)], reward);
                    }else{
                        Material[] eggs = new Material[]{
                            Material.ELDER_GUARDIAN_SPAWN_EGG,
                            Material.ENDERMAN_SPAWN_EGG,
                            Material.ENDERMITE_SPAWN_EGG,
                            Material.EVOKER_SPAWN_EGG,
                            Material.BAT_SPAWN_EGG,
                            Material.BLAZE_SPAWN_EGG,
                            Material.CAVE_SPIDER_SPAWN_EGG,
                            Material.COD_SPAWN_EGG,
                            Material.CHICKEN_SPAWN_EGG,
                            Material.COW_SPAWN_EGG,
                            Material.CREEPER_SPAWN_EGG,
                            Material.DOLPHIN_SPAWN_EGG,
                            Material.DONKEY_SPAWN_EGG,
                            Material.DROWNED_SPAWN_EGG,
                            Material.GHAST_SPAWN_EGG,
                            Material.GUARDIAN_SPAWN_EGG,
                            Material.HORSE_SPAWN_EGG,
                            Material.HUSK_SPAWN_EGG,
                            Material.LLAMA_SPAWN_EGG,
                            Material.MAGMA_CUBE_SPAWN_EGG,
                            Material.MOOSHROOM_SPAWN_EGG,
                            Material.MULE_SPAWN_EGG,
                            Material.OCELOT_SPAWN_EGG,
                            Material.PARROT_SPAWN_EGG,
                            Material.PHANTOM_SPAWN_EGG,
                            Material.PIG_SPAWN_EGG,
                            Material.POLAR_BEAR_SPAWN_EGG,
                            Material.PUFFERFISH_SPAWN_EGG,
                            Material.RABBIT_SPAWN_EGG,
                            Material.SALMON_SPAWN_EGG,
                            Material.SHEEP_SPAWN_EGG,
                            Material.SHULKER_SPAWN_EGG,
                            Material.SILVERFISH_SPAWN_EGG,
                            Material.SKELETON_HORSE_SPAWN_EGG,
                            Material.SKELETON_SPAWN_EGG,
                            Material.SLIME_SPAWN_EGG,
                            Material.SPIDER_SPAWN_EGG,
                            Material.SQUID_SPAWN_EGG,
                            Material.STRAY_SPAWN_EGG,
                            Material.TROPICAL_FISH_SPAWN_EGG,
                            Material.TURTLE_SPAWN_EGG,
                            Material.VEX_SPAWN_EGG,
                            Material.VILLAGER_SPAWN_EGG,
                            Material.VINDICATOR_SPAWN_EGG,
                            Material.WITCH_SPAWN_EGG,
                            Material.WITHER_SKELETON_SPAWN_EGG,
                            Material.WOLF_SPAWN_EGG,
                            Material.ZOMBIE_HORSE_SPAWN_EGG,
                            Material.ZOMBIE_PIGMAN_SPAWN_EGG,
                            Material.ZOMBIE_SPAWN_EGG,
                            Material.ZOMBIE_VILLAGER_SPAWN_EGG
                        };
                        item = new ItemStack(eggs[StorySpirit.random.nextInt(eggs.length)],1);
                    }
                    int c = item.getAmount()%item.getType().getMaxStackSize();
                    if(c>1){
                        c = c-(c%7);
                    }
                    
                    item.setAmount(Math.max(1,c));
                    
                    System.out.println("RECIEVE:");
                    System.out.println(Integer.toString(item.getAmount())+item.getType().toString());
                    MerchantRecipe recipe = new MerchantRecipe( item, StorySpirit.random.nextInt(5)+3);
                    ItemStack costStack = new ItemStack(ingredients[StorySpirit.random.nextInt(ingredients.length)],StorySpirit.random.nextInt(64));
                    int d = costStack.getAmount()%costStack.getType().getMaxStackSize();
                    if(d>1){
                        d =  d-(d%7);
                    }
                    
                    costStack.setAmount(Math.max(1,d));
                    

                    System.out.println("IN EXCHANGE FOR:");
                    System.out.println(Integer.toString(costStack.getAmount())+costStack.getType().toString());
                    recipe.addIngredient(costStack);
                    if(StorySpirit.random.nextBoolean()){
                        ItemStack costStack2 = new ItemStack(ingredients[StorySpirit.random.nextInt(ingredients.length)],StorySpirit.random.nextInt(64));
                        int e = costStack2.getAmount()%costStack2.getType().getMaxStackSize();
                        if(e>1){
                            e = e-(e%7);
                        }
                        
                        costStack2.setAmount(Math.max(1,e));
                        
                        recipe.addIngredient(costStack2);    

                        System.out.println(Integer.toString(costStack2.getAmount())+costStack2.getType().toString());                }
                    recipes.add(i, recipe);
                }
                DataLayer.witchInventories.put(event.getRightClicked().getUniqueId(), recipes);
            }
            m.setRecipes(recipes);
            event.getPlayer().openMerchant(m, false);

        }

        PotionEffectType type = DataLayer.getBlessing(event.getRightClicked());
        boolean rescued = false;
        if(event.getPlayer().isSneaking() && type != null){
            if(event.getRightClicked().getScoreboardTags().contains("friend")){
                for (org.bukkit.entity.Entity e : event.getRightClicked().getNearbyEntities(15, 10, 15)) {
                    if(e instanceof Villager){
                        event.getRightClicked().removeScoreboardTag("friend");
                        Quest.complete(event.getPlayer(), "saved "+event.getRightClicked().getCustomName());
                        if(DataLayer.db.lostFriends.containsKey(event.getRightClicked().getUniqueId().toString())){
                            DataLayer.db.lostFriends.remove(event.getRightClicked().getUniqueId().toString());
                        }
                        rescued = true;
                        break;
                    }
                }
    
            }
            if(!rescued){
                event.getPlayer().addPotionEffect(new PotionEffect(type,20*90*(event.getRightClicked().getCustomName().split(" ").length),1));
                event.getRightClicked().getWorld().spawnParticle(Particle.SPELL_WITCH, event.getRightClicked().getLocation().add(0, 0.5f, 0),10);
            }
        }
        
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        if(event.getEntityType() == EntityType.ZOMBIE || event.getEntityType() == EntityType.ZOMBIE_VILLAGER || event.getEntityType() == EntityType.SKELETON || event.getEntityType() == EntityType.CREEPER || event.getEntityType() == EntityType.PIG_ZOMBIE|| event.getEntityType() == EntityType.BLAZE|| event.getEntityType() == EntityType.CAVE_SPIDER|| event.getEntityType() == EntityType.SPIDER || event.getEntityType() == EntityType.ENDERMAN){
            if(event.getEntity().getKiller() != null && event.getEntity().getKiller() instanceof Player){
                Conversation.publicOpinion(event.getEntity().getKiller(), 0.10f);
            }
        }
        if(event.getEntity().getScoreboardTags().contains("foe")){
            if(event.getEntity().getKiller() != null && event.getEntity().getKiller() instanceof Player){

                Quest.complete(event.getEntity().getKiller(), "slew "+event.getEntity().getCustomName());


                if(DataLayer.db.bosses.containsKey(event.getEntity().getUniqueId().toString())){
                    DataLayer.db.bosses.remove(event.getEntity().getUniqueId().toString());
                }
            }
        }

    }

    @EventHandler
    public void onTrade(InventoryClickEvent event){
        if(event.getInventory().getType().equals(InventoryType.MERCHANT) && event.getWhoClicked() instanceof Player){
            Conversation.publicOpinion((Player)event.getWhoClicked(), 0.05f);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event){
        if(event.getEntity() instanceof Witch && event.getEntity().getScoreboardTags().contains("nice")&&event.getTarget() instanceof Player){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Monster && event.getDamager().isCustomNameVisible()){
            event.setDamage(event.getDamage()*(1+event.getDamager().getCustomName().split(" ").length));
        }
    }
    @EventHandler
    public void onEntityShoot(EntityShootBowEvent event){
        if(event.getEntity() instanceof Monster && event.getEntity().isCustomNameVisible() && event.getProjectile() instanceof Arrow){
            Arrow arrow = (Arrow)event.getProjectile();
            arrow.setKnockbackStrength(arrow.getKnockbackStrength()*(event.getEntity().getCustomName().split(" ").length));
        }
    }



    @EventHandler void onSlimeSplit(SlimeSplitEvent event){
        if(event.getEntity().isCustomNameVisible() && event.getEntity().getCustomName() != null){
            event.setCancelled(true);
            int i = event.getEntity().getSize();
            String[] name = event.getEntity().getCustomName().split(" ");
            Boolean flipped = false;
            if(name.length >1 && Arrays.asList(Namer.adjectives).contains(name[0])){
                String hold = new String(name[0]);
                name[0] = new String(name[1]);
                name[1] = new String(hold);
                flipped = true;
            }
            String baseName = new String(name[0]);
            System.out.println("BASE NAME IS:"+baseName);
            String[] sparename = name.clone();
            for (int k = 0; k < event.getCount(); ++k) {
                int slice =  Math.max(2,StorySpirit.random.nextInt(Math.max(1,(int) (baseName.length()-1))));
                if(k < event.getCount()-1&&slice <baseName.length()){
                    String namePart = baseName.substring(0, slice);
                    if(namePart.length() <1){
                        namePart = Namer.name();
                    }else{
                        baseName = baseName.substring(slice);
                        if(namePart.length() >1){
                            namePart = namePart.substring(0,1).toUpperCase()+namePart.substring(1).toLowerCase();
                        }else{
                            namePart = namePart.toUpperCase();
                        }
                    }
                    sparename[0] = namePart;
                }else{
                    sparename[0] = baseName.substring(0,1).toUpperCase()+baseName.substring(1).toLowerCase();
                }
                String[] flipname = sparename.clone();
                if(flipped){
                    String hold = new String(flipname[0]);
                    flipname[0] = new String(flipname[1]);
                    flipname[1] = new String(hold);
                }

                String newName = String.join(" ",flipname);
                float f = ((float) (k % 2) - 0.5F) * (float) i / 4.0F;
                float f1 = ((float) (k / 2) - 0.5F) * (float) i / 4.0F;
                Location l = new Location(event.getEntity().getWorld(), event.getEntity().getLocation().getX() + (double) f, event.getEntity().getLocation().getY() + 0.5D, event.getEntity().getLocation().getZ() + (double) f1, StorySpirit.random.nextFloat() * 360.0F, 0.0F);

                Slime entityslime = (Slime) event.getEntity().getWorld().spawnEntity(l, EntityType.SLIME);

                entityslime.setSize(i / 2);
                entityslime.setCustomName(newName);
                entityslime.setCustomNameVisible(true);;
                
            }
        }
    }

    @EventHandler void onBreed(EntityBreedEvent event){

        System.out.println("A BREED HAPPEN");
        ArrayList<String> names = new ArrayList<String>();
        if(event.getMother().isCustomNameVisible()){
            String name[] = event.getMother().getCustomName().split(" ");
            if(name.length >1 && Arrays.asList(Namer.adjectives).contains(name[0])){
                String hold = new String(name[0]);
                name[0] = new String(name[1]);
                name[1] = new String(hold);
            }
            names.add(name[0]);
            names.add(name[0]);
        }
        if(event.getFather().isCustomNameVisible()){
            String name[] = event.getFather().getCustomName().split(" ");
            if(name.length >1 && Arrays.asList(Namer.adjectives).contains(name[0])){
                String hold = new String(name[0]);
                name[0] = new String(name[1]);
                name[1] = new String(hold);
            }
            names.add(name[0]);
            names.add(name[0]);
        }
        if(names.size() <= 0){
            return;
        }
        names.add(Namer.name().toLowerCase());
        String newName = "";
        while(true){
            String sample = names.get(StorySpirit.random.nextInt(names.size()));
            System.out.println("CHOSE:"+sample);
            int part1 = StorySpirit.random.nextInt((int) ((sample.length()-1)*0.5));
            int part2 = StorySpirit.random.nextInt(sample.length()-1-part1)+1;
            String newSample = sample.substring(part1,part1+part2);

            System.out.println("SLICE:"+newSample);
            newName = newName+newSample;

            System.out.println("NOW:"+newName);
            if(newName.length() > 5 && (newName.length()>10 || StorySpirit.random.nextBoolean())){
                break;
            }
        }
        newName = newName.substring(0,1).toUpperCase()+newName.substring(1).toLowerCase();
        System.out.println("NAME IS:"+newName);
        
            Character.convert(event.getEntity(), StorySpirit.random.nextInt(18),newName);
        
    }

    


}