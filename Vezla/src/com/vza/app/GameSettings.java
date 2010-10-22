/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vza.app;
 
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handels All of the gameSettings All Settings including Sound, Video, and Player Controls should be registered with this class
 * @author Kyle Williams
 */
public class GameSettings extends Properties{
    protected String name;
    protected String location="assets/";
    protected String extension=".png";
    public GameSettings(){
        name = "Game";
        defaults=loadDefaults();
        load();
    }

    /**
     * Clears properties table and saves it effectvly using defualt values
     */
    public void useDefualts(){this.clear();save();}

    /**
     * Converts a string key mapping and returns it as an integer
     * @param key
     * @return
     */
    public int getIntProperty(String key){return Integer.parseInt(getProperty(key).trim());}

    /**
     * Registers a keyBinding with the inputManager and places it into GameSettings
     * @param key the name of the binding
     * @param value the mouse button value to register
     */
    public void putAndRegisterInputKey(String key,int value){
          com.vza.director.Director.getApp().getInputManager().addMapping(key, new KeyTrigger(value));
          putSetting(key,value);
    }
    /**
     * Registers a mouseBinding with the inputManager and places it into GameSettings
     * @param key the name of the binding
     * @param value the mouse button value to register
     */
    public void putAndRegisterMouseBtn(String key,int value){
        com.vza.director.Director.getApp().getInputManager().addMapping(key, new MouseButtonTrigger(value));
        putSetting(key,value);
    }

    /**
     * Updates the properties file while changing or adding a value
     * PLEASE USE OVER PUT
     * @param key
     * @param value
     */
    public void putSetting(String key, Object value){
        if(value instanceof Integer) super.put(key, Integer.toString((Integer)value));
        else super.put(key, value);
        save();
    }
    /**
     * Saves The Game Settings To a properties file
     */
    public void save(){
        try {
            FileOutputStream os = new FileOutputStream(location+name+extension);
            this.store(os, "updated");
            os.flush();
            os.close();
        } catch (IOException ex) {
            Logger.getLogger(GameSettings.class.getName()).log(Level.WARNING,
                    "Unable to save file\npossible unexistant directory", ex);
        }
    }
    /**
     * Loads The Game Settings from a properties File
     */
    public void load(){
        try {
            FileInputStream in = new FileInputStream(location+name+extension);
                this.load(in);
                in.close();        
        } catch (IOException ex) {
            Logger.getLogger(GameSettings.class.getName()).log(Level.WARNING,
                    "Missing "+name+" File \n will use defualts instead", ex);
                 save();
        }
    }

    private Properties loadDefaults(){
        Properties gameDefault = new Properties();
        gameDefault.put("Time", Integer.toString(-1));
        gameDefault.put("GUI-Select",Integer.toString(KeyInput.KEY_RETURN));
        gameDefault.put("GUI-Back",Integer.toString(KeyInput.KEY_BACK));
        gameDefault.put("GUI-Up",Integer.toString(KeyInput.KEY_UP));
        gameDefault.put("GUI-Down", Integer.toString(KeyInput.KEY_DOWN));
        gameDefault.put("GUI-Left", Integer.toString(KeyInput.KEY_LEFT));
        gameDefault.put("GUI-Right", Integer.toString(KeyInput.KEY_RIGHT));
        return gameDefault;
    }
}
