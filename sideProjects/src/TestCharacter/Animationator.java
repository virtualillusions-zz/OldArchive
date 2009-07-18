package TestCharacter;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;

import com.jmex.model.ogrexml.anim.MeshAnimationController;


public class Animationator {
private MeshAnimationController animControl;
private String name;
private characterMove move;
//private Charactertype player;
	public Animationator(Node player, characterMove move) {
		//name=player.getCharNode().getName();
		this.move = move;
		//animControl =  (MeshAnimationController)player.getCharNode().getController(0);
	//	this.player = player;
	} 
/*
	public void moveForward(String forward){
		String myForward = name+"Forward";
		if(forward!=null)myForward=forward;
		if(!animControl.getActiveAnimation().equals(myForward))
		{
		animControl.setAnimation(myForward);
		animControl.setCurTime(animControl.getAnimationLength(myForward)*FastMath.nextRandomFloat());
		}
	}
	public void moveBackward(String Backward){
		String myBackward = name+"Backward";
		if(Backward!=null)myBackward=Backward;
		if(!animControl.getActiveAnimation().equals(myBackward))
		{
		animControl.setAnimation(myBackward);
		animControl.setCurTime(animControl.getAnimationLength(myBackward)*FastMath.nextRandomFloat());
		}
	}
	public void strafeLeft(String Left){
		String myLeft = name+"Left";
		if(Left!=null)myLeft=Left;
		if(!animControl.getActiveAnimation().equals(myLeft))
		{
		animControl.setAnimation(myLeft);
		animControl.setCurTime(animControl.getAnimationLength(myLeft)*FastMath.nextRandomFloat());
		}
	}
	public void strafeRight(String Right){
		String myRight = name+"Right";
		if(Right!=null)myRight=Right;
		if(!animControl.getActiveAnimation().equals(myRight))
		{
		animControl.setAnimation(myRight);
		animControl.setCurTime(animControl.getAnimationLength(myRight)*FastMath.nextRandomFloat());
		}
	}
	public void Jump(String Jump){
		String myJump = name+"Jump";
		if(Jump!=null)myJump=Jump;
		if(!animControl.getActiveAnimation().equals(myJump))
			{
		animControl.setAnimation(myJump);
		animControl.setCurTime(animControl.getAnimationLength(myJump)*FastMath.nextRandomFloat());
        	}
	}
	
	private void idle(String Idle){
		String myIdle = name+"Idle";
		if(Idle!=null)myIdle=Idle;
		if(!animControl.getActiveAnimation().equals(myIdle))
		{
		animControl.setAnimation(myIdle);
		animControl.setCurTime(animControl.getAnimationLength(myIdle)*FastMath.nextRandomFloat());
		}
	}
	
	public MeshAnimationController getController(){	return animControl;	}
	public void update(float time) {
		
		if(player.getOffGround()==true){moveForward(null);}
		else if(!location.equals(player.getRootNode().getLocalTranslation()))
		{	
			     if(move.prevDirec.equals(Direction.FORWARD))moveForward(null);
			else if(move.prevDirec.equals(Direction.BACKWARD))moveBackward(null);
			else if(move.prevDirec.equals(Direction.LEFT))strafeLeft(null);
			else if(move.prevDirec.equals(Direction.RIGHT))strafeRight(null);
		}else idle(null);
	location.set(player.getRootNode().getLocalTranslation());
	

	}
	private Vector3f location = new Vector3f();
	*/
}
