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
    private boolean idle,comboMod,specialMod;
    private String player;
    //////////////////////////CONTROLS/////////////////
    private float time;
    private final java.util.ArrayList<String> buttonList = new java.util.ArrayList<String>();
    private final float delayInterval = .2f;


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


    /**
     * Sets Up players Control bindings
     * @param player the name of the player on file
     */
    public void setUserController(final String player){
        final com.jme3.input.InputManager inputManager = com.vza.director.Director.getApp().getInputManager();
        time=timer.getTimeInSeconds();
        comboMod=false;
        this.player = player;
        inputManager.addListener(actionator,
                player+"-Up",player+"-Down",player+"-Left",player+"-Right",
                 player+"-Attack1", player+"-Attack2", player+"-Attack3",player+"-Attack4"
                    , player+"-comboMod",player+"-specialMod");
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

    /**
     * Recieves and analyzes input
     */
    private ActionListener actionator = new ActionListener(){
            private int caseNumber=0;          //An integer to determine which case to perform
            private int prevCaseNumber=0;      //The previous case Number
            private String currentButton="";   //The button to be evaluated
            private String previousButton="";  //The previous button evaluated
            @Override
            public void onAction(String name, boolean value, float tpf){
                if(name.equals(player+"-comboMod")) {comboMod=value;return;}
                //////This Section Evaluates once a button is pressed//////////
                else if(value == true)
                {
                    caseNumber = caseNumber(name);
                    if(caseNumber!=0)buttonPressed();
                /////This Section Evaluates once a button is released//////////
                }else{
                    caseNumber = caseNumber(name);
                    if(caseNumber!=0)buttonReleased();
                }
                //This resets all variables
                reset();
            }
            /**
             *
             */
            private int caseNumber(String name){
                    if(name.equals(player+"-Attack1")){
                        currentButton="Attack1";
                        return 1;
                    }else if(name.equals(player+"-Attack2")){
                        currentButton="Attack2";
                        return 2;
                    }else if(name.equals(player+"-Attack3")){
                        currentButton="Attack3";
                        return 3;
                    }else if(name.equals(player+"-Attack4")){
                        currentButton="Attack4";
                        return 4;
                    }else if(name.equals(player+"-Up")){
                        currentButton="Up";
                        return 5;
                    }else if(name.equals(player+"-Down")){
                        currentButton="Down";
                        return 6;
                    }else if(name.equals(player+"-Left")){
                        currentButton="Left";
                        return 7;
                    }else if(name.equals(player+"-Right")){
                        currentButton="Right";
                        return 8;
                    }else if(name.equals(player+"-specialMod")){
                        currentButton="SpecialMod";
                        return 9;
                    }
                return 0;
            }

            private void reset(){
                prevCaseNumber=caseNumber;
                if(caseNumber != 0)caseNumber = 0;
                previousButton=currentButton;
                currentButton="";
            }

            private void buttonPressed(){
                if(buttonList.isEmpty()){
                    buttonList.add(currentButton);
                    time=timer.getTimeInSeconds();
                } else {
                    float curTime=timer.getTimeInSeconds();
                    if(time>=curTime-delayInterval/2){
                        String button = prevCaseNumber<caseNumber?
                            previousButton+"-"+currentButton:currentButton+"-"+previousButton;
                        //This is special block for directional buttons
                        if(caseNumber>4){
                            buttonList.add(button);
                            time=timer.getTimeInSeconds();
                        //This Is special block for attack Buttons
                        } else if (caseNumber < 5){
                            int size= buttonList.size();
                            if(size>1&&buttonList.get(size-2).contains("Attack")) {
                                buttonList.set(size-2, button);
                                buttonList.remove(size-1);
                                buttonList.trimToSize();
                        } else
                                buttonList.set(size-1, button);

                            //Because Two Attack Button Pressed Automatically perform animation
                            System.out.println("what is going in:"+buttonList);
                            performAction(buttonList);
                            buttonList.clear();
                        }
                    }else if(time>curTime-delayInterval){
                        buttonList.add(currentButton);
                        time=timer.getTimeInSeconds();
                    }
                }
            }

            public void buttonReleased(){
                if(!buttonList.isEmpty()&& caseNumber>4
                        && time>timer.getTimeInSeconds()-delayInterval){
                    String lastButton = buttonList.get(buttonList.size()-1);
                    if(lastButton.contains("-")&&lastButton.contains(currentButton)
                            && !lastButton.contains("Attack")){
                        String[] buttons = lastButton.split("-");
                        buttonList.add(
                                currentButton.equals(buttons[0])?
                                    buttons[1]:buttons[0]
                        );
                    }
                }
            }
        };
}
