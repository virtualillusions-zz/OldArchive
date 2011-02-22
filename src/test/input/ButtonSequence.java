/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.input;

/**
 * @author Kyle Williams
 * The Button Sequencer class organizes button combinations as well as button sequences.
 *
 * THIS CLASS SHOULD BE UPDATED
 */
public abstract class ButtonSequence extends java.util.ArrayList<String>{
    public enum BUTTONS{Attack1,Attack2,Attack3,Attack4,Up,Down,Left,Right;}

    protected float time = 0.0f;          //Used to keep track of time since class has been initialized
    protected float recordTime = time;   //Keeps track of the first recorded time
    protected final float TIME_LIMIT = 0.2f;   //200 miliseconds
    protected float bttnInterval = 0.0f; //keeps track of how long a button has been pressed
    protected int buttonIntegrety =0; //The sum of the buttons placement
    protected final java.util.Queue<BUTTONS> buttonsPressed = new java.util.LinkedList<BUTTONS>();
    protected final String conjuction="-";
    /*
     * adds the button to a List of currently held buttons
     */
    public void addButton(String name){
        BUTTONS temp = BUTTONS.valueOf(name);               //Creates a temporary value to holt the current button pressed
        buttonIntegrety+=temp.ordinal();            //get the number of the buttons position and add to the current sequences Integrity
        //This section is primarily for directional buttons 
        //if prev button held for a considerable amount of time add it to the list automatically
        //Since using queue have no control over which position to look at so make this check before adding another button
        if(buttonsPressed.size()>0
                &&(time-bttnInterval)<=TIME_LIMIT/4
                    && buttonIntegrety>8){
            add(buttonsPressed.poll()+conjuction+temp);
            buttonIntegrety=0;
        }else{
            buttonsPressed.add(temp);              //Adds the button to a list of currently pressed buttons
            bttnInterval = time;                    //updates the time interval :: used to check how long this button has been held before the next button is pressed
        }
    }

    /**
     * Performs certain actions when the buttons are released
     */
    public void releaseButton(){
        if(!buttonsPressed.isEmpty()){        //Should only be ran if the buttonsPressed list is not empty

            if(buttonsPressed.size()==1){
                if(buttonIntegrety>=4&&size()>1
                        &&!get(size()-1).contains(conjuction)){    //Greater and equal to only 4 because based on only one button instead of multiple
                    add(get(size()-1)+conjuction+buttonsPressed.peek().toString());
                    add(buttonsPressed.poll().toString());
                }else
                    add(buttonsPressed.poll().toString());
            } else{
                multiButtonPress();            //If the buttons pressed size is larger than 1 then concatenate each
            }
            buttonIntegrety=0;                //Resets ButtonIntegrity
         }
    }


    /**
     * Interprets the button pattern to be passed to this List when a sequence of buttons have been pressed concurently
     */
     public void multiButtonPress(){
            if(buttonIntegrety<=6){  //This section is primarily for AttackButtons they should all be queued and added together
                String bttn=buttonsPressed.poll().toString();
                while(!buttonsPressed.isEmpty()){
                    bttn=bttn+conjuction+buttonsPressed.poll().toString();
                }
                add(bttn);
            }else{           //This section is primarily for DirectionalButtons **Note their still may be some issues with the overal impl of the class
                while(!buttonsPressed.isEmpty()){
                    if(buttonsPressed.size()==1){
                        add(buttonsPressed.poll().toString());
                        break;
                    } else {
                        String[] next = {buttonsPressed.poll().toString(),buttonsPressed.peek().toString()};
                        add(next[0]);
                        add(next[0]+conjuction+next[1]);
                    }
                }
          }             
        }

     /**
      * Updates all timers
      * @param tpf
      */
    public void update(float tpf){
        if(recordTime<time-TIME_LIMIT){
            perform();
            clear();
            recordTime=0;
            bttnInterval=0;
        }
        time+=tpf;
    }

    /*
     * resets the RecordTime of the current button sequence
     */
    public void resetRecordTime(){
         //This section resets the recordTime it is to be kept outside of the if clauses
        if(isEmpty()){recordTime=time+TIME_LIMIT;}

    }

    /**
     * An abstract method that will be placed within the update sequence of ButtonSequence
     */
    public abstract void perform();

    /**
     * A preconfigured listener created for aesthetics
     * @return ActionListener
     */
    public com.jme3.input.controls.ActionListener buttonListener(){
        return new com.jme3.input.controls.ActionListener(){
            public void onAction(String name, boolean isPressed, float tpf) {
                resetRecordTime();
                //////////////////////////////////TODO: FIGURE OUT BUTTON TIMING THEIR SEEMS TO BE A SLIGHT ISSUE WITH RAPID INPUT REGISTRATION
                 if (isPressed){
                        addButton(name);
                    }else{
                        releaseButton();
                    }
            }
       };
    }
}
