/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.flairvisualeditor.util;

import com.spectre.director.ToolsDirector;
import com.spectre.director.ToolsDirector.Utils.FileType;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Kyle Williams
 */
public class FolderChildren extends ChildFactory.Detachable<File> {

    private File dir;
    private String iconPath;
    private AbstractAction act;
    private final FileType fil;
    private static final ToolsDirector.AudioFilter af = new ToolsDirector.AudioFilter();
    private static final ToolsDirector.ModelFilter mf = new ToolsDirector.ModelFilter();
    private static final ToolsDirector.PictureFilter pf = new ToolsDirector.PictureFilter();

    private FolderChildren(File directory, FileType Filter, AbstractAction action) {
        dir = directory;
        fil = Filter;
        act = action;
    }

    public FolderChildren(File directory, FileType Filter, AbstractAction action, String iconPath) {
        this(directory, Filter, action);
        this.iconPath = iconPath;
    }

    @Override
    protected boolean createKeys(List<File> toPopulate) {
        FileFilter filter;
        ArrayList<File> temp = new ArrayList<File>();

        switch (fil) {
            case Model:
                filter = new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        return mf.accept(f);
                    }
                };
                break;
            case PIC:
                filter = new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        return pf.accept(f);
                    }
                };
                break;
            case SFX:
                filter = new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        return af.accept(f);
                    }
                };

                break;
            default:
                filter = null;
                break;
        }

        if (filter == null) {
            Collections.addAll(temp, dir.listFiles());
        } else {
            Collections.addAll(temp, dir.listFiles(filter));
        }

        toPopulate.addAll(temp);
        FileSort.sortFilesNamesAscendingIgnoreCase(toPopulate);

        return true;
    }

    @Override
    protected Node createNodeForKey(File key) {
        String iconDirectory = "com/spectre/flairvisualeditor/util/";
        Node node;

        if (key.isDirectory()) {
            if (iconPath != null) {
                node = new BaseNode(Children.create(new FolderChildren(key, fil, act, iconPath), true),
                        Lookups.fixed(key), iconDirectory + "folder.gif", null);
            } else {
                node = new BaseNode(Children.create(new FolderChildren(key, fil, act), true),
                        Lookups.fixed(key), iconDirectory + "folder.gif", null);
            }
        } else {
            //File
            if (iconPath != null) {
                node = new BaseNode(Children.LEAF, Lookups.fixed(key), iconPath, act);
            } else {
                node = new BaseNode(Children.LEAF, Lookups.fixed(key), iconDirectory + "file.gif", act);
            }
        }
        node.setDisplayName(key.getName());
        return node;
    }
}
