/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.jme3.gde.core.particleemitter.actions;

import com.jme3.gde.core.particleemitter.ParticleEmitterProcessorNode;
import javax.swing.Action;

/**
 *
 * @author Kyle Williams
 */
public interface NewEmitterAction {

    public Action getAction(ParticleEmitterProcessorNode rootNode);
}
