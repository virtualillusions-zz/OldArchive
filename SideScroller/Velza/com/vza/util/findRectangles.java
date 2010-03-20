package com.vza.util;

import java.awt.Rectangle;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

	
public class findRectangles extends PApplet{


		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		PImage rects;
		ArrayList<Rectangle> foundRectangles = new ArrayList<Rectangle>();
		int MARGIN = 50;
		boolean bShowAlgo = true;
		float depth = 400;
		float volume;
		String levelName;
		ArrayList<Rectangle> rectList = new ArrayList<Rectangle>();
		
		public void setup()
		{
		 levelName="TestStage";
		  if (bShowAlgo)
		  {
		    size(550, 300);
		  }
		  else
		  {
		    size(800, 800, P3D);
		  }
		  rects = loadImage("../Levels/"+levelName+".backDraw.png");
		  volume = rects.width * rects.height * 5;
		  FindRectangles(rects, color(255,255,255), color(0,0,0));

		  if (bShowAlgo)
		  {
		    image(rects, MARGIN, MARGIN);
		    rects.updatePixels();
		    image(rects, 300, MARGIN);
		    noLoop();
		  }
		  System.exit(0);
		}

		public void draw()
		{
		  if (bShowAlgo)
		  {
		    background(color(171,205,239));
		    noFill();
		    stroke(color(0,255,0));
		    for (int i = 0; i < foundRectangles.size(); i++)
		    {
			Rectangle r = (Rectangle) foundRectangles.get(i);
			rect(MARGIN + r.x, MARGIN + r.y, r.width, r.height);
		    }
		  }
		  else
		  {
		    // Inspired by CubicGrid example
		    background(255);
		    image(rects, 0, 0);

		    // Center and spin result
		    translate(width/2, height/2, -depth);
		    rotateY(frameCount * 0.01f);
		    rotateX(frameCount * 0.01f);

		    fill(200, 90);
		    stroke(color(0,0,255));
		    DrawBox(0, 0, MARGIN, rects.width, rects.height, MARGIN);

		    fill(128, 80);
		    stroke(color(0,136,0), 70);
		    for (int i = 0; i < foundRectangles.size(); i++)
		    {
			Rectangle r = (Rectangle) foundRectangles.get(i);
			float boxHeight = volume / r.width / r.height;
			DrawBox(r.x, r.y, 0, r.width, r.height, boxHeight);
		    }
		  }
		}

		// It changes img, pass a copy if you need it intact...
		void FindRectangles(PImage img, int backColor, int rectColor)
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
			  println(r);
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

		// Boxes seems to be drawn centered (not documented?)
		// Draw a box with reference on a corner
		void DrawBox(float x, float y, float z, float w, float h, float d)
		{
		  pushMatrix();
		  translate(-x - w / 2, -y - h / 2, -(z - d / 2));
		  box(w, h, d);
		  popMatrix();
		}
		 

	}
