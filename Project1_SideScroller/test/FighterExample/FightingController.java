/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FighterExample;

import com.jme3.animation.AnimChannel;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.input.binding.BindingListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.scene.control.ControlType;
import java.io.IOException;

/**
 *
 * @author Kyle Williams
 */
public class FightingController implements Control,BindingListener{
 public static void main(String[] args){
        SimpleFighting app = new SimpleFighting();
        app.start();
    }
    private Node fighter;
    public FightingController(Node model, AnimChannel animControl) {
        fighter = model;
       
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ControlType getType() {
        return ControlType.VertexAnimation;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * If enabled then playerHasControl
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEnabled() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(float tpf) {
        //throw new UnsupportedOperationException("Not supported yet.");
    System.out.println("rr");
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onBinding(String binding, float value) {
         if (binding.equals("Left")){
            fighter.move(0, 0,-.01f);
        }else if (binding.equals("Right")){
            fighter.move(0, 0, .01f);
        }else if (binding.equals("Jump")){
            fighter.move(0, .01f, 0);
        }
    }
}
