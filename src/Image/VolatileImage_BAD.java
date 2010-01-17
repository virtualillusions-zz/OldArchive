package Image;

import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class VolatileImage_BAD{
/*Hardware acceleration of image drawing operations
 * Store image in systems Video card memory(VRAM) instead of computer's main system memory
 */
	//The resource location of the image
	private static final String imageResourceName = "/rock.png";
	//The handle to the buffered image we will load and display
	private BufferedImage bufferedImage = null;
	private VolatileImage image = null;
	//Read in the image to display and put it in a VolitileImage object
	private void loadImage()
	{
		try
		{
			URL imageURL = getClass().getResource(imageResourceName);
			if(imageURL==null)
			{
				System.out.println("unable to locate the resource"+imageResourceName);
				return;
			}
			bufferedImage = ImageIO.read(imageURL);
			//image = getGraphicsConfiguration().createCompatibleVolitileImage(bufferedImage.getWidth(),
			//																 bufferedImage.getHeight());
			//image.getGraphics().drawImage(bufferedImage,0,0,this);
		}
		catch(IOException x)
		{
			x.printStackTrace();
		}
	}
	
}
