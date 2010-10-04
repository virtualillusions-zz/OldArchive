package FighterExample.etc;

import FighterExample.etc.FighterFactory;
import com.jme3.app.SimplePhysicsApplication;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.nodes.PhysicsCharacterNode;
import com.jme3.bullet.nodes.PhysicsNode;
import com.jme3.input.KeyInput;
import com.jme3.input.binding.BindingListener;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.MeshLoader;

import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import java.util.logging.Logger;
/**
 *
 * @author Kyle Williams
 */
public class Fighting extends SimplePhysicsApplication implements BindingListener{
     public static final Logger getLogger = Logger.getLogger(Fighting.class.getName());
 public static void main(String[] args){
        Fighting app = new Fighting();
        app.start();
    }
  PointLight pl;
    @Override
    public void simpleInitApp() {
        manager.registerLocator("/FighterExample/data/",ClasspathLocator.class.getName(),
				"material","meshxml","skeletonxml","j3m","jpg","tga");

        cam.setFrustumFar(10000);
	flyCam.setMoveSpeed(100);        

        pl = new PointLight();
        pl.setPosition(cam.getLocation());
        rootNode.addLight(pl);

        getPhysicsSpace().setGravity(Vector3f.ZERO);
        FighterFactory.init(manager, rootNode, getPhysicsSpace());

        FighterFactory.get().setFighter("p1","roboto");
        FighterFactory.get().getFighter("p1").setLocalTranslation(new Vector3f(0f,10f,5f));
        //FighterFactory.get().getFighter("roboto");

        //Sets Up Camera
        //cam.setLocation(physicsCharacter.getLocalTranslation().add(-40, 6, 0));
       // cam.lookAt(physicsCharacter.getLocalTranslation(), Vector3f.UNIT_Y);
        //Set up Texture for floor
        Material mat = new Material(manager, "plain_texture.j3md");
        TextureKey texKey = new TextureKey("Monkey.jpg", true);
        texKey.setGenerateMips(true);
        Texture tex = manager.loadTexture(texKey);
        tex.setMinFilter(Texture.MinFilter.Trilinear);
        mat.setTexture("m_ColorMap", tex);

        //Add A floor
        Geometry floorMesh=new Geometry("floor",new Box(Vector3f.ZERO,100f,1f,100f));
        floorMesh.setMaterial(mat);
        floorMesh.updateGeometricState();
        floorMesh.updateModelBound();
        PhysicsNode floor=new PhysicsNode(floorMesh,new BoxCollisionShape(new Vector3f(100f,1f,100f)),0);
        floor.setLocalTranslation(new Vector3f(.6f,4,-.5f));
        rootNode.attachChild(floor);
        floor.updateModelBound();
        floor.updateGeometricState();
        getPhysicsSpace().addQueued(floor);

        //Sets Up Key Bindings
        Keys();
    }

    public void Keys(){
        inputManager.registerKeyBinding("Left", KeyInput.KEY_J);
        inputManager.registerKeyBinding("Right", KeyInput.KEY_L);
        inputManager.registerKeyBinding("Jump", KeyInput.KEY_I);
        //used with method onBinding in BindingListener interface
        //in order to add function to keys
        inputManager.addBindingListener(this);
    }

    @Override
    public void onBinding(String binding, float value) {
        if (binding.equals("Left")){
             walkDirection.addLocal(new Vector3f(0,0,-1f));
        }else if (binding.equals("Right")){
            walkDirection.addLocal(new Vector3f(0,0,1f));
        }else if (binding.equals("Jump")){
          //  this.physicsCharacter.jump();
        }else{
            walkDirection.set(Vector3f.ZERO);
        }
    }

   // private PhysicsCharacterNode physicsCharacter;
    private Vector3f walkDirection=new Vector3f();
    @Override
    public void simpleUpdate(float tpf){
        pl.setPosition(cam.getLocation());
        //Sets Up Camera
        //cam.setLocation(physicsCharacter.getLocalTranslation().add(-40, 6, 0));
        //cam.lookAt(physicsCharacter.getLocalTranslation(), Vector3f.UNIT_Y);
    }
     @Override
    public void simplePhysicsUpdate(float tpf) {
        //physicsCharacter.setWalkDirection(walkDirection);
   }
}
