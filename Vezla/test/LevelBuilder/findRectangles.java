

import java.awt.Rectangle;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

public class findRectangles extends PApplet{


     public void starts(String levelNamed){
         // Initialize and start the applet
          this.init();
          this.start();
         levelName = levelNamed;
         this.setup();
     }


/////////////////////////////////////////////////////////////////////////////////////////////////////
                 private static final long serialVersionUID = 1L;
		PImage rects;
		private ArrayList<Rectangle> foundRectangles = new ArrayList<Rectangle>();
		private int MARGIN = 50;
		private float depth = 400, volume;
		private static String levelName="testStage";
		private static ArrayList<Rectangle> rectList = new ArrayList<Rectangle>();
		
		public void setup()
		{		System.out.println(123123123);
		    size(550, 300);

                 rects = loadImage("../LevelBuilder/"+levelName+".backDraw.png");
                 
		  volume = rects.width * rects.height * 5;
		  FindRectangles(rects, color(255,255,255), color(0,0,0));

		  
		    image(rects, MARGIN, MARGIN);
		    rects.updatePixels();
		    image(rects, 300, MARGIN);
		    noLoop();		  
                }

            public void draw(){
		    background(color(171,205,239));
		    noFill();
		    stroke(color(0,255,0));
		    for (int i = 0; i < foundRectangles.size(); i++)
		    {
			Rectangle r = (Rectangle) foundRectangles.get(i);
			rect(MARGIN + r.x, MARGIN + r.y, r.width, r.height);
		    }
            }

		

		// It changes img, pass a copy if you need it intact...
	private void FindRectangles(PImage img, int backColor, int rectColor)
		{
		  int iw = img.width;
		  int ih = img.height;
		  println("Image: " + iw + "x" + ih);

		  int markColor = color(255,0,0);

		  // Top-left coordinate
		  int left, top;
		  // Botttom-right coordinate
		  int right, bottom;
		  // Current pos
		  int posX = 0, posY = 0;
		  while (posY < ih)
		  {
		    while (posX < iw)
		    {
			if (img.pixels[posX + iw * posY] == rectColor)
			{
			  // Top-left corner
			  left = posX;
			  top = posY;
			  // Walk the upper side
			  while (posX < iw && img.pixels[posX + iw * posY] == rectColor)
			  {
			    posX++;
			  }
			  right = posX - 1;
			  // Walk the right side, marking it (and the left side too)
			  while (posY < ih && img.pixels[right + iw * posY] == rectColor)
			  {
			    img.pixels[left + iw * posY] = img.pixels[right + iw * posY] = markColor;
			    posY++;
			  }
			  bottom = posY - 1;
			  posY = top;
			  Rectangle r = new Rectangle(left, top, right - left, bottom - top);
			  foundRectangles.add(r);
			  //println(r);
			  rectList.add(r);
			}
			else if (img.pixels[posX + iw * posY] == markColor)
			{
			  // A known rectangle, skip it
			  do
			  {
			    posX++;
			  } while (img.pixels[posX + iw * posY] != markColor);
			  posX++;
			}
			else
			{
			  posX++;
			}
		    }
		    posX = 0;
		    posY++;
		  }
		}

              public ArrayList<Rectangle> getRectangles(){ return rectList; 
              }



              
	}