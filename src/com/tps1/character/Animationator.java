package com.tps1.character;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;
import com.jmex.model.ogrexml.anim.MeshAnimationController;
import com.tps1.GameState.DefineGameState;
import com.tps1.GameState.gameSingleton;
import com.tps1.lvlLoader.CopyOfCopyOflevelTester;
import com.tps1.scene.SkyBoxManager.SkyBoxGameState;

public class Animationator {
private characterMove movement;
private MeshAnimationController animControl;
private String name;
	public Animationator(characterMove characterMove, Node charNode) {
		movement = characterMove;
		name=charNode.getName();		
		animControl =  (MeshAnimationController)charNode.getController(0);
	}

	public void moveForward(String forward){
		String myForward = name+"Forward";
		if(forward!=null)myForward=forward;
		if(!animControl.getActiveAnimation().equals(myForward))
		{
		animControl.setAnimation(myForward);
		animControl.setTime(animControl.getAnimationLength(myForward)*FastMath.nextRandomFloat());
		}
	}
	public void moveBackward(String Backward){
		String myBackward = name+"Backward";
		if(Backward!=null)myBackward=Backward;
		if(!animControl.getActiveAnimation().equals(myBackward))
		{
		animControl.setAnimation(myBackward);
		animControl.setTime(animControl.getAnimationLength(myBackward)*FastMath.nextRandomFloat());
		}
	}
	public void strafeLeft(String Left){
		String myLeft = name+"Left";
		if(Left!=null)myLeft=Left;
		if(!animControl.getActiveAnimation().equals(myLeft))
		{
		animControl.setAnimation(myLeft);
		animControl.setTime(animControl.getAnimationLength(myLeft)*FastMath.nextRandomFloat());
		}
	}
	public void strafeRight(String Right){
		String myRight = name+"Right";
		if(Right!=null)myRight=Right;
		if(!animControl.getActiveAnimation().equals(myRight))
		{
		animControl.setAnimation(myRight);
		animControl.setTime(animControl.getAnimationLength(myRight)*FastMath.nextRandomFloat());
		}
	}
	public void Jump(String Jump){
		String myJump = name+"Jump";
		if(Jump!=null)myJump=Jump;
		if(!animControl.getActiveAnimation().equals(myJump))
			{
		animControl.setAnimation(myJump);
		animControl.setTime(animControl.getAnimationLength(myJump)*FastMath.nextRandomFloat());
        	}
	}
	
	private void idle(String Idle){
		String myIdle = name+"Idle";
		if(Idle!=null)myIdle=Idle;
		if(!animControl.getActiveAnimation().equals(myIdle))
		{
		animControl.setAnimation(myIdle);
		animControl.setTime(animControl.getAnimationLength(myIdle)*FastMath.nextRandomFloat());
		}
	}
	
	public MeshAnimationController getController(){	return animControl;	}
	public characterMove getMovements(){return movement;}
	public void update(float time) {
		movement.update(time);
		
		if(movement.getOffGround()){moveForward(null);}
		else if(movement.getRotationalAxis().getVelocity()>0.0)
		{	Vector3f direction = new Vector3f();
			movement.getRotationalAxis().getDirection(direction);
			     if(direction.getZ()>0)moveForward(null);
			else if(direction.getZ()<0)moveBackward(null);
			else if(direction.getX()>0)strafeLeft(null);
			else if(direction.getX()<0)strafeRight(null);
		}else idle(null);

	}

}
