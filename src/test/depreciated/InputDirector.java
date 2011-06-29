///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package test.depreciated;
//
//import com.jme3.app.Application;
//import com.jme3.app.state.AppStateManager;
//import com.jme3.renderer.RenderManager;
//import com.jme3.renderer.ViewPort;
//import com.jme3.scene.Spatial;
//import com.jme3.scene.control.Control;
//import java.util.ArrayList;
//import java.util.HashMap;
//
///**
// * Controls All Input related Activity of the game
// * @author Kyle Williams
// * TODOS: Savable : Players individual buttons if not found then load defaults
// */
//public class InputDirector extends com.spectre.app.SpectreState {
//    //Contains a list of input for players if one is not found then defaults are loaded
//    private HashMap<String,ArrayList> inputKeeper;
//
//    @Override
//    public void spectreState(AppStateManager stateManager, Application app) {
//        //TODOS: in the future this variable should only initiate a new HashMap if one is not found
//        //If HashMap is not found remember to also put defualtUser into the Map
//        inputKeeper = new HashMap<String, ArrayList>();
//        setDefaults();
//    }
//
//    private void setDefaults() {
//        ArrayList defaultUser = new ArrayList();
//        //defaultUser.add(this);
//    }
//
//    private class inputController extends com.jme3.scene.control.AbstractControl{
//
//        @Override
//        protected void controlUpdate(float tpf) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        protected void controlRender(RenderManager rm, ViewPort vp) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        public Control cloneForSpatial(Spatial spatial) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }        
//    }
//}
