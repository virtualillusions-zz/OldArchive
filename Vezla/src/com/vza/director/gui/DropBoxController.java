package com.vza.director.gui;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.dropdown.controller.DropDownControl;
import de.lessvoid.nifty.controls.dropdown.controller.DropDownControlNotify;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties; 
  
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kyle Williams
 */

    public class DropBoxController implements Controller{
        private Screen screen;
        @Override
        public void bind(Nifty nifty, Screen screen, Element elmnt, Properties prprts, ControllerEventListener cl, Attributes atrbts) {
        this.screen=screen;
        }

        @Override
        public void onStartScreen() {
            System.out.println("work");
                  new Components();
        }

        @Override
        public void onFocus(boolean bln) {

        }

        @Override
        public void inputEvent(NiftyInputEvent nie) {

        }


    public class Components implements DropDownControlNotify{

        public Components(){
          setupComponent("resDDC");
          setupComponent("bitDDC");
          setupComponent("refDDC");
          setupComponent("renDDC");
          setupComponent("antDDC");
        }
        private DropDownControl findDropDownControl(final String id) {
            DropDownControl dropDown1 = screen.findControl(id, DropDownControl.class);
            return dropDown1;
        }
        @Override
        public void dropDownSelectionChanged(DropDownControl ddc) {
          //  throw new UnsupportedOperationException("Not supported yet.");
        }

        private void setupComponent(String r){
            DropDownControl DDC = findDropDownControl(r);
            if (DDC != null) {
                DDC.addNotify(this);
                DDC.addItem("Nifty GUI");
                DDC.addItem("Slick2d");
                DDC.addItem("Lwjgl");
                DDC.setSelectedItemIdx(0);
            }
        }

    }
}
