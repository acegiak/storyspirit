package net.machinespirit.storyspirit;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.DaylightDetector;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.GlassPane;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.util.Vector;
import java.util.Collections;

class Renovator {

    public static Float renoChance = 1f; //one over number of minutes


    public static ArrayList<Material> pottedPlants = new ArrayList<Material>(Arrays.asList(Material.POTTED_POPPY,Material.POTTED_CACTUS,Material.POTTED_DEAD_BUSH,Material.POTTED_FERN,Material.POTTED_BLUE_ORCHID,Material.POTTED_DANDELION,Material.POTTED_BROWN_MUSHROOM,Material.POTTED_ACACIA_SAPLING,Material.POTTED_DARK_OAK_SAPLING,Material.POTTED_OXEYE_DAISY,Material.POTTED_RED_MUSHROOM,Material.POTTED_JUNGLE_SAPLING,Material.POTTED_BIRCH_SAPLING,Material.POTTED_OAK_SAPLING,Material.POTTED_SPRUCE_SAPLING));
    public static ArrayList<Material> groundPlants = new ArrayList<Material>(Arrays.asList(Material.SPRUCE_SAPLING,Material.ACACIA_SAPLING,Material.BIRCH_SAPLING, Material.OAK_SAPLING,Material.PEONY,Material.ALLIUM,Material.BLUE_ORCHID,Material.ROSE_BUSH,Material.LILAC,Material.AZURE_BLUET));
    public static ArrayList<Material> natural = new ArrayList<Material>(Arrays.asList(Material.GRAVEL,Material.GRASS_BLOCK, Material.AIR,Material.DIRT,Material.SMOOTH_STONE, Material.ACACIA_LOG,Material.BIRCH_LOG,Material.DARK_OAK_LOG,Material.JUNGLE_LOG, Material.OAK_LOG, Material.SPRUCE_LOG, Material.ACACIA_LEAVES,Material.BIRCH_LEAVES, Material.DARK_OAK_LEAVES, Material.JUNGLE_LEAVES, Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.DIRT, Material.WATER, Material.GRASS, Material.SNOW,Material.SMOOTH_STONE, Material.SAND,Material.DIORITE, Material.ANDESITE));
    public static ArrayList<Material> building = new ArrayList<Material>(Arrays.asList(Material.GRASS_PATH,Material.COBBLESTONE,Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS,Material.JUNGLE_PLANKS,Material.OAK_PLANKS,Material.SPRUCE_PLANKS,Material.GLASS_PANE));
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
        for(int x = head.getX()-2;x <= head.getX()+2; x++){
            for(int y = head.getY()-2;y <= head.getY()+2; y++){
                for(int z = head.getZ()-2;z <= head.getZ()+2; z++){
                    Block b  = e.getWorld().getBlockAt(x, y, z);
                    if(b.getLocation().toVector().distance(head.getLocation().toVector()) >=1 && b.getLocation().toVector().distance(e.getLocation().toVector()) >=1){
                        l.add(b);
                    }
                }
            }
        }
        return l;
    }

    public static void Renovate(Entity e){
        if(e instanceof Villager){
            if( ((Villager)e).getVillagerExperience() <= 0){
                return;
            }
        }
        System.out.println("BUILD");

        ArrayList<Block> reachable = getReachable(e);
        Collections.shuffle(reachable);
        for (Block block : reachable) {
            if(e instanceof Villager){
                if(RenovateNice(block)){
                    if(StorySpirit.random.nextFloat()<0.2){
                        ((Villager)e).setVillagerExperience(((Villager)e).getVillagerExperience()-1);
                    }
                
                    break;
                }
            }
            if(e instanceof Monster){
                if(RenovateNasty(block)){
                
                    break;
                }
            }
        }
    }


    public static Boolean RenovateNasty(Block block){
        float picked = StorySpirit.random.nextFloat()*10;


        if(picked < 0.05){
            if(DungeonPopulator.air.contains(block.getType()) && block.getRelative(BlockFace.DOWN).getType().isOccluding() && DungeonPopulator.air.contains(block.getRelative(BlockFace.UP))){
                block.setType(Material.CRACKED_STONE_BRICKS);
                block.getRelative(BlockFace.UP).setType(Material.SKELETON_SKULL);
                return true;
            }
        }

        if(picked < 0.40){

            if(DungeonPopulator.air.contains(block.getRelative(BlockFace.UP).getType()) && block.getRelative(BlockFace.DOWN).getType().isOccluding()){
                int c = 0;
                if(block.getRelative(BlockFace.NORTH).getType().isSolid()){ c++; }
                if(block.getRelative(BlockFace.SOUTH).getType().isSolid()){ c++; }
                if(block.getRelative(BlockFace.EAST).getType().isSolid()){ c++; }
                if(block.getRelative(BlockFace.WEST).getType().isSolid()){ c++; }
                if(block.getRelative(BlockFace.UP).getType().isSolid()){ c++; }
                if(block.getRelative(BlockFace.DOWN).getType().isSolid()){ c++; }
                if(c==2){
                    block.setType(Material.COBWEB);
                    return true;
                }
            }
        }
        if(picked < 0.60){
            if(DungeonPopulator.air.contains(block.getType()) && block.getRelative(BlockFace.DOWN).getType().isOccluding()){
                block.setType(Material.DEAD_BUSH);
                return true;
            }
        }



        return false;
    }
    public static Boolean RenovateNice(Block block){
        float picked = StorySpirit.random.nextFloat();

        if(picked <0.25){
            LayFoundation(block);
        }
        if(picked <0.75 && Build(block)){
            return true;
        }

        if(DataLayer.getBlueprint(block.getLocation()) != null){
            return false;
        }
        return false;
    }

    public static void Orient(Block block){
        if(block.getBlockData() instanceof Directional){
            Directional data = (Directional)block.getBlockData();

            if(block.getRelative(BlockFace.SOUTH).getType().isOccluding()){ //NORTH
                data.setFacing(BlockFace.NORTH);
            }
            else if(block.getRelative(BlockFace.NORTH).getType().isOccluding()){ //SOUTH
                data.setFacing(BlockFace.SOUTH);
            }
            else if(block.getRelative(BlockFace.EAST).getType().isOccluding()){ //WEST
                data.setFacing(BlockFace.WEST);
            }     
            else if(block.getRelative(BlockFace.WEST).getType().isOccluding()){//EAST
                data.setFacing(BlockFace.EAST);
            }

            block.setBlockData(data);


            
        }

        if(block.getType() == Material.OAK_STAIRS){
            Directional data = (Directional)block.getBlockData();

            if(block.getRelative(BlockFace.SOUTH).getType().isOccluding()){ //NORTH
                data.setFacing(BlockFace.SOUTH);
            }

            else if(block.getRelative(BlockFace.NORTH).getType().isOccluding()){ //SOUTH
                data.setFacing(BlockFace.NORTH);
            }
            else if(block.getRelative(BlockFace.EAST).getType().isOccluding()){ //WEST
                data.setFacing(BlockFace.EAST);
            }     
            else if(block.getRelative(BlockFace.WEST).getType().isOccluding()){//EAST
                data.setFacing(BlockFace.WEST);
            }

            block.setBlockData(data);

        }


        if(block.getBlockData() instanceof MultipleFacing){
            MultipleFacing data = (MultipleFacing)block.getBlockData();

            if(block.getRelative(BlockFace.SOUTH).getType().isOccluding()){ //NORTH
                data.setFace(BlockFace.SOUTH,true);
            }

            else if(block.getRelative(BlockFace.NORTH).getType().isOccluding()){ //SOUTH
                data.setFace(BlockFace.NORTH,true);
            }
            else if(block.getRelative(BlockFace.EAST).getType().isOccluding()){ //WEST
                data.setFace(BlockFace.EAST,true);
            }     
            else if(block.getRelative(BlockFace.WEST).getType().isOccluding()){//EAST
                data.setFace(BlockFace.WEST,true);
            }

            block.setBlockData(data);

        }

        if(block.getType() == Material.DAYLIGHT_DETECTOR){
            DaylightDetector bd = (DaylightDetector)block.getBlockData();
            bd.setInverted(true);
            block.setBlockData(bd);
        }

        if(block.getBlockData() instanceof Bisected && !(block.getBlockData() instanceof Stairs)){
            block.setType(block.getType(),false);


            System.out.println("bottom");

            Bisected data = (Bisected)block.getBlockData().clone();
            data.setHalf(Half.BOTTOM);
            block.setBlockData(data);



            Block block2 = block.getRelative(BlockFace.UP);
            block2.setType(block.getType(),false);
          

            
            System.out.println("top");

            System.out.println(block2.getType().toString());
            System.out.println(block2.toString());
            System.out.println(block2.getBlockData().toString());
            System.out.println(block2.getBlockData().getClass().toString());

            Bisected data2 = (Bisected)block2.getBlockData();
            data2.setHalf(Half.TOP);
            block2.setBlockData(data2);



        }



    }

    public static boolean Build(Block spot){
        if(!natural.contains(spot.getType())){
            return false;
        }

        if((spot.getRelative(BlockFace.UP).getType().isSolid()
        || spot.getRelative(BlockFace.DOWN).getType().isSolid()
        || spot.getRelative(BlockFace.NORTH).getType().isSolid()
        || spot.getRelative(BlockFace.SOUTH).getType().isSolid()
        || spot.getRelative(BlockFace.EAST).getType().isSolid()
        || spot.getRelative(BlockFace.WEST).getType().isSolid())
        && DataLayer.getBlueprint(spot.getLocation()) != null
        ){
            System.out.println("placed:"+DataLayer.getBlueprint(spot.getLocation()).name());
            spot.setType(DataLayer.getBlueprint(spot.getLocation()));
            DataLayer.removeBlueprint(spot.getLocation());

            Orient(spot);
        }

        return false;
    }

    public static boolean LayFoundation(Block origin){
        float picked = StorySpirit.random.nextFloat();

        if(picked < 0.5 && BlueprintLibrary.House(origin)){
            return true;
        }
        if(picked > 0.5 && BlueprintLibrary.Decoration(origin)){
            return true;
        }
        return false;

    }
    




}