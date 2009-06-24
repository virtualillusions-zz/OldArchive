package com.tps1.character;


import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;

import com.tps1.GameState.DefineGameState;
import com.tps1.GameState.gameSingleton;
import com.tps1.lvlLoader.CopyOfCopyOflevelTester;
import com.tps1.scene.SkyBoxManager.SkyBoxGameState;
import com.tps1.util.setUp.Direction;
 

public class characterMove {

	private Node player;
	private float Speed,reSpeed,Reflex,MaxSpeed;
	public Direction prevDirec = Direction.FORWARD;
	private boolean isMoving=false;
	public characterMove(playerGameState player){
		this.player = player.getRootNode();
		MaxSpeed = (float) (player.getCharacterStates()[4]/10.0);
		Reflex = player.getCharacterStates()[3];
		Speed= (float) (MaxSpeed*(1/3.0));
		reSpeed=Speed;
	}
	 /**
     * set the Speed of the player
     * @param Speed the speed of the player
     */
	public void setSpeed(float theSpeed){
		Speed=theSpeed;
		reSpeed=Speed;
	}
	 /**
     * Makes the character Move in a given direction
     * @param theDirection call the enum Direction in order to move in a specific direction
     * @param accel is this character going to be accelerated or not
     */
	public void move(Direction theDirection,boolean accel) {
		isMoving=true;
		boolean isForward = theDirection.equals(Direction.FORWARD) ? true : false;
		player.getLocalTranslation().addLocal
		 (theDirection.getDirection(player).mult(accel(accel,isForward)));
		prevDirec = theDirection;
	} 
	/* 
	 * @param accel is this character going to be accelerated or not
	 * @param isForward is the character moving forward so MaxSpeed can be modified accordingly
	 * @return Speed the accelerated velocity of the character
	 */
	private float accel(boolean accel,boolean isForward){
		if(accel||isForward){
			float Velocity = (float) (isForward==true? MaxSpeed*(2/3.0):MaxSpeed);
			Speed += (gameSingleton.get().timer.getTimePerFrame()) * Velocity;
				if(Speed > Velocity) {	Speed = Velocity;       }
							}
		else Speed = reSpeed;
		return Speed;
	}
	/*@return Speed the decelerated velocity of the character */
	private float deccel(){		
			Speed -= (gameSingleton.get().timer.getTimePerFrame()) * reSpeed;
			if(Speed < 0) {Speed = 0;}	return Speed;
	}
	/*@modifier gradually slows down the player Node*/
	private void slowDown(){
		player.getLocalTranslation().addLocal(prevDirec.getDirection(player).mult(deccel()));	
	}
	/*@modifier if isMoving is false then begins to gradually slow down the player Node by a call to slowDown()*/
	public void decceleration(float time){
		if(!isMoving)slowDown();
		isMoving=false;
	}
 	
	public void jump() {
		// TODO Auto-generated method stub
	}
}
