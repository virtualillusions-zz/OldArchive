package Processing;

import java.applet.Applet;
import java.awt.Graphics;

import javax.swing.JApplet;

public class drawRectangles extends JApplet{

	 
	 public void drawRectangle(int RectX, int RectY, int RectWidth, int RectHeight)
	  {
	    repaint();
	    Graphics g = getGraphics();
	    g.fillRect(RectX, RectY, RectWidth, RectHeight);
	  }
	 
	 public void paint(Graphics g) {
		    drawRectangle(8,0,89,9);
		 	drawRectangle(120,9,67,35);
	        drawRectangle(16,28,81,24);
	        drawRectangle(15,68,172,28);
	        drawRectangle(11,110,148,49);
	        drawRectangle(152,170,15,27);
	        drawRectangle(8,173,42,7);
	        drawRectangle(74,174,24,21);
	 }
	 
}
