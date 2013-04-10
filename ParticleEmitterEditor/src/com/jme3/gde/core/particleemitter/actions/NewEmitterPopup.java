/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.jme3.gde.core.particleemitter.actions;

import com.jme3.effect.ParticleEmitter;
import com.jme3.gde.core.particleemitter.ParticleEmitterProcessorNode;
import com.jme3.gde.core.undoredo.AbstractUndoableSceneEdit;
import com.jme3.gde.core.undoredo.SceneUndoRedoManager;
import com.jme3.scene.Node;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.openide.util.Lookup;
import org.openide.util.actions.Presenter;

/**
 *
 * @author Kyle Williams
 */
public class NewEmitterPopup extends AbstractAction implements Presenter.Popup {

    protected ParticleEmitterProcessorNode filterNode;

    public NewEmitterPopup(ParticleEmitterProcessorNode node) {
        this.filterNode = node;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public JMenuItem getPopupPresenter() {
        JMenu result = new JMenu("Add Particle Emitter..");
        for (NewEmitterAction di : Lookup.getDefault().lookupAll(NewEmitterAction.class)) {
            result.add(new JMenuItem(di.getAction(filterNode)));
        }
        return result;
    }

    private void addParticleEmitterUndo(final Node fpp, final ParticleEmitter filter) {
        //add undo
        if (fpp != null && filter != null) {
            Lookup.getDefault().lookup(SceneUndoRedoManager.class).addEdit(this, new AbstractUndoableSceneEdit() {

                @Override
                public void sceneUndo() throws CannotUndoException {
                    fpp.detachChild(filter);
                }

                @Override
                public void sceneRedo() throws CannotRedoException {
                    fpp.attachChild(filter);
                }

                @Override
                public void awtRedo() {
                    filterNode.refresh();
                }

                @Override
                public void awtUndo() {
                    filterNode.refresh();
                }
            });
        }
    }

    private void setModified() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                filterNode.refresh();
            }
        });
    }
}