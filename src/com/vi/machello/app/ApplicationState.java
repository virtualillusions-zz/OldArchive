/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.app;

import com.google.common.base.Objects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.RenderState;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.simsilica.es.EntityId;
import com.simsilica.lemur.GuiGlobals;
import com.vi.machello.app.debug.DebugState;
import com.vi.machello.input.PlayerButtonMap;
import com.vi.machello.renderer.visual.control.SpatialControl;
import com.vi.machello.util.SystemUtil;
import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.slf4j.LoggerFactory;

/**
 * The
 * <code>ApplicationState</code> extends the
 * <code>AppState</code> class and acts as a removable
 * <code>AppState</code> version of an
 * <code>SimpleApplication</code> class to provide functionality for all base
 * elements of the game.
 *
 * <br><br>
 *
 * In Order to properly use
 * <code>ApplicationState</code> please make sure that the base
 * <code>Application</code>
 * <code>SimpleApplication</code> to ensure rootNode and guiNode are being
 * updated in the correct order, however this is nor required
 *
 *
 * @author Kyle D. Williams
 */
public class ApplicationState extends AbstractAppState implements AppLogger {

    @Override
    public void initialize(AppStateManager stateManager, final Application app) {
        initialized = true;
        log.trace("Initializing {}...", this.getClass().getName());
        this.settings = app.getContext().getSettings();
        setUpLogger(isInDebugMode());
        GuiGlobals.initialize(app);
        //ENABLE JOYSTICK SUPPORT
        //figure a better way to init settings        
//        settings.setUseJoysticks(true);
//        //re-setting settings they can have been merged from the registry.
//        setSettings(settings);
//        //CHECK IF IN DEBUG MODE OR NOT
        //Add joystick support
        //settings.setUseJoysticks(true);
        app.getInputManager().setAxisDeadZone(0.2f);
        //enable depth test and back-face culling for performance
        app.getRenderer().applyRenderState(RenderState.DEFAULT);
        this.guiNode = setUpBaseNode(guiNodeName, app.getGuiViewPort());
        this.rootNode = setUpBaseNode(rootNodeName, app.getViewPort());

        if (isInDebugMode()
                && stateManager.getState(DebugState.class) == null) {
            stateManager.attach(new DebugState());
        }
        this.loadedList = CacheBuilder.newBuilder().maximumSize(100).build(new CacheLoader<String, Spatial>() {
            @Override
            public Spatial load(String key) throws Exception {
                return app.getAssetManager().loadModel(key);
            }
        });
        this.modelBindingMap = Maps.newHashMap();
        this.playerButtonMaps = Maps.newHashMap();
        this.executor = new ScheduledThreadPoolExecutor(SystemUtil.DEFAULT_THREAD_POOL_SIZE);

        /**
         * Create a shutdown hook to explicitly end and destroy possible ongoing
         * processes or data structures before application is ended to ensure a
         * graceful shutdown
         *
         */
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (loadedList != null) {
                    loadedList.cleanUp();
                    loadedList = null;
                }
                if (modelBindingMap != null) {
                    modelBindingMap.clear();
                    modelBindingMap = null;
                }
                if (playerButtonMaps != null) {
                    playerButtonMaps.clear();
                    playerButtonMaps = null;
                }
                if (executor != null) {
                    executor.shutdownNow();
                    executor = null;
                }
            }
        });
    }

    @Override
    public void cleanup() {
        log.trace("cleanup():" + this);
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
        initialized = false;
    }

    private Node setUpBaseNode(String nodeName, ViewPort viewPort) {
        java.util.List<Spatial> scenes = viewPort.getScenes();
        Node node = null;
        boolean attach = true;
        for (Spatial spat : scenes) {
            if (spat.getName().equals(nodeName)) {
                node = (Node) spat;
                attach = false;
                break;
            }
        }
        node = Objects.firstNonNull(node, new Node(nodeName));
        if (nodeName.equals(guiNodeName)) {
            node.setQueueBucket(com.jme3.renderer.queue.RenderQueue.Bucket.Gui);
            node.setCullHint(Spatial.CullHint.Never);
        }
        if (attach) {
            viewPort.attachScene(node);
        }
        return node;
    }

    public boolean isInDebugMode() {
        return settings.getBoolean("DebugMode");
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
     * A node specifically for the graphical user interface
     *
     * @return guiNode Node
     */
    public Node getGuiNode() {
        return guiNode;
    }

    /**
     * provides a store of configuration to be used by the application.
     *
     * @return settings AppSettings
     */
    public AppSettings getSettings() {
        return settings;
    }

    public synchronized LoadingCache<String, Spatial> getLoadedList() {
        return loadedList;
    }

    public synchronized HashMap<EntityId, SpatialControl> getModelBindingMap() {
        return modelBindingMap;
    }

    public synchronized HashMap<EntityId, PlayerButtonMap> getPlayerButtonMap() {
        return playerButtonMaps;
    }

    /**
     * A thread pool that reuses a fixed number of threads operating off a
     * shared unbounded queue. At any point, at most nThreads threads will be
     * active processing tasks. If additional tasks are submitted when all
     * threads are active, they will wait in the queue until a thread is
     * available. If any thread terminates due to a failure during execution
     * prior to shutdown, a new one will take its place if needed to execute
     * subsequent tasks. The threads in the pool will exist until it is
     * explicitly shutdown.
     *
     * @return
     */
    public synchronized ScheduledThreadPoolExecutor getExecutor() {
        return executor;
    }

    @Override
    public final void update(float tpf) {
        rootNode.updateLogicalState(tpf);//IGNORE WARNING NOT EXTENDING SIMPLEAPPLICATION
        guiNode.updateLogicalState(tpf);//IGNORE WARNING NOT EXTENDING SIMPLEAPPLICATION

        rootNode.updateGeometricState();//IGNORE WARNING NOT EXTENDING SIMPLEAPPLICATION
        guiNode.updateGeometricState();//IGNORE WARNING NOT EXTENDING SIMPLEAPPLICATION
    }

    private void setUpLogger(boolean debug) {
        //programatically configuire logback's log level depending debug mode
        ch.qos.logback.classic.Level rootLevel = debug ? ch.qos.logback.classic.Level.DEBUG : ch.qos.logback.classic.Level.WARN;
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(rootLevel);

        //Configure native logger to be handled by logback
        java.util.logging.LogManager.getLogManager().reset();
        org.slf4j.bridge.SLF4JBridgeHandler.install();
        java.util.logging.Logger.getLogger("global").setLevel(java.util.logging.Level.FINEST);
    }
    //ApplicationState Variables
    private AppSettings settings;
    private final String guiNodeName = "Gui Node";
    private final String rootNodeName = "Root Node";
    private Node guiNode;
    private Node rootNode;
    //prevent heirarchal nodal issues later on no way around this so ignore
    private LoadingCache<String, Spatial> loadedList;
    //used to keep track of what entites control which spatials
    private HashMap<EntityId, SpatialControl> modelBindingMap;
    //used to keep track buttons associated with entites
    private HashMap<EntityId, PlayerButtonMap> playerButtonMaps;
    private ScheduledThreadPoolExecutor executor;
}
