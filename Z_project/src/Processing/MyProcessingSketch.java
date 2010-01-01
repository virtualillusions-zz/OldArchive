package Processing;

import java.util.Random;

import processing.core.PApplet;

public class MyProcessingSketch extends PApplet{
	
	public static void main(String args[]) {
	    PApplet.main(new String[] { "--present", "Processing.MyProcessingSketch" });
	  }
	
	public void setup(){
		size(800,600);
		background(0);
	}
	
	public void draw(){
		stroke(255);
		if(mousePressed){
			line(mouseX,mouseY,pmouseX,pmouseY);
		}
	}
	
	public void mousePressed(){
		Random r = new Random();		
		background(r.nextInt(254)+1, r.nextInt(254)+1, r.nextInt(254)+1);
	}	
}
