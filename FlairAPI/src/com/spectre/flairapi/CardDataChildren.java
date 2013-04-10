/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.flairapi;

import com.spectre.deck.card.Animation;
import com.spectre.deck.card.Card;
import com.spectre.deck.card.Effect;
import com.spectre.seriesfiletype.SeriesDataObject.SeriesSaveCookie;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.ChangeSupport;

/**
 * @author Kyle Williams
 */
public class CardDataChildren extends ChildFactory<Object> {

    private Card card;
    private ChangeSupport cs;
    private SeriesSaveCookie sc;

    public CardDataChildren(Card obj, ChangeSupport cs, SeriesSaveCookie sc) {
        card = obj;
        this.cs = cs;
        this.sc = sc;
    }

    @Override
    protected boolean createKeys(List<Object> toPopulate) {
        toPopulate.add(card.getAnimation());

        toPopulate.add(card.getPassiveEffect());

        toPopulate.add(card.getActiveEffect());

        toPopulate.add(card.getContactEffect());

        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Node createNodeForKey(Object key) {
        if (key instanceof Animation) {
            return new AnimationNode((Animation) key, cs, sc);
        } else {
            return new EffectNode((Effect) key, cs, sc);
        }
    }
}
