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
public class PositionPiece extends Vector3fPiece {

    protected final int dir;

    public PositionPiece(float X, float Y, float Z) {
        this(X, Y, Z, Direction.NONE);
    }

    public PositionPiece(float X, float Y, float Z, Direction direction) {
        super(X, Y, Z);
        dir = direction.ordinal();
    }

    public Direction getDir() {
        return Direction.values()[dir];
    }

    @Override
    public String toString() {
        return "PositionPiece[" + super.toString()
                + ", Dir=" + getDir() + "]";
    }
}
