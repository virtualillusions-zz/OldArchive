package TestCharacter;

import util.gameSingleton;
import util.ogre;
import TestTerrain.sceneManager;

import com.jme.bounding.BoundingBox;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.scene.state.ZBufferState;
import com.jmex.terrain.TerrainBlock;

public class characterNode extends Node{
	private Node characterNode;
	private AIController ai = null;
	private playerController player = null;
	public characterNode(String modelURL){
		characterNode = ogre.getCharacter(modelURL);
		this.setName(modelURL);
		characterNode.setName(modelURL=":mesh");
		characterStats = CharacterStats.get().name(modelURL);
		
		scale();
		
		
		this.getLocalTranslation().set((characterNode.getWorldBound().getCenter()).negate());
		this.getLocalTranslation().y+=((BoundingBox)characterNode.getWorldBound()).yExtent;
		this.updateGeometricState(0, true);
		characterNode.setLocalTranslation(this.getLocalTranslation());
		characterNode.updateGeometricState(0, true);
				
		this.attachChild(characterNode);			
		this.updateWorldBound();
		//this.setIsCollidable(true);
		this.updateGeometricState(0.0f, true);
		this.updateRenderState();
		
		setupLight();
		ai = new AIController(this);
		this.addController(ai);
	}

	public void setAsHuman() {
		if(this.getControllers().contains(player)){System.out.println("player controls already attached");return;}
			this.removeController(ai);
			ai=null;
			player = new playerController(this);
			this.addController(player);
	}
	public void setAsComp() {
		if(this.getControllers().contains(ai)){System.out.println("ai controls already attached");return;}
			this.removeController(player);
			player=null;
			ai = new AIController(this);
			this.addController(ai);
	}
	
	
	
	
	public void scale(){
		float N=CharacterStats.get().getWorldScale();
		characterNode.updateWorldBound();
		BoundingBox bb = (BoundingBox) characterNode.getWorldBound();	
		float wantedScale = Math.min(Math.min(N/bb.xExtent, N/bb.yExtent),N/bb.zExtent);		
		characterNode.setLocalScale(wantedScale);	
		characterNode.getWorldScale().set(wantedScale,wantedScale,wantedScale);
		characterNode.setModelBound(new BoundingBox()); 
		characterNode.updateModelBound();
		characterNode.updateWorldBound();
		characterNode.updateGeometricState(0, true);
		characterNode.updateRenderState();
	}
	public void update(float tpf){
   		//make sure that if the player left the level we don't crash. When we add collisions,
		characterMinHeight = sceneManager.Manager().getTerrainHandler().getTerrain().getHeight(
				this.getLocalTranslation());   
        if (!Float.isInfinite(characterMinHeight) && !Float.isNaN(characterMinHeight)) {        	 
        	this.getLocalTranslation().setY(characterMinHeight);        		
        }

        //get the normal of the terrain at our current location. We then apply it to the up vector of the player.
         sceneManager.Manager().getTerrainHandler().getTerrain().getSurfaceNormal(
        		this.getLocalTranslation(), normal);
        if(normal != null) {this.rotateUpTo(normal);}
	}
	
	private void setupLight(){
		ZBufferState buf = gameSingleton.get().getDisplay.getRenderer().createZBufferState();
        buf.setEnabled(true);
        buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        this.setRenderState(buf);   
        //Sets up lighting
        final PointLight light = new PointLight();
        light.setDiffuse(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.75f));
        light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        light.setLocation(new Vector3f(10, 180, 100));
        light.setEnabled(true);

        final LightState lightState = gameSingleton.get().getDisplay.getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.attach(light);
        this.setRenderState(lightState); 
	}
	private int[] characterStats;
    private float characterMinHeight;
	//store the normal of the terrain
	private Vector3f normal = new Vector3f();
	public float getCharacterMinHeight(){return  characterMinHeight;}
	public Node getCharNode(){return characterNode;}
	/**@see {@link CharacterStats#name(String value)}*/
	public int[] getCharacterStates(){return characterStats;}
}
