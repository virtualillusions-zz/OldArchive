/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.renderer.cam;

import com.jme3.app.Application;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.util.TempVars;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;
import com.vi.machello.app.state.EntityAppState;
import com.vi.machello.renderer.cam.components.CameraFocusPiece;
import com.vi.machello.renderer.cam.components.CameraFocusPiece.FocusType;
import com.vi.machello.renderer.cam.components.CameraSceneBoundsPiece;
import com.vi.machello.renderer.visual.components.InScenePiece;
import com.vi.machello.scene.dynamics.components.PositionPiece;
import com.vi.machello.util.math.Vector3Bounds;

/**
 * <b>Name:</b> CameraSystem<br/>
 *
 * <b>Purpose:</b> Positions and Focuses the camera based on Focal Points<br/>
 *
 * <b>Description:</b> Note:For now to prevent confusion x is left y is up and z
 * is forward
 *
 * @author Kyle D. Williams
 */
public class CameraSystem extends EntityAppState {

    @Override
    protected void AppState(Application app, EntityData ed) {
        cam = app.getCamera();
        //having one filter for scene and another for player is not
        //worth the overhead of api sorting entities
        camSet = ed.getEntities(
                CameraFocusPiece.class,
                PositionPiece.class,
                InScenePiece.class);
        targetLocation = new Vector3f();
        prevLocation = new Vector3f();
        camLocation = new Vector3f();
        targetBounds = Vector3Bounds.create();
    }

    @Override
    protected void enable() {
        cam.setLocation(camLocation);
        prevLocation.set(targetLocation);
        cam.lookAt(targetLocation, Vector3f.UNIT_Y);
    }

    @Override
    protected void disable() {
    }

    @Override
    protected void cleanUp() {
        cam = null;
        camSet.release();
        camSet = null;
    }

    @Override
    protected void appUpdate(float tpf) {
        TempVars vars = TempVars.get();
        updatePosition();
        //Compute Camera and Camera Target Position
        Vector3f average = targetBounds.average(vars.vect1);
        Vector3f distance = targetBounds.distance(vars.vect2);
        targetLocation.set(average);//This is the location the camera is looking at
        float x = average.getX();
        //ANGLE RATIO BETEWEN Y AND Z PRODUCED A SPECIFIC ANGLE NOW SEE IF THEIR CAN BE SOME FORM OF RATIO BETWEEN Z AND X
        minDistance = targetBounds.getZ().max + 5f;
        maxDistance = minDistance + 2.5f;
        //System.out.println(targetBounds.getZ().max);
        float z = sceneFocus == true ? maxDistance : distance.getX();
        z = FastMath.clamp(z, minDistance, maxDistance);
        //Tan Theta = O/A = y/z -> y = TanTheta * z
        //Optimal from cam is 53.1301024f so 90-optimal
        //A true isometric view where the x,y,z axis has the same length is 35.264 this gives a ratio of 2:1
        //float deg = 35.264f * FastMath.DEG_TO_RAD;
        //float y = average.getY() + FastMath.tan(deg) * z;//need the value plus the original average height else maybe too low
        float y = average.getY() + 3.535482f;//Pre-Computed Value produces optimal viewing angle
        camLocation.set(x, y, z);//This is where the camera is

        if (smoothMotion == true) {
            //do a smooth transition
            Vector3f camSlerp = vars.vect1;
            camSlerp.set(cam.getLocation());
            camSlerp.interpolateLocal(camLocation, chasingSensitivity);
            cam.setLocation(camSlerp);

            //the cam looks at the target
            //need to also slerp
            prevLocation.interpolateLocal(targetLocation, focusingSensitivity);
            cam.lookAt(prevLocation, Vector3f.UNIT_Y);
        } else {
            //Absolute
            cam.setLocation(camLocation);
            cam.lookAt(targetLocation, Vector3f.UNIT_Y);
        }

        //Needs to be last thing done to make sure
        //future changes have correct values
        updateCameraBounds(vars);
        vars.release();
    }

    /**
     * Check if Camera Focuses if their is a Scene Focus then fixate on the
     * location. If however, their are only player focuses then adjust camera's
     * target position to compensate
     */
    private void updatePosition() {
        //Update coordinate boundaries
        if (camSet.applyChanges()) {
            sceneFocus = false;//better to do it this way to prevent false positive by simply checking if avg is the same
            //first reset targetBounds by setting min to max possible value and max to minimal possible value so they can be overwritten
            //TODO prevent nullset
            targetBounds.reset();
            //update entity  only update if a focus point was added or moved
            for (Entity e : camSet) {
                //get Relevant components
                CameraFocusPiece cfp = e.get(CameraFocusPiece.class);
                PositionPiece p = e.get(PositionPiece.class);
                //Store relevant value
                float x = p.getX();
                float y = p.getY();
                float z = p.getZ();

                if (FocusType.SCENE_FOCUS.equals(cfp.getFocusType())) {
                    //Only searches for initial scene focus
                    //if found focus then set max and min to the focus point
                    targetBounds.set(x, y, z);
                    sceneFocus = true;
                    break;
                } else if (FocusType.PLAYER.equals(cfp.getFocusType())) {
                    targetBounds.setIfBounds(x, y, z);
                }
            }
        }
    }

    /**
     * Updates the bounds used to determine if all relevant objects are within
     * the camera bounds
     */
    private void updateCameraBounds(TempVars vars) {
        //This gives where it is
        //float z = cam.getViewToProjectionZ(cam.getLocation().getZ());
        //This gives max of where it could be
        float z = cam.getViewToProjectionZ(maxDistance);
        //Since camera resolution can be changed must always recompute
        Vector2f origin = vars.vect2d.set(0, 0);
        Vector2f destin = vars.vect2d2.set(cam.getWidth(), cam.getHeight());
        //get some temporary vectors
        Vector3f minVec = vars.vect1.zero();
        Vector3f maxVec = vars.vect2.zero();
        //find the screen boundaries
        cam.getWorldCoordinates(origin, z, minVec);
        cam.getWorldCoordinates(destin, z, maxVec);
        //keep relevant information
        float minX = minVec.x;
        float maxX = maxVec.x;
        CameraSceneBoundsPiece csb = new CameraSceneBoundsPiece(minX, maxX);
        getEntityData().setComponent(getApplicationId(), csb);
    }

    /**
     * Enables smooth motion for this chase camera
     *
     * @param smooth
     */
    public void setSmoothMotion(boolean smooth) {
        this.smoothMotion = smooth;
    }
    //Variables
    private Camera cam;
    private EntitySet camSet;
    private Vector3f targetLocation;
    private Vector3f prevLocation;
    private Vector3f camLocation;
    private Vector3Bounds targetBounds;
    private float minDistance;
    private float maxDistance;
    private float chasingSensitivity = 0.01f;
    private float focusingSensitivity = 0.025f;
    private boolean sceneFocus = false;
    private boolean smoothMotion = false;
}
