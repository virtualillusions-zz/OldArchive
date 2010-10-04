package SideScrollerExample;

import com.jme3.app.SimplePhysicsApplication;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.nodes.PhysicsCharacterNode;
import com.jme3.bullet.nodes.PhysicsNode;
import com.jme3.input.KeyInput;
import com.jme3.input.binding.BindingListener;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 *
 * @author Kyle Williams
 */
public class testSetUp2Physics extends SimplePhysicsApplication implements BindingListener{
    
    public static void main(String[] args){
        testSetUp2Physics app = new testSetUp2Physics();
        app.start();
    }
    @Override
    public void simpleInitApp() {
         manager.registerLocator("/FighterExample/data/",ClasspathLocator.class.getName(),"jpg");

        Material mat = new Material(manager, "plain_texture.j3md");
        TextureKey key = new TextureKey("Monkey.jpg", true);
        key.setGenerateMips(true);
        Texture tex = manager.loadTexture(key);
        tex.setMinFilter(Texture.MinFilter.Trilinear);
        mat.setTexture("m_ColorMap", tex);


        // Add a box to the world
        Geometry geom=new Geometry("box",new Box(Vector3f.ZERO,1f,1f,1f));
        geom.setMaterial(mat);
        physicsCharacter=new PhysicsCharacterNode(geom,new BoxCollisionShape(new Vector3f(1f,1f,1f)),.1f);
        physicsCharacter.setLocalTranslation(new Vector3f(.6f,10,.5f));
        physicsCharacter.updateGeometricState();
        physicsCharacter.updateModelBound();
        rootNode.attachChild(physicsCharacter);
        getPhysicsSpace().addQueued(physicsCharacter);
        physicsCharacter.setMaxJumpHeight(100f);

        //Sets Up Camera
        cam.setLocation(geom.getLocalTranslation().add(-40, 6, 0));
        cam.lookAt(geom.getLocalTranslation(), Vector3f.UNIT_Y);
        //Add a Floor
        Geometry geom2=new Geometry("floor",new Box(Vector3f.ZERO,10f,1f,10f));
        geom2.setMaterial(mat);
        geom2.updateGeometricState();
        geom2.updateModelBound();
        PhysicsNode floor=new PhysicsNode(geom2,new BoxCollisionShape(new Vector3f(10f,1f,10f)),0);
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
            this.physicsCharacter.jump();
        }else{
            walkDirection.set(Vector3f.ZERO);
        }
    }

    private PhysicsCharacterNode physicsCharacter;
    private Vector3f walkDirection=new Vector3f();
    @Override
    public void simpleUpdate(float tpf){
        //Sets Up Camera
        cam.setLocation(physicsCharacter.getLocalTranslation().add(-40, 6, 0));
        cam.lookAt(physicsCharacter.getLocalTranslation(), Vector3f.UNIT_Y);
    }
     @Override
    public void simplePhysicsUpdate(float tpf) {
        physicsCharacter.setWalkDirection(walkDirection);        
   }
}