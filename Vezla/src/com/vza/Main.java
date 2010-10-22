package com.vza;

import com.jme3.renderer.RenderManager;



/** 
 * test
 * @author Kyle Williams
 */
public class Main extends com.vza.app.VezlaApplication{

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    } 
 
    @Override
    protected void vezlaApp() {
        //this.getAppStateManager().attach(new com.vza.app.util.FlyCamState());
        this.getAppStateManager().attach(new com.vza.app.util.ProfilerState());
        ////////////////TEMPORARY
        //this.getAppStateManager().detach(new com.vza.director.GuiDirector());
        this.getAppStateManager().attach(new com.vza.gameState.TestGameState());
    }

    @Override
    protected void vezlaUpdate(float tpf) {    }

    @Override
    public void vezlaRender(RenderManager rm) {
        //TODO: add render code
    }
    
}