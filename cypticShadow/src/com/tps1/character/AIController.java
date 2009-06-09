package com.tps1.character;

import com.jme.input.InputHandler;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;
import com.tps1.GameState.DefineGameState;
import com.tps1.GameState.gameSingleton;
import com.tps1.lvlLoader.CopyOfCopyOflevelTester;
import com.tps1.scene.SkyBoxManager.SkyBoxGameState;
import com.tps1.util.ogre;



@SuppressWarnings("serial")
public class AIController extends Controller{
characterMove move;Animationator animator;controlManager controls;
/**
 * @auther Kyle Williams
 * @description main controller class handles animations, movements and input
 * @param playerGameState player
 */
	public AIController(Charactertype player){
		
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
