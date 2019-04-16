package net.machinespirit.storyspirit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;



public class DataLayer {
    public static File flatFile;
    public static JSONdb db;
    public static Gson parser = new Gson();
    public static HashMap<UUID,List<MerchantRecipe>> witchInventories = new HashMap<UUID,List<MerchantRecipe>>();


    public static void onLoad(File dataFolder,String worldname){

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        flatFile = new File(dataFolder, "storyspirit-"+worldname+".json");
        try {
            if (!flatFile.exists()) {
                
				flatFile.createNewFile();

                FileWriter f = new FileWriter(flatFile);
                f.write("{}");
                f.close();
            }
            String content = new String(Files.readAllBytes(flatFile.toPath()));
            db = parser.fromJson(content, JSONdb.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void save(){
		try {
			FileWriter writer = new FileWriter(flatFile);
            writer.write(parser.toJson(db));
            writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    public static void setName(Entity entity){
        if(db.names == null){
            db.names = new HashMap<String,String>();
        }
        db.names.put(entity.getUniqueId().toString(), entity.getCustomName());
        save();
    }


    public static void setTown(Entity entity, String townName){
        if(db.towns == null){
            db.towns = new HashMap<String,String>();
        }
        db.towns.put(entity.getUniqueId().toString(), townName);
        save();
    }

    public static String getTown(String id){
        if(db.towns == null){
            db.towns = new HashMap<String,String>();
        }
        return db.towns.get(id);
    }

    public static void setBlessing(Entity entity, PotionEffectType effectType){
        if(db.blessings == null){
            db.blessings = new HashMap<String,Integer>();
        }
        db.blessings.put(entity.getUniqueId().toString(), effectType.getId());
        save();
        setName(entity);
    }

    public static PotionEffectType getBlessing(Entity entity){
        if(db.blessings == null){
            return null;
        }
        
        if(!db.blessings.containsKey(entity.getUniqueId().toString())){
            return null;
        }
        
        return PotionEffectType.getById(db.blessings.get(entity.getUniqueId().toString()));
    }


    public static void setOpinion(Entity entity, Player player, float opinionValue){
        if(db.opinions == null){
            db.opinions = new HashMap<String,HashMap<String,Float>>();
        }
        if(!db.opinions.containsKey(entity.getUniqueId().toString())){
            db.opinions.put(entity.getUniqueId().toString(), new HashMap<String,Float>());
        }
        db.opinions.get(entity.getUniqueId().toString()).put(player.getUniqueId().toString(), opinionValue);
        setSeen(entity);
        setName(entity);
    }

    public static String getName(String id){
        return db.names.get(id);
    }

    public static float getOpinion(Entity entity, Player player){
        if(db.opinions == null){
            return Float.NEGATIVE_INFINITY;
        }
        if(!db.opinions.containsKey(entity.getUniqueId().toString()) || db.opinions.get(entity.getUniqueId().toString()) == null || !db.opinions.get(entity.getUniqueId().toString()).containsKey(player.getUniqueId().toString())){
            return Float.NEGATIVE_INFINITY;
        }

        return db.opinions.get(entity.getUniqueId().toString()).get(player.getUniqueId().toString());
    }

    public static void setSeen(Entity entity){
        if(db.lastSeen == null){
            db.lastSeen = new HashMap<String,Vector>();
        }
        db.lastSeen.put(entity.getUniqueId().toString(), entity.getLocation().toVector());

    }

    public static Vector getSeen(String id){
        if(db.lastSeen == null){
            return null;
        }
        if(!db.lastSeen.containsKey(id)){
            return null;
        }
        return db.lastSeen.get(id);
    }

    public static String LocationString(Location location){
        return location.getWorld().getName()+","+location.getX()+","+location.getY()+","+location.getZ();
    }

    public static Material getBlueprint(Location location){
        if(db.blueprints == null){
            return null;
        }
        if(!db.blueprints.containsKey(LocationString(location))){
            return null;
        }

        return Material.getMaterial(db.blueprints.get(LocationString(location)));
    }


    public static void setBlueprint(Block block, Material material){
        setBlueprint(block.getLocation(), material);
    }

    public static void setBlueprint(Location location, Material material){
        if(db.blueprints == null){
            db.blueprints = new HashMap<String,String>();
        }
        db.blueprints.put(LocationString(location), material.name());
    }


    public static void removeBlueprint(Location location){
        if(db.blueprints == null){
            return;
        }
        if(!db.blueprints.containsKey(LocationString(location))){
            return;
        }

        db.blueprints.remove(LocationString(location));
    }




    public static String reasonableVill(Location l){
        if(db.opinions.size() <= 0){
            return null;
        }
        int countdown = Math.max(10,db.opinions.size()/10);
        while(countdown > -10){
            String id = (String)DataLayer.db.opinions.keySet().toArray()[StorySpirit.random.nextInt(DataLayer.db.opinions.size())];
            Vector seen = getSeen(id);
            if(l == null || seen == null){
                continue;
            }
            double distance = l.toVector().distance(seen);
            System.out.println(distance);
            if(distance<1500f || countdown <=0){
                return id;
            }
            countdown--;
        }
        System.out.println("COULDNT FIND A REASONABLE VILLAGER");
        return null;
    }

    public static void setBoss(Entity entity){
        if(db.bosses == null){
            db.bosses = new HashMap<String,double[]>();
        }
        db.bosses.put(entity.getUniqueId().toString(), new double[]{entity.getLocation().getX(),entity.getLocation().getY(),entity.getLocation().getZ()});
        save();
        setName(entity);
    }

    public static void setLost(Entity entity){
        if(db.lostFriends == null){
            db.lostFriends = new HashMap<String,double[]>();
        }
        db.lostFriends.put(entity.getUniqueId().toString(), new double[]{entity.getLocation().getX(),entity.getLocation().getY(),entity.getLocation().getZ()});
        save();
        setName(entity);
    }




}