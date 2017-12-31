package net.machinespirit.storyspirit;


import java.awt.SystemTray;

import org.bukkit.World;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldInitEvent;

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
                event.getPlayer().sendMessage(Conversation.talk((Villager) event.getRightClicked(), event.getPlayer()));
            }
        }
    }
}