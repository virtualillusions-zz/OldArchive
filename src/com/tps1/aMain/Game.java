package com.tps1.aMain;

import com.jme.system.GameSettings;
import com.jme.system.PreferencesGameSettings;
import com.tps1.util.SysInfo;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public final class Game {
 
    private static GameSettings settings;
    
    private static boolean debugMode;
    
    /**
     * Initialize settings
     */
    static {
        settings = new PreferencesGameSettings(Preferences.userRoot().node(SysInfo.gameTitle));
        
        settings.set("title", SysInfo.GameName + " "
                            + SysInfo.GameVersion + " "
                             + SysInfo.ReleaseNumber);
        
        settings.setSamples(0);
        settings.setDepthBits(8);
        settings.setAlphaBits(0);
        settings.setStencilBits(0);
        settings.setFramerate(-1);
        
    }
    
    /**
     * Enable or disable debug mode.
     * 
     * During debug mode, more logging messages are displayed
     * and an in-game console is available.
     */
    public static void setDebug(boolean debug){
        debugMode = debug;
        
        if (debug){
            Logger.getLogger("").setLevel(Level.FINEST);
        }else{
            Logger.getLogger("").setLevel(Level.WARNING);
        }
    }
    
    /**
     * Returns true if debug mode is set.
     */
    public static boolean isDebug(){
        return debugMode;
    }
    
    /**
     * Settings for the game. Cannot be null.
     */
    public static GameSettings getSettings(){
        return settings;
    }
    
}
