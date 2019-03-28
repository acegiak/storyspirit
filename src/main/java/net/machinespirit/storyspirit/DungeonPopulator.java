package net.machinespirit.storyspirit;

import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Dispenser;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Sandstone;
import org.bukkit.material.Torch;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.omg.IOP.ENCODING_CDR_ENCAPS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import javax.swing.text.Position;

import net.machinespirit.storyspirit.StorySpirit;

class DungeonPopulator extends BlockPopulator{

        public static ArrayList<Material> foundations = new ArrayList<Material>(Arrays.asList(
                Material.GRASS,
                Material.STONE,
                Material.DIRT,
                Material.SAND,
                Material.SANDSTONE,
                Material.ACACIA_LOG,
                Material.BIRCH_LOG,
                Material.DARK_OAK_LOG,
                Material.JUNGLE_LOG,
                Material.OAK_LOG,
                Material.SPRUCE_LOG
        ));

        public static ArrayList<Material> logs = new ArrayList<Material>(Arrays.asList(
                Material.ACACIA_LOG,
                Material.BIRCH_LOG,
                Material.DARK_OAK_LOG,
                Material.JUNGLE_LOG,
                Material.OAK_LOG,
                Material.SPRUCE_LOG
        ));
        
        public static Hashtable<Material,Material> leafType = new Hashtable<Material,Material>(){{
                put(Material.OAK_LOG,Material.OAK_LEAVES);
                put(Material.BIRCH_LOG,Material.BIRCH_LEAVES);
                put(Material.DARK_OAK_LOG,Material.DARK_OAK_LEAVES);
                put(Material.JUNGLE_LOG,Material.JUNGLE_LEAVES);
                put(Material.ACACIA_LOG,Material.ACACIA_LEAVES);
                put(Material.SPRUCE_LOG,Material.SPRUCE_LEAVES);
        }};






        public static ArrayList<Material> air = new ArrayList<Material>(Arrays.asList(
                Material.AIR,
                Material.ACACIA_LEAVES,
                Material.BIRCH_LEAVES,
                Material.DARK_OAK_LEAVES,
                Material.JUNGLE_LEAVES,
                Material.OAK_LEAVES,
                Material.SPRUCE_LEAVES,
                Material.SNOW,
                Material.GRASS,
                Material.VINE
        ));


        
        public static ArrayList<Material> dontTouch = new ArrayList<Material>(Arrays.asList(
                        Material.CHEST,
                        Material.DISPENSER,
                        Material.TRIPWIRE,
                        Material.TRIPWIRE_HOOK
        ));

        public static ArrayList<PotionEffectType> debuffs = new ArrayList<PotionEffectType>(Arrays.asList(
                PotionEffectType.LEVITATION,
                PotionEffectType.BLINDNESS,
                PotionEffectType.CONFUSION,
                PotionEffectType.POISON,
                PotionEffectType.HARM,
                PotionEffectType.SLOW,
                PotionEffectType.WEAKNESS,
                PotionEffectType.WITHER,
                PotionEffectType.UNLUCK,
                PotionEffectType.SLOW_DIGGING
        ));



        public static float treasureRate = 0.005f;
        public static float trapRate = 0.015f;
        public static float spawnerRate = 0.10f;
        public static float decoRate = 0.05f;
        public static float fillrate = 0.95f;
        
	@Override
	public void populate(World world, Random random, Chunk chunk) {
                if(random.nextFloat()>1f/175){
                //        System.out.println("CHUNK DONT");
                         return;
                }

                //System.out.println("CHUNK DO");
                Block origin = null;
                
                int ox = random.nextInt(15);
                int oz =  random.nextInt(15);
                ArrayList<Block> possibleOrigins = new ArrayList<Block>();
                for(int nx = 0;nx<15 && origin==null;nx++){
                        for(int nz = 0;nz<15 && origin==null;nz++){
                                int x = (ox+nx)%15;
                                int z = (oz+nz)%15;
                                for(int y = 0; y<255; y++){
                                        Block test = chunk.getBlock(x,y,z);
                                        if(foundations.contains(test.getType())
                                        && air.contains(test.getRelative(0,1,0).getType())
                                        && air.contains(test.getRelative(0,2,0).getType())){
                                                possibleOrigins.add(test);
                                                if(logs.contains(test.getType())){
                                                        possibleOrigins.add(test);
                                                        possibleOrigins.add(test);
                                                        possibleOrigins.add(test);
                                                        possibleOrigins.add(test);
                                                }
                                        }
                                }
                        }
                }
                System.out.println("POSSIBLE ORIGINS:"+Integer.toString(possibleOrigins.size()));
                if(possibleOrigins.size() > 0){
                        origin = possibleOrigins.get(random.nextInt(possibleOrigins.size()));
                }
                if(origin == null){
                        return;
                }
                System.out.println("CHOSE:"+origin.toString());

                if(origin.getType().equals(Material.GRASS) && origin.getRelative(0,1,0).getType().equals(Material.AIR) &&
                origin.getRelative(1,0,1).getType().equals(Material.GRASS) && air.contains(origin.getRelative(1,1,1).getType()) &&
                origin.getRelative(-1,0,1).getType().equals(Material.GRASS) && air.contains(origin.getRelative(-1,1,1).getType()) &&
                origin.getRelative(1,0,-1).getType().equals(Material.GRASS) && air.contains(origin.getRelative(1,1,-1).getType()) &&
                origin.getRelative(-1,0,-1).getType().equals(Material.GRASS) && air.contains(origin.getRelative(-1,1,-1).getType())                    
                ){
                        origin.setType(Material.GRASS_PATH);
                        origin.getRelative(1,0,1).setType(Material.GRASS_PATH);
                        origin.getRelative(-1,0,-1).setType(Material.GRASS_PATH);
                        origin.getRelative(1,0,-1).setType(Material.GRASS_PATH);
                        origin.getRelative(-1,0,1).setType(Material.GRASS_PATH);
                        origin.getRelative(0,-1,0).setType(Material.CHEST);
                        fillchest(origin.getRelative(0,-1,0));
                }else if(logs.contains(origin.getType())){
                        System.out.println("GONNA TRY MAKE A TREEHOUSE, MA");
                        ArrayList<Block> bigtree = new ArrayList<Block>(Arrays.asList(origin.getRelative(1,0,0),origin.getRelative(1,0,1),origin.getRelative(-1,0,0),origin.getRelative(-1,0,1),origin.getRelative(-1,0,-1),origin.getRelative(0,0,-1),origin.getRelative(-1,0,0),origin.getRelative(0,0,1),
                        origin.getRelative(1,-1,0),origin.getRelative(1,-1,1),origin.getRelative(-1,-1,0),origin.getRelative(-1,-1,1),origin.getRelative(-1,-1,-1),origin.getRelative(0,-1,-1),origin.getRelative(-1,-1,0),origin.getRelative(0,-1,1),origin.getRelative(0,-1,0)));
                        int logcount = 0;
                        for (Block b : bigtree) {
                                if(logs.contains(b.getType())){
                                        logcount++;
                                }
                        }

                        System.out.println("WE GOT LOGS: "+Integer.toString(logcount));
                        if(logcount >= 3){
                                System.out.println("WE DOIN IT");;
                                buildRoom(origin.getRelative(0,1,0), 2+StorySpirit.random.nextInt(7), 2+StorySpirit.random.nextInt(7), Arrays.asList(origin.getType()),  Arrays.asList(origin.getType()),  Arrays.asList(origin.getType()), Arrays.asList(null,null,null,leafType.get(origin.getType())),1,0.5f,null, random);
                        }
                }else if(origin.getType().equals(Material.GRASS)||origin.getType().equals(Material.DIRT)){
                        buildRoom(origin.getRelative(0,1,0), 5+StorySpirit.random.nextInt(2), 5+StorySpirit.random.nextInt(2), Arrays.asList(Material.ACACIA_SLAB),  Arrays.asList(Material.ACACIA_FENCE),  Arrays.asList(Material.ACACIA_PLANKS),Arrays.asList(null,null,Material.AIR), -3,0.25f,null, random);
                }else if(origin.getType().equals(Material.SAND) || origin.getType().equals(Material.SANDSTONE )){
                        buildRoom(origin.getRelative(0,1,0), 4+StorySpirit.random.nextInt(5), 4+StorySpirit.random.nextInt(5), Arrays.asList(Material.SANDSTONE),  Arrays.asList(Material.SANDSTONE),  Arrays.asList(Material.SANDSTONE), Arrays.asList(null,null,null,Material.SANDSTONE_STAIRS),1,0.25f,null, random);
                }else{
                        buildRoom(origin.getRelative(0,2,0), 4+StorySpirit.random.nextInt(5), 4+StorySpirit.random.nextInt(5), Arrays.asList(Material.STONE_BRICKS),  Arrays.asList(Material.STONE_BRICKS),  Arrays.asList(Material.STONE_BRICKS), Arrays.asList(null,null,null,Material.IRON_BARS), 1,1f,null, random);
                }

        }

        public void buildRoom(Block origin,int xsize, int zsize, List<Material> roof, List<Material> wall, List<Material> floor, List<Material> crenellations, int interval,float branchChance,List<Block> origins, Random random){
                float filler = 1;
                xsize = (int)Math.floor((double)(xsize/2f))*2+1;
                zsize = (int)Math.floor((double)(zsize/2f))*2+1;
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
                                        if(!dontTouch.contains(thisBlock.getType())){
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
                                        }
                                        allBlocks.add(thisBlock);
                                }
                        }
                }
                if(interval < 2 && interval > -2){
                        //add doors?
                        if(random.nextFloat()<0.5f){
                                if(random.nextFloat()<0.25f){
                                        setBlockType(origin.getRelative(xmin,1,0), Material.IRON_DOOR, 8, true, true);
                                        setBlockType(origin.getRelative(xmin,0,0), Material.IRON_DOOR, 2, true, true);
                                        setBlockType(origin.getRelative(xmin-1,1,1), Material.STONE_BUTTON, 2, true, true);
                                }else if(random.nextFloat()<0.25f){
                                        setBlockType(origin.getRelative(xmin,1,0), Material.OAK_DOOR, 8, true, true);
                                        setBlockType(origin.getRelative(xmin,0,0), Material.OAK_DOOR, 2, true, true);
                                }else{
                                        origin.getRelative(xmin,1,0).setType(Material.AIR);
                                        origin.getRelative(xmin,0,0).setType(Material.AIR);
                                }
                        }
                        if(random.nextFloat()<0.5f){
                                if(random.nextFloat()<0.25f){
                                        setBlockType(origin.getRelative(xmax,1,0), Material.IRON_DOOR, 8, true, true);
                                        setBlockType(origin.getRelative(xmax,0,0), Material.IRON_DOOR, 2, true, true);
                                        setBlockType(origin.getRelative(xmax+1,1,1), Material.STONE_BUTTON, 1, true, true);
                                }else if(random.nextFloat()<0.25f){
                                        setBlockType(origin.getRelative(xmax,1,0), Material.OAK_DOOR, 8, true, true);
                                        setBlockType(origin.getRelative(xmax,0,0), Material.OAK_DOOR, 2, true, true);
                                }else{
                                        origin.getRelative(xmax,1,0).setType(Material.AIR);
                                        origin.getRelative(xmax,0,0).setType(Material.AIR);
                                }
                        }
                        if(random.nextFloat()<0.5f){
                                if(random.nextFloat()<0.25f){
                                        setBlockType(origin.getRelative(0,1,zmin), Material.IRON_DOOR, 8, true, true);
                                        setBlockType(origin.getRelative(0,0,zmin), Material.IRON_DOOR, 1, true, true);
                                        setBlockType(origin.getRelative(1,1,zmin-1), Material.STONE_BUTTON, 4, true, true);

                                }else if(random.nextFloat()<0.25f){
                                        setBlockType(origin.getRelative(0,1,zmin), Material.OAK_DOOR, 8, true, true);
                                        setBlockType(origin.getRelative(0,0,zmin), Material.OAK_DOOR, 3, true, true);
                                }else{
                                        origin.getRelative(0,1,zmin).setType(Material.AIR);
                                        origin.getRelative(0,0,zmin).setType(Material.AIR);
                                }
                        }
                        if(random.nextFloat()<0.5f){
                                if(random.nextFloat()<0.25f){
                                        setBlockType(origin.getRelative(0,1,zmax), Material.IRON_DOOR, 8, true, true);
                                        setBlockType(origin.getRelative(0,0,zmax), Material.IRON_DOOR, 1, true, true);
                                        setBlockType(origin.getRelative(1,1,zmax+1), Material.STONE_BUTTON, 3, true, true);
                                }else if(random.nextFloat()<0.25f){
                                        setBlockType(origin.getRelative(0,1,zmax), Material.OAK_DOOR, 8, true, true);
                                        setBlockType(origin.getRelative(0,0,zmax), Material.OAK_DOOR, 4, true, true);
                                }else{
                                        origin.getRelative(0,1,zmax).setType(Material.AIR);
                                        origin.getRelative(0,0,zmax).setType(Material.AIR);
                                }
                        }
                }

                boolean traps = true;
                boolean light = true;
                boolean stairs = true;
                boolean spiders = true;

                if(random.nextFloat()*filler<spawnerRate && interval <2 && interval >-2){
                        filler = 1f;
                        origin.setType(Material.SPAWNER);
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
                        
                }else if(random.nextFloat()*filler<spawnerRate ){
                        Character.spawn(origin.getWorld(), origin.getRelative(0,1,0).getLocation(), "foe", random.nextInt(20));
                        traps = false;
                }else if(random.nextFloat()*filler<spawnerRate*0.5f ){
                        Character.spawn(origin.getWorld(), origin.getRelative(0,1,0).getLocation(), "friend", random.nextInt(20));
                        traps = false;
                        origin.getRelative(1,1,0).setType(Material.IRON_BARS);
                        origin.getRelative(1,1,1).setType(Material.IRON_BARS);
                        origin.getRelative(1,1,-1).setType(Material.IRON_BARS);
                        origin.getRelative(-1,1,0).setType(Material.IRON_BARS);
                        origin.getRelative(-1,1,1).setType(Material.IRON_BARS);
                        origin.getRelative(-1,1,-1).setType(Material.IRON_BARS);
                        origin.getRelative(0,1,1).setType(Material.IRON_BARS);
                        origin.getRelative(0,1,-1).setType(Material.IRON_BARS);
                        origin.getRelative(1,0,0).setType(Material.IRON_BARS);
                        origin.getRelative(1,0,1).setType(Material.IRON_BARS);
                        origin.getRelative(1,0,-1).setType(Material.IRON_BARS);
                        origin.getRelative(-1,0,0).setType(Material.IRON_BARS);
                        origin.getRelative(-1,0,1).setType(Material.IRON_BARS);
                        origin.getRelative(-1,0,-1).setType(Material.IRON_BARS);
                        origin.getRelative(0,0,1).setType(Material.IRON_BARS);
                        origin.getRelative(0,0,-1).setType(Material.IRON_BARS);
                }else{
                        filler = filler*fillrate;
                }

                

                Collections.shuffle(allBlocks);

                for (Block ablock : allBlocks) {

                        if(traps){

                                //START TRIPWIRES
                                boolean ew = random.nextBoolean();
                                boolean trip = false;
                                if(ablock.getRelative(1,0,0).getType().equals(Material.AIR)&&ablock.getRelative(-1,0,0).getType().equals(Material.AIR)){
                                        ew = true;
                                        trip = true;
                                }
                                if(ablock.getRelative(0,0,1).getType().equals(Material.AIR)&&ablock.getRelative(0,0,-1).getType().equals(Material.AIR)){
                                        ew = false;
                                        trip = true;
                                }
                                if(random.nextFloat()*filler<trapRate && trip){
                                        filler = 1f;
                                        List<Block> bblocks = new ArrayList<Block>();
                                        List<Block> seenblocks = new ArrayList<Block>();
                                        bblocks.add(ablock);
                                        seenblocks.add(ablock);
                                        
                                        List<Block> sblocks = new ArrayList<Block>();
                                        int triggercount = 0;
                                        while(bblocks.size() > 0){
                                                Block bblock = bblocks.get(0);
                                                bblocks.remove(0);
                                                if(bblock.getType().equals(Material.AIR) && bblock.getRelative(0,-1,0).getType().isOccluding()){
                                                if(!ew){
                                                        if(allBlocks.contains(bblock)){
                                                                if(!seenblocks.contains(bblock.getRelative(0,0,1))){
                                                                        bblocks.add(bblock.getRelative(0,0,1));
                                                                        seenblocks.add(bblock.getRelative(0,0,1));
                                                                }
                                                                if(!seenblocks.contains(bblock.getRelative(0,0,-1))){
                                                                        bblocks.add(bblock.getRelative(0,0,-1));
                                                                        seenblocks.add(bblock.getRelative(0,0,-1));
                                                                }
                                                        
                                                                if(bblock.getRelative(0,0,-1).getType().isOccluding()){
                                                                        setBlockType(bblock, Material.TRIPWIRE_HOOK, 4, true,true);
                                                                        setupLauncher(bblock.getRelative(0,1,-1),3);
                                                                        triggercount++;
                                                                }else if(bblock.getRelative(0,0,1).getType().isOccluding()){
                                                                        setBlockType(bblock, Material.TRIPWIRE_HOOK, 6, true,true);
                                                                        setupLauncher(bblock.getRelative(0,1,1),2);
                                                                        triggercount++;
                                                                }else {
                                                                        sblocks.add(bblock);
                                                                }
                                                        }
                                                }else{
                                                        if(allBlocks.contains(bblock)){

                                                                if(!seenblocks.contains(bblock.getRelative(1,0,0))){
                                                                        bblocks.add(bblock.getRelative(1,0,0));
                                                                        seenblocks.add(bblock.getRelative(1,0,0));
                                                                }
                                                                if(!seenblocks.contains(bblock.getRelative(-1,0,0))){
                                                                        bblocks.add(bblock.getRelative(-1,0,0));
                                                                        seenblocks.add(bblock.getRelative(-1,0,0));
                                                                }

                                                                if(bblock.getRelative(-1,0,0).getType().isOccluding()){
                                                                        setBlockType(bblock, Material.TRIPWIRE_HOOK, 7, true,true);
                                                                        setupLauncher(bblock.getRelative(-1,1,0),5);
                                                                        triggercount++;
                                                                } else if(bblock.getRelative(1,0,0).getType().isOccluding()){
                                                                        setBlockType(bblock, Material.TRIPWIRE_HOOK, 5, true,true);
                                                                        setupLauncher(bblock.getRelative(1,1,0),4);
                                                                        triggercount++;
                                                                }else{
                                                                        sblocks.add(bblock);
                                                                }
                                                        }
                                                }
                                        }
                                        }
                                        if(triggercount > 1){
                                                for (Block sBlock : sblocks) {
                                                        sBlock.setType(Material.TRIPWIRE);
                                                        BlockState state = sBlock.getState();
                                                        state.setRawData((byte)4);
                                                        state.update(true,true);
                                                }
                                        }
                                }else{
                                        filler = filler*fillrate;
                                }


                                //START PRESSURE PLATE TNT
                                if(!ablock.getRelative(0,-2,0).getType().equals(Material.AIR)&&random.nextFloat()*filler<trapRate){
                                        filler = 1;
                                        if(ablock.getType().equals(Material.STONE_BRICKS) && ablock.getRelative(0,1,0).getType().equals(Material.AIR) && ablock.getY()<origin.getRelative(0,ymax,0).getY()){
                                                ablock.getRelative(0,1,0).setType(Material.STONE_PRESSURE_PLATE);
                                                ablock.getRelative(0,-1,0).setType(Material.TNT);
                                        }else if(ablock.getType().equals(Material.ACACIA_PLANKS) && ablock.getRelative(0,1,0).getType().equals(Material.AIR) && ablock.getY()<origin.getRelative(0,ymax,0).getY()){
                                                ablock.getRelative(0,1,0).setType(Material.ACACIA_PRESSURE_PLATE);
                                                ablock.getRelative(0,-1,0).setType(Material.TNT);
                                        }else if(ablock.getType().equals(Material.SANDSTONE) && ablock.getRelative(0,1,0).getType().equals(Material.AIR) && ablock.getY()<origin.getRelative(0,ymax,0).getY()){
                                                ablock.getRelative(0,1,0).setType(Material.STONE_PRESSURE_PLATE);
                                                ablock.getRelative(0,-1,0).setType(Material.TNT);
                                        }
                                }else{
                                        filler = filler * 0.9f;
                                }
                                //PRESSURE PLATE SHOOTER
                                if(ablock.getRelative(0,-1,0).getType().isOccluding() && ablock.getType().equals(Material.AIR) &&random.nextFloat()*filler<trapRate){
                                        filler = 1;
                                        ablock.setType(Material.STONE_PRESSURE_PLATE);
                                        if(ablock.getRelative(0,0,-1).getType().isOccluding()){ //NORTH
                                                setupLauncher(ablock.getRelative(0,0,-1),3);
                                        }
                                        else if(ablock.getRelative(0,0,1).getType().isOccluding()){ //SOUTH
                                                setupLauncher(ablock.getRelative(0,0,1),2);
                                        }
                                        else if(ablock.getRelative(-1,0,0).getType().isOccluding()){ //WEST
                                                setupLauncher(ablock.getRelative(1,0,0),4);
                                        }     
                                        else if(ablock.getRelative(-1,0,0).getType().isOccluding()){//EAST
                                                setupLauncher(ablock.getRelative(1,0,0),5);
                                        }
                                }else{
                                        filler=filler*fillrate;
                                }

                        }
                        if(light){
                                if(ablock.getType().equals(Material.AIR) && ablock.getRelative(0,-1,0).getType().equals(Material.AIR) &&random.nextFloat()*filler<decoRate){
                                        filler=1f;
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
                                        else if(ablock.getRelative(1,0,0).getType().isOccluding()){ //WEST
                                                ablock.setType(Material.TORCH);
                                                BlockState state = ablock.getState();
                                                state.setType(Material.TORCH);
                                                state.setRawData((byte)2);
                                                state.update(true,true);
                                        }     
                                        else if(ablock.getRelative(-1,0,0).getType().isOccluding()){//EAST
                                                ablock.setType(Material.TORCH);
                                                BlockState state = ablock.getState();
                                                state.setType(Material.TORCH);
                                                state.setRawData((byte)1);
                                                state.update(true,true);
                                        }
                                }else{
                                        filler=filler*fillrate;
                                }
                        }



                        if(ablock.getType().equals(Material.AIR) && ablock.getRelative(0,-1,0).getType().equals(Material.AIR) &&random.nextFloat()<decoRate){
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
                                else if(ablock.getRelative(1,0,0).getType().isSolid()){ //WEST
                                        ablock.setType(Material.VINE);
                                        BlockState state = ablock.getState();
                                        state.setType(Material.VINE);
                                        state.setRawData((byte)8);
                                        state.update(true,true);
                                }     
                                else if(ablock.getRelative(-1,0,0).getType().isSolid()){//EAST
                                        ablock.setType(Material.VINE);
                                        BlockState state = ablock.getState();
                                        state.setType(Material.VINE);
                                        state.setRawData((byte)2);
                                        state.update(true,true);
                                }
                                bblock = bblock.getRelative(0,-1,0);
                        }while(bblock.getType().equals(Material.AIR));
                        }
                

                        if(ablock.getType().equals(Material.AIR) && ablock.getRelative(0,-1,0).getType().isOccluding() && random.nextFloat()*filler<treasureRate){
                                filler = 1;
                                fillchest(ablock);
                        }else{
                                filler=filler*fillrate;
                        }
                        if(ablock.getType().equals(Material.AIR) && ablock.getRelative(0,-1,0).getType().isOccluding() && random.nextFloat()*filler<treasureRate){
                                filler=1;
                                ablock.setType(Material.TRAPPED_CHEST);
                                List<ItemStack> contents = Loot.getTreasure(ablock.getLocation());
                                if(!ChestUtils.addItemsToChest(ablock, contents,true,StorySpirit.random)){
                                        System.out.println("Could not add items to chest");
                                }
                                ablock.getRelative(0,-2,0).setType(Material.TNT);
                        }else{
                                filler=filler*fillrate;
                        }

                        


                        if(ablock.getType().equals(Material.STONE_BRICKS) && ablock.getRelative(0,1,0).getType().equals(Material.AIR)&&random.nextFloat()*filler<treasureRate && ablock.getY()<origin.getRelative(0,ymax,0).getY()){
                                filler=1;
                                fillchest(ablock);
                                setBlockType(ablock.getRelative(0,1,0), Material.GRAY_CARPET, 8, true, true);
                        }else if(ablock.getType().equals(Material.ACACIA_PLANKS) && ablock.getRelative(0,1,0).getType().equals(Material.AIR)&&random.nextFloat()*filler<treasureRate && ablock.getY()<origin.getRelative(0,ymax,0).getY()){
                                filler=1;
                                fillchest(ablock);
                                setBlockType(ablock.getRelative(0,1,0), Material.ORANGE_CARPET, 12, true, true);
                        }else if(ablock.getType().equals(Material.SANDSTONE) && ablock.getRelative(0,1,0).getType().equals(Material.AIR)&&random.nextFloat()*filler<treasureRate && ablock.getY()<origin.getRelative(0,ymax,0).getY()){
                                filler=1;
                                fillchest(ablock);
                                setBlockType(ablock.getRelative(0,1,0), Material.YELLOW_CARPET, 4, true, true);                               
                        }else{
                                filler=filler*fillrate;
                        }

                        if(ablock.getType().equals(Material.AIR) && ablock.getRelative(0,-1,0).getType().equals(Material.AIR) &&random.nextFloat()*filler<treasureRate*0.25){
                                filler=1;
                                if(ablock.getRelative(0,0,-1).getType().isOccluding()){
                                        ablock.setType(Material.ITEM_FRAME);
                                        ItemFrame itemFrame = (ItemFrame)ablock.getWorld().spawnEntity(ablock.getLocation(), EntityType.ITEM_FRAME);
                                        itemFrame.setItem(new ItemStack(Loot.randomNamed(ablock.getLocation())));
                                        itemFrame.setFacingDirection(BlockFace.NORTH);

                                }else if(ablock.getRelative(0,0,1).getType().isOccluding()){
                                        ablock.setType(Material.ITEM_FRAME);
                                        ItemFrame itemFrame = (ItemFrame)ablock.getWorld().spawnEntity(ablock.getLocation(), EntityType.ITEM_FRAME);
                                        itemFrame.setItem(new ItemStack(Loot.randomNamed(ablock.getLocation())));
                                        itemFrame.setFacingDirection(BlockFace.SOUTH);
                                }else if(ablock.getRelative(1,0,0).getType().isOccluding()){
                                        ablock.setType(Material.ITEM_FRAME);
                                        ItemFrame itemFrame = (ItemFrame)ablock.getWorld().spawnEntity(ablock.getLocation(), EntityType.ITEM_FRAME);
                                        itemFrame.setItem(new ItemStack(Loot.randomNamed(ablock.getLocation())));
                                        itemFrame.setFacingDirection(BlockFace.WEST);

                                }else if(ablock.getRelative(-1,0,0).getType().isOccluding()){
                                        ablock.setType(Material.ITEM_FRAME);
                                        ItemFrame itemFrame = (ItemFrame)ablock.getWorld().spawnEntity(ablock.getLocation(), EntityType.ITEM_FRAME);
                                        itemFrame.setItem(new ItemStack(Loot.randomNamed(ablock.getLocation())));
                                        itemFrame.setFacingDirection(BlockFace.EAST);
                                }
                                
                        }else{
                                filler = filler * 0.9f;
                        }


                        if(ablock.getType().equals(Material.AIR)&&(spiders ^ random.nextFloat()>decoRate*0.25f)){
                                ablock.setType(Material.COBWEB);
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


                        if(ablock.getY() == origin.getRelative(0,ymin,0).getY() && (ablock.getZ() == origin.getRelative(0,0,zmax).getZ() || ablock.getZ() == origin.getRelative(0,0,zmin).getZ())&& (ablock.getX() == origin.getRelative(xmax,0,0).getX() || ablock.getX() == origin.getRelative(xmin,0,0).getX() )){
                                Block bblock = ablock.getRelative(0,-1,0);
                                while(bblock.getType().equals(Material.AIR)||bblock.getType().equals(Material.WATER)){
                                        bblock.setType(floor.get(random.nextInt(floor.size())));
                                        bblock = bblock.getRelative(0,-1,0);
                                }
                                
                                bblock.setType(floor.get(random.nextInt(floor.size())));

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

                                origin.getRelative(0,ymin+1,0).setType(Material.OAK_TRAPDOOR);

                                origin.getRelative(0,ymin,0).setType(Material.LADDER);
                                Block ndown = origin.getRelative(0,ymin,0);
                                while(ndown.getType().equals(Material.AIR) && ndown.getRelative(0,0,1).getType().equals(Material.AIR)){
                                        ndown.getRelative(0,0,1).setType(floor.get(random.nextInt(floor.size())));
                                        ndown.setType(Material.LADDER);
                                        ndown = origin.getRelative(0,-1,0);
                                }
                        }
                        if(random.nextFloat()<branchChance*0.05f){
                                buildRoom(origin.getRelative(0,-1*ymin+ymax,0),xsize,zsize,roof,wall,floor,Arrays.asList(type),interval,newBranchChance,origins,random);
                                origin.getRelative(0,ymax+1,0).setType(Material.OAK_TRAPDOOR);

                                origin.getRelative(0,ymax,0).setType(Material.LADDER);

                                Block ndown = origin.getRelative(0,ymax,0);
                                while(ndown.getType().equals(Material.AIR) && ndown.getRelative(0,0,1).getType().equals(Material.AIR)){
                                        ndown.getRelative(0,0,1).setType(floor.get(random.nextInt(floor.size())));
                                        ndown.setType(Material.LADDER);
                                        ndown = origin.getRelative(0,-1,0);
                                }
                        }
                }

                //HIDE TNT
                for(Block ablock : allBlocks){

                        if(ablock.getType().equals(Material.TNT)){
                                if(ablock.getRelative(0,-1,0).getType().equals(Material.AIR)){ablock.getRelative(0,-1,0).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(1,0,0).getType().equals(Material.AIR)){ablock.getRelative(1,0,0).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(-1,0,0).getType().equals(Material.AIR)){ablock.getRelative(-1,0,0).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(1,0,1).getType().equals(Material.AIR)){ablock.getRelative(1,0,1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(-1,0,1).getType().equals(Material.AIR)){ablock.getRelative(-1,0,1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(1,0,-1).getType().equals(Material.AIR)){ablock.getRelative(1,0,-1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(-1,0,-1).getType().equals(Material.AIR)){ablock.getRelative(-1,0,-1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(0,0,1).getType().equals(Material.AIR)){ablock.getRelative(0,0,1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(0,0,-1).getType().equals(Material.AIR)){ablock.getRelative(0,0,-1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(1,-1,0).getType().equals(Material.AIR)){ablock.getRelative(1,-1,0).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(-1,-1,0).getType().equals(Material.AIR)){ablock.getRelative(-1,-1,0).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(1,-1,1).getType().equals(Material.AIR)){ablock.getRelative(1,-1,1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(-1,-1,1).getType().equals(Material.AIR)){ablock.getRelative(-1,-1,1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(1,-1,-1).getType().equals(Material.AIR)){ablock.getRelative(1,-1,-1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(-1,-1,-1).getType().equals(Material.AIR)){ablock.getRelative(-1,-1,-1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(0,-1,1).getType().equals(Material.AIR)){ablock.getRelative(0,-1,1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(0,-1,-1).getType().equals(Material.AIR)){ablock.getRelative(0,-1,-1).setType(floor.get(random.nextInt(floor.size())));}
                        }

                        if(ablock.getRelative(0,-1,0).getType().equals(Material.TNT)){
                                if(ablock.getRelative(0,-2,0).getType().equals(Material.AIR)){ablock.getRelative(0,-2,0).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(1,-1,0).getType().equals(Material.AIR)){ablock.getRelative(1,-1,0).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(-1,-1,0).getType().equals(Material.AIR)){ablock.getRelative(-1,-1,0).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(1,-1,1).getType().equals(Material.AIR)){ablock.getRelative(1,-1,1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(-1,-1,1).getType().equals(Material.AIR)){ablock.getRelative(-1,-1,1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(1,-1,-1).getType().equals(Material.AIR)){ablock.getRelative(1,-1,-1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(-1,-1,-1).getType().equals(Material.AIR)){ablock.getRelative(-1,-1,-1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(0,-1,1).getType().equals(Material.AIR)){ablock.getRelative(0,-1,1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(0,-1,-1).getType().equals(Material.AIR)){ablock.getRelative(0,-1,-1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(1,-2,0).getType().equals(Material.AIR)){ablock.getRelative(1,-2,0).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(-1,-2,0).getType().equals(Material.AIR)){ablock.getRelative(-1,-2,0).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(1,-2,1).getType().equals(Material.AIR)){ablock.getRelative(1,-2,1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(-1,-2,1).getType().equals(Material.AIR)){ablock.getRelative(-1,-2,1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(1,-2,-1).getType().equals(Material.AIR)){ablock.getRelative(1,-2,-1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(-1,-2,-1).getType().equals(Material.AIR)){ablock.getRelative(-1,-2,-1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(0,-2,1).getType().equals(Material.AIR)){ablock.getRelative(0,-2,1).setType(floor.get(random.nextInt(floor.size())));}
                                if(ablock.getRelative(0,-2,-1).getType().equals(Material.AIR)){ablock.getRelative(0,-2,-1).setType(floor.get(random.nextInt(floor.size())));}
                        }
                }

                
        }


        public void fillchest(Block block){
                block.setType(Material.CHEST);
                if(!ChestUtils.isChest(block)){
                        System.out.println("Tried to put things in a not-chest!");
                }
                List<ItemStack> contents = Loot.getTreasure(block.getLocation());
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
        public static void setupLauncher(Block block, int direction){
                List<ItemStack> projectiles = new ArrayList<ItemStack>();
                projectiles.add(new ItemStack(Material.ARROW,4));
                projectiles.add(new ItemStack(Material.ARROW,4));
                projectiles.add(new ItemStack(Material.ARROW,4));
                projectiles.add(new ItemStack(Material.EGG,4));
                projectiles.add(new ItemStack(Material.SNOWBALL,4));



                ItemStack potion = new ItemStack(Material.SPLASH_POTION, 10);
                PotionMeta potionmeta = (PotionMeta) potion.getItemMeta();
                PotionEffectType debuff = debuffs.get(StorySpirit.random.nextInt(debuffs.size()));      
                potionmeta.addCustomEffect(new PotionEffect(debuff,StorySpirit.random.nextInt(5*60)*20,StorySpirit.random.nextInt(3)+1),true);
                potionmeta.setDisplayName(StorySpirit.toTitleCase("Splash Potion Of "+debuff.getName()));
                potion.setItemMeta(potionmeta);
                projectiles.add(potion);
                

                block.setType(Material.DISPENSER);
                BlockState state = block.getState();
                state.setRawData((byte)direction);
                state.update(true,true);
                if(state instanceof Dispenser){
                        Inventory inventory = ((Dispenser)state).getInventory();
                        int count = StorySpirit.random.nextInt(9);
                        for(int i = 0; i<count;i++){
                                        inventory.addItem(projectiles.get(StorySpirit.random.nextInt(projectiles.size())));
                        }
                }
                
        }
}