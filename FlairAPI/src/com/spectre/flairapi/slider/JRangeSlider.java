package com.spectre.flairapi.slider;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * From Wayward Notes
 * @author Ernie Yu
 * Demo application panel to display a range slider.
 */
public class JRangeSlider extends JPanel implements ChangeListener {

    protected JLabel rangeSliderLabel1 = new JLabel();
    protected JLabel rangeSliderValue1 = new JLabel();
    protected JLabel rangeSliderLabel2 = new JLabel();
    protected JLabel rangeSliderValue2 = new JLabel();
    private RangeSlider rangeSlider = new RangeSlider();
    private int lowerValue = 0;
    private int upperValue = 0;

    public JRangeSlider(String lowerTitle, String upperTitle, int min, int max, int lowerValue, int upperValue) {
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        setLayout(new GridBagLayout());

        this.lowerValue = lowerValue;
        this.upperValue = upperValue;

        rangeSliderLabel1.setText(lowerTitle);
        rangeSliderLabel2.setText(upperTitle);
        rangeSliderValue1.setHorizontalAlignment(JLabel.LEFT);
        rangeSliderValue2.setHorizontalAlignment(JLabel.LEFT);

        rangeSlider.setPreferredSize(new Dimension(240, rangeSlider.getPreferredSize().height));
        rangeSlider.setMinimum(min);
        rangeSlider.setMaximum(max);

        // Add listener to update display.
        rangeSlider.addChangeListener(this);

        add(rangeSliderLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 3), 0, 0));
        add(rangeSliderValue1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 0), 0, 0));
        add(rangeSliderLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 3), 0, 0));
        add(rangeSliderValue2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 0), 0, 0));
        add(rangeSlider, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        
          // Initialize values.
        rangeSlider.setValue(lowerValue);
        rangeSlider.setUpperValue(upperValue);

        // Initialize value display.
        rangeSliderValue1.setText(String.valueOf(rangeSlider.getValue()));
        rangeSliderValue2.setText(String.valueOf(rangeSlider.getUpperValue()));


    }

    @Override
    public void stateChanged(ChangeEvent e) {
        RangeSlider slider = (RangeSlider) e.getSource();
        rangeSliderValue1.setText(String.valueOf(slider.getValue()));
        rangeSliderValue2.setText(String.valueOf(slider.getUpperValue()));
    }

    public RangeSlider getRangeSlider() {
        return rangeSlider;
    }

    public RangeSliderUI getRangeSliderUI() {
        return rangeSlider.getRangeSliderUI();
    }

    public int getUpperValue() {
        return rangeSlider.getUpperValue();
    }

    public int getLowerValue() {
        return rangeSlider.getValue();
    }

}
