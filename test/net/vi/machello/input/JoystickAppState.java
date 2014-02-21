/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vi.machello.input;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.JoyInput;
import com.jme3.input.Joystick;

/**
 *
 * @author Kyle D. Williams
 */
public class JoystickAppState extends AbstractAppState {

    private InputManager inputManager;
    private JoyInput joystick;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        inputManager = app.getInputManager();
        joystick = app.getContext().getJoyInput();
        joystick.initialize();
        joystick.setInputListener(inputManager);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        if (joystick != null) {
            joystick.destroy();
        }
    }

    /**
     * Set the deadzone for joystick axes.
     *
     * <p>{@link com#jme3#input#controls#ActionListener#onAction(java.lang.String, boolean, float)
     * }
     * events will only be raised if the joystick axis value is greater than the
     * <code>deadZone</code>.
     *
     * @param deadZone the deadzone for joystick axes.
     */
    public void setAxisDeadZone(float deadZone) {
        inputManager.setAxisDeadZone(deadZone);
    }

    /**
     * Returns the deadzone for joystick axes.
     *
     * @return the deadzone for joystick axes.
     */
    public float getAxisDeadZone() {
        return inputManager.getAxisDeadZone();
    }

    /**
     * Returns an array of all joysticks installed on the system.
     *
     * @return an array of all joysticks installed on the system.
     */
    public Joystick[] getJoysticks() {
        return joystick.loadJoysticks(inputManager);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        joystick.update();
    }
}
