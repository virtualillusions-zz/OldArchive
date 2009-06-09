package com.tps1.character;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.terrain.TerrainBlock;
import com.tps1.GameState.gameSingleton;
 

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

	Node player;
	public characterMove(playerGameState player){
		this.player = player.getCharNode();
		
		
	}
	
	public void move(Direction forward) {
		// TODO Auto-generated method stub
		
	} 

	public void jump() {
		// TODO Auto-generated method stub
		
	}
	
	public void update(float time){
	
	}
	
}
