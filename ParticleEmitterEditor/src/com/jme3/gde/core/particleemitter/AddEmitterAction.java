package com.jme3.gde.core.particleemitter;

import com.jme3.effect.ParticleEmitter;
import com.jme3.gde.core.particleemitter.actions.NewEmitterAction;
import com.jme3.gde.core.undoredo.AbstractUndoableSceneEdit;
import com.jme3.gde.core.undoredo.SceneUndoRedoManager;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.openide.util.Lookup;
import org.openide.util.actions.Presenter;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Kyle Williams
 */
public class AddEmitterAction extends AbstractAction implements Presenter.Popup {

    protected ParticleEmitterProcessorNode emitterNode;

    public AddEmitterAction(ParticleEmitterProcessorNode node) {
        this.emitterNode = node;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public JMenuItem getPopupPresenter() {
        JMenu result = new JMenu("Add Particle Emitter..");
        for (NewEmitterAction di : Lookup.getDefault().lookupAll(NewEmitterAction.class)) {
            result.add(new JMenuItem(di.getAction(emitterNode)));
        }
        return result;
    }

    private void addFilterUndo(final ParticleEmitterProcessorNode epp, final ParticleEmitter emitter) {
        //add undo
        if (epp != null && emitter != null) {
            Lookup.getDefault().lookup(SceneUndoRedoManager.class).addEdit(this, new AbstractUndoableSceneEdit() {

                @Override
                public void sceneUndo() throws CannotUndoException {
                    epp.removeEmitter(emitter);
                }

                @Override
                public void sceneRedo() throws CannotRedoException {
                    epp.addEmitter(emitter);
                }

                @Override
                public void awtRedo() {
                    emitterNode.refresh();
                }

                @Override
                public void awtUndo() {
                    emitterNode.refresh();
                }
            });
        }
    }

    private void setModified() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                emitterNode.refresh();
            }
        });
    }
}
