package net.machinespirit.storyspirit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class JSONdb {
    public HashMap<String,HashMap<String,Float>> opinions;
    public HashMap<String,String> names;
    public HashMap<String,String> towns;
    public HashMap<String,Integer> blessings;
    public HashMap<String,double[]> bosses;
    public HashMap<String,double[]> lostFriends;
    public HashMap<String,Vector> lastSeen;
    public HashMap<String,String> blueprints;

    //TODO: NAME DEBT - when there aren't any villagers, create a name and give it to the next villager
    // public static HashMap<String,HashMap> witchInventories = new HashMap<String,List<MerchantRecipe>>();

    
}