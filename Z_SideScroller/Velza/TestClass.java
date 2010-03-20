import physics.BaseNode;
import physics.DynamicPhysNode;
import physics.PhysWorld;
import physics.StandingCapsule;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;


public class TestClass extends SimpleApplication{
	public static void main(String[] args){
		TestClass app = new TestClass();
		app.start();
	}
	PhysWorld phys;
	public void simpleInitApp(){
		Material mat = new Material(manager, "plain_texture.j3md");
        TextureKey key = new TextureKey("Monkey.jpg", true);
        key.setGenerateMips(true);
        Texture tex = manager.loadTexture(key);
        tex.setMinFilter(Texture.MinFilter.Trilinear);
        mat.setTexture("m_ColorMap", tex);
		addPhysicsBox(mat);
		addPhysicsFloor(mat);
		phys=new PhysWorld();
		rootNode.updateGeometricState();
		rootNode.updateModelBound();
    
	}
	public void addPhysicsBox(Material mat){
		 // Add a physics box to the world    
        Geometry geom=new Geometry("box",new Box(Vector3f.ZERO,1f,1f,1f));
        geom.setMaterial(mat);
        geom.setLocalTranslation(new Vector3f(.6f,4,.5f));
        StandingCapsule dyna = new StandingCapsule(geom.getLocalTranslation(),1,1);
        dyna.setCollisionGroup(phys.playerCG);
        dyna.setUserNode((BaseNode) rootNode.getChild("base"));
        Node r = new Node();
        r.attachChild(geom);
        //r.attachChild(dyna);
        rootNode.attachChild(r);
        r.updateGeometricState();
        r.updateModelBound();
	}
	
	public void addPhysicsFloor(Material mat){
		 // the floor, does not move (mass=0)
        Geometry geom=new Geometry("box2",new Box(Vector3f.ZERO,100f,0.2f,100f));
        geom.setMaterial(mat);
        geom.updateGeometricState();
        BaseNode base = new BaseNode();
        base.setAsOctreeNode(true);
        base.attachChild(geom);
        base.setName("base");
        rootNode.attachChild(base);
        base.updateModelBound();
        base.updateGeometricState(); 
	}
	 public void simpleUpdate(float tpf){
		 
	    }

}
