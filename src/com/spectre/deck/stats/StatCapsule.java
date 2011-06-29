/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.deck.stats;

/**
 * The Stat capsule is a private class for storing card statistics
 * This Base Class Only Handles 5 different stats and should be used accordingly
 * HP:		Amount      Rate    N/A
 * MP:		Amount      Rate    N/A
 * Knockback:	Amount      Chance  N/A		
 * Speed        Amount      Rate    Duration		
 * Defense	Amount      Rate    Duration
 * Remember: to impliment Expression.java which will allow the use of complex mathematical equations such as Damage=(x/(o*5))+1
 * @author Kyle Williams
 */
public class StatCapsule{
    private int[] amount = new int[]{0,0};
    private int[] rate = new int[]{0,0};
    private int[] misc = new int[]{0,0};
    
    public StatCapsule(){}
    
    public StatCapsule(String params){
        String[] parameter = params.split(";");
        String[] temp = parameter[0].split("|");
        amount[0] = temp[0].equals("")?Integer.parseInt(temp[0]):0;
        amount[1] = temp[1].equals("")?Integer.parseInt(temp[1]):0;
                temp = parameter[1].split("|");
        rate[0] = temp[0].equals("")?Integer.parseInt(temp[0]):0;
        rate[1] = temp[1].equals("")?Integer.parseInt(temp[1]):0;
                temp = parameter[2].split("|");
        misc[0] = temp[0].equals("")?Integer.parseInt(temp[0]):0;
        misc[1] = temp[1].equals("")?Integer.parseInt(temp[1]):0; 
    }
    /*
     * Returns the amount of the stat that will be changed
     */
    public int getAmount(){return amount[0];}
    /**
     * The rate at which the stat will change by
     * @return 
     */
    public int getRate(){return rate[0];}       
    /**
     * Miscellaneous parameters specific to each stat
     * @return miscellaneous parameters
     */
    public int getMisc(){return misc[0];}
    /*
     * Returns the amount of the stat that will be changed that effects the caster
     */
    public int getSelfAmount(){return amount[1];}
    /**
     * The rate at which the stat will change by that effects the faster
     * @return 
     */
    public int getSelfRate(){return rate[1];}       
    /**
     * Miscellaneous parameters which applies to the caster specific to each stat
     * @return miscellaneous parameters
     */
    public int getSelfMisc(){return misc[1];} 
    
    public void setAmount(int param){amount[0]=param;}
    public void setRate(int param){rate[0]=param;}
    public void setMisc(int param){misc[0]=param;}
   
    public void setSelfAmount(int param){amount[1]=param;}
    public void setSelfRate(int param){rate[1]=param;}
    public void setSelfMisc(int param){misc[1]=param;}
    
    
    @Override
     public String toString(){
        return amount[0]+"|"+amount[1]+";"
              +rate[0]+"|"+rate[1]+";"
              +misc[0]+"|"+misc[1];
    }
}
