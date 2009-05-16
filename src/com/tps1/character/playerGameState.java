package com.tps1.character;

import java.util.HashMap;
import java.util.logging.Logger;

import com.jme.bounding.BoundingBox;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.scene.state.ZBufferState;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.physics.DynamicPhysicsNode;

import com.tps1.GameState.gameSingleton;
import com.tps1.util.ogre;


/**
 * <code>playerGameState</code> provides an extremely basic gamestate 
 * that handles motion and player controls
 *  
 * @author Kyle D. Williams
 */
public class playerGameState extends BasicGameState {	
	
    private static final Logger logger = Logger.getLogger(playerGameState.class
            .getName());
	private Node charNode, baseNode;	
	//store character nodes
	private HashMap<String, ogre> charList = new HashMap<String, ogre>(); 

	/**
	 * Creates a new playerGameState with a given name.
	 * 
	 * @param name The name of this GameState.
	 */
	public playerGameState(String theName) { 
		super(theName);
	//remember camera is initialized in PlayerController also remember to attach it to shoulder 
         //sets up lighting
		setupLight(rootNode);
		init(theName);
	 	GameStateManager.getInstance().attachChild(this);
    	}
	
	//Initalizes system
	private void init(String baseNodeName) {		
			        
		//Loads Inital Model//////////////////////////////////
	    charNode = getCharacter(baseNodeName);	     

	    //reduction in based on scale
		charNode.updateGeometricState(0.0f, true);
		float N=.04f;
		charNode.updateWorldBound();
		BoundingBox bb = (BoundingBox) charNode.getWorldBound();
		logger.info("Boudning Box Value: "+bb);
		float wantedScale = Math.min(N/charNode.getLocalScale().x, N/charNode.getLocalScale().y);
		logger.info("wantedScale created: "+wantedScale+ " Character size:" + charNode.getLocalScale());
		wantedScale = Math.min(N/charNode.getLocalScale().z, wantedScale);
		logger.info("wantedScale created: "+wantedScale+ " Character size:" + charNode.getLocalScale());
		charNode.setLocalScale(wantedScale);	
		logger.info("wantedScale created: "+wantedScale+ " Character size:" + charNode.getLocalScale() +"charter World Boundings"+(BoundingBox) charNode.getWorldBound());
		//////Sets up model to be deployed in world////////////
		charNode.setModelBound(new BoundingBox()); 	  
		charNode.updateModelBound(); charNode.updateWorldBound();

	   rootNode.attachChild(charNode);
	   rootNode.updateRenderState();
	   rootNode.updateGeometricState(0.0f, true);
   	   rootNode.addController(new PlayerController(this));

	}	
	
	/**
	 * returns character geometry from generated from ogre class
	 * @return ogreNode
	 */
	private Node getCharacter(String theName)
	{
		if(!charList.containsKey(theName))
			charList.put(theName, new ogre(theName));			
		return ((ogre)charList.get(theName)).newClone();
	}
		
	private void setupLight(Node rootNode){
		ZBufferState buf = gameSingleton.get().getDisplay.getRenderer().createZBufferState();
        buf.setEnabled(true);
        buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        rootNode.setRenderState(buf);   
        //Sets up lighting
        final PointLight light = new PointLight();
        light.setDiffuse(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.75f));
        light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        light.setLocation(new Vector3f(100, 100, 100));
        light.setEnabled(true);

        final LightState lightState = gameSingleton.get().getDisplay.getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.attach(light);
        rootNode.setRenderState(lightState); 
	}
	public void cleanup() {	/* TODO Auto-generated method stub*/	}	
	public Node getCharNode(){return charNode;}

}

