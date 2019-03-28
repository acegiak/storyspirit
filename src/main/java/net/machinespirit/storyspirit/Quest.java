package net.machinespirit.storyspirit;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity.Spigot;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class Quest {
    public static void complete(Player player, String questName){
        player.sendMessage("Quest Complete: "+questName+"!");
        ItemStack token = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta meta = token.getItemMeta();
        meta.setDisplayName(player.getDisplayName()+" "+questName);
        token.setItemMeta(meta);
        player.getWorld().dropItem(player.getLocation(),token);
        player.getWorld().spawn(player.getLocation(), ExperienceOrb.class);
        Conversation.publicOpinion(player, 0.10f);
    }

}