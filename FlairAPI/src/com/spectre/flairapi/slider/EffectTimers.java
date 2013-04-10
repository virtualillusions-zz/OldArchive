/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.flairapi.slider;

import com.jme3.animation.AnimChannel;
import com.spectre.deck.card.Animation;
import com.spectre.deck.card.Effect;
import com.spectre.director.SceneDirector;
import com.spectre.director.ToolsDirector;
import javax.swing.JOptionPane;

/**
 *
 * @author Kyle Williams
 */
public class EffectTimers {

    /**
     * This is used as a helper class to Create a Slider that is used to Set the
     * Start and End Time of an Effect
     *
     * @author Kyle Williams
     */
    public static void SetEffectTime(Animation animation, Effect effect) {
        if (animation.getAnimationName().equals("")) {
            JOptionPane.showMessageDialog(null, "INVALID ACTION\nAnimation Must First Be Set.", "Animation Not Set", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!SceneDirector.getInstance().isCurrentRequest()) {
            SceneDirector.getInstance().displayModel();
        }

        float animLength = SceneDirector.getInstance().getAnimControl().getAnim(animation.getAnimationName()).getLength();
        AnimChannel chnnl = SceneDirector.getInstance().getAnimChannel();

        chnnl.setAnim(animation.getAnimationName());
        JRangeSlider slider = ToolsDirector.setUpTimeRangeSlider("Set Start/End Time (Based On Animation Time only)", "Start Time", "End Time", animLength, effect.getStartTime(), effect.getEndTime());

        effect.setStartTime(animLength
                * slider.getLowerValue() / 100);
        effect.setEndTime(animLength
                * slider.getUpperValue() / 100);
    }

    /**
     * This is used as a helper class to Create a Slider that is used to Set the
     * Start Time of an Effect
     *
     * @author Kyle Williams
     */
    public static void SetEffectStartTime(Animation animation, Effect effect) {
        if (animation.getAnimationName().equals("")) {
            JOptionPane.showMessageDialog(null, "INVALID ACTION\nAnimation Must First Be Set.", "Animation Not Set", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!SceneDirector.getInstance().isCurrentRequest()) {
            SceneDirector.getInstance().displayModel();
        }

        JOptionPane optionPane = new JOptionPane();
        float animLength = SceneDirector.getInstance().getAnimControl().getAnim(animation.getAnimationName()).getLength();
        AnimChannel chnnl = SceneDirector.getInstance().getAnimChannel();
        chnnl.setAnim(animation.getAnimationName());
        ToolsDirector.setUpSlider(optionPane, "Effect Start Time", effect.getStartTime(), animLength, 10, 0, 100, false);
        effect.setStartTime(animLength * new Integer(optionPane.getInputValue().toString()) / 100);
    }
}
