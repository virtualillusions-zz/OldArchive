/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.util.math;

import com.simsilica.es.EntityComponent;

/**
 * Component represents a Vector3f
 *
 * @author Kyle D. Williams
 */
public class Vector3fPiece implements EntityComponent {

    protected float x;
    protected float y;
    protected float z;

    public Vector3fPiece(float X, float Y, float Z) {
        this.x = X;
        this.y = Y;
        this.z = Z;
    }

    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @return the z
     */
    public float getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "Vector3fPiece[x=" + x + ", y=" + y + " z=" + z + "]";
    }

    /**
     * <code>equals</code> determines if two VectorPieces are logically equal,
     * that is, if the values of (x, y, z) are the same for both VectorPieces.
     *
     * @param o the object to compare for equality
     * @return true if they are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vector3fPiece)) {
            return false;
        }

        if (this == o) {
            return true;
        }

        Vector3fPiece comp = (Vector3fPiece) o;
        if (Float.compare(x, comp.x) != 0
                || Float.compare(y, comp.y) != 0
                || Float.compare(z, comp.z) != 0) {
            return false;
        }

        return true;
    }

    /**
     * <code>hashCode</code> returns a unique code for this vector object based
     * on it's values. If two vectors are logically equivalent, they will return
     * the same hash code value.
     *
     * @return the hash code value of this vector.
     */
    @Override
    public int hashCode() {
        int hash = 37;
        hash += 37 * hash + Float.floatToIntBits(x);
        hash += 37 * hash + Float.floatToIntBits(y);
        hash += 37 * hash + Float.floatToIntBits(z);
        return hash;
    }
}
