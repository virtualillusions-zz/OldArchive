package Image;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;

public class ManagedImages_Best {
	private Image image;
	//WORK OKAY 20 fps
	//Read in the image to display using swing ImageIcon class
	private void loadImage1(URL imageURL){image = new ImageIcon(imageURL).getImage();}
	//Read in am o,age Toolkit's createImage(URL) method
	private void loadImage2(URL imageURL){image = Toolkit.getDefaultToolkit().createImage(imageURL);}
	//Read in an image using Toolkits class's method getImage(URL)
	private void loadImage3(URL imageURL){image = new ImageIcon(imageURL).getImage();}
	//WORK BEST 60 fps
	//Read in the image to display using the Component class's method createImage(width,height)
	private void loadImage4(URL imageURL)
	{
		//pack();//component must be displayable
		//image = createImage(bufferedImage.getWidth(),bufferedImage.getHeight());
		//Graphics2D g2d = (Graphics2D)image.getGraphics();
		//g2d.setComposite(AlphaComposite.Src);
		//g2d.drawImage(bufferImage,0,0,this);		
	}
	//Read in the image to using Graphics COnfiguration create CompatibleImage(width,height,transparency) method
	private void loadImage5(URL imageURL)
	{
		//image = getGraphicsConfiguration().createCompatibleImage(bufferedImage.getWidth,bufferedImage.getHeight,Transparency.BITMASK);
		//image.getGraphics().drawImage(bufferedImage,0,0,this);
	}
	
}
