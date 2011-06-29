/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tools.flairEditor;

import com.jme3.animation.AnimChannel;
import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;
import com.spectre.deck.CardSetter;
import com.spectre.deck.MasterDeck;
import java.util.ArrayList;

/**
 *
 * @author Kyle Williams
 */
public final class FlairDirector {
    private static ArrayList<String> animList = new ArrayList<String>();
    private static ArrayList<String> boneList = new ArrayList<String>();
    private static ArrayList<String> archive = new ArrayList<String>();
    private static AnimChannel channel;
    private static SimpleApplication app;
    private static Spatial spat;
    private static MasterDeck deck;
    //APPLICATION
    public static void setApp(SimpleApplication application){app=application;}
    public static SimpleApplication getApp(){return app;}
    //SPATIAL
    public static void setSpatial(Spatial spatial){spat=spatial;}
    public static Spatial getSpatial(){return spat;}
    //ANIMATION LIST
    public static void addAnimation(String animation){animList.add(animation);}
    public static ArrayList<String> getAnimList(){return animList;}
    //ANIMATION CHANNEL
    public static void setAnimChannel(AnimChannel chnl){channel = chnl;}
    public static AnimChannel getAnimChannel(){return channel;}
    //BONE LIST
    public static void addBone(String bone){boneList.add(bone);}
    public static ArrayList<String> getBoneList(){return boneList;}
    //DECK
    public static void setDeck(MasterDeck decks){deck=decks;}
    public static MasterDeck getDeck(){return deck;}
    public static void AdvancedAddCard(String previousName,CardSetter card){AddCard(card);if(!previousName.equals(card.getName()))deck.remove(previousName);}
    private static void AddCard(CardSetter card){deck.put(card.getName(), card);}
    //ARCHIVE
    public static void addArchive(String ach){archive.add(ach);}
    public static ArrayList<String> getArchiveList(){return archive;}
}