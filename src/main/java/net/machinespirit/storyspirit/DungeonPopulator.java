package net.machinespirit.storyspirit;

import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Sandstone;
import org.bukkit.material.Torch;
import org.bukkit.material.Wool;
import org.omg.IOP.ENCODING_CDR_ENCAPS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.text.Position;

import net.machinespirit.storyspirit.StorySpirit;

class DungeonPopulator extends BlockPopulator{

        ArrayList<Material> foundations = new ArrayList<Material>(Arrays.asList(
                Material.GRASS,
                Material.STONE,
                Material.DIRT,
                Material.SAND,
                Material.SANDSTONE
                ));

        ArrayList<Material> air = new ArrayList<Material>(Arrays.asList(
                        Material.AIR,
                        Material.LEAVES,
                        Material.SNOW,
                        Material.GRASS
                        ));

	@Override
	public void populate(World world, Random random, Chunk chunk) {
                if(random.nextFloat()>1f/250f){
                        return;
                }
                Block origin = null;

                int ox = random.nextInt(15);
                int oz =  random.nextInt(15);
                for(int nx = 0;nx<15 && origin==null;nx++){
                        for(int nz = 0;nz<15 && origin==null;nz++){
                                int x = (ox+nx)%15;
                                int z = (oz+nz)%15;
                                ArrayList<Block> possibleOrigins = new ArrayList<Block>();
                                for(int y = 0; y<250; y++){
                                        Block test = chunk.getBlock(x,y,z);
                                        if(foundations.contains(test.getType())
                                        && air.contains(test.getRelative(0,1,0).getType())
                                        && air.contains(test.getRelative(0,2,0).getType())){
                                                possibleOrigins.add(test);
                                        }
                                }
                                if(possibleOrigins.size() > 0){
                                        origin = possibleOrigins.get(random.nextInt(possibleOrigins.size()));
                                }
                        }
                }
                if(origin == null){
                        return;
                }


                if(origin.getType().equals(Material.GRASS) && origin.getRelative(0,1,0).getType().equals(Material.AIR) &&
                origin.getRelative(1,0,1).getType().equals(Material.GRASS) && air.contains(origin.getRelative(1,1,1).getType()) &&
                origin.getRelative(-1,0,1).getType().equals(Material.GRASS) && air.contains(origin.getRelative(-1,1,1).getType()) &&
                origin.getRelative(1,0,-1).getType().equals(Material.GRASS) && air.contains(origin.getRelative(1,1,-1).getType()) &&
                origin.getRelative(-1,0,-1).getType().equals(Material.GRASS) && air.contains(origin.getRelative(-1,1,-1).getType())                    
                ){
                        origin.setType(Material.getMaterial(208));
                        origin.getRelative(1,0,1).setType(Material.getMaterial(208));
                        origin.getRelative(-1,0,-1).setType(Material.getMaterial(208));
                        origin.getRelative(1,0,-1).setType(Material.getMaterial(208));
                        origin.getRelative(-1,0,1).setType(Material.getMaterial(208));
                        origin.getRelative(0,-1,0).setType(Material.CHEST);
                        fillchest(origin.getRelative(0,-1,0));
                }else if(origin.getType().equals(Material.GRASS)){
                        buildRoom(origin.getRelative(0,1,0), 5, 5, Arrays.asList(Material.WOOD_STEP),  Arrays.asList(Material.ACACIA_FENCE),  Arrays.asList(Material.WOOD),Arrays.asList(null,null,Material.AIR), -3,0f,null, random);
                }else if(origin.getType().equals(Material.SAND) || origin.getType().equals(Material.SANDSTONE )){
                        buildRoom(origin.getRelative(0,1,0), 4+StorySpirit.random.nextInt(5), 4+StorySpirit.random.nextInt(5), Arrays.asList(Material.SANDSTONE),  Arrays.asList(Material.SANDSTONE),  Arrays.asList(Material.SANDSTONE), Arrays.asList(null,null,null,Material.SANDSTONE_STAIRS),1,0.25f,null, random);
                        
                }else{
                        buildRoom(origin, 4+StorySpirit.random.nextInt(5), 4+StorySpirit.random.nextInt(5), Arrays.asList(Material.SMOOTH_BRICK),  Arrays.asList(Material.SMOOTH_BRICK),  Arrays.asList(Material.SMOOTH_BRICK), Arrays.asList(null,null,null,Material.IRON_FENCE), 1,1f,null, random);
                }

        }

        public void buildRoom(Block origin,int xsize, int zsize, List<Material> roof, List<Material> wall, List<Material> floor, List<Material> crenellations, int interval,float branchChance,List<Block> origins, Random random){
                if(origins == null){
                        origins = new ArrayList<Block>();
                }
                if(origins.contains(origin)){
                        return;
                }
                origins.add(origin);
                int xmin = 0-(xsize/2);
                int xmax = xmin+xsize-1;
                int ymin = 0-1;
                int ymax = ymin+4;
                int zmin = 0-(zsize/2);
                int zmax = zmin+zsize-1;
                ArrayList<Block> allBlocks = new ArrayList<Block>();
                for(int x = xmin;x <= xmax; x++){
                        for(int y = ymin;y <= ymax; y++){
                                for(int z = zmin;z <= zmax; z++){
                                        Block thisBlock = origin.getRelative(x,y,z);
                                        if(y == ymax){
                                                if(thisBlock.getType().equals(Material.CHEST)|| thisBlock.getType().equals(Material.LAVA)){
                                                        thisBlock.getRelative(0,-1,0).setType(roof.get(random.nextInt(roof.size())));
                                                }else{                                                        
                                                        thisBlock.setType(roof.get(random.nextInt(roof.size())));
                                                }
                                                thisBlock.setType(roof.get(random.nextInt(roof.size())));
                                        }else if(y == ymin){
                                                thisBlock.setType(floor.get(random.nextInt(floor.size())));
                                        }else if(((z == zmin || z == zmax)&&(interval<0^x%Math.abs(interval)==0))
                                                 || ((x == xmin || x == xmax)&&(interval<0^z%Math.abs(interval)==0))){
                                                thisBlock.setType(wall.get(random.nextInt(wall.size())));
                                        }else{
                                                thisBlock.setType(Material.AIR);
                                        }
                                        allBlocks.add(thisBlock);
                                }
                        }
                }
                if(interval < 2 && interval > -2){
                        //add doors?
                        if(random.nextFloat()<0.5f){
                                origin.getRelative(xmin,1,0).setType(Material.AIR);
                                origin.getRelative(xmin,0,0).setType(Material.AIR);
                        }
                        if(random.nextFloat()<0.5f){
                                origin.getRelative(xmax,1,0).setType(Material.AIR);
                                origin.getRelative(xmax,0,0).setType(Material.AIR);
                        }
                        if(random.nextFloat()<0.5f){
                                origin.getRelative(0,1,zmin).setType(Material.AIR);
                                origin.getRelative(0,0,zmin).setType(Material.AIR);
                        }
                        if(random.nextFloat()<0.5f){
                                origin.getRelative(0,1,zmax).setType(Material.AIR);
                                origin.getRelative(0,0,zmax).setType(Material.AIR);
                        }
                }

                boolean traps = true;
                boolean light = true;
                boolean stairs = true;
                boolean spiders = true;

                if(random.nextFloat()<0.1f && interval <2 && interval >-2){
                        origin.setType(Material.MOB_SPAWNER);
                        traps = false;
                        light = false;
                        stairs = false;
                        BlockState blockState = origin.getState();
                        if(blockState instanceof CreatureSpawner){
                                CreatureSpawner spawner = ((CreatureSpawner)blockState);
                                List<EntityType> types = Arrays.asList(new EntityType[]{EntityType.BAT,EntityType.ZOMBIE,EntityType.CREEPER,EntityType.SPIDER,EntityType.CAVE_SPIDER,EntityType.SKELETON,EntityType.ZOMBIE,EntityType.CREEPER,EntityType.SPIDER,EntityType.CAVE_SPIDER,EntityType.SKELETON,EntityType.MAGMA_CUBE,EntityType.SLIME,EntityType.PIG_ZOMBIE,EntityType.SNOWMAN});
                                EntityType type = types.get(StorySpirit.random.nextInt(types.size()));
                                if(type.equals(EntityType.CAVE_SPIDER) || type.equals(EntityType.SPIDER)){
                                        spiders = true;
                                }
                                spawner.setSpawnedType(type);
                                blockState.update();
                        }else{
                                System.out.println("Oops, that's not a spawner at all!");
                                origin.setType(Material.AIR);
                        }
                        
                }

                Collections.shuffle(allBlocks);

                for (Block ablock : allBlocks) {
                        if(traps){                       
                                if(ablock.getType().equals(Material.SMOOTH_BRICK) && ablock.getRelative(0,1,0).getType().equals(Material.AIR)&&random.nextFloat()<0.1f && ablock.getY()<origin.getRelative(0,ymax,0).getY()){
                                        ablock.getRelative(0,1,0).setType(Material.STONE_PLATE);
                                        ablock.getRelative(0,-1,0).setType(Material.TNT);
                                }else if(ablock.getType().equals(Material.WOOD) && ablock.getRelative(0,1,0).getType().equals(Material.AIR)&&random.nextFloat()<0.1f && ablock.getY()<origin.getRelative(0,ymax,0).getY()){
                                        ablock.getRelative(0,1,0).setType(Material.WOOD_PLATE);
                                        ablock.getRelative(0,-1,0).setType(Material.TNT);
                                }else if(ablock.getType().equals(Material.SANDSTONE) && ablock.getRelative(0,1,0).getType().equals(Material.AIR)&&random.nextFloat()<0.1f && ablock.getY()<origin.getRelative(0,ymax,0).getY()){
                                        ablock.getRelative(0,1,0).setType(Material.STONE_PLATE);
                                        ablock.getRelative(0,-1,0).setType(Material.TNT);
                                }
                        }
                        if(light){
                                if(ablock.getType().equals(Material.AIR) && ablock.getRelative(0,-1,0).getType().equals(Material.AIR) &&random.nextFloat()<0.1f){
 
                                        if(ablock.getRelative(0,0,-1).getType().isOccluding()){ //NORTH
                                                ablock.setType(Material.TORCH);
                                                BlockState state = ablock.getState();
                                                state.setType(Material.TORCH);
                                                state.setRawData((byte)3);
                                                state.update(true,true);
                                        }
                                        else if(ablock.getRelative(0,0,1).getType().isOccluding()){ //SOUTH
                                                ablock.setType(Material.TORCH);
                                                BlockState state = ablock.getState();
                                                state.setType(Material.TORCH);
                                                state.setRawData((byte)4);
                                                state.update(true,true);
                                        }
                                        else if(ablock.getRelative(-1,0,0).getType().isOccluding()){ //WEST
                                                ablock.setType(Material.TORCH);
                                                BlockState state = ablock.getState();
                                                state.setType(Material.TORCH);
                                                state.setRawData((byte)1);
                                                state.update(true,true);
                                        }     
                                        else if(ablock.getRelative(-1,0,0).getType().isOccluding()){//EAST
                                                ablock.setType(Material.TORCH);
                                                BlockState state = ablock.getState();
                                                state.setType(Material.TORCH);
                                                state.setRawData((byte)2);
                                                state.update(true,true);
                                        }
                                }
                        }



                        if(light){
                                if(ablock.getType().equals(Material.AIR) && ablock.getRelative(0,-1,0).getType().equals(Material.AIR) &&random.nextFloat()<0.1f){
                                        Block bblock = ablock;
                                        do{
                                        if(ablock.getRelative(0,0,-1).getType().isSolid()){ //NORTH
                                                ablock.setType(Material.VINE);
                                                BlockState state = ablock.getState();
                                                state.setType(Material.VINE);
                                                state.setRawData((byte)4);
                                                state.update(true,true);
                                        }
                                        else if(ablock.getRelative(0,0,1).getType().isSolid()){ //SOUTH
                                                ablock.setType(Material.VINE);
                                                BlockState state = ablock.getState();
                                                state.setType(Material.VINE);
                                                state.setRawData((byte)1);
                                                state.update(true,true);
                                        }
                                        else if(ablock.getRelative(-1,0,0).getType().isSolid()){ //WEST
                                                ablock.setType(Material.VINE);
                                                BlockState state = ablock.getState();
                                                state.setType(Material.VINE);
                                                state.setRawData((byte)2);
                                                state.update(true,true);
                                        }     
                                        else if(ablock.getRelative(-1,0,0).getType().isSolid()){//EAST
                                                ablock.setType(Material.VINE);
                                                BlockState state = ablock.getState();
                                                state.setType(Material.VINE);
                                                state.setRawData((byte)8);
                                                state.update(true,true);
                                        }
                                        bblock = bblock.getRelative(0,-1,0);
                                }while(bblock.getType().equals(Material.AIR));
                                }
                        }

                        if(ablock.getType().equals(Material.AIR) && ablock.getRelative(0,-1,0).getType().isOccluding() && random.nextFloat()<0.025f){
                                fillchest(ablock);
                        }
                        if(ablock.getType().equals(Material.AIR) && ablock.getRelative(0,-1,0).getType().isOccluding() && random.nextFloat()<0.025f){
                                ablock.setType(Material.TRAPPED_CHEST);
                                List<ItemStack> contents = Loot.getTreasure();
                                if(!ChestUtils.addItemsToChest(ablock, contents,true,StorySpirit.random)){
                                        System.out.println("Could not add items to chest");
                                }
                        }

                        


                        if(ablock.getType().equals(Material.SMOOTH_BRICK) && ablock.getRelative(0,1,0).getType().equals(Material.AIR)&&random.nextFloat()<0.05f && ablock.getY()<origin.getRelative(0,ymax,0).getY()){
                                fillchest(ablock);
                                setBlockType(ablock.getRelative(0,1,0), Material.CARPET, 8, true, true);
                        }else if(ablock.getType().equals(Material.WOOD) && ablock.getRelative(0,1,0).getType().equals(Material.AIR)&&random.nextFloat()<0.05f && ablock.getY()<origin.getRelative(0,ymax,0).getY()){
                                fillchest(ablock);
                                setBlockType(ablock.getRelative(0,1,0), Material.CARPET, 12, true, true);
                        }else if(ablock.getType().equals(Material.SANDSTONE) && ablock.getRelative(0,1,0).getType().equals(Material.AIR)&&random.nextFloat()<0.05f && ablock.getY()<origin.getRelative(0,ymax,0).getY()){
                                fillchest(ablock);
                                setBlockType(ablock.getRelative(0,1,0), Material.CARPET, 4, true, true);                               
                        }
                        if(ablock.getType().equals(Material.AIR)&&(spiders ^ random.nextFloat()>0.05f)){
                                ablock.setType(Material.WEB);
                        }




                }

                //CRENNELATION
                Material type = crenellations.get(random.nextInt(crenellations.size()));
                
                for (int i = 0; i<allBlocks.size();i++) {
                        Block ablock = allBlocks.get(i);
                        if(type != null){
                                if(ablock.getY() == origin.getRelative(0,ymax,0).getY() && (ablock.getZ() == origin.getRelative(0,0,zmax).getZ() || ablock.getZ() == origin.getRelative(0,0,zmin).getZ() || ablock.getX() == origin.getRelative(xmax,0,0).getX() || ablock.getX() == origin.getRelative(xmin,0,0).getX() ) && ablock.getRelative(0,1,0).getType().equals(Material.AIR)){
                                        ablock.getRelative(0,1,0).setType(type);;
                                }
                        }


                        if(ablock.getY() == origin.getRelative(0,ymin,0).getY() && (ablock.getZ() == origin.getRelative(0,0,zmax).getZ() || ablock.getZ() == origin.getRelative(0,0,zmin).getZ())&& (ablock.getX() == origin.getRelative(xmax,0,0).getX() || ablock.getX() == origin.getRelative(xmin,0,0).getX() ) && (ablock.getRelative(0,-1,0).getType().equals(Material.AIR)||ablock.getRelative(0,-1,0).getType().equals(Material.WATER))){
                                ablock.getRelative(0,-1,0).setType(floor.get(random.nextInt(floor.size())));

                                allBlocks.add(ablock.getRelative(0,-1,0));
                        }
                }



                if(interval < 2 && interval > -2){
                        //add branches
                        float newBranchChance = Math.max(branchChance-0.05f, 0);

                        if(random.nextFloat()<branchChance*0.125f){
                                buildRoom(origin.getRelative(xmin*2,0,0),xsize,zsize,roof,wall,floor,Arrays.asList(type),interval,newBranchChance,origins,random);
                        }
                        if(random.nextFloat()<branchChance*0.125f){
                                buildRoom(origin.getRelative(xmax*2,0,0),xsize,zsize,roof,wall,floor,Arrays.asList(type),interval,newBranchChance,origins,random);
                        }
                        if(random.nextFloat()<branchChance*0.125f){
                                buildRoom(origin.getRelative(0,0,zmin*2),xsize,zsize,roof,wall,floor,Arrays.asList(type),interval,newBranchChance,origins,random);
                        }
                        if(random.nextFloat()<branchChance*0.125f){
                                buildRoom(origin.getRelative(0,0,zmax*2),xsize,zsize,roof,wall,floor,Arrays.asList(type),interval,newBranchChance,origins,random);
                        }
                        if(random.nextFloat()<branchChance*0.05f){
                                buildRoom(origin.getRelative(0,-1*ymax+ymin,0),xsize,zsize,roof,wall,floor,Arrays.asList(type),interval,newBranchChance,origins,random);

                                origin.getRelative(0,ymin+1,0).setType(Material.TRAP_DOOR);

                                origin.getRelative(0,ymin,0).setType(Material.LADDER);
                        }
                        if(random.nextFloat()<branchChance*0.05f){
                                buildRoom(origin.getRelative(0,-1*ymin+ymax,0),xsize,zsize,roof,wall,floor,Arrays.asList(type),interval,newBranchChance,origins,random);
                                origin.getRelative(0,ymax+1,0).setType(Material.TRAP_DOOR);

                                origin.getRelative(0,ymax,0).setType(Material.LADDER);
                        }
                }

                
        }


        public void fillchest(Block block){
                block.setType(Material.CHEST);
                if(!ChestUtils.isChest(block)){
                        System.out.println("Tried to put things in a not-chest!");
                }
                List<ItemStack> contents = Loot.getTreasure();
                if(!ChestUtils.addItemsToChest(block, contents,true,StorySpirit.random)){
                        System.out.println("Could not add items to chest");
                }
        }

        public static boolean setBlockType(Block block, Material material, int rawData, boolean update, boolean physics) {
                // Get the block state, set the material
                block.setType(material);
                BlockState state = block.getState();
                state.setType(material);
        
                // Apply the raw data
                if(rawData != 0)
                    state.setRawData((byte) (rawData));
        
                // Check if the block update succeed, return the result
                return state.update(update, physics);
            }
        
}