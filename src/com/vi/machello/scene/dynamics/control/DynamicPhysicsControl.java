/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.scene.dynamics.control;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import com.vi.machello.app.control.BaseControl;
import java.io.IOException;
import java.util.List;

/**
 * TODO: make forces relative to size
 * 
 * TODO: FIGURE A WAY TO get onto edges without hopping
 *
 * TODO: Easy way to create gravity perpendicular to plane...HUH I DON'T
 * REMEMBER WHY I NEED THIS KEEP IN CASE REMEMBER
 *
 * @author Kyle D. Williams
 */
public class DynamicPhysicsControl extends BaseControl implements PhysicsControl, PhysicsTickListener, PhysicsCollisionListener {

    public DynamicPhysicsControl() {
    }

    public DynamicPhysicsControl(PhysicsSpace space) {
        physicsSpace = space;
    }

////////////////////Spatial.setSpatial(Spatial spat)/////////////////////////
    @Override
    public void BaseControl() {
        calculateShapes();
        rigidBody = new PhysicsRigidBody(getShape(), mass);
        rigidBody.setAngularFactor(0);

        //Set Final Position and Orientation
        rigidBody.setUserObject(spatial);
        setPhysicsLocation(getSpatialTranslation());
        setPhysicsRotation(getSpatialRotation());

        //Attach to physicsSpace if its available
        enable();

        log.trace("Created Collision Object/s for {0}", spatial.getName());
    }

    @Override
    public void cleanup() {
        super.cleanup();
        physicsSpace.remove(this);
        physicsSpace = null;
        rigidBody.setUserObject(null);
        regShape = null;
        duckShape = null;
    }

    @Override
    protected void enable() {
        super.enable();
        if (physicsSpace != null) {
            if (isEnabled && !addedToPhysicsSpace) {
                if (spatial != null) {
                    setPhysicsLocation(getSpatialTranslation());
                    setPhysicsRotation(getSpatialRotation());
                }
                addPhysics(physicsSpace);
                addedToPhysicsSpace = true;
            }
        }
    }

    @Override
    protected void disable() {
        super.disable();
        if (physicsSpace != null) {
            if (!isEnabled && addedToPhysicsSpace) {
                removePhysics(physicsSpace);
                addedToPhysicsSpace = false;
            }
        }
    }

/////////////////////////PhysicsSpace.add(Object o)/////////////////////////
    @Override
    public PhysicsSpace getPhysicsSpace() {
        return physicsSpace;
    }

    @Override
    public void setPhysicsSpace(PhysicsSpace space) {
        if (space == null) {
            if (physicsSpace != null) {
                removePhysics(physicsSpace);
                addedToPhysicsSpace = false;
            }
        } else {
            if (physicsSpace == space) {
                return;
            } else if (physicsSpace != null) {
                removePhysics(physicsSpace);
            }
            addPhysics(space);
            addedToPhysicsSpace = true;
        }
        physicsSpace = space;
    }

    /**
     * Called when the physics object is supposed to add all objects it needs to
     * manage to the physics space.
     *
     * @param space
     */
    public void addPhysics(PhysicsSpace space) {
        space.getGravity(localUp).normalizeLocal().negateLocal();
        updateLocalCoordinateSystem();

        space.addCollisionObject(rigidBody);
        space.addTickListener(this);
        space.addCollisionListener(this);
    }

    /**
     * Called when the physics object is supposed to remove all objects
     * addedToPhysicsSpace to the physics space.
     *
     * @param space
     */
    public void removePhysics(PhysicsSpace space) {
        space.removeCollisionObject(rigidBody);
        space.removeTickListener(this);
        space.removeCollisionListener(this);
    }

////////////////////////////////COLLISION/////////////////////////
    @Override
    public void collision(PhysicsCollisionEvent event) {
    }
//////////////////////////////////////INPUT/////////////////////////////

    public void actionForward(float value) {
        directionForward = bindMoveVal(value);
    }

    public void actionLeft(float value) {
        directionLeft = bindMoveVal(value);
    }

    /**
     * Used to bind the input directional value between 0.5f and 1f
     *
     * @param value
     * @return
     */
    protected float bindMoveVal(float value) {
        int sign = (int) FastMath.sign(value);
        value = FastMath.abs(value);
        value = value >= .5f ? joggingSpeed : walkingSpeed;
        return sign * value;
    }

    public void actionJump() {
        jump = true;
    }

    /**
     * Toggle character ducking. When ducked the characters capsule collision
     * shape height will be multiplied by duckedFactor to make the capsule
     * smaller. When unducking, the character will check with a ray test if it
     * can in fact unduck and only do so when its possible. You can check the
     * state of the unducking by checking isDucked().
     *
     * @param duck
     */
    public void actionDuck(boolean duck) {
        if (duck) {
            wantToUnDuck = false;
            updateCollisionShape(true);
        } else {
            if (checkCanUnDuck()) {
                updateCollisionShape(false);
            } else {
                wantToUnDuck = true;
            }
        }
    }

    public void actionDash() {
        calculateDashForce(DashDirection.FORWARD);
    }

    public void actionEvade() {
        calculateDashForce(DashDirection.BACKWARD);
    }

    public void actionEvadeLeft() {
        calculateDashForce(DashDirection.LEFT);
    }

    public void actionEvadeRight() {
        calculateDashForce(DashDirection.RIGHT);
    }
//////////////////////////////////////COMPUTATION/////////////////////////////    

    @Override
    protected void controlUpdate(float tpf) {
        super.controlUpdate(tpf);
        rigidBody.getPhysicsLocation(rigidBody_location);
        //rotation has been set through viewDirection
        //applyTransforms
        //Updates Spatial transform to coincide with the parameters
        if (isEnabled && spatial != null) {
            Vector3f localLocation = spatial.getLocalTranslation();
            Quaternion localRotationQuat = spatial.getLocalRotation();
            if (!applyLocal && spatial.getParent() != null) {
                localLocation.set(rigidBody_location).subtractLocal(spatial.getParent().getWorldTranslation());
                localLocation.divideLocal(spatial.getParent().getWorldScale());
                localRotationQuat.set(rotation);

                spatial.setLocalTranslation(localLocation);
                spatial.setLocalRotation(localRotationQuat);
            } else {
                spatial.setLocalTranslation(rigidBody_location);
                spatial.setLocalRotation(rotation);
            }
        }
    }

    /**
     * Called before the physics is actually stepped, use to apply forces etc.
     *
     * @param space the physics space
     * @param tpf the time per frame in seconds
     */
    @Override
    public void prePhysicsTick(PhysicsSpace space, float tpf) {
        isOnGround();//Test to see if on ground before doing anything else

        if (wantToUnDuck && checkCanUnDuck()) {
            wantToUnDuck = false;
            updateCollisionShape(false);
        }
        activePhysicsTick(tpf);
        passivePhysicsTick(tpf);
    }

    /**
     * Called after the physics has been stepped, use to check for forces etc.
     *
     * @param space the physics space
     * @param tpf the time per frame in seconds
     */
    @Override
    public void physicsTick(PhysicsSpace space, float tpf) {
        rigidBody.getLinearVelocity(velocity);
    }

    /**
     * Perform computations based on user input
     */
    protected void activePhysicsTick(float tpf) {
        TempVars vars = TempVars.get();
        Vector3f tempWlkDir = vars.vect1.zero();//make seperate vector to allow interpolation
        //Used later on to create smooth transitions speeding up or slowing down
        interpolationScalar = 0.1f;
        if (!dashVelocity.equals(Vector3f.ZERO)) {
            interpolationScalar = 0.25f;
            dashVelocity.interpolateLocal(Vector3f.ZERO, interpolationScalar);
            if (dashVelocity.length() < interpolationScalar) {
                dashVelocity.zero();
                interpolationScalar = 1f;
                dashCount = 0;
            }
            tempWlkDir.set(dashVelocity);
        } else //BASIC_MOVEMENT
        if (directionForward != 0 || directionLeft != 0) {
            if (directionForward != 0) {
                Vector3f tempCamDirection = vars.vect2.set(walkDirection);
                tempCamDirection.normalizeLocal();
                calculateWalkDirection(directionForward, tempCamDirection,
                        tempWlkDir, localUp);
            }
            if (directionLeft != 0) {
                Vector3f tempCamLeft = vars.vect2.set(localUp);
                tempCamLeft.crossLocal(walkDirection).normalizeLocal();
                calculateWalkDirection(directionLeft, tempCamLeft,
                        tempWlkDir, localUp);
            }
            directionForward = FastMath.abs(directionForward);
            directionLeft = FastMath.abs(directionLeft);
            float walkSpeed = directionForward > directionLeft ? directionForward : directionLeft;
            float tempVal = isRunning == true ? runningSpeed : walkSpeed;
            tempWlkDir.multLocal(BASIC_MAX_SPEED * tempVal);
            directionForward = 0;
            directionLeft = 0;
        }


        //This is addedToPhysicsSpace to allow scalar to remain at 0.01f to help with turning
        //and set here to prevent prolonged stop
        if (tempWlkDir.equals(Vector3f.ZERO)) {
            interpolationScalar = 1f;
        }
        //Interpolate current walk direction to new one to prevent sparatic movement 
        walkVelocity.interpolateLocal(tempWlkDir, interpolationScalar);
        vars.release();
    }

    /**
     * Perform basic computation needed to correctly update spatial physics
     */
    private void passivePhysicsTick(float tpf) {
        TempVars vars = TempVars.get();
        // dampen existing left/forward forces
        float existingLeftVelocity = velocity.dot(localLeft);
        float existingForwardVelocity = velocity.dot(localForward);
        Vector3f counter = vars.vect1;
        existingLeftVelocity = existingLeftVelocity * physicsDamping;
        existingForwardVelocity = existingForwardVelocity * physicsDamping;
        //counter.set(-existingLeftVelocity, 0, -existingForwardVelocity);
        //NO ABOVE ASSUMES X/Z BE SAFE IN CASE LOCAL LEFT CAN CHANGE
        Vector3f counterLocalLeft = vars.vect2.set(localLeft);
        counterLocalLeft.multLocal(-existingLeftVelocity);
        Vector3f counterLocalForward = vars.vect3.set(localForward);
        counterLocalForward.multLocal(-existingForwardVelocity);
        counter.set(counterLocalLeft).addLocal(counterLocalForward);
        ////FINALLY
        localForwardRotation.multLocal(counter);
        velocity.addLocal(counter);

        //CALCULATE WALK DIRECTION/////////
        float designatedVelocity = walkVelocity.length();
        if (designatedVelocity > 0) {
            Vector3f localWalkDirection = vars.vect1;
            //normalize walkdirection
            localWalkDirection.set(walkVelocity).normalizeLocal();
            //check for the existing velocity in the desired direction
            float existingVelocity = velocity.dot(localWalkDirection);
            //calculate the final velocity in the desired direction
            float finalVelocity = designatedVelocity - existingVelocity;
            localWalkDirection.multLocal(finalVelocity);
            //instananeously set view direction
            Vector3f localViewDirection = vars.vect2;
            localViewDirection.set(viewDirection);
            if (dashVelocity.equals(Vector3f.ZERO)) {
                localViewDirection.interpolateLocal(localWalkDirection, 2 * interpolationScalar);
            }
            setViewDirection(localViewDirection);
            //add resulting vector to existing velocity
            velocity.addLocal(localWalkDirection);
        }

        if (onGround || !dashVelocity.equals(Vector3f.ZERO)) {
            //APPLY VELOCITY WHICH ALLOWS THE CHARACTER TO WALK
            rigidBody.setLinearVelocity(velocity);
        }

        if (jump) {
            if (onGround) {
                jumpCount = 0;
            }
            //first jump must be on floor
            boolean checkOne = jumpCount > 0 || jumpCount == 0 && onGround;
            //jump count cannot exceed max jump count allowed
            boolean checkTwo = jumpCount < maxJumpCount;
            if (checkOne && checkTwo) {
                //TODO: precalculate jump force
                Vector3f rotatedJumpForce = vars.vect1.set(localUp);
                rotatedJumpForce.multLocal(actionImpulse);
                rigidBody.applyImpulse(localForwardRotation.multLocal(rotatedJumpForce), Vector3f.ZERO);
                jumpCount++;
            }
            jump = false;
        }
        //TODO: THIS CAUSES ISSUE WHEN CALLING ISFALLING 
        //POSSIBILITY TO RETURN FALSE NEGATIVES DUE TO INCONSISTENT UPDATE
        //final cached values        
        //Calculate new Airtime here to avoid issue
        airTime = onGround ? 0 : airTime + tpf;
        isInAir = airTime > .03f;
        //determine if object is falling using its current and previous position       
        Vector3f tempVec1 = vars.vect1.set(localUp).multLocal(rigidBody_location);
        float length = tempVec1.length();
        isFalling = isInAir && (length < (altitude));
        isRising = isInAir && (length > (altitude));
        //calculate new altitute, used specifically to check if jumping or falling
        altitude = onGround ? 0 : length;
        vars.release();
    }

////////////////////////////////CALCULATIONS//////////////////////
    /*
     * Create new character collision shapes with parameters based on boundingbox
     * Note: ideally their should be a more advanced compoundshape similar to KinemeticRagDoll
     */
    private void calculateShapes() {
        BoundingBox bb = (BoundingBox) spatial.getWorldBound();
        //use smaller side extent over larger side to create a more narrow hitbox 
        //and because A-Pose and T-Pose difference greatly impacts value
        radius = bb.getXExtent() < bb.getZExtent() ? bb.getXExtent() : bb.getZExtent();
        radius /= 2;
        originalHeight = bb.getYExtent() * 2;
        mass = 1 * bb.getVolume();//random
        actionImpulse = mass * 5;
        //create collision shapes and wrap them
        //in compound shape to help with offset
        CompoundCollisionShape ccs = new CompoundCollisionShape();
        regShape = new CapsuleCollisionShape(radius, originalHeight - (2 * radius));//standing     
        ccs.addChildShape(regShape, new Vector3f(0, originalHeight / 2.0f, 0));
        regShape = ccs;

        ccs = new CompoundCollisionShape();
        duckShape = new CapsuleCollisionShape(radius, ((originalHeight * duckedFactor) - (2 * radius)));//ducked    
        ccs.addChildShape(duckShape, new Vector3f(0, (originalHeight * duckedFactor) / 2.0f, 0));
        duckShape = ccs;
    }

    /**
     * This method works similar to Camera.lookAt but where lookAt sets the
     * priority on the direction, this method sets the priority on the up vector
     * so that the result direction vector and rotation is guaranteed to be
     * perpendicular to the up vector.
     *
     * NOTE: ABILITY TO ALTER VIEW DIRECTION
     *
     * @param rotation The rotation to set the result on or null to create a new
     * Quaternion, this will be set to the new "z-forward" rotation if not null
     * @param direction The direction to base the new look direction on, will be
     * set to the new direction
     * @param worldUpVector The up vector to use, the result direction will be
     * perpendicular to this
     * @return
     */
    private void calculateNewForward(Quaternion rotation, Vector3f direction, Vector3f worldUpVector) {
        if (direction == null) {
            return;
        }
        TempVars vars = TempVars.get();
        Vector3f newLeft = vars.vect1;
        Vector3f newLeftNegate = vars.vect2;

        newLeft.set(worldUpVector).crossLocal(direction).normalizeLocal();
        if (newLeft.equals(Vector3f.ZERO)) {
            if (direction.x != 0) {
                newLeft.set(direction.y, -direction.x, 0f).normalizeLocal();
            } else {
                newLeft.set(0f, direction.z, -direction.y).normalizeLocal();
            }
            log.trace("Zero left for direction {0}, up {1}",
                    new Object[]{direction, worldUpVector});
        }
        newLeftNegate.set(newLeft).negateLocal();
        direction.set(worldUpVector).crossLocal(newLeftNegate).normalizeLocal();
        if (direction.equals(Vector3f.ZERO)) {
            direction.set(Vector3f.UNIT_Z);
            log.trace("Zero left for left {0}, up {1}",
                    new Object[]{newLeft, worldUpVector});
        }
        if (rotation != null) {
            rotation.fromAxes(newLeft, worldUpVector, direction);
        }
        vars.release();
    }

    /**
     * Updates the local x/z-flattened view direction and the corresponding
     * rotation quaternion for the spatial.
     */
    private void updateLocalViewDirection() {
        //update local rotation quaternion to use for view rotation
        localForwardRotation.multLocal(rotatedViewDirection.set(viewDirection));
        calculateNewForward(rotation, rotatedViewDirection, localUp);
    }

    /**
     * This method works similar to Camera.lookAt but where lookAt sets the
     * priority on the direction, this method sets the priority on the up vector
     * so that the result direction vector and rotation is guaranteed to be
     * perpendicular to the up vector.
     *
     * A function to aid in the walking vector takes the direction desired and
     * if the integer value is negative the vector is negated
     *
     * @param direction float
     * @param directionVector The direction to base the new look direction on,
     * will be set to the new direction
     * @param store A vector to store the result
     */
    private void calculateWalkDirection(float direction, Vector3f directionVector,
            Vector3f store, Vector3f worldUpVector) {
        //calculateNewForward(rotation, directionVector, worldUpVector);
        //negate if direction negative indicating opposite direction
        if (direction < 0.0f) {
            directionVector.negateLocal();
        }
        //finally add to store value...adding allows diagonal
        store.addLocal(directionVector);
    }

    /**
     * Nudges the character forward, backward or backward and to a side
     *
     * @param dir
     */
    protected void calculateDashForce(DashDirection dir) {
        if (dashCount >= maxDashCount) {
            return;
        }
        switch (dir) {
            case FORWARD:
                dashVelocity.set(walkDirection);
                //
                viewDirection.set(dashVelocity);
                break;
            case BACKWARD:
                dashVelocity.set(walkDirection);
                dashVelocity.negateLocal();
                //
                viewDirection.set(dashVelocity).negateLocal();
                break;
            case LEFT:
                dashVelocity.set(walkDirection);
                dashVelocity.negateLocal();
                //
                Vector3f temp = new Vector3f();
                temp.set(localUp);
                temp.crossLocal(walkDirection);
                dashVelocity.addLocal(temp);
                //
                viewDirection.set(dashVelocity).negateLocal();
                break;
            case RIGHT:
                dashVelocity.set(walkDirection);
                dashVelocity.negateLocal();
                //
                Vector3f temp2 = new Vector3f();
                temp2.set(localUp);
                temp2.crossLocal(walkDirection);
                temp2.negateLocal();
                dashVelocity.addLocal(temp2);
                //
                viewDirection.set(dashVelocity).negateLocal();
                break;
        }
        //THIS WILL EFFECT THE DIRECTION IT IS LOOKING AT
        //calculateNewForward(rotation, dashVelocity, localUp);
        float area = (2 * radius) * 55;//RANDOMLY SELECTED VALUE SEEMS NICE SHOULD ADD SCALING FACTOR LATER
        dashVelocity.multLocal(area);
        localForwardRotation.multLocal(dashVelocity);
        dashCount++;
    }

    /**
     * Check if the character is on the ground. This is determined by a ray test
     * in the center of the character and might return false even if the
     * character is not falling yet.
     *
     * @return
     */
    public void isOnGround() {
        TempVars vars = TempVars.get();
        Vector3f location = vars.vect1;
        Vector3f rayVector = vars.vect2;
        location.set(localUp).multLocal(getCurrentHeight()).addLocal(this.rigidBody_location);
        //values chosen after testing
        float multBy = -getCurrentHeight() - FastMath.ZERO_TOLERANCE;
        rayVector.set(localUp).multLocal(multBy).addLocal(location);
        List<PhysicsRayTestResult> results = physicsSpace.rayTest(location, rayVector);
        vars.release();
        for (PhysicsRayTestResult physicsRayTestResult : results) {
            if (!physicsRayTestResult.getCollisionObject().equals(rigidBody)) {
                onGround = true;
                return;
            }
        }
        onGround = false;
    }

    /**
     * This checks if the character can go from ducked to unducked state by
     * doing a ray test.
     */
    protected boolean checkCanUnDuck() {
        TempVars vars = TempVars.get();
        Vector3f location = vars.vect1;
        Vector3f rayVector = vars.vect2;
        location.set(localUp).multLocal(FastMath.ZERO_TOLERANCE).addLocal(this.rigidBody_location);
        rayVector.set(localUp).multLocal(originalHeight + FastMath.ZERO_TOLERANCE).addLocal(location);
        List<PhysicsRayTestResult> results = physicsSpace.rayTest(location, rayVector);
        vars.release();
        for (PhysicsRayTestResult physicsRayTestResult : results) {
            if (!physicsRayTestResult.getCollisionObject().equals(rigidBody)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This actually sets a new collision shape to the character to change the
     * height of the capsule.
     *
     * @param isDucked
     */
    protected void updateCollisionShape(boolean isDucked) {
        ducked = isDucked;
        rigidBody.setCollisionShape(getShape());
    }

    /**
     * Updates the local coordinate system from the localForward and localUp
     * vectors, adapts localForward, sets localForwardRotation quaternion to
     * local z-forward rotation.
     */
    private void updateLocalCoordinateSystem() {
        //gravity vector has possibly changed, calculate new world forward (UNIT_Z)
        calculateNewForward(localForwardRotation, localForward, localUp);
        localLeft.set(localUp).crossLocal(localForward);
        rigidBody.setPhysicsRotation(localForwardRotation);
        updateLocalViewDirection();
    }
///////////////////////////////GETTERS/SETTERS/////////////////////

    public CollisionShape getShape() {
        //TODO one figured out why changing shapes causes issue return to single line method
        CollisionShape cs = new CapsuleCollisionShape(radius, getCurrentHeight() - (2 * radius));//standing     
        CompoundCollisionShape ccs = new CompoundCollisionShape();
        Vector3f addLocation = new Vector3f(0, getCurrentHeight() / 2, 0);
        ccs.addChildShape(cs, addLocation);
        return ccs;
        //return ducked == false ? regShape : duckShape;
    }

    /**
     * This returns the current positive Y extent of the capsule shape
     *
     * @return
     */
    private float getCurrentHeight() {
        return ducked == true ? this.originalHeight * duckedFactor : this.originalHeight;
    }

    /**
     * Sets the view direction for the character. Note this only defines the
     * rotation of the spatial in the local x/z plane of the character.
     *
     * @param vec
     */
    public void setViewDirection(Vector3f vec) {
        viewDirection.set(vec);
        updateLocalViewDirection();
    }

    public boolean isInAir() {
        return isInAir;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public boolean isRising() {
        return isRising;
    }

    /**
     * Realign the local forward vector to given direction vector, if null is
     * supplied Vector3f.UNIT_Z is used. Input vector has to be perpendicular to
     * current gravity vector. This normally only needs to be called when the
     * gravity direction changed continuously and the local forward vector is
     * off due to drift. E.g. after walking around on a sphere "planet" for a
     * while and then going back to a y-up coordinate system the local z-forward
     * might not be 100% aligned with Z axis.
     *
     * @param vec The new forward vector, has to be perpendicular to the current
     * gravity vector!
     */
    public void resetForward(Vector3f vec) {
        if (vec == null) {
            vec = Vector3f.UNIT_Z;
        }
        localForward.set(vec);
        updateLocalCoordinateSystem();
    }

    /**
     * Get the current linear velocity along the three axes of the character.
     * This is prepresented in world coordinates, parent coordinates when the
     * control is set to applyLocalPhysics.
     *
     * @return The current linear velocity of the character
     */
    public Vector3f getVelocity() {
        return velocity;
    }

    /**
     * Set the gravity for this character. Note that this also realigns the
     * local coordinate system of the character so that continuous changes in
     * gravity direction are possible while maintaining a sensible control over
     * the character.
     *
     * @param gravity
     */
    public void setGravity(Vector3f gravity) {
        rigidBody.setGravity(gravity);
        localUp.set(gravity).normalizeLocal().negateLocal();
        updateLocalCoordinateSystem();
    }

    /**
     * Sets how much the physics forces in the local x/z plane should be
     * dampened.
     *
     * @param physicsDamping The dampening value, 0 = no dampening, 1 = no
     * external force, default = 0.9
     */
    public void setPhysicsDamping(float physicsDamping) {
        this.physicsDamping = physicsDamping;
    }

    /**
     * Gets how much the physics forces in the local x/z plane should be
     * dampened.
     */
    public float getPhysicsDamping() {
        return physicsDamping;
    }

    public void clearForces() {
        rigidBody.clearForces();
        jump = false;
        dashVelocity.zero();
        jumpCount = 0;
        dashCount = 0;
    }

    /**
     * Called when the physics object is supposed to move to the spatial
     * position.
     *
     * @param vec
     */
    public void setPhysicsLocation(Vector3f vec) {
        rigidBody.setPhysicsLocation(vec);
        rigidBody_location.set(vec);
    }

    /**
     * Called when the physics object is supposed to move to the spatial
     * rotation.
     *
     *
     * We don't set the actual physics rotation but the view rotation here. It
     * might actually be altered by the calculateNewForward method.
     *
     * @param quat
     */
    protected void setPhysicsRotation(Quaternion quat) {
        rotation.set(quat);
        rotation.multLocal(rotatedViewDirection.set(viewDirection));
        updateLocalViewDirection();
    }

    public boolean isApplyPhysicsLocal() {
        return applyLocal;
    }

    /**
     * When set to true, the physics coordinates will be applied to the local
     * translation of the Spatial
     *
     * @param applyPhysicsLocal
     */
    public void setApplyPhysicsLocal(boolean applyPhysicsLocal) {
        applyLocal = applyPhysicsLocal;
    }

    protected Vector3f getSpatialTranslation() {
        if (applyLocal) {
            return spatial.getLocalTranslation();
        }
        return spatial.getWorldTranslation();
    }

    protected Quaternion getSpatialRotation() {
        if (applyLocal) {
            return spatial.getLocalRotation();
        }
        return spatial.getWorldRotation();
    }

    public void setWalkDirection(Vector3f direction) {
        walkDirection.set(direction);
    }

    public void setWalkVelocity(Vector3f velocity) {
        walkVelocity.set(velocity);
    }

    /**
     *
     * @return true if object is in an expediated state
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     *
     * @param isRunning
     */
    public void setRun(boolean run) {
        this.isRunning = run;
    }

    public void setActionImpulse(float actionImpulse) {
        this.actionImpulse = actionImpulse;
    }

    public float getActionImpulse() {
        return actionImpulse;
    }

    public int getMaxDashCount() {
        return maxDashCount;
    }

    public void setMaxDashCount(int maxDashCount) {
        this.maxDashCount = maxDashCount;
    }

    public int getMaxJumpCount() {
        return maxJumpCount;
    }

    public void setMaxJumpCount(int maxJumpCount) {
        this.maxJumpCount = maxJumpCount;
    }
/////////////////////////////VARIABLES///////////////////////
    protected PhysicsSpace physicsSpace;
    protected boolean addedToPhysicsSpace = false;
    protected boolean applyLocal = false;
    protected final Vector3f dashVelocity = Vector3f.ZERO.clone();
    protected final Vector3f walkVelocity = Vector3f.ZERO.clone();
    protected final Vector3f walkDirection = Vector3f.UNIT_Z.clone();
    protected boolean isRunning = false;
    protected float physicsDamping = 0.9f;
    private final int BASIC_MAX_SPEED = 25;
    private final float runningSpeed = 2.00f;
    private final float joggingSpeed = 1.0f;
    private final float walkingSpeed = 0.25f;
    private float airTime = 0.0f;
    private float altitude;//calculate altitute, used specifically to check if jumping or falling
    private boolean isInAir = false;
    private boolean isFalling = false;
    private boolean isRising = false;
    private float directionForward = 0;
    private float directionLeft = 0;
    private boolean jump = false;
    private PhysicsRigidBody rigidBody;
    private float originalHeight;
    private float radius;
    private float mass;
    private final float duckedFactor = 0.6f;//NOTE: (0.4 < duckedFactor < 1) in order to prevent jitter
    private int maxJumpCount = 1;
    private int jumpCount = 0;
    private int maxDashCount = 1;
    private int dashCount = 0;
    /**
     * Used to determine the amount of force used when applying an impulse such
     * as dash,jump or evade
     */
    private float actionImpulse = 0;
    /**
     * Used to interpolate and coordinate viewDirection and walkVelocity vectors
     */
    private float interpolationScalar = 0.3f;
    /**
     * Local up direction, derived from gravity.
     */
    private final Vector3f localUp = new Vector3f(0, 1, 0);
    /**
     * Local absolute z-forward direction, derived from gravity and UNIT_Z,
     * updated continuously when gravity changes.
     */
    private final Vector3f localForward = new Vector3f(0, 0, 1);
    /**
     * Local left direction, derived from up and forward.
     */
    private final Vector3f localLeft = new Vector3f(1, 0, 0);
    /**
     * Local z-forward quaternion for the "local absolute" z-forward direction.
     */
    private final Quaternion localForwardRotation = new Quaternion(Quaternion.DIRECTION_Z);
    /**
     * Is a z-forward vector based on the view direction and the current local
     * x/z plane.
     */
    private final Vector3f viewDirection = new Vector3f(0, 0, 1);
    /**
     * Stores final spatial location, corresponds to RigidBody location.
     */
    private final Vector3f rigidBody_location = new Vector3f();
    /**
     * Stores final spatial rotation, is a z-forward rotation based on the view
     * direction and the current local x/z plane. See also rotatedViewDirection.
     */
    private final Quaternion rotation = new Quaternion(Quaternion.DIRECTION_Z);
    /**
     * Used for view direction computation
     */
    private final Vector3f rotatedViewDirection = new Vector3f(0, 0, 1);
    private final Vector3f velocity = new Vector3f();
    private boolean ducked = false;
    private boolean onGround = false;
    private boolean wantToUnDuck = false;
    protected CollisionShape regShape;
    protected CollisionShape duckShape;
///////////////////////////////////MISC/////////////////////////////////////////

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(isEnabled, "enabled", true);
        oc.write(applyLocal, "applyLocalPhysics", false);
        oc.write(spatial, "spatial", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        isEnabled = ic.readBoolean("enabled", true);
        spatial = (Spatial) ic.readSavable("spatial", null);
        applyLocal = ic.readBoolean("applyLocalPhysics", false);
    }

    protected static enum DashDirection {

        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT;
    }
}
