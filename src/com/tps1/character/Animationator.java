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
		
		if(movement.getOffGround()){Jump(null);System.out.println("This is 1 FAIL!!!");}
		else if(movement.getRotationalAxis().getVelocity()>0.0)
		{	Vector3f direction = new Vector3f();
			movement.getRotationalAxis().getDirection(direction);
			     if(direction.getZ()>0)moveForward(null);
			else if(direction.getZ()<0)moveBackward(null);
			else if(direction.getX()>0)strafeLeft(null);
			else if(direction.getX()<0)strafeRight(null);
			System.out.println("This is 2!!!");
		}else idle(null);
		
		movement.jump(9);
	}
	
	
	
	 public static void main(String[] args) throws InterruptedException{
		 System.setProperty("jme.stats", "set");
		 StandardGame standardGame = new StandardGame("GameControl", StandardGame.GameType.GRAPHICAL, null);
	     standardGame.getSettings().setVerticalSync(false);
		 standardGame.start();     

	        try {
				SkyBoxGameState.Manager().setActive(true);
				
				 CopyOfCopyOflevelTester nex = new CopyOfCopyOflevelTester(0,0);
			 	   GameStateManager.getInstance().attachChild(nex);
			 	    nex.setActive(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			gameSingleton.get().stop=true;

			 Charactertype PLAYER1 = new Charactertype("robot");		
		     PLAYER1.getRootNode().getLocalTranslation().setZ(-1f);
			 PLAYER1.setActive(true);	
			/** 
			 Charactertype PLAYER2 = new Charactertype("robot");		
		     PLAYER2.getRootNode().getLocalTranslation().setZ(1f);
			 PLAYER2.setActive(true);	
			 
			 Charactertype PLAYER3 = new Charactertype("robot");		
		     PLAYER3.getRootNode().getLocalTranslation().setZ(-3f);
			 PLAYER3.setActive(true);	
			 
			 Charactertype PLAYER4 = new Charactertype("robot");		
		     PLAYER4.getRootNode().getLocalTranslation().setZ(3f);
			 PLAYER4.setActive(true);	
			 
			 Charactertype PLAYER5 = new Charactertype("robot");		
		     PLAYER5.getRootNode().getLocalTranslation().setZ(-5f);
			 PLAYER5.setActive(true);	
			 
			 Charactertype PLAYER6 = new Charactertype("robot");		
		     PLAYER6.getRootNode().getLocalTranslation().setZ(5f);
			 PLAYER6.setActive(true);	
			 
			 Charactertype PLAYER7 = new Charactertype("robot");		
		     PLAYER7.getRootNode().getLocalTranslation().setZ(-7f);
			 PLAYER7.setActive(true);	
			 
			 Charactertype PLAYER8 = new Charactertype("robot");		
		     PLAYER8.getRootNode().getLocalTranslation().setZ(7f);
			 PLAYER8.setActive(true);	
			 
			 Charactertype PLAYER9 = new Charactertype("robot");		
		     PLAYER9.getRootNode().getLocalTranslation().setZ(-9f);
			 PLAYER9.setActive(true);	
			 
			 Charactertype PLAYER10 = new Charactertype("robot");		
		     PLAYER10.getRootNode().getLocalTranslation().setZ(9f);
			 PLAYER10.setActive(true);	
			 
			 Charactertype PLAYER11 = new Charactertype("robot");		
		     PLAYER11.getRootNode().getLocalTranslation().setZ(11f);
			 PLAYER11.setActive(true);	
			 
			 Charactertype PLAYER12 = new Charactertype("robot");		
		     PLAYER12.getRootNode().getLocalTranslation().setZ(-11f);
			 PLAYER12.setActive(true);	
			 
			 Charactertype PLAYER13 = new Charactertype("robot");		
		     PLAYER13.getRootNode().getLocalTranslation().setZ(13f);
			 PLAYER13.setActive(true);	
			 
			 Charactertype PLAYER14 = new Charactertype("robot");		
		     PLAYER14.getRootNode().getLocalTranslation().setZ(-13f);
			 PLAYER14.setActive(true);	
			 
			 Charactertype PLAYER15 = new Charactertype("robot");		
		     PLAYER15.getRootNode().getLocalTranslation().setZ(15f);
			 PLAYER15.setActive(true);	
			 
			 Charactertype PLAYER16 = new Charactertype("robot");		
		     PLAYER16.getRootNode().getLocalTranslation().setZ(-15f);
			 PLAYER16.setActive(true);	
			 
			 Charactertype PLAYER17 = new Charactertype("robot");		
		     PLAYER17.getRootNode().getLocalTranslation().setZ(17f);
			 PLAYER17.setActive(true);	
			 
			 Charactertype PLAYER18 = new Charactertype("robot");		
		     PLAYER18.getRootNode().getLocalTranslation().setZ(-17f);
			 PLAYER18.setActive(true);	
			 
			 Charactertype PLAYER19 = new Charactertype("robot");		
		     PLAYER19.getRootNode().getLocalTranslation().setZ(19f);
			 PLAYER19.setActive(true);	
			 
			 Charactertype PLAYER20 = new Charactertype("robot");		
		     PLAYER20.getRootNode().getLocalTranslation().setZ(-19f);
			 PLAYER20.setActive(true);	
			 
			 Charactertype PLAYER21 = new Charactertype("robot");		
		     PLAYER21.getRootNode().getLocalTranslation().setZ(21f);
			 PLAYER21.setActive(true);	
			 
			 Charactertype PLAYER22 = new Charactertype("robot");		
		     PLAYER22.getRootNode().getLocalTranslation().setZ(-21f);
			 PLAYER22.setActive(true);	*/
			 
			gameSingleton.get().stop=false;

				     
		     final DefineGameState base = new DefineGameState(); 
		     base.setActive(true);	

		   }
}
