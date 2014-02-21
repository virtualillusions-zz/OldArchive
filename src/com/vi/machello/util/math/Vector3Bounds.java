/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.util.math;

import com.jme3.math.Vector3f;

/**
 *
 * @author Kyle D. Williams
 */
public class Vector3Bounds {

    /**
     * the x value of the vector.
     */
    public Bounds x;
    /**
     * the y value of the vector.
     */
    public Bounds y;
    /**
     * the z value of the vector.
     */
    public Bounds z;

    /**
     * Constructor instantiates a new
     * <code>Vector3Bounds</code> with default values of (0,0) for min and max.
     *
     */
    public Vector3Bounds() {
        this.x = new Bounds();
        this.y = new Bounds();
        this.z = new Bounds();
    }

    /**
     * Constructor instantiates a new
     * <code>Vector3f</code> with provides values.
     *
     * @param x the x value of the vector.
     * @param y the y value of the vector.
     * @param z the z value of the vector.
     */
    public Vector3Bounds(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
        this.x = new Bounds(xMin, xMax);
        this.y = new Bounds(yMin, yMax);
        this.z = new Bounds(zMin, zMax);
    }

    public static Vector3Bounds create() {
        return new Vector3Bounds();
    }

    public static Vector3Bounds create(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
        return new Vector3Bounds(xMin, xMax, yMin, yMax, zMin, zMax);
    }

    /**
     *
     * @return the Bounds Object for X
     */
    public Bounds getX() {
        return x;
    }

    /**
     * set the minimum and maximum values of the X Bounds Object
     *
     * @param min the minimum value.
     * @param max the maximum value.
     * @return this bounds object
     */
    public Bounds setX(float m) {
        x.min = x.max = m;
        return x;
    }

    /**
     *
     * @return the Bounds Object for Y
     */
    public Bounds getY() {
        return y;
    }

    /**
     * set the minimum and maximum values of the Y Bounds Object
     *
     * @param min the minimum value.
     * @param max the maximum value.
     * @return this bounds object
     */
    public Bounds setY(float m) {
        y.min = y.max = m;
        return y;
    }

    /**
     *
     * @return the Bounds Object for Z
     */
    public Bounds getZ() {
        return z;
    }

    /**
     * set the minimum and maximum values of the Z Bounds Object
     *
     * @param min the minimum value.
     * @param max the maximum value.
     * @return this bounds object
     */
    public Bounds setZ(float m) {
        z.min = z.max = m;
        return z;
    }

    /**
     * set the minimum and maximum values of the Bounds Object
     *
     * @param x the x value of the vector.
     * @param y the y value of the vector.
     * @param z the z value of the vector.
     *
     * @return this bounds object
     */
    public Vector3Bounds set(float x, float y, float z) {
        this.x.set(x);
        this.y.set(y);
        this.z.set(z);
        return this;
    }

    /**
     * set the maximum values of the Bounds Object
     *
     * @param x the x value of the vector.
     * @param y the y value of the vector.
     * @param z the z value of the vector.
     *
     * @return this bounds object
     */
    public Vector3Bounds setMax(float x, float y, float z) {
        this.x.max = x;
        this.y.max = y;
        this.z.max = z;
        return this;
    }

    /**
     * set the minimumvalues of the Bounds Object
     *
     * @param x the x value of the vector.
     * @param y the y value of the vector.
     * @param z the z value of the vector.
     *
     * @return this bounds object
     */
    public Vector3Bounds setMin(float x, float y, float z) {
        this.x.min = x;
        this.y.min = y;
        this.z.min = z;
        return this;
    }

    public Vector3f average(Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        return store.set(x.average(), y.average(), z.average());
    }

    public Vector3f distance(Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        return store.set(x.distance(), y.distance(), z.distance());
    }

    public void setIfBounds(float x1, float y1, float z1) {
        x.setIfBounds(x1);
        y.setIfBounds(y1);
        z.setIfBounds(z1);
    }

    /**
     * @see Bounds#reset()
     */
    public void reset() {
        x.reset();
        y.reset();
        z.reset();
    }

    @Override
    public String toString() {
        return String.format(
                "+---------------+---------------------------+\n"
                + "|Coordinate\t|\t  X\t Y\t Z  |\n"
                + "|Minimum Bound \t|\t%2.2f\t%2.2f\t%2.2f|\n"
                + "|Average \t|\t%2.2f\t%2.2f\t%2.2f|\n"
                + "|Maximum Bound \t|\t%2.2f\t%2.2f\t%2.2f|\n"
                + "|Distance\t|\t%2.2f\t%2.2f\t%2.2f|\n"
                + "+---------------+---------------------------+",
                x.min, y.min, z.min,
                x.average(), y.average(), z.average(),
                x.max, y.max, z.max,
                x.distance(), y.distance(), z.distance());
    }
}
