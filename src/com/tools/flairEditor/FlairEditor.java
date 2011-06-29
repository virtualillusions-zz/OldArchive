/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tools.flairEditor;

import com.jme3.animation.AnimControl;
import com.jme3.animation.BoneAnimation;
import com.jme3.animation.Skeleton;
import com.jme3.animation.SkeletonControl;
import com.jme3.app.SimpleApplication;
import com.jme3.input.ChaseCamera;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.AnimData;
import com.jme3.system.AppSettings;
import com.spectre.deck.Card;
import com.spectre.deck.MasterDeck;
import com.spectre.util.SAX.DeckHandler;
import com.spectre.util.SAX.SpectreHandler;
import de.lessvoid.nifty.Nifty;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author Kyle Williams 
 */
public class FlairEditor extends SimpleApplication{
    public static void main(String[] args){
        FlairEditor app = new FlairEditor();
        AppSettings settings = new AppSettings(false);
        settings.setTitle("Flair Editor by Virtual illusions");
        settings.setSettingsDialogImage("/com/tools/flairEditor/logo.jpg");        
        app.setSettings(settings);
        Logger.getLogger("").setLevel(Level.WARNING);
        app.start();
    }
    
    private Spatial spat;
    private DirectionalLight dl;
    
    @Override
    public void simpleInitApp() {        
        try { 
            //Order Important
            setUp();  
            //Fetches all Cards
            setUpCards();
            //SET UP GUI
            setUpGUI();  
        } catch (Exception ex) {
            Logger.getLogger(FlairEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setUpCards() throws Exception{ 
        MasterDeck deck = new MasterDeck();
        DeckHandler handler = new DeckHandler(assetManager),temp;           
        final SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();

        temp=handler.retrieveAllUsableSeries();
        saxParser.parse(new FileInputStream("assets/Deck/SeriesList.xml"),temp);
        String[] series = (String[]) temp.getParameters();
        temp = handler.retrieveAllCards(audioRenderer);
        for(String s:series){
            saxParser.parse(new FileInputStream("assets/Deck/"+s+".xml"),temp); 
            deck.putAll((HashMap<String,Card>)temp.getParameters());
         } 

        FlairDirector.setApp(this);
        FlairDirector.setSpatial(spat);
        FlairDirector.setDeck(deck);
    }
    
    public void setUpGUI() throws Exception{        
       guiNode.detachAllChildren();
            
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
        assetManager, inputManager, audioRenderer, guiViewPort);
        /** Create a new NiftyGUI object */
        Nifty nifty = niftyDisplay.getNifty(); 
        String location = "com/tools/flairEditor/gui/FlairEditor.xml";
        //nifty.validateXml(location);
        /** Read your XML and initialize your custom ScreenController */
        nifty.fromXml(location, "start");
        // attach the Nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);        
    }

    
    public void setUp() throws Exception{  
            //SET UP SPATIAL TO BE CONTROLLED
            spat = assetManager.loadModel("Models/Alex/Alex.j3o");
            getRootNode().attachChild(spat);
            // disable the fly cam in lou of chase camera
            flyCam.setEnabled(false);
            ChaseCamera cCam = new ChaseCamera(getCamera(),spat,getInputManager());
            cCam.setDefaultDistance(5);
            cCam.setDefaultHorizontalRotation(FastMath.PI);
            cCam.setLookAtOffset(new Vector3f(0,0,1));
            cCam.setDragToRotate(true);
            this.getInputManager().clearMappings();
            //GIVE ALL ANIMATIONS TO SPATIAL 
            SpectreHandler handler = new SpectreHandler(assetManager),temp;           
            final SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            
            temp=handler.retrieveAllAnimations();
            saxParser.parse(new FileInputStream("assets/Models/Animations/Animations.xml"),temp);
                        
            //Gives Character all moves
            ArrayList<AnimData> animData = (ArrayList<AnimData>) ((Object[])temp.getParameters())[0];            
            AnimControl ctrl = spat.getControl(AnimControl.class);
            for (AnimData data : animData){
                for(BoneAnimation anim :data.anims){
                    anim.getName();
                    ctrl.addAnim(anim); 
                    FlairDirector.addAnimation(anim.getName());
                }
            }
            Skeleton skeleton = ctrl.getSkeleton();
            for (int i = 0; i < skeleton.getBoneCount(); i++){
                FlairDirector.addBone(skeleton.getBone(i).getName());
            }
            FlairDirector.setAnimChannel(ctrl.createChannel());
                
            //SET UP LIGHTS
            dl = new DirectionalLight();
            dl.setDirection(cam.getDirection());
            getRootNode().addLight(dl);

            cam.setViewPort(  0.5f, 1.0f, 0.05f, 0.55f);
            viewPort.setBackgroundColor(new ColorRGBA(.7f,.9f,.9f,1f));  
    }    
    @Override
    public void simpleUpdate(float tpf){
        dl.setDirection(cam.getDirection());       
    }
}