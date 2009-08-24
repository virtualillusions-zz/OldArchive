package Image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class BufferedExample_BAD {
/*The bufferedImage class is particulary good at giving the programmer direct acess to an image's
 *   actual data. With this access, a programmer can change the color, add image filtering, or just
 *   modify a single pixel.
 * Although this mechanism is great, the downside is that images held in system memory,
 *   which greatly reduces computers performace
 */
	//The resource location of the image
	private static final String imageResourceName = "/rock.png";
	//The handle to the buffered image we will load and display
	private BufferedImage image = null;
	//Read in the image to display and put it in a BufferedImage
	private void loadImage()
	{
		try
		{
			URL imageURL = getClass().getResource(imageResourceName);
			if(imageURL==null)
			{
				System.out.println("Can't load"+imageResourceName);
				return;
			}
			image = ImageIO.read(imageURL);
		}
		catch(IOException x)
		{
			x.printStackTrace();
		}
	}
}
