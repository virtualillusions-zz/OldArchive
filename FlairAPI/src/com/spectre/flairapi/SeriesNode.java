/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.flairapi;

import com.spectre.deck.SupplyDeck;
import com.spectre.deck.card.Card;
import com.spectre.deck.card.CardStats.CardSeries;
import com.spectre.director.EditorDirector;
import com.spectre.flairapi.CardNode.CardChildren;
import com.spectre.seriesfiletype.SeriesDataObject.SeriesSaveCookie;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ChangeSupport;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Kyle Williams
 */
public class SeriesNode extends AbstractNode implements PropertyChangeListener, Lookup.Provider {

    //<editor-fold defaultstate="collapsed" desc="SeriesChildren">    
    public static class SeriesChildren extends ChildFactory.Detachable<CardSeries> {

        private final SupplyDeck sd;
        private final ChangeSupport cs;
        private final SeriesSaveCookie sc;
        private final Loadable ls;

        public SeriesChildren(SupplyDeck sd, ChangeSupport cs, SeriesSaveCookie sc, Loadable ls) {
            this.sd = sd;
            this.cs = cs;
            this.sc = sc;
            this.ls = ls;
        }

        @Override
        protected boolean createKeys(List<CardSeries> toPopulate) {
            toPopulate.addAll(Arrays.asList(CardSeries.values()));
            return true;
        }

        @Override
        protected Node createNodeForKey(CardSeries key) {
            return new SeriesNode(key, sd, cs, sc);
        }

        @Override
        protected void addNotify() {
            sd.putAll(ls.load());
        }
    }
// </editor-fold>   

    public SeriesNode(CardSeries key, SupplyDeck sd, ChangeSupport cs, SeriesSaveCookie sc) {
        super(Children.create(new CardChildren(key, sd, cs, sc), true), Lookups.fixed(key, sd, cs, sc));
    }

    private SupplyDeck getMasterDeck() {
        return getLookup().lookup(SupplyDeck.class);
    }

    @Override
    public String getHtmlDisplayName() {
        CardSeries obj = getLookup().lookup(CardSeries.class);
        return "<font color='!textText'><b>" + obj.toString() + "</b></font>"
                + "<font color='!controlShadow'><i> Series</i></font>";

    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("com/spectre/flairapi/seriesIcon.gif");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    public Action[] getActions(boolean popup) {
        return new Action[]{new AddCard()};
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("date".equals(evt.getPropertyName())) {
            this.fireDisplayNameChange(null, getDisplayName());
        }
    }

    public class AddCard extends AbstractAction {

        public AddCard() {
            super("Create New Card");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String cardName = JOptionPane.showInputDialog(null, "Name Of New Card", "New Card Creation", JOptionPane.QUESTION_MESSAGE).toUpperCase();
            if (cardName == null || cardName.equals("") || getMasterDeck().containsKey(cardName)) {/*
                 * Uppercase used to comply with possible keys which are all
                 * uppercase by default
                 */
                JOptionPane.showMessageDialog(null, "Card Not Created!\nEither CardName Empty or\nCard Name Already In Use.");
            } else {
                CardSeries series = getLookup().lookup(CardSeries.class);
                Card card = EditorDirector.createNewCard(cardName, series);
                getMasterDeck().put(card);
                getLookup().lookup(ChangeSupport.class).fireChange();
            }
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();

        CardSeries obj = getLookup().lookup(CardSeries.class);

        if (obj == null) {
            return sheet;
        }

        try {
            PropertySupport.Reflection prop = new PropertySupport.Reflection<String>(obj, String.class, "toString", null);
            prop.setDisplayName("Series: ");
            prop.setShortDescription("This is the " + obj.toString() + " Card Series");
            set.put(prop);
            prop = new PropertySupport.Reflection<CardSeries>(obj, CardSeries.class, "getAncestor", null);
            prop.setDisplayName("Ancestor: ");
            prop.setShortDescription("The Previous Series to the " + obj.toString() + " is the" + obj.getAncestor() + "Card Series");
            set.put(prop);
            prop = new PropertySupport.Reflection<Integer>(obj, int.class, "getSeriesPosition", null);
            prop.setDisplayName("Stage: ");
            prop.setShortDescription("The Position of this Series to Raw is Position #" + obj.getSeriesPosition());
            set.put(prop);
        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }

        sheet.put(set);

        return sheet;
    }
}