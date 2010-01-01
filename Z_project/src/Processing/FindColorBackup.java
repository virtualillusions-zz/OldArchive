package Processing;

import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PImage;

public class FindColorBackup extends PApplet{
	PImage a;
	int[] aPixels;
	int direction = 1,row =1;
	boolean onetime = true;
	float signal,newWidth;
	
	public void setup() 
	{
	   size(200, 200);
	  aPixels = new int[width*height];
	  frameRate(700);
	  a = loadImage("../Processing/data/TestPicture1.jpg");
	  for(int i=0; i<a.pixels.length; i++) {
	    aPixels[i] = a.pixels[i];
	  }
	 newWidth=width;
	}

	public void draw(){ 
	     
	  if (signal > width*height-1) { 
		System.out.println("Image Successfully Scanned");
		System.exit(0);	  
	  } else if(signal>newWidth){
		  System.out.println("Row "+row+" Done");
		  row++;
		  signal=newWidth;
		  newWidth+=width;
	  }
	  
	 //To detect first pixel of picture without error
	 if(signal==0){
		  if(getCol()!=-1){

		  }
	  }	 
	 //Is the scanner currently on a square
	 else if(getCol()!=-1){
		 //is this the first black of the square
		 if(getPrevCol()==-1){
			  System.out.println("This is the first black");
		 }
		 //Is the scanner currently in the square
		 else{ System.out.println("this is black");}
	  }	   
	  //Is the last pixel of the width of the box
	  else if(getPrevCol()!=-1){
		  System.out.println("This is first white");
	  }
	 //During 
	  else{System.out.println("spacer");}
  
	   etc();
	}
	
	//int origin;
	HashMap<Integer,int[]> square = new HashMap<Integer,int[]>();
	int[] locs = new int[4];
	
	
	private int getCol(){return aPixels[(int)signal];}
	private int getPrevCol(){return aPixels[(int)signal-1];}
	
	private void etc(){
		if(mousePressed) {
		    if(mouseY > height-1) { mouseY = height-1; }
		    if(mouseY < 0) { mouseY = 0; }
		    signal = mouseY*width+mouseX;
		  } else {
		    signal += (float) (direction);  
		  }
		  
		  if(keyPressed) {
		    loadPixels();
		    for (int i=0; i<width*height; i++) { 
		      pixels[i] = aPixels[i];  
		    }
		    updatePixels();
		    rect(signal%width-5, ((int)(signal/width))-5, 10, 10);
		    point(signal%width, ((int)(signal/width)));
		  } else {
		    loadPixels();
		    for (int i=0; i<width*height; i++) { 
		      pixels[i] = aPixels[(int)signal];
		    }
		    updatePixels();
		  }	
	}
	
	
	public void keyPressed(){
		  if(key == CODED) { 			  
			  if (keyCode == UP) {
			      signal=0;
			      } 
			    } 
		  if (keyCode == DOWN) {
		      signal=signal+200;
		      } 
		    } 
		  
	
}
