/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FighterExample;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.scene.Node;
import com.jme3.scene.control.ControlType;
import java.util.LinkedHashMap;

/**
 *
 * @author Kyle Williams
 */
public class FighterEntity{
   
    private LinkedHashMap<String,String[]> stanceList;
    private String Stance;
    private AnimChannel animControl;
    private Node model;
    private boolean isHuman;
    private float blendTime;
    /**
     * MUST Be Called In Order to setUp Fighter
     */
    public FighterEntity(String id, Node model, LinkedHashMap<String,String[]> stances){
        //initially set character to be AI controlled
       isHuman = false;
       this.stanceList=stances;
       //Sets the Initial Stance
       Stance=stanceList.keySet().iterator().next();        
       //Setsup The Fighter
       this.model=model;
       
       //Creates a simple animation control for the whole body so the whole body uses a single aniamtion at a time
       animControl = ((AnimControl)model.getControl(ControlType.BoneAnimation)).createChannel();
      model.setControl(new FightingController(model,animControl));
      blendTime=1.0f;
    }

    /**
     * Sets The Stance of the Character
     * @param Stance
     */
    public void setStance(String Stance){
        this.Stance = Stance;
    }

    /**
     * Get The Active Stance
     * @return Stance
     */
    public Node getModel(){return model;}
    public AnimChannel getAnim(){return animControl;}

    public String getStanceName(){return stanceList.get(Stance)[0];}
    public String getStanceArch(){return stanceList.get(Stance)[1];}
    public void idle(){ animControl.setAnim(stanceList.get(Stance)[2],blendTime);}
    public void walk(){animControl.setAnim(stanceList.get(Stance)[3],blendTime);}
    public void run(){animControl.setAnim(stanceList.get(Stance)[4],blendTime);}
    public void jump(){animControl.setAnim(stanceList.get(Stance)[5],blendTime);}
    public void crouch(){animControl.setAnim(stanceList.get(Stance)[6],blendTime);}
    public void attack1(){animControl.setAnim(stanceList.get(Stance)[7],blendTime);}
    public void attack2(){animControl.setAnim(stanceList.get(Stance)[8],blendTime);}
    public void attack3(){animControl.setAnim(stanceList.get(Stance)[9],blendTime);}
    public void attack4(){animControl.setAnim(stanceList.get(Stance)[10],blendTime);}
    public void penance(){animControl.setAnim(stanceList.get(Stance)[11],blendTime);}
    public void vengence(){animControl.setAnim(stanceList.get(Stance)[12],blendTime);}
    public void rage(){animControl.setAnim(stanceList.get(Stance)[13],blendTime);}
}
