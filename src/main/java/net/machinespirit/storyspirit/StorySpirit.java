package net.machinespirit.storyspirit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class StorySpirit extends JavaPlugin {
    public static Random random;

    public static StorySpirit current;

    public Map<String, Object> worlds = new HashMap<String, Object>();
	public boolean usePermissions;
	public Map<String, Object> lang;
    public String version = getServer().getClass().getName().split("\\.")[3];

    public static String toTitleCase (String s){
        s = s.replace('_', ' ');
        String[] ss = s.split(" ");
        for(int i = 0;i<ss.length;i++){
            ss[i] = ss[i].substring(0, 1).toUpperCase()+ss[i].substring(1).toLowerCase();
        }
        return String.join(" ", ss);
    }

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

        random = new Random();
        String worldName = "world";
        File f = new File(getServer().getWorldContainer().toString()+"/server.properties");
        BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(f));
		
            String line = null;  
            
            while ((line = br.readLine()) != null)  
            {  
                String[] broken = line.split("=");
                if(broken[0].equals("level-name")){
                    worldName=broken[1];
                }
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        System.out.println("WORLDNAME IS: "+worldName);
        DataLayer.onLoad(getDataFolder(),worldName);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
                Renovator.RenovationCheck(getServer().getWorlds().get(0));
            }
        , 20*60, 20*60);
    }


}
