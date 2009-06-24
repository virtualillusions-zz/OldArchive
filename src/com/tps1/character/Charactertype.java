package com.tps1.character;

import com.jme.math.Vector3f;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;

import com.tps1.GameState.DefineGameState;
import com.tps1.GameState.gameSingleton;
import com.tps1.lvlLoader.CopyOfCopyOflevelTester;
import com.tps1.scene.SkyBoxManager.SkyBoxGameState;

public class Charactertype extends playerGameState { 	  	  
    //the one and only instance of the playerGameState
    private static Charactertype instance = null;

    /**
     * Loads and implements a character
     * @param theName the name of the model's mesh
     * @param isHuman is the model being created a user playable character or one controlled by AI
     * @return instance of this field
     */
    public static Charactertype build(String theName,boolean isHuman){
    	 if (isHuman)
    	 {     	  instance = new Charactertype(theName);
	           	  instance.getRootNode().addController(new PlayerController(instance));
	     } else {
	    	      instance = new Charactertype(theName);
	              instance.getRootNode().addController(new AIController(instance));
    	 		}
 	        return instance;
    }
    
    /**
     * Loads a static object to be placed in the world
     * @param object the mesh's url
     * @return instance of this field
     */
    public static Charactertype create(String object){
   	         	instance = new Charactertype(object);
	 	        return instance;
   }
    // the constructor is private, it can only be called inside this class
    public Charactertype(String theName){
    	  super(theName);
   	  // gameSingleton.get().timer.reset();
    } 
   
    // return the character name type
    public String getCharactertype() {
      return name;
    }
    
    public void setCharacterTranslation(Vector3f newLoc){
    	this.getRootNode().setLocalTranslation(newLoc);
    	this.getRootNode().updateWorldBound();
    }
}