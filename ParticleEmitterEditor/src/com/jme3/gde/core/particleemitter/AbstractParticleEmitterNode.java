/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.jme3.gde.core.particleemitter;

import com.jme3.effect.ParticleEmitter;

import com.jme3.gde.core.particleemitter.actions.EnableEmitterAction;
import com.jme3.gde.core.sceneexplorer.nodes.JmeParticleEmitter;
import com.jme3.gde.core.sceneexplorer.nodes.properties.ScenePropertyChangeListener;

import java.awt.Image;
import java.io.IOException;
import javax.swing.Action;
import org.openide.actions.DeleteAction;
import org.openide.actions.MoveDownAction;
import org.openide.actions.MoveUpAction;
import org.openide.awt.Actions;
import org.openide.util.ImageUtilities;
import org.openide.util.actions.SystemAction;

/**
 *
 * @author Kyle Williams
 */
public class AbstractParticleEmitterNode extends JmeParticleEmitter implements ParticleEmitterNode, ScenePropertyChangeListener {

    protected ParticleEmitter emitter;
    private static Image icon;
    private static final String ICON_ENABLED = "com/jme3/gde/core/particleemitter/icons/emitter.gif";
    private static final String ICON_DISABLED = "com/jme3/gde/core/particleemitter/icons/emitter.gif";

    @Override
    public Image getOpenedIcon(int type) {
        return icon;
    }

    public void toggleIcon(boolean enabled) {
        if (enabled) {
            icon = ImageUtilities.loadImage(ICON_ENABLED);

        } else {
            icon = ImageUtilities.loadImage(ICON_DISABLED);

        }
        fireIconChange();
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{
                    Actions.alwaysEnabled(new EnableEmitterAction(this), "Toggle enabled", "", false),
                    SystemAction.get(MoveUpAction.class),
                    SystemAction.get(MoveDownAction.class),
                    null,
                    SystemAction.get(DeleteAction.class),};
    }

    @Override
    public Action getPreferredAction() {
        return Actions.alwaysEnabled(new EnableEmitterAction(this), "Toggle enabled", "", false);
    }

    @Override
    public boolean canDestroy() {
        return true;
    }

    @Override
    public void destroy() throws IOException {
        super.destroy();
        ParticleEmitterProcessorNode nod = (ParticleEmitterProcessorNode) getParentNode();
        nod.removeEmitter(emitter);
        fireSave(true);
    }

    /**
     * @param saveCookie the saveCookie to set
     */
    @Override
    public AbstractParticleEmitterNode setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    @Override
    public void propertyChange(final String name, final Object before, final Object after) {
        if (name.equals("Enabled")) {
            toggleIcon((Boolean) after);
        }
        fireSave(true);
        firePropertyChange(name, before, after);
    }

    public ParticleEmitter getEmitter() {
        return emitter;
    }
}
