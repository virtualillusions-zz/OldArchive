package com.vza.characters;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.binding.BindingListener;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.vza.util.State.Director;

/**
 * Class to manage, update and render multiple characters 
 * @author Kyle Williams
 *
 */
public class modelDirector implements Director,BindingListener{
	private AssetManager manager;
	//The root node of the scene graph
	private Node root;
	InputManager inputManager;
	/**
	 * Finds all characters stored in XML File in the Levels Directory
	 * @param inputManager 
	 */
	public modelDirector(Node rootNode,AssetManager manager,InputManager inputManager){
		manager.registerLocator("/Characters/CharacterData/",ClasspathLocator.class.getName(),
				"material","meshxml","skeletonxml","j3m","jpg","tga");	
		
		this.manager=manager;
		root = new Node("sceneNode");
		
		rootNode.attachChild(root);		
		
		//rootNode.attachChild(root);
		this.manager=manager;
		this.inputManager=inputManager;
		testBoxCharacter();
	}
	
	public void testBoxCharacter(){
		
		Material mat = new Material(manager, "plain_texture.j3md");
        TextureKey key = new TextureKey("TestStage.png", true);
        key.setGenerateMips(true);
        Texture tex = manager.loadTexture(key);
        tex.setMinFilter(Texture.MinFilter.Trilinear);
        mat.setTexture("m_ColorMap", tex);
		// Add a physics box to the world
        Box boxGeom=new Box(Vector3f.ZERO,10f,10f,10f);
        Geometry geom3=new Geometry("testBox",boxGeom);
        geom3.setMaterial(mat);
        
        root.attachChild(geom3);
        
        inputManager.registerKeyBinding("Forward", KeyInput.KEY_UP);
        inputManager.registerKeyBinding("Left", KeyInput.KEY_LEFT);
        inputManager.registerKeyBinding("Right", KeyInput.KEY_RIGHT);
        inputManager.registerKeyBinding("Backward", KeyInput.KEY_DOWN);
        //used with method onBinding in BindingListener interface
        //in order to add function to keys
        inputManager.addBindingListener(this);
	}
	
	public void onBinding(String binding, float value) {
		Spatial testBox = root.getChild("testBox");
        if (binding.equals("Forward")){
            testBox.move(1, 0, 0);            
        }else if (binding.equals("Left")){
        	testBox.move(0, 0,-1);
        }else if (binding.equals("Right")){
        	testBox.move(0, 0, 1);            
        }else if (binding.equals("Backward")){
        	testBox.move(-1, 0, 0);
        }
    }
	
	
	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {return "model Handler";}

	/**
	 * used to check if the state is active or inactive 
	 * @return is Active
	 */
	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Used to Directly Access the Render Manager
	 * @param rm
	 */
	public void render(RenderManager rm) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Used to set if the GameState is active or inactive
	 * @param active
	 */
	public void setActive(boolean active) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Gets called every successive frame
	 * @param tpf The Elapsed Time since the Last Frame
	 */
	public void update(float tpf) {
		
		
	}

	

}
