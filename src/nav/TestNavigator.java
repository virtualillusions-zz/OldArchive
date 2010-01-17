package nav;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.VBOInfo;
import com.jme.scene.shape.Sphere;
import java.util.logging.Level;
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
 */
public class TestNavigator extends SimpleGame {
 
    private static final Logger logger = Logger
            .getLogger(TestNavigator.class.getName());
 
    private Navigator _navigator;
 
    public static void main(String[] args) {
 
        TestNavigator app = new TestNavigator();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }
 
    @Override
    protected void simpleUpdate() {
        _navigator.simpleUpdate(cam);
    }
 
    @Override
    protected void simpleInitGame() {
 
        display.setTitle("Test Navigator");
 
        Vector3f waypoint = new Vector3f(0, 0, -350);
 
        // Add a sphere - it will be our waypoint
        Sphere s = new Sphere("Sphere", 12, 12, 12);
        s.setModelBound(new BoundingBox());
        s.updateModelBound();
 
        // set the location of the sphere to be the waypoint
        s.setLocalTranslation(waypoint);
 
        rootNode.attachChild(s);
        s.setVBOInfo(new VBOInfo(true));
        s.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
 
        // Create the navigator
        _navigator = new Navigator(0.25f, rootNode);
 
        // Set north
        // same dimension as skybox
        _navigator.setNorthVec(new Vector3f(0, 0, -100));
 
        // set the location of the waypoint
        // if the waypoint is not set (null) then
        // the outer waypoint card is not visible
        _navigator.setWaypoint(waypoint);
 
        // get the location of the upper left corner of the screen
        // in order to place the navigator on-screen
        float locX = _navigator.getWidth() - _navigator.getWidth() / 2;
        float locY = display.getHeight() - _navigator.getHeight() + _navigator.getHeight() / 2;
 
        // set navigator's on-screen location
        _navigator.setLocation(locX, locY);
 
    }
}