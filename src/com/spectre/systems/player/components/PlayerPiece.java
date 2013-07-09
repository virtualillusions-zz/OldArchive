/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.player.components;

import com.simsilica.es.PersistentComponent;

/**
 * A component used to distinguish players
 *
 * @author Kyle
 */
public class PlayerPiece implements PersistentComponent {

    private String playerName;

    public PlayerPiece(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Returns the players Name
     *
     * @return playerName
     */
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String toString() {
        return "PlayerPiece[PlayerName=" + playerName + "]";
    }
}
