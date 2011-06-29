package test.nav;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;


/**
 * The Navigator is a combination compass and waypoint display.
 * The North indicator 'card' rotates with changes in the camera's heading
 * (the direction that it's pointing) relative to North; the outer waypoint
 * ring rotates with changes in the camera's location relative to the waypoint
 * (the bearing from camera to waypoint). Both North and the waypoint are
 * user-settable. If no waypoint is set (null) then the outer waypoint
 * ring is not visible.
 * * Note: Don't forget to call simpleUpdate from the game simpleUpdate()
 * @author Kropotkin (with lots of code and comments from HUD tutorial)
 */
public class Navigator {
    public static void main(String[] args) { TestNavigator app = new TestNavigator();app.start();  }
    protected static AssetManager assets;
 
    // in the case of the compass, 'up' is along the Z axis.
    protected Vector3f _upVector         = new Vector3f(0, 0, 1);
    // for the waypoint, it's along the Y axis
    protected Vector3f _waypointUpVector = new Vector3f(0, 1, 0);
 
    // default values
    protected Vector3f   _northVec     = new Vector3f(0, 0, -10000).normalizeLocal();
    protected Vector2f   _north2f      = new Vector2f(0, -10000).normalizeLocal();
    protected Quaternion _compassQuat  = new Quaternion();
    protected Quaternion _waypointQuat = new Quaternion();
    protected Vector2f   _camDirection2f;
    protected Vector2f   _camLocation2f;
    protected Vector3f   _cameraDirectionVec;
    protected Vector3f   _cameraLocationVec;
    protected float      _compassLocalScale;
    protected float      _compassLocationX;
    protected float      _compassLocationY;
 
    // inner compass
    protected Node _compassNode;
    private Node   _parentNode;
 
    // outer waypoint
    protected Vector3f _waypoint;
    protected Vector2f _waypoint2f;
    protected Node     _waypointNode;
    
    private float width = 100f;
    private float height = 100f;
    private float locX = 0f;
    private float locY = 0f;

 
    public Navigator(Node parentNode,AssetManager assetManager) {
        assets = assetManager;
        _parentNode        = parentNode;
        _compassNode       = new Node("_compassNode");
        _compassNode.setQueueBucket(Bucket.Opaque);  
        _compassNode.setCullHint(Spatial.CullHint.Never);
        _compassNode.attachChild(createObject("Test/CompassNorth.png"));
        _parentNode.attachChild(_compassNode);
    }
 
    public void setWaypoint(Spatial target) {        
        _waypoint     = target.getLocalTranslation().clone();
        _waypoint2f   = new Vector2f(_waypoint.x, _waypoint.z);
        _waypointNode = new Node("_waypointNode");
        _waypointNode.setQueueBucket(Bucket.Opaque);   
        _waypointNode.setCullHint(Spatial.CullHint.Never);
        _waypointNode.attachChild(createObject("Test/CompassWaypoint.png"));
        _parentNode.attachChild(_waypointNode);
    }
    
    public final Geometry createObject(String Texture){
        Quad q = new Quad(width,height,false);
        
        Geometry g = new Geometry("quad", q);
        
        Material mat = new Material(assets, "Common/MatDefs/Misc/SimpleTextured.j3md");
        mat.setTexture("ColorMap", assets.loadTexture(Texture));
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);  // activate transparency

        g.center();
        
        g.setMaterial(mat);
        return g;
    }
 
 
    public void setLocation(float locX, float locY) {
        this.locX=locX;
        this.locY=locY;
    }
    
    public float getWidth(){return width;}
    public float getHeight(){return height;}
    public void setScale(float Width,float Height){
        width=Width;
        height=Height;
        this._compassNode.setLocalScale(new Vector3f(width, height, 1f));
        this._waypointNode.setLocalScale(new Vector3f(width, height, 1f));
    }
    
 
    public void simpleUpdate(Camera cam) { 
        // update the inner compass indicator
        //Rotate Properly
        _cameraDirectionVec = cam.getDirection().normalize();
        _camDirection2f     = new Vector2f(_cameraDirectionVec.x, _cameraDirectionVec.z);
        _compassQuat.fromAngleNormalAxis(_north2f.angleBetween(_camDirection2f), getUpVector());
        _compassNode.setLocalRotation(_compassQuat);
        //Translate Properly
        _compassNode.setLocalTranslation(cam.getLocation().add(locX, locY, 0));
 
        // update the outer waypoint
        if (_waypoint != null) {
            //Rotate Properly
            _cameraLocationVec = cam.getLocation().normalize();
            _camLocation2f     = new Vector2f(_cameraLocationVec.x, _cameraLocationVec.z);
            _waypointQuat.fromAngleNormalAxis(_camLocation2f.angleBetween(_waypoint2f) + FastMath.DEG_TO_RAD * 180, getUpVector());
            _waypointNode.setLocalRotation(_waypointQuat);
            //Translate Properly
            _waypointNode.setLocalTranslation(cam.getLocation().add(locX, locY, 0));
        }
    }
 
    /**
     * @return the _upVector
     */
    public Vector3f getUpVector() {
        return _upVector;
    }
 
    /**
     * @param _upVector the _upVector to set
     */
    public void setUpVector(Vector3f _upVector) {
        this._upVector = _upVector;
    }
 
    /**
     * @return the _waypointUpVector
     */
    public Vector3f getWaypointUpVector() {
        return _waypointUpVector;
    }
 
    /**
     * @param _waypointUpVector the _waypointUpVector to set
     */
    public void setWaypointUpVector(Vector3f _waypointUpVector) {
        this._waypointUpVector = _waypointUpVector;
    }
 
    /**
     * @return the _northVec
     */
    public Vector3f getNorthVec() {
        return _northVec;
    }
 
    /**
     * @param northVec the _northVec to set
     */
    public void setNorthVec(Vector3f northVec) {
        this._northVec = northVec;
        _north2f.x     = _northVec.x;
        _north2f.y     = _northVec.z;
    }
}