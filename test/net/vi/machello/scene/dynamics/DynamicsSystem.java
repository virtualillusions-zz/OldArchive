/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vi.machello.scene.dynamics;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.jme3.app.Application;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.vi.machello.app.debug.DebugState;
import com.vi.machello.app.state.EntityAppState;
import com.vi.machello.renderer.cam.components.CameraSceneBoundsPiece;
import com.vi.machello.renderer.visual.components.InScenePiece;
import com.vi.machello.renderer.visual.control.SpatialControl;
import com.vi.machello.scene.dynamics.components.MovementPiece;
import com.vi.machello.scene.dynamics.components.PositionPiece;
import com.vi.machello.scene.dynamics.components.RigidBodyPiece;
import com.vi.machello.util.math.Bounds;
import com.vi.machello.util.math.MathUtil;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <b>Name:</b> DynamicsSystem<br/>
 *
 * <b>Purpose:</b> Detects Collision with scene and other players and moves
 * actors according<br/>
 *
 * <b>Description:</b> Keeps track of the list of collidable models and performs
 * collision checks. Uses simple radius threshold and AABB collision.
 * @deprecated 
 * @author Kyle D. Williams
 */
public class DynamicsSystem extends EntityAppState {

    @Override
    protected void AppState(Application app, EntityData ed) {
        //It only makes sense this is seperate so can get full list of moving component
        collisionSet = ed.getEntities(
                RigidBodyPiece.class,
                PositionPiece.class,
                MovementPiece.class,
                InScenePiece.class);
        collisionObjects = Lists.newArrayList();
    }

    @Override
    protected void enable() {
        root = getCoreState().getRootNode();
    }

    @Override
    protected void disable() {
        root = null;
    }

    @Override
    protected void cleanUp() {
        collisionSet.release();
        collisionSet.release();
        collisionSet = null;
        collisionSet = null;
    }

    @Override
    protected void appUpdate(float tpf) {
        //TODO Check collision set for basic needs ie is colliding or not on floor first 
        if (collisionSet.applyChanges()) {
            csb = getApplicationComponent(CameraSceneBoundsPiece.class);
            PopulateList:
            if (populateList()) {//Check if should skip calc or not
                positionCollision();
                //If everything goes according to plan then update PositionPiece
                updatePosition();
                //clear list so can start new
                collisionObjects.clear();
            }
        }
    }

    private boolean populateList() {
        boolean continueCollisionUpdate = false;
        //first reset targetBounds by setting min to max possible value and max to minimal possible value so they can be overwritten
        zBounds.set(Float.MAX_VALUE, Float.MIN_VALUE);
        //Record all relevant values
        //TODO CHECK TO SEE IF FALLING
        for (Entity e : collisionSet) {
            CollisionObject co = new CollisionObject(e);
            if (!MovementPiece.IDLE.equals(co.movement)) {
                continueCollisionUpdate = true;
            }
            //Finally add to temp list to check against on next lookup 
            //ONLY IF IT HAS BEEN CORRECTLY INITIALIZED
            if (co.isInitialized) {
                collisionObjects.add(co);
                //UPDATE Z BOUNDS IE FORWARD AND BACKWARD
                //Done up here so i can add update position to same loop
                float z = co.newPosition.getZ();
                zBounds.setIfBounds(z);
            }
        }
        //Max and min's should not be more than maxZDistance away from one another
        float max = zBounds.max;
        float min = zBounds.min;
        zBounds.min = max - (maxZDistance / 2f);
        zBounds.max = min + maxZDistance / 2f;
        return continueCollisionUpdate;
    }

    /**
     * TODO: Right now this is for everything however later should restrict it
     * to only entities that has some sort of collidable component to do this
     * just cast a loop call getGeometry and get the EntityControl to give the
     * colliding objects entity ID and from their normal check REMEMBER ALSO
     * USING THIS TO CLIP IMMOBILE ENTITIES AS WELL
     *
     * Need to do a check if their is ground to move forward
     *
     * Check to make sure ground in position moving isn't further than 1/4
     * character height if it is must jump to get down any further than that
     * think this out
     */
    private void positionCollision() {
        TempVars vars = TempVars.get();
        //its okay to remove components since checking against everything anyway
        for (Iterator<CollisionObject> it = collisionObjects.iterator(); it.hasNext();) {
            //Initialize Variables
            CollisionObject co = it.next();
            //IGNORE IDLE But don't throw away need to check
            if (MovementPiece.IDLE.equals(co.movement)) {
                continue;
            }
            checkBounds(co);
            Vector3f pos = vars.vect1;
            MathUtil.pieceToVec(co.position, pos);
            Vector3f newPos = vars.vect2;
            MathUtil.pieceToVec(co.newPosition, newPos);
            Vector3f mov = vars.vect3;
            MathUtil.pieceToVec(co.movement, mov);
            //Begin collision calculations    
            boolean collision = false;
            /**
             * First do a check to make sure another is not contesting the
             * position and we are not within its radius must use iterator so
             * safe to remove values
             */
            FirstCollisionTest:
            for (CollisionObject co2 : collisionObjects) {
                if (co.equals(co2)) {
                    continue;
                }
                //Initialize Variables
                Vector3f pos2 = vars.vect4;
                MathUtil.pieceToVec(co2.newPosition, pos2);
                //Calc Threshold
                float threshold = co.radius + co2.radius;
                threshold *= threshold;
                //Calc distance
                float distSq = newPos.distanceSquared(pos2);
                if (distSq < threshold) {
                    collision = true;
                    break;
                }
            }
            /**
             * next if no collision detected in previous test see if their is
             * visibly some object in the way. To get a wider scope cast a ray
             * set origin to new position and direction up and back toward
             * player. This assumes new Position isn't an unreasonable distance
             */
            SecondCollisionTest:
            if (collision == false) {
                //Point dir to head of where character will be at new position
                Vector3f direction = vars.vect4;
                direction.set(Vector3f.UNIT_Y);//calc vert            
                direction.addLocal(mov);//calc horizontal
                //Set then do a test
                ray.setOrigin(newPos);
                ray.setDirection(direction);
                //the limit should be the height to make sure character can clear
                ray.setLimit(co.height);//MATH IS FUN
                root.collideWith(ray, results);
                //Exclude self or rootnode from collision results
                for (Iterator<CollisionResult> crit = results.iterator(); crit.hasNext();) {
                    CollisionResult cr = crit.next();
                    Spatial g = cr.getGeometry();
                    if (g.equals(co.spat)
                            || g.equals(root)
                            || g.hasAncestor((Node) co.spat)) {
                        crit.remove();
                    }
                }
                //Okay to override collision value since looking 
                //for true and had to be false to get into this check
                collision = results.size() > 0;
                //Clear cache for next calc                
                results.clear();
            }

//System.out.println(co.spat.getName()+":\nCollision:"+collision+"\nCollision Bounds:"+csb+" -> MyX:"+x+"\nCamBounds:"+zBounds+" -> MyZ:"+z+"\n\n");
            FINALLY:
            if (collision == true) {
                it.remove();
            }
        }
        vars.release();
    }

    private void checkBounds(CollisionObject co) {
        TempVars vars = TempVars.get();
        //THIRD CHECK TEST MOVEMENT BOUNDARIES 
        //X is left and right on screen and is tested by physical camera bounds
        //Z is forward and backward on screen and dictates a character cannot be more than 20 units away from the max or min
        PositionPiece pp = co.newPosition;
        float x = pp.getX();
        float y = pp.getY();
        float z = pp.getZ();
        //ThirdCollisionTest://Check Camera Bounds Collision
//            if (x >= csb.getMaxBound()
//                    || x <= csb.getMinBound()
//                    || z >= zBounds.max
//                    || z <= zBounds.min) {
//                collision = true;
//                System.out.println("X-Bounds[" + (x < csb.getMaxBound()) + "," + (x > csb.getMinBound()) + "]");
//                System.out.println("Z-Bounds[" + (z < zBounds.max) + "," + (z > zBounds.min) + "]");
//            }
        boolean b;
        //Check X Bounds
        x = FastMath.clamp(x, csb.getMinBound(), csb.getMaxBound());
        b = x != pp.getX();
        //check Z Bounds
        z = FastMath.clamp(z, zBounds.min, zBounds.max);
        b = b || z != pp.getZ();
        //Check if on floor if not descent 
        //Set then do a test
        Vector3f origin = MathUtil.pieceToVec(co.newPosition, vars.vect1);
        Vector3f dir = vars.vect2.set(0, -1, 0);
        origin.add(0, -1, 0);
        ray.setOrigin(origin);
        ray.setDirection(dir);
        //the limit should be the height to make sure character can clear
        ray.setLimit(Float.MIN_VALUE);//MATH IS FUN
        root.collideWith(ray, results);
        System.out.println(results.size());
        DebugState.VisualVectorReference(root, getApp().getAssetManager(), origin, dir);
        if (results.size() == 0) {
            System.out.println("");
            y -= Float.MIN_VALUE;
            b=true;
        }
        results.clear();

        if (b == true) {
            PositionPiece op = co.position;
            PositionPiece np = new PositionPiece(x, y, z);
            co.newPosition = np;
            co.movement = new MovementPiece(
                    np.getX() - op.getX(),
                    np.getY() - op.getY(),
                    np.getZ() - op.getZ());
        }
        vars.release();
    }

    public void updatePosition() {
        //TODO: Think about making a syncronizedarraylist and having this loop on update while prev loop on a different thread
        for (CollisionObject co : collisionObjects) {
            if (MovementPiece.IDLE.equals(co.movement)) {
                continue;
            }
            getEntityData().setComponent(co.id, co.newPosition);
            //escape movement piece
            getEntityData().setComponent(co.id, MovementPiece.IDLE);
        }
    }
    private EntitySet collisionSet;
    /**
     * A list filled with entries of collision objects
     */
    private ArrayList<CollisionObject> collisionObjects;
    private final Ray ray = new Ray();
    private final CollisionResults results = new CollisionResults();
    private final Bounds zBounds = Bounds.create();
    private final float maxZDistance = 8f;//leave alone 8 is actually pretty big this is just enough for 4 characters side by side
    private CameraSceneBoundsPiece csb;
    private Node root;

    private class CollisionObject {

        public final boolean isInitialized;
        public final EntityId id;
        public final RigidBodyPiece rigidBodyPiece;
        public MovementPiece movement;
        public final PositionPiece position;
        public PositionPiece newPosition;
        public final Spatial spat;
        public final float radius;
        public final float height;

        public CollisionObject(Entity e) {
            this.id = e.getId();
            this.rigidBodyPiece = e.get(RigidBodyPiece.class);
            this.position = e.get(PositionPiece.class);
            this.movement = Objects.firstNonNull(
                    e.get(MovementPiece.class),
                    MovementPiece.IDLE);
            //Calculations
            this.newPosition = new PositionPiece(
                    position.getX() + movement.getX(),
                    position.getY() + movement.getY(),
                    position.getZ() + movement.getZ(),
                    movement.getDir());
            Spatial s = null;
            float r = 0;
            float h = 0;
            boolean i = false;
            try {
                /**
                 * TOO LAZY TO TEST IF CONTROL OR SPATIAL IS NULL JUST CATCH
                 * NULL POINTER EXCEPTION THEN SKIP LOOP ITERATION by setting
                 * initialized to false
                 */
                SpatialControl sic = getCoreState().getModelBindingMap().get(id);
                s = sic.getSpatial();
                r = sic.getRadius();
                h = sic.getHeight();
                i = true;
            } catch (NullPointerException ex) {
                //IGNORE EXCEPTION AWARE OF POSSIBLE ISSUE JUST SKIP LOOP ITERATION
            }
            this.spat = s;
            this.radius = r;
            this.height = h;
            this.isInitialized = i;
        }
    }
}
