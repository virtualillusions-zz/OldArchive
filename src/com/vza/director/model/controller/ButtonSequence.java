/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vza.director.model.controller;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author Kyle Williams
 * The Button Sequencer class organizes button combinations as well as button sequences.
 *
 * THIS CLASS SHOULD BE UPDATED
 *TODO
 *  opposite directional buttons cannot be pressed
 *  REMEBER TO SORT QUEUE BEFORE ADDING TO BUTTONSEQUENCE
 */
public abstract class ButtonSequence extends java.util.ArrayList<String>{    
    public enum BUTTONS{Attack1,Attack2,Attack3,Attack4,Up,Down,Left,Right;
                        private float recordTime=0;
                        public BUTTONS setTime(float time){recordTime=time; return this;}
    }
    private float time = 0.0f;               //Used to keep track of time since class has been initialized
    private final float BUTTON_TIME_LIMIT = 0.1f;
    private final float TIME_LIMIT = .3f;       //Max Time in Between Button Presses Allowed
        private float recordTime = 0.0f;         //This is used to keep track of the previous button press

    private int attackAdded=0;           //This is used to identify how many attackButtons are being held at a time
    private final String conjunction="+";//The seperator used to group dual button presses
    private boolean changed=false;       //This boolean is used to see if anything changed since the last update cycle
    private boolean constraint=false;       //This boolean is to prevent premature update of main Functions
    private final java.util.Set<BUTTONS> buttonsQueued = java.util.EnumSet.noneOf(BUTTONS.class); //Created as a HashSet to prevent duplicates

    /*
     * adds the button to a List of currently held buttons
     */
    public void addButton(String temp){
        
        buttonsQueued.add(BUTTONS.valueOf(temp).setTime(time+BUTTON_TIME_LIMIT));
        changed=true;
        if(temp.contains("Attack"))attackAdded+=1;
    }

    /*
     * adds the button to a List of currently held buttons
     */
    public void releaseButton(String temp){
        if(!buttonsQueued.isEmpty()){
            buttonsQueued.remove(BUTTONS.valueOf(temp).setTime(0));
            if(!isEmpty())changed=true;
        }
        if(temp.contains("Attack"))attackAdded-=1;
    }


     /**
      * Updates all timers
      * @param tpf
      */
    public void update(float tpf){
       buttonUpdate();
       if(constraint){
            if(changed){
               addQueue();
               changed=false;
               System.out.println(this);
               perform(this);//Main part put outside should be actively updated
            }

            if(time>recordTime){reset();}
            time+=tpf;//MUST BE THE LAST THING TO BE UPDATED
       }
    }

    /**
     * resets everything to be ready for the next button combination
     */
    private void reset(){
      time=0;
      clear();
      recordTime=0;
      changed=false;
      constraint=false;
      buttonsQueued.clear();
    }

    public void buttonUpdate(){
        ArrayList k = new ArrayList();
        for(BUTTONS e:buttonsQueued){
            if(time>e.recordTime){
                k.add(e);
            }
        }
        buttonsQueued.removeAll(k);
    }

    /**
     * Adds the buttons to the main list
     */
    private void addQueue(){
        
        //This section adds whats in the buttonQueue into the list
        if(!buttonsQueued.isEmpty()){
            String bttn=null;
            for(BUTTONS k:buttonsQueued){        //cycles through the buttons currently pressed
                if(bttn!=null){                 //if this is not the initial pass through
                   bttn=bttn+conjunction+k;     //concatenate this button to the end of the previous
                }else{
                   bttn=k.toString();
                }
            }
            //THIS ALLOWS USERS WITH SLOWER KEYBOARDS TO INPUT THREIR MOVES CORRECTLY
                add(bttn);
        }
    }


    /*
     * resets the RecordTime of the current button sequence
     */
    private void resetRecordTime(){
        recordTime=time+TIME_LIMIT;
        if(constraint!=true){constraint=true;} //This constraint is set True here so anytime a button is pressed it is set to Two
    }

    /**
     * An abstract method that should be used to perform what ever actions necessary
     */
    public abstract void perform(java.util.ArrayList<String> buttonSequence);

    /**
     * A pre-configured listener created for aesthetics
     * @return ActionListener
     */
    public com.jme3.input.controls.ActionListener buttonListener(){
        return new com.jme3.input.controls.ActionListener(){
            public void onAction(String name, boolean isPressed, float tpf) {                
                resetRecordTime();
                String button = name.split("-")[1]; //This is done to take only the important part of the input say testPlayer-Attack4->Attack4
                //////////////////////////////////TODO: FIGURE OUT BUTTON TIMING THEIR SEEMS TO BE A SLIGHT ISSUE WITH RAPID INPUT REGISTRATION
                 if (isPressed){
                        addButton(button);
                  }else{
                        releaseButton(button);
                }
            }
       };
    }
}
