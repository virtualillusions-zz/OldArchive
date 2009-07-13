/**
 * 
 */
package TestTerrain;

import java.util.HashMap;

import util.gameSingleton;

import com.jme.scene.Node;

/**
 * @author Kyle Williams
 *	Class for handling Terrains
 */
public class TerrainManager{
	 //store character nodes
	private  HashMap<String, sceneNode> terrainList = new HashMap<String, sceneNode>();  
	private Node Handler,baseNode,terrainNode;
	public TerrainManager() {
		Handler = gameSingleton.get().getSceneHandlerNode();
		baseNode=null;
		terrainNode=null;
		Handler.attachChild(terrainNode);
		Handler.attachChild(baseNode);

	}
	/*attaches the node to the terrainNode*/
	private void attachScene(sceneNode child){
		Handler.detachChild(terrainNode);
		terrainNode=child;
		Handler.attachChild(terrainNode);
        Handler.updateGeometricState(0, true);
        Handler.updateRenderState(); 
	}
	/*attaches the node to the baseNode*/
	private void attachBase(sceneNode child){
		Handler.detachChild(baseNode);
	  	baseNode=child;
        Handler.attachChild(baseNode);
        Handler.updateGeometricState(0, true);
        Handler.updateRenderState(); 		
	}
	/**@param name the name of the level to load 
	 * @param isTerrain is the model to be attached a base or a scene*/
	public void build(String name){
		if(!terrainList.containsKey(name)){
		gameSingleton.getLogger.entering(this.getClass().getName(), "build()");
		gameSingleton.getLogger.severe("NO TERRAIN WITH SUCH NAME REGISTERED");
		return;
		}
		if(name.contains("base:")){attachBase(terrainList.get(name));return;}
		attachScene(terrainList.get(name));		
	}
	/**registers a terrain with the terrainList please identify the base Node with base:
	 * @param scene the scene to register*/
	public void registerTerrain(sceneNode scene){terrainList.put(scene.getName(), scene);}
	  /**detaches the current terrainNode*/
	  public void detachTerrainNode() {
	        Handler.detachChild(terrainNode);
	        Handler.updateGeometricState(0, true);
	        Handler.updateRenderState(); 
	    }	
	  /**the TerrainManager's cleanup function*/
		public void cleanup() {	Handler.detachAllChildren(); Handler=null;}

}
