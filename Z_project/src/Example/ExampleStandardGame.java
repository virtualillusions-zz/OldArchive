package Example;

import java.util.concurrent.Callable;

import com.jme.util.GameTaskQueueManager;
import com.jmex.editors.swing.settings.GameSettingsPanel;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;

public class ExampleStandardGame{
    public static void main(String[] args) throws Exception {
        System.setProperty("jme.stats", "set");
        StandardGame game = new StandardGame("HelloExample");
        if (!GameSettingsPanel.prompt(game.getSettings())) {
            return;
        }
        game.start();
        GameTaskQueueManager.getManager().update(new Callable<Object>() {
            public Object call() throws Exception {
            	//Initiate Skybox
            	SkyBoxGameState state1 = new SkyBoxGameState(); // Create our game state
                GameStateManager.getInstance().attachChild(state1); // Attach it to the GameStateManager
                state1.setActive(true); // Activate it
                //Initiate Terrain
                TerrainGameState state2 = new TerrainGameState(); // Create our game state
                GameStateManager.getInstance().attachChild(state2); // Attach it to the GameStateManager
                state2.setActive(true); // Activate it
                return null;
            } 
        }
        ).get();
    }
}