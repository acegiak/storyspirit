package net.machinespirit.storyspirit;
import net.machinespirit.storyspirit.Namer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StoryCommander implements CommandExecutor {
    StorySpirit plugin;

    public StoryCommander(StorySpirit plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdName = cmd.getName().toLowerCase();

        Player target = sender.getServer().getPlayer(sender.getName());
        

        if (cmdName.equals("name")) {
            sender.sendMessage(Namer.name());
            return true;
        }

        if (cmdName.equals("spawn")) {
            sender.sendMessage("spawning a friend");
            Character.spawn(target.getWorld(), target.getLocation(), args.length>0?args[0]:"");
            return true;
        }

        return false;
    }
}
