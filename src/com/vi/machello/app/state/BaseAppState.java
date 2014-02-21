/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.app.state;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;

/**
 * <code>BaseAppState</code>, provides base functionality for all commonly used
 * methods found in more complex applications. It implements common methods in
 * order to make creation of AppStates easier.
 *
 * @author Kyle D. Williams
 */
public abstract class BaseAppState extends CoreAppState {

    /**
     * Main Entry Point for basic AppStates
     *
     * @param app
     */
    protected abstract void AppState(Application app);

    @Override
    public final void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        if (isInitialized()) {
            AppState(app);
            if (isEnabled()) {
                enable();
            }
        }
    }
}