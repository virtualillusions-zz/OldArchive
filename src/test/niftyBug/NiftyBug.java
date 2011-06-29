/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.niftyBug;

import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.Scrollbar;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.SliderChangedEvent;

import de.lessvoid.nifty.controls.checkbox.builder.CheckboxBuilder;
import de.lessvoid.nifty.controls.scrollbar.builder.ScrollbarBuilder;
import de.lessvoid.nifty.controls.slider.builder.SliderBuilder;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.impl.Gradient;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;

/**
 *
 * @author Kyle Williams
 */
public class NiftyBug extends com.jme3.app.SimpleApplication implements de.lessvoid.nifty.screen.ScreenController{
    
    public static void main(String[] args){
        new NiftyBug().start();
    }

    @Override
    public void simpleInitApp() {   
        flyCam.setDragToRotate(true);
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        /** Create a new NiftyGUI object */
        Nifty nifty = niftyDisplay.getNifty(); 
        String location = "test/niftyBug/newNiftyGui.xml";
        //nifty.validateXml(location);
        /** Read your XML and initialize your custom ScreenController */
        nifty.fromXml(location, "start");
        // attach the Nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay); 
    }

    public void bind(Nifty nifty, Screen screen) {
       
        innerPanel = new PanelBuilder("innerPanel"){{
        childLayoutHorizontal();
        width(percentage(75));height(percentage(75));
        backgroundColor("#f60f");
        alignCenter();valignCenter();
        padding(percentage(10));
        control(new SliderBuilder("slider",true));
        control(new SliderBuilder("scrollbar", true));
        control(new CheckboxBuilder("checkbox"));
        }}.build(nifty, screen, screen.findElementByName("panel"));
       
        screen.layoutLayers();

        innerPanel.hide();
        slider = screen.findNiftyControl("slider", Slider.class);
        checkbox= screen.findNiftyControl("checkbox", CheckBox.class);
        scrollbar = screen.findNiftyControl("scrollbar",Slider.class);
        //innerPanel.show();
        slider.setValue(0);
        scrollbar.setValue(100);
        checkbox.setChecked(true);
        
        innerPanel.show();
    }
    
    public void onStartScreen() {
        
    }

    public void onEndScreen() {
        
    }
    
    private Element innerPanel;
    private Slider slider;
    private Slider scrollbar;
    private CheckBox checkbox;
        
}
