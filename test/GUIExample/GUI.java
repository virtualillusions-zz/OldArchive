//package GUIExample;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package GUIExample;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
/**
 *
 * @author Kyle Williams
 */
public class GUI extends SimpleApplication{

    private Nifty nifty;

    public static void main(String[] args){
        AppSettings settings = new AppSettings(true);
        settings.setAudioRenderer("LWJGL");
        GUI app = new GUI();
        app.setSettings(settings);
        app.start();
    }

    public void simpleInitApp() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                                                          inputManager,
                                                          audioRenderer,
                                                          guiViewPort);

        nifty = niftyDisplay.getNifty();

       //nifty.fromXml("tutorial/tutorial.xml", "start");
       nifty.fromXml("VezlaGui.xml", "start");

        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);

        // disable the fly cam
        flyCam.setEnabled(false);
        //flyCam.setDragToRotate(true);
        // allow us to interact with gui components
        //inputManager.setCursorVisible(true);
    }
}