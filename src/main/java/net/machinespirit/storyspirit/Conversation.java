package net.machinespirit.storyspirit;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;


public class Conversation {
	static String[] general = new String[]{"What do you want?","Have I got a deal for you!","Who are you supposed to be?","I'm growing a beard!","Burn the bodies but keep the skulls!","What's your luckydo?"};

	public static String talk(Villager villi,Player p){
		Character.villiconvert(villi);
		
		for(Object o : villi.getWorld().getLivingEntities()){
            if(o instanceof Villager){
                Character.villiconvert((Villager) o);
                // float avg = (gossip.opinionOf(p.getName())+talker.opinionOf(p.getName()))/2f;
                // gossip.opinionGoes(p.getName(),(avg - gossip.opinionOf(p.getName()))*0.05f);
                // talker.opinionGoes(p.getName(),(avg - talker.opinionOf(p.getName()))*0.05f);
            }
		}
		

		ArrayList<ConversationPoint> points = new ArrayList<ConversationPoint>();
		// String questy = null;
		// if(Character.rand.nextFloat() < talker.opinionOf(p.getName())){
		// 	questy = delivery(villi,p);
		// }
		// if(questy != null){
		// 	return questy;
		// }
		
		
		String ret = villi.getCustomName()+": ";
        ArrayList<String> choices = new ArrayList<String>(Arrays.asList(general));
        
        for(Object r : villi.getRecipes()){
			MerchantRecipe recipe = (MerchantRecipe)r;
			if(!recipe.getResult().getType().equals(Material.EMERALD)){
				choices.add("You wan buy "+recipe.getResult().getType().toString().replace('_', ' ')+"?");
				choices.add("Do you need "+recipe.getResult().getType().toString().replace('_', ' ')+"?");
				choices.add("I have plenty of "+recipe.getResult().getType().toString().replace('_', ' ')+"!");
				if(recipe.getIngredients().get(0).getType().equals(Material.EMERALD)){
					points.add(new ConversationPoint("Here, Friend! Take some "+recipe.getResult().getType().toString(),20f,Math.max(1f,(float)recipe.getIngredients().get(0).getAmount()),Math.max(1f,(float)recipe.getIngredients().get(0).getAmount())*-0.5f,recipe.getResult()));
					points.add(new ConversationPoint(p.getName()+"! Here, have this "+recipe.getResult().getType().toString()+"! For you!",20f,Math.max(1f,(float)recipe.getIngredients().get(0).getAmount()),Math.max(1f,(float)recipe.getIngredients().get(0).getAmount())*-0.5f,recipe.getResult()));
					points.add(new ConversationPoint("Oh, also, I got you this "+recipe.getResult().getType().toString()+". Take it.",20f,Math.max(1f,(float)recipe.getIngredients().get(0).getAmount()),Math.max(1f,(float)recipe.getIngredients().get(0).getAmount())*-0.5f,recipe.getResult()));
					points.add(new ConversationPoint("You look like you need "+recipe.getResult().getType().toString()+". Take it.",20f,Math.max(1f,(float)recipe.getIngredients().get(0).getAmount()),Math.max(1f,(float)recipe.getIngredients().get(0).getAmount())*-0.5f,recipe.getResult()));
					points.add(new ConversationPoint("You need "+recipe.getResult().getType().toString()+" more than me.",20f,Math.max(1f,(float)recipe.getIngredients().get(0).getAmount()),Math.max(1f,(float)recipe.getIngredients().get(0).getAmount())*-0.5f,recipe.getResult()));
				}
				
			}
			if(!recipe.getIngredients().get(0).getType().equals(Material.EMERALD)){
				choices.add("You got any "+recipe.getIngredients().get(0).getType().toString().replace('_', ' ')+"?");
				choices.add("I need some "+recipe.getIngredients().get(0).getType().toString().replace('_', ' ')+".");
				choices.add("If you see any "+recipe.getIngredients().get(0).getType().toString().replace('_', ' ')+" let me know.");
			}
		}
		// // for(Quest q : Quest.quests){
		// 	String qtext = q.QuestText(villi);
		// 	if(qtext != null){
		// 		choices.add(qtext);
		// 	}
		// }
		String[] c = choices.toArray(new String[choices.size()]);
		
		points.addAll(ConversationPoint.FromStringset(c, 5f, -0.5f, -0.005f));
		
		points.addAll(ConversationPoint.FromStringset(new String[]{"Me not that kind of villager!","Oh no, not you again.","Quit touchin me!","What?","WHAT?","WHAT?!","WHAT IS IT THAT YOU WANT?!","Get out of here.","You're not welcome here, stranger.","Are you as stupid as you look?","Why are you bothering us?","Please, just leave us alone.","We don't have anything for you!","Take your big swinging limbs and get out of here","Please leave.","You disgust me."}, 0f, -10f, -0.01f));
		points.addAll(ConversationPoint.FromStringset(new String[]{"Oh, hello friend!", "Oh it's you again! Great!","What can we do for you?","Welcome to our village!","Oh! hello "+p.getName()+"!","Can I get you anything?"}, 10f, 0.5f, +0.005f));
		points.addAll(ConversationPoint.FromStringset(new String[]{"Hey there!", "Please tell us an adventure story!","Do you remember that time with the thing?","Blessings upon you, friend!","Good to see you, "+p.getName()+"!","What can I do for an upstanding citizen such as yourself?"}, 10f, 1f, +0.005f));
		
		float rep = getOpinion(villi,p);
		for(int i = points.size()-1;i>=0;i--){
			if(rep > points.get(i).max || rep < points.get(i).min){
				points.remove(i);
			}
		}
		if(points.size() <1){
			return ret+" Ugh.";
		}
        ConversationPoint point = points.get(StorySpirit.random.nextInt(points.size()));
        changeOpinion(villi, p, point.mod);
		System.out.println(point.toString());
		if(point.give != null){
			if(point.give.getAmount() < 1){
				point.give.setAmount(1);
			}
            System.out.print("GIVING ITEM: "+point.give.toString());
            Item item = (Item)p.getWorld().spawnEntity(p.getLocation(), EntityType.DROPPED_ITEM);
            item.setItemStack(point.give);
		}

		//System.out.println(talker.name+"("+villi.getPersistentID().toString()+":"+talker.id.toString()+")'s opinion of "+p.getName()+" is "+Float.toString(talker.opinionOf(p.getName())));
		//System.out.println(villi.getCustomName()+"("+villi.getUniqueID().toString()+":"+talker.id.toString()+")'s opinion of "+p.getName()+" is "+Float.toString(talker.opinionOf(p.getName())));
		return ret+point.message;
	}
	

	// public static String delivery(Villager villi,Player player){
	// 	if(Namer.rand.nextInt(StoryEventHandler.villagequestchance)<1){
	// 		Character giver = Character.villiconvert((EntityLivingBase) villi);
	
	// 		Character c = Quest.questgiver(villi.worldObj, villi.getPosition());
	// 		if(giver.id != c.id){
	// 			String n = null;
	// 			if(c != null){
	// 				ItemStack s = null;
	// 				switch(new Random().nextInt(5)){
	// 				case 0:
	// 					s = new ItemStack(Items.book);
	// 					n= Namer.random(new String[]{"Note","Missive","Letter","Order","Instructions"})+" from "+giver.name;
	// 					break;
	// 				case 1:
	// 					s = new ItemStack(Blocks.chest);
	// 					n=Namer.random(new String[]{"Package","Bundle","Box","Order","Delivery"})+" from "+giver.name;
	// 					break;
	// 				case 2:
	// 					s = new ItemStack(Items.paper);
	// 					n=Namer.random(new String[]{"Note","Missive","Letter","Order","Instructions"})+" from "+giver.name;
	// 					break;
	// 				case 3:
	// 					s = new ItemStack(Items.armor_stand);
	// 					n=Namer.random(new String[]{"Doll","Gift","Contraption"})+" from "+giver.name;
	// 					break;
	// 				case 4:
	// 					s = new ItemStack(Blocks.trapped_chest);
	// 					n=Namer.random(new String[]{"Package","Bundle","Box","Order","Delivery"})+" from "+giver.name;
	// 					break;
	// 				case 5:
	// 					s = new ItemStack(Items.rabbit_stew);
	// 					n=Namer.random(new String[]{"Meal","Ingredients","Food","Bits","Delivery"})+" from "+giver.name;
	// 					break;
	// 				case 6:
	// 					s = new ItemStack(Items.painting);
	// 					n=Namer.random(new String[]{"Self Portait","Lewd Illustration","Mysterious Landscape","Abstract Art"})+" from "+giver.name;
	// 					break;
	// 				case 7:
	// 					s = new ItemStack(Items.wooden_hoe);
	// 					n=Namer.random(new String[]{"Old Tool","Dirty Hoe","Spare Hoe","Broken Tool"})+" from "+giver.name;
	// 					break;
						
	// 				}
		
	// 				NBTTagCompound display = new NBTTagCompound();
	// 				NBTTagCompound l = new NBTTagCompound();
	// 				NBTTagList lr = new NBTTagList();
					
	// 				l.setString("Name",n );
	// 				lr.appendTag(new NBTTagString("Delivery for "+c.name));
	// 				l.setTag("Lore", lr);
	// 				display.setTag("display", l);
	// 				s.setTagCompound(display);
	// 				Quest q = new Quest();
	// 				q.loot = n;
	// 				q.questgiver = c ;
	// 				q.title = "Deliver "+n+" to "+c.name;
	// 				q.shortname = n;
	// 				Entity e = c.getEntity();
	// 				if(e == null){
	// 					return null;
	// 				}
	// 				q.lootPos = c.getPosition();
	// 				q.store();
	// 				villi.worldObj.spawnEntityInWorld(new EntityItem(villi.worldObj,villi.posX,villi.posY,villi.posZ,s));
	// 				return villi.getName()+": Please take this to "+c.name+(new Random().nextBoolean()?"":" to the "+q.DirectionFrom(villi.getPosition()))+".";
	// 			}
	// 		}
			
	// 	}
	// 	return null;
    // }
    
    static public float getOpinion(Villager villi, Player p){
        if(!villi.hasMetadata(p.getUniqueId().toString()+":opinion")){
            Opinion defaultvalue = new Opinion(0.5f);
            villi.setMetadata(p.getUniqueId().toString()+":opinion",defaultvalue);
        }
        return villi.getMetadata(p.getUniqueId().toString()+":opinion").get(0).asFloat();
    }

    static public void changeOpinion(Villager villi, Player p, float mod){
        float existing = villi.getMetadata(p.getUniqueId().toString()+":opinion").get(0).asFloat();
        villi.setMetadata(p.getUniqueId().toString()+":opinion",new Opinion(existing+mod));
    }
}
