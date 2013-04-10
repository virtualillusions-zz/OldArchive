/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.flairapi;

import com.spectre.director.EditorDirector;
import com.spectre.director.SceneDirector;
import com.spectre.deck.SupplyDeck;
import com.spectre.deck.card.Card;
import com.spectre.deck.card.CardStats.CardRange;
import com.spectre.deck.card.CardStats.CardSeries;
import com.spectre.deck.card.CardStats.CardTrait;
import com.spectre.director.Debug_Director;
import com.spectre.seriesfiletype.SeriesDataObject.SeriesSaveCookie;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.ErrorManager;
import org.openide.actions.RenameAction;
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
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Kyle Williams
 */
public class CardNode extends AbstractNode {

    //<editor-fold defaultstate="collapsed" desc="Card Children">    
    public static class CardChildren extends ChildFactory.Detachable<Card> {

        private final CardSeries cardSeries;
        private final SupplyDeck sd;
        private final ChangeSupport cs;
        private final SeriesSaveCookie sc;

        @Override
        protected void addNotify() {
            super.addNotify();
            cs.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent ev) {
                    refresh(true);
                }
            });
        }

        public CardChildren(CardSeries key, SupplyDeck sd, ChangeSupport cs, SeriesSaveCookie sc) {
            this.cardSeries = key;
            this.sd = sd;
            this.cs = cs;
            this.sc = sc;
        }

        @Override
        protected boolean createKeys(List<Card> toPopulate) {
            toPopulate.addAll(sd.filterCardsBySeries(cardSeries, true));
            return true;
        }

        @Override
        protected Node createNodeForKey(Card key) {
            return new CardNode(key, sd, cs, sc);
        }
    }
    // </editor-fold>   

    public CardNode(Card key, SupplyDeck sd, ChangeSupport cs, SeriesSaveCookie sc) {
        super(Children.create(new CardDataChildren(key, cs, sc), true), Lookups.fixed(key, sd, cs, sc));//.create(new CardChildren(), true), Lookups.singleton(obj));
        setDisplayName(key.getName());
        key.lockData(false);
    }

    private SupplyDeck getMasterDeck() {
        return getLookup().lookup(SupplyDeck.class);
    }

    @Override
    public String getHtmlDisplayName() {
        Card obj = getLookup().lookup(Card.class);
        return "<font color='!textText'>" + obj.getName() + "</font>";
    }

    @Override
    public Action getPreferredAction() {
        return new AbstractAction("Display Model") {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEditor.actionPerformed(e);
                displayModel.actionPerformed(e);
            }
        };
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("com/spectre/flairapi/cardIcon.gif");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    public boolean canRename() {
        return true;
    }

    @Override
    public void setName(String nue) {
        if (nue == null || nue.equals("") || getMasterDeck().containsKey(nue.toUpperCase())) {
            JOptionPane.showMessageDialog(null, "The Card Name \n" + nue + "\n has Already been Taken");
        } else {
            Card card = getLookup().lookup(Card.class);
            int i = JOptionPane.showConfirmDialog(null, "Are You Sure You want to Rename\n" + card.getName() + " to " + nue.toUpperCase() + "?", "Delete " + card.getName() + "?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (i == JOptionPane.YES_OPTION) {
                getMasterDeck().remove(card.getName());
                card.setData("cardName", nue.toUpperCase());
                getMasterDeck().put(card);
                getLookup().lookup(ChangeSupport.class).fireChange();
                fireDisplayNameChange(null, getDisplayName());
                LookupEvent evt = new LookupEvent(getLookup().lookupResult(Card.class));
                ((LookupListener) EditorDirector.getFliarDataEditorTopComponent()).resultChanged(evt);
            }
        }
    }

    @Override
    public Action[] getActions(boolean popup) {
        return new Action[]{
                    displayModel,
                    null,
                    openEditor,
                    null,
                    Play,
                    null,
                    SystemAction.get(RenameAction.class),
                    null,
                    Delete
                };
    }
    @SuppressWarnings("unchecked")
    private AbstractAction displayModel = new AbstractAction("Display Model") {
        @Override
        public void actionPerformed(ActionEvent e) {
            SceneDirector.getInstance().displayModel();
        }
    };
    @SuppressWarnings("unchecked")
    private AbstractAction openEditor = new AbstractAction("Open Editor") {
        @Override
        public void actionPerformed(ActionEvent e) {
            EditorDirector.getFliarDataEditorTopComponent().open();
            EditorDirector.getFlairVisualEditorTopComponent().open();

            LookupEvent evt = new LookupEvent(getLookup().lookupResult(Card.class));
            ((LookupListener) EditorDirector.getFliarDataEditorTopComponent()).resultChanged(evt);
            ((LookupListener) EditorDirector.getFlairVisualEditorTopComponent()).resultChanged(evt);

            evt = new LookupEvent(getLookup().lookupResult(ChangeSupport.class));
            ((LookupListener) EditorDirector.getFliarDataEditorTopComponent()).resultChanged(evt);
            ((LookupListener) EditorDirector.getFlairVisualEditorTopComponent()).resultChanged(evt);

            evt = new LookupEvent(getLookup().lookupResult(SeriesSaveCookie.class));
            ((LookupListener) EditorDirector.getFlairVisualEditorTopComponent()).resultChanged(evt);

            EditorDirector.getFliarDataEditorTopComponent().requestActive();
            EditorDirector.getFlairVisualEditorTopComponent().requestActive();

        }
    };
    private AbstractAction Play = new AbstractAction("Play") {
        @Override
        public void actionPerformed(ActionEvent e) {
            Card card = getLookup().lookup(Card.class);
            if (card.getAnimation().getAnimationName() == null || card.getAnimation().getAnimationName().equals("")) {
                JOptionPane.showMessageDialog(null, "The Animation Name is not Set.", "Null Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (card.getActiveEffect().getEmitters().size() <= 0 || card.getContactEffect().getEmitters().size() <= 0) {
                JOptionPane.showMessageDialog(null, "Either Active or Contact Effect emitters are not Set.", "Null Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SceneDirector.getInstance().displayModel();
            //This is to Fix an issue evident in the file choosers in which it must access the 
            FileObject o = getLookup().lookup(SeriesSaveCookie.class).getSaveFileObject();//((DataNode) getParentNode().getParentNode()).getDataObject().getPrimaryFile();
            String path = FileUtil.toFile(o).getParent();
            Debug_Director.setAssetsPath(path);
            SceneDirector.getInstance().getCardManifestController().addToActiveQueue(card);
        }
    };
    private AbstractAction Delete = new AbstractAction("Delete") {
        @Override
        public void actionPerformed(ActionEvent e) {
            Card card = getLookup().lookup(Card.class);
            int i = JOptionPane.showConfirmDialog(null, "Are You Sure You Wish To Delete Card\n" + card.getName() + "?", "Delete " + card.getName() + "?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (i == JOptionPane.YES_OPTION) {
                i = JOptionPane.showConfirmDialog(null, "You Are About To Delete Card\n" + card.getName() + "?", "Deleting " + card.getName(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (i == JOptionPane.OK_OPTION) {

                    getMasterDeck().remove(card.getName(), card);
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

        Card card = getLookup().lookup(Card.class);

        if (card == null) {
            return sheet;
        }

        try {
            Property prop = new PropertySupport.Reflection<String>(card, String.class, "getName", null);
            prop.setName("Name");
            set.put(prop);

            prop = new PropertySupport.Reflection<Date>(card, Date.class, "getDateModified", null);
            prop.setName("Date Modified");
            set.put(prop);


            prop = new PropertySupport.Reflection<CardSeries>(card, CardSeries.class, "getSeries", null);
            prop.setName("Series");
            set.put(prop);

            prop = new PropertySupport.Reflection<CardTrait>(card, CardTrait.class, "getTrait", null);
            prop.setName("Trait");
            set.put(prop);

            prop = new PropertySupport.Reflection<CardRange>(card, CardRange.class, "getRange", null);
            prop.setName("Range");
            set.put(prop);

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }

        sheet.put(set);

        return sheet;

    }
}