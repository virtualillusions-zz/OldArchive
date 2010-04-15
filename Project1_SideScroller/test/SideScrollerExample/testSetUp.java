package SideScrollerExample;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.binding.BindingListener;
import com.jme3.material.Material;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 *
 * @author Kyle Williams
 */
public class testSetUp extends SimpleApplication implements BindingListener{
    
    public static void main(String[] args){
        testSetUp app = new testSetUp();
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
        geom=new Geometry("box",new Box(Vector3f.ZERO,1f,1f,1f));
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
        //Sets Up Camera
        cam.setLocation(geom.getLocalTranslation().add(-40, 6, 0));
        cam.lookAt(geom.getLocalTranslation(), Vector3f.UNIT_Y);
        //Add a Floor
        geom2=new Geometry("floor",new Box(Vector3f.ZERO,100f,0.2f,100f));
        geom2.setMaterial(mat);
        geom.setLocalTranslation(new Vector3f(.6f,4,-.5f));
        geom2.updateGeometricState();
        geom2.updateModelBound();
        rootNode.attachChild(geom2);
        
        r = new Ray(geom.getLocalTranslation(), Vector3f.UNIT_Y.negate());
        res = new CollisionResults();
        //Sets Up Key Bindings
        Keys();
    }
    Geometry geom,geom2;
    Ray r;
    CollisionResults res;
    CollisionResult res2;
    @Override
    public void simpleUpdate(float tpf){
        //Sets Up Camera
        cam.setLocation(geom.getLocalTranslation().add(-40, 6, 0));
        cam.lookAt(geom.getLocalTranslation(), Vector3f.UNIT_Y);


        rootNode.updateGeometricState();
        geom2.collideWith(r, res);

        res2 = res.getClosestCollision();
        res.clear();


        if(res2!=null&&res2.getDistance()>=1&&!jump){
            geom.move(0, -0.01f, 0);
        }
        jump=false;
    }
    
    boolean jump=false;

    public void Keys(){
        inputManager.registerKeyBinding("Left", KeyInput.KEY_J);
        inputManager.registerKeyBinding("Right", KeyInput.KEY_L);
        inputManager.registerKeyBinding("Jump", KeyInput.KEY_I);
        //used with method onBinding in BindingListener interface
        //in order to add function to keys
        inputManager.addBindingListener(this);
    }

    public void onBinding(String binding, float value) {
        if (binding.equals("Left")){
            geom.move(0, 0,-.01f);
        }else if (binding.equals("Right")){
            geom.move(0, 0, .01f);
        }else if (binding.equals("Jump")){
            jump=true;
            geom.move(0, .01f, 0);
            System.out.println(value);
        }
    }
}