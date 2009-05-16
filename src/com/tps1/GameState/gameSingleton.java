package com.tps1.GameState;

import com.jme.scene.CameraNode;
import com.jme.system.DisplaySystem;
import com.jme.util.Timer;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.physics.PhysicsSpace;
import com.tps1.aMain.Game;

public class gameSingleton extends BasicGameState{

   //the one and only instance of the gameSingleton*/
	private static gameSingleton instance = null;	
    //the one and only instance of the PhysicsSpace within the game*/
	private PhysicsSpace physics;
	//the Speed of the physics engine
    private float physicsSpeed = 2;
    /**@return the main displaySystem for this game*/
	public final DisplaySystem getDisplay =DisplaySystem.getDisplaySystem(Game.getSettings().getRenderer());
	/**@return the main Camera for this game*/
	public final CameraNode getCamNode =  new CameraNode("cam node", getDisplay.getRenderer().getCamera());
	/**High resolution timer. remember that tpf is .getTimePerFrame();*/
    public Timer timer=Timer.getTimer();   
	
	private gameSingleton() {
		super("gameHandler");
		physics = PhysicsSpace.create();
		GameStateManager.getInstance().attachChild(this);
		this.setActive(true);	
	}
	
	 /**
	 * @return the one and only instance of this game
	 */
	 public static gameSingleton get() {
	        if (instance == null) {
	        	instance = new gameSingleton();
	        }
	        return instance;
	    }
	
	
	 /**
	 * @return the physics space for this game
	 */
	 public PhysicsSpace getPhysicsSpace() {
		return physics;
	}

	/**
	* The multiplier for the physics time. Default speed for game is 2. 0 means no physics processing.
	* @param physicsSpeed new speed
	*/
	public void setPhysicsSpeed( float physicsSpeed ) {
	       this.physicsSpeed = physicsSpeed;
	}
    /**drawing and calculating the first frame usually takes longer than the rest to 
     * avoid a rushing simulation we reset the timer */
	boolean firstFrame = true; 	
	public void update(float tpf) {
		if(firstFrame){timer.reset();firstFrame=false;}
		if(!stop)physics.update(tpf*physicsSpeed);
	}
    public boolean stop;
	@Override public void cleanup() {/* TODO Auto-generated method stub*/}
	@Override 	public void render(float tpf) {/* TODO Auto-generated method stub*/}

}
