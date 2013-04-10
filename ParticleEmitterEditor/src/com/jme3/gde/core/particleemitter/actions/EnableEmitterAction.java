/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.jme3.gde.core.particleemitter.actions;

import com.jme3.effect.ParticleEmitter;
import com.jme3.gde.core.particleemitter.AbstractParticleEmitterNode;
import com.jme3.gde.core.scene.SceneApplication;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kyle Williams
 */
public class EnableEmitterAction implements ActionListener {

    private final AbstractParticleEmitterNode context;

    public EnableEmitterAction(AbstractParticleEmitterNode context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {

        SceneApplication.getApplication().enqueue(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                ParticleEmitter filter = context.getEmitter();
                filter.setEnabled(!filter.isEnabled());
                Logger.getLogger(EnableEmitterAction.class.getName()).log(Level.INFO, "{0} {1}", new Object[]{filter.isEnabled() ? "Enabled" : "Disabled", filter.getName()});
                context.propertyChange("Enabled", !filter.isEnabled(), filter.isEnabled());

                return null;
            }
        });
    }
}
