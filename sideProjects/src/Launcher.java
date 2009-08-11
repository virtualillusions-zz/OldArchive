import java.util.concurrent.Callable;

import Levels.DefaultTest;
import TestCharacter.CharacterStats;
import TestCharacter.characterManager;
import TestTerrain.sceneManager;

import com.jme.renderer.ColorRGBA;
import com.jme.util.GameTaskQueueManager;
import com.jmex.game.StandardGame;
import com.jmex.game.state.DebugGameState;
import com.jmex.game.state.GameStateManager;

import util.gameSingleton;

public class Launcher {
    public static void main(String[] args) throws Exception{
	  System.setProperty("jme.stats", "set");
      final StandardGame standardGame = new StandardGame("GameControl", StandardGame.GameType.GRAPHICAL, null);
      preStartup(standardGame);
      standardGame.start();  
      

      
       
      gameSingleton.init(standardGame).setActive(true);
     // new TestFly();
     sceneManager.Manager().setActive(true);
    // sceneManager.Manager().getTerrainHandler().registerTerrain(new DefaultTest());
     sceneManager.Manager().getTerrainHandler().registerTerrains();
    sceneManager.Manager().getTerrainHandler().build("DefaultTest");
     //gameSingleton.getStats().setWorldScale(50);
     characterManager.get().setActive(true);
     characterManager.get().load("ninja", "TestModel");
     //characterManager.get().Manager("TestModel").setAsHuman();
      
    //  DebugGameState bugger = new DebugGameState();
     // GameStateManager.getInstance().attachChild(bugger);
     // bugger.getRootNode().attachChild(characterManager.get().getRootNode());
     // bugger.setActive(true);
    }
    
    public static void preStartup(final StandardGame standardGame){
    			    	standardGame.getSettings().setVerticalSync(false);   
    			        standardGame.getSettings().setFullscreen(true);
    			        standardGame.getSettings().setWidth(1280);
    			        standardGame.getSettings().setHeight(1024);
    			        standardGame.getSettings().setDepth(16);
    			        standardGame.getSettings().setFrequency(60);
    			        standardGame.setBackgroundColor(new ColorRGBA(.9411764706f,1.0f,.9450980392f,1.0f));

    	}
        
        
        


}
