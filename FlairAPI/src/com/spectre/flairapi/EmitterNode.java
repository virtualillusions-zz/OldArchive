   /*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.flairapi;

import com.jme3.gde.core.assets.BinaryModelDataObject;
import com.spectre.deck.card.Card;
import com.spectre.deck.card.Effect;
import com.spectre.director.SceneDirector;
import com.spectre.seriesfiletype.SeriesDataObject.SeriesSaveCookie;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Kyle Williams
 */
public class EmitterNode extends AbstractNode {
    //<editor-fold defaultstate="collapsed" desc="Emitter Children">    

    public static class EmitterChildren extends ChildFactory.Detachable<String> {

        private Effect effect;
        private ChangeSupport cs;
        private SeriesSaveCookie sc;

        @Override
        protected void addNotify() {
            cs.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent ev) {
                    refresh(true);
                }
            });
        }

        public EmitterChildren(Effect effect, ChangeSupport cs, SeriesSaveCookie sc) {
            this.effect = effect;
            this.cs = cs;
            this.sc = sc;
        }

        @Override
        protected boolean createKeys(List<String> toPopulate) {
            toPopulate.addAll(effect.getEmitters());
            return true;
        }

        @Override
        protected Node createNodeForKey(String key) {
            return new EmitterNode(key, cs, sc);
        }
    }
    // </editor-fold>   

    public EmitterNode(String key, ChangeSupport cs, SeriesSaveCookie sc) {
        super(Children.LEAF, Lookups.fixed(key, cs, sc));
        setDisplayName(key.toString());
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("com/spectre/flairapi/emitterIcon.gif");
    }

    @Override
    public Action getPreferredAction() {
        return new AbstractAction("Play Emitter") {
            @Override
            public void actionPerformed(ActionEvent e) {
                BinaryModelDataObject data = null;
                try {
                    String path = getLookup().lookup(String.class);
                    String directory = ((EffectNode) getParentNode()).getDirectory();
                    File fi = new File(directory + "/Deck/Effects/" + path);
                    FileObject fo = FileUtil.toFileObject(fi);
                    data = (BinaryModelDataObject) DataObject.find(fo);
                } catch (DataObjectNotFoundException ex) {
                    Exceptions.printStackTrace(ex);
                }
                new com.jme3.gde.scenecomposer.OpenSceneComposer(data).actionPerformed(null);
            }
        };
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    public Action[] getActions(boolean popup) {
        return new Action[]{
                    Delete
                };
    }
    AbstractAction Delete = new AbstractAction("Delete") {
        @Override
        public void actionPerformed(ActionEvent e) {
            int i = JOptionPane.showConfirmDialog(null, "Are You Sure You Wish To Delete This Emmitter\n" + getDisplayName() + "?", "Delete " + getDisplayName() + "?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (i == JOptionPane.YES_OPTION) {
                i = JOptionPane.showConfirmDialog(null, "You Are About To Delete Emmiter\n" + getDisplayName() + "?", "Deleting " + getDisplayName(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (i == JOptionPane.OK_OPTION) {
                    SceneDirector.getInstance().displayModel();
                    Effect effect = getParentNode().getLookup().lookup(Effect.class);
                    String emitter = getLookup().lookup(String.class);
                    effect.getEmitters().remove(emitter);

                    getLookup().lookup(ChangeSupport.class).fireChange();
                    getParentNode().getParentNode().getParentNode().getLookup().lookup(Card.class).setData("dateModified", new java.util.Date().getTime());
                }
            }
        }
    };

    @Override
    @SuppressWarnings("rawtypes")
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();

        String emitter = getLookup().lookup(String.class);

        if (emitter == null) {
            return sheet;
        }

        try {

            Property prop = new PropertySupport.Reflection<String>(emitter, String.class, "toString", null);
            prop.setName("Name: ");
            set.put(prop);

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }

        sheet.put(set);

        return sheet;

    }
}