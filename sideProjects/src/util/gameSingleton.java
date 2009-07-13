package util;

import java.util.logging.Logger;

import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;

import com.jme.renderer.Renderer;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jme.util.Timer;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateNode;

import com.jmex.game.state.GameStateManager;


public class gameSingleton extends GameStateNode<GameState>{
	public static final Logger getLogger = Logger.getLogger(gameSingleton.class.getName());
    public static InputHandler input;
    public static StandardGame game;
    private final Node  charHandlerNode = new Node("Character Handler"),
    			       SceneHandlerNode = new Node("Scene Handler"),
    			             MasterNode = new Node("MASTER~NODE");
	
		
	/*The one and only instance of gameSingleton*/
	private static gameSingleton instance = null;
      /**@return the main displaySystem for this game*/
	public final DisplaySystem getDisplay =DisplaySystem.getDisplaySystem();
	/**@return the main displaySystem for this game*/
	public final Renderer getRenderer =DisplaySystem.getDisplaySystem().getRenderer();
	/**@return the main Camera for this game*/
	public final CameraNode getCamNode =  new CameraNode("Cam-Node", getRenderer.getCamera());
	/**High resolution timer. remember that tpf is .getTimePerFrame();*/
    public final Timer timer=Timer.getTimer(); 
    /*A lightstate to turn on and off for the rootNode*/
	public final LightState getLightState = getRenderer.createLightState();
	
	public gameSingleton(StandardGame game) {
		super("Utilites-Manager");
		gameSingleton.game=game;
		//setupLight();		
        input = new FirstPersonHandler(getCamNode.getCamera(),8,1);
	//	rootNode.updateGeometricState( 0.0f, true );
      //  rootNode.updateRenderState();
       // rootNode.attachChild(MasterNode);
        MasterNode.attachChild(charHandlerNode);
        MasterNode.attachChild(SceneHandlerNode);
        GameStateManager.getInstance().attachChild(this);
        }
	/**
	 * Initializes GameState Manager
	 * @param game
	 * @return
	 */
     public static gameSingleton init(StandardGame game){
    	 if(instance==null)
     		instance = new gameSingleton(game);
    	 KeyBindingManager.getKeyBindingManager().add("exit", KeyInput.KEY_ESCAPE);
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
     } return instance;   }     
	 @Override
     public void update(float tpf){
    	 super.update(tpf);
    	 input.update(tpf);
    	 getCamNode.updateFromCamera();
    	 if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit")) {game.finish();}
     	 }
	 /**Used to organize all Characters loaded in a single Node*/
	 public Node charHandlerNode(){return charHandlerNode; }
	 /**Used to organize all Scene elements loaded in a single Node*/
	 public Node getSceneHandlerNode(){return SceneHandlerNode; }
	 public Node getMasterNode(){return MasterNode; }
}
