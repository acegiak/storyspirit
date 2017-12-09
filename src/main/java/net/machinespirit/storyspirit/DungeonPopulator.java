package net.machinespirit.storyspirit;


import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import java.util.Random;
import net.machinespirit.storyspirit.StorySpirit;

class DungeonPopulator extends BlockPopulator{

	@Override
	public void populate(World world, Random random, Chunk chunk) {
        // Block block = chunk.getBlock(15,90,15);
        // block.setType(Material.GOLD_BLOCK);
        // System.out.println("ADDED GOLD CORNER");
	}

}