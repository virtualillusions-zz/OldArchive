/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spectre.deck;


import com.spectre.deck.stats.StatCapsule;
import com.jme3.audio.AudioNode;
import com.jme3.effect.ParticleEmitter;
import com.spectre.deck.stats.SpecialCapsule;
import com.spectre.deck.stats.TranslationCapsule;
import com.spectre.util.Attribute.*;
import java.util.ArrayList;

/**
 * Remember: if self kill particles at end animation if particles 0 if duration lon and particles not 0 kill at end of duration else kill particles when make contact
 * @author Kyle Williams
 */
public class Card implements Cloneable{
    protected String cardName = "newCard";
    protected String picLoc;    //The location of the picture for this card found with name
    protected int maxUses = -1;
    protected String predecessor = "n/a";
    protected ArrayList<String> successor = new ArrayList<String>();
    protected String desc = "New Card";
    protected String animName = "idle";
    protected float angle = 0;
    protected CardSeries series = CardSeries.Raw;
    protected CardTrait trait = CardTrait.Damage;
    protected CardRange range = CardRange.Long;
    protected StatCapsule HP = new StatCapsule();
    protected StatCapsule MP = new StatCapsule();
    protected StatCapsule knockback = new StatCapsule();
    protected StatCapsule speed = new StatCapsule();
    protected StatCapsule deffense = new StatCapsule();
    protected SpecialCapsule special = new SpecialCapsule();
    protected TranslationCapsule trans = new TranslationCapsule();
    protected ArrayList<ParticleEmitter> fx = new ArrayList<ParticleEmitter>();
    protected ArrayList<AudioNode> sfx = new ArrayList<AudioNode>();

    /**
     * @return cardName the name of the card
     */
    public String getName(){return cardName;}
    public int getMaxUses(){return maxUses;}
    public String getPredecessor(){return predecessor;}
    public ArrayList<String> getSuccessor(){return successor;}
    public String getDescription(){return desc;}
    public String getAnimName(){return animName;}
    public CardSeries getSeries(){return series;}
    public CardTrait getTrait(){return trait;}
    public CardRange getRange(){return range;}
    public float getAngle(){return angle;}
    public StatCapsule getHPStat(){return HP;}
    public StatCapsule getMPStat(){return MP;}
    public StatCapsule getKnockbackStat(){return knockback;}
    public StatCapsule getSpeedStat(){return speed;}
    public StatCapsule getDeffenseStat(){return deffense;}
    public SpecialCapsule getSpecialStat(){return special;}
    public TranslationCapsule getTranslationStat(){return trans;}
    public ArrayList<ParticleEmitter> getEffects(){return fx;}
    public ArrayList<AudioNode> getSoundEffects(){return sfx;}
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Section 1\n");
        sb.append("Name       :     ").append(getName()).append("\n");
        sb.append("MaxUses    :     ").append(getMaxUses()).append("\n");
        sb.append("Predecessor:     ").append(getPredecessor()).append("\n");
        for(String k : getSuccessor()){
            sb.append("Successor  : ").append(k).append("\n");
        }
        sb.append("Section 2\n");
        sb.append("Range       :    ").append(getRange()).append("\n");
        sb.append("Angle       :    ").append(getAngle()).append("\n");
        sb.append("Series      :    ").append(getSeries()).append("\n");
        sb.append("Trait       :    ").append(getTrait()).append("\n");
        sb.append("Description :    \n").append(getDescription());
        return sb.toString();
    }
    
    /**
     * This Is The Most Important Method in Call
     * This specific Call Serves only To show visual elements of the Card
     * The Rest of the Card Elements will be presented in the class EFICard
     * @see EFICard
     * @param Spat 
     */
    public void perform(com.jme3.scene.Spatial Spat){
        
    }
    
    @Override 
    public Card clone(){
        CardSetter card = new CardSetter();
        card.setAngle(getAngle()+"");
        card.setAnimName(getAnimName());
        card.setDeffenseStat(getDeffenseStat().toString());
        card.setDescription(getDescription());
        card.setHPStat(getHPStat().toString());
        card.setKnockbackStat(getKnockbackStat().toString());
        card.setMPStat(getMPStat().toString());
        card.setMaxUses(getMaxUses()+"");
        card.setName(getName());
        card.setPredecessor(getPredecessor());
        card.setRange(getRange()+"");
        card.setSeries(getSeries()+"");
        card.setSpecialStat(getSpecialStat().toString());
        card.setSpeedStat(getSpeedStat().toString());
        card.setTrait(getTrait()+"");
        card.setTranslationStat(getTranslationStat().toString());
        for(ParticleEmitter effect:getEffects()){
            card.addEffect(effect.clone());
        }
        for(AudioNode sfx:getSoundEffects()){
            card.addSoundEffect(sfx.clone());
        }
        for(String s:getSuccessor()){
            card.addSuccessor(s+"");
        }
        return card;
    }
}