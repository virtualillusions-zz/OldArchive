package com.tps1.character;

import com.acarter.scenemonitor.SceneMonitor;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;
import com.jme.input.controls.GameControl;
import com.jme.input.controls.GameControlManager;
import com.jme.input.controls.binding.KeyboardBinding;
import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector3f;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;
import com.jmex.physics.contact.ContactInfo;
import com.tps1.GameState.DefineGameState;
import com.tps1.GameState.gameSingleton;
import com.tps1.character.characterMove.Direction;
import com.tps1.lvlLoader.CopyOfCopyOflevelTester;
import com.tps1.scene.SkyBoxManager.SkyBoxGameState;

public class controlManager {
private InputHandler theInput;
	public controlManager(final characterMove move, InputHandler input) {
		theInput = input;
		// Create our Controls
	    GameControlManager manager = new GameControlManager();
	    //Move Forward
	    GameControl forward = manager.addControl("Forward");
	    forward.addBinding(new KeyboardBinding(KeyInput.KEY_I));
	    //Move Backward
	    GameControl backward = manager.addControl("Backward");
	    backward.addBinding(new KeyboardBinding(KeyInput.KEY_K));
	    //Strafe Left
	    GameControl strafeLeft = manager.addControl("Strafe Left");
	    strafeLeft.addBinding(new KeyboardBinding(KeyInput.KEY_J));
	    //Strafe Right
	    GameControl strafeRight = manager.addControl("Strafe Right");
	    strafeRight.addBinding(new KeyboardBinding(KeyInput.KEY_L));
	    //Jump
	    GameControl jump = manager.addControl("Jump");
	    jump.addBinding(new KeyboardBinding(KeyInput.KEY_SPACE));
	    
	   

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
