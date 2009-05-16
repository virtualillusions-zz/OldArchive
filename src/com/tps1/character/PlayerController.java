package com.tps1.character;

import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.scene.Controller;
import com.jme.system.DisplaySystem;

import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;
import com.jmex.physics.PhysicsSpace;
import com.tps1.GameState.DefineGameState;
import com.tps1.GameState.gameSingleton;
import com.tps1.lvlLoader.CopyOfCopyOflevelTester;
import com.tps1.scene.SkyBoxManager.SkyBoxGameState;


@SuppressWarnings("serial")
public class PlayerController extends Controller{
InputHandler input = new InputHandler();
Animationator animator;controlManager controls;
	public PlayerController(playerGameState player){
		player.getRootNode().detachAllChildren();
		animator = new Animationator(new characterMove(player, input), player.getCharNode());
		controls = new controlManager(animator,input);
		player.getRootNode().updateGeometricState(0.0f, true);
	}

	@Override
	public void update(float time){animator.update(time);input.update(time);}	

}
