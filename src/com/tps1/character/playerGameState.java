package com.tps1.character;

import com.jme.bounding.BoundingBox;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;

import com.jme.scene.state.LightState;
import com.jme.scene.state.ZBufferState;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameStateManager;

import com.tps1.GameState.gameSingleton;
import com.tps1.util.ogre;

 
/**
 * <code>playerGameState</code> 
 * provides an extremely basic gamestate 
 * that handles loading objects and setting them in 3D space
 *  
 * @author Kyle D. Williams
 */
public class playerGameState extends BasicGameState {	
	
   	private Node charNode,moveNode;	
	  private int[] characterStats;
	  private float characterMinHeight;
	private boolean isOffGround;

	/**
	 * Creates a new playerGameState with a given name.
	 * 
	 * @param name The name of this GameState.
	 */
	protected playerGameState(String theName) { 
		super(theName);
         //sets up lighting
		setupLight(rootNode);
	   	characterStats = CharacterStats.get().name(theName);
		init(theName);
	 	GameStateManager.getInstance().attachChild(this);
    	} 

	//Initalizes system
	private void init(String baseNodeName) {				        
		//Loads Inital Model//////////////////////////////////
	    charNode = ogre.getCharacter(baseNodeName);			
		
		//reduction in based on scale
		scale(charNode);

		rootNode.getLocalTranslation().set((charNode.getWorldBound().getCenter()).negate());
		rootNode.getLocalTranslation().y+=((BoundingBox)charNode.getWorldBound()).yExtent;
		rootNode.updateGeometricState(0, true);
		charNode.setLocalTranslation(rootNode.getLocalTranslation());
		charNode.updateGeometricState(0, true);
		
		moveNode= new Node(baseNodeName+" move");
		moveNode.attachChild(charNode);
		moveNode.updateWorldBound();
		rootNode.attachChild(moveNode);			
		rootNode.updateWorldBound();
	    rootNode.updateGeometricState(0.0f, true);
	    rootNode.updateRenderState();
	    rootNode.setIsCollidable(true);
	   // rootNode.hasCollision(scene, true);	   
	}	
	
	private void scale(Node theNode){
		float N=CharacterStats.get().getWorldScale();
		theNode.updateWorldBound();
		BoundingBox bb = (BoundingBox) theNode.getWorldBound();	
		float wantedScale = Math.min(Math.min(N/bb.xExtent, N/bb.yExtent),N/bb.zExtent);		
		theNode.setLocalScale(wantedScale);	
		theNode.getWorldScale().set(wantedScale,wantedScale,wantedScale);
		theNode.setModelBound(new BoundingBox()); 
		theNode.updateModelBound();
		theNode.updateGeometricState(0, true);
		theNode.updateWorldBound();
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
        light.setLocation(new Vector3f(10, 180, 100));
        light.setEnabled(true);

        final LightState lightState = gameSingleton.get().getDisplay.getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.attach(light);
        rootNode.setRenderState(lightState); 
	}
	
	public void cleanup() {	/* TODO Auto-generated method stub*/}	
	public Node getCharNode(){return charNode;}
	public Node getMoveNode(){return moveNode;}
	/**@see {@link CharacterStats#name(String value)}*/
	public int[] getCharacterStates(){return characterStats;}
	public float getCharacterMinHeight(){return  characterMinHeight;}
	public boolean getOffGround(){return  isOffGround;}

	@Override
	public void update(float tpf){
		super.update(tpf);
		isOffGround=false;
		//make sure that if the player left the level we don't crash. When we add collisions,
        //the fence will do its job and keep the player inside.
           characterMinHeight = gameSingleton.get().getCurrentBlock().getHeight(getRootNode()
        		.getLocalTranslation());   
        if (!Float.isInfinite(characterMinHeight) && !Float.isNaN(characterMinHeight)) {        	 
        	 getRootNode().getLocalTranslation().setY(characterMinHeight);        		
        }
		   
   //get the normal of the terrain at our current location. We then apply it to the up vector of the player.
        gameSingleton.get().getCurrentBlock().getSurfaceNormal(getRootNode().getLocalTranslation(), normal);
        if(normal != null) {
        	getRootNode().rotateUpTo(normal);
        }	
	}
	//store the normal of the terrain
	private Vector3f normal = new Vector3f();
}

