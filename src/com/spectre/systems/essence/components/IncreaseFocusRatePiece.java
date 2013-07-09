/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.essence.components;

import com.simsilica.es.EntityComponent;

/**
 * Boolean used to check if focus rate should be increased and by how much
 *
 * @author Kyle Williams
 */
public class IncreaseFocusRatePiece implements EntityComponent {

    private boolean increaseFocusRate;
    private float increasedRate;

    public IncreaseFocusRatePiece() {
        increaseFocusRate = false;
        increasedRate = 1.5f;
    }

    public IncreaseFocusRatePiece(boolean increaseFocusRate) {
        this.increaseFocusRate = increaseFocusRate;
    }

    public IncreaseFocusRatePiece(float increasedRate) {
        this.increaseFocusRate = true;
        this.increasedRate = increasedRate;
    }

    public boolean isIncreaseFocusRate() {
        return increaseFocusRate;
    }

    public float getIncreasedRate() {
        return increasedRate;
    }

    @Override
    public String toString() {
        return "IncreaseFocusRatePiece[increaseFocusRate=" + increaseFocusRate + ", increasedRate=" + increasedRate + "]";
    }
}
