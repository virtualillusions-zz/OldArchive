/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.essence.components;

import com.simsilica.es.EntityComponent;

/**
 * Character state healthy tired or injured used to prevent reseting of idle
 *
 * @author Kyle Williams
 */
public class CharacterStatePiece implements EntityComponent {

    private CharacterState type;

    public CharacterStatePiece() {
        this(CharacterState.Healthy);
    }

    public CharacterStatePiece(CharacterState type) {
        this.type = type;
    }

    public CharacterState getType() {
        return type;
    }

    public enum CharacterState {

        Healthy, Tired, Injured;
    }
}
