package Virtualproject2D;

import com.jme.app.AbstractGame;
import com.jme.app.SimpleGame;
import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Box;


/**
 * startDate: June 03 2008
 * @author Kyle Williams
 *endDate:~
 */
public class Virtualproject2D extends SimpleGame {
	public static void main(String[] args) {
		Virtualproject2D app = new Virtualproject2D();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}
	
	protected void simpleInitGame() {
		int x=settings.getWidth();
		int y=settings.getHeight();
		
   display.getRenderer().setBackgroundColor(ColorRGBA.white);
  display.setTitle("Virtualproject2D");

	}
	
	protected void simpleUpdate()
	/**
	 * I am overriding the simpleUpdate() function in SimpleGame. This function is called every frame. 
	 * By putting queries here, I can poll for a key every frame. 
	 * Which is exactly what I do right off the bat:
	 */
	{

	}
	

}

