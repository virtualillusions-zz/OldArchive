/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.scene.dynamics.components;

import com.vi.machello.util.math.Vector3fPiece;

/**
 * Represents the position and the direction an entity is facing
 *
 * @author Kyle D. Williams
 */
public class MovementPiece extends Vector3fPiece {

    public static final MovementPiece IDLE = new MovementPiece();
    protected final int dir;

    public MovementPiece() {
        this(0, 0, 0, Direction.NONE);
    }

    public MovementPiece(float X, float Y, float Z) {
        this(X, Y, Z, Direction.NONE);
    }

    public MovementPiece(float X, float Y, float Z, Direction direction) {
        super(X, Y, Z);
        dir = direction.ordinal();
    }

    public Direction getDir() {
        return Direction.values()[dir];
    }
    
    public boolean isIdle(){
        return this.equals(IDLE);
    }

    @Override
    public String toString() {
        return "MovementPiece[" + super.toString()
                + ", Dir=" + getDir() + "]";
    }
}
