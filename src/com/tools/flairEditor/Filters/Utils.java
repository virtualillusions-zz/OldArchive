/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tools.flairEditor.Filters;
import java.io.File;
import javax.swing.ImageIcon;


/**
 *
 * @author Kyle Williams
 */
public class Utils {

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

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Utils.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}