package net.machinespirit.storyspirit;

import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.Inventory;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Sandstone;
import org.bukkit.material.Torch;
import org.bukkit.material.Wool;

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
                        Material.SNOW
                        ));

	@Override
	public void populate(World world, Random random, Chunk chunk) {
                if(random.nextFloat()>1f/5f){
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
                origin.getRelative(1,0,1).getType().equals(Material.GRASS) && origin.getRelative(1,1,1).getType().equals(Material.AIR) &&
                origin.getRelative(-1,0,1).getType().equals(Material.GRASS) && origin.getRelative(-1,1,1).getType().equals(Material.AIR) &&
                origin.getRelative(1,0,-1).getType().equals(Material.GRASS) && origin.getRelative(1,1,-1).getType().equals(Material.AIR) &&
                origin.getRelative(-1,0,-1).getType().equals(Material.GRASS) && origin.getRelative(-1,1,-1).getType().equals(Material.AIR)                     
                ){
                        origin.setType(Material.getMaterial(208));
                        origin.getRelative(1,0,1).setType(Material.getMaterial(208));
                        origin.getRelative(-1,0,-1).setType(Material.getMaterial(208));
                        origin.getRelative(1,0,-1).setType(Material.getMaterial(208));
                        origin.getRelative(-1,0,1).setType(Material.getMaterial(208));
                        origin.getRelative(0,-1,0).setType(Material.CHEST);
                        fillchest(origin.getRelative(0,-1,0));
                }else if(origin.getType().equals(Material.GRASS)){
                        buildRoom(origin.getRelative(0,1,0), 6, 5, Arrays.asList(Material.WOOD_STEP),  Arrays.asList(Material.ACACIA_FENCE),  Arrays.asList(Material.WOOD), -3,0f, random);
                }else if(origin.getType().equals(Material.SAND) || origin.getType().equals(Material.SANDSTONE )){
                        buildRoom(origin.getRelative(0,1,0), 5, 7, Arrays.asList(Material.SANDSTONE),  Arrays.asList(Material.SANDSTONE),  Arrays.asList(Material.SANDSTONE), 1,0f, random);
                        
                }else{
                        buildRoom(origin.getRelative(0,1,0), 5, 5, Arrays.asList(Material.SMOOTH_BRICK),  Arrays.asList(Material.SMOOTH_BRICK),  Arrays.asList(Material.SMOOTH_BRICK), 1,0.25f, random);
                }

        }

        public void buildRoom(Block origin,int xsize, int zsize, List<Material> roof, List<Material> wall, List<Material> floor, int interval,float branchChance,Random random){
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

                if(random.nextFloat()<0.1f && interval <2 && interval >-2){
                        origin.setType(Material.MOB_SPAWNER);
                        traps = false;
                        light = false;
                        stairs = false;
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
                                if(ablock.getType().equals(Material.AIR) && ablock.getRelative(0,-1,0).getType().isBlock() &&random.nextFloat()<0.1f){
                                        ablock.setType(Material.TORCH);
                                        ablock.getState().setData(new MaterialData(Material.CARPET,(byte)3));
                                        ablock.getState().update();
                                }
                        }
                        if(ablock.getType().equals(Material.SMOOTH_BRICK) && ablock.getRelative(0,1,0).getType().equals(Material.AIR)&&random.nextFloat()<0.1f && ablock.getY()<origin.getRelative(0,ymax,0).getY()){
                                ablock.getRelative(0,1,0).setType(Material.CARPET);
                                ablock.getState().setData(new MaterialData(Material.CARPET,(byte)3));
                                ablock.getState().update();
                                ablock.setType(Material.CHEST);
                        }else if(ablock.getType().equals(Material.WOOD) && ablock.getRelative(0,1,0).getType().equals(Material.AIR)&&random.nextFloat()<0.1f && ablock.getY()<origin.getRelative(0,ymax,0).getY()){
                                ablock.getRelative(0,1,0).setType(Material.CARPET);
                                ablock.getState().setData(new MaterialData(Material.CARPET,(byte)3));
                                ablock.getState().update();
                                ablock.setType(Material.CHEST);
                        }else if(ablock.getType().equals(Material.SANDSTONE) && ablock.getRelative(0,1,0).getType().equals(Material.AIR)&&random.nextFloat()<0.1f && ablock.getY()<origin.getRelative(0,ymax,0).getY()){
                                ablock.getRelative(0,1,0).setType(Material.CARPET);
                                ablock.getState().setData(new MaterialData(Material.CARPET,(byte)3));
                                ablock.getState().update();
                                ablock.setType(Material.CHEST);
                                fillchest(ablock);
                                
                        }

                }
        }


        public void fillchest(Block block){
                BlockState state = block.getState();
                if(state instanceof Chest){
                        System.out.print("CHEST LOOT TIME");
                        Inventory inventory = ((Chest)state).getBlockInventory();
                        int count = StorySpirit.random.nextInt(12);
                        for(int i = 0;i<count;i++){
                                inventory.addItem(Loot.randomLootItem());
                        }
                        state.update();
                }

        }
}