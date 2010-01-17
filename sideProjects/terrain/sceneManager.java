package terrain;
 
import util.gameSingleton;

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
    if(terrainInstance==null){terrainInstance = new TerrainManager();terrainInstance.registerTerrains();}
    		return terrainInstance;
    }
    @Override
	public void cleanup() {	
    	getTerrainHandler().cleanup();
    	gameSingleton.get().getSceneHandlerNode().detachAllChildren();
    					}
}