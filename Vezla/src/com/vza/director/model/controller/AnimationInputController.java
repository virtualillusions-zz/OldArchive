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
import com.jme3.input.controls.AnalogListener;
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
    private Style activeStyle;
    private String curAnim="";
    private GaugeController gaugeController;
    private com.jme3.system.Timer timer;
    private boolean idle;
    public AnimationInputController(){
        timer=com.vza.director.Director.getApp().getTimer();
    }

    public void setActiveStyle(Style newStyle){
        activeStyle = newStyle;
        idle=false;
        performAction(null);
        activeStyle.getAnimController().addListener(new AnimEventListener(){
            @Override
            public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName){
                if(!idle){
                    performAction(null);
                }
                
            }
            @Override
             public void onAnimChange(AnimControl control, AnimChannel channel, String animName){
                curAnim=animName;
            }
        });
    }
//////////////////////////CONTROLS/////////////////
    private float time;
    private boolean attkButton;
    private final java.util.ArrayList<String> buttonList = new java.util.ArrayList<String>();
    private final float delayInterval = .2f;

    /**
     * Sets Up players Control bindings
     * @param player the name of the player on file
     */
    public void setUserController(final String player){
        final com.jme3.input.InputManager inputManager = com.vza.director.Director.getApp().getInputManager();
        time=timer.getTimeInSeconds();
        String Dual=null;
        attkButton=false;

        /**
        * Mappings not needed as it is set way beforehand
        */
        inputManager.addListener(new ActionListener(){      
           @Override
           public void onAction(String name, boolean value, float tpf){
               System.out.println(name);
               if(value){
                   String button="";
                   if(name.equals(player+"-Up")){
                        button="Up";
                    } else if (name.equals(player + "-Down")){
                        button="Down";
                    } else if (name.equals(player + "-Left")) {
                        button="Left";
                    } else if (name.equals(player + "-Right")) {
                        button="Right";
                    } else if (name.equals(player + "-Attack1")){
                        button="Attack1";
                    } else if (name.equals(player + "-Attack2")){
                        button="Attack2";
                    } else if (name.equals(player + "-Attack3")){
                        button="Attack3";
                    } else if (name.equals(player + "-Attack4")){
                        button="Attack4";
                    } else if (name.equals(player + "-comboMod")){
                        System.out.println("comboModifier");
                    } else if (name.equals(player + "-specialMod")){
                        System.out.println("specialModifier");
                    }
                   
                   if(!button.equals("")){
                      if(!buttonList.isEmpty() && time >= timer.getTimeInSeconds() - delayInterval/2
                              && !buttonList.get(buttonList.size()-1).contains("-")){
                           int last = buttonList.size()-1;
                           buttonList.set(buttonList.size()-1,buttonList.get(last)+"-"+button);
                           update(tpf);
                       }else if(buttonList.isEmpty()||time >= timer.getTimeInSeconds() - delayInterval){
                           buttonList.add(button);
                           time = timer.getTimeInSeconds();
                       }
                   }
               }
           }

         },player+"-Up",player+"-Down",player+"-Left",player+"-Right", player+"-Attack1", player+"-Attack2",
                 player+"-Attack3",player+"-Attack4", player+"-comboMod",player+"-specialMod");

         inputManager.addListener(new AnalogListener(){
         @Override
           public void onAnalog(String name, float value, float tpf){
             //System.out.println(name+value);
               // if(name.equals(player+"-Left"))
               //     System.out.println("left");//left();
               // else if(name.equals(player+"-Right"))
               //     System.out.println("right()");
            }
         } ,player+"-Left",player+"-Right",player+"-Up",player+"-Down",player+"-comboMod");
    }
   @Override
    public void update(float tpf) {
        if(!buttonList.isEmpty() && (timer.getTimeInSeconds()-delayInterval>time)){
            System.out.println("what is Going in: "+buttonList);
            performAction(buttonList);
            buttonList.clear();
       }
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
    public void performAction(java.util.ArrayList<String> Action){
        if(Action==null){
             if(gaugeController.getLifeSegment()==4)
                activeStyle.performMappedAction("Great-Idle");
             else if(gaugeController.getLifeSegment()==1)
                activeStyle.performMappedAction("Tired-Idle");
             else
                activeStyle.performMappedAction("Okay-Idle");
            idle=true;
            curAnim="";
        } else if (curAnim.equals("")
               || activeStyle.getAnimChannel().getTime()>= activeStyle.getAnimChannel().getAnimMaxTime()/2){
            idle=false;
            activeStyle.performMappedAction(Action,curAnim);
       }
    }



    /**
     * Perform Walk
     */
    //public void left(){performAction("Forward");}
     /**
     * Perform Walk
     */
    //public void right(){performAction("Backward");}




    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSpatial(Spatial spatial) {this.gaugeController=spatial.getControl(GaugeController.class); }
    public boolean enabled=true;
    @Override
    public void setEnabled(boolean enabled) {this.enabled=enabled;}

    @Override
    public boolean isEnabled() {return enabled;}

    @Override
    public void render(RenderManager rm, ViewPort vp) {    }

    @Override
    public void write(JmeExporter ex) throws IOException {        throw new UnsupportedOperationException("Not supported yet.");    }

    @Override
    public void read(JmeImporter im) throws IOException {        throw new UnsupportedOperationException("Not supported yet.");    }
}
