/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.deck;

import com.spectre.util.Attribute;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

/**
 * This is a Deck That should encompass every card in the game
 * @author Kyle Williams
 */
public class MasterDeck extends java.util.HashMap<String,Card>{
    HashSet<Attribute.CardSeries> series = new HashSet<Attribute.CardSeries>();
    @Override
    public Card put(String key, Card value) {
        series.add(value.series);
        return super.put(key, value);
    }
    /**
     * Returns the series Types held by this deck
     * @return 
     */
    public ArrayList<Attribute.CardSeries> getSeriesList(){return new ArrayList(series);}
    /**
     * Creates and returns a list of cards in a specified series
     * @param cardSeries
     * @return Cards in the specified Series
     */
    public ArrayList<Card> filterCardsBySeries(Attribute.CardSeries cardSeries){
        ArrayList<Card> cards = new ArrayList<Card>();
        for(Map.Entry<String,Card> card:entrySet()){
            if(cardSeries.equals(card.getValue().series)){
                cards.add(card.getValue());
            }
        }        
        return cards;
    }
        /**
     * Creates and returns a list of cards in a specified series
     * @param cardSeries
     * @return Cards in the specified Series
     */
    public ArrayList<String> filterCardNamesBySeries(Attribute.CardSeries cardSeries){
        ArrayList<String> cards = new ArrayList<String>();
        for(Map.Entry<String,Card> card:entrySet()){
            if(cardSeries.equals(card.getValue().series)){
                cards.add(card.getValue().getName());
            }
        }        
        return cards;
    }
}
