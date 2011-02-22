/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.misc;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.debug.WireBox;

/**
 *
 * @author Kyle Williams
 */
public class testRotation extends SimpleApplication{
    private DirectionalLight dl;
    private Spatial model;
    private Geometry geom,geom2;

    @Override
    public void simpleInitApp() {
        setup();

        keys();

    }

    public void keys(){
        getInputManager().addMapping("Up", new KeyTrigger(KeyInput.KEY_I));
        getInputManager().addMapping("Down", new KeyTrigger(KeyInput.KEY_K));
        getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
        getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_L));
        getInputManager().addMapping("Target1", new KeyTrigger(KeyInput.KEY_O));
        getInputManager().addMapping("Target2", new KeyTrigger(KeyInput.KEY_U));
        getInputManager().addListener(buttons, "Up","Down","Left","Right","Target1","Target2");
    }
    public ActionListener buttons = new ActionListener(){
        public void onAction(String name, boolean isPressed, float tpf) {
            if(isPressed){
                if(name.equals("Target1")){
                    Vector3f trans = geom.getWorldTranslation();
                    Vector3f t = new Vector3f(trans.getX(),0,trans.getZ());
                    model.lookAt(t, Vector3f.UNIT_Y);
                } else if (name.equals("Target2")) {
                    Vector3f trans = geom2.getWorldTranslation();
                    Vector3f t = new Vector3f(trans.getX(),0,trans.getZ());
                    Quaternion q = model.getLocalRotation();
                    q.lookAt(t, Vector3f.UNIT_Y);
                    model.setLocalRotation(q);
                }else{
                    float rad = 0;
                   if (name.equals("Down")){
                        rad=0;
                    }else if(name.equals("Right")){
                        rad=90;
                    } else if(name.equals("Up")){
                        rad=180;
                    }else if(name.equals("Left")){
                        rad=270;
                    }
                    model.setLocalRotation(
                            new Quaternion().fromAngleNormalAxis(FastMath.DEG_TO_RAD*rad, Vector3f.UNIT_Y)
                            );
                }
            }
        }
    };

    @Override
    public void simpleUpdate(float tpf){
        dl.setDirection(getCamera().getDirection().normalizeLocal());
    }

    public void setup(){
        dl = new DirectionalLight();
        getRootNode().addLight(dl);
 
        putArrow(Vector3f.ZERO, Vector3f.UNIT_X, ColorRGBA.Red);
        putArrow(Vector3f.ZERO, Vector3f.UNIT_Y, ColorRGBA.Green);
        putArrow(Vector3f.ZERO, Vector3f.UNIT_Z, ColorRGBA.Blue);
        putGrid(Vector3f.ZERO, ColorRGBA.Cyan);

        model= getAssetManager().loadModel("Models/Reino/Reino.j3o");
        getRootNode().attachChild(model);
        this.getCamera().setLocation(model.getLocalTranslation().add(0,3,5));
        this.getCamera().lookAt(model.getLocalTranslation(), Vector3f.UNIT_Y);

        geom = putBox(0.5f, ColorRGBA.Yellow);
                 geom.setLocalTranslation(model.getLocalTranslation().add(2,2,-2));
        geom2 = putBox(0.5f, ColorRGBA.Orange);
                 geom2.setLocalTranslation(model.getLocalTranslation().add(-2,0,-2));

    }

    public Geometry putShape(Mesh shape, ColorRGBA color){
        Geometry g = new Geometry("shape", shape);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/WireColor.j3md");
        mat.setColor("m_Color", color);
        g.setMaterial(mat);
        rootNode.attachChild(g);
        return g;
    }

      public void putArrow(Vector3f pos, Vector3f dir, ColorRGBA color){
        Arrow arrow = new Arrow(dir);
        arrow.setLineWidth(4); // make arrow thicker
        putShape(arrow, color).setLocalTranslation(pos);
    }
    public void putGrid(Vector3f pos, ColorRGBA color){
        putShape(new Grid(10, 10, 2f), color).center().move(pos);
    }
   
    public Geometry putBox(float size, ColorRGBA color){
        return putShape(new WireBox(size, size, size), color);
    }


    public static void main(String[] args){
        testRotation app = new testRotation();
        app.start();
    }
}
