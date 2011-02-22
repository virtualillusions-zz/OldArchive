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
import java.util.EnumSet;

/**
 *
 * @author Kyle Williams
 */
public class ButtonSequenceTest1 extends com.jme3.app.SimpleApplication{

    public static void main(String[] args){
        ButtonSequenceTest1 app = new ButtonSequenceTest1();
        app.start();
    }

        protected ArrayList<String> ButtonSequence;
        protected float time = 0.0f;
        protected float recordTime = time;   //Keeps track of the first recorded time
        protected float TIME_LIMIT = 0.2f;   //200 miliseconds

        public enum BUTTONS{Attack1,Attack2,Attack3,Attack4,Up,Down,Left,Right,BLANK;}
    ActionListener ac = new ActionListener(){
        protected ArrayList<String> buttonsPressed = new ArrayList<String>();
        protected float bttnInterval = 0.0f; //keeps track of how long a button has been pressed
        public void onAction(String name, boolean isPressed, float tpf) {
             //This section resets the recordTime it is to be kept outside of the if clauses
             if(ButtonSequence.isEmpty()){recordTime=time+TIME_LIMIT;}
                 //////////////////////////////////TODO: FIGURE OUT BUTTON TIMING THEIR SEEMS TO BE A SLIGHT ISSUE WITH RAPID INPUT REGISTRATION

             if (isPressed){
                    buttonsPressed.add(name);                     //adds the button to a List of currently held buttons
                    if(buttonsPressed.size()>1                    //This section is primarily for directional buttons
                            &&(time-bttnInterval)>=TIME_LIMIT/2){ //addition clauses should be made to allow it to only apply to directional buttons
                        ButtonSequence.add(buttonsPressed.get(0));
                    }
                    bttnInterval = time;
                }else{
                     if(!buttonsPressed.isEmpty()){                                //Should only be ran if the buttonsPressed list is not empty
                        if(buttonsPressed.size()==1){// || (time-bttnInterval)<=TIME_LIMIT/2){
                            ButtonSequence.addAll(buttonsPressed);
                            buttonsPressed.clear();
                        } else {                                                  //If the buttons pressed size is larger than 1 then concatenate each
                            String bttns=buttonsPressed.get(0);
                            ///IDEA:::::::::: SEPERATE for statement into two statements handle directions directly and attacks directly
                            ///Handle it specially if last value is not an attack move else break them up //////////////
                            for(int i=1; i<buttonsPressed.size();i++){            //using the "-" symbol as a divider
                                bttns=bttns+"-"+buttonsPressed.get(i);
                            }
                            String k = buttonsPressed.get(buttonsPressed.size()-1);   //add the previous button to a temp value --> nxt cmmnt
                            ButtonSequence.add(bttns);
                            buttonsPressed.clear();
                            if((recordTime>=time-TIME_LIMIT)){buttonsPressed.add(k);        }   // in case a condition is met to add it to the next list of held bttns
                           /* else  if(ButtonSequence.size()>1
                                    &&ButtonSequence.get(ButtonSequence.size()-2).contains(k)
                                        &&ButtonSequence.get(ButtonSequence.size()-2).contains("-")){
                                ButtonSequence.add(ButtonSequence.size()-2, k);
                            }*/
                        }
                     }
                }
        }
    };
ArrayList<String> keeper = new ArrayList<String>();
    @Override
    public void simpleUpdate(float tpf){
        if(recordTime<time-TIME_LIMIT){
            if(!ButtonSequence.isEmpty()&&!keeper.equals(ButtonSequence)){
                keeper.clear();
                keeper.addAll(ButtonSequence);
            }
            ButtonSequence.clear();
            recordTime=0;
        }
        SequenceText.setText("Button-Sequence:"+keeper);
        //System.out.println("Button Sequence:"+ButtonSequence);
        time+=tpf;

    }

    protected BitmapText SequenceText;
    @Override
    public void simpleInitApp() {
        loadSequence();
        ButtonSequence = new ArrayList<String>();
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
