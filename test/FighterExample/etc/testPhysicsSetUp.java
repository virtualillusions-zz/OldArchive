package FighterExample.etc;

import com.jme3.app.SimplePhysicsApplication;
import com.jme3.asset.TextureKey;
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

public class testPhysicsSetUp extends SimplePhysicsApplication implements BindingListener{

    public static void main(String[] args){
        testPhysicsSetUp app = new testPhysicsSetUp();
        app.start();
    }
    @Override
    public void simpleInitApp() {
        cam.setFrustumFar(10000);
	flyCam.setMoveSpeed(100);
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
        physicsCharacter.setLocalTranslation(new Vector3f(0f,10f,2f));
        physicsCharacter.updateGeometricState();
        physicsCharacter.updateModelBound();
        physicsCharacter.setMaxJumpHeight(100f);
        rootNode.attachChild(physicsCharacter);
        getPhysicsSpace().addQueued(physicsCharacter);

         // Add a box to the world
        Geometry geom2=new Geometry("box",new Box(Vector3f.ZERO,1f,1f,1f));
        geom2.setMaterial(mat);
        PhysicsCharacterNode physicsCharacter2=new PhysicsCharacterNode(geom2,new BoxCollisionShape(new Vector3f(1f,1f,1f)),.1f);
        physicsCharacter2.setLocalTranslation(new Vector3f(0f,10f,-2f));
        physicsCharacter2.updateGeometricState();
        physicsCharacter2.updateModelBound();
        physicsCharacter2.setMaxJumpHeight(100f);
        rootNode.attachChild(physicsCharacter2);
        getPhysicsSpace().addQueued(physicsCharacter2);

        //Sets Up Camera
        cam.setLocation(geom.getLocalTranslation().add(-40, 6, 0));
        cam.lookAt(geom.getLocalTranslation(), Vector3f.UNIT_Y);
        //Add a Floor
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
        //cam.setLocation(physicsCharacter.getLocalTranslation().add(-40, 6, 0));
        //cam.lookAt(physicsCharacter.getLocalTranslation(), Vector3f.UNIT_Y);
    }
     @Override
    public void simplePhysicsUpdate(float tpf) {
        physicsCharacter.setWalkDirection(walkDirection);
   }
}