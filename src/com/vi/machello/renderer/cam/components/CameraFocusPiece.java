/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.renderer.cam.components;

import com.simsilica.es.EntityComponent;

/**
 * Component used to determine the priority of the visual asset bound to the
 * entity
 *
 * @author Kyle D. Williams
 */
public class CameraFocusPiece implements EntityComponent {

    private int type;

    public CameraFocusPiece() {
        this(FocusType.PLAYER);
    }

    public CameraFocusPiece(FocusType type) {
        this.type = type.ordinal();
    }

    /**
     * @return the type of visual element the entity represents
     */
    public FocusType getFocusType() {
        return FocusType.values()[type];
    }

    @Override
    public String toString() {
        return "CameraFocusPiece[type=" + getFocusType() + "]";
    }

    public enum FocusType {

        /**
         * Any Archetype that is directly controlled by the player
         */
        PLAYER,
        /**
         * Primary Camera Focus
         */
        SCENE_FOCUS;
    }
}
