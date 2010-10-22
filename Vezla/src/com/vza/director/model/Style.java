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
 * Styles are
 * @author Kyle Williams
 */
public final class Style extends HashMap<ArrayList<String>,Object>{
    private String styleName;
    private AnimControl control;
    private Spatial model;
    private String styleTranslationName;
    private String archeType;

    public Style(String styleName){
        this.styleName=styleName;
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
    public void performMappedAction(String button){
        for(ArrayList<String> b:this.keySet()){
            if(b.get(0).equals(button)){
                ((AttackKey)get(b)).performKey(this.model, getAnimChannel());
                return;
            }
        }
    }

    /**
     * Takes a sequence of button presses and searches until it finds a corresponding attack that That also has
     * its Prevanimation equal to the currently playing animation
     * @param button A sequence of button presses
     * @param prevAni The currently playing Animation
     */
    public void performMappedAction(ArrayList<String> button,String prevAni){
        //Loop All sequences until a combination is found
        if(!button.isEmpty()){
            for(int i=button.size();i>0;i--){
                //System.out.println("On Cycle: "+i+" with buttons: "+button);
                if(containsKey(button)&&((AttackKey)get(button)).getPrevAni().equals(prevAni)){
                    ((AttackKey)get(button)).performKey(this.model, getAnimChannel());
                    System.out.println(0+"th condition: What is coming Out"+button);
                    return;
                } else if(button.get(button.size()-1).contains("-")) {
                    int last = button.size()-1;
                    String[] swap = button.get(last).split("-");
                    button.set(last, swap[1]+"-"+swap[0]);
                    if(containsKey(button)&&((AttackKey)get(button)).getPrevAni().equals(prevAni)){
                        ((AttackKey)get(button)).performKey(this.model, getAnimChannel());
                        System.out.println(1+"st condition: What is coming Out"+button);
                        return;
                    }
                    button.set(last, swap[0]);
                    if(containsKey(button)&&((AttackKey)get(button)).getPrevAni().equals(prevAni)){
                        ((AttackKey)get(button)).performKey(this.model, getAnimChannel());
                         System.out.println(2+"nd condition: What is coming Out"+button);
                        return;
                    }
                    button.set(last, swap[1]);
                    if(containsKey(button)&&((AttackKey)get(button)).getPrevAni().equals(prevAni)){
                        ((AttackKey)get(button)).performKey(this.model, getAnimChannel());
                        System.out.println(3+"rd condition: What is coming Out"+button);
                        return;
                    }
                    if(swap[0].contains("Attack")&&!swap[1].contains("Attack")){
                      button.set(last, swap[1]);
                      button.add(swap[0]);
                      if(containsKey(button)&&((AttackKey)get(button)).getPrevAni().equals(prevAni)){
                        ((AttackKey)get(button)).performKey(this.model, getAnimChannel());
                        System.out.println(4+"th condition: What is coming Out"+button);
                        return;
                      }
                    }
                    if(!swap[0].contains("Attack")&&swap[1].contains("Attack")){
                      button.set(last, swap[0]);
                      button.add(swap[1]);
                      if(containsKey(button)&&((AttackKey)get(button)).getPrevAni().equals(prevAni)){
                        ((AttackKey)get(button)).performKey(this.model, getAnimChannel());
                        System.out.println(5+"th condition: What is coming Out"+button);
                        return;
                      }
                    }
                }
               button.remove(0);
            }
        }
    }
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
    public AnimChannel getAnimChannel(){return control.getChannel(0);}
}
