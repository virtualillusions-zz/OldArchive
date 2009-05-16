package com.tps1.character;

import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.scene.Controller;
import com.jme.system.DisplaySystem;

import com.jmex.physics.PhysicsSpace;


@SuppressWarnings("serial")
public class PlayerController extends Controller{
InputHandler input = new InputHandler();
Animationator animator;
	public PlayerController(playerGameState player){
		player.getRootNode().detachAllChildren();
		animator = new Animationator(new characterMove(player, input), player.getCharNode());
		new FirstPersonHandler(DisplaySystem.getDisplaySystem().getRenderer().getCamera(),8,1);
		//((FirstPersonHandler)handler).getKeyboardLookHandler().setMoveSpeed(10);
		player.getRootNode().updateGeometricState(0.0f, true);
	}

	@Override
	public void update(float time){animator.update(time);/* TODO Set-up Key Handeling*/}	

}
