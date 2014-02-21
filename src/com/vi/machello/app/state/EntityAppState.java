/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.app.state;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.vi.machello.app.EntityDataState;

/**
 * <code>EntityAppState</code>, provides added functionality for entity based
 * classes which process entities commonly called Processors or better known as
 * Entity Systems. It implements common methods in order to make creation of
 * Systems straight forward.
 *
 * @author Kyle D. Williams
 */
public abstract class EntityAppState extends CoreAppState {

    private EntityData entityData;
    private EntityId applicationId;

    /**
     * Main Entry Point for AppStates that require entities
     *
     * @param app
     * @param ed
     */
    protected abstract void AppState(Application app, EntityData ed);

    @Override
    public final void initialize(AppStateManager stateManager, Application app) {
        //Remember outside calls can return null since app and stateManager
        //has not been defined in super class

        EntityDataState eds = stateManager.getState(EntityDataState.class);
        synchronized (this) {
            if (eds == null) {
                eds = new EntityDataState();
                stateManager.attach(eds);
            }
        }

        //EntityAppState needs to initialize second...or close to first in the Queue
        //and sadly no other way to do it at the moment besides 
        //adding current appstate to the end of the queue
        if (eds.isInitialized() == false) {
            getLogger().trace("Entity Data must be initialized before {}...", this);
            stateManager.detach(this);
            stateManager.attach(this);
        } else {
            //order is important
            super.initialize(stateManager, app);
            if (isInitialized()) {
                entityData = eds.getEntityData();
                applicationId = eds.getApplicationId();
                AppState(app, entityData);
                if (isEnabled()) {
                    enable();
                }
            }
        }
    }

    public EntityData getEntityData() {
        return entityData;
    }

    public EntityId getApplicationId() {
        return applicationId;
    }

    public <T extends EntityComponent> T getApplicationComponent(Class<T> type) {
        return entityData.getComponent(applicationId, type);
    }
}
