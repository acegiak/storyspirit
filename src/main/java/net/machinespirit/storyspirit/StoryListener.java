package net.machinespirit.storyspirit;


import java.awt.SystemTray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.text.html.parser.Entity;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
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

public class StoryListener implements Listener
{
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event)
    {
        if(StorySpirit.random.nextFloat() < 1f/150f){
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
                        Quest.complete(event.getPlayer(), "Deliver "+event.getPlayer().getItemInHand().getItemMeta().getDisplayName()+" to "+event.getRightClicked().getCustomName());
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

            Material[] ingredients = new Material[]{Material.SPIDER_EYE,Material.WATER_LILY,Material.SLIME_BALL,Material.APPLE};
            Material[] offered = new Material[]{Material.LEATHER_LEGGINGS,Material.LEATHER_BOOTS,Material.APPLE,Material.BEETROOT_SOUP,Material.EMERALD,Material.PAINTING};
            List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();

            if(DataLayer.witchInventories.containsKey(event.getRightClicked().getUniqueId())){
                recipes = DataLayer.witchInventories.get(event.getRightClicked().getUniqueId());
            }else{
                int count = StorySpirit.random.nextInt(7)+1;
                for(int i = 0;i<count;i++){
                    ItemStack item = null;
                    float choicespace = StorySpirit.random.nextFloat();
                    if(choicespace<0.6f){
                        item = new ItemStack(373, StorySpirit.random.nextInt(16), (short) (8193+StorySpirit.random.nextInt(13)));
                    }else if(choicespace<0.8f){
                        item = new ItemStack(offered[StorySpirit.random.nextInt(offered.length)], StorySpirit.random.nextInt(16));
                    }else{
                        int[] eggs = new int[]{4,5,6,23,27,34,35,36,37,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,66,67,68,69,28,29,31,32,65,90,91,92,93,94,95,96,97,98,99,100,101,102,103,105,120};
                        item = new ItemStack(Material.MONSTER_EGG,1,(short)eggs[StorySpirit.random.nextInt(eggs.length)]);
                    }
                    MerchantRecipe recipe = new MerchantRecipe( item, StorySpirit.random.nextInt(5)+3);
                    int icount = StorySpirit.random.nextInt(2)+1;
                    for(int j=0;j<icount;j++){
                        recipe.addIngredient(new ItemStack(ingredients[StorySpirit.random.nextInt(ingredients.length)],StorySpirit.random.nextInt(64)));
                    }
                    recipes.add(i, recipe);
                }
                DataLayer.witchInventories.put(event.getRightClicked().getUniqueId(), recipes);
            }
            m.setRecipes(recipes);
            event.getPlayer().openMerchant(m, false);

        }

        PotionEffectType type = DataLayer.getBlessing(event.getRightClicked());
        if(event.getPlayer().isSneaking() && type != null){
            if(event.getRightClicked().getScoreboardTags().contains("friend")){
                for (org.bukkit.entity.Entity e : event.getRightClicked().getNearbyEntities(25, 10, 25)) {
                    if(e instanceof Villager){
                        event.getRightClicked().removeScoreboardTag("friend");
                        Quest.complete(event.getPlayer(), "Deliver "+event.getRightClicked().getCustomName()+" to safety");
                        if(DataLayer.db.lostFriends.containsKey(event.getRightClicked().getUniqueId().toString())){
                            DataLayer.db.lostFriends.remove(event.getRightClicked().getUniqueId().toString());
                        }
                        break;
                    }
                }
    
            }
            
                event.getPlayer().addPotionEffect(new PotionEffect(type,20*90*(event.getRightClicked().getCustomName().split(" ").length),1));
                event.getRightClicked().getWorld().spawnParticle(Particle.SPELL_WITCH, event.getRightClicked().getLocation().add(0, 0.5f, 0),10);
            
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

                Quest.complete(event.getEntity().getKiller(), "Defeat "+event.getEntity().getCustomName());


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
}