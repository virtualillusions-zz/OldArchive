/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package depreciated;

import com.vza.director.model.controller.AnimationInputController;

/**
 *  Advises how each model should be controlled
 * @author Kyle Williams
 */
public interface InputController {
    com.jme3.input.InputManager inputManager = com.vza.director.Director.getApp().getInputManager();
    /**
     * Performs all operations to set up the controller for use
     * @param ici
     */
    public void setUP(AnimationInputController ici);
}
