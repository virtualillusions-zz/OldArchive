package com.tps1.character;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;

import com.jme.input.controls.GameControl;
import com.jme.input.controls.GameControlManager;
import com.jme.input.controls.binding.KeyboardBinding;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;
import com.tps1.GameState.DefineGameState;
import com.tps1.GameState.gameSingleton;
import com.tps1.character.characterMove.Direction;
import com.tps1.lvlLoader.CopyOfCopyOflevelTester;
import com.tps1.scene.SkyBoxManager.SkyBoxGameState;

public class controlManager {
private InputHandler theInput;
private  GameControlManager manager;characterMove thisMove;
	public controlManager(characterMove move, InputHandler input) {
		theInput = input;
		thisMove=move;
		// Create our Controls
	    manager = new GameControlManager();
	    //Move Forward
	    manager.addControl("Forward").addBinding(new KeyboardBinding(KeyInput.KEY_I));
	    //Move Backward
	   manager.addControl("Backward").addBinding(new KeyboardBinding(KeyInput.KEY_K));
	    //Strafe Left
	    manager.addControl("Strafe Left").addBinding(new KeyboardBinding(KeyInput.KEY_J));
	    //Strafe Right
	    manager.addControl("Strafe Right").addBinding(new KeyboardBinding(KeyInput.KEY_L));
	    //Jump
	    manager.addControl("Jump").addBinding(new KeyboardBinding(KeyInput.KEY_SPACE)); 
	}

	private float value(String action) {
        return manager.getControl(action).getValue();
    }
	
	    public void update(float time) {
	        if (value("Forward") > 0) {thisMove.move(Direction.FORWARD); }	       
	        if (value("Backward") > 0) {thisMove.move(Direction.BACKWARD); }	       
	        if (value("Strafe Left") > 0) {thisMove.move(Direction.LEFT); }	       
	        if (value("Strafe Right") > 0) {thisMove.move(Direction.RIGHT); }	
	        
	    }	
	
	public static void main(String[] args) throws InterruptedException{
		 System.setProperty("jme.stats", "set");
		 StandardGame standardGame = new StandardGame("GameControl", StandardGame.GameType.GRAPHICAL, null);
	     standardGame.getSettings().setVerticalSync(false);
		 standardGame.start();     

	        try {
				SkyBoxGameState.Manager().setActive(true);
				
				 CopyOfCopyOflevelTester nex = new CopyOfCopyOflevelTester(0,0);
			 	   GameStateManager.getInstance().attachChild(nex);
			 	    nex.setActive(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			gameSingleton.get().stop=true;

			 Charactertype PLAYER1 = new Charactertype("robot");		
			 PLAYER1.setActive(true);	
						 
			gameSingleton.get().stop=false;

				     
		     final DefineGameState base = new DefineGameState(); 
		     base.setActive(true);	

		   }

}
