/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vza.director;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsSpace.BroadphaseType;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;

import depreciated.collision.CollisionNode;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
/**
 * This Director handles all collision within this game world
 * @author Kyle Williams
 */
public final class PhysicsDirector implements com.jme3.app.state.AppState,com.jme3.bullet.PhysicsTickListener{

    private boolean initialized = false;
    private com.jme3.scene.Node platforms;
    private java.util.ArrayList<CollisionNode> models;


    @Override
    public void initialize(AppStateManager stateManager, Application app) {
            initialized=true;
        //////////////Basic Physics Implimentation/////////////
        //start physics thread(pool)
            startPhysicsOnExecutor();           
       //////////////AppState Implimentation/////////////
            com.vza.app.VezlaApplication vapp = (com.vza.app.VezlaApplication)app;
            //This gets all of the models attached to modelNode and adds it to the models HashMap
            models = new java.util.ArrayList<CollisionNode>();
    }

    public void addModel(CollisionNode model){models.add(model);}
    public void removeModel(CollisionNode model){models.remove(model);}
    public void updateScene(){

    }

    /**
     * This updates all Collision Logic
     * @param tpf
     */
    @Override
    public void update(float tpf) {

    }




    @Override
    public void stateAttached(AppStateManager stateManager) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    @Override
    public void render(RenderManager rm) {
    //    throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isInitialized() {return initialized; }

    @Override
    public void cleanup() {
        if(executor!=null){
            executor.shutdown();
            executor=null;
        }
        initialized=false;
    }

    
    //////////////Basic Physics Implimentation/////////////
    /**
     * This updated function is a special update queue for physics based implimentations
     * @param tpf
     */
    public void simplePhysicsUpdate(float tpf) {
        //TODO: figure a way to get this into update remember their is a class physicsUpdater at the bottom
    }
    
    protected ScheduledThreadPoolExecutor executor;
    private PhysicsSpace pSpace;
    public BroadphaseType broadphaseType = BroadphaseType.DBVT;
    public Vector3f worldMin = new Vector3f(-10000f, -10000f, -10000f);
    public Vector3f worldMax = new Vector3f(10000f, 10000f, 10000f);
    public long detachedPhysicsLastUpdate = 0;


    private boolean startPhysicsOnExecutor() {
        if (executor != null) {
            executor.shutdown();
        }
        executor = new ScheduledThreadPoolExecutor(1);
        final PhysicsDirector app=this;
        Callable<Boolean> call = new Callable<Boolean>() {
            public Boolean call() throws Exception {
                detachedPhysicsLastUpdate = System.currentTimeMillis();
                pSpace = new PhysicsSpace(worldMin, worldMax, broadphaseType);
                pSpace.addTickListener(app);
                return true;
            }
        };
        try {
            return executor.submit(call).get();
        } catch (InterruptedException ex) {
            com.vza.app.VezlaApplication.logger.log(Level.SEVERE, null, ex);
            return false;
        } catch (ExecutionException ex) {
            com.vza.app.VezlaApplication.logger.log(Level.SEVERE, null, ex);
            return false;
        }
    }
    private Callable<Boolean> parallelPhysicsUpdate = new Callable<Boolean>() {

        public Boolean call() throws Exception {
            pSpace.update(Director.getApp().getTimer().getTimePerFrame() * Director.getApp().getSpeed());
            return true;
        }
    };

    private Callable<Boolean> detachedPhysicsUpdate = new Callable<Boolean>() {

        public Boolean call() throws Exception {
            pSpace.update(getPhysicsSpace().getAccuracy() * Director.getApp().getSpeed());
            long update = System.currentTimeMillis() - detachedPhysicsLastUpdate;
            detachedPhysicsLastUpdate = System.currentTimeMillis();
            executor.schedule(detachedPhysicsUpdate, Math.round(getPhysicsSpace().getAccuracy() * 1000000.0f) - (update * 1000), TimeUnit.MICROSECONDS);
            return true;
        }
    };

    public PhysicsSpace getPhysicsSpace() {
        return pSpace;
    }

    public void physicsTick(PhysicsSpace space, float f) {
        simplePhysicsUpdate(f);
    }

    public void physicsUpdate(float tpf){     
            //submit physics update
            Future physicsFuture = executor.submit(parallelPhysicsUpdate);
            //render frame concurrently
            Director.getApp().getRenderManager().render(tpf);
            Director.getApp().vezlaRender(Director.getApp().getRenderManager());
            try {
                physicsFuture.get();
            } catch (InterruptedException ex) {
                com.vza.app.VezlaApplication.logger.log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                com.vza.app.VezlaApplication.logger.log(Level.SEVERE, null, ex);
            }        
    }

    public void setBroadphaseType(BroadphaseType broadphaseType) {
        this.broadphaseType = broadphaseType;
    }

    public void setWorldMin(Vector3f worldMin) {
        this.worldMin = worldMin;
    }

    public void setWorldMax(Vector3f worldMax) {
        this.worldMax = worldMax;
    }

    public abstract interface physicsUpdater{
        public abstract void physicsUpdate();
    }
}