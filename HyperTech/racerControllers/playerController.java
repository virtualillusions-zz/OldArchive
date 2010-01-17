package racerControllers;

import com.jme.input.KeyInput;
import com.jme.input.controls.GameControl;
import com.jme.input.controls.GameControlManager;
import com.jme.input.controls.binding.KeyboardBinding;
import com.jme.input.controls.controller.ActionController;
import com.jme.input.controls.controller.ActionRepeatController;
import com.jme.input.controls.controller.GameControlAction;
import com.jme.scene.Controller;
import com.jme.scene.Node;

@SuppressWarnings("serial")
public class playerController extends Controller{
controlManager controls;
/**
 * @auther Kyle Williams
 * @description main controller class handles animations, movements and input
 * @param playerGameState player
 */
	public playerController(Node player){controls = new controlManager(player);}
	@Override
	public void update(float time){controls.update(time);}	
	
class controlManager {
		private GameControlManager manager; 
		private MoveController movement;
		private Node rootNode;
		/**
		 * @auther Kyle Williams
		 * @Desciption creates a manager and sets up input bindings
		 * @param characterMove move
		 * @param Node root
		 */
		
			public controlManager(Node move) 
			{
				
				//animator = new Animationator(move.getNode());
				// Create our Controls
			    manager = new GameControlManager();
			    //Move Forward
			    manager.addControl("Forward").addBinding(new KeyboardBinding(KeyInput.KEY_I));
			    rootNode.addController(new ActionRepeatController(manager.getControl("Forward"),0,
			    		new Runnable() {@Override public void run(){		}}));

			    
			    //Move Backward
			   manager.addControl("Backward").addBinding(new KeyboardBinding(KeyInput.KEY_K));
			   rootNode.addController(new ActionRepeatController(manager.getControl("Backward"),0,
			    		new Runnable() {@Override public void run(){		}}));
			    //Strafe Left
			    manager.addControl("Strafe Left").addBinding(new KeyboardBinding(KeyInput.KEY_J));
			    rootNode.addController(new ActionRepeatController(manager.getControl("Strafe Left"),0,
			    		new Runnable() {@Override public void run(){		}}));
			    //Strafe Right
			    manager.addControl("Strafe Right").addBinding(new KeyboardBinding(KeyInput.KEY_L));
			    rootNode.addController(new ActionRepeatController(manager.getControl("Strafe Right"),0,
			    		new Runnable() {@Override public void run(){		}}));
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
								public void pressed(GameControl control, float time) {		}
								public void released(GameControl control, float time) {}
												}			)
			    						);
			}
		public void update(float time){ }
		}
}
