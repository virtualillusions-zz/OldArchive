/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vza.director.model.controller;

import com.jme3.bullet.nodes.PhysicsCharacterNode;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.vza.director.Director;
import java.io.IOException;

/**
 * This class is the main control class for all movement of the fighter
 * ANIMATIONINPUTCONTROLLER MUST BE INITIATED
 * @author Kyle Williams
 */
public final class MoveController implements com.jme3.scene.control.Control{
    private boolean enabled=true;
    private PhysicsCharacterNode character=null;
    private boolean left = false, right = false, up = false, down = false;      //A list of possible directions
    private Vector3f walkDirection = new Vector3f();    //This Vector holds the physics property that changes the characters position
    private Camera cam = Director.getApp().getCamera();
    private String player=null;
    private com.vza.director.model.Style activeStyle = null;
    private com.vza.director.model.controller.TargetController targetControl;
    public MoveController() {
    }

    @Override
    public void update(float tpf) {
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.02f);
        walkDirection.set(0, 0, 0);
        if (left&&!right)walkDirection.addLocal(camLeft);
        if (right&&!left)walkDirection.addLocal(camLeft.negate());
        character.setWalkDirection(walkDirection);

        
        character.getLocalTranslation().setZ(0);


        if(character.onGround()&&(left||right))movingAnimation();
        
        if(up){
            character.jump();
        }
    }

    public ActionListener movement = new ActionListener(){
        
        public void onAction(String name, boolean isPressed, float tpf) {
 
            //This sets the booleans true or false;
            if(name.equals(player+"-Up")){
                up=isPressed;
                
               // activeStyle.performMappedAction("Jump");
            }
            else if(name.equals(player+"-Down")){
                down=isPressed;
                //activeStyle.performMappedAction("Crouch");
            }
            else if(name.equals(player+"-Left")){
                left=isPressed;
            }
            else if(name.equals(player+"-Right")){
                right=isPressed;

            }
        }

    };

    public void movingAnimation(){
        String direction="Forward";

        if(character.onGround()&&up)
            direction="Forward+Jump";

         if(left&&!right){
            if(targetControl.isLookingAt()){
                if(!targetControl.isMovingLeft())
                    if(character.onGround()&&up){ direction="Backward+Jump";}
                    else {direction="Backward";}
            }else targetControl.lookLeft();
        }else if(right&&!left){
             if(targetControl.isLookingAt()){
               if(targetControl.isMovingLeft())
                    if(character.onGround()&&up){ direction="Backward+Jump";}
                    else {direction="Backward";}
             }else targetControl.lookRight();
        }else direction=null;

        //activeStyle.performMappedAction(direction);
    }

    /**
     * @param pR
     * @param aS
     * @return
     */
    public ActionListener getListener(String pR,com.vza.director.model.Style aS,com.vza.director.model.controller.TargetController tC){
        player=pR;
        activeStyle = aS;
        targetControl=tC;
        return movement;
    }


    @Override
    public void setSpatial(Spatial spatial) {
        character=(PhysicsCharacterNode) spatial;
        character.setGravity(character.getGravity()*2);
        character.setJumpSpeed(8f);
        character.setFallSpeed(character.getJumpSpeed()*4);
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setEnabled(boolean enabled) {this.enabled=enabled;}

    @Override
    public boolean isEnabled() {return enabled;}

    @Override
    public void render(RenderManager rm, ViewPort vp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void addSequence(ButtonSequence buttonSequence) {
        System.out.println("Milk Milk Lemonade Around The Corner Fudge Is Made");
        //throw new UnsupportedOperationException("Not yet implemented");
    }


}
