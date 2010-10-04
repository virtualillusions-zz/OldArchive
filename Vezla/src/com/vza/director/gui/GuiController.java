package com.vza.director.gui;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package GUIExample;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.CustomControlCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.List;

/**
 *
 * @author Kyle Williams
 */
public class GuiController implements ScreenController{
 private Nifty nifty;
 private Screen screen;
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        // nifty.gotoScreen("start");
    }

    @Override
    public void onEndScreen() {
        System.out.println("ChangingScreen");
        //System.exit(0);
    }

    public void startGame(){
        com.vza.director.Director.getApp().getAppStateManager().attach(new com.vza.gameState.TestGameState());
    }
    public void goToScreen(String toScreen){
        if(toScreen.equalsIgnoreCase("quit")){
            nifty.createPopup("popupExit");
            nifty.showPopup(screen, "popupExit", null);
            System.out.println("is being acknowledged");
        }
        else nifty.gotoScreen(toScreen);
    }

  public void optionsAddTab(String addTab, String addTo) {
     List<Element> elements = screen.findElementByName(addTo).getElements();
        if(!elements.get(0).getId().equalsIgnoreCase(addTab)){
            if (!elements.isEmpty()) {
              nifty.removeElement(screen, elements.get(0));
            }
            CustomControlCreator createMultiplayerPanel = new CustomControlCreator(addTab, addTab);
            createMultiplayerPanel.create(nifty, screen, screen.findElementByName(addTo));
       }
  }


      /**
   * popupExit.
   * @param exit exit string
   */
  public void popupExit(final String exit) {
    this.nifty.closePopup("popupExit", new EndNotify() {
      public void perform() {
        if ("yes".equals(exit)) {
          nifty.setAlternateKey("fade");
          nifty.exit();
          com.vza.director.Director.getApp().stop();
        }
      }
    }
    );
  }
}