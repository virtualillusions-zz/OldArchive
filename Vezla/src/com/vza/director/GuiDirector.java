/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vza.director;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import de.lessvoid.nifty.Nifty; 
  
/** 
 * In a broad sense This state directs the way the gui interacts with the user
 * @author Kyle Williams
 */
public final class GuiDirector implements com.jme3.app.state.AppState{
    private static Nifty nifty;
    private NiftyJmeDisplay niftyDisplay;
    private boolean initialized = false; 
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
       initialized=true; 
        //sets up the GUI and uses the LWJGL for audio rendering
       app.getContext().getSettings().setAudioRenderer("LWJGL");
       niftyDisplay = new NiftyJmeDisplay(app.getAssetManager(),
                                          app.getInputManager(),
                                          app.getAudioRenderer(),
                                          Director.getApp().getGuiViewPort());
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/VezlaGui.xml","start");
        Director.getApp().getGuiViewPort().addProcessor(niftyDisplay);
        nifty.exit();
    }
 
    public static Nifty getNifty(){return nifty;}


    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void stateAttached(AppStateManager stateManager) {
        //vapp.getGuiViewPort().addProcessor(niftyDisplay);
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
    public void cleanup() {
        initialized=false;
    }

}
