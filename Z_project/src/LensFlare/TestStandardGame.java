package LensFlare;

import java.util.concurrent.Callable;

import com.jme.bounding.BoundingBox;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.LightNode;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.shape.Box;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jmex.editors.swing.settings.GameSettingsPanel;
import com.jmex.effects.LensFlare;
import com.jmex.effects.LensFlareFactory;
import com.jmex.game.StandardGame;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;

public class TestStandardGame {
	public static void main(String[] args) throws Exception {
	    System.setProperty("jme.stats", "set");
		final StandardGame game = new StandardGame("A Simple Test");
		if (GameSettingsPanel.prompt(game.getSettings())) {
			game.start();
			GameTaskQueueManager.getManager().update(new Callable<Object>() {
				public Object call() {
					GameState state = new MyGameState(game);
					state.setActive(true);
					GameStateManager.getInstance().attachChild(state);
					return null;
				}
			}).get();
		}
	}
}

class MyGameState extends BasicGameState {
	StandardGame game;
	LightState lightState;
	LightNode lightNode;
	LensFlare flare;
	
	public MyGameState(StandardGame game) {
		super("my_gs");
		this.game = game;
		DisplaySystem display = DisplaySystem.getDisplaySystem();
		Camera cam = display.getRenderer().getCamera();
		
		LightState lightState = display.getRenderer().createLightState();
		rootNode.setRenderState(lightState);
        display.setTitle("Lens Flare!");
        cam.setLocation(new Vector3f(0.0f, 0.0f, 200.0f));
        cam.update();

        PointLight dr = new PointLight();
        dr.setEnabled(true);
        dr.setDiffuse(ColorRGBA.white.clone());
        dr.setAmbient(ColorRGBA.gray.clone());
        dr.setLocation(new Vector3f(0f, 0f, 0f));

        lightState.attach(dr);
        lightState.setTwoSidedLighting(true);

        lightNode = new LightNode("light");
        lightNode.setLight(dr);

        Vector3f min2 = new Vector3f(-0.5f, -0.5f, -0.5f);
        Vector3f max2 = new Vector3f(0.5f, 0.5f, 0.5f);
        Box lightBox = new Box("box", min2, max2);
        lightBox.setModelBound(new BoundingBox());
        lightBox.updateModelBound();
        lightNode.attachChild(lightBox);
        lightNode.setLocalTranslation(new Vector3f(-14f, 14f, -14f));

        Box box2 = new Box("blocker", new Vector3f(-5, -5, -5), new Vector3f(5,
                5, 5));
        box2.setModelBound(new BoundingBox());
        box2.updateModelBound();
        box2.setLocalTranslation(new Vector3f(100, 0, 0));
        rootNode.attachChild(box2);

        // clear the lights from this lightbox so the lightbox itself doesn't
        // get affected by light:
        lightBox.setLightCombineMode(LightCombineMode.Off);

        // Setup the lensflare textures.
        TextureState[] tex = new TextureState[4];
        tex[0] = display.getRenderer().createTextureState();
        tex[0].setTexture(TextureManager.loadTexture(LensFlare.class
                .getClassLoader()
                .getResource("jmetest/data/texture/flare1.png"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, Image.Format.RGBA8,
                0.0f, true));
        tex[0].setEnabled(true);

        tex[1] = display.getRenderer().createTextureState();
        tex[1].setTexture(TextureManager.loadTexture(LensFlare.class
                .getClassLoader()
                .getResource("jmetest/data/texture/flare2.png"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear));
        tex[1].setEnabled(true);

        tex[2] = display.getRenderer().createTextureState();
        tex[2].setTexture(TextureManager.loadTexture(LensFlare.class
                .getClassLoader()
                .getResource("jmetest/data/texture/flare3.png"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear));
        tex[2].setEnabled(true);

        tex[3] = display.getRenderer().createTextureState();
        tex[3].setTexture(TextureManager.loadTexture(LensFlare.class
                .getClassLoader()
                .getResource("jmetest/data/texture/flare4.png"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear));
        tex[3].setEnabled(true);

        flare = LensFlareFactory.createBasicLensFlare("flare", tex);
        flare.setRootNode(rootNode);
        //lightNode.attachChild(flare);
        Box box = new Box("my box", new Vector3f(0, 0, 0), 10, 10, 10);
        box.setModelBound(new BoundingBox());
        box.updateModelBound();
        rootNode.attachChild(box);
        rootNode.attachChild(lightNode);

        // notice that it comes at the end
        lightNode.attachChild(flare);
		KeyBindingManager.getKeyBindingManager().add("exit", KeyInput.KEY_ESCAPE);
		display.getRenderer().getCamera().getLocation().z -= 2;
		
		rootNode.updateGeometricState(0, true);
		rootNode.updateRenderState();
	}
	@Override
	public void update(float tpf) {
		super.update(tpf);
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit")) {
			game.finish();
		}
	}
}