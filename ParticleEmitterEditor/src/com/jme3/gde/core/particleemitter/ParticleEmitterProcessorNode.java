/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.jme3.gde.core.particleemitter;

import com.jme3.effect.ParticleEmitter;
import com.jme3.gde.core.assets.EmitterDataObject;
import com.jme3.gde.core.particleemitter.actions.NewEmitterPopup;
import com.jme3.gde.core.scene.SceneApplication;
import com.jme3.scene.Spatial;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Kyle Williams
 */
public class ParticleEmitterProcessorNode extends AbstractNode {

    private EmitterDataObject dataObject;
    private com.jme3.scene.Node epp;

    public ParticleEmitterProcessorNode(EmitterDataObject dataObject) {
        super(new EmitterChildren(dataObject), Lookups.singleton(new EmitterIndexSupport()));

        //Lookups.singleton(new FilterIndexSupport((FilterChildren)this.getChildren()));
        this.dataObject = dataObject;
        setName(dataObject.getName()); 
        getLookup().lookup(EmitterIndexSupport.class).setParticleEmitterProcessorNode(this);
        ((EmitterChildren) getChildren()).setParticleEmitterProcessorNode(this);

    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("com/jme3/gde/core/particleemitter/icons/particle.gif");
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    public com.jme3.scene.Node getEmitterNode() {
        if (epp == null) {
            this.epp = dataObject.loadAsset();
        }
        return epp;
    }

    //this allow the reordering on drop of a Node
    @Override
    public PasteType getDropType(Transferable t, int action, final int index) {

        final Node node = NodeTransfer.node(t, action);
        return new PasteType() {

            @Override
            public Transferable paste() throws IOException {
                EmitterIndexSupport indexSupport = getLookup().lookup(EmitterIndexSupport.class);
                int nodeIndex = indexSupport.indexOf(node);
                if (nodeIndex < index) {
                    indexSupport.move(index - 1, nodeIndex);
                } else {
                    indexSupport.move(index, nodeIndex);
                }

                return null;
            }
        };
    }

    public void refresh() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                ((EmitterChildren) getChildren()).addNotify();
                ((EmitterChildren) getChildren()).doRefresh();
            }
        });
    }

    public void addEmitter(final ParticleEmitter spat) {
        SceneApplication.getApplication().enqueue(new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                getEmitterNode().attachChild(spat);
                return null;
            }
        });
        setModified();
        refresh();
    }

    public void removeEmitter(final ParticleEmitter spat) {
        SceneApplication.getApplication().enqueue(new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                getEmitterNode().detachChild(spat);
                return null;
            }
        });
        setModified();
        refresh();
    }

    protected void setModified() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                dataObject.setModified(true);
            }
        });
    }

    @Override
    public Action[] getActions(boolean context) {
//        return super.getActions(context);
        return new Action[]{
                    new NewEmitterPopup(this)
                };
    }

    public static class EmitterChildren extends Children.Keys<Object> {

        private EmitterDataObject dataObject;
        private ParticleEmitterProcessorNode node;
        private boolean readOnly = false;

        public EmitterChildren(EmitterDataObject dataObject) {
            this.dataObject = dataObject;
        }

        public void setParticleEmitterProcessorNode(ParticleEmitterProcessorNode node) {
            this.node = node;
        }

        @Override
        protected void addNotify() {
            super.addNotify();
            setKeys(createKeys());
        }

        protected void doRefresh() {
            refresh();
        }

        protected void reorderNotify() {
            setKeys(createKeys());
        }

        protected List<Object> createKeys() {
            try {
                return SceneApplication.getApplication().enqueue(new Callable<List<Object>>() {

                    @Override
                    public List<Object> call() throws Exception {
                        
                        List<Object> keys = new LinkedList<Object>();
                        for (Iterator it = node.getEmitterNode().getChildren().iterator(); it.hasNext();) {
                            ParticleEmitter filter = (ParticleEmitter) it.next();
                            keys.add(filter);
                        }
                        
                        if(keys.isEmpty()){
                            keys.add(new ParticleEmitter());
                        }
                        return keys;
                    }
                }).get();
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            } catch (ExecutionException ex) {
                Exceptions.printStackTrace(ex);
            }
            return null;
        }

        @Override
        protected Node[] createNodes(Object t) {
            com.jme3.scene.Node node = (com.jme3.scene.Node) t;
            for (ParticleEmitterNode di : Lookup.getDefault().lookupAll(ParticleEmitterNode.class)) {
                if (di.getExplorerObjectClass().getName().equals(node.getClass().getName())) {
                    Node[] ret = di.createNodes(node, dataObject, readOnly);
                    if (ret != null) {
                        return ret;
                    }
                }
            }
            return new Node[]{};
        }
    }
}
