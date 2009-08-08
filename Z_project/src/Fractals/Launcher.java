package Fractals;

import jmetest.TutorialGuide.HelloAnimation;

import com.jme.app.SimpleGame;
import com.jme.app.AbstractGame.ConfigShowMode;

public class Launcher extends SimpleGame {
	    public static void main(String[] args) {
	    	Launcher app = new Launcher();
	        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
	        app.start();
	    }

		@Override
		protected void simpleInitGame() {
			Menger menger = new Menger("Menger");			
			Sierpinski sierpinski = new Sierpinski("Sierpinski");	
			menger.getLocalTranslation().setX(3/2);
			sierpinski.getLocalTranslation().setX(-3/2);
			rootNode.attachChild(menger);
			rootNode.attachChild(sierpinski);
		}
}
