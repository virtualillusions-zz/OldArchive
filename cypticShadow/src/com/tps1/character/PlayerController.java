package com.tps1.character;

import com.jme.input.InputHandler;
import com.jme.scene.Controller;



@SuppressWarnings("serial")
public class PlayerController extends Controller{
InputHandler input = new InputHandler();
characterMove move;Animationator animator;controlManager controls;
	public PlayerController(playerGameState player){
		player.getRootNode().detachAllChildren();
		move = new characterMove(player, input);
		animator = new Animationator(move, player.getCharNode());
		controls = new controlManager(move,input);
		player.getRootNode().updateGeometricState(0.0f, true);
	}

	@Override
	public void update(float time){
		move.update(time);
		animator.update(time);
		input.update(time);
		}	

}
