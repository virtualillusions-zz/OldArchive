/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.flairvisualeditor.util;

import java.awt.Image;
import java.io.File;
import javax.swing.AbstractAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Kyle Williams
 */
public class BaseNode extends AbstractNode {

    private final String iconPath;
    private final AbstractAction act;

    public BaseNode(Children children, Object key, String iconPath, AbstractAction act) {
        super(children, Lookups.fixed(key));
        this.iconPath = iconPath;
        this.act = act;
    }

    @Override
    public String getHtmlDisplayName() {
        String name = null;
        Object key = getLookup().lookup(Object.class);
        if (key instanceof File) {
            name = ((File) key).getName();
        } else {
            name = key.toString();
        }

        return "<font color='!textText'>" + name + "</font>";
    }

    @Override
    public Image getIcon(int type) {
        if (iconPath != null) {
            return ImageUtilities.loadImage(iconPath);
        } else {
            return super.getIcon(type);
        }
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    public javax.swing.Action getPreferredAction() {
        if (act != null) {
            return act;
        } else {
            return super.getPreferredAction();
        }
    }
}
