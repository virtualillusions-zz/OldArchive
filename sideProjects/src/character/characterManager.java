package character;

import java.util.HashMap;
import java.util.Map.Entry;

import terrain.sceneManager;
import util.gameSingleton;
import util.ogre;


import com.jme.math.Vector3f;
import com.jmex.game.state.BasicGameState;
import com.jmex.terrain.TerrainBlock;

public class characterManager extends BasicGameState{	
	//REMEBER TO IMPLIMENT TOMMOROW
	public enum characterFaction{Human,Zombie,Alien,Mutant;}	
	//the one and only instance of the characterManager
	private static characterManager instance = null;
	/**retrieves an instance of the characterManager*/
	public static characterManager get(){
		if(instance ==null){instance = new characterManager();}return instance;
	}
	//store character nodes
	private static  HashMap<String, characterNode> charList = new HashMap<String, characterNode>(); 
	/**returns stored cloned character Nodes*/
	public HashMap<String, characterNode> getcharList(){return charList;}
	  public characterManager() {
		super("CharacterManager");
        getRootNode().updateGeometricState(0, true);
        getRootNode().updateRenderState();
        gameSingleton.get().charHandlerNode().attachChild(getRootNode());
        //equivalent to attaching to gameStateManager
        gameSingleton.get().attachChild(this);	
        }

	/**
	 * Manages the individual character models
	 * @param instanceName the name the model was saved under
	 * @return the instance of the model
	 */
	public static characterNode get(String instanceName) {return charList.get(instanceName);}

	/**
	 * 
	 * @param modelURL the single word string to load the character ex: ninja, robot
	 * @param namedModel the name to save the model under within the heightmap
	 */
	public void load(String modelURL, String namedModel) {
		characterNode character = new characterNode(modelURL);	
		charList.put(namedModel, character);
		getRootNode().attachChild(charList.get(namedModel));
		getRootNode().updateGeometricState(0, true);
		getRootNode().updateRenderState();
	}
	
    @Override
    public void update(float tpf){
    	super.update(tpf);
    	for(Entry<String, characterNode> character:charList.entrySet())
    	{	character.getValue().update(tpf);  	}
    }

}
