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
import com.jme.scene.state.LightState;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.game.state.BasicGameState;
/**
 * SkyBoxManager
 * @author Kyle Williams
 * @Description Division of code for creation of skybox and handling of terrain
 * SkyBox created on initiation
 */
public class sceneManager extends BasicGameState{
	    
	    // the one and only instance of the skyBoxmanager
	    private static sceneManager instance = null;
	    
	    //A sky box for our scene
	    private Skybox sb = null;
	    // a reference to the camera, needed to update the position of the skybox
	    private Camera cam=gameSingleton.get().getCamNode.getCamera();;
	
	  /**creates an instance of the skyBoxManager*/
	    public static sceneManager Manager() {
	        if (instance == null) {impliment();}
	        return instance;
	    }
	 //creates a skybox
	public sceneManager() {
		super("sceneManager~SkyBox");
			//Create a skybox to surround our world
        setupSky();
        //gameSingleton.get().setupLight(getRootNode());		
        gameSingleton.get().getSceneHandlerNode().attachChild(getCreatedSkyBox());
        new startLensFlare(getCreatedSkyBox());
        getRootNode().attachChild(gameSingleton.get().getSceneHandlerNode());
        getRootNode().updateGeometricState(0, true);
        getRootNode().updateRenderState();
        //equivalent to attaching to gameStateManager
        gameSingleton.get().attachChild(this);
	}
    //creates the isntance inside the openGL update Queue
	private static void impliment(){
		try {
			GameTaskQueueManager.getManager().update(new Callable<Object>() {
			    public Object call() throws Exception {
			    	//Initiate Skybox 
			    	instance = new sceneManager();			  

			    	return null;						} 					}			
													).get();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	/**Randomizes the skyBox Texture*/
	public void Randomize(){setupSky();}
	
	private void setupSky(){
        sb = new Skybox("skybox",200,200,200);
          
        //remember to number the skybox folders 1 2 3 etc...
        String[] skyStyles = { "Example 1","Example 2","Example 3","Example 4"};
        String sky = skyStyles[new Random().nextInt(skyStyles.length)];
               
        try{
            ResourceLocatorTool.addResourceLocator(
                    ResourceLocatorTool.TYPE_TEXTURE, 
                    new SimpleResourceLocator(getClass().getResource("/data/SkyBox/"+sky+"/")));
        }catch(Exception e){
        	gameSingleton.getLogger.entering(sceneManager.class.getName(), "setupSky()");
        	gameSingleton.getLogger.warning("Unable to access texture directory.");
            e.printStackTrace();
        }

        sb.setTexture(Skybox.Face.North, TextureManager.loadTexture("north.jpg", 
                											Texture.MinificationFilter.BilinearNearestMipMap,
                													Texture.MagnificationFilter.Bilinear));
        sb.setTexture(Skybox.Face.South, TextureManager.loadTexture("south.jpg", 
															Texture.MinificationFilter.BilinearNearestMipMap,
																	Texture.MagnificationFilter.Bilinear));
        sb.setTexture(Skybox.Face.West, TextureManager.loadTexture("west.jpg", 
                												Texture.MinificationFilter.BilinearNearestMipMap,
                													Texture.MagnificationFilter.Bilinear));       
        sb.setTexture(Skybox.Face.East, TextureManager.loadTexture("east.jpg", 
        													Texture.MinificationFilter.BilinearNearestMipMap,
        														Texture.MagnificationFilter.Bilinear));
        sb.setTexture(Skybox.Face.Up, TextureManager.loadTexture("top.jpg", 
        													Texture.MinificationFilter.BilinearNearestMipMap,
        														Texture.MagnificationFilter.Bilinear));
        sb.setTexture(Skybox.Face.Down, TextureManager.loadTexture("bottom.jpg", 
        													Texture.MinificationFilter.BilinearNearestMipMap,
        														Texture.MagnificationFilter.Bilinear));
        sb.preloadTextures();
    }
	// return the reference to the Skybox
    public Skybox getCreatedSkyBox() {  return sb;  }
    
    @Override
    public void update(float tpf){
    	super.update(tpf);
    	//Move the skybox into position
    	getCreatedSkyBox().getLocalTranslation().set(cam.getLocation().x, cam.getLocation().y, cam.getLocation().z); 
    }
    
    private TerrainManager terrainInstance = null;
    
    public TerrainManager getTerrainHandler(){
    	if(terrainInstance==null){terrainInstance = new TerrainManager();}
    		return terrainInstance;
    }
    
    
	public void setupLight(Node rootNode){
		 // ---- LIGHTS
        /** Set up a basic, default light. */
        PointLight light = new PointLight();
        light.setDiffuse( new ColorRGBA( 0.75f, 0.75f, 0.75f, 0.75f ) );
        light.setAmbient( new ColorRGBA( 0.5f, 0.5f, 0.5f, 1.0f ) );
        light.setLocation( new Vector3f( 100, 100, 100 ) );
        light.setEnabled( true );
        LightState lightState = gameSingleton.get().getLightState;
        /** Attach the light to a lightState and the lightState to rootNode. */
        lightState.setEnabled( true );
        lightState.attach( light );
        lightState.setTwoSidedLighting(true);
        rootNode.setRenderState( lightState );
	}
    @Override
	public void cleanup() {	
    	getTerrainHandler().cleanup();
    	gameSingleton.get().getSceneHandlerNode().detachAllChildren();
    	sb.detachAllChildren();	
    					}
}
    




