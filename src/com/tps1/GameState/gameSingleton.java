package com.tps1.GameState;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.jme.scene.CameraNode;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueue;
import com.jme.util.Timer;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.terrain.TerrainBlock;
import com.tps1.aMain.Game;

public class gameSingleton extends BasicGameState{

   //the one and only instance of the gameSingleton*/
	private static gameSingleton instance = null;	
	
    /**@return the main displaySystem for this game*/
	public final DisplaySystem getDisplay =DisplaySystem.getDisplaySystem(Game.getSettings().getRenderer());
	/**@return the main Camera for this game*/
	public final CameraNode getCamNode =  new CameraNode("cam node", getDisplay.getRenderer().getCamera());
	/**High resolution timer. remember that tpf is .getTimePerFrame();*/
    public Timer timer=Timer.getTimer();   
	
	private gameSingleton() {
		super("gameHandler");
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

	 TerrainBlock tb;
	 public void setCurrentBlock(TerrainBlock tb){
		 if (tb!=null)
			 this.tb = tb;
	 }
	 public TerrainBlock getCurrentBlock(){
		 return tb;
	 }
 
	
	 
    /**drawing and calculating the first frame usually takes longer than the rest to 
     * avoid a rushing simulation we reset the timer */
	boolean firstFrame = true; 	
	public void update(float tpf) {
		if(firstFrame){timer.reset();firstFrame=false;}
	}
	
	 public void update(Runnable action) {
	        
	    }
	 
	@Override public void cleanup() {/* TODO Auto-generated method stub*/}
	@Override 	public void render(float tpf) {/* TODO Auto-generated method stub*/}

}
