/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.flairapi;

import com.jme3.animation.AnimChannel;
import com.spectre.director.EditorDirector;
import com.spectre.director.SceneDirector;
import com.spectre.director.ToolsDirector;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.spectre.controller.character.SpectreAnimationController;
import com.spectre.deck.card.Animation;
import com.spectre.deck.card.Card;
import com.spectre.deck.card.Effect;
import com.spectre.seriesfiletype.SeriesDataObject.SeriesSaveCookie;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ChangeSupport;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Kyle Williams
 */
public class AnimationNode extends AbstractNode {

    public AnimationNode(Animation key, ChangeSupport cs, SeriesSaveCookie sc) {
        super(Children.LEAF, Lookups.fixed(key, cs, sc));
        setDisplayName(key.toString());
        cs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                fireDisplayNameChange(null, getHtmlDisplayName());
            }
        });
    }

    @Override
    public Action getPreferredAction() {
        return play;
    }

    @Override
    public String getHtmlDisplayName() {
        Animation obj = getLookup().lookup(Animation.class);
        return "<font color='!textText'>" + obj.toString() + "</font>";
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("com/spectre/flairapi/animIcon.gif");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    public Action[] getActions(boolean popup) {
        return new Action[]{openEditor, play, null, setAnim, null, setSpeed, null, setStartTime};
    }
    private static javax.swing.JPanel AnimationTab;
    @SuppressWarnings("unchecked")
    private AbstractAction openEditor = new AbstractAction("Open Editor") {
        @Override
        public void actionPerformed(ActionEvent e) {
            org.openide.windows.TopComponent vit = EditorDirector.getFlairVisualEditorTopComponent();
            vit.open();
            if (AnimationTab == null) {
                findAnimationsTab(vit);
            }
            vit.requestActive();
            vit.requestAttention(true);
            vit.requestFocus(true);
            AnimationTab.getParent().getParent().requestFocus();
            AnimationTab.getParent().requestFocus();
            AnimationTab.requestFocus();
        }

        public void findAnimationsTab(org.openide.windows.TopComponent tc) {
            for (java.awt.Component c : tc.getComponents()) {
                if ("scrollPanel".equals(c.getName())) {
                    javax.swing.JScrollPane ScrollPanel = (javax.swing.JScrollPane) c;
                    javax.swing.JTabbedPane TabbedPane = (javax.swing.JTabbedPane) ScrollPanel.getComponent(0);
                    AnimationTab = (JPanel) TabbedPane.getComponent(0);
                    if (AnimationTab == null) {
                        throw new NullPointerException("Something Did not Initalize correctly in AnimationNode.openEditor.findAnimationsTab()");
                    }
                    break;
                }
            }
        }
    };
    private AbstractAction play = new AbstractAction("Play Animation") {
        @Override
        public void actionPerformed(ActionEvent e) {
            Animation anim = getLookup().lookup(Animation.class);
            String animName = anim.getAnimationName();

            if (!SceneDirector.getInstance().animExists(animName)) {
                return;
            }

            SceneDirector.getInstance().displayModel();
            SpectreAnimationController animCont = SceneDirector.getInstance().getAnimationController();
            animCont.changeAnimation(animName, SpectreAnimationController.AnimPriority.DEBUG, LoopMode.DontLoop, anim.getSpeed(), anim.getStartTime());
        }
    };
    @SuppressWarnings("unchecked")
    private AbstractAction setAnim = new AbstractAction("Set Animation") {
        @Override
        public void actionPerformed(ActionEvent e) {
            Animation animation = getLookup().lookup(Animation.class);
            Collection<String> coll = SceneDirector.getInstance().getAllAnimations();
            String[] animNames = (String[]) coll.toArray(new String[coll.size()]);
            String selection = (String) JOptionPane.showInputDialog(null, "Please Select An Animation from the list of available ones", "Available Animations",
                    JOptionPane.INFORMATION_MESSAGE, null, animNames,
                    animation.getAnimationName() == null ? animNames[0] : animation.getAnimationName());
            if (selection == null) {
                return;
            }
            int yes = JOptionPane.showConfirmDialog(null, "Are You Sure You Want to Change the Current Animation to " + selection + "?\n This will also remove all Effects In All Lists",
                    "Animation Change Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (yes == JOptionPane.YES_OPTION) {
                if (selection == null) {
                    return;
                }
                animation.setAnimationName(selection);
                animation.setSpeed(1);
                animation.setStartTime(0);
                getLookup().lookup(ChangeSupport.class).fireChange();
                Card card = getParentNode().getLookup().lookup(Card.class);

                float animLength = SceneDirector.getInstance().getAnimControl().getAnimationLength(animation.getAnimationName());

                Effect eff = card.getPassiveEffect();
                eff.resetEffect();
                eff.setEndTime(animLength);

                eff = card.getActiveEffect();
                eff.resetEffect();
                eff.setStartTime(animLength);

                card.getContactEffect().resetEffect();

                getLookup().lookup(ChangeSupport.class).fireChange();
                card.setData("dateModified", new java.util.Date().getTime());
            }
        }
    };
    private AbstractAction setSpeed = new AbstractAction("Set Speed") {
        @Override
        public void actionPerformed(ActionEvent e) {
            Animation animation = getLookup().lookup(Animation.class);
            if (animation.getAnimationName().equals("")) {
                JOptionPane.showMessageDialog(null, "INVALID ACTION\nAnimation Must First Be Set.", "Animation Not Set", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane optionPane = new JOptionPane();
            float k = animation.getSpeed();
            if (k < 1) {
                k = k + 1;
            }
            SceneDirector.getInstance().displayModel();
            SpectreAnimationController sac = SceneDirector.getInstance().getAnimationController();
            sac.changeAnimation(animation.getAnimationName(), SpectreAnimationController.AnimPriority.DEBUG, LoopMode.Loop);
            ToolsDirector.setUpSlider(optionPane, "Animation Speed", k, 2, 25, -50, 100, true);
            //need to make this so lower than .5 is negative
            k = ((float) 2 * new Integer(optionPane.getInputValue().toString()) / 100);
            if (k < 1) {
                animation.setSpeed(k - 1);
            } else {
                animation.setSpeed(k);
            }
            getLookup().lookup(ChangeSupport.class).fireChange();
            getParentNode().getLookup().lookup(Card.class).setData("dateModified", new java.util.Date().getTime());
        }
    };
    private AbstractAction setStartTime = new AbstractAction("Set Start Time") {
        @Override
        public void actionPerformed(ActionEvent e) {
            Animation animation = getLookup().lookup(Animation.class);
            if (animation.getAnimationName().equals("")) {
                JOptionPane.showMessageDialog(null, "INVALID ACTION\nAnimation Must First Be Set.", "Animation Not Set", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane optionPane = new JOptionPane();
            float animLength = SceneDirector.getInstance().getAnimControl().getAnim(animation.getAnimationName()).getLength();
            SceneDirector.getInstance().displayModel();
            
            SceneDirector.getInstance().setAnim(animation.getAnimationName());
            
            ToolsDirector.setUpSlider(optionPane, "Animation Start Time", animation.getStartTime(), animLength, 10, 0, 100, false);
            animation.setStartTime(animLength * new Integer(optionPane.getInputValue().toString()) / 100);
            getLookup().lookup(ChangeSupport.class).fireChange();
            getParentNode().getLookup().lookup(Card.class).setData("dateModified", new java.util.Date().getTime());
        }
    };

    @Override
    @SuppressWarnings("rawtypes")
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();

        Animation anim = getLookup().lookup(Animation.class);

        if (anim == null) {
            return sheet;
        }

        try {
            Property prop = new PropertySupport.Reflection<String>(anim, String.class, "getAnimationName", null);
            prop.setName("Name: ");
            set.put(prop);

            prop = new PropertySupport.Reflection<Float>(anim, float.class, "getSpeed", null);
            prop.setName("Speed: ");
            set.put(prop);

            prop = new PropertySupport.Reflection<Float>(anim, float.class, "getStartTime", null);
            prop.setName("Start Time: ");
            set.put(prop);

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }

        sheet.put(set);

        return sheet;

    }
}