
import com.jme.math.Vector3f;

import com.tps1.character.playerGameState;

public class characterMove {
	public enum Direction{
		FORWARD{ public Vector3f getDirection() { return Vector3f.UNIT_Z; }},
		BACKWARD{ public Vector3f getDirection() { return new Vector3f(0,0,-1); }},
		LEFT{ public Vector3f getDirection() { return Vector3f.UNIT_X; }},
		RIGHT{ public Vector3f getDirection() { return new Vector3f(-1,0,0); }},
		FORWARD_LEFT{ public Vector3f getDirection() { return new Vector3f(1,0,1); }},
		FORWARD_RIGHT{ public Vector3f getDirection() { return new Vector3f(-1,0,1); }},
		BACKWARD_LEFT{ public Vector3f getDirection() { return new Vector3f(1,0,-1); }},
		BACKWARD_RIGHT{ public Vector3f getDirection() { return new Vector3f(-1,0,-1); }};		
		 public abstract Vector3f getDirection();			
	}
	

	public characterMove(playerGameState player) {
		
	}
	
	
	public void update(float tpf){
		
		}
	
	/**
	 * Jump directly up. direction influenced from previous momentum
	 * @param scale how heavy is the force up
	 */	 
	public void jump(){
	}
	
	/**OverLoaded jump() method
	 * Jump in a given direction
	 * @param scale how heavy is the force up
	 * @return 
	 */	 
	public void jump(float x,int scale, float z){
	}
	
	/**
	 * moves uniform in a given direction
	 * @param direction use the Direction enum to set a specific direction
	 */
	public void move(Direction direction){
	
	 	}


}
