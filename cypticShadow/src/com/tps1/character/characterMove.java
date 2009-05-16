package com.tps1.character;
 
import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.contact.ContactInfo;
import com.jmex.physics.contact.MutableContactInfo;
import com.jmex.physics.geometry.PhysicsCapsule;
import com.jmex.physics.geometry.PhysicsSphere;
import com.jmex.physics.impl.ode.joints.OdeJoint;
import com.jmex.physics.impl.ode.joints.RotationalOdeJointAxis;
import com.jmex.physics.material.Material;
import com.tps1.GameState.DefineGameState;
import com.tps1.GameState.gameSingleton;
import com.tps1.lvlLoader.CopyOfCopyOflevelTester;
import com.tps1.scene.SkyBoxManager.SkyBoxGameState;

public class characterMove {
	
	private static final Material characterMaterial;
	private final OdeJoint joint;
	private RotationalOdeJointAxis rotAxis;
	private boolean offGround,allowFall, isMoving;
	
	 static {
	        characterMaterial = new Material( "Character Material" );
	        characterMaterial.setDensity( 1f );
	        MutableContactInfo contactDetails = new MutableContactInfo();
	        contactDetails.setBounce(0);
	        contactDetails.setMu(1);
	        contactDetails.setMuOrthogonal(1);
	        contactDetails.setDampingCoefficient(10);
	        characterMaterial.putContactHandlingDetails( Material.DEFAULT, contactDetails );
	    }
	
	private final DynamicPhysicsNode feetNode, physNode;
	private final Node baseNode, charNode;
    private final InputHandler contactDetect;
	/**
	 * Initalizes the ode Ogre walking system
	 * @param mainNode the Node that acts as the base to hold the branches of the system
	 * @param modelNode the Node containing character Geometry
	 * @param physicsSpace the active and dominate physicsSpace
	 * @param input
	 */
	public characterMove(playerGameState player, InputHandler input) {
		contactDetect = input;
		physNode= gameSingleton.get().getPhysicsSpace().createDynamicNode();physNode.setName("physNode");;
		baseNode = player.getRootNode();
		charNode = player.getCharNode();
		feetNode=gameSingleton.get().getPhysicsSpace().createDynamicNode();feetNode.setName("feetNode");
		joint = (OdeJoint) gameSingleton.get().getPhysicsSpace().createJoint();
		offGround=false;
		isMoving=true;
		createPhysics();
	}
		
	/**
	 * creates a basic movement system for characters
	 */
	private void createPhysics(){
		BoundingBox bbox = (BoundingBox) charNode.getWorldBound();

		float cRadius = bbox.xExtent > bbox.zExtent ? bbox.zExtent : bbox.xExtent;
		float cHeight = bbox.yExtent *2f;
				
		PhysicsSphere feetGeom = feetNode.createSphere("Feet");
		feetGeom.setLocalScale(cRadius);
		feetNode.setMaterial(characterMaterial);
		feetNode.computeMass();
		charNode.setLocalTranslation(0, -cRadius/baseNode.getLocalScale().y, 0);
		baseNode.attachChild(feetNode);

		physNode.setLocalTranslation(0, 0, 0);
		physNode.setAffectedByGravity(false);
		PhysicsCapsule torsoGeom = physNode.createCapsule("Torso");
		torsoGeom.setLocalScale(new Vector3f(cRadius, cHeight-4*cRadius, cHeight/2.0f));
		torsoGeom.setLocalTranslation(0, cHeight - ((torsoGeom.getLocalScale().y)/2f+2f*cRadius), 0);
		Quaternion rot = new Quaternion();
		rot.fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
		torsoGeom.setLocalRotation(rot);
		physNode.computeMass();
		physNode.attachChild(charNode);
		baseNode.attachChild(physNode); 
		
		joint.setName("ROTATIONAL ODE AXIS");
		// A single joint is created and the Direction Will change depending on the users needs:
		rotAxis = (RotationalOdeJointAxis) joint.createRotationalAxis();
		rotAxis.setDirection(Vector3f.UNIT_Z);
		joint.attach(physNode, feetNode);		
		rotAxis.setRelativeToSecondObject( true );		
		rotAxis.setAvailableAcceleration(0f);rotAxis.setDesiredVelocity(0f);
		setUp();
	}
	
	public void update(float tpf){
		if(!allowFall){preventFall();}else{resetFeetRotation();}		
		if(!isMoving){feetNode.clearDynamics();}		
		offGround = false;	
		contactDetect.update(tpf);	
	}
	/**
	 *In order to make sure the character does not fall over
	 * we need to reset his vertical orientation once in a while 
	 */
	private void preventFall(){
		Quaternion rotation = physNode.getLocalRotation();
		Vector3f[] axes = new Vector3f[3];
		rotation.toAxes(axes);
		
		rotation.fromAxes(axes[0], Vector3f.UNIT_Y, axes[2]);
		physNode.setLocalRotation(rotation);
		physNode.setAngularVelocity(Vector3f.ZERO);
	    physNode.updateWorldVectors();
	}
	/**
	 * Countermeasures to make sure the feetNode remains stable
	 */
	private void resetFeetRotation(){
		Quaternion rotation = feetNode.getLocalRotation();
		Vector3f[] axes = new Vector3f[3];
		rotation.toAxes(axes);
		
		rotation.fromAxes(Vector3f.UNIT_X, Vector3f.UNIT_Y, axes[2]);
		feetNode.setLocalRotation(rotation);
		 
	}
	/** Detects when a Node collides with another Node */
	private void setUp(){
		   SyntheticButton collButton = feetNode.getCollisionEventHandler();
	        contactDetect.addAction( new InputAction() {	        	
	            public void performAction( InputActionEvent evt ) {
	                ContactInfo contactInfo = (ContactInfo) evt.getTriggerData();
	                Vector3f vec = contactInfo.getContactNormal(null);
	                if (vec.dot(Vector3f.UNIT_Y) > 1f){
	                    offGround = true;
	                }
	                Vector3f vel = contactInfo.getContactVelocity(null);
	                if (vel.length() > 10){
	                    System.out.println("POWERFUL CONTACT: "+vel.length());
	                }
	            }	            
	        }, collButton, false );		
	}
	/**
	 * Jump directly up. direction influenced from previous momentum
	 * @param scale how heavy is the force up
	 */	 
	public void jump(int scale){
		if(!offGround)
			feetNode.addForce(new Vector3f(0, scale, 0f));
	}
	/**OverLoaded jump() method
	 * Jump in a given direction
	 * @param scale how heavy is the force up
	 */	 
	public void jump(float x,int scale, float z){
		if(!offGround)
		feetNode.addForce(new Vector3f(x, 100f * scale, z));
	}
	/**
	 * moves uniform in a given direction
	 * @param Speed
	 * @param direction
	 */
	public void move(float Speed, Vector3f direction){
		if(!offGround)
			if (Speed == 0){
				rotAxis.setDesiredVelocity(0);
			}else{
				if(direction!=null){rotAxis.setDirection(direction);}
				rotAxis.setAvailableAcceleration( Speed );
				rotAxis.setDesiredVelocity( Speed );
			}
	}
	/**
	 * Accelerates uniformly until maxSpeed is met and then decelerates to desiredForce
	 * @param desiredForce
	 * @param acceleration
	 * @param direction
	 * @param maxSpeed
	 * @return 
	 * @preCondition {@link characterMove#move(int scale, Vector3f direction)} is called
	 */
	/*public void moveForward(int Speed, float acceleration, Vector3f direction, int maxSpeed){
		float run = 1+gameSingleton.get().timer.getTimeInSeconds();
		
        	while(run>gameSingleton.get().timer.getTimeInSeconds())
        		//{   move(maxSpeed,direction);
        		rotAxis.setAvailableAcceleration(acceleration/(Speed*10));
        	
        	//}else{System.out.println("check");
    		      move(Speed,direction);
        	     //}
            
	}*/
	public RotationalOdeJointAxis getRotationalAxis(){return rotAxis;}

public boolean getOffGround(){return offGround;}
}
