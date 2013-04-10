/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.flairapi;

import com.jme3.gde.core.assets.BinaryModelDataObject;
import com.jme3.gde.core.scene.SceneApplication;
import com.jme3.gde.core.scene.SceneRequest;
import com.spectre.director.ToolsDirector.Utils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.concurrent.Callable;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;

/**
 * MODIFED CODE FROM "Java Swing, 2nd Edition"
 * @author Kyle Williams
 */
//AudioAccessory.java
//An accessory for JFileChooser that lets you play music clips. Only the
//simple .au, .aiff and .wav formats available through the Applet sound
//classes can be played.
//
public class EffectAccessory extends JPanel implements PropertyChangeListener, ActionListener {

    private JLabel fileLabel;
    private JButton playButton;
    private FileObject fo;
    private Boolean sceneStarted;

    public EffectAccessory() {
        // Set up the accessory. The file chooser will give us a reasonable
        // size.
        setLayout(new BorderLayout());
        fileLabel = new JLabel("Clip Name");
        add(fileLabel, BorderLayout.NORTH);
        JPanel p = new JPanel();
        playButton = new JButton("Play");
        playButton.setToolTipText("Play this audio clip.");
        playButton.setEnabled(false);
        p.add(playButton);
        add(p, BorderLayout.CENTER);
        sceneStarted = false;

        playButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                play();
            }
        });
    }

    @Override
    public void propertyChange(final PropertyChangeEvent e) {

        String pname = e.getPropertyName();
        if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(pname)) {
            // Ok, the user selected a file in the chooser
            File fi = (File) e.getNewValue();
            if (fi == null || !fi.isFile()) {
                return;
            }

            // Make reasonably sure it's an audio file
            String extension = Utils.getExtension(fi);
            if (extension.equals(Utils.J3O)) {
                fo = FileUtil.toFileObject(fi);
                fileLabel.setText("  " + fo.getName());
                playButton.setEnabled(true);
            } else {
                fo = null;
                fileLabel.setText("Clip Name");
                playButton.setEnabled(false);
            }
        }
    }

    private void play() {
        if (fo != null) {

            BinaryModelDataObject data = null;
            try {
                data = (BinaryModelDataObject) DataObject.find(fo);
            } catch (DataObjectNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            }
            new com.jme3.gde.scenecomposer.OpenSceneComposer(data).actionPerformed(null);
            sceneStarted = true;
            playButton.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (sceneStarted == true) {
            SceneApplication.getApplication().enqueue(new Callable<Object>() {

                @Override
                public Object call() throws Exception {
                    SceneRequest sceneRequest = SceneApplication.getApplication().getCurrentSceneRequest();
                    SceneApplication.getApplication().closeScene(sceneRequest);
                    return null;
                }
            });
            sceneStarted = false;
        }
    }
}