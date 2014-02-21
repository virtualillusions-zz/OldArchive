/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vi.machello.renderer;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.vi.machello.app.ApplicationState;
import com.vi.machello.app.EntityDataState;
import com.vi.machello.app.state.EntityAppState;
import com.vi.machello.renderer.cam.CameraSystem;
import com.vi.machello.renderer.cam.components.CameraFocusPiece;
import com.vi.machello.renderer.cam.components.CameraFocusPiece.FocusType;
import com.vi.machello.renderer.visual.VisualSystem;
import com.vi.machello.renderer.visual.components.InScenePiece;
import com.vi.machello.renderer.visual.components.VisualRepPiece;
import com.vi.machello.scene.dynamics.components.PositionPiece;

/**
 *
 * @author Kyle D. Williams
 */
public class CameraSystemTest extends EntityAppState {

    private EntityId id;
    private EntityId id2;
    private float posX;
    private boolean focus = true;
    public Spatial primary;
    public Node root;
    public EntityId fID;

    @Override
    protected void AppState(Application app, EntityData ed) {
        id = ed.createEntity();
        //A new TestComponent is added to the Entity
        ed.setComponent(id, new VisualRepPiece("testData/Jaime/Jaime.j3o"));
        ed.setComponent(id, new PositionPiece(0, 0, 0));
        ed.setComponent(id, new CameraFocusPiece());
        posX = 0f;
        ed.setComponent(id, new InScenePiece());
        //A new TestComponent is added to the Entity
        EntityId idE = ed.createEntity();
        ed.setComponent(idE, new VisualRepPiece("testData/Jaime/Jaime.j3o"));
        ed.setComponent(idE, new PositionPiece(-5f, 0, 0));
        ed.setComponent(idE, new CameraFocusPiece());
        ed.setComponent(idE, new InScenePiece());
 

        InputManager inputManager = app.getInputManager();
        /**
         * Map one or more inputs to an action
         */
        inputManager.addMapping("Left",
                new KeyTrigger(KeyInput.KEY_H),
                new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Right",
                new KeyTrigger(KeyInput.KEY_K),
                new KeyTrigger(KeyInput.KEY_L));

        inputManager.addMapping("Add Focus",
                new KeyTrigger(KeyInput.KEY_A));

        inputManager.addMapping("Remove Focus",
                new KeyTrigger(KeyInput.KEY_R));
        /**
         * Add an action to one or more listeners
         */
        inputManager.addListener(actionListener, "Left", "Right", "Add Focus", "Remove Focus");

        id2 = ed.createEntity();
        ed.setComponent(id2, new CameraFocusPiece(FocusType.SCENE_FOCUS));
        ed.setComponent(id2, new PositionPiece(0, 0, 0));
        ed.setComponent(id2, new InScenePiece());
        primary = createSpatial(new Vector3f(0, 0, 0), new ColorRGBA(1f, 0f, 0f, 1f), app.getAssetManager());
        root = ((SimpleApplication) app).getRootNode();
        root.attachChild(primary);
    }
    /**
     * Use ActionListener to respond to pressed/released inputs (key presses,
     * mouse clicks)
     */
    private AnalogListener actionListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("Left")) {
                getEntityData().setComponent(id, new PositionPiece(posX -= tpf * 2, 0, 0));
            } else if (name.equals("Right")) {
                getEntityData().setComponent(id, new PositionPiece(posX += tpf * 2, 0, 0));
            } else if (name.equals("Add Focus") && focus == false) {
                getEntityData().setComponent(id2, new InScenePiece());
                root.attachChild(primary);
                focus = true;
            } else if (name.equals("Remove Focus") && focus == true) {
                getEntityData().removeComponent(id2, InScenePiece.class);
                root.detachChild(primary);
                focus = false;
            }
        }
    };

    @Override
    protected void cleanUp() {
    }

    public Spatial createSpatial(Vector3f loc, ColorRGBA color, AssetManager assetManager) {
        Box b = new Box(1, 1, 1); // create cube shape at the origin
        Spatial spat = new Geometry("Box", b);  // create cube geometry from the shape
        spat.setLocalTranslation(loc);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);   // set color of material to blue
        spat.setMaterial(mat);                   // set the cube's material
        return spat;
    }

    public static void main(String[] args) {
        Application app = new SimpleApplication(
                new ApplicationState(),
                new EntityDataState(),
                new VisualSystem(),
                new CameraSystem(),
                new CameraSystemTest()) {
            @Override
            public void simpleInitApp() {
                /**
                 * A white ambient light source.
                 */
                AmbientLight ambient = new AmbientLight();
                ambient.setColor(ColorRGBA.White);
                getRootNode().addLight(ambient);
            }
        };
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
        settings.putBoolean("DebugMode", true);
        app.setSettings(settings);
        app.start();
    }
}
