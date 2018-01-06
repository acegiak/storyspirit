package net.machinespirit.storyspirit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

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
import java.util.Map;



public class DataLayer {
    public static File flatFile;
    public static JSONdb db;
    public static Gson parser = new Gson();


    public static void onLoad(File dataFolder){

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        flatFile = new File(dataFolder, "storyspirit.json");
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


    public static void setOpinion(Entity entity, float opinionValue){
        if(db.opinions == null){
            db.opinions = new HashMap<String,Float>();
        }
        db.opinions.put(entity.getUniqueId().toString(), opinionValue);
        save();
        setName(entity);
    }

    public static String getName(String id){
        return db.names.get(id);
    }

    public static float getOpinion(Entity entity){
        if(db.opinions == null){
            return Float.NEGATIVE_INFINITY;
        }
        if(!db.opinions.containsKey(entity.getUniqueId().toString())){
            return Float.NEGATIVE_INFINITY;
        }

        return db.opinions.get(entity.getUniqueId().toString());
    }




}