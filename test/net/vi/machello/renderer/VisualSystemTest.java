/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vi.machello.renderer;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
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
import com.vi.machello.renderer.visual.VisualSystem;
import com.vi.machello.renderer.visual.components.InScenePiece;
import com.vi.machello.renderer.visual.components.VisualRepPiece;
import com.vi.machello.scene.dynamics.components.PositionPiece;

/**
 *
 * @author Kyle D. Williams
 */
public class VisualSystemTest extends EntityAppState {

    private EntityId id;
    public Spatial primary;
    public Node root;

    @Override
    protected void AppState(Application app, EntityData ed) {
        id = ed.createEntity();
        //A new TestComponent is added to the Entity
        ed.setComponent(id, new VisualRepPiece("testData/Jaime/Jaime.j3o"));
        ed.setComponent(id, new PositionPiece(0, 0, 0));
        ed.setComponent(id, new InScenePiece());



        InputManager inputManager = app.getInputManager();
        /**
         * Map one or more inputs to an action
         */
        inputManager.addMapping("Add",
                new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Remove",
                new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("Change",
                new KeyTrigger(KeyInput.KEY_C));
        /**
         * Add an action to one or more listeners
         */
        inputManager.addListener(actionListener, "Add", "Remove", "Change");

    }
    /**
     * Use ActionListener to respond to pressed/released inputs (key presses,
     * mouse clicks)
     */
    private ActionListener actionListener = new ActionListener() {
        private boolean changed = true;

        public void onAction(String name, boolean isPressed, float tpf) {
            if (isPressed) {
                log.debug(name);
                if (name.equals("Add")) {
                    getEntityData().setComponent(id, new InScenePiece());
                } else if (name.equals("Remove")) {
                    getEntityData().removeComponent(id, InScenePiece.class);
                } else if (name.equals("Change")) {
                    changed = !changed;
                    String s = changed == true ? "testData/Jaime/Jaime.j3o" : "testData/Sinbad/Sinbad.mesh.xml";
                    getEntityData().setComponent(id, new VisualRepPiece(s));
                }
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
                new VisualSystemTest()) {
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
