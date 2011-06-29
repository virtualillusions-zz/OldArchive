/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tools.flairEditor.gui;

import com.spectre.deck.CardSetter;

import com.spectre.deck.MasterDeck;
import com.spectre.util.Attribute;
import com.tools.flairEditor.FlairDirector;

import com.tools.flairEditor.sTax.CardXmlExporter;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;
import de.lessvoid.nifty.controls.scrollpanel.builder.ScrollPanelBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kyle Williams
 */
public class FlairEditorGUIController implements ScreenController{


    public void bind(Nifty nifty, Screen screen) { 
        this.nifty=nifty;
        this.screen=screen;
        initialize();
        screen.layoutLayers();
    }
    
    public void initialize(){
         this.cardExporter = new CardXmlExporter();

        //must be done in this sequence
        screen.findElementByName("title").getRenderer(TextRenderer.class).setText("VIRTUAL ILLUSION'S FLAIR EDITOR TOOL version 0.1_BETA");
        
        //BUILDS A CUSTUM LISTBOX TO PROPERLY FIT THE PANEL
        new ListBoxBuilder("ListOfSeries"){{
            width(percentage(100));
            hideHorizontalScrollbar();
            displayItems(nifty.getRenderEngine().getHeight()/56);
            selectionModeSingle();
            set("forceSelection","true");
        }}.build(nifty, screen, screen.findElementByName("LOS"));        
        //BUILDS A CUSTOM LISTBOX TO PROPERLY FIT THE PANEL
        new ListBoxBuilder("ListOfCards"){{
            width(percentage(100));
            hideHorizontalScrollbar();
            displayItems(nifty.getRenderEngine().getHeight()/63);
            selectionModeSingle();
            set("forceSelection","true");
        }}.build(nifty, screen, screen.findElementByName("LOCS")); 
        //Builds a Custom Scroll Panel for a List of Card Attributes
        new ScrollPanelBuilder("ScrollBuilder") {{
            width(percentage(90));
            height(percentage(90));
            this.paddingLeft("10%");
            //set("horizontal", "false");
            childLayoutVertical(); 
            panel(new PanelBuilder(""){{
                width(percentage(100));
                height(percentage(100));
                paddingLeft(percentage(30));
                childLayoutVertical(); 
                control(new LabelBuilder("ListOfAttributes","List of Card Attributes"){{
                    width(percentage(100));
                    height(percentage(100));
                    textHAlignLeft();
                    textVAlignCenter();
                }});
            }});
        }}.build(nifty, screen, screen.findElementByName("LOA"));    
        
        LOS  = screen.findElementByName("LOS").findNiftyControl("ListOfSeries",ListBox.class);
        refreshLOS();
        LOCS = screen.findElementByName("LOCS").findNiftyControl("ListOfCards",ListBox.class);        
        LOA  = screen.findElementByName("LOA").findNiftyControl("ListOfAttributes",Label.class); 
        this.archive=FlairDirector.getArchiveList();
        this.deck=FlairDirector.getDeck();        
        refresh();
    }

    @NiftyEventSubscriber(id="ListOfSeries")
    public void onListOfSeriesSelectionChanged(String id, ListBoxSelectionChangedEvent changed){refreshLOCS();}
    @NiftyEventSubscriber(id="ListOfCards")
    public void onListOfCardsInSeriesSelectionChanged(String id, ListBoxSelectionChangedEvent changed){refreshLOA();}
    //ADD NEW CLAUSE
    @NiftyEventSubscriber(id="addNew")
    public void onAddNewButtonClicked(String id, ButtonClickedEvent event){
        Element p = this.nifty.createPopupWithId("Popup", "Popup");   
        p.findElementByName("mainContainer").setConstraintHeight(new SizeValue("175px"));
        //Controls
        p.findElementByName("text").getRenderer(TextRenderer.class).setText("Type the Name of the New Card:");
        TextFieldBuilder t = new TextFieldBuilder("textField") {{
            width(percentage(100));
        }};
        t.build(nifty, screen, p.findElementByName("controls"));
        new PanelBuilder("Preset") {{
              childLayoutHorizontal();
              paddingTop("10px");
              width(percentage(100));
              control(new LabelBuilder("preset", "PreSet:"));
              control(new DropDownBuilder("presetDD") {{
                    width("*"); 
                    this.paddingLeft("10px");
                }});              
        }}.build(nifty, screen, p.findElementByName("controls"));
        DropDown dd = p.findNiftyControl("presetDD", DropDown.class);
        dd.addItem("new card");
        dd.addAllItems(deck.filterCardNamesBySeries(LOS.getSelection().get(0)));
        //BUTTONS
        new ButtonBuilder("okButton", "OK"){{
            width(percentage(50));
            interactOnClick("addNew(yes)");
        }}.build(nifty, screen, p.findElementByName("buttons"));
        new ButtonBuilder("cancelButton", "Cancel"){{
            width(percentage(50));
            interactOnClick("addNew(no)");
        }}.build(nifty, screen, p.findElementByName("buttons"));
        //Execute
        this.nifty.showPopup(this.screen, "Popup", null);
    }
    /**
     * Adds a New Card to the deck by button press
     * NIFTY CALL
     * @param addNew 
     */
    public void addNew(final String addNew) {
        final String name = nifty.findPopupByName("Popup").findNiftyControl("textField", TextField.class).getText();
        this.nifty.closePopup("Popup", new EndNotify() {
            public void perform() {
                if ("yes".equals(addNew)) { 
                        
                    final String k = (String) nifty.findPopupByName("Popup").findNiftyControl("presetDD", DropDown.class).getSelection();
                    
                    if(name.equals("")||deck.containsKey(name)){cardCreationAlert();return;}
                    //TODO:Properly Set Default or Preset Cards
                    CardSetter card = new CardSetter();
                    if(!k.equals("new card")){
                        card=(CardSetter) deck.get(k).clone();
                    }
                    //CardSetter card = new CardSetter();
                    card.setSeries(LOS.getSelection().get(0).toString());
                    card.setName(name);
                    ((EditScreenController)nifty.getScreen("editScreen").getScreenController()).setCard(card);
                    nifty.gotoScreen("editScreen");
                    nifty.setAlternateKey("fade");                    
                }
            }
        });
    }
    
    public void cardCreationAlert(){
        Element p = this.nifty.createPopupWithId("Popup", "Popup");   
        p.findElementByName("mainContainer").setConstraintHeight(new SizeValue("100px"));
        //Controls
        p.findElementByName("text").getRenderer(TextRenderer.class).setText("NoName/DuplicateName Alert:");
        
        new ButtonBuilder("closeButton", "Close"){{
            width(percentage(100));
            interactOnClick("closePopup()");
        }}.build(nifty, screen, p.findElementByName("buttons"));
        //Execute
        this.nifty.showPopup(this.screen, "Popup", null);
    }
    public void closePopup() {
        this.nifty.closePopup("Popup", new EndNotify() {
            public void perform() {nifty.setAlternateKey("fade");onAddNewButtonClicked("",null);}
        });
    }
    
    //EDIT CLAUSE
    @NiftyEventSubscriber(id="edit")
     public void onEditButtonClicked(String id, ButtonClickedEvent event){
        if(LOCS.getSelection().size()<=0)return;
        CardSetter card = (CardSetter) deck.get(LOCS.getSelection().get(0));
        ((EditScreenController)nifty.getScreen("editScreen").getScreenController()).setCard(card);
        nifty.gotoScreen("editScreen"); 
    }
    //DELETE CLAUSE
    @NiftyEventSubscriber(id="delete")
    public void onDeleteButtonClicked(String id, ButtonClickedEvent event){
        if(LOCS.getSelection().size()<=0)return;
        String name = null;
        try{name = LOCS.getSelection().get(0);}
        catch(Exception e){Logger.getAnonymousLogger().log(Level.SEVERE, "ERROR: No Such Card {0}", e.getMessage()); }
        if(name!=null){
            Element p = this.nifty.createPopupWithId("Popup", "Popup");   
            p.findElementByName("mainContainer").setConstraintHeight(new SizeValue("125px"));
            //Controls
            p.findElementByName("text").getRenderer(TextRenderer.class).setText("Are You Sure You Want to Delete: "+name);
            //BUTTONS
            new ButtonBuilder("yesButton", "Yes"){{
                width(percentage(50));
                interactOnClick("delete(yes)");
            }}.build(nifty, screen, p.findElementByName("buttons"));
            new ButtonBuilder("noButton", "No"){{
                width(percentage(50));
                interactOnClick("delete(no)");
            }}.build(nifty, screen, p.findElementByName("buttons"));
            //Execute
            this.nifty.showPopup(this.screen, "Popup", null);
        }
    }   
     /**
     * Deletes A Card to the deck by button press
     * NIFTY CALL
     * @param addNew 
     */
    public void delete(final String addNew) {
        this.nifty.closePopup("Popup", new EndNotify() {
            public void perform() {
                if ("yes".equals(addNew)) {
                    deck.remove(LOCS.getSelection().get(0));
                    refresh();
                    nifty.setAlternateKey("fade");                    
                }
            }
        });
    }
    //SYNC CLAUSE
    @NiftyEventSubscriber(id="sync")
    public void onSyncButtonClicked(String id, ButtonClickedEvent event){cardExporter.export(deck);}
    //RESTORE CLAUSE NOT FULLY IMPLIMENTED
    @NiftyEventSubscriber(id="restore")
    public void onRestoreButtonClicked(String id, ButtonClickedEvent event){
        Element p = this.nifty.createPopupWithId("Popup", "Popup");   
        p.findElementByName("mainContainer").setConstraintHeight(new SizeValue("125px"));
        //Controls
        p.findElementByName("text").getRenderer(TextRenderer.class).setText("Please Select An Archive: ");
        p.findElementByName("text").getRenderer(TextRenderer.class).setText("Currently This doesn't do anything\nleave me the hell alone:\n ");  

        new DropDownBuilder("dropDown") {{
          width("*");          
        }}.build(nifty, screen, p.findElementByName("text").getParent());
        DropDown d = p.findNiftyControl("dropDown", DropDown.class);
        d.addAllItems(archive);
        //BUTTONS
        new ButtonBuilder("okButton", "Ok"){{
            width(percentage(50));
            interactOnClick("restore(yes)");
        }}.build(nifty, screen, p.findElementByName("buttons"));
        new ButtonBuilder("cancelnButton", "Cancel"){{
            width(percentage(50));
            interactOnClick("restore(no)");
        }}.build(nifty, screen, p.findElementByName("buttons"));
        //Execute
        this.nifty.showPopup(this.screen, "Popup", null);
    }
    /**
     * Confirms Restoration of a previous Card List
     * NIFTY CALL
     * @param restore 
     */
    public void restore(final String restore) {
        //GETS THE SELECTION to Restore
        final String m = (String) nifty.findPopupByName("Popup").findNiftyControl("dropDown",DropDown.class).getSelection();
        this.nifty.closePopup("Popup", new EndNotify() {
            public void perform() {                
                if ("yes".equals(restore)) {
                    nifty.setAlternateKey("fade");  
                    //CONFIRMATION
                    Element p = nifty.createPopupWithId("Popup", "Popup");   
                    p.findElementByName("mainContainer").setConstraintHeight(new SizeValue("115px"));
                    //Controls
                    p.findElementByName("text").getRenderer(TextRenderer.class).setText("Are You Sure You Want To Restore:\n "+m);                    
                    //BUTTONS
                    new ButtonBuilder("okButton", "Ok"){{
                        width(percentage(50));
                        interactOnClick(m);
                    }}.build(nifty, screen, p.findElementByName("buttons"));
                    new ButtonBuilder("cancelnoButton", "Cancel"){{
                        width(percentage(50));
                        interactOnClick("restoreTwo(no)");
                    }}.build(nifty, screen, p.findElementByName("buttons"));
                    //Execute
                    nifty.showPopup(screen, "Popup", null);                    
                }
            }
        });
    }
    /**
     * Restores a previous Card List
     * NIFTY CALL
     * @param addNew 
     */
    public void restoreTwo(final String restore) {
        this.nifty.closePopup("Popup", new EndNotify() {
            public void perform() {
                if (!"no".equals(restore)) {
                    nifty.setAlternateKey("fade");  
                    //TODO:         
                }
            }
        });
    }
    
    //EXIT CLAUSE
    @NiftyEventSubscriber(id="exit")
    public void onExitButtonClicked(String id, ButtonClickedEvent event){
        Element p = this.nifty.createPopupWithId("Popup", "Popup");   
        p.findElementByName("mainContainer").setConstraintHeight(new SizeValue("100px"));
        //Controls
        p.findElementByName("text").getRenderer(TextRenderer.class).setText("Exit Program:");
        //BUTTONS
        new ButtonBuilder("yesButton", "Yes"){{
            width(percentage(50));
            interactOnClick("exitProgram(yes)");
        }}.build(nifty, screen, p.findElementByName("buttons"));
        new ButtonBuilder("noButton", "No"){{
            width(percentage(50));
            interactOnClick("exitProgram(no)");
        }}.build(nifty, screen, p.findElementByName("buttons"));
        //EXECUTE
        this.nifty.showPopup(this.screen, "Popup", null);
    }
    /**
     * Exits The Program
     * NIFTY CALL
     * @param exit
     */
    public void exitProgram(final String exit) {
        this.nifty.closePopup("Popup", new EndNotify() {
            public void perform() {
                if ("yes".equals(exit)) {
                    nifty.setAlternateKey("fade"); 
                    nifty.exit();
                    FlairDirector.getApp().stop();
                }
            }
        });
    }
    /**
     * Refresh Series and Attributes Windows
     */
    public void refresh(){
        refreshLOCS();
        refreshLOA();
    }
    private void refreshLOS(){
        for(Attribute.CardSeries c:Attribute.CardSeries.values()){
            LOS.addItem(c);
        }   
        LOS.sortAllItems(Attribute.lexiSortSeries);
        LOS.selectItemByIndex(0);     
    }
    private void refreshLOCS(){
        if(LOCS.getItems().size()>0)LOCS.clear();
        Attribute.CardSeries temp = LOS.getSelection().get(0);
        LOCS.addAllItems(deck.filterCardNamesBySeries(temp));
        LOCS.sortAllItems();
        LOCS.selectItemByIndex(0);        
    }
    
    private void refreshLOA(){    
        if(LOCS.getSelection().size()>0){
            String card = LOCS.getSelection().get(0).toString();    
            String txt = card!=null?deck.get(card).toString():"List of Card Attributes";
            LOA.setText(txt);
        }
    }

    @Override
    public void onStartScreen() {refresh();}    
    @Override
    public void onEndScreen() {}
    private ArrayList<String> archive;
    private MasterDeck deck;
    private ListBox<Attribute.CardSeries> LOS;
    private ListBox<String> LOCS;
    private Label LOA;
    private Nifty nifty;
    private Screen screen;
    private CardXmlExporter cardExporter;
}
