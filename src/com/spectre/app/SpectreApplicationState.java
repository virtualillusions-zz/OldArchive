/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.app;

import com.spectre.app.debug.DebugState;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.jme3.animation.Animation;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.BulletAppState.ThreadingType;
import com.jme3.input.InputManager;
import com.jme3.material.RenderState;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.AnimData;
import com.jme3.system.AppSettings;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.sql.SqlEntityData;
import com.spectre.systems.player.components.PlayerHighScorePiece;
import com.spectre.systems.player.components.PlayerPiece;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

/**
 * The
 * <code>SpectreApplicationState</code> extends the
 * <code>AppState</code> class and acts as a removable
 * <code>AppState</code> version of an
 * <code>SpectreApplicationState</code> class to provide functionality for all
 * base elements of Spectre.
 *
 * <br><br>
 *
 * In Order to properly use
 * <code>SpectreApplicationState</code> please make sure that the base
 * <code>Application</code> class is an instance of
 * <code>SpectreApplication</code> or
 * <code>SimpleApplication</code> to ensure rootNode and guiNode are being
 * updated in the correct order
 *
 * @author Kyle Williams
 */
public class SpectreApplicationState implements AppState {

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        initialized = true;
        this.app = app;
        this.inputManager = app.getInputManager();
        this.settings = app.getContext().getSettings();
        this.stateManager = app.getStateManager();
        this.assetManager = app.getAssetManager();
        boolean isSpectreApp = app instanceof SpectreApplication;
        //enable depth test and back-face culling for performance
        if (isSpectreApp) {
            app.getRenderer().applyRenderState(RenderState.DEFAULT);
        }
        this.guiNode = isSpectreApp ? ((SpectreApplication) app).getGuiNode()
                : setUpBaseNode(guiNodeName, app.getGuiViewPort());
        this.rootNode = isSpectreApp ? ((SpectreApplication) app).getRootNode()
                : setUpBaseNode(rootNodeName, app.getViewPort());
        this.modelSubNode = isSpectreApp ? ((SpectreApplication) app).getModelNode()
                : setUpNode(modelNodeName, rootNode);
        this.sceneSubNode = isSpectreApp ? ((SpectreApplication) app).getSceneNode()
                : setUpNode(sceneNodeName, rootNode);
        app.getInputManager().setAxisDeadZone(0.5f);//2f);
        setUpEntitySystem(isSpectreApp);
        setUpOtherVariables(isSpectreApp);
        //SET UP DEBUG IF NEEDED
        if (isInDebugMode() && stateManager.getState(DebugState.class) == null) {
            stateManager.attach(new DebugState());
        }
    }

    public boolean isInDebugMode() {
        return settings.getBoolean("DebugMode");
    }

    /**
     * @return the {@link Application application}.
     */
    public Application getApplication() {
        return app;
    }

    /**
     * @return the {@link InputManager input manager}.
     */
    public InputManager getInputManager() {
        return inputManager;
    }

    /**
     * @return The {@link AssetManager asset manager} for this application.
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }

    /**
     * A node specifically for the graphical user interface
     *
     * @return guiNode Node
     */
    public Node getGuiNode() {
        return guiNode;
    }

    /**
     * The master node that all nodes are added to, should not be called to
     * attach any other node to normally
     *
     * @return rootNode Node
     */
    public Node getRootNode() {
        return rootNode;
    }

    /**
     * A node which Handel all models within the world
     *
     * @return modelNode Node
     */
    public Node getModelNode() {
        return modelSubNode;
    }

    /**
     * A node which Handel all scenes within the world
     *
     * @return sceneNode Node
     */
    public Node getSceneNode() {
        return sceneSubNode;
    }

    /**
     * The main entry point for retrieving entities and components.
     *
     * @return entityData EntityData
     */
    public EntityData getEntityData() {
        return entityData;
    }

    /**
     * provides a store of configuration to be used by the application.
     *
     * @return settings AppSettings
     */
    public AppSettings getAppSettings() {
        return settings;
    }

    private Node setUpBaseNode(String nodeName, ViewPort viewPort) {
        List<Spatial> scenes = viewPort.getScenes();
        Node node = null;
        for (Spatial spat : scenes) {
            if (spat.getName().equals(nodeName)) {
                node = (Node) spat;
                break;
            }
        }
        if (node == null) {
            throw new NullPointerException(nodeName + " returned null on look up,\n"
                    + "such a node is unable to be created due to complex update statements");
            //CAN'T UPDATE THESE NODE CORRECTLY
            //node = new Node(nodeName);
            //viewPort.attachScene(node);
        }

        if (nodeName.equals(guiNodeName)) {
            node.setQueueBucket(RenderQueue.Bucket.Gui);
            node.setCullHint(Spatial.CullHint.Never);
        }
        return node;
    }

    private Node setUpNode(String nodeName, Node root) {
        Node node = (Node) root.getChild(nodeName);
        node = Objects.firstNonNull(node, new Node(nodeName));
        root.attachChild(node);
        return node;
    }

    private void setUpEntitySystem(boolean isSpectreApp) {
        if (isSpectreApp) {
            entityData = ((SpectreApplication) app).getEntityData();
            return;
        }
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
            entityData = new SqlEntityData(sqlPath, writeDelay);
        } catch (SQLException ex) {
            SpectreApplication.logger.log(Level.SEVERE,
                    "Failed to Load EntityData in SpectreApplication.java",
                    ex);
        }
    }

    private void setUpOtherVariables(boolean isSpectreApp) {
        if (isSpectreApp) {
            SpectreApplication sApp = ((SpectreApplication) app);
            modelBindingsList = sApp.getModelBindingsList();
            animationsList = sApp.getAnimationList();
            playerList = sApp.getPlayerList();
            physicsDirector = sApp.getPhysicsDirector();
            return;
        }
        modelBindingsList = Maps.newHashMap();
        animationsList = Maps.newHashMap();
        playerList = Maps.newHashMap();
        physicsDirector = stateManager.getState(BulletAppState.class);
        if (physicsDirector == null) {
            physicsDirector = new BulletAppState();
            physicsDirector.setThreadingType(ThreadingType.PARALLEL);
            stateManager.attach(physicsDirector);
        }
    }

    /**
     * Adds a new player to the game
     *
     * @param player the name of the player
     */
    private void addPlayer(String player) {
        EntityId id = getEntityData().createEntity();
        getEntityData().setComponents(id, new PlayerPiece(player), new PlayerHighScorePiece(0L));
        playerList.put(player, id);
    }

    /**
     * Returns the player if one is not found creates a new instance
     *
     * @param player the name of the player
     * @return PlayerController
     */
    public EntityId getPlayerId(String player) {
        if (!playerList.containsKey(player)) {
            com.spectre.app.SpectreApplication.logger.log(Level.FINE, "Player {0} cannot be found as part of the playerList, a new player shall be made in its place", player);
            addPlayer(player);
        }
        return playerList.get(player);
    }

    /**
     * Controls all of the physics Handling in the Application
     *
     * @see com.jme3.bullet.BulletAppState
     * @return physicsDirector
     */
    public BulletAppState getPhysicsDirector() {
        return physicsDirector;
    }

    /**
     * @see com.jme3.bullet.PhysicsSpace
     * @return physicsSpace
     */
    public com.jme3.bullet.PhysicsSpace getPhysicsSpace() {
        return getPhysicsDirector().getPhysicsSpace();
    }

    /**
     * A HashMap of all Entity Controlled Spatials
     *
     * ie spatials that must be bound to an entity for identification
     *
     * @return modelList HashMap<EntityId, Spatial>
     */
    public HashMap<EntityId, Spatial> getModelBindingsList() {
        return modelBindingsList;
    }

    /**
     * Returns all animations saved under a skeleton
     *
     * @param skeletonName
     * @param spat
     * @return
     */
    public AnimData getAnimations(String skeletonName) {
        return animationsList.get(skeletonName);
    }

    /**
     * Returns the first animation with animName Better to do this way so we can
     * ignore naming conventions and can link character animations as well
     *
     * @param animName
     * @param spat
     * @return
     */
    public Animation getAnimation(String animName) {
        for (AnimData animData : animationsList.values()) {
            for (Animation anim : animData.anims) {
                if (anim.getName().equals(animName)) {
                    return anim;
                }
            }
        }
        return null;
    }

    /**
     * Returns the Animation of a specified skeleton
     *
     * @param skeletonName
     * @param animName
     * @param spat
     * @return
     */
    public Animation getAnimation(String skeletonName, String animName) {
        for (Animation anim : animationsList.get(skeletonName).anims) {
            if (anim.getName().equals(animName)) {
                return anim;
            }
        }
        return null;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void stateAttached(AppStateManager stateManager) {
        if (stateManager.getState(SpectreApplicationState.class) != null) {
            stateManager.detach(this);
        }
    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void render(RenderManager rm) {
    }

    @Override
    public void postRender() {
    }

    @Override
    public void cleanup() {
        initialized = false;
    }
    /**
     * <code>initialized</code> is set to true when the method
     * {@link AbstractAppState#initialize(com.jme3.app.state.AppStateManager, com.jme3.app.Application) }
     * is called. When {@link AbstractAppState#cleanup() } is called,
     * <code>initialized</code> is set back to false.
     */
    protected boolean initialized = false;
    private boolean enabled = true;
    //SpectreApplicationState Variables
    private Application app;
    private AppSettings settings;
    private InputManager inputManager;
    private AppStateManager stateManager;
    private AssetManager assetManager;
    private final String guiNodeName = "Gui Node";
    private final String rootNodeName = "Root Node";
    private final String modelNodeName = "Model Node";
    private final String sceneNodeName = "Scene Node";
    private Node guiNode;
    private Node rootNode;
    private Node modelSubNode;
    private Node sceneSubNode;
    private EntityData entityData;
///////////Director Variables
    private HashMap<EntityId, Spatial> modelBindingsList;
    private BulletAppState physicsDirector;
    private HashMap<String, EntityId> playerList;
    private HashMap<String, AnimData> animationsList;//use animeData over Animation to allow easier templating
    //private FilterSubDirector filterDirector;
    //private SupplyDeck cardList;
}
