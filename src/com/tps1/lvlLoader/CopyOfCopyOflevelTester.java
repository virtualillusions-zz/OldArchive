package com.tps1.lvlLoader;

import com.jme.bounding.BoundingBox;
import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Box;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.BasicGameState;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.tps1.GameState.gameSingleton;

public class CopyOfCopyOflevelTester extends BasicGameState {
    InputHandler input;
    private Camera cam;
    private DisplaySystem display;
    public CopyOfCopyOflevelTester(int act, int scene){   
    	super("act:"+act+"-scene:"+scene);	
    	cam = gameSingleton.get().getCamNode.getCamera();
    	display = gameSingleton.get().getDisplay;
    	initLevel(gameSingleton.get().getPhysicsSpace());
        input = new FirstPersonHandler(DisplaySystem.getDisplaySystem().getRenderer().getCamera(),8,1);
    }
  
    protected void initLevel(PhysicsSpace base) {    
        
    	initSetup();
        
    	 // first we will create the floor
        // as the floor can't move we create a _static_ physics node
        StaticPhysicsNode staticNode = base.createStaticNode();

        // attach the node to the root node to have it updated each frame
        rootNode.attachChild( staticNode );

        // now we do not create a collision geometry but a visual box
        final Box visualFloorBox = new Box( "floor", new Vector3f(), 200, 0.25f, 200 );
        // note: we have used the constructor (name, center, xExtent, yExtent, zExtent)
        //       thus our box is centered at (0,0,0) and has size (10, 0.5f, 10)

        // we have to attach it to our node
        staticNode.attachChild( visualFloorBox );

        // now we let jME Physics 2 generate the collision geometry for our box
        staticNode.setModelBound(new BoundingBox());
        staticNode.updateModelBound();
        staticNode.generatePhysicsGeometry();
        rootNode.getLocalTranslation().set(0, -8f, 0);                 
        rootNode.updateGeometricState(0, true);
		rootNode.updateWorldBound();
		rootNode.updateRenderState();
        
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

