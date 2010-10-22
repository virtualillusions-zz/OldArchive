/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vza.director;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import com.vza.director.model.CharacterNode;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * This class is in charge of fighter management use it to get all available fighters
 * @author Kyle Williams
 */
public final class ModelDirector implements com.jme3.app.state.AppState{

    private boolean initialized = false;
    private HashMap<String,CharacterNode> characterList;
    private Node root;
    private Spatial[] activeModels;
    private PhysicsDirector cD;
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        initialized=true;
        root=Director.getApp().getModelNode();
        characterList = new HashMap<String,CharacterNode>();
        findAndSetAllCharacters();
        cD=stateManager.getState(PhysicsDirector.class);
    }
    /**
     * Returns the character
     * @param ID the name of the character
     * @return The Characters's chracter class
     */
    public CharacterNode getFighter(String ID){    
        if(!characterList.containsKey(ID)){
            com.vza.app.VezlaApplication.logger.log(Level.SEVERE, "Character " +ID+" cannot be found in the loaded characterList please check spelling",new IOException());
         }                  
        return characterList.get(ID);
    }

    /**
     * Returns the character's Phsyics Node
     * @param ID the name of the character
     * @return The Character's Physics Node
     */
    public com.jme3.bullet.nodes.PhysicsCharacterNode getFighterPhysics(String ID){return getFighter(ID).getPhysicsModel();}
    /**
     * Returns the inputCOntrollerInterface of the selected character
     * @param ID
     * @return
     */
   public com.vza.director.model.controller.AnimationInputController
           getFighterInputController(String ID){return getFighter(ID).getInputController();}

    /**
     * Attaches Character To Root
     * @param ID
     */
    public void attachToRoot(String ID){
        root.attachChild(getFighterPhysics(ID));
        Director.getApp().getPhysicsSpace().addAll(getFighterPhysics(ID));
    }
    
    /**
     * Detaches Character From RooT
     * @param ID
     */
    public void detachFromRoot(String ID){
        Director.getApp().getPhysicsSpace().removeAll(getFighterPhysics(ID));
        root.detachChild(getFighterPhysics(ID));
    }
    public Spatial[] getActiveModels(){return activeModels;}
    /**
     * This is an influence for the camera it must be set for proper operations
     * @param aM
     */
    public void setActiveModels(Spatial[] aM){activeModels=aM;}
    @Override
    public boolean isInitialized() {return initialized;}
    @Override
    public void stateAttached(AppStateManager stateManager) {    }
    @Override
    public void stateDetached(AppStateManager stateManager) {    }
    @Override
    public void update(float tpf) {    }
    @Override
    public void render(RenderManager rm) {    }
    @Override
    public void cleanup() {
        initialized=false;
    }
//////////////////////////THIS IS THE XML SECTION WHICH SETS UP AND LOADS CHARACTERS////////////////////////////////////////
    private void findAndSetAllCharacters(){
        try{
            com.vza.app.util.SAX.EzHandler ezHandler = new com.vza.app.util.SAX.EzHandler().getHandler1(),helper;
            final SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            final String Directory = "assets/Models/";
            saxParser.parse(new FileInputStream(Directory+"characterList.xml"),ezHandler);
            String characterName;
             Object[] styleList;
            //This Section searchs for and creates Characters
            for(int i=0; i<Array.getLength(ezHandler.getStuff());i++){
                characterName = (String) Array.get(ezHandler.getStuff(),i);
                helper = ezHandler.getHandler2(characterName);
                saxParser.parse(new FileInputStream(Directory+characterName+"/"+characterName+".xml"), helper);  
                CharacterNode character = new CharacterNode(characterName,(String)Array.get(helper.getStuff(),0),(String)Array.get(helper.getStuff(),1));
                styleList = (Object[]) (Array.get(helper.getStuff(), 2));
                //This Section searchs for and creates Styles
               for(int j = 0; j<styleList.length;j++){
                 helper = ezHandler.getHandler3(characterName);
                 saxParser.parse(new FileInputStream(Directory+characterName+"/"+styleList[j]+".xml"), helper);
                 character.addStyle((com.vza.director.model.Style) helper.getStuff());
                }
               characterList.put(characterName, character);
            }
        } catch(Throwable err){
            err.printStackTrace();
            com.vza.app.VezlaApplication.logger.warning("Finding All Characters Have Failed");
        }
    }
}
