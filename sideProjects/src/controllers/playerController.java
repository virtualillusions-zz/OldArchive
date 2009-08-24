package controllers;

import character.characterNode;

import com.jme.input.KeyInput;
import com.jme.input.controls.GameControl;
import com.jme.input.controls.GameControlManager;
import com.jme.input.controls.binding.KeyboardBinding;
import com.jme.input.controls.controller.ActionController;
import com.jme.input.controls.controller.ActionRepeatController;
import com.jme.input.controls.controller.GameControlAction;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import util.setUp.Direction;

@SuppressWarnings("serial")
public class playerController extends Controller{
controlManager controls;
/**
 * @auther Kyle Williams
 * @description main controller class handles animations, movements and input
 * @param playerGameState player
 */
	public playerController(characterNode player){controls = new controlManager(new characterMove(player));}
	@Override
	public void update(float time){controls.update(time);}	
	
class controlManager {
		private  GameControlManager manager;Animationator animator;characterMove thisMove;Node rootNode;
		/**
		 * @auther Kyle Williams
		 * @Desciption creates a manager and sets up input bindings
		 * @param characterMove move
		 * @param Node root
		 */
		
			public controlManager(characterMove move) 
			{
				thisMove=move;
				rootNode=move.getNode();
				animator = new Animationator(move.getNode());
				// Create our Controls
			    manager = new GameControlManager();
			    //Move Forward
			    manager.addControl("Forward").addBinding(new KeyboardBinding(KeyInput.KEY_I));
			    rootNode.addController(new ActionRepeatController(manager.getControl("Forward"),0,
			    		new Runnable() {@Override public void run(){thisMove.move(Direction.FORWARD,false);}}));

			    
			    //Move Backward
			   manager.addControl("Backward").addBinding(new KeyboardBinding(KeyInput.KEY_K));
			   rootNode.addController(new ActionRepeatController(manager.getControl("Backward"),0,
			    		new Runnable() {@Override public void run(){thisMove.move(Direction.BACKWARD,false);}}));
			    //Strafe Left
			    manager.addControl("Strafe Left").addBinding(new KeyboardBinding(KeyInput.KEY_J));
			    rootNode.addController(new ActionRepeatController(manager.getControl("Strafe Left"),0,
			    		new Runnable() {@Override public void run(){thisMove.move(Direction.LEFT,false);}}));
			    //Strafe Right
			    manager.addControl("Strafe Right").addBinding(new KeyboardBinding(KeyInput.KEY_L));
			    rootNode.addController(new ActionRepeatController(manager.getControl("Strafe Right"),0,
			    		new Runnable() {@Override public void run(){thisMove.move(Direction.RIGHT,false);}}));
			  //Jump
			    manager.addControl("Jump").addBinding(new KeyboardBinding(KeyInput.KEY_SPACE)); 
			}
			/**@return controlManager*/public GameControlManager get(){return manager;}
			/**
		 	 * Makes Node jump
		 	 * @param length of time in air
		 	 */
			public void jump(final float length, final float height){				
			    rootNode.addController(new ActionController(manager.getControl("Jump"),
			    		new GameControlAction() {
								public void pressed(GameControl control, float time) {thisMove.jump(length);	}
								public void released(GameControl control, float time) {}
												}			)
			    						);
			}
			/**@return characterMove*/public characterMove getcharacterMove(){return thisMove;}
			/**@return characterMove*/public Animationator getAnimationator(){return animator;}

			public void update(float time){animator.update(time);getcharacterMove().decceleration(time);}
		}
}
