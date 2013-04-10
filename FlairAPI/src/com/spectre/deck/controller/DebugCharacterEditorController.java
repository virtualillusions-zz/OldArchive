/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.deck.controller;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.spectre.deck.card.Card;
import com.spectre.director.SceneDirector;

/**
 * A simple helper Controller To add a card to the CardManifestController
 * Repeatedly This allows new results can be seen dynamically NOTE: PLEASE BE
 * SURE TO ADD CARDMANIFESTCONTROLLER class to the spatial before adding this
 * controller.
 *
 *
 *
 *
 *
 * LOOK INTO EVEYTHING TO SLEEPY TO EXPLAIN
 *
 *
 *
 *
 * @author Kyle Williams
 */
public class DebugCharacterEditorController extends com.jme3.scene.control.AbstractControl {

    private Card card;
    private CardManifestController cmc;

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        this.cmc = spatial.getControl(CardManifestController.class);
    }

    /**
     * Resets This Controller
     */
    public void clear() {
        this.card = null;
    }

    /**
     * Card To Be added to Manifest Controller
     *
     * @param baseCard
     */
    public void setCard(Card baseCard) {
        SceneDirector.getInstance().displayModel();
        this.card = baseCard;
    }

    /**
     *
     * @param f
     */
    @Override
    protected void controlUpdate(float tpf) {
        if (card != null
                && this.spatial != null
                && this.spatial.getParent() != null
                && !cmc.contains(card)) {
            cmc.clearQueue();
            cmc.addToActiveQueue(card);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public Control cloneForSpatial(Spatial sptl) {
        return null;
    }
}