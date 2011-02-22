package test.input;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import java.util.HashSet;

public class TestCombo2 extends SimpleApplication implements ActionListener {

    private HashSet<String> pressedMappings = new HashSet<String>();

    private static final float TIME_LIMIT = 0.2f; // 500 ms

    private int move1state = 0;
    private float move1time = 0;
    private boolean move1finalstate = false;

    private float time = 0;

    public static void main(String[] args){
        TestCombo2 app = new TestCombo2();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Test multiple inputs per mapping
        inputManager.addMapping("Left",  new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Up",    new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Down",  new KeyTrigger(KeyInput.KEY_DOWN));

        // Test multiple listeners per mapping
        inputManager.addListener(this, "Left", "Right", "Up", "Down");
    }

    @Override
    public void simpleUpdate(float tpf){
        time += tpf;
        secondCounter = 0;

        // cancel the moves if the time limit passed
        if (!move1finalstate && move1time > 0 && move1time + TIME_LIMIT < time){
            move1time  = 0;
            move1state = 0;
            fpsText.setText("");
            move1finalstate = false;
        }
    }

    public boolean checkPressed(String ... mappings){
        for (String mapping : mappings){
            if (!pressedMappings.contains(mapping))
                return false;
        }
        return true;
    }

    public boolean checkUnpressed(String ... mappings){
        for (String mapping : mappings){
            if (pressedMappings.contains(mapping))
                return false;
        }
        return true;
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed){
            pressedMappings.add(name);
        }else{
            pressedMappings.remove(name);
        }

        switch (move1state){
            case 0: // Down
                if (checkPressed("Down")
                 && checkUnpressed("Right")){
                    move1state++;
                    move1time = time;
                    fpsText.setText( "[Down]" );
                }
                break;
            case 1: // Down + Right
                if (checkPressed("Down", "Right")){
                    move1state++;
                    move1time = time;
                    fpsText.setText("[Down], [Down+Right]");
                }
                break;
            case 2: // Right
                if (checkPressed("Right")
                 && checkUnpressed("Down")){
                    move1state++;
                    move1time = time;
                    fpsText.setText("[Down], [Down+Right], [Right]");
                    move1finalstate = true;
                }
                break;
            case 3: // Nothing
                if (checkUnpressed("Down", "Right")){
                    fpsText.setText("[Down], [Down+Right], [Right], {FIREBALL SPELL}");
                    move1state = 0;
                    move1time  = time;
                    move1finalstate = false;
                }
                break;
        }
    }

}
