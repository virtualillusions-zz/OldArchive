package util;

import com.jme.bounding.BoundingBox;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.light.LightNode;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.effects.LensFlare;
import com.jmex.effects.LensFlareFactory;

public class startLensFlare {
	 private LightNode lightNode;
	 private LensFlare flare;
	 public Node rootNode;
		
	public startLensFlare(Node root){
		rootNode = root;
		setup();
		rootNode.updateGeometricState(0, true);
		rootNode.updateRenderState();
	}
	
	private void setup(){
		PointLight dr = new PointLight();
        dr.setEnabled(true);
        dr.setDiffuse(ColorRGBA.white.clone());
        dr.setAmbient(ColorRGBA.gray.clone());
        dr.setLocation(new Vector3f(0f, 0f, 0f));

        gameSingleton.get().getLightState.attach(dr);
        gameSingleton.get().getLightState.setTwoSidedLighting(true);

        lightNode = new LightNode("light");
        lightNode.setLight(dr);

        Vector3f min2 = new Vector3f(-0.5f, -0.5f, -0.5f);
        Vector3f max2 = new Vector3f(0.5f, 0.5f, 0.5f);
        Box lightBox = new Box("box", min2, max2);
        lightBox.setModelBound(new BoundingBox());
        lightBox.updateModelBound();
        lightNode.attachChild(lightBox);
        lightNode.setLocalTranslation(new Vector3f(-14f, 14f, -14f).mult(10f));

        

        // clear the lights from this lightbox so the lightbox itself doesn't
        // get affected by light:
        lightBox.setLightCombineMode(LightCombineMode.Off);

        // Setup the lensflare textures.
        TextureState[] tex = new TextureState[4];
        tex[0] = gameSingleton.get().getRenderer.createTextureState();
        tex[0].setTexture(TextureManager.loadTexture(startLensFlare.class
                .getClassLoader()
                .getResource("data/lensFlare/flare1.png"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, Image.Format.RGBA8,
                0.0f, true));
        tex[0].setEnabled(true);

        tex[1] = gameSingleton.get().getRenderer.createTextureState();
        tex[1].setTexture(TextureManager.loadTexture(startLensFlare.class
                .getClassLoader()
                .getResource("data/lensFlare/flare2.png"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear));
        tex[1].setEnabled(true);

        tex[2] = gameSingleton.get().getRenderer.createTextureState();
        tex[2].setTexture(TextureManager.loadTexture(startLensFlare.class
                .getClassLoader()
                .getResource("data/lensFlare/flare3.png"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear));
        tex[2].setEnabled(true);

        tex[3] = gameSingleton.get().getRenderer.createTextureState();
        tex[3].setTexture(TextureManager.loadTexture(startLensFlare.class
                .getClassLoader()
                .getResource("data/lensFlare/flare4.png"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear));
        tex[3].setEnabled(true);

        flare = LensFlareFactory.createBasicLensFlare("flare", tex);
        flare.setRootNode(rootNode);
     
        rootNode.attachChild(lightNode);

        // notice that it comes at the end
        lightNode.attachChild(flare);
        
	}
}
