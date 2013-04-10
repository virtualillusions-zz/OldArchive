/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.jme3.gde.core.particleemitter;

import com.jme3.gde.core.assets.EmitterDataObject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "ParticleEmitters",
id = "com.jme3.gde.core.particleemitter.OpenEmitterAction")
@ActionRegistration(displayName = "#CTL_OpenEmitterAction")
@ActionReferences({
    @ActionReference(path = "Loaders/application/jme3particleemitter/Actions", position = 10)
})
@Messages("CTL_OpenEmitterAction=Open Emitter")
public final class OpenEmitterAction implements ActionListener {

    private final EmitterDataObject context;

    public OpenEmitterAction(EmitterDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        // TODO use context
        ParticleEmitterExplorerTopComponent.findInstance().loadFile(context);
    }
}
