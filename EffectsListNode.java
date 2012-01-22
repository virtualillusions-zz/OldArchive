/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.flairapi;

import com.spectre.deck.Card;
import com.spectre.deck.Effect;
import com.spectre.flairapi.EffectNode.EffectChildren;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
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
class EffectsListNode extends AbstractNode {

    private final String dispName;

    public EffectsListNode(ArrayList<Effect> key, String DisplayName/*This extra field fixes an issue thats caused when adding to the ArrayList*/, ChangeSupport cs) {
        super(Children.create(new EffectChildren(key, cs), true), Lookups.fixed(key, cs));
        dispName = DisplayName;
        setDisplayName(dispName);
    }

    @Override
    public String getHtmlDisplayName() {
        return "<font color='!textText'>" + dispName + "</font>";
    }

    @Override
    public Image getIcon(int type) {
        if (getDisplayName().equals("Passive Effects")) {
            return ImageUtilities.loadImage("com/spectre/flairapi/passiveEffectIcon.gif");
        }
        return null;
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    public Action[] getActions(boolean popup) {
        return new Action[]{
                    new AbstractAction("Create New Effect") {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Card card = getParentNode().getLookup().lookup(Card.class);
                            if (card.getAnimation().getAnimationName().equals("")) {
                                JOptionPane.showMessageDialog(null, "Please Select An animation before selecting effects.\n If animation is changed all effects will be removed.");
                                return;
                            }

                            int create = JOptionPane.showConfirmDialog(null, "Would You Like To Create A New Effect", "Create Effect", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (create == JOptionPane.YES_OPTION) {/*
                                 * Uppercase used to comply with possible keys
                                 * which are all uppercase by default
                                 */
                                Effect effect = new Effect();

                                if (getDisplayName().equals("Passive Effects")) {
                                    card.getPassiveEffects().add(effect);
                                } else if (getDisplayName().equals("Active Effects")) {
                                    card.getActiveEffects().add(effect);
                                } else if (getDisplayName().equals("Contact Effects")) {
                                    card.getContactEffects().add(effect);
                                } else {
                                    throw new UnsupportedOperationException("Unable To Add Effect To A Plausabile List.");
                                }
                                card.setData("dateModified", new java.util.Date().getTime());
                                getLookup().lookup(ChangeSupport.class).fireChange();
                            }
                        }
                    },
                    null,
                    new AbstractAction("Delete All") {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int create = JOptionPane.showConfirmDialog(null, "Are You Sure You Want To Delete All Effects In This List", "Delete All Effects", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (create == JOptionPane.YES_OPTION) {
                                create = JOptionPane.showConfirmDialog(null, "Delete All Effects?\n Effects Deleted are not recoverable", "Are You Sure Delete All Effects?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                                if (create == JOptionPane.OK_OPTION) {
                                    Card card = getParentNode().getLookup().lookup(Card.class);
                                    if (getDisplayName().equals("Passive Effects")) {
                                        card.getPassiveEffects().clear();
                                    } else if (getDisplayName().equals("Active Effects")) {
                                        card.getActiveEffects().clear();
                                    } else if (getDisplayName().equals("Contact Effects")) {
                                        card.getContactEffects().clear();
                                    } else {
                                        throw new UnsupportedOperationException("Unable To Add Effect To A Plausabile List.");
                                    }
                                    card.setData("dateModified", new java.util.Date().getTime());
                                    getLookup().lookup(ChangeSupport.class).fireChange();
                                }
                            }
                        }
                    }
                };
    }

    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();

        ArrayList effectList = getLookup().lookup(ArrayList.class);

        if (effectList == null) {
            return sheet;
        }

        try {

            Property prop = new PropertySupport.Reflection<String>(this, String.class, "getDisplayName", null);
            prop.setName("Name: ");
            set.put(prop);

            prop = new PropertySupport.Reflection<Integer>(effectList, int.class, "size", null);
            prop.setName("List Size: ");
            set.put(prop);

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }

        sheet.put(set);

        return sheet;

    }
}