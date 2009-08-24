package SimplePhysicsTest;

import jmetest.intersection.octree.DebugOctree;
import jmetest.intersection.octree.OctreeDebugger;

import com.jme.app.SimpleGame;
import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.bounding.BoundingBox;
import com.jme.input.KeyInput;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.SharedMesh;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Dodecahedron;
import com.jme.scene.shape.Octahedron;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.CullState;
import com.jmex.bounding.Octree;
import com.jmex.simplephysics.Collider;
import com.jmex.simplephysics.CollisionListener;
import com.jmex.simplephysics.CollisionScene;
import com.jmex.simplephysics.DynamicCollider;
import com.jmex.simplephysics.EllipseCollider;
import com.jmex.simplephysics.StaticCollider;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.util.MidPointHeightMap;

public class mainClass2 extends SimpleGame {

	Node pickNode;
	
	private CollisionScene collisionScene;

	private DynamicCollider playerCollider, walkingCollider, ballCollider;
	
	private Collider lastCollided = null;
	
	private Vector3f force = new Vector3f();
	
	private float forceFactor = 1000;
	//force.x = -forceFactor*4;
	//private Vector3f gravity = new Vector3f(0,-9.85f,0);
	private Vector3f gravity = new Vector3f(0,-forceFactor*1.95f,0);
	
	public static void main(String[] args) {
		mainClass2 app = new mainClass2();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	@Override
	protected void simpleInitGame() {
		
		//Create a normal scene
		key = KeyInput.get();
		
	    CullState cs = display.getRenderer().createCullState();
	    cs.setCullFace(CullState.Face.Back );
	    cs.setEnabled(true);
	    
		PointLight pl = new PointLight();
		pl.setDiffuse(new ColorRGBA(0.4f, 0.4f, 0.4f, 1));
		pl.setEnabled(true);
		pl.setLocation(new Vector3f(500,1500,500));
		lightState.attach(pl);
		
        MidPointHeightMap heightMap = new MidPointHeightMap(64, 1.9f);
        Vector3f terrainScale = new Vector3f(20,.5f,20);
        TerrainBlock terrain = new TerrainBlock("Terrain", heightMap.getSize(), terrainScale,
                                           heightMap.getHeightMap(),
                                           new Vector3f(0, 0, 0));
        terrain.setModelBound(new BoundingBox());
        terrain.updateModelBound();
        
               
        Box bridge = new Box("Bridge", new Vector3f(0,0,0), 100, 5, 10);
        bridge.setModelBound(new BoundingBox());
        bridge.updateModelBound();
        bridge.setLocalTranslation(new Vector3f(100, terrain.getHeight(100,50) + 5, 50));
 
        //Create the colliders
        Sphere walking = new Sphere("walking", 20, 20, 1);
        walking.setLocalScale(new Vector3f(2,3,2));
        walkingCollider = new EllipseCollider(new Vector3f(100,terrain.getHeight(100, 150)+3,150), walking.getLocalScale(), walking);
        walkingCollider.getPhysicMaterial().set(0.05f, 2, 0.7f, 0.05f, true);
        walkingCollider.getPhysicState().setGravity(gravity);

        Sphere ball = new Sphere("ball", 20, 20, 1);
        ball.setLocalScale(4);
        ballCollider = new EllipseCollider(new Vector3f(100, terrain.getHeight(100,50)+25, 50), ball.getLocalScale(), ball);
        ballCollider.getPhysicState().setGravity(gravity);
        ballCollider.getPhysicMaterial().set(0.01f, 1, 1, 0.5f, true);
  
        playerCollider = new EllipseCollider(new Vector3f(50, terrain.getHeight(50,50)+25,50), new Vector3f(2,3,2), null);
        playerCollider.getPhysicMaterial().set(0.05f, 1, 0.7f, 0.05f, true);
        playerCollider.getPhysicState().setGravity(gravity);
        
        //Build the collision scene
        collisionScene = new CollisionScene();
        collisionScene.attachDynamic(walkingCollider);
        collisionScene.attachDynamic(ballCollider);
        collisionScene.attachDynamic(playerCollider);
        //Objects won't bounce when hitting the terrain, but will when hitting other statics (bounce is enabled by default)
        collisionScene.attachStatic(new StaticCollider(terrain,100)).getPhysicMaterial().set(.01f, 0, false);
       
        collisionScene.attachStatic(new StaticCollider(bridge,25));
        System.out.println("Building octree, wait a minute...");
        OctreeDebugger.initialize(display, rootNode);
        collisionScene.build(); //Build octrees (may take a while)
        
        OctreeDebugger.initialize(display, rootNode);
       
        //Let's try a collision listener
        ballCollider.addListener(new CollisionListener() {
			public void collided(Collider collider, Collider collided, Vector3f position, Vector3f normal, Vector3f velocity) {
				if (lastCollided != collided) {
					lastCollided = collided;
				} else {
					return;
				}
				if (collider.getSpatial() == null) {
					System.out.println("Camera collided with " + collided.getSpatial().getName());
				} else if (collided.getSpatial() == null) {
					System.out.println(collider.getSpatial().getName() + " collided with Camera");
				} else {
					System.out.println(collider.getSpatial().getName() + " collided with " + collided.getSpatial().getName());
				}
			}
        });
        
        //Build the rendering scene
        pickNode = new Node("Pick");
        pickNode.attachChild(terrain);
       
        pickNode.attachChild(bridge);
        pickNode.updateGeometricState(0, true);
		rootNode.attachChild(pickNode);

		rootNode.attachChild(walking);
		rootNode.attachChild(ball);
        
        pickNode.lock();
	}
	
	private KeyInput key;

	private Vector3f walkForce = new Vector3f(20,0,0);
	
	private Vector3f walkAxis = new Vector3f(0,1,0);

	private Matrix3f rotm = new Matrix3f();
	
	private float angle = 0;
	
	protected void simpleUpdate() {
		//force.zero();
        if(key.isKeyDown(KeyInput.KEY_W)) {
        	force.z = forceFactor*1.5f;
        }
        if(key.isKeyDown(KeyInput.KEY_S)) {
        	force.z = -forceFactor*.75f;
        }
        if(key.isKeyDown(KeyInput.KEY_A)) {
        	force.y = forceFactor*1.25f;
        }
        if(key.isKeyDown(KeyInput.KEY_D)) {
        	force.y = -forceFactor*1.25f;
        }
        if(key.isKeyDown(KeyInput.KEY_SPACE)) {
        	force.x = forceFactor*5.75f;
        }
        if(key.isKeyDown(KeyInput.KEY_F)) {
        	//force.x = -forceFactor*4;
        }
        //Add a rotate force based on the cameras direction
        rotm.fromAxes(walkAxis, cam.getLeft(), cam.getDirection());
        rotm.mult(force, force);
        playerCollider.getPhysicState().setForce(force);
        
        //Adds a rotate force to walk in circles
        angle += (tpf * 0.2) % 1;
        rotm.fromAngleAxis(angle * FastMath.PI,walkAxis);
        rotm.mult(walkForce, force);
		walkingCollider.getPhysicState().setForce(force);
		
		long startTime = System.nanoTime();
		//Do the magic
		collisionScene.update(tpf);
		long stopTime = System.nanoTime();
	    float result = (stopTime - startTime) * 0.00001f;
	    
	    //Update the camera's location
		cam.getLocation().set(playerCollider.getLocation());
		cam.update();

	    String s = "Calc time: %.4f";
		display.setTitle(String.format(s, result));
	}
}
