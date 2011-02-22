/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vza.director.model.controller;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.input.controls.ActionListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.vza.director.model.Style;
import java.io.IOException;

/**
 * This class controls all animation of the fighter;
 * @author Kyle Williams
 */
public final class AnimationInputController implements Control{
    private String player;
    private Style activeStyle;
    private String curAnim="";
    public boolean enabled=true;
    private GaugeController gaugeController;
    private TargetController targetController;
    private boolean idle,comboMod,stanceMod;
    private boolean isCrouching=false;          //must be set initially so as to not interrupt stance change
    private MoveController moveController;


    public void setActiveStyle(Style newStyle){
        activeStyle = newStyle;
        idle=false;
        performAction(null);
        activeStyle.getAnimController().addListener(new AnimEventListener(){
            @Override
            public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName){
                //if(!idle||!animName.contains("idle")){
                   performAction(null);
                //}
            }
            @Override
             public void onAnimChange(AnimControl control, AnimChannel channel, String animName){
                curAnim=animName;
            }
        });
    }


    /**
     * Sets Up players Control bindings
     * @param player the name of the player on file
     */
    public void setUserController(final String player){
        final com.jme3.input.InputManager inputManager = com.vza.director.Director.getApp().getInputManager();
        comboMod=false;
        stanceMod=false;
        this.player = player;
        inputManager.addListener(actionator,                                                //This adds Button Combinational support
                player+"-Up",player+"-Down",player+"-Left",player+"-Right",
                 player+"-Attack1", player+"-Attack2", player+"-Attack3",player+"-Attack4");
        inputManager.addListener(basicMod,player+"-comboMod",player+"-specialMod");
        /**@todo fix max jump height to be given by activeStyle*/
        //moveController.setMaxJumpHeight(actoveStyle.getMaxJumpHeight());
        inputManager.addListener(moveController.getListener(player,activeStyle,targetController),
                player+"-Up",player+"-Down",player+"-Left",player+"-Right");
        
    }

    /**TODO: THIS SHOULD BE TEMPORARY BECAUSE EACH CONTROLLER SHOULD BE AS INDEPENDENT AS POSSIBLE
     * This adds properly sets up the needed Move Controller SHOULD NOT BE TAMPORED WITH
     */
     public void addMoveController(MoveController mC){
         moveController=mC;
     }

   /**
     * Perform Action
     * @param Action the animation to perform
     * if Action = null
     * performs main idle animation
     * Perform Tired-Idle
     * Used when fighter life<=25%
     * Perform Okay-Idle
     * Used when 25%<fighter life<75%
     * Perform Great-Idle
     * Used when fighter life >75%
     */
     /*********THEIR IS A PROBLEM WITH REOCCURING ATTACK CYCLE!!!!*********/
    public void performAction(java.util.ArrayList<String> Action){
        if(Action==null){
             String idleCondition="";
             if(isCrouching){idleCondition="Crouch-Idle";}
             else { idleCondition = gaugeController.getLifeCondition(); }
            activeStyle.performMappedAction(idleCondition,"");
            idle=true;
            curAnim="";
        } else if (curAnim.equals("")
               || activeStyle.getAnimChannel().getTime()>= activeStyle.getAnimChannel().getAnimMaxTime()/2){
            activeStyle.performMappedAction(Action,curAnim,comboMod);
            idle=!activeStyle.foundPrevAni();
       }
    }

    @Override
    public void setSpatial(Spatial spatial) {
        gaugeController=spatial.getControl(GaugeController.class);
        targetController=spatial.getControl(TargetController.class);
    }
       //////////////////////////CONTROLS/////////////////
     ButtonSequence buttonSequence = new ButtonSequence() {
        @Override
        public void perform(java.util.ArrayList<String> buttons) {
            //if(!buttonSequence.isEmpty())
               // performAction(buttonSequence);
            //System.out.println(player+buttonSequence);
        }
    };

     /**
     * Receives and analyzes input
     */
    private ActionListener actionator = buttonSequence.buttonListener();
    /**
     * Checks for Mod Keys and basic Movement
     */
    private ActionListener basicMod = new ActionListener(){
        @Override
        public void onAction(String name, boolean isPressed, float tpf){
            if(name.equals(player + "-comboMod"))comboMod=isPressed;
            else if(name.equals(player + "-stanceMod"))stanceMod=isPressed;
        }
    };


    @Override
    public void update(float tpf) {
        buttonSequence.update(tpf); 
    }

    public String getCurrentPlayer(){return player;}

    @Override
    public void setEnabled(boolean enabled) {this.enabled=enabled;}

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEnabled() {return enabled;}

    @Override
    public void render(RenderManager rm, ViewPort vp) {    }

    @Override
    public void write(JmeExporter ex) throws IOException {        throw new UnsupportedOperationException("Not supported yet.");    }

    @Override
    public void read(JmeImporter im) throws IOException {        throw new UnsupportedOperationException("Not supported yet.");    }
}
