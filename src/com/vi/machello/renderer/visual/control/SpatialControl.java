/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.renderer.visual.control;

import com.google.common.collect.Lists;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.Skeleton;
import com.jme3.bounding.BoundingBox;
import com.simsilica.es.EntityId;
import com.vi.machello.app.control.BaseControl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * <b>Name:</b> SpatialControl<br/>
 *
 * <b>Purpose:</b> Collect and store Spatial information for a given
 * EntityId<br/>
 *
 * <b>Description:</b> Provides basic spatial information for the a given
 * spatial along with utility methods to control basic spatial function such as
 * animation.
 *
 * @author Kyle D. Williams
 */
public class SpatialControl extends BaseControl implements AnimEventListener {

    private final EntityId id;
    private float radius;
    private float height;
    private float mass;
    private AnimControl control;
    private AnimChannel mainChannel;
    private ArrayList<AnimEvent> animDone;
    private ArrayList<AnimEvent> animChange;
    private ArrayList<String> boneList;

    public SpatialControl(EntityId id) {
        this.id = id;
        this.animDone = Lists.newArrayList();
        this.animChange = Lists.newArrayList();
        this.boneList = Lists.newArrayList();
    }

    @Override
    public void BaseControl() {
        //setUpBounds();
        BoundingBox bb = (BoundingBox) spatial.getWorldBound();
        //use smaller side extent over larger side to create a more narrow hitbox 
        //and because A-Pose and T-Pose difference greatly impacts value
        radius = bb.getXExtent() < bb.getZExtent() ? bb.getXExtent() : bb.getZExtent();
        radius /= 2;
        height = bb.getYExtent() * 2;
        mass = 1 * bb.getVolume();//random
        //setUpAnimControl();
        control = spatial.getControl(com.jme3.animation.AnimControl.class);
        if (control != null) {
            control.addListener(this);
            if (control.getNumChannels() < 1) {
                mainChannel = control.createChannel();
            } else {
                mainChannel = control.getChannel(0);
            }
            animDone.clear();
            animChange.clear();

            //setUpBoneList();
            boneList.clear();
            Skeleton skeleton = control.getSkeleton();
            if (boneList == null) {
                boneList = new ArrayList<String>();
            } else {
                boneList.clear();
            }
            for (int i = 0; i < skeleton.getBoneCount(); i++) {
                boneList.add(skeleton.getBone(i).getName());
            }
            Collections.sort(boneList);
        }
    }

    @Override
    public void cleanup() {
        if (control != null) {
            control.removeListener(this);
        }
    }

    public EntityId getId() {
        return id;
    }

    /**
     * @return The name of the model being represented by the spatial
     */
    public String getAssetName() {
        return this.getSpatial().getKey().getName();
    }

    public float getRadius() {
        return radius;
    }

    public float getHeight() {
        return height;
    }

    public float getMass() {
        return mass;
    }

    public ArrayList getBoneList() {
        return boneList;
    }

    public AnimControl getAnimControl() {
        return control;
    }

    public AnimChannel getAnimChannel() {
        return mainChannel;
    }

    /**
     * Enqueue an AnimEvent to be invoked when an animation "cycle" is done.
     */
    public boolean addAnimCycleDoneEvent(AnimEvent ae) {
        return animDone.add(ae);
    }

    /**
     * Deque an AnimEvent for the AnimCycleDone loop
     *
     * @param AnimEvent the AnimEvent to remove
     * @return true if this list contained the specified element
     */
    public boolean removeAnimCycleDoneEvent(AnimEvent ae) {
        return animDone.remove(ae);
    }

    /**
     * Enqueue an AnimEvent to be invoked when a animation is set to play by the
     * user on the given channel.
     */
    public boolean addAnimChangeEvent(AnimEvent ae) {
        return animChange.add(ae);
    }

    /**
     * Deque an AnimEvent for the AnimChange loop
     *
     * @param AnimEvent AnimEvent to remove
     * @return true if this list contained the specified element
     */
    public boolean removeAnimChangeEvent(AnimEvent ae) {
        return animChange.remove(ae);
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        for (Iterator<AnimEvent> it = animDone.iterator(); it.hasNext();) {
            AnimEvent ae = it.next();
            boolean isRepeatable = ae.event(control, channel, animName);
            if (!isRepeatable) {
                it.remove();
            }
        }
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        for (Iterator<AnimEvent> it = animDone.iterator(); it.hasNext();) {
            AnimEvent ae = it.next();
            boolean isRepeatable = ae.event(control, channel, animName);
            if (!isRepeatable) {
                it.remove();
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 71 * hash + Float.floatToIntBits(this.radius);
        hash = 71 * hash + Float.floatToIntBits(this.height);
        hash = 71 * hash + Float.floatToIntBits(this.mass);
        hash = 71 * hash + (this.control != null ? this.control.hashCode() : 0);
        hash = 71 * hash + (this.mainChannel != null ? this.mainChannel.hashCode() : 0);
        hash = 71 * hash + (this.animDone != null ? this.animDone.hashCode() : 0);
        hash = 71 * hash + (this.animChange != null ? this.animChange.hashCode() : 0);
        hash = 71 * hash + (this.boneList != null ? this.boneList.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SpatialControl other = (SpatialControl) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (Float.floatToIntBits(this.radius) != Float.floatToIntBits(other.radius)) {
            return false;
        }
        if (Float.floatToIntBits(this.height) != Float.floatToIntBits(other.height)) {
            return false;
        }
        if (Float.floatToIntBits(this.mass) != Float.floatToIntBits(other.mass)) {
            return false;
        }
        if (this.control != other.control && (this.control == null || !this.control.equals(other.control))) {
            return false;
        }
        if (this.mainChannel != other.mainChannel && (this.mainChannel == null || !this.mainChannel.equals(other.mainChannel))) {
            return false;
        }
        if (this.animDone != other.animDone && (this.animDone == null || !this.animDone.equals(other.animDone))) {
            return false;
        }
        if (this.animChange != other.animChange && (this.animChange == null || !this.animChange.equals(other.animChange))) {
            return false;
        }
        if (this.boneList != other.boneList && (this.boneList == null || !this.boneList.equals(other.boneList))) {
            return false;
        }
        return true;
    }
}
