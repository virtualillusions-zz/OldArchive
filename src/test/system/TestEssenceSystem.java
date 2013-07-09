/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.system;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.simsilica.es.EntityId;
import com.spectre.app.SpectreApplication;
import com.spectre.scene.visual.components.InScenePiece;
import com.spectre.systems.essence.EssenceSystem;
import com.spectre.systems.essence.components.EssencePiece;

/**
 *
 * @author Kyle Williams
 */
public class TestEssenceSystem extends SpectreApplication {

    public static void main(String[] args) {
        TestEssenceSystem app = new TestEssenceSystem();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
        settings.putBoolean("DebugMode", true);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void SpectreApp() {
        getStateManager().attach(new EssenceSystem());

        getStateManager().attach(new AbstractAppState() {
            @Override
            public void initialize(AppStateManager stateManager, Application app) {
                super.initialize(stateManager, app);
                EntityId entityId = getEntityData().createEntity();
                getEntityData().setComponents(entityId, new EssencePiece(), new InScenePiece());
            }
        });
    }

    @Override
    public void spectreUpdate(float tpf) {
    }

    @Override
    public void spectreRender(RenderManager rm) {
    }
}
