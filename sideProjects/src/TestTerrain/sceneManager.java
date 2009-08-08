package TestTerrain;
 
import java.util.Random;
import java.util.concurrent.Callable;

import util.gameSingleton;
import util.startLensFlare;

import com.jme.image.Texture;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.state.FogState;
import com.jme.scene.state.LightState;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.game.state.BasicGameState;
/**
 * @author Kyle Williams
 * @Description Division of code for managing scene related objects in the world
 * 
 */
public class sceneManager extends BasicGameState{
	    
	    // the one and only instance of the sceneManager
	    private static sceneManager instance = null;
	    /**@return the one and only isntance of sceneManager  */
	    public static sceneManager Manager() {
	        if (instance == null) {instance = new sceneManager();}
	        return instance;
	    }

	 private sceneManager() {
		super("sceneManager~SkyBox");
			
        getRootNode().attachChild(gameSingleton.get().getSceneHandlerNode());
        getRootNode().updateGeometricState(0, true);
        getRootNode().updateRenderState();
        //equivalent to attaching to gameStateManager
        gameSingleton.get().attachChild(this);
	}
     
    private TerrainManager terrainInstance = null;
    
    public TerrainManager getTerrainHandler(){
    	if(terrainInstance==null){terrainInstance = new TerrainManager();}
    		return terrainInstance;
    }
    @Override
	public void cleanup() {	
    	getTerrainHandler().cleanup();
    	gameSingleton.get().getSceneHandlerNode().detachAllChildren();
    					}
}