/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package depreciated;

import com.vza.director.model.controller.*;
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
public abstract class ButtonSequence3 extends java.util.ArrayList<String>{    

    private float time = 0.0f;               //Used to keep track of time since class has been initialized
    private float recordTime = 0.0f;         //This is used to keep track of the previous button press
    private final float TIME_LIMIT = 0.3f;   //Max Time in Between Button Presses Allowed
    private int attackAdded=0;           //This is used to identify how many attackButtons are being held at a time
    private final String conjunction="+";//The seperator used to group dual button presses
    private boolean changed=false;       //This boolean is used to see if anything changed since the last update cycle
    private boolean constraint=false;       //This boolean is to prevent premature update of main Functions
    private final HashSet<String> buttonsQueued = new HashSet<String>(); //Created as a HashSet to prevent duplicates

    /*
     * adds the button to a List of currently held buttons
     */
    public void addButton(String temp){
        buttonsQueued.add(temp);
        changed=true;
        if(temp.contains("Attack"))attackAdded+=1;
    }

    /*
     * adds the button to a List of currently held buttons
     */
    public void releaseButton(String temp){
        if(!buttonsQueued.isEmpty()){
            buttonsQueued.remove(temp);
            if(!isEmpty())changed=true;
        }
        if(temp.contains("Attack"))attackAdded-=1;
    }


     /**
      * Updates all timers
      * @param tpf
      */
    public void update(float tpf){
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


    /**
     * Adds the buttons to the main list
     */
    private void addQueue(){
        
        if(!buttonsQueued.isEmpty()) houseKeeping(); //delete buttons held for more than two segments
        //This section adds whats in the buttonQueue into the list
        if(!buttonsQueued.isEmpty()){
            String bttn=null;
            for(String k:buttonsQueued){        //cycles through the buttons currently pressed
                if(bttn!=null){                 //if this is not the initial pass through
                   bttn=bttn+conjunction+k;     //concatenate this button to the end of the previous
                }else{
                   bttn=k;
                }
            }
            //THIS ALLOWS USERS WITH SLOWER KEYBOARDS TO INPUT THREIR MOVES CORRECTLY
            if(savingCheck(bttn))
                add(bttn);
        }
    }

    /**
     *This makes sure no attack button is within two segments ie attack1,attack1+Right,attack1
     */
    private void houseKeeping(){
        if(size()>=2){
            java.util.ArrayList k = new java.util.ArrayList<String>();
                for(String button:buttonsQueued){
                    //if this is an attack button and was held for more than 2 segments delete
                    if(get(size()-1).contains(button)
                                &&get(size()-2).contains(button)){
                        if(button.contains("Attack"))
                            k.add(button);
                        //else if(size()>2&&get(size()-3).contains(button)){
                          //  k.add(button);
                       // }
                    }
                }
            System.out.println("REMOVED: "+k);
            buttonsQueued.removeAll(k);
        }
    }

    /*
     * This check is to make sure everything is working as planned regardless of user hardware
     */
    private boolean savingCheck(String bttn){
        //if list is not empty, no attack buttons are being held and the last button was a conjunction
        if(!isEmpty()&&get(size()-1).contains(conjunction)){

            if(attackAdded==0){                          //primarily for directional buttons
                if(size()>1){
                    for(String k:buttonsQueued){        //this section makes sure that a directional button sequence occurs properly
                        if(get(size()-1).contains(k)&&!get(size()-2).contains(k)){  //no tripple directional button press
                            add(k);
                            return false;
                        }
                    }
                }
            }else{
                
            }
        }
        return true;
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
