package Example;

import com.jme.app.SimpleGame;
import com.jme.input.FirstPersonHandler;
import com.jme.math.FastMath;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.model.ModelFormatException;
import com.jmex.model.ogrexml.MaterialLoader;
import com.jmex.model.ogrexml.SceneLoader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExampleScenery extends SimpleGame {

    private static final Logger logger = Logger.getLogger(ExampleScenery.class.getName());
    
    private Node model;
    
    public static void main(String[] args){
        ExampleScenery app = new ExampleScenery();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }
    
    protected void loadMeshModel() throws ModelFormatException{
        SceneLoader loader = new SceneLoader();
        MaterialLoader matLoader = new MaterialLoader();
        
        try {
            URL matURL = ExampleScenery.class.getClassLoader()
            						.getResource("com/tps1/data/textures/exampleTerrain/terrain.material");
            URL meshURL = ExampleScenery.class.getClassLoader()
            						.getResource("com/tps1/data/textures/exampleTerrain/scene.scene");
            
            if (matURL != null){
                matLoader.load(matURL.openStream());
            //    if (matLoader.getMaterials().size() > 0)
              //      loader.setMaterials(matLoader.getMaterials());
            }
          if (meshURL != null){	loader.load(meshURL.openStream());}
          
          
            	 	            	
        } catch (IOException ex) {
            Logger.getLogger(ExampleScenery.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void simpleInitGame() {
        try {
        	// DISPLAYS ANOTHER ERROR IF I CHANGE THE URL TO "com/tps1/data/textures/exampleTerrain/"
            SimpleResourceLocator locator = new SimpleResourceLocator(ExampleScenery.class
                                                    .getClassLoader()
                                                    .getResource("com/radakan/jme/mxml/data/"));
            ResourceLocatorTool.addResourceLocator(
                    ResourceLocatorTool.TYPE_TEXTURE, locator);
            ResourceLocatorTool.addResourceLocator(
                    ResourceLocatorTool.TYPE_MODEL, locator);
        } catch (URISyntaxException e1) {
            logger.log(Level.WARNING, "unable to setup texture directory.", e1);
        }
        
        Logger.getLogger("com.jme.scene.state.lwjgl").setLevel(Level.SEVERE);
        
        DisplaySystem.getDisplaySystem().setTitle("Test Scene");
        display.getRenderer().setBackgroundColor(ColorRGBA.brown);
        ((FirstPersonHandler)input).getKeyboardLookHandler().setMoveSpeed(300);
        cam.setFrustumFar(20000f);
        try {
			loadMeshModel();
		} catch (ModelFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //MeshCloner.setVBO(model);
        
        // memory tracking code.
//            System.gc();
//            long memB4 = TestMeshLoading.getUsedMemory();
//            System.gc();
//            System.gc();
//            long memDiff = TestMeshLoading.getUsedMemory() - memB4;
//            System.gc();
//            System.out.println(TestMeshLoading.mem(memDiff));
     /*   
        int modelN = 0;
        for (int x = 0; x < 10; x++){
            for (int y = 0; y < 10; y++){
                Node clone = MeshCloner.cloneMesh(model);
                clone.setLocalTranslation(75 * x,  0,  75 * y);
                rootNode.attachChild(clone);
                
                if (clone.getControllerCount() > 0){
                    MeshAnimationController animControl = (MeshAnimationController) clone.getController(0);
                    animControl.setAnimation("Walk");
                  animControl.setTime(animControl.getAnimationLength("Walk") * FastMath.nextRandomFloat());
                    clone.addController(new MeshLodController((animControl)));
                }
                
                modelN++;
            }
        }
        
        System.out.println("Has "+modelN+" models..");
       */ 
        rootNode.updateGeometricState(0, true);
        rootNode.updateRenderState();
    }

    
    
}