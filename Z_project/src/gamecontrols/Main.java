package gamecontrols;
 
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;
 /**
  * 
  * @author Kyle Williams
  *OK, what we want to do here is a little application using the GameControl and GameControlManager classes. These classes are much easier to use than the old input system (Thanks, darkfrog!). We use StandardGame, because SimpleGame has too much “magic” going on. To keep things simple, we will do nothing but control a little cute cube with a monkey on it. Sounds simple and is simple. This project uses JME2.
First a very basic main class, which just creates a single GameState:
  */
public class Main {
 
    private static StandardGame standardGame;
 
    public static void main(String[] args) {
        standardGame = new StandardGame("GameControl", StandardGame.GameType.GRAPHICAL, null);
        standardGame.start();
 
        GameState cubeState = new CubeGameState();
        GameStateManager.getInstance().attachChild(cubeState);
        cubeState.setActive(true);
    }
}