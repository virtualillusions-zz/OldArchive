/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.scene.visual;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.spectre.app.SpectreAppState;
import com.spectre.app.SpectreApplication;
import com.spectre.app.SpectreApplicationState;
import com.spectre.scene.visual.components.*;
import com.spectre.scene.visual.components.VisualRepPiece.VisualType;
import com.spectre.systems.input.Buttons;
import com.spectre.systems.input.components.ActionModePiece;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

/**
 * @author Kyle
 */
public class VisualSystem extends SpectreAppState implements ActionListener {

    private EntityData ed;
    private Node modelNode;
    private Node sceneNode;
    private EntitySet visualRepSet;
    private InputManager inputManager;
    //prevent heirarchal nodal issues later on no way around this so ignore
    private LoadingCache<String, Spatial> loadedList;
    //used to keep track of what entites control which spatials
    private HashMap<EntityId, Spatial> modelBindingList;

    @Override
    public void SpectreAppState(final SpectreApplicationState sAppState) {
        this.ed = sAppState.getEntityData();
        this.modelBindingList = sAppState.getModelBindingsList();
        this.modelNode = sAppState.getModelNode();
        this.sceneNode = sAppState.getSceneNode();
        this.visualRepSet = ed.getEntities(
                VisualRepPiece.class,
                InScenePiece.class);
        this.inputManager = sAppState.getInputManager();
        this.loadedList = CacheBuilder.newBuilder().maximumSize(100).build(new CacheLoader<String, Spatial>() {
            @Override
            public Spatial load(String key) throws Exception {
                return sAppState.getAssetManager().loadModel(key);
            }
        });
    }

    @Override
    public void spectreUpdate(float tpf) {
        if (visualRepSet.applyChanges()) {
            add(visualRepSet.getAddedEntities());
            remove(visualRepSet.getRemovedEntities());
            add(visualRepSet.getChangedEntities());
        }
    }

    private void add(Set<Entity> addedEntities) {
        for (Iterator<Entity> it = addedEntities.iterator(); it.hasNext();) {
            Entity e = it.next();
            EntityId id = e.getId();
            VisualRepPiece vrp = e.get(VisualRepPiece.class);
            Node root = vrp.getVisualType() == VisualType.Character ? modelNode : sceneNode;
            String assetPath = vrp.getAssetName();
            try {
                Spatial model = modelBindingList.get(id);
                if (model == null) {
                    model = loadedList.get(vrp.getAssetName()).clone();
                    //Set Spatial mapping
                    modelBindingList.put(e.getId(), model);
                    ///SECTION MUST BE PLACED INSIDE TO PREVENT LOOP
                    //Alert All systems of a change
                    e.set(new InScenePiece());
                    //Set Stance Piece
                    e.set(new ActionModePiece());
                } else if (!model.getKey().getName().equals(assetPath)) {
                    model.removeFromParent();//must be used this way in case a
                    //sceneNode for some reason becomes a modelNode
                    //reload new asset
                    model = loadedList.get(vrp.getAssetName()).clone();
                    //replace spatial mapping
                    modelBindingList.put(e.getId(), model);
                    ///SECTION MUST BE PLACED INSIDE TO PREVENT LOOP
                    //Alert All systems of a change
                    e.set(new InScenePiece());
                    //Set Stance Piece
                    e.set(new ActionModePiece());
                }
                //FINISH UP 
                //attaching outside allows for more possibilities to fix issues
                root.attachChild(model);
                inputManager.addListener(this, Buttons.getActionButton(e.getId()));
            } catch (ExecutionException ex) {
                SpectreApplication.logger.log(Level.SEVERE, "Unable to correctly load Asset: " + vrp.getAssetName(), ex);
            }
        }
    }

    private void remove(Set<Entity> removedEntities) {
        for (Iterator<Entity> it = removedEntities.iterator(); it.hasNext();) {
            Entity e = it.next();
            VisualRepPiece vrp = e.get(VisualRepPiece.class);
            Node root = vrp.getVisualType() == VisualType.Character ? modelNode : sceneNode;
            Spatial model = modelBindingList.remove(e.getId());
            root.detachChild(model);
            //Generally frowned on but required to update classes in case of issue
            ed.removeComponent(e.getId(), InScenePiece.class);
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        for (Iterator<Entity> it = visualRepSet.iterator(); it.hasNext();) {
            Entity entity = it.next();
            if (name.contains(entity.getId() + ":" + Buttons.ControlInputs.Mode)) {
                entity.set(new ActionModePiece(isPressed));
                break;
            }
        }
    }

    @Override
    public void cleanUp() {
        //TODO move to closing statement of application or state figure this out
        //this.remove(visualRepSet);
        this.modelNode = null;
        this.sceneNode = null;
        visualRepSet.release();
        visualRepSet.applyChanges();
        remove(visualRepSet);
        this.visualRepSet = null;
        this.inputManager.removeListener(this);
        this.inputManager = null;
        this.loadedList = null;
        this.modelBindingList = null;
    }
}