/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.guiPosition;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * @author Kyle Williams
 */
public class GUIPosition extends SimpleApplication implements ScreenController{    
    private Nifty nifty;
    private Screen screen;
    private Spatial model;
    private AmbientLight al;
    private Element mainPanel;
    private Vector3f screenCoordinates = new Vector3f();

    @Override
    public void simpleInitApp() {
        setUpNifty();
        this.getFlyByCamera().setMoveSpeed(30f);
        al = new AmbientLight();
        getRootNode().addLight(al);
        model = getAssetManager().loadModel("Models/Alex/Alex.j3o");
        getRootNode().attachChild(model);
    }        



    @Override
    public void simpleUpdate(float tpf) {        
            if(screenCoordinates.length()!=0)screenCoordinates.zero();
            getCamera().getScreenCoordinates(model.getLocalTranslation(), screenCoordinates);
            //System.out.println(screenCoordinates);
            SizeValue x = new SizeValue(((int)screenCoordinates.getX())+"%");
            SizeValue y = new SizeValue(((int)screenCoordinates.getY())+"%");
            SizeValue h = new SizeValue(((int)screenCoordinates.getZ())+"h");
            SizeValue w = new SizeValue(((int)screenCoordinates.getZ())+"w");
            for(Element layer:screen.getLayerElements()){
                updatePosition(layer,x, y, h, w);
                layer.layoutElements();
            }
            screen.layoutLayers();
    }

    public void updatePosition(Element ele, SizeValue newX,SizeValue newY,SizeValue newHeight,SizeValue newWidth){
        //ele.setConstraintHeight(newHeight);
        //ele.setConstraintWidth(newWidth);
        ele.setConstraintX(newX);
        ele.setConstraintY(newY);
        if(!ele.getElements().isEmpty()){
            for(Element elem:ele.getElements()){
                updatePosition(elem,newX, newY, newHeight, newWidth);
            }
        }
        ele.layoutElements();
    }

     protected void setUpNifty(){
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                                                      inputManager,
                                                      audioRenderer,
                                                      guiViewPort);
        this.nifty = niftyDisplay.getNifty();

        nifty.fromXml("test/guiPosition/EssenceInterface2.xml", "start");
        this.screen=nifty.getScreen("start");
        this.mainPanel=screen.findElementByName("mainPanel");

        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);
    }

    @Override
    public void bind(Nifty nifty, Screen screen) { }
    @Override
    public void onStartScreen() {}
    @Override
    public void onEndScreen() {}

    public static void main(String[] args){GUIPosition app = new GUIPosition();app.start();}
}
