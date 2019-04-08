package net.machinespirit.storyspirit;

import java.util.Collection;
import java.util.Random;

public class Namer {
	static String[] vowels = new String[]{"a","e","i","o","u","a","e","i","o","u","a","e","i","o","u","y","oo","ay","oy","oi","ee","ea","ie"};
	static String[] consonants = new String[]{"b","d","g","h","k","l","m","n","p","b","d","g","h","k","l","m","n","p","b","d","g","h","k","l","m","n","p","b","d","g","h","k","l","m","n","p","r","s","t","qu","r","s","t","v","w","x","y","z","th","sh","ch","gh"};
	static String[] syllables = new String[]{"kay","lee","rog","ger","er","han","dor","syd","ney","del","mol","bon","bonk","in","dord","bar",
		"ig","gang","lion","mag","ker","wenk","dreth","hab","gunt","sharp","cut","tang","hob","hag","kor","forth","pool","tenk","beg","pig","tug",
		"belly","might","nop","geg","illy","ben","worg","fat","gun","pod","er","ly","spat","tom","son","mat","bil","jim","mik","tod","sam","dana",
		"ali","sara","nani","cun","da","li","ni","atog","gol","hax","hod","mank","ton","bunt","ing","bree","mog","win","mad","ison","race","ris","lis",
		"rice","lace","bang","dor","mor","hor","wise","cam","mal","com","rey","nold","fry","wash","burn","sum","mers","buf","fy","finn","lin","ton","ash",
		"gon","bat","nil","har","tim","tin","jon","son","wil","mor","gan","red","blu","sham","forth","mold","gee","wick","stern","brad","pyre","rip","er","reg",
		"kit","nol","fres","yan","cris","fold","dal","val","inga","unga","onga","par","wil","myp","ald","heg","won","nir","ble","sni","shre","bren","hro","thre",
		"lig","amp","nro","arg","ops","ond","reb","yal","wri","fro","mo","no","ke","dilly","mot","weg","gle","rhi","sho","nock","keg","mock","led","tri","tre","mep",
		"tha","ma","gria","shto","ugs","unt","erd","bla","shin","dig","krunk","bag","sold","free","pod","weel","tod","ron","nick","olas","cycle","chael","ander","brot",
		"can","dol","maik","pod","fick","don","reen","tors","wilt","nock","brig","fold","meek","song","than","thon","rold","seel","bols","kred","fang","mult","rowd",
		"ship","lop","granst","yol","vol","yurn","yust","drig","drall","nox","tret","may","worn","jit","rosh","ram","wry","bull","sull","bos","son",
		"dates","xeri","mithri","poly","abad","grad","nox","ptero","saur","fant","atog","andro","integri","intra","macro","hypo","micro","omni","pater","orni","orth",
		"paleo","plasm","pseudo","pro","proto","semi","taxo","trans","ultra","uni","mono","hex","trip","tetra","oct","cide","logos","meter","ism","gamy","dactyl","cyst",
		"nomer","phase","phage","itis","phyte","stasis","stat","thes","vor","pod","some","hydra","morph","xera","dothro","tris","kon","ate","fil","nossa","xerad"
	};
	
	static String[] adjectives = new String[]{"Amazing","Terrifying","Unbroken","Silent","Hungry","Chosen","Immortal","Important","Furry",
		"Nice","Homely","Slow","Round","Sharp","Blunt","Unbowing","Fearful","Unchallenged","Glorious","Funny","Dangerous","Short","Creepy","Slimy",
		"Wet","Dry","Big","Small","Blurry","Old","New","Quick","Unfortunate","Fortunate","Curried","Brave","Stupid","Melancholy","Frightful","Lost",
		"Bawdy","Filthy","Evil","Bleak","Sorrowful","Devious","Unfathomable","Blighted","Terrifying","Horrifying","Horrific","Terrific","Spiteful","Lusty"};
	static String[] titles = new String[]{"Warrior","Wizard","Hoplite","Master","King","World Destroyer","World Eater","Queen","Regent","Assassin","Bishop",
		"Cleric","Killer","Baker","Butcher","Candlestick Maker","Peasant","Officer","Knight","Seer","Oracle","General","Tyrant","Fool","Idiot","Monster","Priest",
		"Nightmare King","Child Deciever","Critic","God","Painbringer","Daywalker"};

	static String[] lessgoodadjectives = new String[]{"spare","old","trusty","extra","beloved","useful","handy","sturdy","damaged","unbreakable","heirloom","basic",
	"fancy","tricky","fiddly","rusty","dirty","shiny","painted","prized",""};	
	static String[] townsuffixes = new String[]{"ton","town","ston","stone","wick","well","abad","shire","grad","cross"};	
	static Random rand = new Random();
	static String random(String[] strings){
		return (String) strings[new Random().nextInt(strings.length)];
	}
	
	static String syllable(){
		String ret = "";
		for(int i = rand.nextInt(2);i<rand.nextInt(5)+3;i++){
			if(i%2 == 0){
				ret += random(consonants);
			}else{
				ret += random(vowels);
			}
		}
		return ret;
	}
	
	
	
	public static String name(){
		String ret = "";
		int j = rand.nextInt(2)+1;
		for(int i = 0;i<j;i++){
			ret+= random(syllables);
			if(j == 1 && i == 0){
				j++;
			}
			
		}
		return ret.substring(0,1).toUpperCase()+ret.substring(1);
	}
	
	public static String townName(){
		return name()+townsuffixes[StorySpirit.random.nextInt(townsuffixes.length)];
	}

}
