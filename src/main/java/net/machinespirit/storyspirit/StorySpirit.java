package net.machinespirit.storyspirit;

import java.util.Random;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class StorySpirit extends JavaPlugin {
    public static Random random;

    public static Plugin current;


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

    }


}
