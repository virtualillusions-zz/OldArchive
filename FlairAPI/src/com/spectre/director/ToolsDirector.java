/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.director;

import com.jme3.animation.AnimChannel;
import com.spectre.flairapi.EffectAccessory;
import com.spectre.flairapi.slider.JRangeSlider;
import com.spectre.flairapi.slider.RangeSlider;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Kyle Williams
 */
public class ToolsDirector {

    public static void setUpSlider(final JOptionPane optionPane, final String message, final float init, final float maxPossible, final int tick, final int min, final int max, final boolean isSpeedSet) {
        JSlider slider = new JSlider();
        slider.setMajorTickSpacing(tick);
        slider.setMaximum(max);
        slider.setMinimum(min);
        //0 is 0 5=1 slower<5 faster>5
        int value = (int) (init / maxPossible * max);
        slider.setValue(value);
        final JLabel val = new JLabel(init + "");
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        optionPane.setInputValue(new Integer(slider.getValue()));
        ChangeListener changeListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                JSlider theSlider = (JSlider) changeEvent.getSource();
                if (!theSlider.getValueIsAdjusting()) {
                    optionPane.setInputValue(new Integer(theSlider.getValue()));
                    val.setText((maxPossible * theSlider.getValue()) / max + "");
                }

                AnimChannel chnnl = SceneDirector.getInstance().getAnimChannel();
                if (isSpeedSet == true) {
                    float speed = theSlider.getValue() < 50 ? (2 * ((float) theSlider.getValue() / 100)) - 1 : 2 * ((float) theSlider.getValue() / 100);
                    SceneDirector.getInstance().getAnimChannel().setSpeed(speed);
                } else {
                    //loads the model so its easier to see and edit
                    chnnl.setSpeed(0f);
                    float maxTime = chnnl.getAnimMaxTime();
                    float startTime = maxTime * ((float) theSlider.getValue() / 100);
                    chnnl.setTime(startTime);
                }
            }
        };
        slider.addChangeListener(changeListener);
        optionPane.setMessage(new Object[]{"Select a value: ", val, slider});
        optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
        optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(null, message);
        dialog.setVisible(true);
    }

    public static JRangeSlider setUpTimeRangeSlider(final String headerTitle, final String lowerTitle, final String upperTitle, final float maxValue, final float lowerValue, final float upperValue) {


        int lower = (int) ((lowerValue / maxValue) * 100);
        int upper = (int) ((upperValue / maxValue) * 100);

        JRangeSlider slider = new JRangeSlider(lowerTitle, upperTitle, 0, 100, lower, upper) {

            @Override
            public void stateChanged(ChangeEvent e) {
                RangeSlider slider = (RangeSlider) e.getSource();
                int lV = slider.getValue();
                int uV = slider.getUpperValue();

                //The Slider Value Currently Moving
                int sliderValue = 0;
                if (slider.getRangeSliderUI().isLowerDragging()) {
                    sliderValue = slider.getValue();
                    if (lV >= uV) {
                        lV -= 0.001f;
                        slider.setValue(lV);
                    }
                } else if (slider.getRangeSliderUI().isUpperDragging()) {
                    sliderValue = slider.getUpperValue();
                    if (uV <= lV) {
                        uV += 0.001f;
                        slider.setUpperValue(uV);
                    }
                } else {
                    return;
                }

                rangeSliderValue1.setText(String.valueOf((lV * maxValue) / 100));
                rangeSliderValue2.setText(String.valueOf((uV * maxValue) / 100));

                //loads the model so its easier to see and edit
                AnimChannel chnnl = SceneDirector.getInstance().getAnimChannel();
                chnnl.setSpeed(0f);
                float maxTime = chnnl.getAnimMaxTime();
                float startTime = maxTime * ((float) sliderValue / 100);
                chnnl.setTime(startTime);
            }
        };

        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage(slider);
        optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
        optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(null, headerTitle);
        dialog.setVisible(true);

        return slider;
    }

    /**
     * Creates a File Explorer to locate a specific sound
     * @param path
     * @return 
     */
    public static String fileExplorer(String directory, String path) {
        java.io.File root = new java.io.File(directory + "/Deck/" + path).getAbsoluteFile();
        if (!root.isDirectory()) {
            JOptionPane.showMessageDialog(null, "It seems as though the directory " + path + " is missing!! \n Please make sure that the series "
                    + "file is within \n the same folder as the Sound or Effects directory \n Preferably assets/Deck/", "Invalid Directory Path", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        javax.swing.filechooser.FileSystemView fsv = new SingleRootFileSystemView(root);
        Boolean old = UIManager.getBoolean("FileChooser.readOnly");/////This Helps Prevent Renaming
        UIManager.put("FileChooser.readOnly", Boolean.TRUE);/////This Helps Prevent Renaming
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser(fsv);
        UIManager.put("FileChooser.readOnly", old);/////This Helps Prevent Renaming
        fc.setDragEnabled(false); // Helps Prevent Dragging

        if (path.equals("Effects")) {
            fc.addChoosableFileFilter(new ModelFilter());

            EffectAccessory ea = new EffectAccessory();
            fc.setAccessory(ea);
            fc.addPropertyChangeListener(ea); // to receive selection changes
            fc.addActionListener(ea);
        } else if (path.equals("Audio")) {
            fc.addChoosableFileFilter(new AudioFilter());

            //AudioAccessory aa = new AudioAccessory(directory);
            //fc.setAccessory(aa);
            //fc.addPropertyChangeListener(aa); // to receive selection changes
        }

        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int returnval = fc.showDialog(new javax.swing.JFrame(), "Select");

        if (returnval != JFileChooser.APPROVE_OPTION) {
            return null;
        } else {
            File selFile = fc.getSelectedFile();
            String[] relative = selFile.getPath().split(root.getName());
            return relative[1];
        }
    }

    /**
     *  A FileSystemView class that limits the file selections to a single root.
     *
     *  When used with the JFileChooser component the user will only be able to
     *  traverse the directories contained within the specified root fill.
     *
     *  The "Look In" combo box will only display the specified root.
     *
     *  The "Up One Level" button will be disable when at the root.
     *
     */
    private static class SingleRootFileSystemView extends FileSystemView {

        File root;
        File[] roots = new File[1];

        public SingleRootFileSystemView(File root) {
            super();
            this.root = root.getAbsoluteFile();
            roots[0] = root;
        }

        @Override
        public File createNewFolder(File containingDir) {
            File folder = new File(containingDir, "New Folder");
            folder.mkdir();
            return folder;
        }

        @Override
        public File getDefaultDirectory() {
            return root;
        }

        @Override
        public File getHomeDirectory() {
            return root;
        }

        @Override
        public File[] getRoots() {
            return roots;
        }
    }

    public static class ModelFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = Utils.getExtension(f);
            if (extension != null) {
                if (Utils.isType(f, Utils.FileType.Model)) {
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }

        //The description of this filter
        @Override
        public String getDescription() {
            return "Models/Effects";
        }
    }

    public static class PictureFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = Utils.getExtension(f);
            if (extension != null) {
                if (Utils.isType(f, Utils.FileType.PIC) ) {
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }

        //The description of this filter
        @Override
        public String getDescription() {
            return "Pictures";
        }
    }

    public static class AudioFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = Utils.getExtension(f);
            if (extension != null) {
                if (Utils.isType(f, Utils.FileType.SFX)) {
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }

        //The description of this filter
        @Override
        public String getDescription() {
            return "jME Audio";
        }
    }

    public static class Utils {

        public enum FileType {
            Model, SFX, PIC
        }
        public final static String PNG = "png";
        public final static String JPG = "jpg";
        public final static String GIF = "gif";
        public final static String DDS = "dds";
        public final static String HDR = "hdr";
        public final static String PFM = "pfm";
        public final static String TGA = "tga";
        public final static String WAV = "wav";
        public final static String OGG = "ogg";
        public final static String J3O = "j3o";
        /*
         * Get the extension of a file.
         */

        public static String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
            }
            return ext;
        }

        public static boolean isType(File f, FileType type) {
            if (f == null || f.isDirectory()) {
                return false;
            } else if (type.equals(FileType.Model)) {
                if (getExtension(f).equals(J3O)) {
                    return true;
                }
            } else if (type.equals(FileType.SFX)) {
                if (getExtension(f).equals(OGG) || getExtension(f).equals(WAV)) {
                    return true;
                }
            } else if (type.equals(FileType.PIC)) {
                if (getExtension(f).equals(PNG) || getExtension(f).equals(JPG) || getExtension(f).equals(GIF)
                        || getExtension(f).equals(DDS) || getExtension(f).equals(HDR)
                        || getExtension(f).equals(PFM) || getExtension(f).equals(TGA)) {
                    return true;
                }
            }
            return false;
        }
    }
}
