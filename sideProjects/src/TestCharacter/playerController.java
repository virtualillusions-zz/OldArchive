package TestCharacter;

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
characterMove move;Animationator animator;controlManager controls;
/**
 * @auther Kyle Williams
 * @description main controller class handles animations, movements and input
 * @param playerGameState player
 */
	public playerController(Node player){		
	///	    move = new characterMove(player);
	///	animator = new Animationator(player,move);
	///	controls = new controlManager(move, player.getCharNode());
	}

	@Override
	public void update(float time){
	//	animator.update(time);
		move.decceleration(time);
		}	
	
class controlManager {
		private  GameControlManager manager;characterMove thisMove;Node rootNode;
		/**
		 * @auther Kyle Williams
		 * @Desciption creates a manager and sets up input bindings
		 * @param characterMove move
		 * @param Node root
		 */
		/**
			public controlManager(characterMove move, Node root) {
				thisMove=move;
				rootNode=root;
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
			    rootNode.addController(new ActionController(manager.getControl("Jump"),new GameControlAction() {
					public void pressed(GameControl control, float time) {thisMove.jump();	}
					public void released(GameControl control, float time) {}}));
			}*/
							}
}
