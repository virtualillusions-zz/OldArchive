/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
 
package com.vza.director;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;
import com.vza.app.GameSettings;
import com.vza.app.VezlaApplication;

/**
 *  This Class is a singleton that holds all valuable information to allow the game to run
 * @author Kyle Williams 
 */
public final class Director implements com.jme3.app.state.AppState{

     private boolean initialized = false;
     private static ModelDirector modelDirector;
     private static SceneDirector sceneDirector;
     private static VezlaApplication app;
     private static AppStateManager stateManager;
     private static GameSettings gameSettings;

    @Override
    public void initialize(AppStateManager StateManager, Application application) {
      initialized=true;
        synchronized(this) {
            app= (VezlaApplication)application;
            stateManager=StateManager;
            gameSettings=getApp().getGameSettings();
        }
    }
    /**
     * Returns VezlaApplication in order to make system related calls
     * @return
     */
    public static VezlaApplication getApp(){return app;}
    /**
     * Creates the character and sets up to be controlled through AI
     * @param character
     * @param player
     */
    public static void buildCharacter(String character){getModelDirector().attachToRoot(character);}
    /**
     * Creates the character and sets up the player controller
     * @param character
     * @param player
     */
    public static void buildCharacter(String character, com.vza.app.Player player){
        getModelDirector().attachToRoot(character);
        getModelDirector().getFighterInputController(character).setUserController(player.getName());
    }

    /**
     * @see com.vza.director.ModelDirector
     * @return modelDirector instance
     */
    public static ModelDirector getModelDirector(){
        if(modelDirector==null)modelDirector = getAppState(ModelDirector.class);
        return modelDirector;
    }
    /**
     * @see com.vza.director.SceneDirector
     * @return sceneDirector instance
     */
    public static SceneDirector getSceneDirector(){
        if(sceneDirector==null)sceneDirector = getAppState(SceneDirector.class);
        return sceneDirector;
    }
    
    /**
     * @param <T> AppState Class
     * @param stateClass the AppStates .class
     * @return the desired appstate if it is attached
     */
    public static <C extends com.jme3.app.state.AppState>C getAppState(Class<C> stateClass){
        return stateManager.getState(stateClass);
    }

    /**
     * The user settings for the game
     * @return gameSettings
     */
    public static GameSettings getGameSettings(){return gameSettings;}

    /**
     * This loads a specific GUI screen
     * @param path the path to the GUI XML
     * @param screen the screen that should be shown
     */ 
    public static de.lessvoid.nifty.Nifty getGUI(){return GuiDirector.getNifty();}
 
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
    public void cleanup() {initialized=false; }

}
