package Example;

import java.util.logging.Logger;

import com.jme.image.Texture;
import com.jme.renderer.Camera;
import com.jme.scene.Skybox;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.DebugGameState;

/**
 * @author Kyle Williams
 * Manages The Skybox
 */
public class SkyBoxManager {
    private static Logger logger = Logger.getLogger(SkyBoxManager.class.getName());
    
    // the one and only instance of the skyBoxmanager
    private static SkyBoxManager instance = null;
    
    //A sky box for our scene
    private Skybox sb = null;
    // a reference to the camera, needed to update the position of the skybox
    protected Camera cam2;

    // init must be called once when the game starts
    // after that the SkyboxManager can be accessed from everywhere with
    // SkyBoxManager.get()
    public static void init(Camera cam1) {
        instance = new SkyBoxManager(cam1);
    }
    
    public static SkyBoxManager get() {
        if (instance == null) {
            // init has not been called yet.
            logger.severe("ALERT, call init first!!");
        }
        return instance;
    }
    
    // the constructor is private, it can only be called inside this class
    private SkyBoxManager(Camera cam1){
        cam2 = cam1;
        //Create a skybox to surround our world
        setupSky();
    }

    private void setupSky(){
        sb = new Skybox("skybox",200,200,200);

        try{
            ResourceLocatorTool.addResourceLocator(
                    ResourceLocatorTool.TYPE_TEXTURE, 
                    new SimpleResourceLocator(getClass().getResource("/jmetest/data/texture/")));
        }catch(Exception e){
            logger.warning("Unable to access texture directory.");
            e.printStackTrace();
        }

        sb.setTexture(Skybox.Face.North, TextureManager.loadTexture("north.jpg", 
                											Texture.MinificationFilter.BilinearNearestMipMap,
                													Texture.MagnificationFilter.Bilinear));
        sb.setTexture(Skybox.Face.West, TextureManager.loadTexture("west.jpg", 
                												Texture.MinificationFilter.BilinearNearestMipMap,
                													Texture.MagnificationFilter.Bilinear));
        sb.setTexture(Skybox.Face.South, TextureManager.loadTexture("south.jpg", 
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

    // move the skybox to the location of the camera
    // this method needs to me called in the games update Method
    // like: SkyBoxManager.get.update();
    protected void update (){
        //Move the skybox into position
        sb.getLocalTranslation().set(cam2.getLocation().x, cam2.getLocation().y, cam2.getLocation().z); 
    }

    // return the reference to the Skybox
    public Skybox getCreatedSkyBox() {
        return sb;
    }
}
    class SkyBoxGameState extends DebugGameState {
        public SkyBoxGameState() throws Exception {
            super();
             // initialize the SkyboxManager
            // this needs to be called once, before you can get() the manager
            SkyBoxManager.init(DisplaySystem.getDisplaySystem().getRenderer().getCamera());

            rootNode.attachChild(SkyBoxManager.get().getCreatedSkyBox());

            rootNode.updateGeometricState(0, true);
            rootNode.updateRenderState();
        }
      
        @Override
        public void update(float tpf) {
            //Move the skybox into position to the location of the camera so it moves with it
            SkyBoxManager.get().update();
            super.update(tpf);
        }
    }
