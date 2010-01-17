package Example;

import com.jme.system.DisplaySystem;
import com.jmex.game.state.DebugGameState;

class MyGameState extends DebugGameState {
    public MyGameState() throws Exception {
        super();
        TerrainManager manager = new TerrainManager();
        // First a hand made terrain
//        rootNode.attachChild(manager.getHomegrownTerrainBlock());
        // Next an automatically generated terrain with a texture
//        rootNode.attachChild(manager.getHeightmapterrainblock());
        // Finally a terrain loaded from a greyscale image with fancy textures on it.
        rootNode.attachChild(manager.getComplexTerrainBlock());
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