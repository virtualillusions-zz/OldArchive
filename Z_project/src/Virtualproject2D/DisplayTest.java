package Virtualproject2D;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class DisplayTest {
	public static void main(String[] args){
		//Class can be determine what modes are available
		//This class provides access to the collection of GraphicsDevice objects avalable on the computer
		GraphicsEnvironment gfxEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		//GraphicsDevice object can describe many devices types, such as display screens,printers,
		//and image buffers. It is key to know what screens devices are available
		GraphicsDevice[] screenDevList = gfxEnv.getScreenDevices();
		//If the game will only use the default displays screen, the getDefaultScreenDevice method
		//can be used
		GraphicsDevice defaultScreenDevice = gfxEnv.getDefaultScreenDevice();
		//Use to request the display cababilities of the display device
		DisplayMode[] displayModes = defaultScreenDevice.getDisplayModes();
		//Print all possible displays to screen
		 for (int i = 0; i < displayModes.length; i++) {
	           System.out.println(new DisplayWrapper(displayModes[i]));      
	        }		 
	}
}
