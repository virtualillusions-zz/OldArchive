package character;

import java.io.Serializable;

import terrain.sceneManager;
import util.gameSingleton;
import util.ogre;

import com.jme.bounding.BoundingBox;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.FogState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.ZBufferState;

import controllers.AIController;
import controllers.playerController;

public class characterNode extends Node implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Node charNode;
	private AIController ai = null;
	private playerController player = null;
	public characterNode(String modelURL){
		charNode = ogre.getCharacter(modelURL);
		this.setName(modelURL);
		charNode.setName(modelURL=":mesh");
		characterStats = gameSingleton.getStats().name(modelURL);
		
		scale();
		
		this.attachChild(charNode);	
		this.updateWorldBound();
		this.updateWorldVectors();
		setupLight();
		this.updateGeometricState(0.0f, true);
		this.updateRenderState();		
		
		ai = new AIController(this);
		this.addController(ai);
		this.setIsCollidable(true);
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
		float N=gameSingleton.getStats().getWorldScale();
		charNode.updateWorldBound();
		BoundingBox bb = (BoundingBox) charNode.getWorldBound();	
		float wantedScale = Math.min(Math.min(N/bb.xExtent, N/bb.yExtent),N/bb.zExtent);		
		charNode.setLocalScale(wantedScale);	
		charNode.getWorldScale().set(wantedScale,wantedScale,wantedScale);
		charNode.setModelBound(new BoundingBox()); 
		charNode.updateModelBound();
		charNode.updateWorldBound();
		charNode.updateGeometricState(0, true);
		charNode.updateRenderState();
	}
	public void update(float tpf){
   		//make sure that if the player left the level we don't crash. When we add collisions,
		characterMinHeight = sceneManager.Manager().getTerrainHandler().getTerrain().getHeight(
				this.getLocalTranslation());   
        if (!Float.isInfinite(characterMinHeight) && !Float.isNaN(characterMinHeight)) { 
        	if(this.getLocalTranslation().getY()>=characterMinHeight+.1f)
        		{this.getLocalTranslation().y-=gameSingleton.getStats().getFallRate(characterStats[5]);}
        	else this.getLocalTranslation().setY(characterMinHeight);        		
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
        
        FogState fs = gameSingleton.get().getRenderer.createFogState();
		fs.setDensity(.005f);	     
					
		fs.setColor(new ColorRGBA(.9411764706f,1.0f,.9450980392f,1.0f));

        fs.setStart(200);
        fs.setEnd(400);	       
        fs.setDensityFunction(FogState.DensityFunction.Linear);
        fs.setQuality(FogState.Quality.PerVertex);	   
        fs.setEnabled(true);
      // this.setRenderState(fs);
	}
	private int[] characterStats;
    private float characterMinHeight;
	//store the normal of the terrain
	private Vector3f normal = new Vector3f();
	public float getCharacterMinHeight(){return  characterMinHeight;}
	public Node getCharNode(){return charNode;}
	/**@see {@link CharacterStats#name(String value)}*/
	public int[] getCharacterStates(){return characterStats;}
}
