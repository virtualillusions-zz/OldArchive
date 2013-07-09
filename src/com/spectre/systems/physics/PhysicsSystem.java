/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.physics;

import com.google.common.collect.Maps;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.simsilica.es.ComponentFilter;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.simsilica.es.filter.FieldFilter;
import com.spectre.app.SpectreAppState;
import com.spectre.app.SpectreApplicationState;
import com.spectre.app.SpectreControl;
import com.spectre.scene.camera.CameraSystem;
import com.spectre.scene.camera.components.CameraDirectionPiece;
import com.spectre.scene.visual.VisualSystem;
import com.spectre.scene.visual.components.*;
import com.spectre.scene.visual.components.VisualRepPiece.VisualType;
import com.spectre.systems.input.components.ActionModePiece;
import com.spectre.systems.physics.subsystems.CharacterPhysicsController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import test.system.TestPhysicsSystem;

/**
 *
 * @author Kyle
 */
public class PhysicsSystem extends SpectreAppState {

    public static void main(String[] args) {
        Application app = new SimpleApplication(
                new SpectreApplicationState(),
                new VisualSystem(),
                new CameraSystem(),
                new PhysicsSystem(),
                new TestPhysicsSystem()) {
            @Override
            public void simpleInitApp() {
            }
        };
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
        settings.putBoolean("DebugMode", true);
        settings.setUseJoysticks(true);
        app.setSettings(settings);
        app.start();
    }
    protected EntitySet physicsSet;
    private HashMap<EntityId, Spatial> modelBindingsList;
    private InputManager inputManager;
    private PhysicsSpace physicsSpace;
    /**
     * forced to keep track of controller to prevent spatial switching issues
     */
    private HashMap<EntityId, SpectreControl> physContMap;
    /**
     * Very elaborate Entity Set used to add camera component values to the
     * control if needed, should be aware that control should handle the entity
     * it belongs to does not have a camera attached
     */
    private EntitySet camSet;
    /**
     * Roundabout way to update needed ActionMode Piece
     */
    private EntitySet actionSet;

    @Override
    public void SpectreAppState(SpectreApplicationState sAppState) {
        EntityData ed = sAppState.getEntityData();
        this.physicsSet = ed.getEntities(
                VisualRepPiece.class,
                InScenePiece.class);
        this.camSet = ed.getEntities(
                CameraDirectionPiece.class,
                InScenePiece.class);
        ComponentFilter filter = FieldFilter.create(VisualRepPiece.class, "type", VisualRepPiece.VisualType.Character);
        this.actionSet = ed.getEntities(filter,
                ActionModePiece.class,
                VisualRepPiece.class,
                InScenePiece.class);
        this.modelBindingsList = sAppState.getModelBindingsList();
        this.inputManager = sAppState.getInputManager();
        this.physicsSpace = sAppState.getPhysicsSpace();
        this.physContMap = Maps.newHashMap();
        
        sAppState.getPhysicsDirector().getPhysicsSpace().setGravity(new Vector3f(0,-9.81f*10,0));
    }

    @Override
    public void cleanUp() {
        this.actionSet.release();
        this.actionSet.applyChanges();
        changeAction(actionSet.getRemovedEntities(), true);
        this.actionSet = null;
        this.camSet.release();
        this.camSet.applyChanges();
        removeCam(camSet.getRemovedEntities());
        this.camSet = null;
        this.physicsSet.release();
        this.physicsSet.applyChanges();
        remove(physicsSet.getRemovedEntities());
        this.physicsSet = null;

    }

    @Override
    protected void spectreUpdate(float tpf) {
        if (physicsSet.applyChanges()) {
            add(physicsSet.getAddedEntities());
            remove(physicsSet.getRemovedEntities());
            add(physicsSet.getChangedEntities());
        }
        if (camSet.applyChanges()) {
            addCam(camSet.getAddedEntities());
            removeCam(camSet.getRemovedEntities());
            addCam(camSet.getChangedEntities());
        }
        if (actionSet.applyChanges()) {
            changeAction(actionSet.getAddedEntities(), false);
            changeAction(actionSet.getRemovedEntities(), true);
            changeAction(actionSet.getChangedEntities(), false);
        }
    }

    private void add(Set<Entity> addedEntities) {
        for (Iterator<Entity> it = addedEntities.iterator(); it.hasNext();) {
            Entity e = it.next();
            EntityId id = e.getId();
            Spatial spat = modelBindingsList.get(id);
            SpectreControl cc = physContMap.get(id);
            VisualRepPiece vrp = e.get(VisualRepPiece.class);
            VisualType vt = vrp.getVisualType();
            if (spat == null) {
                /**
                 * When updated in Visual System this entity will be re-added to
                 * this update
                 */
                continue;
            } else if (cc != null) {
                if (vt.equals(VisualType.Character)) {
                    if (cc instanceof CharacterPhysicsController) {
                        cc.replaceSpatial(spat);
                        return;
                    }
                }
            }
            ////////////////Add Control
            if (vrp.getVisualType().equals(VisualType.Character)) {
                cc = new CharacterPhysicsController(e, inputManager, physicsSpace);
                spat.addControl(cc);
                physContMap.put(id, cc);
            }
        }
    }

    private void remove(Set<Entity> removedEntities) {
        for (Iterator<Entity> it = removedEntities.iterator(); it.hasNext();) {
            Entity e = it.next();
            EntityId id = e.getId();
            SpectreControl sc = physContMap.get(id);
            sc.destroy();
            physContMap.remove(id);
        }
    }

    private void addCam(Set<Entity> addedEntities) {
        for (Iterator<Entity> it = addedEntities.iterator(); it.hasNext();) {
            Entity e = it.next();
            EntityId id = e.getId();
            SpectreControl pc = physContMap.get(id);
            if (pc != null && pc instanceof CharacterPhysicsController) {
                ((CharacterPhysicsController) pc).updateEntity(e);
            }
        }
    }

    private void removeCam(Set<Entity> removedEntities) {
        for (Iterator<Entity> it = removedEntities.iterator(); it.hasNext();) {
            EntityId id = it.next().getId();
            Entity oe = physicsSet.getEntity(id);
            SpectreControl pc = physContMap.get(id);
            if (pc != null && pc instanceof CharacterPhysicsController) {
                ((CharacterPhysicsController) pc).updateEntity(oe);
            }
        }
    }

    private void changeAction(Set<Entity> addedEntities, boolean removed) {
        for (Iterator<Entity> it = addedEntities.iterator(); it.hasNext();) {
            Entity entity = it.next();
            SpectreControl pc = physContMap.get(entity.getId());
            if (pc != null && pc instanceof CharacterPhysicsController) {
                boolean isActionMode = removed;
                //System.out.println(removed);
                if (removed == false) {
                    isActionMode = entity.get(ActionModePiece.class).isActionMode();
                }
                ((CharacterPhysicsController) pc).setActionMode(isActionMode);
            }
        }
    }
}
