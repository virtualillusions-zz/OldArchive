package gamecontrols;
 
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.game.state.BasicGameState;
 
/**
 *
 * @author Gronau
 * Nothing new here. Next the game state with the cute little cube:
 */
public class CubeGameState extends BasicGameState {
 
    private static String texture = "jmetest/data/images/Monkey.jpg";
 
    public CubeGameState() {
        super("cubeGameState");
        final Box box = new Box("MonkeyBox", new Vector3f(0, 0, 0), 5, 5, 5);
 
        //Material: gray
        final MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
        ms.setEmissive(new ColorRGBA(0.5f, 0.5f, 0.5f, 1));
        box.setRenderState(ms);
 
        //Texture: the Monkey
        final TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        final Texture t = TextureManager.loadTexture(
                BasicGameState.class.getClassLoader().getResource(texture),
                Texture.MinificationFilter.BilinearNoMipMaps,
                Texture.MagnificationFilter.Bilinear);
        ts.setTexture(t);
        box.setRenderState(ts);
        box.setModelBound(new BoundingBox());
        box.updateModelBound();
        Node boxNode = new Node("MonkeyBoxNode");
        boxNode.attachChild(box);
        getRootNode().attachChild(boxNode);
 
        //Spot on!
        final PointLight light = new PointLight();
        light.setDiffuse(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.75f));
        light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        light.setLocation(new Vector3f(100, 100, 100));
        light.setEnabled(true);
 
        final LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.attach(light);
        getRootNode().setRenderState(lightState);
 
        getRootNode().updateRenderState();
        //Oh, and don't forget the controller...
        getRootNode().addController(new CubeController(boxNode));
    }
}