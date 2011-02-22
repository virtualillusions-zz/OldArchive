/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.input;

import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import java.util.ArrayList;

/**
 *
 * @author Kyle Williams
 */
public class ButtonSequenceTest extends com.jme3.app.SimpleApplication{

    public static void main(String[] args){
        ButtonSequenceTest app = new ButtonSequenceTest();
        app.start();
    }

        protected ButtonSequence ButtonSequence;        
        protected ActionListener ac;
    ArrayList<String> keeper = new ArrayList<String>();
    @Override
    public void simpleUpdate(float tpf){
        ButtonSequence.update(tpf);
        SequenceText.setText("Button-Sequence:"+keeper);
        //System.out.println("Button Sequence:"+ButtonSequence);

    }

    protected BitmapText SequenceText;
    @Override
    public void simpleInitApp() {
        loadSequence();
        ButtonSequence = new ButtonSequence(){
            @Override
            public void perform(){
                 if(!ButtonSequence.isEmpty()&&!keeper.equals(ButtonSequence)){
                keeper.clear();
                keeper.addAll(ButtonSequence);
                }
            }
        };
        ac=ButtonSequence.buttonListener();
        loadButtonMappings();
    }

    public void loadSequence(){
        SequenceText = new BitmapText(assetManager.loadFont("Interface/Fonts/Default.fnt"), false);
        SequenceText.setLocalTranslation(getCamera().getWidth()/4-SequenceText.getLineWidth(), getCamera().getHeight()/2+SequenceText.getLineHeight(), 0);
        SequenceText.setText("Button Sequence:");
        guiNode.attachChild(SequenceText);
    }

    public void loadButtonMappings(){
        getInputManager().addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
        getInputManager().addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
        getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        getInputManager().addMapping("Attack1", new KeyTrigger(KeyInput.KEY_W));
        getInputManager().addMapping("Attack2", new KeyTrigger(KeyInput.KEY_S));
        getInputManager().addMapping("Attack3", new KeyTrigger(KeyInput.KEY_A));
        getInputManager().addMapping("Attack4", new KeyTrigger(KeyInput.KEY_D));
        getInputManager().addListener(ac, "Up","Down","Left","Right","Attack1","Attack2","Attack3","Attack4");
    }
}
