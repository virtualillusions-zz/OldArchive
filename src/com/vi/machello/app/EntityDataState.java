/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.app;

import com.jme3.app.Application;
import com.jme3.system.AppSettings;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.vi.machello.app.components.ApplicationPiece;
import com.vi.machello.app.state.BaseAppState;
import java.util.Iterator;

/**
 * The primary Entity Database The main entry point for retrieving entities and
 * components.
 *
 * While This should really be its own AppState it makes more sense for users to
 * access the EntityDatabaseState class through SpectreApplicationState atm
 *
 * @author Kyle D. Williams
 */
public class EntityDataState extends BaseAppState {

    private EntityData entityData;
    private EntityId applicationId;

    public EntityDataState() {
        this.entityData = null;
    }

    public EntityDataState(EntityData ed) {
        this.entityData = ed;
    }

    public EntityData getEntityData() {
        return entityData;
    }

    /**
     * a entity used to keep valuable information that must be kept for the
     * functioning of all systems
     *
     * @return
     */
    public EntityId getApplicationId() {
        return applicationId;
    }

    @Override
    protected void AppState(Application app) {
        if (entityData == null) {
            AppSettings settings = app.getContext().getSettings();
            if (settings.getBoolean("DebugMode")) {
                entityData = new com.simsilica.es.base.DefaultEntityData();
            } else {
                //FINALLY SET UP THE ENTITY SYSTEM AND CALL SUBCLASS INITIALIZE
                if (!settings.containsKey("SqlPath")) {
                    settings.putString("SqlPath", "SqlDatabase");
                }

                if (!settings.containsKey("SqlWriteDelay")) {
                    settings.put("SqlWriteDelay", Long.valueOf(100L));
                }
                String sqlPath = settings.getString("SqlPath");
                Long writeDelay = (Long) settings.get("SqlWriteDelay");
                try {
                    entityData = new com.simsilica.es.sql.SqlEntityData(sqlPath, writeDelay);
                } catch (java.sql.SQLException ex) {
                    getLogger().error("Failed to Load EntityData in EntityDataState.java",
                            ex);
                }
            }
        }

        //TODO: this is an ugly procedure to find the ApplicationStorageEntity
        if (applicationId == null) {
            EntitySet es = entityData.getEntities(ApplicationPiece.class);
            Iterator<Entity> it = es.iterator();
            if (it.hasNext()) {
                applicationId = it.next().getId();
            } else {
                applicationId = entityData.createEntity();
                entityData.setComponent(applicationId, new ApplicationPiece());
            }
            es.release();
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (entityData != null) {
                    entityData.close();
                }
            }
        });
    }

    @Override
    protected void cleanUp() {
        entityData.close();
        entityData = null; // cannot be reused
    }
}
