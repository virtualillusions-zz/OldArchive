package com.vza.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.ui.Picture;

public class TestGUI extends SimpleApplication{
	
	public static void main(String[] args){
		TestGUI app = new TestGUI();
        app.start();
    }

    public void simpleInitApp() {
    	manager.registerLocator("/com/vza/gui/", ClasspathLocator.class.getName(), "jpg");
        Picture p = new Picture("Picture");
        p.setPosition(0, 0);
        p.setWidth(settings.getWidth());
        p.setHeight(settings.getHeight());
        p.setImage(manager, "Random.jpg", false);

        // attach geometry to orthoNode
        guiNode.attachChild(p);
    }

}
