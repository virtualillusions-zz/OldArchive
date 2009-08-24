package character;

import java.util.HashMap;

 
public class CharacterStats {
	private static HashMap<String, int[]> statList = new HashMap<String, int[]>();  
	private static CharacterStats instance = null;
	private int Scale;
	/**
	 * A hashmap of stats for differnt characters
	 * @return instance of CharacterStats
	 */
	
	public CharacterStats(){
		statList.put( "Avenger",new int[]{3,3,2,1,1,3});
		statList.put( "Phantom",new int[]{1,2,1,2,3,2});
		statList.put("Sentinel",new int[]{2,1,3,3,2,1});
		statList.put( "DEFAULT",new int[]{1,1,1,1,1,2});
		Scale = 2;
	}
	/**
	 * Returns an array of character stats; stats are in this order
	 * 0:Health
	 * 1:Shield Strength
	 * 2:Accuracy
	 * 3:Reflex
	 * 4:Max Speed
	 * 5:Weight
	 * @param value the name of the character to find if its not found the character will have default stats
	 * @return an array of a characters base stats
	 */
	public int[] name(String value){
		return statList.containsKey(value)==true? statList.get(value):statList.get("DEFAULT");
	}
	
	public int getWorldScale(){return Scale;}
	public void setWorldScale(int newScale){Scale=newScale;}
	public float getFallRate(int weight){return weight/4.0f;		}
}
