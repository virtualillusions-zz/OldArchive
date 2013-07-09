/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.system;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.simsilica.es.EntityId;
import com.spectre.app.SpectreApplication;
import com.spectre.scene.visual.VisualSystem;
import com.spectre.scene.visual.components.InScenePiece;
import com.spectre.scene.visual.components.VisualRepPiece;
import com.spectre.scene.visual.components.VisualRepPiece.VisualType;
import com.spectre.systems.input.Buttons;
import com.spectre.systems.input.components.ActionModePiece;

/**
 *
 * @author Kyle
 */
public class TestVisualRepSystem extends SpectreApplication {

    private EntityId entityId;

    public static void main(String[] args) {
        TestVisualRepSystem app = new TestVisualRepSystem();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
        settings.putBoolean("DebugMode", true);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void SpectreApp() {
        getStateManager().attach(new VisualSystem());


        getStateManager().attach(new AbstractAppState() {
            @Override
            public void initialize(AppStateManager stateManager, Application app) {
                super.initialize(stateManager, app);

                //Creates a new EntityId, the id is handled as an object to prevent botching
                entityId = getEntityData().createEntity();
                //A new TestComponent is added to the Entity
                getEntityData().setComponent(entityId, new VisualRepPiece("testData/Jaime/Jaime.j3o"));
                getEntityData().setComponent(entityId, new InScenePiece());
                Buttons.setUpRemote(inputManager, 1, entityId + "");
            }
        });
        /**
         * A white ambient light source.
         */
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        getRootNode().addLight(ambient);


        inputManager.addMapping("add", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("remove", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("change", new KeyTrigger(KeyInput.KEY_C));
        inputManager.addMapping("id", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(actionListener, "add", "remove", "change", "id");
    }
    private ActionListener actionListener = new ActionListener() {
        private boolean changed = true;

        @Override
        public void onAction(String name, boolean pressed, float tpf) {
            if (name.equals("id")) {
                ActionModePiece amp = getEntityData().getComponent(entityId, ActionModePiece.class);
                System.out.println(entityId + " : " + amp.toString());

            } else if (pressed) {
                System.out.println(name);
                if (name.equals("add")) {
                    getEntityData().setComponent(entityId, new InScenePiece());
                } else if (name.equals("remove")) {
                    getEntityData().removeComponent(entityId, InScenePiece.class);
                } else if (name.equals("change")) {
                    changed = !changed;
                    String s = changed == true ? "testData/Jaime/Jaime.j3o" : "testData/Terrain/Terrain.mesh.xml";
                    VisualType type = changed ? VisualType.Character : VisualType.Scene;
                    getEntityData().setComponent(entityId, new VisualRepPiece(s, type));
                }
            }
        }
    };

    @Override
    public void spectreUpdate(float tpf) {
    }

    @Override
    public void spectreRender(RenderManager rm) {
    }
}
