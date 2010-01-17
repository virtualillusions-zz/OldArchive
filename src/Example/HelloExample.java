package Example;


import com.jme.app.SimpleGame;

/**
 * Started Date: Aug 19, 2004<br><br>
 *
 * This program introduces jME's terrain utility classes and how they are used.  It
 * goes over ProceduralTextureGenerator, ImageBasedHeightMap, MidPointHeightMap, and
 * TerrainBlock.
 * 
 * @author Jack Lindamood
 */
public class HelloExample extends SimpleGame {
    
    public static void main(String[] args) {
        HelloExample app = new HelloExample();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    protected void simpleInitGame() {
        TerrainManager textureManager = new TerrainManager();
        // Finally a terrain loaded from a greyscale image with fancy textures on it.
        rootNode.attachChild(textureManager.getComplexTerrainBlock());
        // initialize the SkyboxManager
        // this needs to be called once, before you can get() the manager
        SkyBoxManager.init(cam);       
        rootNode.attachChild(SkyBoxManager.get().getCreatedSkyBox());
    }
    
    @Override
    protected void simpleUpdate() {
        //Move the skybox into position to the location of the camera so it moves with it
        SkyBoxManager.get().update();
    }
}