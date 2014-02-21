/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vi.machello.scene;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.vi.machello.app.ApplicationState;
import com.vi.machello.app.EntityDataState;
import com.vi.machello.app.state.EntityAppState;
import com.vi.machello.renderer.cam.CameraSystem;
import com.vi.machello.renderer.cam.components.CameraFocusPiece;
import com.vi.machello.renderer.visual.VisualSystem;
import com.vi.machello.renderer.visual.components.InScenePiece;
import com.vi.machello.renderer.visual.components.VisualRepPiece;
import com.vi.machello.scene.dynamics.components.MovementPiece;
import com.vi.machello.scene.dynamics.components.PositionPiece;
import com.vi.machello.scene.dynamics.components.RigidBodyPiece;
import com.vi.machello.scene.dynamics.components.RigidBodyPiece.CollisionType;
import net.vi.machello.scene.dynamics.DynamicsSystem;

/**
 *
 * @author Kyle D. Williams
 */
public class TestDynamics extends EntityAppState {

    private EntityId id;
    private EntityId id2;
    private boolean focus = true;
    public Spatial primary;
    public Node root;
    public EntityId fID;

    @Override
    protected void AppState(Application app, EntityData ed) {
        id = ed.createEntity();
        //A new TestComponent is added to the Entity 
        ed.setComponent(id, new VisualRepPiece("testData/Jaime/Jaime.j3o"));
        ed.setComponent(id, new PositionPiece(0f, 5, 0));//40.5.0
        ed.setComponent(id, new CameraFocusPiece());
        ed.setComponent(id, new RigidBodyPiece());
        ed.setComponent(id, new MovementPiece());
        ed.setComponent(id, new InScenePiece());
        //A new TestComponent is added to the Entity
        id2 = ed.createEntity();
        ed.setComponent(id2, new VisualRepPiece("testData/Jaime/Jaime.j3o"));
        ed.setComponent(id2, new PositionPiece(-5f, 5, 0));
        ed.setComponent(id2, new CameraFocusPiece());
        ed.setComponent(id2, new RigidBodyPiece(false));
        ed.setComponent(id, new MovementPiece());
        ed.setComponent(id2, new InScenePiece());
        EntityId idE = ed.createEntity();
        ed.setComponent(idE, new VisualRepPiece("testData/town/main.j3o"));
        ed.setComponent(idE, new PositionPiece(0f, 0, 0));
        ed.setComponent(idE, new RigidBodyPiece(CollisionType.STATIC));
        ed.setComponent(id, new MovementPiece());
        ed.setComponent(idE, new InScenePiece());


        InputManager inputManager = app.getInputManager();
        /**
         * Map one or more inputs to an action
         */
        inputManager.addMapping("Left",
                new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Right",
                new KeyTrigger(KeyInput.KEY_L));

        inputManager.addMapping("Up1",
                new KeyTrigger(KeyInput.KEY_V));
        inputManager.addMapping("Down1",
                new KeyTrigger(KeyInput.KEY_B));
        inputManager.addMapping("Left1",
                new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("Right1",
                new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("Away1",
                new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("Toward1",
                new KeyTrigger(KeyInput.KEY_G));

        inputManager.addMapping("Reset",
                new KeyTrigger(KeyInput.KEY_R));


        /**
         * Add an action to one or more listeners
         */
        inputManager.addListener(actionListener, "Left", "Right",
                "Up1", "Down1", "Left1", "Right1", "Away1", "Toward1", "Reset");

    }
    /**
     * Use ActionListener to respond to pressed/released inputs (key presses,
     * mouse clicks)
     *
     */
    private AnalogListener actionListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            tpf = 0.025f;
            float factor = 50 * tpf;
            if (name.equals("Left")) {
                getEntityData().setComponent(id, new MovementPiece(-factor * tpf, 0, 0));
            }
            if (name.equals("Right")) {
                getEntityData().setComponent(id, new MovementPiece(factor * tpf, 0, 0));
            }
            if (name.equals("Up1")) {
                getEntityData().setComponent(id2, new MovementPiece(0, factor * tpf, 0));
            }
            if (name.equals("Down1")) {
                getEntityData().setComponent(id2, new MovementPiece(0, -factor * tpf, 0));
            }
            if (name.equals("Left1")) {
                getEntityData().setComponent(id2, new MovementPiece(-factor * tpf, 0, 0));
            }
            if (name.equals("Right1")) {
                getEntityData().setComponent(id2, new MovementPiece(factor * tpf, 0, 0));
            }
            if (name.equals("Away1")) {
                getEntityData().setComponent(id2, new MovementPiece(0, 0, -factor * tpf));
            }
            if (name.equals("Toward1")) {
                getEntityData().setComponent(id2, new MovementPiece(0, 0, factor * tpf));
            }
            if (name.equals("Reset")) {
                getEntityData().setComponent(id, new PositionPiece(0, 5, 0));
                getEntityData().setComponent(id2, new PositionPiece(-5f, 5, 0));
            }
        }
    };

    @Override
    protected void cleanUp() {
    }

    public static void main(String[] args) {
        Application app = new SimpleApplication(
                new ApplicationState(),
                new EntityDataState(),
                new VisualSystem(),
                new CameraSystem(),
                //new FlyCamAppState(),
                new DynamicsSystem(),
                new TestDynamics()) {
            @Override
            public void simpleInitApp() {
                //this.flyCam.setMoveSpeed(100f);
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
