/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.deck.stats;

/**
 * The Stat capsule is a private class for storing card statistics
 * This Base Class Only Handles Translation and should be used accordingly
 * Translation: Direction   Speed
 * should be know Direction = Forward    Left
 * @author Kyle Williams
 */
public class TranslationCapsule{
    private int[][] dir = new int[][]{{0,0},{0,0}};
    private int[] speed = new int[]{0,0};
    
    public TranslationCapsule(){}
    
    public TranslationCapsule(String params){
        String[] parameter = params.split(";");
        String[] temp = parameter[0].split("|");
        String[] t = temp[0].split(",");
        dir[0][0] = t[0].equals("")?Integer.parseInt(t[0]):0;
        dir[0][1] = t[1].equals("")?Integer.parseInt(t[1]):0; 
                 t = temp[1].split(",");
        dir[1][0] = t[0].equals("")?Integer.parseInt(t[0]):0;
        dir[1][1] = t[1].equals("")?Integer.parseInt(t[1]):0;       
                temp = parameter[1].split("|");
        speed[0] = temp[0].equals("")?Integer.parseInt(temp[0]):0;
        speed[1] = temp[1].equals("")?Integer.parseInt(temp[1]):0; 
    }
    
    public int getForward(){return dir[0][0];}

    public int getLeft(){return dir[0][1];}

    public int getSpeed(){return speed[0];}
    
    public int getSelfForward(){return dir[1][0];}

    public int getSelfLeft(){return dir[1][1];}

    public int getSelfSpeed(){return speed[1];}
    
    public void setForward(int param){dir[0][0]=param;}

    public void setLeft(int param){dir[0][1]=param;}

    public void setSpeed(int param){speed[0]=param;}
    
    public void setSelfForward(int param){dir[1][0]=param;}

    public void setSelfLeft(int param){dir[1][1]=param;}

    public void setSelfSpeed(int param){speed[1]=param;}
    
    @Override
     public String toString(){
        return  dir[0][0]+","+dir[0][1]+"|"+dir[1][0]+","+dir[1][1]+";"+speed[0]+"|"+speed[1];
    }
}
