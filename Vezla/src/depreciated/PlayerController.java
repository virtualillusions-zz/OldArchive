/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package depreciated;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.vza.director.model.controller.AnimationInputController;

/**
 * This Controller will allow set up the character to be controlled entirely on player input
 * @author Kyle Williams
 */
public final class PlayerController implements InputController{
    private String player;
    /**
     * Sets up the player Controller
     * @param player the player to control this character
     */
    public PlayerController(String player){
        this.player = player;
    }
    @Override
    public void setUP(final AnimationInputController ici) {
       /**
        * Mappings not needed as it is set way beforehand
        */
        inputManager.addListener(new ActionListener(){
            
           @Override
           public void onAction(String name, boolean value, float tpf){
               System.out.println(tpf);
               if(value)
                   if(name.equals(player+"-Up"))
                       System.out.println("Up");
                   else if(name.equals(player+"-Down"))
                       System.out.println("Down");
                   /* if(name.equals(player+"-Jump"))
                        ici.performAction("Jump");
                    else if(name.equals(player+"-Crouch"))
                        ici.performAction("Crouch");
                    else if(name.equals(player+"-Attack1"))
                         ici.performAction("Attack1");
                    else if(name.equals(player+"-Attack2"))
                         ici.performAction("Attack2");
                    else if(name.equals(player+"-Attack3"))
                         ici.performAction("Attack3");
                    else if(name.equals(player+"-Attack4"))
                         ici.performAction("Attack4");
                    else if(name.equals(player+"-comboMod"))
                        System.out.println("comboModifier");
                    else if(name.equals(player+"-specialMod"))
                        System.out.println("specialModifier");
                    */

           }
         },player+"-Up",player+"-Down",player+"-Left",player+"-Right",
            player+"-Attack1",player+"-Attack2",player+"-Attack3",player+"-Attack4",
                player+"-comboMod",player+"-specialMod");

         inputManager.addListener(new AnalogListener(){
         @Override
           public void onAnalog(String name, float value, float tpf){
                //if(name.equals(player+"-Left"))
                //    ici.left();
                //else if(name.equals(player+"-Right"))
                //    ici.right();
            }
         } ,player+"-Left",player+"-Right");
    }
}