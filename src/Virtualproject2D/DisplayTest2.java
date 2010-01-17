package Virtualproject2D;

import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;

public class DisplayTest2 {
	public static DisplayMode[] requestedDisplayModes = new DisplayMode[]{
			new DisplayMode(1280,1024,32,DisplayMode.REFRESH_RATE_UNKNOWN),
			new DisplayMode(1024,768,32,DisplayMode.REFRESH_RATE_UNKNOWN),
			new DisplayMode(800,600,16,DisplayMode.REFRESH_RATE_UNKNOWN),
			new DisplayMode(640,480,16,DisplayMode.REFRESH_RATE_UNKNOWN)
	};
	public static DisplayMode[] displayModes = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayModes();
	//method to compare the video mode "wishlist"
	//This method searches through the returned values in the displayModes array trying to find a match
	//for the height,width, and bit depth of the display.
	//It returns the highest possible matches for all three of these factors
	public static DisplayMode findRequestedMode(){
		DisplayMode best = null;
		//loop through each of our requested modes
		for(int rIndex=0; rIndex < requestedDisplayModes.length; rIndex++){
			//loop through each of the available modes
			for(int mIndex=0; mIndex < displayModes.length; mIndex++){
				if(displayModes[mIndex].getWidth()==requestedDisplayModes[rIndex].getWidth() && 
						displayModes[mIndex].getHeight()==requestedDisplayModes[rIndex].getHeight()&&
							displayModes[mIndex].getBitDepth()==requestedDisplayModes[rIndex].getBitDepth())
				{//we found a resolution match
					if (best==null)
					{
						//if the refresh rate was specified try to match that as well
						if(requestedDisplayModes[rIndex].getRefreshRate()!=DisplayMode.REFRESH_RATE_UNKNOWN)
						{
							if(displayModes[mIndex].getRefreshRate()==requestedDisplayModes[rIndex].getRefreshRate())
							{
								best = displayModes[mIndex];
								return best;
							}
						}
						else 
						{
							best = displayModes[mIndex];
							return best;
						}
					}
				}
			}
		}
		//no matching modes so we return null
		return best;
	}
}
