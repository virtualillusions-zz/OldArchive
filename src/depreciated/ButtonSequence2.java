/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package depreciated;

/**
 * @author Kyle Williams
 * The Button Sequencer class organizes button combinations as well as button sequences.
 *
 * THIS CLASS SHOULD BE UPDATED
 *
 * REMEBER TO SORT QUEUE BEFORE ADDING TO BUTTONSEQUENCE
 */
public abstract class ButtonSequence2 extends java.util.ArrayList<String>{
    public enum BUTTONS{Attack1(1),Attack2(1),Attack3(1),Attack4(1),Up(0),Down(0),Left(0),Right(0);
                        private int priority;
                        private BUTTONS(int prty){priority=prty;}
                        public int getPriority(){return priority;}
    }

    protected float time = 0.0f;          //Used to keep track of time since class has been initialized
    protected float recordTime = 0.0f;   //Keeps track of the first recorded time
    protected final float TIME_LIMIT = 0.3f;   //Max Time in Between Button Presses Allowed
    protected final float DUAL_LIMIT = 0.05f;  //Time Between Button Presses That allows Dual Buttons
    protected float bttnInterval = 0.0f; //keeps track of how long a button has been pressed
    protected String buttonQueued = "";
    protected int buttonsHeld = 0;
    protected final String conjunction="+";
    public boolean constraint=false;   //This boolean is to prevent premature update of main Functions
    /*
     * adds the button to a List of currently held buttons
     */
    public void addButton(String temp){
        buttonQueued=temp;
        buttonsHeld+=1;

        //LOOK INTO USING RECORD TIME!! FURTHER RESEARCH REQUIRED
        if(!isEmpty()&&time<=bttnInterval
                &&!get(size()-1).equals(temp)){
            int r = size()-1;
            //This fixes a problem with <,<+^,^
            if(!get(r).contains(conjunction)&&time<bttnInterval)
                add(get(r)+conjunction+temp);
            else
                set(r,get(r)+conjunction+temp);
        }else{
            add(temp);
        }

        bttnInterval = time+DUAL_LIMIT;    //updates the time interval :: used to check how long this button has been held before the next button is pressed
    }

    public void releaseButton(String temp){
        buttonsHeld-=1;
        if(buttonQueued.equals(temp))buttonQueued="";
   }


     /**
      * Updates all timers
      * @param tpf
      */
    public void update(float tpf){
        if(constraint){
            time+=tpf;
            if(!isEmpty()){
                if(time>bttnInterval){
                    if(!get(size()-1).equals(buttonQueued)&&!buttonQueued.equals("")&&buttonsHeld==1)
                        add(buttonQueued);
                    buttonQueued="";
                }

                if(time>recordTime){
                    System.out.println("COMPLETED"+this);
                    //perform(this);
                    clear();
                    buttonQueued="";
                    recordTime=0;
                    bttnInterval=0;
                    time=0;
                    constraint=false;
                }
            }
        }
        
    }

    /*
     * resets the RecordTime of the current button sequence
     */
    public void resetRecordTime(){
        if(constraint!=true)constraint=true; //This constraint is set True here so anytime a button is pressed it is set to Two
        //This section resets the recordTime it is to be kept outside of the if clauses
        recordTime=time+TIME_LIMIT;
    }

    /**
     * An abstract method that should be used to perform what ever actions necesary
     */
    public abstract void perform(java.util.ArrayList<String> buttonSequence);

    /**
     * A preconfigured listener created for aesthetics
     * @return ActionListener
     */
    public com.jme3.input.controls.ActionListener buttonListener(){
        return new com.jme3.input.controls.ActionListener(){
            public void onAction(String name, boolean isPressed, float tpf) {                
                resetRecordTime();
                String button = name.split("-")[1]; //This is done to take only the important part of the input say testPlayer-Attack4->Attack4
                BUTTONS temp = BUTTONS.valueOf(button); //Creates a temporary value to holt the current button pressed
                //////////////////////////////////TODO: FIGURE OUT BUTTON TIMING THEIR SEEMS TO BE A SLIGHT ISSUE WITH RAPID INPUT REGISTRATION
                 if (isPressed){
                        addButton(button);
                  }else{
                        releaseButton(button);
                }              
            }
       };
    }
}
