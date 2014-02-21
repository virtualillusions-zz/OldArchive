/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.renderer.cam.components;

import com.simsilica.es.EntityComponent;

/**
 * Component used to keep track of the scene bounds IE where the player can no
 * longer advance
 *
 * @author Kyle D. Williams
 */
public class CameraSceneBoundsPiece implements EntityComponent {

    private float min;
    private float max;

    public CameraSceneBoundsPiece() {
        this(0.0f, 0.0f);
    }

    public CameraSceneBoundsPiece(float minX, float maxX) {
        this.min = minX;
        this.max = maxX;
    }

    /**
     *
     * @return float The Lower Bound
     */
    public float getMinBound() {
        return min;
    }

    /**
     *
     * @return float The Upper Bound
     */
    public float getMaxBound() {
        return max;
    }

    @Override
    public String toString() {
        return "CameraSceneBounds[" + "min=" + min + ", max=" + max + ']';
    }
}
