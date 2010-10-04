/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vza.director.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dropdown.controller.DropDownControl;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;

import com.jme3.system.*;
import com.vza.director.Director;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.checkbox.CheckboxControl;
import de.lessvoid.nifty.controls.dropdown.controller.DropDownControlNotify;
import de.lessvoid.xml.xpp3.Attributes;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Kyle Williams
 */
public class SettingsController implements Controller,DropDownControlNotify{
    public Nifty nifty;
    public Screen screen;
    public String currentRes;
    public boolean changed, fullscreen, vSync;
    public String res,refRate,antiAli,bitDepth;
    public Element elmnt;

    // connection to properties file.
    private AppSettings settings;


    // Array of supported display modes
    private DisplayMode[] modes = null;

    public void bind(Nifty nifty, Screen screen, Element elmnt, Properties prprts, ControllerEventListener cl, Attributes atrbts) {
        this.nifty=nifty;
        this.screen=screen;
        this.elmnt=elmnt;
        this.settings = new AppSettings(false);
        this.settings.copyFrom(com.vza.director.Director.getApp().getAppSettings());
        GraphicsDevice device = GraphicsEnvironment
                                    .getLocalGraphicsEnvironment()
                                    .getDefaultScreenDevice();

        modes = device.getDisplayModes();
        Arrays.sort(modes, new DisplayModeSorter());

          currentRes = settings.getWidth()+" x "+settings.getHeight();

          updateSettings();
    }

    public void onStartScreen() {
     //   throw new UnsupportedOperationException("Not supported yet.");
    }
    public void onFocus(boolean bln) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void inputEvent(NiftyInputEvent nie) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

   @Override
    public void dropDownSelectionChanged(DropDownControl ddc) {
        System.out.println(screen.findControl(ddc.getSelectedItem(),DropDownControl.class));
    }

   /**
    * @param id
    * @return
    */
   private Controller findControl(final String id, final String controlType) {
        Controller controller = screen.findControl(id,
                controlType.equals("DB")? DropDownControl.class:CheckboxControl.class);
        return controller;
    }

       /**
        * Updates all settings components
        */
        private void updateSettings(){
          setupComponent("resDDC","DB");
          setupComponent("bitDDC","DB");
          setupComponent("refDDC","DB");
          setupComponent("antDDC","DB");
          setupComponent("fsCB","CB");
          setupComponent("vsCB","CB");
        }

        /**
         * Sets Up dropDownBox and CheckBox
         * @param r
         */
        private void setupComponent(String r,String controlType){
            Controller control = findControl(r,controlType);
            if (control != null) {
                if(controlType.equals("DB")){
                    if(r.equals("resDDC")){
                       updateResolution((DropDownControl) control);
                    }else if(r.equals("bitDDC")){
                        updateBitDepth((DropDownControl) control);
                    }else if(r.equals("refDDC")){
                        updateRefreshRate((DropDownControl) control);
                    }else if(r.equals("antDDC")){
                        updateAntiAlising((DropDownControl) control);
                    }
                } else if(controlType.equals("CB")){
                    if(r.equals("fsCB")){
                        ((CheckboxControl)control).setChecked(Director.getApp().getAppSettings().isFullscreen());
                    }else if(r.equals("vsCB")){
                        ((CheckboxControl)control).setChecked(Director.getApp().getAppSettings().isVSync());
                    }
                }
            }
        }
       /**
        * adds items to the dropDown list
        * @param items
        * @param DDC
        */
       private void addItems(String[] items, DropDownControl DDC){
            DDC.addNotify(this);
            for(int i=0; i<items.length;i++){
                DDC.addItem(items[i]);
            }
        }
        /**
         * Update Resolution
         * @param DDC
         */
        public void updateResolution(DropDownControl DDC){
            addItems(getResolutions(modes),DDC);
            DDC.setSelectedItem(currentRes);
        }
        /**
         * Updates the BitDepth
         * @param DDC
         */
        public void updateBitDepth(DropDownControl DDC){
             addItems(getDepths(currentRes,modes),DDC);
            DDC.setSelectedItem(settings.getDepthBits()+" bpp");
        }

        /**
         * Updates the RefreshRateList
         * @param DDC
         */
        public void updateRefreshRate(DropDownControl DDC){
            addItems(getFrequencies(currentRes,modes),DDC);
            DDC.setSelectedItem(settings.getFrequency()+" Hz");
        }

        /**
         * Updates AntiAlising
         * @param DDC
         */
        public void updateAntiAlising(DropDownControl DDC){
            String[] aa = new String[]{ "0x", "2x", "4x", "6x", "8x", "16x" };
            addItems(aa,DDC);
            DDC.setSelectedItem(settings.getSamples()+"x");
        }





    /**
     * <code>verifyAndSaveCurrentSelection</code> first verifies that the
     * display mode is valid for this system, and then saves the current
     * selection as a properties.cfg file.
     *
     * @return if the selection is valid
     */
    private boolean verifyAndSaveCurrentSelection() {
        String display = this.res;
        boolean fullscreen = this.fullscreen;
        boolean vsync = this.vSync;

        int width = Integer.parseInt(display.substring(0, display
                .indexOf(" x ")));
        display = display.substring(display.indexOf(" x ") + 3);
        int height = Integer.parseInt(display);

        String depthString = this.bitDepth;
        int depth = -1;
        if (depthString.equals("???")){
            depth = 0;
        }else{
            depth = Integer.parseInt(depthString.substring(0, depthString
                .indexOf(' ')));
        }

        String freqString = this.refRate;
        int freq = -1;
        if (fullscreen){
            if (freqString.equals("???")){
                freq = 0;
            }else{
                freq = Integer.parseInt(freqString.substring(0, freqString
                        .indexOf(' ')));
            }
        }

        String aaString = this.antiAli;
        int multisample = -1;
        if (aaString.equals("Disabled")){
            multisample = 0;
        }else{
            multisample = Integer.parseInt(aaString.substring(0, aaString.indexOf('x')));
        }

        // FIXME: Does not work in Linux
        /*
         * if (!fullscreen) { //query the current bit depth of the desktop int
         * curDepth = GraphicsEnvironment.getLocalGraphicsEnvironment()
         * .getDefaultScreenDevice().getDisplayMode().getBitDepth(); if (depth >
         * curDepth) { showError(this,"Cannot choose a higher bit depth in
         * windowed " + "mode than your current desktop bit depth"); return
         * false; } }
         */

        String renderer = Director.getApp().getAppSettings().getRenderer();

        boolean valid = false;

        // test valid display mode when going full screen
        if (!fullscreen)
            valid = true;
        else {
            GraphicsDevice device = GraphicsEnvironment
                                        .getLocalGraphicsEnvironment()
                                        .getDefaultScreenDevice();
            valid = device.isFullScreenSupported();
        }

        if (valid) {
            //use the GameSettings class to save it.
            settings.setWidth(width);
            settings.setHeight(height);
            settings.setBitsPerPixel(depth);
            settings.setFrequency(freq);
            settings.setFullscreen(fullscreen);
            settings.setVSync(vsync);
            settings.setRenderer(renderer);
            settings.setSamples(multisample);
           /*s try {
                Director.getApp().getAppSettings().copyFrom(settings);
                Director.getApp().getAppSettings().save();
            } catch (IOException ioe) {
                Logger.getAnonymousLogger().log(Level.WARNING,
                        "Failed to save setting changes", ioe);
            }*/
        } //else
          //  showError(
            //        this,
              //      "Your monitor claims to not support the display mode you've selected.\n"
                //            + "The combination of bit depth and refresh rate is not supported.");
        return valid;
    }

    /**
     * Returns every unique resolution from an array of <code>DisplayMode</code>s.
     */
    private String[] getResolutions(DisplayMode[] modes) {
        if(settings.isFullscreen()==false){
            return new String[]{"320 x 240","640 x 480","800 x 600",
                                "1024 x 768","1152 x 864","1280 x 720"};
        }
        ArrayList<String> resolutions = new ArrayList<String>(modes.length);
        for (int i = 0; i < modes.length; i++) {
            String res = modes[i].getWidth() + " x " + modes[i].getHeight();
            if (!resolutions.contains(res))
                resolutions.add(res);
        }

        String[] res = new String[resolutions.size()];
        resolutions.toArray(res);
        return res;
    }

    /**
     * Returns every possible bit depth for the given resolution.
     */
    private String[] getDepths(String resolution, DisplayMode[] modes) {
        if(settings.isFullscreen()==false){
            return new String[]{"16 bpp", "24 bpp"};
        }
        ArrayList<String> depths = new ArrayList<String>(4);
        for (int i = 0; i < modes.length; i++) {
            // Filter out all bit depths lower than 16 - Java incorrectly
            // reports
            // them as valid depths though the monitor does not support them
            if (modes[i].getBitDepth() < 16 && modes[i].getBitDepth() > 0)
                continue;

            String res = modes[i].getWidth() + " x " + modes[i].getHeight();
            String depth = modes[i].getBitDepth() + " bpp";
            if (res.equals(resolution) && !depths.contains(depth))
                depths.add(depth);
        }

        if (depths.size() == 1 && depths.contains("-1 bpp")){
            // add some default depths, possible system is multi-depth supporting
            depths.clear();
            depths.add("24 bpp");
        }

        String[] res = new String[depths.size()];
        depths.toArray(res);
        return res;
    }

    /**
     * Returns every possible refresh rate for the given resolution.
     */
    private static String[] getFrequencies(String resolution,
            DisplayMode[] modes) {
        ArrayList<String> freqs = new ArrayList<String>(4);
        for (int i = 0; i < modes.length; i++) {
            String res = modes[i].getWidth() + " x " + modes[i].getHeight();
            String freq;
            if (modes[i].getRefreshRate() == DisplayMode.REFRESH_RATE_UNKNOWN){
                freq = "???";
            }else{
                freq = modes[i].getRefreshRate() + " Hz";
            }

            if (res.equals(resolution) && !freqs.contains(freq))
                freqs.add(freq);
        }

        String[] res = new String[freqs.size()];
        freqs.toArray(res);
        return res;
    }

    /**
     * Utility class for sorting <code>DisplayMode</code>s. Sorts by
     * resolution, then bit depth, and then finally refresh rate.
     */
    private class DisplayModeSorter implements Comparator<DisplayMode> {
        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(DisplayMode a, DisplayMode b) {
            // Width
            if (a.getWidth() != b.getWidth())
                return (a.getWidth() > b.getWidth()) ? 1 : -1;
            // Height
            if (a.getHeight() != b.getHeight())
                return (a.getHeight() > b.getHeight()) ? 1 : -1;
            // Bit depth
            if (a.getBitDepth() != b.getBitDepth())
                return (a.getBitDepth() > b.getBitDepth()) ? 1 : -1;
            // Refresh rate
            if (a.getRefreshRate() != b.getRefreshRate())
                return (a.getRefreshRate() > b.getRefreshRate()) ? 1 : -1;
            // All fields are equal
            return 0;
        }
    }
}
