package raycastingTest;

import javax.swing.ImageIcon;

import jmetest.terrain.TestTerrain;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.NodeHandler;
import com.jme.intersection.BoundingPickResults;
import com.jme.intersection.PickData;
import com.jme.intersection.PickResults;
import com.jme.intersection.TrianglePickResults;
import com.jme.light.DirectionalLight;
import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.CameraNode;
import com.jme.scene.shape.Box;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.FaultFractalHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

public class mainClass extends SimpleGame{

	 public static void main(String[] args) {
		 	mainClass app = new mainClass();
	        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
	        app.start();
	    }
	  private CameraNode camNode;
	  private TerrainPage page;
	  private Box myBox;
	@Override
	protected void simpleInitGame() {
		display.setTitle("RayCasting!");
		startTerrain();
		Vector3f min2 = new Vector3f(-10, -5, -5);
		Vector3f max2 = new Vector3f(10, 5, 5);
		myBox = new Box("Box",min2, max2);
		myBox.setModelBound(new BoundingBox());
		myBox.updateModelBound();
		rootNode.attachChild(myBox);
		myBox.getLocalTranslation().set(camNode.getLocalTranslation());	
		//myBox.getLocalTranslation().set(myBox.getLocalTranslation().addLocal(0, 420, 0));
		
		myBox.updateGeometricState(0, true);
		myBox.updateWorldBound();
		rootNode.updateGeometricState(0, true);
		rootNode.updateWorldBound();
		
		ray = new Ray(myBox.center, new Vector3f(0,-1,0));
		ray.setOrigin(myBox.getLocalTranslation());
	    //Find the results (Use which ever method suits your needs
	     results = new TrianglePickResults();
	     //results = new BoundingPickResults();
	    //We normally want the distance to see which object is closest
	    results.setCheckDistance(true);
	    //Get the results from a node
	    page.findPick(ray, results);
	    //Loop the results
	    if(results.getNumber() > 0) {
	     closest = results.getPickData(0);
	    System.out.println("The closest hit: " + closest.getTargetMesh().getName());
	    }
	}
	PickData closest;
	PickResults results ;
	Ray ray;
	
	 protected void simpleUpdate() {
	        super.simpleUpdate();
	        if (!Float.isInfinite(closest.getDistance()) && !Float.isNaN(closest.getDistance()))
	        	{if(closest.getDistance()>myBox.yExtent)
	        		{myBox.getLocalTranslation().y-=.1;}}
	        ray.getOrigin().set(myBox.getLocalTranslation());
	        results.clear();
	        page.findPick(ray, results);
	        closest = results.getPickData(0);
	        myBox.getLocalTranslation().x+=.1;
		    System.out.println("The Distance: " + closest.getDistance());
		    System.out.println("myBox Y-Distance: " + myBox.getLocalTranslation().getY());		    
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
		    camNode.setLocalTranslation(new Vector3f(0, 250, -20));
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
		    page = new TerrainPage("Terrain", 33, heightMap.getSize(), terrainScale,
		                                     heightMap.getHeightMap());

		    page.setDetailTexture(1, 16);
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
