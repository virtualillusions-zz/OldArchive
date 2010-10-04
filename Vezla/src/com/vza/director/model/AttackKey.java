/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vza.director.model;

import com.jme3.animation.AnimChannel;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
 
/**
 *
 * @author Kyle Williams
 */
public final class AttackKey {
    ArrayList<String> button = new ArrayList<String>();
    private String animation,prevAni;
    private boolean isLast,isBlock,isBlockBreaker;
    public AttackKey(ArrayList<String> button, String animation){
        this.button=button;
        this.animation=animation;
        this.prevAni="";
        this.isLast=false;
        this.isBlock=false;
        this.isBlockBreaker=false;
    }

    /**
     * Returns the button/s needed to perform this move
     * @return An Array of Buttons Required to perform the Animation
     */
    public final ArrayList<String> getButton(){return button;}

    /**
     * Returns the name of the attack
     * @return aniamtion
     */
    public final String getAnimationName(){return animation;}

    /**
     * Returns if this is the last attack in a combo
     * @return isLast
     */
    public final boolean getisLast(){return isLast;}

    /**
     * Returns if this is the last attack in a combo
     * @return isLast
     */
    public final boolean getisBlock(){return isBlock;}

    /**
     * Returns if this is the last attack in a combo
     * @return isLast
     */
    public final boolean getisBlockBreaker(){return isBlockBreaker;}

    /**
     * Performs This attack
     * @param model
     * @param control
     */
    public void performKey(Spatial model, AnimChannel control){
        control.setAnim(animation,0);
    }
    /**
     * ONLY USE THIS KEY MARKS THE END OF A COMBOSET
     * this is used to set the isLast boolean to true
     */
    public final void setIsLast(){isLast = true;}
     /**
     * this is used to set the block boolean to true
     */
    public final void setBlock(){isBlock=true;}
     /**
     * this is used to set the blockBreaker boolean to true
     */
    public final void setBlockBreaker(){isBlockBreaker=true;}
    /**
     * The sequence Control is the attack key animation that is called right before this one 
     * @param seqCont
     */
    //TODO: PrevAnimation or PrevButton hmmm
    public final void setPrevAni(String prev){this.prevAni=prev;}
    /**
     * Gets the key that is used right before this one
     */
    public final String getPrevAni(){return prevAni;}
}
