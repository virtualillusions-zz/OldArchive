/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author Kyle Williams
 */
public class SideScrolling extends SimpleApplication implements BindingListener{

    public static findRectangles fRC;
    public static void main(String[] args){
        fRC = new findRectangles();    
        fRC.starts("testStage");
        SideScrolling app = new SideScrolling();        
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
        cam.setFrustumFar(10000);
	flyCam.setMoveSpeed(100);

        System.out.println(fRC.getRectangles());

        Geometry plat;

        for(Rectangle r:fRC.getRectangles()){
           plat = new Geometry("Platform", new Box(new Vector3f((r.x + r.width / 2),-(r.y + r.height / 2),0),r.width/2,r.height/2,10));
           plat.setMaterial(mat);
           plat.updateModelBound();
           platList.add(plat);
           rootNode.attachChild(plat);
        }
        geom.setLocalTranslation(platList.get(0).getLocalTranslation());

        r = new Ray(geom.getLocalTranslation(), Vector3f.UNIT_Y.negate());
        res = new CollisionResults();

        Keys();
    }

    public boolean jump=false, camtached=true;
    public Geometry geom;
    public Ray r;
    public CollisionResults res;
    public ArrayList<Geometry> platList = new ArrayList<Geometry>();

    @Override
    public void simpleUpdate(float tpf){
        //Sets Up Camera
        if(camtached==true){
        cam.setLocation(geom.getLocalTranslation().add(0, 6, 70));
        cam.lookAt(geom.getLocalTranslation(), Vector3f.UNIT_Y);
        }

        rootNode.updateGeometricState();
        for(Geometry plat:platList){
        plat.collideWith(r, res);  

            if(res.getClosestCollision()!=null&&res.getClosestCollision().getDistance()>=1&&!jump){
                geom.move(0, -0.01f, 0);
            }
        res.clear();
        }

        jump=false;
    }


    public void Keys(){
        inputManager.registerKeyBinding("Left", KeyInput.KEY_J);
        inputManager.registerKeyBinding("Right", KeyInput.KEY_L);
        inputManager.registerKeyBinding("Jump", KeyInput.KEY_I);
        inputManager.registerKeyBinding("EXIT", KeyInput.KEY_DELETE);
        inputManager.registerKeyBinding("Camera-tached", KeyInput.KEY_K);
        //used with method onBinding in BindingListener interface
        //in order to add function to keys
        inputManager.addBindingListener(this);
    }
   
    public void onBinding(String binding, float value) {
        if (binding.equals("Left")){
            geom.move(-.01f, 0,0);
        }else if (binding.equals("Right")){
            geom.move(.01f, 0, 0);
        }else if (binding.equals("Jump")){
            jump=true;
            geom.move(0, .01f, 0);
            System.out.println(value);
        }else if(binding.equals("Camera-tached")){
            if(camtached==true)camtached=false;
            else camtached=true;
        }else if(binding.equals("EXIT")){
            this.stop();
            fRC.exit();
            System.exit(0);
        }
    }

    
}
