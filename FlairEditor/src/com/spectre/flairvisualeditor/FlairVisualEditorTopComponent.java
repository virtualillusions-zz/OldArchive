/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.flairvisualeditor;

import com.jme3.animation.AnimChannel;
import com.jme3.gde.core.scene.SceneApplication;
import com.spectre.deck.card.Animation;
import com.spectre.deck.card.Card;
import com.spectre.deck.card.Effect;
import com.spectre.director.EditorDirector;
import com.spectre.director.SceneDirector;
import com.spectre.director.ToolsDirector.Utils.FileType;
import com.spectre.flairapi.slider.EffectTimers;
import com.spectre.flairvisualeditor.util.*;
import com.spectre.seriesfiletype.SeriesDataObject.SeriesSaveCookie;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.Callable;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.IconView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ChangeSupport;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.openide.windows.Mode;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//com.spectre.flairvisualeditor//FlairVisualEditor//EN",
autostore = false)
@TopComponent.Description(preferredID = "FlairVisualEditorTopComponent",
iconBase = "com/spectre/flairvisualeditor/icon.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = "com.spectre.flairvisualeditor.FlairVisualEditorTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_FlairVisualEditorAction",
preferredID = "FlairVisualEditorTopComponent")
public final class FlairVisualEditorTopComponent extends TopComponent implements ExplorerManager.Provider, LookupListener {

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public FlairVisualEditorTopComponent() {
        //initial node setup
        Collection<String> c = SceneDirector.getInstance().getAnimControl().getAnimationNames();
        String iconPath = "com/spectre/flairvisualeditor/animIcon.gif";
        animNode = new AbstractNode(Children.create(new BasicChildren(c, iconPath, animNodeAction), true));
        c = SceneDirector.getInstance().getBoneList();
        iconPath = "com/spectre/flairvisualeditor/boneIcon.gif";
        boneNode = new AbstractNode(Children.create(new BasicChildren(c, iconPath, boneNodeAction), true));
        emitterNode = new AbstractNode(Children.LEAF);
        soundNode = new AbstractNode(Children.LEAF);
        //Component initializations
        initComponents();
        setName(NbBundle.getMessage(FlairVisualEditorTopComponent.class, "CTL_FlairVisualEditorTopComponent"));
        setToolTipText(NbBundle.getMessage(FlairVisualEditorTopComponent.class, "HINT_FlairVisualEditorTopComponent"));
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_KEEP_PREFERRED_SIZE_WHEN_SLIDED_IN, Boolean.TRUE);

        associateLookup(ExplorerUtils.createLookup(mgr, getActionMap()));
    }

    @Override
    public void open() {
        Mode m = WindowManager.getDefault().findMode("output");
        if (m != null) {
            m.dockInto(this);
        }
        ////////PUT HERE AS A WAY USERS CAN UPDATE ANIMATIONS AUDIO ETC
        
        /////////////////////////////////////////
        super.open();
    }

    @Override
    public void componentOpened() {
        if (SceneDirector.getInstance().getPlayer() == null) {
            SceneDirector.getInstance().displayModel();
            return;
        }
        if (!EditorDirector.getFliarDataEditorTopComponent().isOpened()) {
            EditorDirector.getFliarDataEditorTopComponent().open();//Open Everything
        }
        result = Utilities.actionsGlobalContext().lookupResult(Card.class);
        result.addLookupListener(this);
        result2 = Utilities.actionsGlobalContext().lookupResult(ChangeSupport.class);
        result2.addLookupListener(this);
        result3 = Utilities.actionsGlobalContext().lookupResult(SeriesSaveCookie.class);
        result3.addLookupListener(this);

        //Makes Anim Tab first to appear
        TabbedPane.setSelectedIndex(0);
        SceneDirector.getInstance().getPlayer();//This Makes sure the model is loaded        
        if (mgr.getRootContext() != animNode) {
            mgr.setRootContext(animNode);
        }
    }

    @Override
    public void componentClosed() {
        if (isSaved == false) {
            shouldSave(card);
        }
        result.removeLookupListener(this);
        result = null;
        result2.removeLookupListener(this);
        result2 = null;
        result3.removeLookupListener(this);
        result3 = null;
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return mgr;
    }

    /**
     * Set the ExplorerManager's RootContext Node
     *
     * @param node
     */
    public void setIconViewNode(AbstractNode node) {
        if (mgr.getRootContext() != node) {
            mgr.setRootContext(node);
        }
    }
    // </editor-fold>

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        effectsButtonGroup = new javax.swing.ButtonGroup();
        ScrollPanel = new javax.swing.JScrollPane();
        TabbedPane = new javax.swing.JTabbedPane();
        AnimationTab = new javax.swing.JPanel();
        animParam = new javax.swing.JPanel();
        animSpeedTitle = new javax.swing.JLabel();
        animSpeedLabel = new javax.swing.JLabel();
        animSpeedSlider = new javax.swing.JSlider();
        animTimeTitle = new javax.swing.JLabel();
        animTimeLabel = new javax.swing.JLabel();
        animTimeSlider = new javax.swing.JSlider();
        animResetButton = new javax.swing.JButton();
        animationsPane = new IconView();
        Effects = new javax.swing.JPanel();
        effectsTab = new javax.swing.JTabbedPane();
        effectBoneTab = new javax.swing.JPanel();
        boneScrollPane = new IconView();
        effectSoundTab = new javax.swing.JPanel();
        soundScrollPane = new IconView();
        ((IconView)soundScrollPane).setShowParentNode(true);
        effectEffectTab = new javax.swing.JPanel();
        effectScrollPane = new IconView();
        ((IconView)effectScrollPane).setShowParentNode(true);
        effectsButtonGroupPanel = new javax.swing.JPanel();
        passiveEffectButton = new javax.swing.JRadioButton();
        currentSelectedEffect=0;
        activeEffectButton = new javax.swing.JRadioButton();
        contactEffectButton = new javax.swing.JRadioButton();
        effectsButtonGroupPanel1 = new javax.swing.JPanel();
        effectResetButton = new javax.swing.JButton();
        effectTimerButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        TabbedPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TabbedPaneStateChanged(evt);
            }
        });

        AnimationTab.setPreferredSize(new java.awt.Dimension(800, 250));
        AnimationTab.setLayout(new java.awt.BorderLayout());

        animParam.setPreferredSize(new java.awt.Dimension(959, 50));

        animSpeedTitle.setFont(new java.awt.Font("Times New Roman", 0, 18));
        org.openide.awt.Mnemonics.setLocalizedText(animSpeedTitle, org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.animSpeedTitle.text")); // NOI18N

        animSpeedLabel.setFont(new java.awt.Font("Times New Roman", 0, 18));
        org.openide.awt.Mnemonics.setLocalizedText(animSpeedLabel, org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.animSpeedLabel.text")); // NOI18N

        animSpeedSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                animSpeedSliderStateChanged(evt);
            }
        });

        animTimeTitle.setFont(new java.awt.Font("Times New Roman", 0, 18));
        org.openide.awt.Mnemonics.setLocalizedText(animTimeTitle, org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.animTimeTitle.text")); // NOI18N

        animTimeLabel.setFont(new java.awt.Font("Times New Roman", 0, 18));
        org.openide.awt.Mnemonics.setLocalizedText(animTimeLabel, org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.animTimeLabel.text")); // NOI18N

        animTimeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                animTimeSliderStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(animResetButton, org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.animResetButton.text")); // NOI18N
        animResetButton.setEnabled(false);
        animResetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                animResetButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout animParamLayout = new javax.swing.GroupLayout(animParam);
        animParam.setLayout(animParamLayout);
        animParamLayout.setHorizontalGroup(
            animParamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(animParamLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(animSpeedTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(animSpeedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(animSpeedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(animTimeTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(animTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(animTimeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(animResetButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        animParamLayout.setVerticalGroup(
            animParamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(animParamLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(animParamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(animResetButton)
                    .addComponent(animTimeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(animSpeedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(animParamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(animTimeTitle)
                        .addComponent(animTimeLabel))
                    .addGroup(animParamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(animSpeedTitle)
                        .addComponent(animSpeedLabel)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        AnimationTab.add(animParam, java.awt.BorderLayout.PAGE_START);
        AnimationTab.add(animationsPane, java.awt.BorderLayout.CENTER);

        TabbedPane.addTab(org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.AnimationTab.TabConstraints.tabTitle"), AnimationTab); // NOI18N
        AnimationTab.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.AnimationTab.AccessibleContext.accessibleName")); // NOI18N

        Effects.setPreferredSize(new java.awt.Dimension(800, 250));

        effectsTab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                effectsTabStateChanged(evt);
            }
        });

        javax.swing.GroupLayout effectBoneTabLayout = new javax.swing.GroupLayout(effectBoneTab);
        effectBoneTab.setLayout(effectBoneTabLayout);
        effectBoneTabLayout.setHorizontalGroup(
            effectBoneTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(effectBoneTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(boneScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                .addContainerGap())
        );
        effectBoneTabLayout.setVerticalGroup(
            effectBoneTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(effectBoneTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(boneScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addContainerGap())
        );

        effectsTab.addTab(org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.effectBoneTab.TabConstraints.tabTitle"), effectBoneTab); // NOI18N

        javax.swing.GroupLayout effectSoundTabLayout = new javax.swing.GroupLayout(effectSoundTab);
        effectSoundTab.setLayout(effectSoundTabLayout);
        effectSoundTabLayout.setHorizontalGroup(
            effectSoundTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(effectSoundTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(soundScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                .addContainerGap())
        );
        effectSoundTabLayout.setVerticalGroup(
            effectSoundTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, effectSoundTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(soundScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addContainerGap())
        );

        effectsTab.addTab(org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.effectSoundTab.TabConstraints.tabTitle"), effectSoundTab); // NOI18N

        javax.swing.GroupLayout effectEffectTabLayout = new javax.swing.GroupLayout(effectEffectTab);
        effectEffectTab.setLayout(effectEffectTabLayout);
        effectEffectTabLayout.setHorizontalGroup(
            effectEffectTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(effectEffectTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(effectScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                .addContainerGap())
        );
        effectEffectTabLayout.setVerticalGroup(
            effectEffectTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(effectEffectTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(effectScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addContainerGap())
        );

        effectsTab.addTab(org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.effectEffectTab.TabConstraints.tabTitle"), effectEffectTab); // NOI18N

        effectsButtonGroup.add(passiveEffectButton);
        passiveEffectButton.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(passiveEffectButton, org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.passiveEffectButton.text")); // NOI18N
        passiveEffectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passiveEffectButtonActionPerformed(evt);
            }
        });

        effectsButtonGroup.add(activeEffectButton);
        org.openide.awt.Mnemonics.setLocalizedText(activeEffectButton, org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.activeEffectButton.text")); // NOI18N
        activeEffectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activeEffectButtonActionPerformed(evt);
            }
        });

        effectsButtonGroup.add(contactEffectButton);
        org.openide.awt.Mnemonics.setLocalizedText(contactEffectButton, org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.contactEffectButton.text")); // NOI18N
        contactEffectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactEffectButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout effectsButtonGroupPanelLayout = new javax.swing.GroupLayout(effectsButtonGroupPanel);
        effectsButtonGroupPanel.setLayout(effectsButtonGroupPanelLayout);
        effectsButtonGroupPanelLayout.setHorizontalGroup(
            effectsButtonGroupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(effectsButtonGroupPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(effectsButtonGroupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(passiveEffectButton)
                    .addComponent(activeEffectButton)
                    .addComponent(contactEffectButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        effectsButtonGroupPanelLayout.setVerticalGroup(
            effectsButtonGroupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(effectsButtonGroupPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(passiveEffectButton)
                .addGap(18, 18, 18)
                .addComponent(activeEffectButton)
                .addGap(18, 18, 18)
                .addComponent(contactEffectButton)
                .addContainerGap(136, Short.MAX_VALUE))
        );

        org.openide.awt.Mnemonics.setLocalizedText(effectResetButton, org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.effectResetButton.text")); // NOI18N
        effectResetButton.setEnabled(false);
        effectResetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                effectResetButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(effectTimerButton, org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.effectTimerButton.text")); // NOI18N
        effectTimerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                effectTimerButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout effectsButtonGroupPanel1Layout = new javax.swing.GroupLayout(effectsButtonGroupPanel1);
        effectsButtonGroupPanel1.setLayout(effectsButtonGroupPanel1Layout);
        effectsButtonGroupPanel1Layout.setHorizontalGroup(
            effectsButtonGroupPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(effectsButtonGroupPanel1Layout.createSequentialGroup()
                .addGroup(effectsButtonGroupPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(effectTimerButton)
                    .addComponent(effectResetButton))
                .addContainerGap())
        );
        effectsButtonGroupPanel1Layout.setVerticalGroup(
            effectsButtonGroupPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(effectsButtonGroupPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(effectTimerButton)
                .addGap(18, 18, 18)
                .addComponent(effectResetButton)
                .addContainerGap(172, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout EffectsLayout = new javax.swing.GroupLayout(Effects);
        Effects.setLayout(EffectsLayout);
        EffectsLayout.setHorizontalGroup(
            EffectsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EffectsLayout.createSequentialGroup()
                .addComponent(effectsButtonGroupPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(effectsTab, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(effectsButtonGroupPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        EffectsLayout.setVerticalGroup(
            EffectsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EffectsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(EffectsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(effectsTab, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                    .addComponent(effectsButtonGroupPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(effectsButtonGroupPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        TabbedPane.addTab(org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.Effects.TabConstraints.tabTitle"), Effects); // NOI18N
        Effects.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.Effects.AccessibleContext.accessibleName")); // NOI18N

        ScrollPanel.setViewportView(TabbedPane);
        TabbedPane.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.TabbedPane.AccessibleContext.accessibleName")); // NOI18N

        add(ScrollPanel, java.awt.BorderLayout.CENTER);
        ScrollPanel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(FlairVisualEditorTopComponent.class, "FlairVisualEditorTopComponent.ScrollPanel.AccessibleContext.accessibleName")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Generated Methods">
private void animSpeedSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_animSpeedSliderStateChanged
    if (getAnimation() == null) {
        return;
    }
    //Speed takes care of itself it should be degenerative if lower than 50% and higher if it is 50%
    //slider=animSpeed, 0=-2, 25=-1, 50=0, 75=1, 100=2
    float speed = (4 * ((float) animSpeedSlider.getValue() / 100)) - 2;
    animSpeedLabel.setText(speed + "");
    getAnimation().setSpeed(speed);
    setNotSaved();
}//GEN-LAST:event_animSpeedSliderStateChanged

private void animTimeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_animTimeSliderStateChanged
    if (getAnimation() == null) {
        return;
    }
    AnimChannel chnnl = SceneDirector.getInstance().getAnimControl().getChannel(0);
    chnnl.setSpeed(0f);
    float maxTime = chnnl.getAnimMaxTime();
    float time = maxTime * ((float) animTimeSlider.getValue() / 100);
    animTimeLabel.setText(time + "");
    getAnimation().setStartTime(time);
    setNotSaved();
}//GEN-LAST:event_animTimeSliderStateChanged

private void passiveEffectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passiveEffectButtonActionPerformed
    currentSelectedEffect = 0;
    effectResetButtonActionPerformed(null);
}//GEN-LAST:event_passiveEffectButtonActionPerformed

private void activeEffectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activeEffectButtonActionPerformed
    currentSelectedEffect = 1;
    effectResetButtonActionPerformed(null);
}//GEN-LAST:event_activeEffectButtonActionPerformed

private void contactEffectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactEffectButtonActionPerformed
    currentSelectedEffect = 2;
    effectResetButtonActionPerformed(null);
}//GEN-LAST:event_contactEffectButtonActionPerformed

private void TabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabbedPaneStateChanged
    javax.swing.JTabbedPane pane = (javax.swing.JTabbedPane) evt.getSource();
    // Get current tab
    int sel = pane.getSelectedIndex();
    if (sel == 0) {
        setIconViewNode(animNode);
    } else {
        effectsTab.setSelectedIndex(0);
        setIconViewNode(boneNode);
    }
}//GEN-LAST:event_TabbedPaneStateChanged

private void effectsTabStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_effectsTabStateChanged
    javax.swing.JTabbedPane pane = (javax.swing.JTabbedPane) evt.getSource();
    // Get current tab
    int sel = pane.getSelectedIndex();
    //Rememeber Reset Depends On This Order
    switch (sel) {
        case 0:
            setIconViewNode(boneNode);
            break;
        case 1:
            setIconViewNode(soundNode);
            break;
        case 2:
            setIconViewNode(emitterNode);
            break;
        default:
            throw new IndexOutOfBoundsException("For some odd reason TabbedPane selected a Tab >1");
    }

    currentEffectIconViewDefaultSelect();//preselect effect attributes 
}//GEN-LAST:event_effectsTabStateChanged

private void animResetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_animResetButtonActionPerformed
    int i = JOptionPane.NO_OPTION;

    if (evt != null) {
        i = JOptionPane.showConfirmDialog(null, "Reset Animation Data To An Earlier State", "Reset Animation Properties", JOptionPane.YES_NO_OPTION);
    }

    if (i == JOptionPane.YES_OPTION || evt == null) {
        //Get All Original Values
        Animation anim = prevCard.getAnimation();
        final String name = anim.getAnimationName();
        getAnimation().setAnimationName(name);
        java.awt.EventQueue.invokeLater(new java.lang.Runnable() {
            @Override
            public void run() {
                //Select Original Animation in IconView        
                try {
                    for (org.openide.nodes.Node node : animNode.getChildren().getNodes()) {
                        if (node.getDisplayName().equals(name)) {
                            getExplorerManager().setSelectedNodes(new org.openide.nodes.Node[]{node});
                            break;
                        }
                    }
                } catch (PropertyVetoException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
        float temp;//create temp variable for calculations

        float speed = anim.getSpeed();//First get original speed        
        getAnimation().setSpeed(speed);//Set New Speed to Old Speed
        //Set Slider to proper Location
        temp = 25 * speed + 50;
        animSpeedSlider.setValue((int) temp);

        float start = anim.getStartTime(); //First get original animation start time    
        getAnimation().setStartTime(start);//Set New Animation Start Time to Old Animation Start Time
        //Set Slider to proper Location
        float maxTime = SceneDirector.getInstance().getAnimControl().getChannel(0).getAnimMaxTime();
        temp = 100 * start / maxTime;
        animTimeSlider.setValue((int) temp);

        setNotSaved();
    }
}//GEN-LAST:event_animResetButtonActionPerformed

private void effectTimerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_effectTimerButtonActionPerformed
    if (getAnimation() != null) {
        if (currentSelectedEffect == 0) { //passive effect
            EffectTimers.SetEffectTime(getAnimation(), getSelectedEffect()); //Set Start And End Time
        } else if (currentSelectedEffect == 1) { //active effect
            EffectTimers.SetEffectStartTime(getAnimation(), getSelectedEffect()); //Set Start Time
        }
        //This is in wrong location and will automatically trigger a save cookie...this is intented for simplicity
        setNotSaved();
    }
}//GEN-LAST:event_effectTimerButtonActionPerformed
private void effectResetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_effectResetButtonActionPerformed
    //If not Set Up Properly Return
    if (getPassiveEffect() == null) {
        return;
    }

    int i = JOptionPane.NO_OPTION;
    Effect effect = this.getSelectedEffect();


    if (evt != null) {
        String type = effect.getEffectType().toString();
        i = JOptionPane.showConfirmDialog(null, "Reset All " + type + " Effect Data To An Earlier State",
                "Reset " + type + " Effect Properties", JOptionPane.YES_NO_OPTION);
    }

    if (i == JOptionPane.YES_OPTION || evt == null) {
        //Need To Change Depending on Effect and current tab bone sound emitter
        //should use null state to determine if should be reset or not
        //Initial States wil take Care of Themselves

        if (currentSelectedEffect == 0) {//passive
            cloneEffect(getPassiveEffect(), prevCard.getPassiveEffect());
        } else if (currentSelectedEffect == 1) {//active
            cloneEffect(getActiveEffect(), prevCard.getActiveEffect());
        } else if (currentSelectedEffect == 2) {//contact
            cloneEffect(getContactEffect(), prevCard.getContactEffect());
        }
    }

}//GEN-LAST:event_effectResetButtonActionPerformed

    /**
     * PreSelects the Nodes in the Effect Pane for Visual Reference Allows user
     * to see what is currently Selected for the effect
     */
    public void currentEffectIconViewDefaultSelect() {
        //If not Set Up Properly Return
        if (getPassiveEffect() == null) {
            return;
        }

        //Select Original Animation in IconView         
        java.util.ArrayList<String> searchlist = new java.util.ArrayList<String>();
        org.openide.nodes.Node searchedNode;


        int index = effectsTab.getSelectedIndex();
        if (index == 0) {//Bone
            searchedNode = boneNode;
            searchlist.add(getSelectedEffect().getBoneLocation());
        } else if (index == 1) {//Sound
            searchedNode = soundNode;
            searchlist.add(getSelectedEffect().getAudioLocation());
        } else if (index == 2) {//Emitter
            searchedNode = emitterNode;
            searchlist.addAll(getSelectedEffect().getEmitters());
        } else {
            return;
        }

        //Most use ArrayList instead as their may be unselected nodes in first view resulting in null 
        //array index's which is not allowed when selected nodes
        java.util.ArrayList<org.openide.nodes.Node> nodesFound = new java.util.ArrayList<org.openide.nodes.Node>(searchlist.size());

        for (org.openide.nodes.Node node : searchedNode.getChildren().getNodes()) {
            if (searchlist.contains(node.getDisplayName())) {
                nodesFound.add(node);
            }
        }
        try {
            //Most convert nodes to array list this way to prevent null index's      
            org.openide.nodes.Node[] n = nodesFound.toArray(new org.openide.nodes.Node[nodesFound.size()]);
            getExplorerManager().setSelectedNodes(n);//Select Nodes
        } catch (PropertyVetoException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="etc">
    /**
     * Updates a card's attributes with the dummy card used in its place
     */
    private void updateCard() {
        Animation anim = prevCard.getAnimation(), anim2 = card.getAnimation();
        anim.setAnimationName(anim2.getAnimationName());
        anim.setSpeed(anim2.getSpeed());
        anim.setStartTime(anim2.getStartTime());

        cloneEffect(prevCard.getActiveEffect(), getActiveEffect());
        cloneEffect(prevCard.getPassiveEffect(), getPassiveEffect());
        cloneEffect(prevCard.getContactEffect(), getContactEffect());
    }

    /**
     * Clones One Effect Onto another
     *
     * @param cloned the effect being written to
     * @param effect2 the effect parameters are coming from
     */
    private void cloneEffect(Effect cloned, Effect effect) {
        cloned.setAudioLocation(effect.getAudioLocation());
        cloned.setBoneLocation(effect.getBoneLocation());
        cloned.setEndTime(effect.getEndTime());
        cloned.setStartTime(effect.getStartTime());
        cloned.getEmitters().clear();//Clears Everything To Write new to it
        for (String em : effect.getEmitters()) {
            cloned.addEmitter(em);
        }
    }

    private Animation getAnimation() {
        if (card == null) {
            return null;
        }
        return card.getAnimation();
    }

    private Effect getSelectedEffect() {
        switch (currentSelectedEffect) {
            case 0:
                return getPassiveEffect();
            case 1:
                return getActiveEffect();
            case 2:
                return getContactEffect();
            default:
                return null;//throw new IndexOutOfBoundsException("For some odd reason this Pane selected a Effect >2");
        }
    }

    private Effect getPassiveEffect() {
        if (card == null) {
            return null;
        }
        return card.getPassiveEffect();
    }

    private Effect getActiveEffect() {
        if (card == null) {
            return null;
        }
        return card.getActiveEffect();
    }

    private Effect getContactEffect() {
        if (card == null) {
            return null;
        }
        return card.getContactEffect();
    }

    /**
     * Locates the Sound and Emitter Files depending on the project and updates
     * the emitterNode and soundNode accordingly
     */
    public void resetIconNodes() {

        com.spectre.director.Debug_Director.setAssetsPath(sc.getSaveFileObject().getParent().getPath());

        String iconPath = "com/spectre/flairvisualeditor/";

        File f = new File(sc.getSaveFileObject().getParent().getPath() + "/Deck/Effects");
        emitterNode = new AbstractNode(Children.create(new FolderChildren(f, FileType.Model, effectNodeAction, iconPath + "emitterIcon.gif"), true), Lookups.fixed(f));

        f = new File(sc.getSaveFileObject().getParent().getPath() + "/Deck/Audio");
        soundNode = new AbstractNode(Children.create(new FolderChildren(f, FileType.SFX, soundNodeAction, iconPath + "sfxIcon.gif"), true), Lookups.fixed(f));
    }

    /**
     * Sets the isSaved boolean to false and updates characterEditorController
     */
    public void setNotSaved() {
        SceneApplication.getApplication().enqueue(new Callable<Object>() {
            @Override
            public Object call() throws Exception {

                SceneDirector.getInstance().getDebugCharacterEditorController().setCard(card);

                return null;
            }
        });


        isSaved = false;
        if (cs != null) {
            cs.fireChange();
        }
    }

    /**
     * Message Dialog To Save Card Entries before Switching cards.
     *
     * @param c
     */
    public void shouldSave(Card c) {
        //SOMETHING IS OFF WITH THE WAY THIS WORKS
        //if (!card.equals(c)) {
        int i = JOptionPane.showConfirmDialog(null, "Save Changes to" + card.getName(), "Save" + card.getName(), JOptionPane.YES_NO_OPTION);
        //If okay was selected
        if (i == JOptionPane.YES_OPTION) {
            try {
                updateCard();
                sc.save();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            prevCard = c;
            card = prevCard.clone();
        } else if (i == JOptionPane.NO_OPTION) {
            prevCard = c;
            card = prevCard.clone();
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Results Changed Method">
    @Override
    public void resultChanged(LookupEvent ev) {
        Lookup.Result r = (Lookup.Result) ev.getSource();
        java.util.Collection c = r.allInstances();
        if (!c.isEmpty()) {
            Object o = c.iterator().next();
            if (o instanceof Card) {
                //Checks if Same Card Selected
                if (prevCard != null && (prevCard.equals((Card) o) || card.equals((Card) o))) {
                    return;
                }

                if (isSaved == false) {
                    shouldSave((Card) o);
                } else {
                    prevCard = (Card) o;
                    card = prevCard.clone();
                }

                //Sets up some external stuff
                setDisplayName("EFIC Visual Editor: " + card.getName());
                setIconViewNode(animNode);
                TabbedPane.setSelectedIndex(0);
                effectsTab.setSelectedIndex(0);

                passiveEffectButton.setSelected(true);
                currentSelectedEffect = 0;//0 for passive  

                //Sets the Card to be Dynamically Edited
                SceneDirector.getInstance().getDebugCharacterEditorController().setCard(card);

                animResetButtonActionPerformed(null);
                effectResetButtonActionPerformed(null);

                isSaved = true;
            } else if (o instanceof ChangeSupport) {
                cs = (ChangeSupport) o;
            } else {
                SeriesSaveCookie newSC = (SeriesSaveCookie) o;
                if (sc != null && sc.equals(newSC)) {
                    return;
                }
                sc = newSC;
                resetIconNodes();
            }
        }

        if (sc != null && cs != null) {
            effectResetButton.setEnabled(true);
            animResetButton.setEnabled(true);
        } else {
            effectResetButton.setEnabled(false);
            animResetButton.setEnabled(false);
        }
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Node Action Definitions">
    AbstractAction animNodeAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (getAnimation() != null) {
                String aN = ((BaseNode) e.getSource()).getDisplayName();
                getAnimation().setAnimationName(aN);
                setNotSaved();
            }
        }
    };
    AbstractAction boneNodeAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (getSelectedEffect() != null) {
                String bone = ((BaseNode) e.getSource()).getDisplayName();
                getSelectedEffect().setBoneLocation(bone);
                setNotSaved();
            }
        }
    };
    AbstractAction soundNodeAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (getSelectedEffect() != null) {
                String sfx = ((BaseNode) e.getSource()).getDisplayName();
                getSelectedEffect().setAudioLocation(sfx);
                setNotSaved();
            }
        }
    };
    AbstractAction effectNodeAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (getSelectedEffect() != null) {
                String emitter = ((BaseNode) e.getSource()).getDisplayName();
                java.util.List<String> emitters = getSelectedEffect().getEmitters();
                if (!emitters.contains(emitter)) {
                    emitters.add(emitter);
                } else {
                    emitters.remove(emitter);
                }
                setNotSaved();
            }
        }
    };
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Generated Variables">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AnimationTab;
    private javax.swing.JPanel Effects;
    private javax.swing.JScrollPane ScrollPanel;
    private javax.swing.JTabbedPane TabbedPane;
    private javax.swing.JRadioButton activeEffectButton;
    private javax.swing.JPanel animParam;
    private javax.swing.JButton animResetButton;
    private javax.swing.JLabel animSpeedLabel;
    private javax.swing.JSlider animSpeedSlider;
    private javax.swing.JLabel animSpeedTitle;
    private javax.swing.JLabel animTimeLabel;
    private javax.swing.JSlider animTimeSlider;
    private javax.swing.JLabel animTimeTitle;
    private javax.swing.JScrollPane animationsPane;
    private javax.swing.JScrollPane boneScrollPane;
    private javax.swing.JRadioButton contactEffectButton;
    private javax.swing.JPanel effectBoneTab;
    private javax.swing.JPanel effectEffectTab;
    private javax.swing.JButton effectResetButton;
    private javax.swing.JScrollPane effectScrollPane;
    private javax.swing.JPanel effectSoundTab;
    private javax.swing.JButton effectTimerButton;
    private javax.swing.ButtonGroup effectsButtonGroup;
    private javax.swing.JPanel effectsButtonGroupPanel;
    private javax.swing.JPanel effectsButtonGroupPanel1;
    private javax.swing.JTabbedPane effectsTab;
    private javax.swing.JRadioButton passiveEffectButton;
    private javax.swing.JScrollPane soundScrollPane;
    // End of variables declaration//GEN-END:variables
    private final ExplorerManager mgr = new ExplorerManager();
    private Lookup.Result result = null, result2 = null, result3 = null;
    private boolean isSaved = true;
    private Card prevCard, card;
    private ChangeSupport cs;
    private SeriesSaveCookie sc;
    private int currentSelectedEffect;
    private AbstractNode animNode;
    private AbstractNode boneNode;
    private AbstractNode emitterNode;
    private AbstractNode soundNode;
    // </editor-fold>
}
