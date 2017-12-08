package net.machinespirit.storyspirit;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class StoryListener implements Listener
{
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event)
    {
        System.out.println("creaturespawn");
        if(StorySpirit.random.nextFloat() < 0.1f){
            System.out.println("replaced");
            event.setCancelled(true);
            Character.spawn(event.getLocation().getWorld(), event.getLocation(),event.getEntityType(),StorySpirit.random.nextInt(18));
        }
    }

}