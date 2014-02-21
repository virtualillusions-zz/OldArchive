/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.renderer.visual;

import com.google.common.cache.LoadingCache;
import com.jme3.app.Application;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.vi.machello.app.state.EntityAppState;
import com.vi.machello.renderer.visual.components.InScenePiece;
import com.vi.machello.renderer.visual.components.VisualRepPiece;
import com.vi.machello.renderer.visual.control.SpatialControl;
import com.vi.machello.scene.dynamics.components.Direction;
import com.vi.machello.scene.dynamics.components.PositionPiece;
import com.vi.machello.util.math.MathUtil;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * <b>Name:</b> VisualSystem<br/>
 *
 * <b>Purpose:</b> Display and positions visual aspects of the game<br/>
 *
 * <b>Description:</b> Defacto Visual System used in almost all cases
 *
 * @author Kyle D. Williams
 */
public class VisualSystem extends EntityAppState {

    private static final Quaternion RIGHT = new Quaternion().fromAngleAxis(FastMath.PI / 2, new Vector3f(0, 1, 0));
    private static final Quaternion LEFT = new Quaternion().fromAngleAxis(FastMath.PI * 3 / 2, new Vector3f(0, 1, 0));

    @Override
    protected void AppState(final Application app, EntityData ed) {
        this.modelNode = new Node("Model Node");
        this.visualRepSet = ed.getEntities(
                VisualRepPiece.class,
                InScenePiece.class);
        this.visualPosRepSet = ed.getEntities(//seperate set allows for constant update of position without constant rechecking visuals
                VisualRepPiece.class,
                PositionPiece.class,
                InScenePiece.class);
        this.modelBindingMap = getCoreState().getModelBindingMap();
        this.loadedList = getCoreState().getLoadedList();
    }

    @Override
    protected void enable() {
        getCoreState().getRootNode().attachChild(modelNode);
        visualRepSet.applyChanges();
        add(visualRepSet);
        visualPosRepSet.applyChanges();
        update(visualPosRepSet);
    }

    @Override
    protected void disable() {
        modelNode.removeFromParent();
        remove(visualRepSet);
    }

    @Override
    protected void cleanUp() {
        this.visualRepSet.release();
        this.visualPosRepSet.release();
        this.visualRepSet = null;
        this.visualPosRepSet = null;
        this.modelNode = null;
        this.loadedList = null;
        this.modelBindingMap = null;
    }

    @Override
    public void appUpdate(float tpf) {
        if (visualRepSet.applyChanges()) {
            add(visualRepSet.getAddedEntities());
            remove(visualRepSet.getRemovedEntities());
            add(visualRepSet.getChangedEntities());
        }
        if (visualPosRepSet.applyChanges()) {
            update(visualPosRepSet.getAddedEntities());
            update(visualPosRepSet.getChangedEntities());
        }
    }

    private void add(Set<Entity> addedEntities) {
        for (Entity e : addedEntities) {
            EntityId id = e.getId();
            VisualRepPiece vrp = e.get(VisualRepPiece.class);

            try {
                SpatialControl control = modelBindingMap.get(id);
                Spatial model = null;
                if (control != null) {
                    model = control.getSpatial();
                    if (!control.getAssetName().equals(vrp.getAssetName())) {
                        model.removeFromParent();//must be used this way in case a
                        model = null;
                        //Alert All systems of a change
                        //Done so intelligent systems can re/add any spatial controls
                        e.set(new InScenePiece());
                    } else {
                        getLogger().info("Model Already Exists for: Entity {}", e.getId());
                    }
                }
                //Seperate due to efficient logic above
                if (model == null) {
                    model = loadedList.get(vrp.getAssetName()).clone();
                    if (control != null) {
                        control.replaceSpatial(model);
                    } else {
                        control = new SpatialControl(id);
                        model.addControl(control);
                    }
                    //Set Spatial mapping
                    modelBindingMap.put(e.getId(), control);
                }
                Node parent = model.getParent();
                if (parent == null || !parent.equals(modelNode)) {
                    modelNode.attachChild(model);
                }
            } catch (ExecutionException ex) {
                getLogger().error("Unable to correctly load Asset:,{}\n{} ", vrp.getAssetName(), ex);
            }
        }
    } 

    private void remove(Set<Entity> removedEntities) {
        for (Entity e : removedEntities) {
            EntityId id = e.getId();
            SpatialControl control = modelBindingMap.remove(id);
            if (control == null) {
                getLogger().error("Entity {}'s model was not found", id);
                continue;
            }
            Spatial model = control.getSpatial();
            model.removeFromParent();
            control.destroy();
            //Generally frowned on but required to update classes in case of issue
            getEntityData().removeComponent(id, InScenePiece.class);
        }
    }

    private void update(Set<Entity> posEntities) {
        for (Entity e : posEntities) {
            PositionPiece pos = e.get(PositionPiece.class);
            Spatial spat = modelBindingMap.get(e.getId()).getSpatial();
            if (spat == null) {
                getLogger().error("Unable to locate entity {}'s visual representation", e.getId());
                continue;
            }
            TempVars vars = TempVars.get();
            MathUtil.pieceToVec(pos, vars.vect1);
            boolean isLEFT = vars.vect1.getX() < spat.getLocalTranslation().getX();
            boolean isRIGHT = vars.vect1.getX() > spat.getLocalTranslation().getX();
            //rotate first so easier to face direction
            if (pos.getDir().equals(Direction.LEFT) || isLEFT) {
                spat.setLocalRotation(LEFT);
            } else if (pos.getDir().equals(Direction.RIGHT) || isRIGHT) {
                spat.setLocalRotation(RIGHT);
            }
            //Move Spatial
            spat.setLocalTranslation(vars.vect1);
            vars.release();
        }
    }
    //VARIABLESs
    private Node modelNode;
    private EntitySet visualRepSet;
    private EntitySet visualPosRepSet;
    //prevent heirarchal nodal issues later on no way around this so ignore
    private LoadingCache<String, Spatial> loadedList;
    //used to keep track what spatial information belongs to which entites
    private HashMap<EntityId, SpatialControl> modelBindingMap;
}
