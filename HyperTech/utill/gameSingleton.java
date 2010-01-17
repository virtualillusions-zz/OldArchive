package utill;

import java.util.logging.Logger;


import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;

import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateNode;

import com.jmex.game.state.GameStateManager;
import com.jmex.simplephysics.CollisionScene;


public final class gameSingleton extends GameStateNode<GameState>{
	public static final Logger getLogger = Logger.getLogger(gameSingleton.class.getName());
	
    protected static StandardGame game;
    private static Node  MasterNode = new Node("MASTER~NODE");
    private CollisionScene collisionScene;
	/*The one and only instance of gameSingleton*/
	private static gameSingleton instance = null;
      /**
       * DisplaySystem = this
       * Renderer = this.getRenderer()
       * @return the main displaySystem for this game*/
	public final  DisplaySystem getDisplay =DisplaySystem.getDisplaySystem();
	public final  CameraNode getCamNode =  new CameraNode("Cam-Node", DisplaySystem.getDisplaySystem().getRenderer().getCamera());
	
    /*A lightstate to turn on and off for the rootNode*/
	public final  LightState getLightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
	 public static int getWorldScale = 2;
	
/*Temporary*/private final InputHandler input= new FirstPersonHandler(getCamNode.getCamera(),100,1);
	private gameSingleton(StandardGame game) {
		super("Utilites-Manager");
		this.game=game;
		
/*Temporary*/KeyBindingManager.getKeyBindingManager().add("exit", KeyInput.KEY_ESCAPE);
        GameStateManager.getInstance().attachChild(this);
        collisionScene = new CollisionScene();
        collisionScene.update(.001f);
        key = KeyInput.get();
        }
	private KeyInput key;
	/**
	 * Initializes GameState Manager
	 * @param game
	 * @return
	 */
     public static gameSingleton init(StandardGame game){
    	 if(instance==null)
     		instance = new gameSingleton(game);
    	
    	 
        return instance;
     }
     /**
      * returns the one and only instance of gameSingleton
      * @return instance(gameSingleton)
      */
     public static gameSingleton get(){
    	 if (instance == null) {
    		 // init has not been called yet.
    	 getLogger.entering(gameSingleton.class.getName(), "get()");
         getLogger.severe("ALERT, call init first!!");
    	 }
         	return instance;   
    }
    	      
	 @Override
     public void update(float tpf){
    	 super.update(tpf);    	 
/*Temporary*/if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit")) {game.finish();} input.update(tpf);
	 }
	public final CollisionScene getPhysics() {return collisionScene;	}
}
