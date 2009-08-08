package Fractals;

import java.nio.ByteBuffer;
import java.util.Random;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.input.FirstPersonHandler;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SharedNode;
import com.jme.scene.shape.Capsule;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.BufferUtils;


/**
 * Fur effect using shell shading. 
 * 
 * @author mazander
 */
public class FurTest extends SimpleGame {
    
    private Quaternion rotQuat = new Quaternion();
    private float angle = 0;
    private Vector3f axis = new Vector3f(1, 1, 0);
    private Node furryNode;


    public static void main(String[] args) {
    	FurTest app = new FurTest();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    protected void simpleUpdate() {
        if (tpf < 1) {
            angle = angle + (tpf * 25);
            if (angle > 360) {
                angle = 0;
            }
        }

        rotQuat.fromAngleNormalAxis(angle * FastMath.DEG_TO_RAD, axis);
        furryNode.setLocalRotation(rotQuat);
    }

    protected void simpleInitGame() {
        display.setTitle("Fur Test");
        Node node = new Node("scene");
        Capsule torus = new Capsule("", 4, 12, 12, 4, 6);
        torus.setModelBound(new BoundingBox());
        torus.updateModelBound();
        node.attachChild(torus);
        FurFactory furFactory = new FurFactory();
        furFactory.width = 256;
        furFactory.height = 256;
        furFactory.layers = 40;
        
        furryNode = furFactory.createFurryNode(node);
        rootNode.attachChild(furryNode);
        input = new FirstPersonHandler(cam);
    }

	
	public class FurFactory {
		
		public int layers = 20;
		
		public int width = 128;
		
		public int height = 128;
		
		public float hairdensity = 0.85f;
		
		public float hairLength = 0.5f;
		
		public ColorRGBA transparent = new ColorRGBA(0f,0f,0f,0f);
		
		public ColorRGBA skin = new ColorRGBA(.1f, .3f, .5f,1f);
		
		public ColorRGBA start = new ColorRGBA(.1f, .2f, .3f,1f);
		
		public ColorRGBA end = new ColorRGBA(.2f, .4f, .8f,1f);

		public Texture2D[] createTextures() {
			int totalPixels = width * height;
			Texture2D[] textures = new Texture2D[layers];
			ByteBuffer[] buffers = new ByteBuffer[layers];
			for (int i = 0; i < layers; i++) {
				buffers[i] = BufferUtils.createByteBuffer(4 * totalPixels);
			}
			
			ColorRGBA color = new ColorRGBA();
			Random random = new Random();
			int skinColor = skin.asIntARGB();
			int transparentColor = transparent.asIntARGB();
			for (int i = 0; i < totalPixels; i++) {
				float hairColor = random.nextFloat();
				int hairLength = random.nextInt(layers);
				for (int j = 0; j < layers; j++) {
					if(j == 0) {
						buffers[j].putInt( skinColor );
					} else if (hairColor <= hairdensity) {
						float hairThickness = j < hairLength ? (float) hairLength / j : 0f;
						color.interpolate(start, end, hairColor / hairdensity);
						color.a = hairThickness;
						buffers[j].putInt(color.asIntARGB());
					} else {
						buffers[j].putInt( transparentColor);
					}
				}
			}
			for (int i = 0; i < layers; i++) {
				Image image = new Image(Image.Format.RGBA8, width, height, buffers[i]);
				textures[i] = new Texture2D();
				textures[i].setImage(image);
				textures[i].setWrap(Texture.WrapMode.Repeat);
			}

			return textures;
		}
		
		public Node createFurryNode(Node node) {
			Texture2D[] textures = createTextures();
			return createFurryNode(node, textures);
		}
		
		public Node createFurryNode(Node node, Texture2D[] textures) {
			Renderer renderer = DisplaySystem.getDisplaySystem().getRenderer();
			
			CullState cs = renderer.createCullState();
			cs.setCullFace(CullState.Face.Back);
			
			BlendState as = renderer.createBlendState();
			as.setSourceFunction(BlendState.SourceFunction.SourceAlpha );
			as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
			as.setTestFunction(BlendState.TestFunction.Always);
			as.setBlendEnabled(true);
			as.setTestEnabled(true);
			as.setEnabled(true);

	        ZBufferState zs = renderer.createZBufferState();
	        zs.setEnabled(true);
	        zs.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);

			Node skinNode = new Node();
			Node hairNode = new Node();
			for (int i = 0; i < layers; i++) {
				TextureState ts = renderer.createTextureState();
				ts.setTexture(textures[i]);
				// set render states
				SharedNode shared = new SharedNode(node);
				shared.setRenderState(cs);
				shared.setRenderState(ts);
				shared.setRenderState(as);
				shared.setRenderState(zs);
				// set local scale
				float scale = 1f + i * hairLength / (layers - 1);
				shared.setLocalScale(scale);
				if(i == 0) {
					skinNode.attachChild(shared);
				} else {
					hairNode.attachChild(shared);
				}
			}
			hairNode.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
	        Node resultNode = new Node();
	        resultNode.attachChild(skinNode);
	        resultNode.attachChild(hairNode);
			
			return resultNode;
		}
	}

}