/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.jme3.gde.core.particleemitter.actions;

import com.jme3.effect.ParticleEmitter;
import com.jme3.gde.core.particleemitter.ParticleEmitterProcessorNode;
import com.jme3.gde.core.scene.SceneApplication;
import com.jme3.gde.core.undoredo.AbstractUndoableSceneEdit;
import com.jme3.gde.core.undoredo.SceneUndoRedoManager;
import java.awt.event.ActionEvent;
import java.util.concurrent.Callable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.openide.util.Lookup;

/**
 *
 * @author Kyle Williams
 */
public abstract class AbstractNewEmitterAction implements NewEmitterAction {

    protected String name = "*";

    protected abstract ParticleEmitter doCreateEmitter();

    protected Action makeAction(final ParticleEmitterProcessorNode rootNode) {
        return new AbstractAction(name) {

            @Override
            public void actionPerformed(ActionEvent e) {
                SceneApplication.getApplication().enqueue(new Callable<Void>() {

                    @Override
                    public Void call() throws Exception {
                        final ParticleEmitter filter = doCreateEmitter();
                        if (filter != null && rootNode != null) {
                            rootNode.addEmitter(filter);
                            SceneUndoRedoManager undoRedo = Lookup.getDefault().lookup(SceneUndoRedoManager.class);
                            if (undoRedo == null) {
                                return null;
                            }
                            undoRedo.addEdit(this, new AbstractUndoableSceneEdit() {

                                @Override
                                public void sceneUndo() throws CannotUndoException {
                                    rootNode.removeEmitter(filter);
                                }

                                @Override
                                public void sceneRedo() throws CannotRedoException {
                                    rootNode.addEmitter(filter);
                                }

                                @Override
                                public void awtRedo() {
//                                    rootNode.refresh();
                                }

                                @Override
                                public void awtUndo() {
//                                    rootNode.refresh();
                                }
                            });
                        }
                        return null;
                    }
                });
            }
        };
    }

    @Override
    public Action getAction(ParticleEmitterProcessorNode rootNode) {
        return makeAction(rootNode);
    }
}
