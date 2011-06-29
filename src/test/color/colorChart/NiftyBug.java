/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.color.colorChart;

import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;

import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;

import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.screen.Screen;

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
        String location = "test/color/colorChart/newNiftyGui.xml";
        //nifty.validateXml(location);
        /** Read your XML and initialize your custom ScreenController */
        nifty.fromXml(location, "start");
        // attach the Nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay); 
    }

    public void bind(Nifty nifty, Screen screen) {
        this.screen=screen;
        this.nifty=nifty;
        c = screen.findElementByName("chooser").getRenderer(PanelRenderer.class);
        v = screen.findElementByName("resultingColor").getRenderer(PanelRenderer.class);
        R=screen.findNiftyControl("Red", TextField.class);
        G=screen.findNiftyControl("Green", TextField.class);
        B=screen.findNiftyControl("Blue", TextField.class);
        H=screen.findNiftyControl("Hue", TextField.class);
        S=screen.findNiftyControl("Saturation", TextField.class);
        V=screen.findNiftyControl("Value", TextField.class);
        Hex=screen.findNiftyControl("Hex", TextField.class);
        color = new Color(255,0,0);
        performingReset=false;
        reset();
    }

    @NiftyEventSubscriber(id="Red")
    public void onRedChanged(String id, TextFieldChangedEvent changed){
        if(!isParsableToInt(changed.getText()))return;        
        color.setRed(changed.getText());reset();
    } 
    @NiftyEventSubscriber(id="Green")
    public void onGreenChanged(String id, TextFieldChangedEvent changed){
        if(!isParsableToInt(changed.getText()))return;             
        color.setGreen(changed.getText());reset();
    }  
    @NiftyEventSubscriber(id="Blue")
    public void onBlueChanged(String id, TextFieldChangedEvent changed){
        if(!isParsableToInt(changed.getText()))return;     
        color.setBlue(changed.getText());reset();
    }  
    @NiftyEventSubscriber(id="Hue")
    public void onHueChanged(String id, TextFieldChangedEvent changed){
        if(!isParsableToInt(changed.getText()))return;     
        color.setHue(changed.getText());reset();
    }  
    @NiftyEventSubscriber(id="hueBar")
    public void onHueBarChanged(String id, SliderChangedEvent changed){
        H.setText(changed.getValue()+"");
    }
    @NiftyEventSubscriber(id="Saturation")
    public void onSaturationChanged(String id, TextFieldChangedEvent changed){
        if(!isParsableToInt(changed.getText()))return;     
        color.setSaturation(changed.getText());reset();
    }  
    @NiftyEventSubscriber(id="Value")
    public void onValueChanged(String id, TextFieldChangedEvent changed){
        if(!isParsableToInt(changed.getText()))return;     
        color.setValue(changed.getText());reset();
    }  
    @NiftyEventSubscriber(id="Hex")
    public void onHexChanged(String id, TextFieldChangedEvent changed){
        if(!isParsableToInt(changed.getText()))return;     
        color.setHex(changed.getText());reset();
    }
    public void reset(){
        if(performingReset==false){
            performingReset=true;
            c.setBackgroundColor(new de.lessvoid.nifty.tools.Color(color.getRed()/255.f,color.getGreen()/255.f,color.getBlue()/255.f,1f)); 
            v.setBackgroundColor(new de.lessvoid.nifty.tools.Color(color.getRed()/255.f,color.getGreen()/255.f,color.getBlue()/255.f,1f)); 
            R.setText(color.getRed()+"");
            G.setText(color.getGreen()+"");
            B.setText(color.getBlue()+"");
            H.setText(color.getHue()+"");
            S.setText(color.getSaturation()*100+"");
            V.setText(color.getValue()*100+"");
            Hex.setText(color.getHex()+"");
            performingReset=false;  
        }    
    }
    public boolean isParsableToInt(String i){
        try{
            Integer.parseInt(i);
            return true;
        }
        catch(NumberFormatException nfe){
            return false;
        }
    }
    private boolean performingReset;
    public void onStartScreen() {
        
    }

    public void onEndScreen() {
        
    }
    
    private TextField R,G,B,H,S,V,Hex;
    private Screen screen;
    private Nifty nifty;
    private PanelRenderer v,c;
    private Color color;
    
    
    
    private class Color{
        private int rgb[];private int hsv[];
        private String hex;
        public Color(int r,int g,int b){
            rgb=new int[]{r,g,b};
            hsv= new int[]{0,0,0};
            hex = 0x000000+"";
            setRed(r+"");setGreen(g+"");setBlue(b+"");           
        }
        
        public final void setRed(String r){rgb[0]=Integer.parseInt(r);              rgbTOhsv();rgbTOhex();}
        public final void setGreen(String g){rgb[1]=Integer.parseInt(g);            rgbTOhsv();rgbTOhex();}
        public final void setBlue(String b){rgb[2]=Integer.parseInt(b);             rgbTOhsv();rgbTOhex();}
        public final void setHue(String h){hsv[0]=Integer.parseInt(h);          hsvTOrgb();rgbTOhex();}
        public final void setSaturation(String s){hsv[1]=Integer.parseInt(s);   hsvTOrgb();rgbTOhex();}
        public final void setValue(String v){hsv[2]=Integer.parseInt(v);        hsvTOrgb();rgbTOhex();}  
        public final void setHex(String h){
            hex=Integer.toHexString(Integer.parseInt(h)); 
            int hex2 = Integer.parseInt(hex);
            //hex to rgb
            rgb[0] = (hex2 & 0xFF0000) >> 16;
            rgb[1] = (hex2 & 0xFF00) >> 8;
            rgb[2] = (hex2 & 0xFF);
            //rgb to hsb
            setRed(rgb[0]+"");setGreen(rgb[1]+"");setBlue(rgb[2]+"");    
        }
        
        public void hsvTOrgb(){
            float c = hsv[1]*hsv[2];
            float h1 = hsv[0]/60;
            float x= c*(1-Math.abs((h1%2)-1));
            float r=0;float g=0; float b=0;
            if(h1<1.0){r=c;g=x;b=0;}
       else if(h1<2.0){r=x;g=c;b=0;}
       else if(h1<3.0){r=0;g=c;b=x;}
       else if(h1<4.0){r=0;g=x;b=c;}
       else if(h1<5.0){r=x;g=0;b=c;}
                 else {r=c;g=0;b=x;}
            
            float m = hsv[2]-c;
            rgb[0]= (int) (r+m);
            rgb[1]= (int) (g+m);
            rgb[2]= (int) (b+m);    
            System.out.println(rgb[0]);
            System.out.println(rgb[1]);
            System.out.println(rgb[2]);
            
            //int rgb2 = java.awt.Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]);
                   //rgb[0] = (rgb2>>16)&0xFF;
            //rgb[1] = (rgb2>>8)&0xFF;
            //rgb[2] = rgb2&0xFF;
        }
        public void rgbTOhsv(){
            float[] m = new float[3];
            java.awt.Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], m);
            hsv[0] =(int) m[0];hsv[1] =(int) m[1];hsv[2] =(int) m[2];
        }
        
        public void rgbTOhex(){
            java.awt.Color c = new java.awt.Color(rgb[0],rgb[1],rgb[2]);
            hex = Integer.toHexString( c.getRGB() & 0x00ffffff );
        }
        
        
        
        
        public int getRed(){return rgb[0];}
        public int getGreen(){return rgb[1];}
        public int getBlue(){return rgb[2];}
        public int getHue(){return hsv[0];}
        public int getSaturation(){return hsv[1];}
        public int getValue(){return hsv[2];} 
        public String getHex(){return hex;}
    }   
}
