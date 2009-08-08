package com.fightingchaos.war.controller;

import java.util.Random;

import com.fightingchaos.war.navigation.Cell;
import com.fightingchaos.war.navigation.Mesh;
import com.fightingchaos.war.navigation.Path;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.util.Timer;

/**
 * A movable object in the game world
 * 
 * Portions Copyright (C) Greg Snook, 2000
 * 
 * @author TR
 * 
 */
public class Entity extends Node{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Navigationmesh the entity is on
	 */
	protected Mesh naviMesh = null;

	/**
	 * Cell in the navigationmesh the entity is on
	 */
	protected Cell currentCell = null;
	/**
	 * world position
	 */
	// FIXME replace with localtranslation ?
	private Vector3f position = new Vector3f();
	/**
	 * current movement vector
	 */
	protected Vector3f movement = new Vector3f();
	/**
	 * the maximum speed this entity can travel
	 */
	float maxSpeed = 1.0f;
	
	Node debugPoints = new Node();

	/**
	 * is this entity using a path to navigate?
	 */
	protected boolean pathActive = false;
	/**
	 * the path to follow (can be null if not following a path)
	 */
	protected Path path = new Path();
	/**
	 * next waypoint in path
	 */
	Path.WAYPOINT nextWaypoint = null;
	
	private Spatial gfxNode;

	public Entity(Mesh Parent, Vector3f Position, Cell CurrentCell) {
		this.naviMesh = Parent;
		setPosition(Position);
		this.currentCell = CurrentCell;
		setNaviMesh(Parent);
	}
	
	public void setNaviMesh(Mesh naviMesh) {
		this.naviMesh = naviMesh;
		if (naviMesh != null) {
			// if no starting cell is provided, find one by searching the mesh
			if (currentCell == null) {
				currentCell = naviMesh.FindClosestCell(position);
			}

			// make sure our position is within the current cell
			setPosition(naviMesh.SnapPointToCell(currentCell, position));
		}
	}

	void UpdateMovement(float elapsedTime) {
		float distance;
		float max_distance = maxSpeed * elapsedTime;

		// if we have no parent mesh, return. We are simply a static object.
		if (naviMesh == null) {
			return;
		}

		// Figure out where we are going
		if (pathActive) {
			// Move along the waypoint path
//			if (nextWaypoint != path.WaypointList().get(
//					path.WaypointList().size()-1)) {
				// determine our desired movement vector
				movement = nextWaypoint.Position;
				movement = movement.subtract(position);
//			} else {
//				// we have reached the end of the path
//				pathActive = false;
////				movement.x = movement.y = movement.z = 0.0f;
//			}
		} else {
			// apply some friction to our current movement (if any)
			movement.x *= 0.75f;
			movement.y *= 0.75f;
			movement.z *= 0.75f;
		}

		// Adjust Movement

		// scale back movement by our max speed if needed
		distance = movement.length();
		if (distance > max_distance) {
			movement = movement.normalize();
			movement = movement.mult(max_distance);
		}

		// come to a full stop when we go below a certain threshold
		if (Math.abs(movement.x) < 0.005f)
			movement.x = 0.0f;
		if (Math.abs(movement.y) < 0.005f)
			movement.y = 0.0f;
		if (Math.abs(movement.z) < 0.005f)
			movement.z = 0.0f;

		//
		// Constrain any remaining Horizontal movement to the parent navigation
		// rink
		//
		if (movement.x != 0.0f || movement.z != 0.0f) {
			//
			// Constrain any remaining Horizontal movement to the parent
			// navigation rink
			//
			if (naviMesh != null) {
				// compute the next desired location
				Vector3f NextPosition = position.add(movement);
				Cell NextCell = null;

				// test location on the NavigationMesh and resolve collisions
				NextCell = naviMesh.ResolveMotionOnMesh(position, currentCell,
						NextPosition);
//System.out.println("new Pos:"+NextPosition);
				setPosition(NextPosition);
//if(currentCell != NextCell)
//System.out.println("new Cell:"+NextCell);	
				currentCell = NextCell;
			}
		} else if (pathActive) {
			// If we have no remaining movement, but the path is active,
			// we have arrived at our desired waypoint.
			// Snap to it's position and figure out where to go next
			setPosition((nextWaypoint).Position);
			movement.x = movement.y = movement.z = 0.0f;
			distance = 0.0f;
			nextWaypoint = path.GetFurthestVisibleWayPoint(nextWaypoint);
//System.out.println("Next waypoint:"+nextWaypoint);
			if (nextWaypoint == path.WaypointList().get(
					path.WaypointList().size()-1) && nextWaypoint.Position.equals(this.position)) {
				pathActive = false;
				path.m_WaypointList.clear();
//System.out.println("Path disabled");
//				movement.x = movement.y = movement.z = 0.0f;
			}
		}

	}
	
	public void GotoLocation(Vector3f Position) {
		Cell c = naviMesh.FindClosestCell(Position);
		if(c != null)
			GotoLocation(Position, c);
		else
			System.out.println("Warning no path found");
	}

	// Find a path to the designated location on the mesh
	public void GotoLocation(Vector3f Position, Cell cell) {
		if (naviMesh != null) {
			
			cell.MapVectorHeightToCell(Position);
			movement.x = movement.y = movement.z = 0.0f;

float dT = Timer.getTimer().getTimeInSeconds();
			pathActive = naviMesh.BuildNavigationPath(path, currentCell,
					position, cell, Position.clone());
System.out.println("path building took "+(Timer.getTimer().getTimeInSeconds()-dT)+" seconds");
			
//for(Path.WAYPOINT p : path.m_WaypointList )
//	System.out.println("Path:"+p);

			if (pathActive) {
				nextWaypoint = path.WaypointList().get(0);
				
				pathDebugRender();
			}
		}
	}

	// Pick a random location to move to on the mesh
	void GotoRandomLocation() {
		if (naviMesh != null) {
			Random rand = new Random();
			// pick a random cell and go there
			int index = rand.nextInt() % naviMesh.TotalCells();
			Cell pCell = naviMesh.Cell(index);

			GotoLocation(pCell.CenterPoint(), pCell);
		}
	}

	// Add a movement vector to our current motion
	void AddMovement(Vector3f Movement) {
		movement.add(Movement);
	}

	// Set a movement vector as our current motion
	// FIXME this should be move = mode and not add!??
	void SetMovement(Vector3f Movement) {
		movement.add(Movement);
	}

	// Add a movement value to our current motion in the x direction
	public void SetMovementX(float X) {
		movement.x += X;
	}

	// Add a movement value to our current motion in the y direction
	void SetMovementY(float Y) {
		movement.y += Y;
	}

	// Add a movement value to our current motion in the z direction
	public void SetMovementZ(float Z) {
		movement.z += Z;
	}

	// Set our maximum speed. This is the distance in world units we may travel
	// per second.
	public void SetMaxSpeed(float speed) {
		maxSpeed = speed;
	}
	
	public float getMaxSpeed(){
		return maxSpeed;
	}
	
	boolean PathIsActive() {
		return (pathActive);
	}

	public Vector3f Position() {
		return (position);
	}

	Vector3f Movement() {
		return (movement);
	}

	public Cell CurrentCell() {
		return (currentCell);
	}

	public Spatial getGfxNode() {
		return gfxNode;
	}

	public void setGfxNode(Spatial gfxNode) {
		this.gfxNode = gfxNode;
		attachChild(gfxNode);
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
		if(gfxNode != null)
			gfxNode.setLocalTranslation(position);
	}
	
	/**
     * <code>updateWorldData</code> updates all the children maintained by
     * this node.
     * 
     * @param time
     *            the frame time.
     */
    @Override
    public void updateWorldData(float time) {
    	super.updateWorldData(time);
    	UpdateMovement(0.05f);
    }
    
	private void pathDebugRender() {
		if(parent != null){
			parent.detachChild(debugPoints);
			debugPoints.detachAllChildren();
			for(Path.WAYPOINT w :path.m_WaypointList){
				Box b = new Box("WAYPOINT",w.Position,0.8f,0.8f,0.8f);
				b.setDefaultColor(ColorRGBA.red);
				b.setLightCombineMode(Spatial.LightCombineMode.Off);
		        b.setTextureCombineMode(Spatial.TextureCombineMode.Off);
		        debugPoints.attachChild(b);
			}
			parent.attachChild(debugPoints);
			debugPoints.updateRenderState();
		}
	}
}
