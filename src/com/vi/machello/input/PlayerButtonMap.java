/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.input;

import com.jme3.input.KeyInput;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.vi.machello.scene.player.components.PlayerPiece;

/**
 * Defines a set of standard input function IDs and their default control
 * mappings for basic player input
 *
 * @author Kyle D. Williams
 */
public final class PlayerButtonMap {

    private static InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();
    /**
     * The group to which these functions are assigned for easy grouping and
     * also for easy activation and deactivation of the entire group.
     */
    public final String GROUP;
    /**
     * Causes the Character to Attack 1 or Attack 2
     */
    public final FunctionId ATTACK;
    /**
     * Causes the Character to Jump
     */
    public final FunctionId JUMP;
    /**
     * Causes the Character to Guard if held
     *
     * <p>
     *
     * Causes the Character to Parry if tapped or grapple if held at the time of
     * opponent attack
     */
    public final FunctionId DEFENSE;
    /**
     * Performs Character Special
     */
    public final FunctionId SPECIAL;
    /**
     * Moves the Character Up or Down
     */
    public final FunctionId MOVE_V;
    /**
     * Moves the Character Left or Right
     */
    public final FunctionId MOVE_H;
    /**
     * Forces the character to face Left or Right
     */
    public final FunctionId FOCUS;

    public PlayerButtonMap(PlayerPiece player) {
        GROUP = player.getName() + "'s Controls";
        ATTACK = new FunctionId(GROUP, "Attack");
        JUMP = new FunctionId(GROUP, "Jump");
        DEFENSE = new FunctionId(GROUP, "Grapple");
        SPECIAL = new FunctionId(GROUP, "Special");
        MOVE_V = new FunctionId(GROUP, "Move_Vertical");
        MOVE_H = new FunctionId(GROUP, "Move_Horizontal");
        FOCUS = new FunctionId(GROUP, "Focus");
    }

    /**
     * Enable player input
     */
    public void activateGroup() {
        inputMapper.activateGroup(GROUP);
    }

    /**
     * Disable All input for player
     *
     * @param inputMapper
     */
    public void deactivateGroup() {
        inputMapper.deactivateGroup(GROUP);
    }

    /**
     * Initializes default keyboard input mappings for basic player control
     */
    public void initializeDefaultMappings() {
        // Default key mappings
        inputMapper.map(ATTACK, InputState.POSITIVE, KeyInput.KEY_I);//Attack 2
        inputMapper.map(ATTACK, InputState.NEGATIVE, KeyInput.KEY_J);//Attack 1
        inputMapper.map(JUMP, KeyInput.KEY_K);
        inputMapper.map(DEFENSE, KeyInput.KEY_L);
        inputMapper.map(SPECIAL, KeyInput.KEY_I, KeyInput.KEY_J);
        inputMapper.map(FOCUS, InputState.POSITIVE, KeyInput.KEY_R);//Focus Right
        inputMapper.map(FOCUS, InputState.NEGATIVE, KeyInput.KEY_Q);//Focus Left
        inputMapper.map(FOCUS, InputState.OFF, KeyInput.KEY_R, KeyInput.KEY_Q);//Turn off Focus
        inputMapper.map(MOVE_V, InputState.POSITIVE, KeyInput.KEY_W);//Move Up
        inputMapper.map(MOVE_V, InputState.NEGATIVE, KeyInput.KEY_S);//Move Down
        inputMapper.map(MOVE_H, InputState.NEGATIVE, KeyInput.KEY_A);//Move Left
        inputMapper.map(MOVE_H, InputState.POSITIVE, KeyInput.KEY_D);//Move Right
    }
}
