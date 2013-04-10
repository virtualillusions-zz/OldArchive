/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.flairvisualeditor.util;

import java.util.Collection;
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
public class BasicChildren extends ChildFactory<String> {

    private Collection<String> c;
    private String iconPath;
    private AbstractAction act;

    public BasicChildren(Collection<String> c, String iconPath, AbstractAction act) {
        this.c = c;
        this.iconPath = iconPath;
        this.act = act;
    }

    @Override
    protected boolean createKeys(List<String> toPopulate) {
        toPopulate.addAll(c);
        return true;
    }

    @Override
    protected Node createNodeForKey(String key) {
        BaseNode node = new BaseNode(Children.LEAF, Lookups.fixed(key), iconPath, act);
        node.setDisplayName(key);
        return node;
    }
}
