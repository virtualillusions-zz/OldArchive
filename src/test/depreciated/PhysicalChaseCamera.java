package test.depreciated;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.input.*;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.spectre.app.ChaseCamera;
import java.io.IOException;
import java.util.LinkedList;

/**
 * this is an extended version of ChaseCamera.
 * @see ChaseCamera for original
 * @author Mark
 */
public class PhysicalChaseCamera implements ActionListener, AnalogListener, Control {

    private Spatial target = null;
    private float minVerticalRotation = 0.00f;
    private float maxVerticalRotation = FastMath.PI /3.5f;/// 2;
    private float minDistance = 3.0f;//1.0f;
    private float maxDistance = 6.0f;//40.0f;
    private float maxDistanceByZoom = 20.0f;
    private float distance = 20;
    private float zoomSpeed = 2f;
    private float rotationSpeed = 1.0f;
    private float rotation = 0;
    private float trailingRotationInertia = 0.05f;
    private float zoomSensitivity = 5f;
    private float rotationSensitivity = 5f;
    private float chasingSensitivity = 5f;
    private float trailingSensitivity = 0.5f;
    private float vRotation = FastMath.PI / 6;
    private boolean smoothMotion = false;
    private boolean trailingEnabled = true;
    private float rotationLerpFactor = 0;
    private float trailingLerpFactor = 0;
    private boolean rotating = false;
    private boolean vRotating = false;
    private float targetRotation = rotation;
    private InputManager inputManager;
    private Vector3f initialUpVec;
    private float targetVRotation = vRotation;
    private float vRotationLerpFactor = 0;
    private float targetDistance = distance;
    private float distanceLerpFactor = 0;
    private boolean zooming = false;
    private boolean trailing = false;
    private boolean chasing = false;
    private boolean canRotate;
    private float offsetDistance = 0.002f;
    private Vector3f prevPos;
    private boolean targetMoves = false;
    private boolean enabled = true;
    private Camera cam = null;
    private Vector3f targetDir;
    private float previousTargetRotation;
    private Vector3f pos;
    private Vector3f maxPos = new Vector3f();
    private PhysicsSpace myPhysicsSpace;
    boolean zoomin;


    /**
     * Constructs the physical chase camera, registers inputs and physics
     * @param cam the application camera
     * @param target the spatial to follow
     * @param inputManager the inputManager of the application to register inputs
     * @param physicsSpace the PhysicsSpace, the cam shall be active in
     */
    public PhysicalChaseCamera(Camera cam, final Spatial target,
            InputManager inputManager, PhysicsSpace physicsSpace) {
        //chase cam constructor stuff
        this.target = target;
        this.cam = cam;
        initialUpVec = cam.getUp().clone();

        //add PhysicsNode to world physicsSpace
        myPhysicsSpace = physicsSpace;

        //chase cam stuff.
        computePosition();
        target.addControl(this);
        prevPos = new Vector3f(target.getWorldTranslation());
        cam.setLocation(pos);
        registerWithInput(inputManager);
    }

    public void onAction(String name, boolean keyPressed, float tpf) {
        if (name.equals("toggleRotate") && enabled) {
            if (keyPressed) {
                canRotate = true;
                inputManager.setCursorVisible(false);
            } else {
                canRotate = false;
                inputManager.setCursorVisible(true);
            }
        }
    }

    public void onAnalog(String name, float value, float tpf) {

        if (name.equals("mouseLeft")) {
            rotateCamera(-value);
        } else if (name.equals("mouseRight")) {
            rotateCamera(value);
        } else if (name.equals("Up")) {
            vRotateCamera(value);
        } else if (name.equals("Down")) {
            vRotateCamera(-value);
        } else if (name.equals("ZoomIn")) {
            zoomCamera(value);
            if (zoomin == false) {
                distanceLerpFactor = 0;
            }
            zoomin = true;
        } else if (name.equals("ZoomOut")) {
            zoomCamera(-value);
            if (zoomin == true) {
                distanceLerpFactor = 0;
            }
            zoomin = false;
        }

    }

    /**
     * Registers inputs with the input manager
     * @param inputManager
     */
    public void registerWithInput(InputManager inputManager) {
        String[] inputs = {"toggleRotate", "Down", "Up", "mouseLeft", "mouseRight", "ZoomIn", "ZoomOut"};

        this.inputManager = inputManager;

        inputManager.addMapping("Up", new MouseAxisTrigger(1, true));
        inputManager.addMapping("Down", new MouseAxisTrigger(1, false));
        inputManager.addMapping("ZoomIn", new MouseAxisTrigger(2, true));
        inputManager.addMapping("ZoomOut", new MouseAxisTrigger(2, false));
        inputManager.addMapping("mouseLeft", new MouseAxisTrigger(0, true));
        inputManager.addMapping("mouseRight", new MouseAxisTrigger(0, false));
        inputManager.addMapping("toggleRotate", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("toggleRotate", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        inputManager.addListener(this, inputs);


    }

    private void computePosition() {
        float hDistance = (distance) * FastMath.sin((FastMath.PI / 2) - vRotation);
        pos = new Vector3f(hDistance * FastMath.cos(rotation), (distance) * FastMath.sin(vRotation), hDistance * FastMath.sin(rotation));
        pos = pos.add(target.getWorldTranslation());

        hDistance = (maxDistanceByZoom) * FastMath.sin((FastMath.PI / 2) - vRotation);
        maxPos = new Vector3f(hDistance * FastMath.cos(rotation), (maxDistanceByZoom) * FastMath.sin(vRotation), hDistance * FastMath.sin(rotation));
        maxPos = maxPos.add(target.getWorldTranslation());
        this.collide();
    }

    //rotate the camera around the target on the horizontal plane
    private void rotateCamera(float value) {
        if (!canRotate || !enabled) {
            return;
        }
        rotating = true;
        targetRotation += value * rotationSpeed;


    }

    //move the camera toward or away the target
    private void zoomCamera(float value) {
        if (!enabled) {
            return;
        }

        zooming = true;
        maxDistanceByZoom += value * zoomSpeed;
        if (maxDistanceByZoom > maxDistance) {
            maxDistanceByZoom = maxDistance;
        }
        if (maxDistanceByZoom < minDistance) {
            maxDistanceByZoom = minDistance;
        }
        if ((targetVRotation < minVerticalRotation) && (targetDistance > (minDistance + 1.0f))) {
            targetVRotation = minVerticalRotation;
        }
    }

    //rotate the camera around the target on the vertical plane
    private void vRotateCamera(float value) {
        if (!canRotate || !enabled) {
            return;
        }
        vRotating = true;
        targetVRotation += value * rotationSpeed;
        if (targetVRotation > maxVerticalRotation) {
            targetVRotation = maxVerticalRotation;
        }
        if ((targetVRotation < minVerticalRotation) && (targetDistance > (minDistance + 1.0f))) {
            targetVRotation = minVerticalRotation;
        }
    }

    /**
     * Updates the camera, should only be called internally
     */
    protected void updateCamera(float tpf) {
        if (enabled) {
            if (smoothMotion) {
                //computation of target direction
                targetDir = target.getWorldTranslation().subtract(prevPos);
                float dist = targetDir.length();

                //Low pass filtering on the target postition to avoid shaking when physics are enabled.
                if (offsetDistance < dist) {
                    //target moves, start chasing.
                    chasing = true;
                    //target moves, start trailing if it has to.
                    if (trailingEnabled) {
                        trailing = true;
                    }
                    //target moves...
                    targetMoves = true;
                } else {
                    //if target was moving, we compute a slight offset in rotation to avoid a rought stop of the cam
                    //We do not if the player is rotationg the cam
                    if (targetMoves && !canRotate) {
                        if (targetRotation - rotation > trailingRotationInertia) {
                            targetRotation = rotation + trailingRotationInertia;
                        } else if (targetRotation - rotation < -trailingRotationInertia) {
                            targetRotation = rotation - trailingRotationInertia;
                        }
                    }
                    //Target stops
                    targetMoves = false;
                }

                //the user is rotating the cam by dragging the mouse
                if (canRotate) {
                    //reseting the trailing lerp factor
                    trailingLerpFactor = 0;
                    //stop trailing user has the control
                    trailing = false;
                }


                if (trailingEnabled && trailing) {
                    if (targetMoves) {
                        //computation if the inverted direction of the target
                        Vector3f a = targetDir.negate().normalizeLocal();
                        //the x unit vector
                        Vector3f b = Vector3f.UNIT_X;
                        //2d is good enough
                        a.y = 0;
                        //computation of the rotation angle between the x axis and the trail
                        if (targetDir.z > 0) {
                            targetRotation = FastMath.TWO_PI - FastMath.acos(a.dot(b));
                        } else {
                            targetRotation = FastMath.acos(a.dot(b));
                        }
                        if (targetRotation - rotation > FastMath.PI || targetRotation - rotation < -FastMath.PI) {
                            targetRotation -= FastMath.TWO_PI;
                        }

                        //if there is an important change in the direction while trailing reset of the lerp factor to avoid jumpy movements
                        if (targetRotation != previousTargetRotation && FastMath.abs(targetRotation - previousTargetRotation) > FastMath.PI / 8) {
                            trailingLerpFactor = 0;
                        }
                        previousTargetRotation = targetRotation;
                    }
                    //computing lerp factor
                    trailingLerpFactor = Math.min(trailingLerpFactor + tpf * tpf * trailingSensitivity, 1);
                    //computing rotation by linear interpolation
                    rotation = FastMath.interpolateLinear(trailingLerpFactor, rotation, targetRotation);

                    //if the rotation is near the target rotation we're good, that's over
                    if (targetRotation + 0.01f >= rotation && targetRotation - 0.01f <= rotation) {
                        trailing = false;
                        trailingLerpFactor = 0;
                    }
                }

                //linear interpolation of the distance while chasing
                if (chasing) {
                    distance = target.getWorldTranslation().subtract(cam.getLocation()).length();
                    distanceLerpFactor = Math.min(distanceLerpFactor + (tpf * tpf * chasingSensitivity * 0.05f), 1);
                    distance = FastMath.interpolateLinear(distanceLerpFactor, distance, targetDistance);
                    if (targetDistance + 0.01f >= distance && targetDistance - 0.01f <= distance) {
                        distanceLerpFactor = 0;
                        chasing = false;
                    }
                }

                //linear interpolation of the distance while zooming
                if (zooming) {
                    distanceLerpFactor = Math.min(distanceLerpFactor + (tpf * tpf * zoomSensitivity), 1);
                    distance = FastMath.interpolateLinear(distanceLerpFactor, distance, targetDistance);
                    if (targetDistance + 0.1f >= distance && targetDistance - 0.1f <= distance) {
                        zooming = false;
                        distanceLerpFactor = 0;
                    }
                }

                //linear interpolation of the rotation while rotating horizontally
                if (rotating) {
                    rotationLerpFactor = Math.min(rotationLerpFactor + tpf * tpf * rotationSensitivity, 1);
                    rotation = FastMath.interpolateLinear(rotationLerpFactor, rotation, targetRotation);
                    if (targetRotation + 0.01f >= rotation && targetRotation - 0.01f <= rotation) {
                        rotating = false;
                        rotationLerpFactor = 0;
                    }
                }

                //linear interpolation of the rotation while rotating vertically
                if (vRotating) {
                    vRotationLerpFactor = Math.min(vRotationLerpFactor + tpf * tpf * rotationSensitivity, 1);
                    vRotation = FastMath.interpolateLinear(vRotationLerpFactor, vRotation, targetVRotation);
                    if (targetVRotation + 0.01f >= vRotation && targetVRotation - 0.01f <= vRotation) {
                        vRotating = false;
                        vRotationLerpFactor = 0;
                    }
                }
                //computing the position
                computePosition();
                //setting the position at last
                cam.setLocation(pos);
            } else {
                //easy no smooth motion
                vRotation = targetVRotation;
                rotation = targetRotation;
                distance = targetDistance;
                computePosition();
                cam.setLocation(pos);
            }
            //keeping track on the previous position of the target
            prevPos = new Vector3f(target.getWorldTranslation());

            //the cam looks at the target
            cam.lookAt(target.getWorldTranslation(), initialUpVec);
        }
    }

    /**
     * Return the enabled/disabled state of the camera
     * @return true if the camera is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Enable or disable the camera
     * @param enabled true to enable
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            canRotate = false; // reset this flag in-case it was on before
        }
    }

    /**
     * Returns the max zoom distance of the camera (default is 40)
     * @return maxDistance
     */
    public float getMaxDistance() {
        return maxDistance;
    }

    /**
     * Sets the max zoom distance of the camera (default is 40)
     * @param maxDistance
     */
    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }

    /**
     * Returns the min zoom distance of the camera (default is 1)
     * @return minDistance
     */
    public float getMinDistance() {
        return minDistance;
    }

    /**
     * Sets the min zoom distance of the camera (default is 1)
     * @return minDistance
     */
    public void setMinDistance(float minDistance) {
        this.minDistance = minDistance;
    }

    /**
     * clone this camera for a spatial
     * @param spatial
     * @return
     */
    public Control cloneForSpatial(Spatial spatial) {
        ChaseCamera cc = new ChaseCamera(cam, spatial, inputManager);
        cc.setMaxDistance(getMaxDistance());
        cc.setMinDistance(getMinDistance());
        return cc;
    }

    /**
     * Sets the spacial for the camera control, should only be used internally
     * @param spatial
     */
    public void setSpatial(Spatial spatial) {
        target = spatial;
    }

    /**
     * update the camera control, should on ly be used internally
     * @param tpf
     */
    public void update(float tpf) {
        updateCamera(tpf);
    }

    /**
     * renders the camera control, should on ly be used internally
     * @param rm
     * @param vp
     */
    public void render(RenderManager rm, ViewPort vp) {
        //nothing to render
    }

    /**
     * Write the camera
     * @param ex the exporter
     * @throws IOException
     */
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(maxDistance, "maxDistance", 40);
        capsule.write(minDistance, "minDistance", 1);
    }

    /**
     * Read the camera
     * @param im
     * @throws IOException
     */
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        maxDistance = ic.readFloat("maxDistance", 40);
        minDistance = ic.readFloat("minDistance", 1);
    }

    /**
     *
     * @deprecated use getMaxVerticalRotation()
     */
    @Deprecated
    public float getMaxHeight() {
        return getMaxVerticalRotation();
    }

    /**
     *
     * @deprecated use setMaxVerticalRotation()
     */
    @Deprecated
    public void setMaxHeight(float maxHeight) {
        setMaxVerticalRotation(maxHeight);
    }

    /**
     *
     * @deprecated use getMinVerticalRotation()
     */
    @Deprecated
    public float getMinHeight() {
        return getMinVerticalRotation();
    }

    /**
     *
     * @deprecated use setMinVerticalRotation()
     */
    @Deprecated
    public void setMinHeight(float minHeight) {
        setMinVerticalRotation(minHeight);
    }

    /**
     * returns the maximal vertical rotation angle of the camera around the target
     * @return
     */
    public float getMaxVerticalRotation() {
        return maxVerticalRotation;
    }

    /**
     * sets the maximal vertical rotation angle of the camera around the target default is Pi/2;
     * @param maxVerticalRotation
     */
    public void setMaxVerticalRotation(float maxVerticalRotation) {
        this.maxVerticalRotation = maxVerticalRotation;
    }

    /**
     * returns the minimal vertical rotation angle of the camera around the target
     * @return
     */
    public float getMinVerticalRotation() {
        return minVerticalRotation;
    }

    /**
     * sets the minimal vertical rotation angle of the camera around the target default is 0;
     * @param minHeight
     */
    public void setMinVerticalRotation(float minHeight) {
        this.minVerticalRotation = minHeight;
    }

    /**
     * returns true is smmoth motion is enabled for this chase camera
     * @return
     */
    public boolean isSmoothMotion() {
        return smoothMotion;
    }

    /**
     * Enables smooth motion for this chase camera
     * @param smoothMotion
     */
    public void setSmoothMotion(boolean smoothMotion) {
        this.smoothMotion = smoothMotion;
    }

    /**
     * returns the chasing sensitivity
     * @return
     */
    public float getChasingSensitivity() {
        return chasingSensitivity;
    }

    /**
     * Sets the chasing sensitivity, the lower the value the slower the camera will go in the trail of the target when it moves
     * @param chasingSensitivity
     */
    public void setChasingSensitivity(float chasingSensitivity) {
        this.chasingSensitivity = chasingSensitivity;
    }

    /**
     * Returns the rotation sensitivity
     * @return
     */
    public float getRotationSensitivity() {
        return rotationSensitivity;
    }

    /**
     * Sets the rotation sensitivity, the lower the value the slower the camera will rotates around the target when draging with the mouse
     * default is 5
     * @param rotationSensitivity
     */
    public void setRotationSensitivity(float rotationSensitivity) {
        this.rotationSensitivity = rotationSensitivity;
    }

    /**
     * returns true if the trailing is enabled
     * @return
     */
    public boolean isTrailingEnabled() {
        return trailingEnabled;
    }

    /**
     * Enable the camera trailing : The camera smoothly go in the targets trail when it moves.
     * @param trailingEnabled
     */
    public void setTrailingEnabled(boolean trailingEnabled) {
        this.trailingEnabled = trailingEnabled;
    }

    /**
     * returns the trailing rotation inertia
     * @return
     */
    public float getTrailingRotationInertia() {
        return trailingRotationInertia;
    }

    /**
     * Sets the trailing rotation inertia : default is 0.1. This prevent the camera to roughtly stop when the target stops moving
     * before the camera reached the trail position.
     * @param trailingRotationInertia
     */
    public void setTrailingRotationInertia(float trailingRotationInertia) {
        this.trailingRotationInertia = trailingRotationInertia;
    }

    /**
     * returns the trailing sensitivity
     * @return
     */
    public float getTrailingSensitivity() {
        return trailingSensitivity;
    }

    /**
     * Sets the trailing sensitivity, the lower the value, the slower the camera will go in the target trail when it moves.
     * default is 0.5;
     * @param trailingSensitivity
     */
    public void setTrailingSensitivity(float trailingSensitivity) {
        this.trailingSensitivity = trailingSensitivity;
    }

    /**
     * returns the zoom sensitivity
     * @return
     */
    public float getZoomSensitivity() {
        return zoomSensitivity;
    }

    /**
     * Sets the zoom sensitivity, the lower the value, the slower the camera will zoom in and out.
     * default is 5.
     * @param zoomSensitivity
     */
    public void setZoomSensitivity(float zoomSensitivity) {
        this.zoomSensitivity = zoomSensitivity;
    }

    /**
     * Sets the default distance at start of applicaiton
     * @param defaultDistance
     */
    public void setDefaultDistance(float defaultDistance) {
        distance = defaultDistance;
        targetDistance = distance;
    }

    /**
     * sets the default horizontal rotation of the camera at start of the application
     * @param angle
     */
    public void setDefaultHorizontalRotation(float angle) {
        rotation = angle;
        targetRotation = angle;
    }

    /**
     * sets the current zoom distance for the chase camera
     * @param new distance
     */
    public void alterDistance(float alterBy) {
        this.zoomCamera(alterBy);
    }

    /**
     * sets the default vertical rotation of the camera at start of the application
     * @param angle
     */
    public void setDefaultVerticalRotation(float angle) {
        vRotation = angle;
        targetVRotation = angle;
    }

    public void collide() {
        LinkedList<PhysicsRayTestResult> testResults;
        testResults = (LinkedList) myPhysicsSpace.rayTest(target.getWorldTranslation(), maxPos);
        float hitFraction = 1f;
        if(testResults != null && testResults.size() > 0) {
            hitFraction = testResults.getFirst().getHitFraction();
        }
        targetDistance = ((float)((int)(hitFraction*100)))/100 * maxDistanceByZoom;
    }
}