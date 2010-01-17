

import character.characterManager;

import com.jme.renderer.ColorRGBA;
import com.jmex.game.StandardGame;

import terrain.sceneManager;
import util.gameSingleton;

public class Launcher {
    public static void main(String[] args) throws Exception{
	  System.setProperty("jme.stats", "set");
      final StandardGame standardGame = new StandardGame("GameControl", StandardGame.GameType.GRAPHICAL, null);
      preStartup(standardGame);
      standardGame.start();  
      

      
       
      gameSingleton.init(standardGame).setActive(true);

     sceneManager.Manager().setActive(true);
    sceneManager.Manager().getTerrainHandler().build("DefaultTest");
     gameSingleton.getStats().setWorldScale(10);
     characterManager.get().setActive(true);
     characterManager.get().load("ninja", "TestModel");
     characterManager.get("TestModel").setAsHuman();

   //  System.out.println(characterManager.get().getcharList());
     //System.out.println(characterManager.get().getcharList().size());
     //System.out.println(ogre.getcharList());
     //System.out.println(ogre.getcharList().size());
     //characterManager.get().Manager("TestModel").setAsHuman();
      
    //  DebugGameState bugger = new DebugGameState();
     // GameStateManager.getInstance().attachChild(bugger);
     // bugger.getRootNode().attachChild(characterManager.get().getRootNode());
     // bugger.setActive(true);
    }
    
    private static void preStartup(final StandardGame standardGame){
    			    	standardGame.getSettings().setVerticalSync(false);   
    			        standardGame.getSettings().setFullscreen(true);
    			        standardGame.getSettings().setWidth(1280);
    			        standardGame.getSettings().setHeight(1024);
    			        standardGame.getSettings().setDepth(16);
    			        standardGame.getSettings().setFrequency(60);
    			        standardGame.setBackgroundColor(new ColorRGBA(.9411764706f,1.0f,.9450980392f,1.0f));

    	}
        
        
        


}
