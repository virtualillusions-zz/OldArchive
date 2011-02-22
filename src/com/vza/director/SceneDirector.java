/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vza.director;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;

/**
 *
 * @author Kyle Williams
 */
public final class SceneDirector implements com.jme3.app.state.AppState{
    private boolean initialized = false;
   private boolean isActive =true;
    private Node root;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        initialized=true;
        root=Director.getApp().getSceneNode();
    }

    @Override
    public void stateAttached(AppStateManager stateManager) {    }

    @Override
    public void stateDetached(AppStateManager stateManager) {    }

    @Override
    public void update(float tpf) {    }

    @Override
    public void render(RenderManager rm) {    }


    @Override
    public boolean isInitialized() {return initialized;}


    @Override
    public void cleanup() {       initialized=false;    }

    public void setActive(boolean active) {isActive=active;}

    public boolean isActive() {return isActive;}

    public void postRender() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

}
