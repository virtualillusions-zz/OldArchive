/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tools.flairEditor.Filters;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Kyle Williams
 */
public class MeshFilter extends FileFilter {

    //Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.J3O)  ) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "jME Mesh";
    }
}
