package net.machinespirit.storyspirit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class StorySpirit extends JavaPlugin {
    public static Random random;

    public static StorySpirit current;

    public Map<String, Object> worlds = new HashMap<String, Object>();
	public boolean usePermissions;
	public Map<String, Object> lang;
    public String version = getServer().getClass().getName().split("\\.")[3];


    @Override
    public void onDisable() {
        // Don't log disabling, Spigot does that for you automatically!
    }

    @Override
    public void onEnable() {
        current = this;
        // Don't log enabling, Spigot does that for you automatically!

        // Commands enabled with following method must have entries in plugin.yml
        getCommand("name").setExecutor(new StoryCommander(this));
        getCommand("spawn").setExecutor(new StoryCommander(this));

        getServer().getPluginManager().registerEvents(new StoryListener(), this);

        //VILLAGE INFO

        // get config
		// this.saveDefaultConfig();
		// usePermissions = getConfig().getBoolean("usePermissions");
		// lang = getConfig().getConfigurationSection("msg").getValues(false);
		// getLogger().info("Using permissions: "+usePermissions);		
		
		// create a map of WorldServers by its name, for later quick search
	    try
		{
			Object minecraftServer = Class.forName("net.minecraft.server."+version+".MinecraftServer").getMethod("getServer").invoke(null);
			for (Object worldServer : (List<Object>) minecraftServer.getClass().getField("worlds").get(minecraftServer))
			{
				Object craftWorld = worldServer.getClass().getSuperclass().getMethod("getWorld").invoke(worldServer);
				this.worlds.put((String) craftWorld.getClass().getMethod("getName").invoke(craftWorld), worldServer);
			}
		}
		catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e){e.printStackTrace();}

        random = new Random();
        String worldName = getServer().getName();
        DataLayer.onLoad(getDataFolder(),worldName);


    }


}
