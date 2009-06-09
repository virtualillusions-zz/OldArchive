package com.tps1.lvlLoader;

import javax.swing.ImageIcon;

import jmetest.terrain.TestTerrain;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.game.state.BasicGameState;

import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.util.MidPointHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;
import com.tps1.GameState.gameSingleton;

public class CopyOfCopyOflevelTester extends BasicGameState {
    InputHandler input;
    private Camera cam;
    private DisplaySystem display;
    public CopyOfCopyOflevelTester(int act, int scene){   
    	super("act:"+act+"-scene:"+scene);	
    	cam = gameSingleton.get().getCamNode.getCamera();
    	display = gameSingleton.get().getDisplay;
    	initLevel();
        input = new FirstPersonHandler(DisplaySystem.getDisplaySystem().getRenderer().getCamera(),8,1);
        gameSingleton.get().setCurrentBlock(tb);
    }
    private TerrainBlock tb;
    protected void initLevel() {    
        
    	initSetup();
        
    	MidPointHeightMap heightMap = new MidPointHeightMap(64, 1f);
        // Scale the data
        Vector3f terrainScale = new Vector3f(4, 0.0575f, 4);
        // create a terrainblock
         tb = new TerrainBlock("Terrain", heightMap.getSize(), terrainScale,
                heightMap.getHeightMap(), new Vector3f(0, 0, 0));

        tb.setModelBound(new BoundingBox());
        tb.updateModelBound();

        // generate a terrain texture with 2 textures
        ProceduralTextureGenerator pt = new ProceduralTextureGenerator(
                heightMap);
        pt.addTexture(new ImageIcon(TestTerrain.class.getClassLoader()
                .getResource("jmetest/data/texture/grassb.png")), -128, 0, 128);
        pt.addTexture(new ImageIcon(TestTerrain.class.getClassLoader()
                .getResource("jmetest/data/texture/dirt.jpg")), 0, 128, 255);
        pt.addTexture(new ImageIcon(TestTerrain.class.getClassLoader()
                .getResource("jmetest/data/texture/highest.jpg")), 128, 255,
                384);
        pt.createTexture(32);
        
        // assign the texture to the terrain
        TextureState ts = display.getRenderer().createTextureState();
        Texture t1 = TextureManager.loadTexture(pt.getImageIcon().getImage(),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, true);
        ts.setTexture(t1, 0);

        tb.setRenderState(ts);
        tb.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
        
        rootNode.attachChild(tb);

        rootNode.updateGeometricState(0, true);
		rootNode.updateWorldBound();
		rootNode.updateRenderState();
		gameSingleton.get().setCurrentBlock(tb);
    }
    
    private void initSetup(){
    	display.getRenderer().getQueue().setTwoPassTransparency(false);
        cam.setFrame(new Vector3f(-14, 1.5f, -2.5f), new Vector3f(0, 0, 1), new Vector3f(0, 1, 0), new Vector3f(-1, 0, 0));
        cam.update();
        
        CullState cullState = display.getRenderer().createCullState();
        cullState.setCullFace(CullState.Face.Back);
       // rootNode.setRenderState(cullState);
    	/**
		ZBufferState buf = display.getRenderer().createZBufferState();
        buf.setEnabled(true);
        buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        rootNode.setRenderState(buf);    
        */
      //Sets up lighting
        final PointLight light = new PointLight();
        light.setDiffuse(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.75f));
        light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        light.setLocation(new Vector3f(100, 100, 100));
        light.setEnabled(true);

        final LightState lightState = display.getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.attach(light);
        rootNode.setRenderState(lightState); 
	}
    
    @Override
    public void update(float tpf) { // TODO Auto-generated method stub
    	super.update(tpf);
    	input.update(tpf);
    }

    
    @Override
	public final void cleanup(){	// TODO Auto-generated method stub
	}
}

