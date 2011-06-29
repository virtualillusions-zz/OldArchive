/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.guiPosition;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;

/**
 *
 * @author Kyle Williams
 */
public class GuiScreenController implements ScreenController{
        private Screen screen;
        private Element mainLayer;

        @Override
        public void bind(Nifty nifty, Screen screen) {
            this.screen=screen;
            mainLayer = screen.findElementByName("main");

            System.out.println("HELLO, WORLD!");
        }
        public void updatePosition(SizeValue newX,SizeValue newY,SizeValue newHeight,SizeValue newWidth){
            mainLayer.setConstraintHeight(newHeight);
            mainLayer.setConstraintWidth(newWidth);
            mainLayer.setConstraintX(newX);
            mainLayer.setConstraintY(newY);
            screen.layoutLayers();
        }

        @Override
        public void onStartScreen() {}
        @Override
        public void onEndScreen() {}
}
