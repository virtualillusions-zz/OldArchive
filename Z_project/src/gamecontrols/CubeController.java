package gamecontrols;
 
import com.jme.input.controls.GameControl;
import com.jme.input.controls.GameControlManager;
import com.jme.input.controls.binding.KeyboardBinding;
import com.jme.input.controls.binding.MouseButtonBinding;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import static gamecontrols.CubeController.CubeAction.*;
import static com.jme.input.KeyInput.*;
import static com.jme.input.controls.binding.MouseButtonBinding.*;
 
/**
Now we need some explanations. First, I'm using a bunch of enums for the different actions. This is not required for the GameControl system, it works with Strings. But enums have some nice properties compared to Strings: They are real classes, you can use them in switch statements and you can never spell them wrong, because the compiler will warn you. Using Strings as “tags” for something is always dangerous. In my little example enums might look superficial, but if you're dealing with twenty and more GameControls, you'll see why enums do a better job here. As always, static imports can help to ease the pain.
So, how does it work? First, there is a class GameControlManager, which can be used for creating, storing and removing the GameControls. That's why you don't need the GameControls themselve as member variables.
First we create the GameControls in a loop - one for each enum, using the same name. This alone does not much, because we haven't attached any input source to them. The most common input source are key strokes. There is a little helper method called bindKey, which does the job, using the KeyboardBinding class. The key codes are defined in the class KeyInput (there is a static import for it). Another useful input source are mouse buttons. Not surprisingly there is another class MouseButtonBinding for the job, but again I wrote a little helper method for this. There are several other predefined Binding classes: MouseAxisBinding, MouseOffsetBinding, JoystickAxisBinding, JoystickButtonBinding and ProcessorCoreTemparatureBinding - no, sorry, the last one comes in JME3. But you can write your own Bindings when you have to deal with fancy input devices.
As you can see, we can attach both key events and mouse clicks to the same GameControl. That is no problem, both work fine. So, how do we know when a GameControl got some input? Again, there is a little helper function called “value”, which saves us from looking up the GameControl ourselves. Now here comes something interesting: The value has the type float. This is a nice trick, because keys and mouse buttons may have only two states (pressed or not), but other input devices like joystick and mouse axis have a continous value space. So for a key or a mouse button, you can just look if value is greater than 0, then it's pressed. For mouse and joystick axis, you can use the “real” value and calculate from this.
Again, the update method is not very fancy. Note that we multiply with the time in order to get a smooth effect - else our rotation would be frame rate dependent.
So that's it - I told you it's not difficult.
Have fun! Landei
*/
public class CubeController extends Controller {
 
    enum CubeAction {LEFT, RIGHT, UP, DOWN, EXIT};
 
    private final static float SPEED = 2F;
 
    private final Node node;
    private final GameControlManager manager;
    private float vAngle = 0F;
    private float hAngle = 0F;
 
    public CubeController(Node node) {
        this.node = node;
        this.manager = new GameControlManager();
 
        //create all actions
        for (CubeAction action : CubeAction.values()) {
            manager.addControl(action.name());
        }
        //bind keys
        bindKey(EXIT, KEY_X);
        bindKey(UP, KEY_UP);
        bindKey(DOWN, KEY_DOWN);
        bindKey(LEFT, KEY_LEFT);
        bindKey(RIGHT, KEY_RIGHT);
 
        //bind mouse buttons
        bindMouseButton(LEFT, LEFT_BUTTON);
        bindMouseButton(RIGHT, RIGHT_BUTTON);
    }
 
 
    private void bindKey(CubeAction action, int... keys) {
        final GameControl control = manager.getControl(action.name());
        for (int key : keys) {
          control.addBinding(new KeyboardBinding(key));
        }
    }
 
    private void bindMouseButton(CubeAction action, int mouseButton) {
        final GameControl control = manager.getControl(action.name());
        control.addBinding(new MouseButtonBinding(mouseButton));
    }
 
    private float value(CubeAction action) {
        return manager.getControl(action.name()).getValue();
    }
 
    @Override
    public void update(float time) {
        if (value(EXIT) > 0) {
            System.exit(0); //OK, this is just a demo...
        }
        hAngle += SPEED * time * (value(LEFT) - value(RIGHT));
        vAngle += SPEED * time * (value(DOWN) - value(UP));
        node.getLocalRotation().fromAngles(vAngle, hAngle, 0f);
      }
 
}
