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
     private boolean isActive =true;
     private static ModelDirector modelDirector;
     private static SceneDirector sceneDirector;
     private static VezlaApplication app;
     private static AppStateManager stateManager;
     private static GameSettings gameSettings;
     private static com.jme3.bullet.PhysicsSpace pSpace;
     private static java.util.HashMap<String,com.vza.app.Player> activePlayers = new java.util.HashMap<String,com.vza.app.Player>();


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
     * Creates the character and sets up the player controller
     * @param character
     * @param player
     */
    public static void buildCharacter(String character, String player){
        activePlayers.get(player).setModelName(character);
        getModelDirector().attachToRoot(character);
        getModelDirector().getFighterInputController(character).setUserController(activePlayers.get(player).getName());
    }
    /**
     * deletes the character and dettaches from update
     * @param character
     * @param player
     */
    public static void deleteCharacter(String character, String player){
        activePlayers.get(player).setModelName(null);
        getModelDirector().detachFromRoot(character);
    }

    public static void createPlayer(String player){activePlayers.put(player, new com.vza.app.Player(player));}
    public static com.vza.app.Player getPlayer(String player){return activePlayers.get(player);}

    public static com.vza.director.model.Character getFighter(String player){
        return getModelDirector().getFighter(activePlayers.get(player).getModelName());
    }
    /**
     * Returns the character's Phsyics Node
     * @param ID the name of the character
     * @return The Character's Physics Node
     */
    public static com.jme3.bullet.nodes.PhysicsCharacterNode getFighterPhysics(String player){
        return getModelDirector().getFighter(activePlayers.get(player).getModelName()).getPhysicsModel();
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

    /**@see com.vza.director.CollisionDirector*/
    public static com.vza.director.PhysicsDirector getPhysicsDirector(){return getAppState(com.vza.director.PhysicsDirector.class);}


    public static com.jme3.bullet.PhysicsSpace getPhysicsSpace(){
        if(pSpace==null)pSpace=getPhysicsDirector().getPhysicsSpace();
        return pSpace;
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

    public void setActive(boolean active) {isActive=active;}

    public boolean isActive() {return isActive;}

    public void postRender() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

}
