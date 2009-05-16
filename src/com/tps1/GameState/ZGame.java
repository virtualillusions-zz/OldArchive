package com.tps1.GameState;
 
import java.util.logging.Logger;



import com.tps1.scene.SkyBoxManager.SkyBoxGameState;
import com.tps1.util.SysInfo;

import com.jmex.game.StandardGame;

/**
 * 
 * @author Kyle Williams
 *	@info The Main Game routine
 */
public class ZGame{
	public static  StandardGame ZGame;
    //public static StandardGame ZGame;
    private static Logger logger = Logger.getLogger(ZGame.class.getName());
    /**Creates StandardGame and sets the settings*/
    public void init(){
    	System.setProperty("jme.stats", "set");
    	ZGame = new StandardGame(SysInfo.gameTitle);
     // GameSettings settings = new PreferencesGameSettings(Preferences.userRoot().node(SysInfo.GameName));; //  settings1=settings; //settings GameSettings settings1 //try{  // settings.save();} // catch (IOException ex){ // logger.log(Level.WARNING, "Failed to save settings to system", ex);} // if (!GameSettingsPanel.prompt(settings)) {// return;   //  }
   	 ZGame.start();
    }
    /**Begins GameQueue*/
    public void start() throws Exception{
			SkyBoxGameState.Manager().setActive(true);		     
     }
    
    //just to test the program directly
    public static void main(String[] args) throws Exception{
        ZGame main = new ZGame();
        logger.info("Initializing System...");
        main.init();
        logger.info("Starting Game...");
        main.start();           
    }    
}
