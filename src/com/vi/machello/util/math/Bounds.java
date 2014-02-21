/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.util.math;

/**
 *
 * @author Kyle D. Williams
 */
public class Bounds {

    /**
     * the lower bound value.
     */
    public float min;
    /**
     * the upper bound value
     */
    public float max;

    /**
     * Creates a Bounds object with minimum and maximum values set to 0.
     */
    public Bounds() {
        min = max = 0;
    }

    /**
     * Creates a Bounds object given initial min and max values.
     *
     * @param min The minimum value
     * @param max The maximum value
     */
    public Bounds(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public static Bounds create() {
        return new Bounds();
    }

    public static Bounds create(float min, float max) {
        return new Bounds(min, max);
    }

    /**
     * set the minimum and maximum values of the Bounds Object
     *
     * @param min the minimum value.
     * @param max the maximum value.
     * @return this bounds object
     */
    public Bounds set(float min, float max) {
        this.min = min;
        this.max = max;
        return this;
    }

    /**
     * set the minimum and maximum values of the Bounds Object
     *
     * @param min the minimum value.
     * @param max the maximum value.
     * @return this bounds object
     */
    public Bounds set(float m) {
        min = max = m;
        return this;
    }

    public void setMin(float val) {
        min = val;
    }

    public void setMax(float val) {
        min = val;
    }

    public boolean setIfMin(float val) {
        min = min > val || min == Float.MAX_VALUE ? val : min;
        return min == val;
    }

    public boolean setIfMax(float val) {
        max = max < val || max == Float.MIN_VALUE ? val : max;
        return max == val;
    }

    public void setIfBounds(float val) {
        setIfMin(val);
        setIfMax(val);
    }

    /**
     * Sets Min to max possible value and max to min possible value. Useful when
     * searching a list for max and min values
     */
    public void reset() {
        set(Float.MAX_VALUE, Float.MIN_VALUE);
    }

    public float average() {
        return (max + min) / 2;
    }

    public float distance() {
        return max - min;
    }

    public boolean isEqual() {
        return min == max;
    }

    @Override
    public String toString() {
        return "Bounds[" + "min=" + min + ", max=" + max + ']';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Float.floatToIntBits(this.min);
        hash = 59 * hash + Float.floatToIntBits(this.max);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bounds other = (Bounds) obj;
        if (Float.floatToIntBits(this.min) != Float.floatToIntBits(other.min)) {
            return false;
        }
        if (Float.floatToIntBits(this.max) != Float.floatToIntBits(other.max)) {
            return false;
        }
        return true;
    }
}
