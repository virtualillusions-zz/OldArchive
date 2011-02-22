/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vza.director.model;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class is a hashmap of all animations that form a Specific Style grouped by previous animation
 * @param String the previous Animation
 * @param ArrayList the animations that are allowed to play after a specific one
 * @author Kyle Williams
 */
public final class Style extends HashMap<String,ArrayList<AttackKey>>{
    private String styleName;
    private AnimControl control;
    private AnimChannel channel;
    private Spatial model;
    private String styleTranslationName;
    private String archeType;
    private boolean applicable;         //This is only true if the previous Animation was found in the map


    public Style(String styleName){
        this.styleName=styleName;
        applicable=false;
    }

    /**
     * An Important
     * @param preKey
     * @param value
     */
    public void addKey(AttackKey value) {
        if(this.keySet().contains(value.getPrevAni())){
            get(value.getPrevAni()).add(value);
        }else{
           ArrayList<AttackKey> attackList = new ArrayList<AttackKey>();
           attackList.add(value);
           put(value.getPrevAni(),attackList);
        }
    }

    public void setInfo(String translationName,String ArcheType){
        styleTranslationName=translationName;
        archeType = ArcheType;
    }

    /**
     * THE USE OF THIS FUNCTION IS NOT PERFERED.
     * Automatically performs a single button animation
     * @param button 
     */
    public void performMappedAction(String button,String prevAni){        
        for(AttackKey attck:get(prevAni)){
            if(attck.getButton().get(0).equals(button)){
                attck.performKey(model, channel);
                return;
            }
        }
    }

    /**
     * 
     * @param button the specific buttonCombination
     * @param list
     * @return
     */
    private boolean cycle(ArrayList<String> button,ArrayList<AttackKey> list){
        for(AttackKey attck:list){
            if(attck.getButton().equals(button)){
                attck.performKey(model, channel);
                return true;
            }
        }
        return false;
    }

    /**
     * Takes a sequence of button presses and searches until it finds a corresponding attack that That also has
     * its Prevanimation equal to the currently playing animation
     * TODO
     * NEED TO FIX THIS SECTION PARTIAL RETHINK IMPL BTW COMBOMOD IS currently unused
     * @param button A sequence of button presses
     * @param prevAni The currently playing Animation
     * @param comboMod the combo modification boolean
     * @return applicable returns a boolean to determine if an animation was found or not
    */
    public void performMappedAction(ArrayList<String> button,String prevAni,boolean comboMod){
        //Loop All sequences until a combination is found
        if(!button.isEmpty()&&keySet().contains(prevAni)){
            ArrayList<AttackKey> animSelect = get(prevAni);       
            //next see if any button combination mataches the list
            for(int i=button.size();i>0;i--){
                //Main Section:: Checks To see if the Whole Sequence Is identical to any held to any held in the current Style
                if(cycle(button, animSelect)){
                    applicable=true;
                    return;
                 //SubSection:: focus on the last button if is conjointed
                } else if(button.get(button.size()-1).contains("-")) {
                    int last = button.size()-1;
                    String[] swap = button.get(last).split("-");
                    //first try swapping around the conjuction
                    button.set(last, swap[1]+"-"+swap[0]);
                    if(cycle(button, animSelect)){
                        applicable=true;
                        return;
                    }
                    //second break apart the concatenation if one is attack and the other is directional
                    if(swap[0].contains("Attack")&&!swap[1].contains("Attack")){
                        button.set(last, swap[1]);
                        button.add(swap[0]);
                        if(cycle(button, animSelect)){
                            applicable=true;
                            return;
                        }
                        button.remove(last);
                    }
                    //third break apart the concatenation if one is attack and the other is directional
                    if(!swap[0].contains("Attack")&&swap[1].contains("Attack")){
                        button.set(last, swap[0]);
                        button.add(swap[1]);
                        if(cycle(button, animSelect)){
                            applicable=true;
                            return;
                        }
                        button.remove(last);
                    }
                    //fourth try using one of the attackKeys in the conjucture
                    button.set(last, swap[0]);
                    if(cycle(button, animSelect)){
                        applicable=true;
                        return;
                    }
                    //fifth try using the other attackKey of the conjucture
                    button.set(last, swap[1]);
                    if(cycle(button, animSelect)){
                        applicable=true;
                        return;
                    }

                    //Reset the original buttons so that this can reloop properly
                    button.set(last, swap[0]+"-"+swap[1]);
                }               
               button.remove(0);
               applicable=false;
            }
        }
    }

    /***/
    public boolean foundPrevAni(){return applicable;}

    /**
     * Returns the name of The Style this lsit contains
     * @return Style Name
     */
    public String getName(){return styleName;}
    /**
     * Returns the Style's ArcheType
     * @return ArcheType
     */
    public String getType(){return archeType;}
    /**
     * Returns the Translated version of the Style's Name
     * @return Translated Name
     */
    public String getRealName(){return styleTranslationName;}
    /*
     * Should be performed before Style is ever accessed
     */
    public void setModel(Spatial character){
        this.model=character;
        this.control=model.getControl(com.jme3.animation.AnimControl.class);
        channel = control.getChannel(0);
    }

    /**
     * The animation Controller of the Model
     * @return AnimControl
     */
    public AnimControl getAnimController(){return control;}
    /**
     * Returns the Main Animation Controller for the Model
     * @return AnimChanel
     */
    public AnimChannel getAnimChannel(){return channel;}
}
