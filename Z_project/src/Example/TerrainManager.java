package Example;

import java.net.URL;
import java.util.logging.Logger;

import javax.swing.ImageIcon;


import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.game.state.DebugGameState;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.util.ImageBasedHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

/**
* @author Kyle Williams
* Manages Terrains
*/
public class TerrainManager{
    private static Logger logger = Logger.getLogger(TerrainManager.class.getName());
   
    // the one and only instance of the Terrainmanager
    private TerrainBlock complexTerrainBlock = null;

    // the constructor is private, it can only be called inside this class
    public TerrainManager(){
        complexTerrain();
    }
 
    private void complexTerrain() {
        // This grayscale image will be our terrain
        URL grayScale=HelloExample.class.getClassLoader().getResource("jmetest/data/texture/bubble.jpg");

        // These will be the textures of our terrain.
        URL waterImage=HelloExample.class.getClassLoader().getResource("jmetest/data/texture/water.png");
        URL dirtImage=HelloExample.class.getClassLoader().getResource("jmetest/data/texture/dirt.jpg");
        URL highest=HelloExample.class.getClassLoader().getResource("jmetest/data/texture/highest.jpg");


        //  Create an image height map based on the gray scale of our image.
        ImageBasedHeightMap ib=new ImageBasedHeightMap(
                new ImageIcon(grayScale).getImage()
        );
        // Create a terrain block from the image's grey scale
    // Create a terrain block from the image's grey scale
        complexTerrainBlock = new TerrainBlock("image icon",ib.getSize(),
                new Vector3f(.5f,.05f,.5f),ib.getHeightMap(),
                new Vector3f(0,0,0));

        //  Create an object to generate textured terrain from the image based height map.
        ProceduralTextureGenerator pg=new ProceduralTextureGenerator(ib);
        //  Look like water from height 0-60 with the strongest "water look" at 30
        pg.addTexture(new ImageIcon(waterImage),0,30,60);
        //  Look like dirt from height 40-120 with the strongest "dirt look" at 80
        pg.addTexture(new ImageIcon(dirtImage),40,80,120);
        //  Look like highest (pure white) from height 110-256 with the strongest "white look" at 130
        pg.addTexture(new ImageIcon(highest),110,130,256);

        //  Tell pg to create a texture from the ImageIcon's it has recieved.
        pg.createTexture(256);
        TextureState ts=DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        // Load the texture and assign it.
        ts.setTexture(
                TextureManager.loadTexture(
                          pg.getImageIcon().getImage(),
                          Texture.MinificationFilter.Trilinear,
                          Texture.MagnificationFilter.Bilinear,
                          true
                )
        );
        complexTerrainBlock.setRenderState(ts);

        // Give the terrain a bounding box
        complexTerrainBlock.setModelBound(new BoundingBox());
      complexTerrainBlock.updateModelBound();

        // Move the terrain in front of the camera
        complexTerrainBlock.setLocalTranslation(new Vector3f(0,0,-50));

    }

    public TerrainBlock getComplexTerrainBlock() {
        return complexTerrainBlock;
    }
} 
    class TerrainGameState extends DebugGameState {
        public TerrainGameState() throws Exception {
            super();
            TerrainManager manager = new TerrainManager();
            // Finally a terrain loaded from a greyscale image with fancy textures on it.
            rootNode.attachChild(manager.getComplexTerrainBlock());
          //rootNode.updateGeometricState(0, true);
            rootNode.updateRenderState();
        }
     }


