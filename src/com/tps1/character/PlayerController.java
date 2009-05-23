package com.tps1.character;

import com.jme.input.InputHandler;
import com.jme.scene.Controller;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;
import com.tps1.GameState.DefineGameState;
import com.tps1.GameState.gameSingleton;
import com.tps1.lvlLoader.CopyOfCopyOflevelTester;
import com.tps1.scene.SkyBoxManager.SkyBoxGameState;
import com.tps1.util.ogre;



@SuppressWarnings("serial")
public class PlayerController extends Controller{
characterMove move;Animationator animator;controlManager controls;
/**
 * @auther Kyle Williams
 * @description main controller class handles animations, movements and input
 * @param playerGameState player
 */
	public PlayerController(playerGameState player){
		player.getRootNode().detachAllChildren();
		move = new characterMove(player);
		animator = new Animationator(move, player.getCharNode());
		controls = new controlManager(move, player.getCharNode());
		player.getRootNode().updateGeometricState(0.0f, true);
	}

	@Override
	public void update(float time){
		move.update(time);
		animator.update(time);
		}	
	
	
	public static void main(String[] args) throws InterruptedException{
		 System.setProperty("jme.stats", "set");
		 StandardGame standardGame = new StandardGame("GameControl", StandardGame.GameType.GRAPHICAL, null);
	     standardGame.getSettings().setVerticalSync(false);
		 standardGame.start();     

	        try {SkyBoxGameState.Manager().setActive(true);
				 CopyOfCopyOflevelTester nex = new CopyOfCopyOflevelTester(0,0);
			 	   GameStateManager.getInstance().attachChild(nex);
			 	    nex.setActive(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//gameSingleton.get().lock();

			 Charactertype PLAYER1 = new Charactertype("robot");	
			 PLAYER1.setActive(true);	
						 
			 //gameSingleton.get().unlock();
				     
		     final DefineGameState base = new DefineGameState(); 
		     base.setActive(true);	
		     
		     System.out.println(ogre.charList.keySet());

		   }

}
