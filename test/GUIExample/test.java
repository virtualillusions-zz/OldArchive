package GUIExample;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kyle Williams
 */
public class test {
    public static void main(String[] test){
        new test();
    }

    public test(){
         GraphicsDevice device = GraphicsEnvironment
                                    .getLocalGraphicsEnvironment()
                                    .getDefaultScreenDevice();
          // Array of supported display modes
         DisplayMode[] modes  = device.getDisplayModes();
       for(String r:getResolutions(modes)){
           System.out.println(r);
           for(String k:getFrequencies(r,modes)){
           System.out.println(k);
           }
            for(String i:getDepths(r,modes)){
           System.out.println(i);
             }
       }



    }

     /**
     * Returns every unique resolution from an array of <code>DisplayMode</code>s.
     */
    private static String[] getResolutions(DisplayMode[] modes) {
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
    private static String[] getDepths(String resolution, DisplayMode[] modes) {
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

