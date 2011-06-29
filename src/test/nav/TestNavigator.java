package test.nav;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import java.util.logging.Logger;


/**
 *
 * Test the Navigator. Ensure that it does point North and
 * that the waypoint, if set, points to the actual waypoint.
 * If the waypoint is not set (null) then the outer waypoint ring
 * is not displayed.
 * The Navigator is a combination compass and waypoint display.
 * The North indicator 'card' rotates with changes in the camera's heading
 * (the direction that it's pointing) relative to North; the outer waypoint
 * ring rotates with changes in the camera's location relative to the waypoint
 * (the bearing from camera to waypoint). Both North and the waypoint are
 * user-settable. If no waypoint is set (null) then the outer waypoint
 * ring is not visible.
 * * Note the call to _navigator.simpleUpdate in simpleUpdate()
 * @author Kropotkin
 * ported by 
 * @author Kyle Williams
 */
public class TestNavigator extends SimpleApplication {
 
    private static final Logger logger = Logger
            .getLogger(TestNavigator.class.getName());
 
    private Navigator _navigator;
 
    public static void main(String[] args) { 
        TestNavigator app = new TestNavigator();
        app.start();
    }
  
    @Override
    public void simpleUpdate(float tpf) {
        _navigator.simpleUpdate(cam);
    }
 
    @Override
    public void simpleInitApp() {
         /** Must add a light to make the lit object visible! */
        AmbientLight sun = new AmbientLight();
        rootNode.addLight(sun);
    
        this.flyCam.setMoveSpeed(100);
        settings.setTitle("Test Navigator");
        Vector3f waypoint = new Vector3f(0, 0, 0);
 
        // Add a sphere - it will be our waypoint
        Sphere s = new Sphere(14,14,1);
        Geometry geom = new Geometry("Box", s);
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat2);
       
        // set the location of the sphere to be the waypoint
        geom.setLocalTranslation(waypoint);
 
        rootNode.attachChild(geom);        
        // Create the navigator
        _navigator = new Navigator(rootNode,assetManager);
 
        // set the location of the waypoint
        // if the waypoint is not set (null) then
        // the outer waypoint card is not visible
        _navigator.setWaypoint(geom);
 
        // get the location of the upper left corner of the screen
        // in order to place the navigator on-screen
        float locX = _navigator.getWidth()/2;
        float locY = settings.getHeight()-_navigator.getHeight()/2;
 
        // set navigator's on-screen location
        _navigator.setLocation(locX, locY);        
    }
}