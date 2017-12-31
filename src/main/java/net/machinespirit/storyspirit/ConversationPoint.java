package net.machinespirit.storyspirit;

import java.util.ArrayList;
import org.bukkit.inventory.ItemStack;

public class ConversationPoint {
	public float max = 0.5f;
	public float min = -0.5f;
	public String message = "Oh, hello";
	public ItemStack give;
	public float mod = 0f;
	
	public ConversationPoint(String message){
		this.message = message;
	}
	public ConversationPoint(String message, float max,float min,float mod, ItemStack give){
		this.message = message;
		this.max = max;
		this.min = min;
		this.mod = mod;
		this.give = give;
	}
	
	public static ArrayList<ConversationPoint> FromStringset(String[] set, float max,float min,float mod){
		ArrayList<ConversationPoint> ret = new ArrayList<ConversationPoint>();
		for(String s : set){
			ret.add(new ConversationPoint(s, max,min,mod,null));
		}
		return ret;
	}
	
	public String toString(){
		return message+": \r\n"+Float.toString(min)+" - "+Float.toString(max)+" ("+Float.toString(mod)+")\r\n"+(give==null?"NONE":give.toString());
	}
	
}
