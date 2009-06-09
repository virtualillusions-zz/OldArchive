package Example;

import java.net.URL;

import javax.swing.ImageIcon;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.util.ImageBasedHeightMap;
import com.jmex.terrain.util.MidPointHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;


/**
 * Start Date: 6/25/08
 *
 * This program introduces jME's terrain utility classes and how they are used.  It
 * goes over ProceduralTextureGenerator, ImageBasedHeightMap, MidPointHeightMap, and
 * TerrainBlock.
 * 
 * @author Kyle Williams
 */

public class HelloTerrainoriginal extends SimpleGame{
	
	public static void main(String[] args){
		HelloTerrainoriginal app = new HelloTerrainoriginal();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}
	
	protected void simpleInitGame(){
		//First a hand made terrain
		homeGrownHeightMap();
		//Next an automatically generated terrain with a texture
		generatedHeightMap();
		//Finally a terrain loaded from a gray-scale image with fancy textures on it.
		complexTerrain();
		}
	private void homeGrownHeightMap(){
		//The map for our terrain. Each value is a height on the terrain
		/**Look at the map array. It is just an array of integer values. Each integer represents a height. This map will be low down the 
		 * diagonal, and slope up just like the integer values do.
		*/
		float[] map=new float[]{
				1,2,3,4,
				2,1,2,3,
				3,2,1,2,
				4,3,2,1
				};
		
		/**Create a terrain block. Our integer values will scale on the map 2x larger x, and 2x larger z. 
		 * Our map's origin will be the regular origin, and it won't create and AreaClodMesh from it.		
		*/
		TerrainBlock tb=new TerrainBlock("block",4,
                new Vector3f(2,1,2),
                map,
                new Vector3f(0,0,0));
		//Give the terrain a bounding box.
		tb.setModelBound(new BoundingBox());
		tb.updateModelBound();
		
		//Attach the terrain TriMesh to our rootNode
		rootNode.attachChild(tb);
		
		/**
		 * The terrain is a square. If you took the map [] and created a height for each number, it would look like this. 
		 * This is how terrain is generated with TerrainBlock. The first parameter of TerrainBlock is the block’s name 
		 * (because all spatial objects must have a name). The next is the size of the block (4×4). The 3rd is a scale value for the terrain. 
		 * We pass in integer terrain values, but what if we want float heights. The scale value for the terrain creates the terrain stretched
		 *  twice as large along the x and z axis. Using this, you can manipulate the appearance and size of your terrain. The last two 
		 *  parameters are the origin for the terrain (0, 0, 0 for this example) and finally if we want to create the terrain as an 
		 *  AreaClodMesh. If true, the terrain will take up more memory but will on average generate faster FPS because it will be a clod. 
		 */
	}

	private void generatedHeightMap(){
		//This will be the texture for the terrain.
		URL grass=HelloTerrainoriginal.class.getClassLoader().getResource("jmetest/data/texture/grassb.png");
		
		//Use the helper class to create a terrain for us. the terrain will be 64x64 
		/**
		 * Here we use MidPointHeightMap to generate a terrain for us. The terrain will be 64 by 64 and we give it a roughness value of 1.5. 
		 * The roughness value changes how smooth the terrain becomes. You can play with it for the desired result. The only new thing about 
		 * using TerrainBlock is we get our size and height map array from the midpoint height map. 
		 */
		 MidPointHeightMap mph=new MidPointHeightMap(64,1.5f);
	        // Create a terrain block from the created terrain map.
	        TerrainBlock tb=new TerrainBlock("midpoint block",mph.getSize(),
	                new Vector3f(1,.11f,1),
	                mph.getHeightMap(),
	                new Vector3f(0,-25,0));
		//Add the texture
		TextureState ts=display.getRenderer().createTextureState();
		 ts.setTexture(TextureManager.loadTexture(grass,
	                Texture.MinificationFilter.BilinearNearestMipMap,
	                Texture.MagnificationFilter.Bilinear));		tb.setRenderState(ts);
		
		//Give the terrain a bounding box
		tb.setModelBound(new BoundingBox());
		tb.updateModelBound();
		
		//Attach the terrain TriMesh to rootNode
		rootNode.attachChild(tb);		
	}

	private void complexTerrain() {
        // This grayscale image will be our terrain
		//Picture used to create our terrain**simple bubble pic with use of hightmap allows to be a terrain
		URL grayScale=HelloTerrainoriginal.class.getClassLoader().getResource("jmetest/data/texture/bubble.jpg");

        // These will be the textures of our terrain.
        URL waterImage=HelloTerrainoriginal.class.getClassLoader().getResource("jmetest/data/texture/water.png");
        URL dirtImage=HelloTerrainoriginal.class.getClassLoader().getResource("jmetest/data/texture/dirt.jpg");
        URL highest=HelloTerrainoriginal.class.getClassLoader().getResource("jmetest/data/texture/highest.jpg");


        //  Create an image height map based on the gray scale of our image.
        /**
         * You’ll notice the parts of my terrain that are the farthest up are the parts of the gray scale image that are the whitest.
         *  The class ImageBasedHeightMap creates a height map from an image where the tallest parts are white and the smallest parts are black.  
         */
        ImageBasedHeightMap ib=new ImageBasedHeightMap(
                new ImageIcon(grayScale).getImage()
        );
        // Create a terrain block from the image's grey scale
        TerrainBlock tb=new TerrainBlock("image icon",ib.getSize(),
                new Vector3f(.5f,.05f,.5f),ib.getHeightMap(),
                new Vector3f(0,0,0));

        //  Create an object to generate textured terrain from the image based height map.
        /**we use ProceduralTextureGenerator. It takes a height map and some ImageIcons and produces a blend at the heights you specify for 
         * each texture. ** uses textures at certain heights on the map
        */
        ProceduralTextureGenerator pg=new ProceduralTextureGenerator(ib);
        //  Look like water from height 0-60 with the strongest "water look" at 30
        pg.addTexture(new ImageIcon(waterImage),0,30,60);
        //  Look like dirt from height 40-120 with the strongest "dirt look" at 80
        pg.addTexture(new ImageIcon(dirtImage),40,80,120);
        //  Look like highest (pure white) from height 110-256 with the strongest "white look" at 130
        pg.addTexture(new ImageIcon(highest),110,130,256);

        //  Tell pg to create a texture from the ImageIcon's it has recieved.
        pg.createTexture(256);
        TextureState ts=display.getRenderer().createTextureState();
        // Load the texture and assign it.
        ts.setTexture(
                TextureManager.loadTexture(
                		  pg.getImageIcon().getImage(),
                          Texture.MinificationFilter.Trilinear,
                          Texture.MagnificationFilter.Bilinear,
                          true
                )
        );
        tb.setRenderState(ts);

        // Give the terrain a bounding box
        tb.setModelBound(new BoundingBox());
        tb.updateModelBound();

        // Move the terrain in front of the camera
        tb.setLocalTranslation(new Vector3f(0,0,-50));

        // Attach the terrain to our rootNode.
        rootNode.attachChild(tb);
    }
	/**
	 * That’s all there is too it. A Terrain class which is pretty important that I didn’t touch on is TerrainPage.
	 *  It creates a terrain and splits it into different BoundingBoxes so that the part of the Terrain you’re not viewing can be culled.
	 */
}