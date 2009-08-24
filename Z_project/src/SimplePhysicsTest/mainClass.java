package SimplePhysicsTest;

import javax.swing.ImageIcon;

import jmetest.intersection.octree.OctreeDebugger;
import jmetest.terrain.TestTerrain;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.KeyInput;
import com.jme.input.NodeHandler;
import com.jme.light.DirectionalLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.CameraNode;
import com.jme.scene.shape.Box;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.simplephysics.*;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.FaultFractalHeightMap;
import com.jmex.terrain.util.MidPointHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

public class mainClass extends SimpleGame{

	 public static void main(String[] args) {
		 	mainClass app = new mainClass();
	        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
	        app.start();
	    }
	  private CameraNode camNode;
	  private TerrainBlock page;
	  
	  private CollisionScene collisionScene;
	  private DynamicCollider dynamNode;
	  private float forceFactor = 1000;
	  private Vector3f gravity = new Vector3f(0,-forceFactor*1.95f,0);
	@Override
	protected void simpleInitGame() {
		display.setTitle("Simple Physics Test!");
		startTerrain();
		Vector3f min2 = new Vector3f(-10, -5, -5);
		Vector3f max2 = new Vector3f(10, 5, 5);
		Box myBox = new Box("Box",min2, max2);
		myBox.setModelBound(new BoundingBox());
		myBox.updateModelBound();
		rootNode.attachChild(myBox);
		myBox.getLocalTranslation().set(camNode.getLocalTranslation());	

		myBox.updateGeometricState(0, true);
		myBox.updateWorldBound();
		rootNode.updateGeometricState(0, true);
		rootNode.updateWorldBound();
		
		dynamNode= new EllipseCollider(myBox.getLocalTranslation(), myBox.getLocalScale(), myBox);
		dynamNode.getPhysicMaterial().set(0.05f, 1, 0.7f, 0.05f, true);
		dynamNode.getPhysicState().setGravity(gravity);
		
		collisionScene = new CollisionScene();
		collisionScene.attachDynamic(dynamNode);
		collisionScene.attachStatic(new StaticCollider(page,100)).getPhysicMaterial().set(.01f, 0, false);
		System.out.println("Building octree, wait a minute...");
		OctreeDebugger.initialize(display, rootNode);
		collisionScene.build();
		OctreeDebugger.initialize(display, rootNode);
		System.out.println("...octree building completed");
		key = KeyInput.get();
	}
	private KeyInput key;

	 protected void simpleUpdate() {
	        super.simpleUpdate();
	        if(key.isKeyDown(KeyInput.KEY_L)) {
	        	dynamNode.getPhysicState().setForce(forceFactor*5.75f, 0, 0);
	        }
	        else
	        dynamNode.getPhysicState().setForce(0, 0, 0);
	       
	        collisionScene.update(tpf);
	        
	 }

	 private void startTerrain(){
		 	rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);

		    DirectionalLight dl = new DirectionalLight();
		    dl.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		    dl.setDirection(new Vector3f(1, -0.5f, 1));
		    dl.setEnabled(true);
		    lightState.attach(dl);

		    cam.setFrustum(1.0f, 1000.0f, -0.55f, 0.55f, 0.4125f, -0.4125f);
		    cam.update();

		    camNode = new CameraNode("Camera Node", cam);
		    camNode.setLocalTranslation(new Vector3f(300, 250, 200));
		    camNode.updateWorldData(0);
		    input = new NodeHandler(camNode, 150, 1);
		    rootNode.attachChild(camNode);
		    display.getRenderer().setBackgroundColor(new ColorRGBA(0.5f,0.5f,0.5f,1));

		    DirectionalLight dr = new DirectionalLight();
		    dr.setEnabled(true);
		    dr.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		    dr.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
		    dr.setDirection(new Vector3f(0.5f, -0.5f, 0).normalizeLocal());

		    CullState cs = display.getRenderer().createCullState();
		    cs.setCullFace(CullState.Face.None);
		    cs.setEnabled(true);
		    rootNode.setRenderState(cs);

		    lightState.attach(dr);

		    FaultFractalHeightMap heightMap = new FaultFractalHeightMap(257, 32, 0, 255,
			        0.75f);
			    Vector3f terrainScale = new Vector3f(10,1,10);
			    heightMap.setHeightScale( 0.001f);
		    page = new TerrainBlock("Terrain", heightMap.getSize(), terrainScale,
                    heightMap.getHeightMap(),
                    new Vector3f(0, 0, 0));
		    page.setModelBound(new BoundingBox());
		    page.updateModelBound();

		    rootNode.attachChild(page);

		    ProceduralTextureGenerator pt = new ProceduralTextureGenerator(heightMap);
		    pt.addTexture(new ImageIcon(TestTerrain.class.getClassLoader().getResource(
		        "jmetest/data/texture/grassb.png")), -128, 0, 128);
		    pt.addTexture(new ImageIcon(TestTerrain.class.getClassLoader().getResource(
		        "jmetest/data/texture/dirt.jpg")), 0, 128, 255);
		    pt.addTexture(new ImageIcon(TestTerrain.class.getClassLoader().getResource(
		        "jmetest/data/texture/highest.jpg")), 128, 255, 384);

		    pt.createTexture(512);

		    TextureState ts = display.getRenderer().createTextureState();
		    ts.setEnabled(true);
		    Texture t1 = TextureManager.loadTexture(
		        pt.getImageIcon().getImage(),
		        Texture.MinificationFilter.Trilinear,
		        Texture.MagnificationFilter.Bilinear,
		        true);
		    ts.setTexture(t1, 0);

		    Texture t2 = TextureManager.loadTexture(TestTerrain.class.getClassLoader().
		                                            getResource(
		        "jmetest/data/texture/Detail.jpg"),
		                                            Texture.MinificationFilter.Trilinear,
		                                            Texture.MagnificationFilter.Bilinear);
		    ts.setTexture(t2, 1);
		    t2.setWrap(Texture.WrapMode.Repeat);

		    t1.setApply(Texture.ApplyMode.Combine);
		    t1.setCombineFuncRGB(Texture.CombinerFunctionRGB.Modulate);
		    t1.setCombineSrc0RGB(Texture.CombinerSource.CurrentTexture);
		    t1.setCombineOp0RGB(Texture.CombinerOperandRGB.SourceColor);
		    t1.setCombineSrc1RGB(Texture.CombinerSource.PrimaryColor);
		    t1.setCombineOp1RGB(Texture.CombinerOperandRGB.SourceColor);

		    t2.setApply(Texture.ApplyMode.Combine);
		    t2.setCombineFuncRGB(Texture.CombinerFunctionRGB.AddSigned);
		    t2.setCombineSrc0RGB(Texture.CombinerSource.CurrentTexture);
		    t2.setCombineOp0RGB(Texture.CombinerOperandRGB.SourceColor);
		    t2.setCombineSrc1RGB(Texture.CombinerSource.Previous);
		    t2.setCombineOp1RGB(Texture.CombinerOperandRGB.SourceColor);
		    rootNode.setRenderState(ts);

		    FogState fs = display.getRenderer().createFogState();
		    fs.setDensity(0.5f);
		    fs.setEnabled(true);
		    fs.setColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f));
		    fs.setEnd(1000);
		    fs.setStart(500);
		    fs.setDensityFunction(FogState.DensityFunction.Linear);
		    fs.setQuality(FogState.Quality.PerVertex);
		    rootNode.setRenderState(fs);
	 }

}
