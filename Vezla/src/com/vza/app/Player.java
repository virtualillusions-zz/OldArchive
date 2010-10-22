/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vza.app;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import java.util.Properties;

/**
 * Abstraction of Player class from the game
 * @author Kyle Williams
 */
public final class Player extends GameSettings{
    /**
     * new player object be sure to call setMappings directly afterwards
     * @param name
     */
    public Player(String name){
        this.name=name;
        defaults = loadDefaults();
        load();
    }

    public String getName(){return name;}
    /**
     * BE SURE TO CALL THIS THE SECOND or the buttons will not work
     * Sets up all mappings with the Game
     */
    public void addIntoPlay(){
       for(String key : this.stringPropertyNames()) {
           if(!key.equals("Difficulty")&&!key.equals("High-Score")){
                int value = getIntProperty(key);
                com.vza.director.Director.getApp().getInputManager().addMapping(key, new KeyTrigger(value));
            }
        }
    }

    /**
     * Registers a button with the InputManager be sure to use either Jump ,Ccrouch, Forward,Bbackward, Attack1-4, comboMod or specialMod
     * @param button
     * @param key
     */
    public void setButton(String button, int key){
        if(!button.equals("Difficulty")&&!button.equals("High-Score")){
        String bttn = name+"-"+button;
        this.put(bttn, Integer.toString(key));
        com.vza.director.Director.getApp().getInputManager().deleteMapping(bttn);
        com.vza.director.Director.getApp().getInputManager().addMapping(bttn, new KeyTrigger(key));
        }
    }

    /*
     * removes the user from the game 
     */
    public void removeFromPlay(){
        for(String key : this.stringPropertyNames())
           if(!key.equals("Difficulty")&&!key.equals("High-Score"))
               com.vza.director.Director.getApp().getInputManager().deleteMapping(key);
    }

    public void permanentlyDeletePlayer(){
    removeFromPlay();
    // A File object to represent the filename
    java.io.File f = new java.io.File(location+name+extension);

    // Make sure the file or directory exists and isn't write protected
    if (!f.exists())
      throw new IllegalArgumentException("Delete: no such file or directory: " + name);

    if (!f.canWrite())
      throw new IllegalArgumentException("Delete: write protected: "
          + name);

    // Attempt to delete it
    boolean success = f.delete();

    if (!success)
      throw new IllegalArgumentException("Delete: deletion failed");
    }

    private Properties loadDefaults(){
        Properties gameDefault = new Properties();
        gameDefault.put("Difficulty", "Normal");
        gameDefault.put("High-Score", Integer.toString(0));
        gameDefault.put(name+"-Up", Integer.toString(KeyInput.KEY_W));
        gameDefault.put(name+"-Down", Integer.toString(KeyInput.KEY_S));
        gameDefault.put(name+"-Left", Integer.toString(KeyInput.KEY_A));
        gameDefault.put(name+"-Right", Integer.toString(KeyInput.KEY_D));
        gameDefault.put(name+"-Attack1", Integer.toString(KeyInput.KEY_J));
        gameDefault.put(name+"-Attack2", Integer.toString(KeyInput.KEY_I));
        gameDefault.put(name+"-Attack3", Integer.toString(KeyInput.KEY_K));
        gameDefault.put(name+"-Attack4", Integer.toString(KeyInput.KEY_L));
        gameDefault.put(name+"-comboMod", Integer.toString(KeyInput.KEY_SPACE));
        gameDefault.put(name+"-specialMod", Integer.toString(KeyInput.KEY_C));
        return gameDefault;
    }
}