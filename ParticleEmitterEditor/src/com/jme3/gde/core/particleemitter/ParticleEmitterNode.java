/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.jme3.gde.core.particleemitter;

import org.openide.loaders.DataObject;
import org.openide.nodes.Node;

/**
 *
 * @author Kyle Williams
 */
public interface ParticleEmitterNode {

    public Class<?> getExplorerObjectClass();

    public Node[] createNodes(Object key, DataObject data, boolean readOnly);
}
