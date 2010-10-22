/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vza.director.model;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.bullet.nodes.PhysicsCharacterNode;
import com.jme3.scene.Spatial;
import com.vza.director.Director;
import com.vza.director.model.controller.AnimationInputController;
import com.vza.director.model.controller.GaugeController;
import com.vza.director.model.controller.TargetController;
import java.io.File;
import java.io.IOException;

import java.util.HashMap;

/**
 * This class provides a interface for all of the functions needed for a character in the game world to function properly
 * @author Kyle Williams
 */
public final class CharacterNode{
    public String name,bio,activeStyle;
    //This is a list of all styles this doubles as a list of styles to search for inside the directory
    private HashMap<String,Style> styleMap;
    private PhysicsCharacterNode character;
    private Spatial model;
    private AnimControl control;
    private AnimChannel channel;
    private final AnimationInputController inputController = new AnimationInputController();
    private final GaugeController gaugeController = new GaugeController();
    private final TargetController targetController = new TargetController();

    /**
     * This constructor finds the character in a specified location
     * thus creating it in the game world as well as adding all necessary controllers to allow it to function correctly
     * @param name the name of the character
     * @param bio the character's information
     * @throws IOException
     */
    public CharacterNode(String name,String balanceType, String bio) throws IOException{
        this.name=name;
        this.bio=bio;
        this.styleMap=new HashMap<String,Style>();
        this.activeStyle=null;
        
        String directory = "Models/"+name+"/";
        if(new File("assets/"+directory+name+".scene").exists()){
            model=Director.getApp().getAssetManager().loadModel(directory+name+".scene");
        }else if(new File("assets/"+directory+name+".j3o").exists()){
            model=Director.getApp().getAssetManager().loadModel(directory+name+".j3o");
        }else if(new File("assets/"+directory+name+".mesh.xml").exists()){
            model=Director.getApp().getAssetManager().loadModel(directory+name+".mesh.xml");
            com.vza.app.VezlaApplication.logger.warning("The Model "+name+" is using .mesh.xml format");
        }else throw new IOException(name+"-mesh not found in directory. Thier is neither a .j3o file or an .mesh.xml file");

        //This final section sets up all of the controllers for the cahracter to move and operate with the world
        control = model.getControl(AnimControl.class);
        channel = control.createChannel();
        model.addControl(gaugeController);
        model.addControl(inputController);
        model.addControl(targetController);

         // We set up collision detection for the player by creating
    // a capsule collision shape and a physics character node.
    // The physics character node offers extra settings for
    // size, stepheight, jumping, falling, and gravity.
    // We also put the player in its starting position.
    model.center();
    com.jme3.bounding.BoundingBox bb = (com.jme3.bounding.BoundingBox)model.getWorldBound();
    float radius = bb.getXExtent()>bb.getZExtent()? bb.getXExtent():bb.getZExtent();
    float height = bb.getYExtent();
    character = new PhysicsCharacterNode(model
            ,new com.jme3.bullet.collision.shapes.CapsuleCollisionShape(radius, height, 1), .05f);
    character.setJumpSpeed(20);
    character.setFallSpeed(30);
    character.setGravity(30);
    character.attachDebugShape(Director.getApp().getAssetManager());
    }
    /**Forces the character to look Left*/
   public void lookLeft(){targetController.lookLeft();}
   /**Forces the character to look Right*/
   public void lookRight(){targetController.lookRight();}
   /**
    * Forces the character to look at another character or object
    * @param target the object or character to look at
    */
   public void lookAt(Spatial target){targetController.lookAt(target);}
    /**@return returns the character's info*/
    public String getBio(){return bio;}
    /**@return returns the character's name*/
    public String getName(){return name;}
    /**@return returns the character's spatial*/
    public Spatial getModel(){return model;}
    /**@return returns the character's PhysicsCharacterNode*/
    public PhysicsCharacterNode getPhysicsModel(){return character;}
    /**@return returns the character's current stance*/
    public String getActiveStyle(){return activeStyle;}
    public AnimChannel getAnimChannel(){return channel;}
    public AnimControl getAnimController(){return control;}
    /**@see com.vza.director.model.controller.GaugeController  */
    public GaugeController getLifeController(){return gaugeController;}
    /**@see com.vza.director.model.controller.InputControllerInterface*/
    public AnimationInputController getInputController(){return inputController;}

    /**
     * The changes the active Style and returns it as well
     * Calls must be in this order
     * @param styleName
     * @return
     */
    public void useStyle(String styleName){
        activeStyle=styleName;
        getModel().getControl(AnimationInputController.class).setActiveStyle(styleMap.get(styleName));
    }
    /**
     * gets the active Style
     * Please leave all calls to change of animation to the inputController
     * @return
     */
    public synchronized Style getStyle(){return styleMap.get(activeStyle);}
    /**
     * Returns a HashMap of the characters Styles
     * @return
     */
    public HashMap<String,Style> getStyleMap(){return styleMap;}
    /**
     * Adds a Style to the character's Style Map
     * This should not be called under normal conditions
     * @param style
     */
    public void addStyle(Style style){
        style.setModel(model);
        styleMap.put(style.getName(),style);
        if(activeStyle==null) useStyle(style.getName());
    }
}
