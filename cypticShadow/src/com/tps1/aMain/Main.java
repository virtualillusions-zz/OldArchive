package com.tps1.aMain;

import com.jme.system.GameSettings;
import com.jme.util.resource.ResourceLocator;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;

import com.tps1.GameState.ZGame;
import com.tps1.util.ErrorHandler;

import java.io.IOException;
import java.net.URISyntaxException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kyle Williams
 */ 
public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName());
    private static GameSettings settings=null;
   public static  ZGame game;
    private void init(){
        ErrorHandler.bindUncoughtExceptionHandler();
        Game.setDebug(true);
        
        settings = Game.getSettings();
        logger.fine("Settings loaded from registry");
        
        GameSettingsDialog1 dialog = new GameSettingsDialog1(settings);
        dialog.configure();
        
        try{
            dialog.waitFor();
            
           if (!dialog.isInitGameAllowed()){
               logger.fine("Game initialization canceled by user");
                shutDown();
            }
            
           settings.save();
            logger.fine("Settings saved to registry");
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, "Interrupt ");
        } catch (IOException ex){
            logger.log(Level.WARNING, "Failed to save settings to system", ex);
        }
      
       try {
        	 logger.fine("Setting up locators");
            ResourceLocator textureLocator = new SimpleResourceLocator(Main.class.getResource("/com/tps1/data/textures/"));
            ResourceLocator imageLocator = new SimpleResourceLocator(Main.class.getResource("/com/tps1/data/images/"));

            ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, textureLocator);
            ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, imageLocator);
       
        } catch (URISyntaxException ex){
            ErrorHandler.reportError("Texture directory missing", ex);
            logger.info("Missing Texture Directory");
        }                
    }
    
    private void start() {
    	try{
        game=new ZGame();
    	game.init();
    	logger.fine("Display started");
    	game.start();
    	}catch (InterruptedException ex) {
            ErrorHandler.reportError("Interrupt while building game", ex);
        } catch (InstantiationException ex){
            ErrorHandler.reportError("Game implementation cannot be loaded", ex);
        } catch (Throwable ex){
            ErrorHandler.reportError("Error Loading Game", ex);
            shutDown();
        }
        
/**
        try{
            JmeContext context = JmeContext.create(LWJGLContext.class, JmeContext.CONTEXT_WINDOW);
            context.setSettings(Game.getSettings());
            context.start();
            logger.fine("Display started");
            context.waitFor();
            logger.info("Display created successfuly");
            
            InputPass input = new InputPass(null, true);
            context.getPassManager().add(input);
            
           ExitListenerPass exitListener = new ExitListenerPass();
            context.getPassManager().add(exitListener);
            
          JmeConsole jmeConsole = new JmeConsole();
            jmeConsole.addConsoleListener(new ScriptSystem(jmeConsole, true));
            context.getPassManager().add(jmeConsole);
        } catch (InterruptedException ex) {
            ErrorHandler.reportError("Interrupt while creating display", ex);
        } catch (InstantiationException ex){
            ErrorHandler.reportError("Display implementation cannot be loaded", ex);
        }*/
    }
    
    public static void main(String[] args) throws Exception{
        Main main = new Main();
        logger.info("Initializing System...");
        main.init();
     // Invoke the garbage collector (may cause a delay)
     //[Remember best to use before explicitly invoke the garbage collector during idle periods, 
        //or before creating a large number of objects Also remember to null out pointers at the earliest]
        //time
    	System.gc();
    	logger.info("Starting Game...");
        main.start();   
        
    }
    
    private void shutDown()
	{
		logger.info("Shutting down Game...");
		//TODO: Shutdown game
		logger.info("Goodbye!");
		System.exit(0);
	}
}
