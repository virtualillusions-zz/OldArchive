/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vza.gameState.constant;

import com.jme3.app.state.AppState;
import com.jme3.scene.Spatial;
import java.util.ArrayList;

/**
 *
 * @author Kyle Williams
 */
public abstract interface GameState extends AppState{

    public ArrayList active = new ArrayList();

    /**
     * Called to provide an array of participating characters
     */
    public Spatial[] getCharacters();
    /**
     * Called to provide the level that will be generated 
     */
    public Spatial getScene();
}
