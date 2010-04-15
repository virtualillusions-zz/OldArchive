/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FighterExample.etc;

import FighterExample.*;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.bullet.nodes.PhysicsCharacterNode;
import com.jme3.scene.Node;
import com.jme3.scene.control.ControlType;
import java.util.Properties;

/**
 *
 * @author Kyle Williams
 */
public class FighterEntity{
    public String Stance;
    public AnimChannel channel;
    public Node mesh;
    public PhysicsCharacterNode model;
    public Properties props;
    /**
     * MUST Be Called In Order to setUp Fighter
     */
    public FighterEntity(String id, PhysicsCharacterNode model, Properties props){
        //Sets the Initial Stance
        Stance="";
        this.props=props;
        
        //Setsup The Fighter
        this.model=model;
        mesh=(Node) model.getChild(0);        

        channel = ((AnimControl)mesh.getControl(ControlType.BoneAnimation)).createChannel();
       // channel.setAnim(Stance+"idle");

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
    public String getStance(){return Stance;}

    public Node getMesh(){return mesh;}
    public PhysicsCharacterNode getModel(){return model;}

    /**
         * Get a property of this entity.
         * @param propertyName the property name to retrieve.
         * @return The entity's property linked to propertyName.
         */
        public Object getProperty(String propertyName) {
                return props.get(propertyName);
        }

        /**
         * Binds a property name of the entity with it's property object.
         * @param propertyName the property name.
         * @param property the propery to bind with the name.
         */
        public void setProperty(String propertyName, Object property) {
                props.put(propertyName, property);
        }
}
