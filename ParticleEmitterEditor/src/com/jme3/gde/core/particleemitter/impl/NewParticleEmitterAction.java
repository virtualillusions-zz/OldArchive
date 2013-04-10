/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.jme3.gde.core.particleemitter.impl;

import com.jme3.effect.ParticleEmitter;
import com.jme3.gde.core.particleemitter.actions.AbstractNewEmitterAction;
import com.jme3.gde.core.particleemitter.actions.NewEmitterAction;

/**
 *
 * @author Kyle Williams
 */
@org.openide.util.lookup.ServiceProvider(service = NewEmitterAction.class)
public class NewParticleEmitterAction extends AbstractNewEmitterAction {

    public NewParticleEmitterAction() {
        name = "Particle Emitter";
    }

    @Override
    protected ParticleEmitter doCreateEmitter() {
        return new ParticleEmitter();
    }
    
}
