/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.scene.dynamics.components;

import com.simsilica.es.EntityComponent;

/**
 * Component used to determine the collision type and if the entity is affected
 * by gravity
 *
 * @author Kyle D. Williams
 */
public class RigidBodyPiece implements EntityComponent {

    private int type;
    private boolean affectedByForces;

    /**
     * Creates a dynamic entity affected by forces
     */
    public RigidBodyPiece() {
        this(CollisionType.DYNAMIC, true);
    }

    /**
     * Creates a dynamic entity static entity not affected by forces and
     * dynamics are
     *
     * @param type the CollisionType
     */
    public RigidBodyPiece(CollisionType type) {
        this(type, type.equals(CollisionType.DYNAMIC) ? true : false);
    }

    /**
     * Creates a dynamic entity
     *
     * @param affectedByForces is the entity affected by forces
     */
    public RigidBodyPiece(boolean affectedByForces) {
        this(CollisionType.DYNAMIC, affectedByForces);
    }

    /**
     * @param type the CollisionType
     * @param affectedByForces is the entity affected by forces
     */
    public RigidBodyPiece(CollisionType type, boolean affectedByForces) {
        this.type = type.ordinal();
        this.affectedByForces = type.equals(CollisionType.STATIC) ? false : affectedByForces;
    }

    /**
     *
     * @return CollisionType - the collision type of the entity
     */
    public CollisionType getType() {
        return CollisionType.values()[type];
    }

    /**
     * @return true if entity is affected by gravity
     */
    public boolean isAffectedByForces() {
        return affectedByForces;
    }

    @Override
    public String toString() {
        return "CollisionTypePiece[CollisionType=" + getType()
                + ", AffectedByGravity=" + isAffectedByForces() + ']';
    }

    public enum CollisionType {

        /**
         * Indicates an entity not affected by collision forces
         */
        STATIC,
        /**
         * Indicates an entity affected by collision forces
         */
        DYNAMIC
    }
}
