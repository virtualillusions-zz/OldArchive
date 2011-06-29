/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.etc;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.system.AppSettings;

/**
 *
 * @author Kyle Williams
 */
public class TestInput extends SimpleApplication implements RawInputListener{
    public static void main(String[] args){
        TestInput app = new TestInput(); 
        AppSettings settings = new AppSettings(true);
        settings.setUseJoysticks(true);
        app.setSettings(settings);
        app.start();}
    BitmapText fpsText2;
    @Override
    public void simpleInitApp() {
        this.getInputManager().addRawInputListener(this);
        fpsText2 = new BitmapText(guiFont, false);
        fpsText2.setLocalTranslation(settings.getWidth()/2-fpsText2.getLineWidth(), settings.getHeight()/2+fpsText2.getLineHeight(), 0);
        fpsText2.setText("[BUTTON INDEX]");
        guiNode.attachChild(fpsText2);
    }

    public void beginInput() {
    }

    public void endInput() {
    }

    public void onJoyAxisEvent(JoyAxisEvent evt) {
         fpsText2.setText("JoyStick Axis Event \nAxis Index:"+evt.getAxisIndex()+" JoyIndex:"+evt.getJoyIndex());
    }

    public void onJoyButtonEvent(JoyButtonEvent evt) {
         fpsText2.setText("JoyButton Event\nButton Index:"+evt.getButtonIndex()+" JoyIndex:"+evt.getJoyIndex());
    }

    public void onMouseMotionEvent(MouseMotionEvent evt) {
         fpsText2.setText("MouseMotion Event\nDeltaWheel "+evt.getDeltaWheel()+"\nDX:"+evt.getDX()+" DY:"+evt.getDY()+"\nX:"+evt.getX()+" Y:"+evt.getY());
    }

    public void onMouseButtonEvent(MouseButtonEvent evt) {
         fpsText2.setText("MouseButton Event\nMouseButton Event"+evt.getButtonIndex()+"\nX:"+evt.getX()+ " Y:"+evt.getY());
    }

    public void onKeyEvent(KeyInputEvent evt) {
        fpsText2.setText("KeyInputEvent Event\nKeyChar:"+evt.getKeyChar()+"\nKeyCode:"+evt.getKeyCode());
    }

    public void onTouchEvent(TouchEvent evt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
