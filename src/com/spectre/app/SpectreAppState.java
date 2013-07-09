/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.app;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;

/**
 * <code>SpectreAppState</code> is centered arround the use of
 * <code>SpectreApplicationState</code>, which provides base functionality for
 * all commonly used methods found in more complex applications. It implements
 * common methods in order to make creation of AppStates easier.
 *
 * @see SpectreApplicationState
 * @author Kyle Williams
 */
public abstract class SpectreAppState implements AppState {

    private boolean initialized = false;
    private boolean isEnabled = true;

    public abstract void SpectreAppState(SpectreApplicationState sAppState);

    /**
     * Called by AppStateManager when transitioning this AppState from
     * terminating to detached. This method is called the following render pass
     * after the AppState has been detached and is always called once and only
     * once for each time initialize() is called. Either when the AppState is
     * detached or when the application terminates (if it terminates normally).
     */
    protected void cleanUp() {
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        SpectreApplicationState sAppState = stateManager.getState(SpectreApplicationState.class);
        if (sAppState == null) {
            sAppState = new SpectreApplicationState();
            stateManager.attach(sAppState);
        }
        //sAppState needs to initialize first in the Queue
        //and sadly no other way to do it at the moment besides adding to the end of the queu
        if (sAppState.isInitialized() == false) {
            stateManager.detach(this);
            stateManager.attach(this);
        } else {
            initialized = true;
            SpectreAppState(sAppState);
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setEnabled(boolean active) {
        isEnabled = active;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void stateAttached(AppStateManager stateManager) {
    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
    }

    /**
     * To be implemented in subclass.
     */
    protected void spectreUpdate(float tpf) {
    }

    /**
     * To be implemented in subclass.
     */
    protected void spectreRender(RenderManager rm) {
    }

    @Override
    public final void update(float tpf) {
        if (!isEnabled) {
            return;
        }
        spectreUpdate(tpf);
    }

    @Override
    public final void render(RenderManager rm) {
        if (!isEnabled) {
            return;
        }
        spectreRender(rm);
    }

    @Override
    public void postRender() {
    }

    @Override
    public final void cleanup() {
        if (initialized == true) {
            initialized = false;
            cleanUp();
        }
    }
}