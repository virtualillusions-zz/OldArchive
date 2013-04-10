/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.jme3.gde.core.particleemitter;

import com.jme3.effect.ParticleEmitter;
import com.jme3.gde.core.particleemitter.ParticleEmitterProcessorNode.EmitterChildren;
import com.jme3.gde.core.scene.SceneApplication;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import org.openide.nodes.Index;
import org.openide.nodes.Node;

/**
 *
 * @author Kyle Williams
 */
public class EmitterIndexSupport extends Index.Support {

    ParticleEmitterProcessorNode eppNode;
    EmitterChildren children;

    public EmitterIndexSupport() {
    }

    @Override
    public Node[] getNodes() {
        return eppNode.getChildren().getNodes();
    }

    @Override
    public int getNodesCount() {
        return eppNode.getChildren().getNodesCount();
    }

    public ParticleEmitterProcessorNode getFilterPostProcessorNode() {
        return eppNode;
    }

    public void setParticleEmitterProcessorNode(ParticleEmitterProcessorNode eppNode) {
        this.eppNode = eppNode; 
    }

    @Override
    public void reorder(final int[] perm) {

        SceneApplication.getApplication().enqueue(new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                List<ParticleEmitter> filters = new ArrayList<ParticleEmitter>();
                for (Iterator it = eppNode.getEmitterNode().getChildren().iterator(); it.hasNext();) {
                    ParticleEmitter f = (ParticleEmitter) it.next();
                    filters.add(f);
                }
                System.err.println("reordering");
                eppNode.getEmitterNode().detachAllChildren();
                for (int i = 0; i < perm.length; i++) {
                    eppNode.getEmitterNode().attachChild(filters.get(perm[i]));
                }
                return null;
            }
        });
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                ((EmitterChildren) eppNode.getChildren()).reorderNotify();
                ((EmitterChildren) eppNode.getChildren()).doRefresh();
            }
        });

    }
}
