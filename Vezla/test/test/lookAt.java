/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.binding.BindingListener;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.MeshLoader;
/**
 *
 * @author Kyle Williams
 */
public class lookAt extends SimpleApplication implements BindingListener{

    public static void main(String[] args){
        lookAt app = new lookAt();
        app.start();
    }

    @Override
    public void simpleInitApp() {  
// manager.registerLocator("/etc/.../FighterExample/data",ClasspathLocator.class.getName(),"material","meshxml","skeletonxml","j3m","jpg","tga");
        // create the geometry and attach it
        Spatial cobra =  assetManager.loadModel("Models/Oto/Oto.meshxml");
       // cobra.setMaterial(manager.loadMaterial("OTO.j3m"));
        cobra.setLocalTranslation(0f, 0f, -8f);
        cobra.setName("p1");
        rootNode.attachChild(cobra);

        Spatial cobra2 = cobra.clone();
        cobra2.setLocalTranslation(0f, 0f, 8f);
        cobra2.setName("p2");
        rootNode.attachChild(cobra2);        

        //Sets Up Camera
        cam.setLocation(cobra.getLocalTranslation().add(-40, 6, 0));
        cam.lookAt(cobra.getLocalTranslation(), Vector3f.UNIT_Y);
        cam.setFrustumFar(10000);
	flyCam.setMoveSpeed(100);
        //Creat Light
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(cam.getDirection().normalizeLocal());
        dl.setColor(new ColorRGBA(255f, 4f, 2f, 1.0f));
        rootNode.addLight(dl);
        //setUp Keys
        Keys();        
    }

    @Override
    public void simpleUpdate(float tpf){
       lookAt();
    }

    public void lookAt(){
         //Look at each other
        Quaternion p1r = rootNode.getChild("p1").getLocalRotation();
        Vector3f   p1t = rootNode.getChild("p1").getWorldTranslation();

        Quaternion p2r = rootNode.getChild("p2").getLocalRotation();
        Vector3f   p2t = rootNode.getChild("p2").getWorldTranslation();
        
        lookAt(p1r,p2t.subtract(p1t), Vector3f.UNIT_Y);
        lookAt(p2r,p1t.subtract(p2t), Vector3f.UNIT_Y);

        rootNode.getChild("p1").setLocalRotation(p1r);
        rootNode.getChild("p2").setLocalRotation(p2r);
    }

    public void lookAt(Quaternion quat, Vector3f direction, Vector3f up ) {
        direction.setY(0);
        direction.normalizeLocal();
        up.normalizeLocal();
        Vector3f left = up.cross(direction);
        quat.fromAxes(left,up,direction);
        System.out.println(up); 
    }


    public void Keys(){
        inputManager.registerKeyBinding("Left", KeyInput.KEY_J);
        inputManager.registerKeyBinding("Right", KeyInput.KEY_L);
        inputManager.registerKeyBinding("Up", KeyInput.KEY_I);
        inputManager.registerKeyBinding("Down", KeyInput.KEY_K);
        inputManager.registerKeyBinding("Forward", KeyInput.KEY_U);
        inputManager.registerKeyBinding("Backward", KeyInput.KEY_O);
        //used with method onBinding in BindingListener interface
        //in order to add function to keys
        inputManager.addBindingListener(this);
    }

    @Override
    public void onBinding(String binding, float value) {

        if (binding.equals("Left")){
             rootNode.getChild("p1").move(0,0,-.01f);
        }else if (binding.equals("Right")){
            rootNode.getChild("p1").move(0,0,.01f);
        }else if (binding.equals("Up")){
            rootNode.getChild("p1").move(0,.01f,0);
        }else if (binding.equals("Down")){
            rootNode.getChild("p1").move(0,-.01f,0);
        }else if (binding.equals("Forward")){
            rootNode.getChild("p1").move(.01f,0,0);
        }else if (binding.equals("Backward")){
            rootNode.getChild("p1").move(-.01f,0,0);
        }
    }

    @Override
    public void onPreUpdate(float tpf) {
      //  throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onPostUpdate(float tpf) {
      //  throw new UnsupportedOperationException("Not supported yet.");
    }
}