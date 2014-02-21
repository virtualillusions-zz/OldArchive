/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.scene.dynamics.control;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.vi.machello.app.control.BaseControl;

/**
 *
 * @author Kyle D. Williams
 */
public class StaticPhysicsControl extends BaseControl implements PhysicsControl, PhysicsCollisionListener {

////////////////////Spatial.setSpatial(Spatial spat)/////////////////////////
    /**
     * Creates new collision shapes for static elements. Used mostly for scene
     * and its elements
     */
    protected void createCollisionShape(CollisionShape regShape) {
        if (spatial == null) {
            return;
        }
        if (spatial instanceof Geometry) {
            Geometry geom = (Geometry) spatial;
            Mesh mesh = geom.getMesh();
            if (mesh instanceof Sphere) {
                regShape = new SphereCollisionShape(((Sphere) mesh).getRadius());
            } else if (mesh instanceof Box) {
                regShape = new BoxCollisionShape(new Vector3f(((Box) mesh).getXExtent(), ((Box) mesh).getYExtent(), ((Box) mesh).getZExtent()));
            }
        } else {
            regShape = CollisionShapeFactory.createMeshShape(spatial);
        }
    }
//////////////////////////////////////COMPUTATION/////////////////////////////   

    public void collision(PhysicsCollisionEvent event) {
    }

    @Override
    protected void BaseControl() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPhysicsSpace(PhysicsSpace space) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PhysicsSpace getPhysicsSpace() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
