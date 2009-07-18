package TestCharacter;

import com.jme.scene.Controller;
import com.jme.scene.Node;



@SuppressWarnings("serial")
public class AIController extends Controller{
characterMove move;Animationator animator;//controlManager controls;
/**
 * @auther Kyle Williams
 * @description main controller class handles animations, movements and input
 * @param playerGameState player
 */
	public AIController(Node player){
		
	//	player.getRootNode().detachAllChildren();
		//move = new characterMove(player);
		//animator = new Animationator(move, player.getCharNode());
		//controls = new controlManager(move, player.getCharNode());
	}
 
	@Override
	public void update(float time){
		//move.update(time);
		}	


}
