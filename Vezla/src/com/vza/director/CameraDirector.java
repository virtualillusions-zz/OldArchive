/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vza.director;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.MotionAllowedListener;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;

/**
 * A Firt Person view camera Director
 * This state directs the camera and is one of the most important
 * aspects to the game.
 * The View and rotation of this controller is dependant on the amount
 * of active fighters and thier location in game Space.
 * The Max active fighters. In the event that the max is met the camera
 * makes sure that both fighters are always in view and pans and zooms
 * to keep the characters at a preset distance from the borders of the
 * camera
 * @author Kyle Williams
 */
public final class CameraDirector implements com.jme3.app.state.AppState{

    private boolean initialized = false;
    public Camera cam;
    public Spatial[] activeModels;

    public void initialize(AppStateManager stateManager, Application app) {
        initialized=true;
        this.cam=app.getCamera();
       // activeModels=Director.getActiveModels();
    }

    public boolean isSinglePlayer(){return Director.getAppState(ModelDirector.class).getActiveModels().length>1?false:true;}



      private float rotationSpeed = 1f;
    private float moveSpeed = 3f;
    private MotionAllowedListener motionAllowed = null;

       private void rotateCamera(float value, Vector3f axis){

        Matrix3f mat = new Matrix3f();
        mat.fromAngleNormalAxis(rotationSpeed * value, axis);

        Vector3f up = cam.getUp();
        Vector3f left = cam.getLeft();
        Vector3f dir = cam.getDirection();

        mat.mult(up, up);
        mat.mult(left, left);
        mat.mult(dir, dir);

        Quaternion q = new Quaternion();
        q.fromAxes(left, up, dir);
        q.normalize();

        cam.setAxes(q);
    }

    private void zoomCamera(float value){
        // derive fovY value
        float h = cam.getFrustumTop();
        float w = cam.getFrustumRight();
        float aspect = w / h;

        float near = cam.getFrustumNear();

        float fovY = FastMath.atan(h / near)
                  / (FastMath.DEG_TO_RAD * .5f);
        fovY += value * 0.1f;

        h = FastMath.tan( fovY * FastMath.DEG_TO_RAD * .5f) * near;
        w = h * aspect;

        cam.setFrustumTop(h);
        cam.setFrustumBottom(-h);
        cam.setFrustumLeft(-w);
        cam.setFrustumRight(w);
    }

    private void riseCamera(float value){
        Vector3f vel = new Vector3f(0, value * moveSpeed, 0);
        Vector3f pos = cam.getLocation().clone();

        if (motionAllowed != null)
            motionAllowed.checkMotionAllowed(pos, vel);
        else
            pos.addLocal(vel);

        cam.setLocation(pos);
    }

    private void moveCamera(float value, boolean sideways){
        Vector3f vel = new Vector3f();
        Vector3f pos = cam.getLocation().clone();

        if (sideways){
            cam.getLeft(vel);
        }else{
            cam.getDirection(vel);
        }
        vel.multLocal(value * moveSpeed);

        if (motionAllowed != null)
            motionAllowed.checkMotionAllowed(pos, vel);
        else
            pos.addLocal(vel);

        cam.setLocation(pos);
    }











    public boolean isInitialized() {
        return initialized;
    }

    public void stateAttached(AppStateManager stateManager) {

    }

    public void stateDetached(AppStateManager stateManager) {

    }

    public void update(float tpf) {

    }

    public void render(RenderManager rm) {

    }

    public void cleanup() {
        initialized =false;
    }

}
