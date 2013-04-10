/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.flairapi;

import com.spectre.deck.card.Animation;
import com.spectre.deck.card.Card;
import com.spectre.deck.card.Effect;
import com.spectre.deck.card.Effect.EffectType;
import com.spectre.director.SceneDirector;
import com.spectre.director.ToolsDirector;
import com.spectre.flairapi.EmitterNode.EmitterChildren;
import com.spectre.flairapi.slider.EffectTimers;
import com.spectre.seriesfiletype.SeriesDataObject.SeriesSaveCookie;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ChangeSupport;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Kyle Williams
 */
public class EffectNode extends AbstractNode {
    //<editor-fold defaultstate="collapsed" desc="Effect Children">    

    public static class EffectChildren extends ChildFactory.Detachable<Effect> {

        private ArrayList<Effect> effects;
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

        public EffectChildren(ArrayList<Effect> effects, ChangeSupport cs, SeriesSaveCookie sc) {
            this.effects = effects;
            this.cs = cs;
            this.sc = sc;
        }

        @Override
        protected boolean createKeys(List<Effect> toPopulate) {
            toPopulate.addAll(effects);
            return true;
        }

        @Override
        protected Node createNodeForKey(Effect key) {
            return new EffectNode(key, cs, sc);
        }
    }
    // </editor-fold>   
    private String directory;//the project directory

    public EffectNode(Effect key, ChangeSupport cs, SeriesSaveCookie sc) {
        super(Children.create(new EmitterChildren(key, cs, sc), true), Lookups.fixed(key, cs, sc));
        setDisplayName(key.toString());
    }

    @Override
    public Image getIcon(int type) {
        if (getEffectType() == EffectType.Active) {
            return ImageUtilities.loadImage("com/spectre/flairapi/activeEffectIcon.gif");
        } else if (getEffectType() == EffectType.Contact) {
            return ImageUtilities.loadImage("com/spectre/flairapi/contactEffectIcon.gif");
        } else if (getEffectType() == EffectType.Passive) {
            return ImageUtilities.loadImage("com/spectre/flairapi/passiveEffectIcon.gif");
        } else {
            return null;
        }
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    public EffectType getEffectType() {
        return getLookup().lookup(Effect.class).getEffectType();
    }

    @Override
    public String getHtmlDisplayName() {
        Effect obj = getLookup().lookup(Effect.class);
        EffectType type = obj.getEffectType();
        String boneLocation = obj.getBoneLocation();
        String audioLocation = obj.getAudioLocation().equals("") ? "N/A" : obj.getAudioLocation();
        int emitterSize = obj.getEmitters().size();
        return "<font color='!textText'>" + type + " Effect</font>"
                + "<font color='!controlShadow'><i> [" + boneLocation + ", " + audioLocation + ", " + emitterSize + "]</i></font>";
    }

    /**
     * @return directory the directory the projects DataNode is in
     */
    public String getDirectory() {
        if (directory == null) {
            //This is to Fix an issue evident in the file choosers in which it must access the 
            FileObject o;

            o = getLookup().lookup(SeriesSaveCookie.class).getSaveFileObject();//((DataNode) getParentNode().getParentNode().getParentNode()).getDataObject().getPrimaryFile();

            directory = FileUtil.toFile(o).getParent();
        }
        return directory;
    }

    public Card getCard() {
        return getParentNode().getLookup().lookup(Card.class);
    }

    @Override
    public Action[] getActions(boolean popup) {
        Action[] actions;
        if (getEffectType() == EffectType.Passive) {
            actions = new Action[]{
                setTime,
                null,
                setBoneLocation,
                null,
                addEmitter,
                null,
                setSFX,
                deleteSFX,
                null,
                Reset
            };
            return actions;
        } else if (getEffectType() == EffectType.Active) {
            actions = new Action[]{
                setStartTime,
                null,
                setBoneLocation,
                null,
                addEmitter,
                null,
                setSFX,
                deleteSFX,
                null,
                Reset
            };
            return actions;
        } else if (getEffectType() == EffectType.Contact) {
            actions = new Action[]{
                addEmitter,
                null,
                setSFX,
                deleteSFX,
                null,
                Reset
            };
            return actions;
        }


        return null;
    }
    private AbstractAction setTime = new AbstractAction("Set Start/End Time") {
        @Override
        public void actionPerformed(ActionEvent e) {

            final Animation animation = getCard().getAnimation();
            final Effect effect = getLookup().lookup(Effect.class);
            EffectTimers.SetEffectTime(animation, effect);//Does the Bulk Of The Work
            fireDisplayNameChange(null, getHtmlDisplayName());
            getLookup().lookup(ChangeSupport.class).fireChange();
            getCard().setData("dateModified", new java.util.Date().getTime());
        }
    };
    private AbstractAction setStartTime = new AbstractAction("Set Start Time") {
        @Override
        public void actionPerformed(ActionEvent e) {
            final Animation animation = getCard().getAnimation();
            final Effect effect = getLookup().lookup(Effect.class);
            EffectTimers.SetEffectStartTime(animation, effect);//Does the Bulk Of The Work
            getLookup().lookup(ChangeSupport.class).fireChange();
            getCard().setData("dateModified", new java.util.Date().getTime());
        }
    };
    @SuppressWarnings("unchecked")
    private AbstractAction setBoneLocation = new AbstractAction("set Bone Location") {
        @Override
        public void actionPerformed(ActionEvent e) {
            Effect effect = getLookup().lookup(Effect.class);
            Collection coll = SceneDirector.getInstance().getBoneList();
            String[] boneList = (String[]) coll.toArray(new String[coll.size()]);
            String selection = (String) JOptionPane.showInputDialog(null, "Select A Bone For The Effect To Originate From", "Select Bone",
                    JOptionPane.QUESTION_MESSAGE, null, boneList, effect.getBoneLocation());

            if (selection == null) {
                return;
            }
            int yes = JOptionPane.showConfirmDialog(null, "Are You Sure You Want to Change the Origin to " + selection + "!!",
                    "Origin Change Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (yes == JOptionPane.YES_OPTION) {
                effect.setBoneLocation(selection);
                fireDisplayNameChange(null, getHtmlDisplayName());
                getLookup().lookup(ChangeSupport.class).fireChange();
                getCard().setData("dateModified", new java.util.Date().getTime());
            }
        }
    };
    private AbstractAction setSFX = new AbstractAction("Set Sound Effect") {
        @Override
        public void actionPerformed(ActionEvent e) {
            String audioName = ToolsDirector.fileExplorer(getDirectory(), "SoundEffects");
            if (audioName == null) {
                return;
            }
            Effect effect = getLookup().lookup(Effect.class);
            effect.setAudioLocation(audioName);

            fireDisplayNameChange(null, getHtmlDisplayName());
            getLookup().lookup(ChangeSupport.class).fireChange();
            getCard().setData("dateModified", new java.util.Date().getTime());
        }
    };
    private AbstractAction deleteSFX = new AbstractAction("Remove Sound Effect") {
        @Override
        public void actionPerformed(ActionEvent e) {
            Effect effect = getLookup().lookup(Effect.class);
            int i = JOptionPane.showConfirmDialog(null, "Are you sure you wish to remove the SFX:\n" + effect.getAudioLocation() + " From this Effect?", "Unset SFX", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (i
                    == JOptionPane.YES_OPTION) {
                effect.setAudioLocation("");
                fireDisplayNameChange(null, getHtmlDisplayName());
                getLookup().lookup(ChangeSupport.class).fireChange();
                getCard().setData("dateModified", new java.util.Date().getTime());
                JOptionPane.showMessageDialog(null, "Sound Removed!");
            }
        }
    };
    private AbstractAction addEmitter = new AbstractAction("Add New Emitter") {
        @Override
        public void actionPerformed(ActionEvent e) {
            String effectName = ToolsDirector.fileExplorer(getDirectory(), "Effects");
            if (effectName == null) {
                return;


            }
            Effect effect = getLookup().lookup(Effect.class);


            if (effect.getEmitters().contains(effectName)) {
                JOptionPane.showMessageDialog(null, "Only One Type of Emmitter Allowed Per Effect", "Invalid Operation", JOptionPane.ERROR_MESSAGE);
                return;
            }

            effect.addEmitter(effectName);

            fireDisplayNameChange(
                    null, getHtmlDisplayName());
            getLookup().lookup(ChangeSupport.class).fireChange();
            getCard().setData("dateModified", new java.util.Date().getTime());
        }
    };
    AbstractAction Reset = new AbstractAction("Reset") {
        @Override
        public void actionPerformed(ActionEvent e) {
            int i = JOptionPane.showConfirmDialog(null, "Are You Sure You Wish To Reset This Effect\n" + getDisplayName() + "?", "Delete " + getDisplayName() + "?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (i == JOptionPane.YES_OPTION) {
                i = JOptionPane.showConfirmDialog(null, "You Are About To Delete Effect\n" + getDisplayName() + "?", "Deleting " + getDisplayName(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (i == JOptionPane.OK_OPTION) {
                    SceneDirector.getInstance().displayModel();
                    Effect effect = getLookup().lookup(Effect.class);

                    effect.resetEffect();

                    getCard().setData("dateModified", new java.util.Date().getTime());

                    getLookup().lookup(ChangeSupport.class).fireChange();
                }
            }
        }
    };

    @Override
    @SuppressWarnings("rawtypes")
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();

        Effect effect = getLookup().lookup(Effect.class);


        if (effect
                == null) {
            return sheet;
        }



        try {

            Property prop = new PropertySupport.Reflection<String>(effect, String.class, "getAudioLocation", null);
            prop.setName("Name: ");
            set.put(prop);

            prop = new PropertySupport.Reflection<String>(effect, String.class, "getBoneLocation", null);
            prop.setName("Bone Location: ");
            set.put(prop);

            prop = new PropertySupport.Reflection<String>(effect, String.class, "getAudioLocation", null);
            prop.setName("Audio: ");
            set.put(prop);

            prop = new PropertySupport.Reflection<Float>(effect, float.class, "getStartTime", null);
            prop.setName("Start Time: ");
            set.put(prop);

            prop = new PropertySupport.Reflection<Float>(effect, float.class, "getEndTime", null);
            prop.setName("End Time: ");
            set.put(prop);

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }

        sheet.put(set);

        return sheet;
    }
}