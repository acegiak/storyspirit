package net.machinespirit.storyspirit;


import java.awt.SystemTray;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.text.html.parser.Entity;

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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.inventory.ItemStack;
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
                    if(event.getPlayer().getItemInHand().getItemMeta().getLore().get(0).contains(event.getRightClicked().getCustomName())){
                        Quest.complete(event.getPlayer(), "Deliver "+event.getPlayer().getItemInHand().getItemMeta().getDisplayName()+" to "+event.getRightClicked().getCustomName());
                        event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
                        delivered = true;
                    }
                }catch(NullPointerException e){
                    
                }
                if(!delivered){
                    event.getPlayer().sendMessage(Conversation.talk((Villager) event.getRightClicked(), event.getPlayer()));
                }
            }
        }
        PotionEffectType type = DataLayer.getBlessing(event.getRightClicked());
        if(event.getPlayer().isSneaking() && type != null){
            event.getPlayer().addPotionEffect(new PotionEffect(type,20*90,1));
            event.getRightClicked().getWorld().spawnParticle(Particle.SPELL_WITCH, event.getRightClicked().getLocation().add(0, 0.5f, 0),10);
        }
        if(event.getRightClicked().getScoreboardTags().contains("friend")){
            for (org.bukkit.entity.Entity e : event.getRightClicked().getNearbyEntities(25, 10, 25)) {
                if(e instanceof Villager){
                    event.getRightClicked().removeScoreboardTag("friend");
                    Quest.complete(event.getPlayer(), "Deliver "+event.getRightClicked().getCustomName()+" to safety");
                    break;
                }
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

                Quest.complete(event.getEntity().getKiller(), "Defeat "+event.getEntity().getCustomName());
            }
        }

    }

    @EventHandler
    public void onTrade(InventoryClickEvent event){
        if(event.getInventory().getType().equals(InventoryType.MERCHANT) && event.getWhoClicked() instanceof Player){
            Conversation.publicOpinion((Player)event.getWhoClicked(), 0.05f);
        }
    }
}