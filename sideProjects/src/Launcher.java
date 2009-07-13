import Levels.DefaultTest;
import TestTerrain.sceneManager;

import com.jmex.game.StandardGame;

import util.gameSingleton;

public class Launcher {
    public static void main(String[] args) throws Exception{
	  System.setProperty("jme.stats", "set");
      final StandardGame standardGame = new StandardGame("GameControl", StandardGame.GameType.GRAPHICAL, null);
      standardGame.getSettings().setVerticalSync(false);   
      standardGame.getSettings().setFullscreen(true);
      standardGame.getSettings().setWidth(1280);
      standardGame.getSettings().setHeight(1024);
      standardGame.getSettings().setDepth(32);
      standardGame.getSettings().setFrequency(60);
      standardGame.start();  
      
      gameSingleton.init(standardGame).setActive(true);
     // new TestFly();
     sceneManager.Manager().setActive(true);
     sceneManager.Manager().getTerrainHandler().registerTerrain(new DefaultTest());
     sceneManager.Manager().getTerrainHandler().build("DefaultTest");
      
      
   //   DebugGameState bugger = new DebugGameState();
   //   GameStateManager.getInstance().attachChild(bugger);
    //  bugger.setActive(true);
    }

}
