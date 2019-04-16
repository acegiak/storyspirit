package net.machinespirit.storyspirit;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.DaylightDetector;

class BlueprintLibrary {

    public static boolean House(Block origin){
        Float picked = StorySpirit.random.nextFloat();
        System.out.println("CHECKING HOUSE FOUNDATION");

        if(!origin.getRelative(BlockFace.DOWN).getType().isOccluding()){
            System.out.println("No solid:"+origin.getRelative(BlockFace.DOWN).getType().name());

            return false;
        }
        if(origin.getType().isOccluding()){
            System.out.println("too solid: "+origin.getType().name());
            return false;
            
        }
        if(!Renovator.natural.contains(origin.getRelative(BlockFace.DOWN).getType())){
            System.out.println("not natural base: "+origin.getRelative(BlockFace.DOWN).getType().name());
            return false;
            
        }

        for(int x=origin.getX()-10;x <= origin.getX()+10;x++){
            for(int z=origin.getZ()-10;z <= origin.getZ()+10;z++){
                for(int y=origin.getY()-10;y <= origin.getY()+10;y++){
                    if(DataLayer.getBlueprint(origin.getLocation()) != null){
                        System.out.println("Too Close to Other Blueprint");
                        return false;
                    }
                }
            }
        }


        int width = StorySpirit.random.nextInt(3)+1;
        int length = StorySpirit.random.nextInt(3)+1;
        int weven = StorySpirit.random.nextInt(1);
        int leven = StorySpirit.random.nextInt(1);
        int height = StorySpirit.random.nextInt(1)+4;
        if(width+weven <2){
            width++;
        }

        if(length+leven <2){
            width++;
        }

        for(int x=origin.getX()-width;x <= origin.getX()+width+weven;x++){
            for(int z=origin.getZ()-length;z <= origin.getZ()+length+leven;z++){
                for(int y=origin.getY();y <= origin.getY()+height;y++){
                    if(!Renovator.natural.contains(origin.getWorld().getBlockAt(x, y, z).getType())){
                        System.out.println("not natural:"+origin.getWorld().getBlockAt(x, y, z).getType().name());
                        return false;
                    }
                    if(DataLayer.getBlueprint(origin.getWorld().getBlockAt(x, y, z).getLocation()) != null){
                        System.out.println("In another blueprint");

                        return false;
                    }
                }
            }
        }

        System.out.println("LAYING FOUNDATION:house");

        for(int x=origin.getX()-width;x <= origin.getX()+width+weven;x++){
            for(int z=origin.getZ()-length;z <= origin.getZ()+length+leven;z++){
                for(int y=origin.getY();y <= origin.getY()+height;y++){

                    if(y == origin.getY()){
                        DataLayer.setBlueprint(origin.getWorld().getBlockAt(x, y, z).getLocation(), Material.COBBLESTONE);
                    }else
                    if(y == origin.getY()+height){
                        DataLayer.setBlueprint(origin.getWorld().getBlockAt(x, y, z).getLocation(), Material.OAK_WOOD);
                    }else

                    if((z == origin.getZ()-length || z == origin.getZ()+length+leven) && (x == origin.getX()-length || x == origin.getX()+width+weven)){
                        DataLayer.setBlueprint(origin.getWorld().getBlockAt(x, y, z).getLocation(), Material.OAK_WOOD);
                    }else

                    if((z == origin.getZ()-length || z == origin.getZ()+length+leven) || (x == origin.getX()-length || x == origin.getX()+width+weven)){
                        DataLayer.setBlueprint(origin.getWorld().getBlockAt(x, y, z).getLocation(), Material.OAK_PLANKS);
                    }
                    if(y == origin.getY()+2 && (x == origin.getX() ^ y == origin.getY())){
                        float f = StorySpirit.random.nextFloat();
                        if(f < 0.33){
                            DataLayer.setBlueprint(origin.getWorld().getBlockAt(x, y-1, z).getLocation(), Material.OAK_DOOR);
                        }else if(f < 0.66){
                            DataLayer.setBlueprint(origin.getWorld().getBlockAt(x, y, z).getLocation(), Material.GLASS_PANE);
                        }
                    }
                }
            }
        }
        return true;
    }

    public static boolean Decoration(Block block){
        float picked = StorySpirit.random.nextFloat();




        for(int x=block.getX()-1;x <= block.getX()+1;x++){
            for(int z=block.getZ()-1;z <= block.getZ()+1;z++){
                for(int y=block.getY()-1;y <= block.getY()+1;y++){
                    if(DataLayer.getBlueprint(block.getLocation()) != null){
                        System.out.println("Too Close to Other Blueprint");
                        return false;
                    }
                }
            }
        }

        //in air
        if(DungeonPopulator.air.contains(block.getType())
            && DungeonPopulator.air.contains(block.getRelative(BlockFace.DOWN).getType()) 
            && (block.getRelative(BlockFace.NORTH).getType().isOccluding() || block.getRelative(BlockFace.SOUTH).getType().isOccluding() || block.getRelative(BlockFace.EAST).getType().isOccluding() || block.getRelative(BlockFace.WEST).getType().isOccluding()
            )){
                if(block.getLightFromBlocks()<10){
                    if(picked < 0.5){
                        DataLayer.setBlueprint(block,Material.WALL_TORCH);
                        System.out.println("LAYING FOUNDATION:torch");
                        return true;
                    }
                    if(picked < 0.65 && DungeonPopulator.air.contains(block.getRelative(BlockFace.UP).getType()) && block.getLightFromSky() >13){
                        DataLayer.setBlueprint(block,Material.REDSTONE_LAMP);
                        DataLayer.setBlueprint(block.getRelative(BlockFace.UP),Material.DAYLIGHT_DETECTOR);

                        System.out.println("LAYING FOUNDATION:lamp");
                        return true;
                    }
                }
        }


        //on ground
        if(DungeonPopulator.air.contains(block.getType()) && block.getRelative(BlockFace.DOWN).getType().isOccluding()){
            if(picked < 0.05){
                if(block.getRelative(BlockFace.DOWN).getType() == Material.GRASS_BLOCK){
                    DataLayer.setBlueprint(block,Renovator.groundPlants.get(StorySpirit.random.nextInt(Renovator.groundPlants.size())));
                    System.out.println("LAYING FOUNDATION:plant");
                    return true;
                }
            }
            if(picked < 0.15){
                if(block.getRelative(BlockFace.NORTH).getType().isOccluding() || block.getRelative(BlockFace.SOUTH).getType().isOccluding() || block.getRelative(BlockFace.EAST).getType().isOccluding() || block.getRelative(BlockFace.WEST).getType().isOccluding()){
                    DataLayer.setBlueprint(block,Renovator.pottedPlants.get(StorySpirit.random.nextInt(Renovator.pottedPlants.size())));
                    System.out.println("LAYING FOUNDATION:potplant");
                    return true;
                }
            }
            if(picked < 0.50){
                if(block.getRelative(BlockFace.DOWN).getType() != Material.GRASS_BLOCK && block.getRelative(BlockFace.DOWN).getType() != Material.GRASS_PATH && block.getRelative(BlockFace.DOWN).getType() != Material.SAND && block.getLightFromSky()<14){
                    System.out.println("LAYING FOUNDATION:carpet");
                    DataLayer.setBlueprint(block,Material.GRAY_CARPET);
                    return true;
                }
            }
            if(picked < 0.60){

                if(DungeonPopulator.air.contains(block.getRelative(BlockFace.UP).getType()) && block.getRelative(BlockFace.DOWN).getType().isOccluding() && Renovator.building.contains(block.getRelative(BlockFace.DOWN).getType())){
                    int c = 0;
                    if(block.getRelative(BlockFace.NORTH).getType().isSolid()){ c++; }
                    if(block.getRelative(BlockFace.SOUTH).getType().isSolid()){ c++; }
                    if(block.getRelative(BlockFace.EAST).getType().isSolid()){ c++; }
                    if(block.getRelative(BlockFace.WEST).getType().isSolid()){ c++; }
                    if(c==2){
                        DataLayer.setBlueprint(block,Material.OAK_FENCE);
                        DataLayer.setBlueprint(block.getRelative(BlockFace.UP),Material.OAK_PRESSURE_PLATE);
                        System.out.println("LAYING FOUNDATION:table");
                        return true;
                    }
                }
            }
            if(picked < 0.60){

                if(DungeonPopulator.air.contains(block.getRelative(BlockFace.UP).getType()) && block.getRelative(BlockFace.DOWN).getType().isOccluding() && Renovator.building.contains(block.getRelative(BlockFace.DOWN).getType())){
                    if(block.getRelative(BlockFace.NORTH).getType().isOccluding()
                    || block.getRelative(BlockFace.SOUTH).getType().isOccluding()
                    || block.getRelative(BlockFace.EAST).getType().isOccluding()
                    || block.getRelative(BlockFace.WEST).getType().isOccluding()
                    ){
                        System.out.println("LAYING FOUNDATION:stairs");
                        DataLayer.setBlueprint(block,Material.OAK_STAIRS);
                    }
                }
            }

        }
        return false;
    }

}