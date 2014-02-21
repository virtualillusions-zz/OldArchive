/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.app.state;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;
import com.vi.machello.app.AppLogger;
import com.vi.machello.app.ApplicationState;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.slf4j.Logger;

/**
 * <code>CoreAppState</code> implements some common methods that make creation
 * of AppStates easier. <br><br>
 *
 * *NOTE: Packaged Internal class not meant to be extended by user.
 *
 * @author Kyle D. Williams
 */
abstract class CoreAppState implements AppState, AppLogger {
//THIS IS A PACKAGE SPECIFIC APP STATE DEFAULT MODIFIER USED TO PREVENT USE FROM 
//OUTSIDE OF PACKAGE

    private boolean initialized = false;
    private boolean isEnabled = true;
    private Application app = null;
    private AppStateManager stateManager = null;
    private ApplicationState aps = null;

    /**
     * Please remember when overriding this to call super.initialize to insure
     * proper initialization
     *
     * @param stateManager
     * @param app
     */
    public void initialize(AppStateManager stateManager, Application app) {
        getLogger().trace("Initializing {}...", this.getClass().getName());
        aps = stateManager.getState(ApplicationState.class);
        synchronized (this) {
            if (aps == null) {
                aps = new ApplicationState();
                stateManager.attach(aps);
            }
        }

        //Application needs to initialize first in the Queue
        //and sadly no other way to do it at the moment besides 
        //adding current appstate to the end of the queue
        if (aps.isInitialized() == false) {
            getLogger().trace("AplicationState must be initialized before {}...", this);
            stateManager.detach(this);
            stateManager.attach(this);
        } else {
            this.app = app;
            this.stateManager = stateManager;
            initialized = true;
        }
    }

    /**
     * Called by AppStateManager when transitioning this AppState from
     * terminating to detached. This method is called the following render pass
     * after the AppState has been detached and is always called once and only
     * once for each time initialize() is called. Either when the AppState is
     * detached or when the application terminates (if it terminates normally).
     */
    protected abstract void cleanUp();

    @Override
    public final void cleanup() {
        if (initialized == true) {
            log.trace("Cleaning Up {}...", this.getClass().getName());
            if (isEnabled()) {
                disable();
            }
            cleanUp();
            initialized = false;
        }
    }

    public static Logger getLogger() {
        return log;
    }

    /**
     * @see Application
     */
    public final Application getApp() {
        return app;
    }

    /**
     * @see ApplicationState
     */
    public ApplicationState getCoreState() {
        return aps;
    }

    /**
     * @see ApplicationState#getExecutor()
     */
    public ScheduledThreadPoolExecutor getExecutor() {
        return getCoreState().getExecutor();
    }

    public final <T extends AppState> T getState(Class<T> type) {
        return stateManager.getState(type);
    }

    @Override
    public final boolean isInitialized() {
        return initialized;
    }

    @Override
    public final boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public final void setEnabled(boolean active) {
        if (this.isEnabled == active) {
            return;
        }
        this.isEnabled = active;
        if (!isInitialized()) {
            return;
        }
        if (active) {
            log.trace("Enabling {}...", this.getClass().getName());
            enable();
        } else {
            log.trace("Disabling {}...", this.getClass().getName());
            disable();
        }
    }

    /**
     * <code>enable</code> is initially called when the App state is initialized
     * and the any time the system is re-enabled afterwards after first being
     * disabled
     *
     * @see AppState#setEnabled(boolean)
     */
    protected void enable() {
    }

    /**
     * <code>disable</code> is called whenever the enabled is set to false
     * through setEnabled
     *
     * @see AppState#setEnabled(boolean)
     */
    protected void disable() {
    }

    @Override
    public final void update(float tpf) {
        if (!isEnabled) {
            return;
        }
        appUpdate(tpf);
    }

    @Override
    public final void render(RenderManager rm) {
        if (!isEnabled) {
            return;
        }
        appRender(rm);
    }

    /**
     * To be implemented in subclass.
     */
    protected void appUpdate(float tpf) {
    }

    /**
     * To be implemented in subclass.
     */
    protected void appRender(RenderManager rm) {
    }

    @Override
    public void stateAttached(AppStateManager stateManager) {
    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
    }

    @Override
    public void postRender() {
    }
}