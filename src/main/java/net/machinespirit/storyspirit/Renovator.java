package net.machinespirit.storyspirit;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.DaylightDetector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.util.Vector;
import java.util.Collections;

class Renovator {

    public static Float renoChance = 1/3f; //one over number of minutes


    public static ArrayList<Material> pottedPlants = new ArrayList<Material>(Arrays.asList(Material.POTTED_POPPY,Material.POTTED_CACTUS,Material.POTTED_DEAD_BUSH,Material.POTTED_FERN,Material.POTTED_BLUE_ORCHID,Material.POTTED_DANDELION,Material.POTTED_BROWN_MUSHROOM,Material.POTTED_ACACIA_SAPLING,Material.POTTED_DARK_OAK_SAPLING,Material.POTTED_OXEYE_DAISY,Material.POTTED_RED_MUSHROOM,Material.POTTED_JUNGLE_SAPLING,Material.POTTED_BIRCH_SAPLING,Material.POTTED_OAK_SAPLING,Material.POTTED_SPRUCE_SAPLING));
    public static ArrayList<Material> groundPlants = new ArrayList<Material>(Arrays.asList(Material.SPRUCE_SAPLING,Material.ACACIA_SAPLING,Material.BIRCH_SAPLING, Material.OAK_SAPLING,Material.PEONY,Material.ALLIUM,Material.BLUE_ORCHID,Material.ROSE_BUSH,Material.LILAC));
    public static void RenovationCheck(World world){
        for(Entity entity : world.getEntitiesByClasses(Villager.class,Zombie.class,Skeleton.class)){
            if(entity.getCustomName() != null && StorySpirit.random.nextFloat() < renoChance){
                Renovate(entity);
            }
        }
    }

    public static ArrayList<Block> getReachable(Entity e){
        Block head = e.getLocation().add(new Vector(0,1,0)).getBlock();
        ArrayList<Block> l = new ArrayList<Block>();
        for(int x = head.getX()-1;x <= head.getX()+1; x++){
            for(int y = head.getY()-2;y <= head.getY()+1; y++){
                for(int z = head.getZ()-1;z <= head.getZ()+1; z++){
                    Block b  = e.getWorld().getBlockAt(x, y, z);
                    if(b != head && b != e.getLocation().getBlock()){
                        l.add(b);
                    }
                }
            }
        }
        return l;
    }

    public static void Renovate(Entity e){
        if(e instanceof Villager){
            if( ((Villager)e).getRiches() <= 0){
                return;
            }
        }

        ArrayList<Block> reachable = getReachable(e);
        Collections.shuffle(reachable);
        for (Block block : reachable) {
            if(Renovate(block)){
                if(e instanceof Villager){
                    ((Villager)e).setRiches(((Villager)e).getRiches()-1);
                }
                break;
            }
        }
    }
    public static Boolean Renovate(Block block){

        //in air
        if(DungeonPopulator.air.contains(block.getType())
            && !block.getRelative(BlockFace.DOWN).getType().isOccluding()
            && (block.getRelative(BlockFace.NORTH).getType().isOccluding() || block.getRelative(BlockFace.SOUTH).getType().isOccluding() || block.getRelative(BlockFace.EAST).getType().isOccluding() || block.getRelative(BlockFace.WEST).getType().isOccluding()
            )){
                float picked = StorySpirit.random.nextFloat();
                if(picked < 0.5){
                    block.setType(Material.WALL_TORCH);
                    Orient(block);
                    return true;
                }
                if(picked < 0.65 && DungeonPopulator.air.contains(block.getRelative(BlockFace.UP).getType()) && block.getLightFromSky() >13){
                    block.setType(Material.REDSTONE_LAMP);
                    block.getRelative(BlockFace.UP).setType(Material.DAYLIGHT_DETECTOR);
                    DaylightDetector bd = (DaylightDetector)block.getRelative(BlockFace.UP).getState();
                    bd.setInverted(true);
                    
                    return true;
                }
        }


        //on ground
        if(DungeonPopulator.air.contains(block.getType()) && block.getRelative(BlockFace.DOWN).getType().isOccluding()){
            float picked = StorySpirit.random.nextFloat();
            if(picked < 0.05){
                if(block.getRelative(BlockFace.DOWN).getType() == Material.GRASS_BLOCK){
                    block.setType(groundPlants.get(StorySpirit.random.nextInt(groundPlants.size())));
                    return true;
                }
            }
            if(picked < 0.25){
                if(block.getRelative(BlockFace.NORTH).getType().isOccluding() || block.getRelative(BlockFace.SOUTH).getType().isOccluding() || block.getRelative(BlockFace.EAST).getType().isOccluding() || block.getRelative(BlockFace.WEST).getType().isOccluding()){
                    block.setType(pottedPlants.get(StorySpirit.random.nextInt(pottedPlants.size())));
                    return true;
                }
            }
            if(picked < 0.50){
                if(block.getRelative(BlockFace.DOWN).getType() != Material.GRASS_BLOCK && block.getRelative(BlockFace.DOWN).getType() != Material.GRASS_PATH && block.getRelative(BlockFace.DOWN).getType() != Material.SAND && block.getLightFromSky()<14){
                    block.setType(Material.GRAY_CARPET);
                    return true;
                }
            }
            if(picked < 0.60){

                if(DungeonPopulator.air.contains(block.getRelative(BlockFace.UP).getType()) && block.getRelative(BlockFace.DOWN).getType().isOccluding()){
                    int c = 0;
                    if(block.getRelative(BlockFace.NORTH).getType().isSolid()){ c++; }
                    if(block.getRelative(BlockFace.SOUTH).getType().isSolid()){ c++; }
                    if(block.getRelative(BlockFace.EAST).getType().isSolid()){ c++; }
                    if(block.getRelative(BlockFace.WEST).getType().isSolid()){ c++; }
                    if(c==2){
                        block.setType(Material.OAK_FENCE);
                        block.getRelative(BlockFace.UP).setType(Material.OAK_PRESSURE_PLATE);
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public static void Orient(Block block){
        if(block.getType() == Material.WALL_TORCH){
            BlockState bs = block.getState();

            if(block.getRelative(0,0,-1).getType().isOccluding()){ //NORTH
                bs.setRawData((byte)3);
            }
            else if(block.getRelative(0,0,1).getType().isOccluding()){ //SOUTH
                    bs.setRawData((byte)4);
            }
            else if(block.getRelative(1,0,0).getType().isOccluding()){ //WEST
                    bs.setRawData((byte)2);
            }     
            else if(block.getRelative(-1,0,0).getType().isOccluding()){//EAST
                    bs.setRawData((byte)1);
            }

            bs.update(true,true);

        }
    }


}