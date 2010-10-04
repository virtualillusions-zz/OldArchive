/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FighterExample;

import FighterExample.etc.Fighting;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.binding.BindingListener;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import java.util.Date;
import java.util.logging.Logger;

/**
 *
 * @author Kyle Williams
 */
public class SimpleFighting extends SimpleApplication implements BindingListener{

     public static final Logger getLogger = Logger.getLogger(Fighting.class.getName());
 public static void main(String[] args){
        SimpleFighting app = new SimpleFighting();
        app.start();
    }
  PointLight pl;
   Node roboto;
   Geometry floorMesh;
   boolean jump=false,camFollow=true;
    Ray r;
    CollisionResults res;
    CollisionResult res2;
    float feetHeight;

    @Override
    public void simpleInitApp() {
        Date OldDate = new Date("01/01/2010");
        Date TodaysDate = new Date();
        long mills_per_day = 1000 * 60 * 60 * 24;
        long day_diff = ( TodaysDate.getTime() - OldDate.getTime() ) / mills_per_day;
        float buildDate = day_diff/100f;

        BitmapText build = new BitmapText(guiFont, false);
        build.setSize(guiFont.getCharSet().getRenderedSize());
        build.setLocalTranslation(0, fpsText.getLineHeight()*2, 0);
        build.setText("VEZLA Alpha Build: "+buildDate);
        guiNode.attachChild(build);

        manager.registerLocator("/FighterExample/data/",ClasspathLocator.class.getName(),
				"material","meshxml","skeletonxml","j3m","jpg","tga");

        cam.setFrustumFar(10000);
	flyCam.setMoveSpeed(100);

        pl = new PointLight();
        pl.setPosition(cam.getLocation());
        rootNode.addLight(pl);

        FighterFactory.init(manager, rootNode);

        roboto=FighterFactory.get().setFighter("p1","roboto");

        roboto.setLocalTranslation(new Vector3f(0f,10f,5f));
        r = new Ray(roboto.getLocalTranslation(), Vector3f.UNIT_Y.negate());
        res = new CollisionResults();

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
        floorMesh=new Geometry("floor",new Box(Vector3f.ZERO,100f,10f,100f));
        floorMesh.setMaterial(mat);
        rootNode.attachChild(floorMesh);
        //Sets Up Key Bindings
        Keys();
    }
    
    public void Keys(){
        inputManager.registerKeyBinding("Left", KeyInput.KEY_J);
        inputManager.registerKeyBinding("Right", KeyInput.KEY_L);
        inputManager.registerKeyBinding("Jump", KeyInput.KEY_I);
        inputManager.registerKeyBinding("camFollow", KeyInput.KEY_SPACE);
        //used with method onBinding in BindingListener interface
        //in order to add function to keys
        inputManager.addBindingListener(this);
    }

    @Override
    public void onBinding(String binding, float value) {
        if (binding.equals("Left")){
            roboto.move(0, 0,-.01f);
        }else if (binding.equals("Right")){
            roboto.move(0, 0, .01f);
        }else if (binding.equals("Jump")){
            jump=true;
            roboto.move(0, .01f, 0);
        }else if(binding.equals("camFollow")){
            camFollow=!camFollow;
        }
    }

    @Override
    public void simpleUpdate(float tpf){
        pl.setPosition(cam.getLocation());
        r.setOrigin(roboto.getLocalTranslation());

        //Sets Up Camera
        if(camFollow==true){
            cam.setLocation(roboto.getLocalTranslation().add(-40, 6, 0));
            cam.lookAt(roboto.getLocalTranslation(), Vector3f.UNIT_Y);
        }

        rootNode.updateGeometricState();
        floorMesh.collideWith(r, res);

        res2 = res.getClosestCollision();
        res.clear();

        if(res2!=null&&res2.getDistance()>=.01&&!jump){
            roboto.move(0, -0.01f, 0);
        }
        jump=false;
    }
}