/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.renderer.visual.control;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;

/**
 * <code>AnimEvent</code> allows users to encapsulate code events regarding
 * AnimControl. <p>
 *
 * @see SpatialControl#addAnimCycleDoneEvent(AnimEvent)
 * @see SpatialControl#addAnimChangeEvent(AnimEvent)
 * @author Kyle D. Williams
 */
public interface AnimEvent {

    /**
     * <b>if</b>
     * <code>AnimCycleDoneEvent</code> Invoked when an animation "cycle" is
     * done. For non-looping animations, this event is invoked when the
     * animation is finished playing. For looping animations, this even is
     * invoked each time the animation is restarted. <p>
     *
     * <b>if</b>
     * <code>AnimChangeEvent</code>Invoked when a animation is set to play by
     * the user on the given channel. <p>
     *
     * @param control The control to which the listener is assigned.
     * @param channel The channel being altered
     * @param animName The new animation that is done if AnimCycleDoneEvent or
     * The new animation name set if AnimChangeEvent
     * @return <code>true</code> if this animation event should be repeated
     * every time, depending on which list/s this event was added to, the given
     * <code>AnimCycleDone</code> or <code>AnimChange</code> is invoked
     */
    public abstract boolean event(AnimControl control, AnimChannel channel, String animName);
}