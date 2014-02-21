/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vi.machello.input;

import com.jme3.app.SimpleApplication;
import com.jme3.input.Joystick;
import com.jme3.input.JoystickButton;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.AnalogFunctionListener;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;

/**
 *
 * @author Kyle D. Williams
 */
public class InputTest extends SimpleApplication {

    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean pressed, float tpf) {
            System.out.println(name + " = " + pressed);
        }
    };
    public AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            System.out.println(name + " = " + value);
        }
    };
    private StateFunctionListener actionFuncListener = new StateFunctionListener() {
        public void valueChanged(FunctionId func, InputState value, double tpf) {
            System.out.println(func.getId() + " = " + value);
        }
    };
    public AnalogFunctionListener analogFuncListener = new AnalogFunctionListener() {
        public void valueActive(FunctionId func, double value, double tpf) {
            System.out.println(func.getId() + " = " + value);
        }
    };

    public static void main(String[] args) {
        InputTest app = new InputTest();
        AppSettings settings = new AppSettings(true);
        settings.setUseJoysticks(true);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();

        // Test multiple inputs per mapping
        inputManager.addMapping("My Action", new KeyTrigger(KeyInput.KEY_A));
        // Test multiple listeners per mapping
        inputManager.addListener(actionListener, "My Action");
        inputManager.addListener(analogListener, "My Action");


        FunctionId action = new FunctionId("My Action Function");
        inputMapper.map(action, KeyInput.KEY_S);
        inputMapper.addAnalogListener(analogFuncListener, action);
        inputMapper.addStateListener(actionFuncListener, action);

        Joystick[] jsa = inputManager.getJoysticks();
        if (jsa.length > 0) {
            Joystick js = inputManager.getJoysticks()[0];
            //   js.getButton(JoystickButton.BUTTON_0).assignButton("My Action");
            //js.getButton(JoystickButton.BUTTON_1)
            JoystickButton jsb = js.getButton(JoystickButton.BUTTON_1);
            Button b = new Button(jsb.getName(), jsb.getLogicalId());

            inputMapper.map(action, b);
        }

    }
}