/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.util.math;

import com.simsilica.es.EntityComponent;

/**
 * Component represents a Quaternion
 *
 * @author Kyle D. Williams
 */
public class QuaternionPiece implements EntityComponent {

    protected float x;
    protected float y;
    protected float z;
    protected float w;

    public QuaternionPiece(float X, float Y, float Z, float W) {
        this.x = X;
        this.y = Y;
        this.z = Z;
        this.w = W;
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

    /**
     * @return the w
     */
    public float getW() {
        return w;
    }

    @Override
    public String toString() {
        return "QuaternionPiece[x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
    }

    /**
     * <code>equals</code> determines if two QuaternionPiecess are logically
     * equal, that is, if the values of (x, y, z, w) are the same for both
     * QuaternionPieces.
     *
     * @param o the object to compare for equality
     * @return true if they are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof QuaternionPiece)) {
            return false;
        }

        if (this == o) {
            return true;
        }

        QuaternionPiece comp = (QuaternionPiece) o;
        if (Float.compare(x, comp.x) != 0
                || Float.compare(y, comp.y) != 0
                || Float.compare(z, comp.z) != 0
                || Float.compare(w, comp.w) != 0) {
            return false;
        }
        return true;
    }

    /**
     *
     * <code>hashCode</code> returns the hash code value as an integer and is
     * supported for the benefit of hashing based collection classes such as
     * Hashtable, HashMap, HashSet etc.
     *
     * @return the hashcode for this instance of Quaternion.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 37;
        hash = 37 * hash + Float.floatToIntBits(x);
        hash = 37 * hash + Float.floatToIntBits(y);
        hash = 37 * hash + Float.floatToIntBits(z);
        hash = 37 * hash + Float.floatToIntBits(w);
        return hash;

    }
}
