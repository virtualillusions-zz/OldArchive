/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vi.machello.scene;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.vi.machello.app.state.BaseAppState;
import com.vi.machello.scene.dynamics.control.DynamicPhysicsControl;
import physics.PhysicsTestHelper;

/**
 *
 * @author Kyle D. Williams
 */
public class TestDynamicPhysicsControl extends BaseAppState implements ActionListener, AnalogListener {

    public Node spat;
    public Node root;
    public CameraNode cn;
    public DynamicPhysicsControl dpc;
    public Node world;
    public boolean toggleCam;
    public boolean toggleScale;
    public DynamicPhysicsControl[] dpcs;
    public PhysicsSpace space;

    @Override
    protected void AppState(Application app) {
        BulletAppState bullet = new BulletAppState();
        app.getStateManager().attach(bullet);
        space = bullet.getPhysicsSpace();
        AssetManager assets = app.getAssetManager();
        root = getCoreState().getRootNode();
        Camera cam = app.getCamera();
        InputManager input = app.getInputManager();
        dpcs = new DynamicPhysicsControl[2];
        toggleCam = true;
        toggleScale = true;
        //Level 
        world = new Node("world");
        PhysicsTestHelper.createPhysicsTown(world, assets, space);
        //Character
        spat = (Node) assets.loadModel("testData/Jaime/Jaime.j3o");
        //CameraNode 
        cn = new CameraNode("CamNode", cam);
        root.attachChild(cn);
        cn.setLocalTranslation(0, 100, 0);
        cn.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        //Attach
        root.attachChild(world);
        root.attachChild(spat);
        //        
        input.addMapping("Forward",
                new KeyTrigger(KeyInput.KEY_W));
        input.addMapping("Back",
                new KeyTrigger(KeyInput.KEY_S));
        input.addMapping("Left",
                new KeyTrigger(KeyInput.KEY_A));
        input.addMapping("Right",
                new KeyTrigger(KeyInput.KEY_D));
        input.addMapping("Run",
                new KeyTrigger(KeyInput.KEY_LSHIFT));
        input.addMapping("Jump",
                new KeyTrigger(KeyInput.KEY_SPACE));
        input.addMapping("Duck",
                new KeyTrigger(KeyInput.KEY_C));
        input.addMapping("Dash",
                new KeyTrigger(KeyInput.KEY_I));
        input.addMapping("Evade",
                new KeyTrigger(KeyInput.KEY_K));
        input.addMapping("EvadeLeft",
                new KeyTrigger(KeyInput.KEY_J));
        input.addMapping("EvadeRight",
                new KeyTrigger(KeyInput.KEY_L));
        input.addMapping("Reset",
                new KeyTrigger(KeyInput.KEY_R));
        input.addMapping("toggleCam",
                new KeyTrigger(KeyInput.KEY_T));
        input.addMapping("toggleScale",
                new KeyTrigger(KeyInput.KEY_H));
        input.addListener(this,
                "Left", "Right", "Forward", "Back",
                "Run", "Jump", "Duck", "Dash", "Evade",
                "EvadeLeft", "EvadeRight", "Reset",
                "toggleCam", "toggleScale");
        onAction("toggleCam", true, 0f);
        onAction("toggleScale", true, 0f);
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("Run")) {
            dpc.setRun(isPressed);
        } else if (name.equals("Duck")) {
            dpc.actionDuck(isPressed);
        } else if (isPressed == true) {
            if (name.equals("Jump")) {
                dpc.actionJump();
            } else if (name.equals("Dash")) {
                dpc.actionDash();
            } else if (name.equals("Evade")) {
                dpc.actionEvade();
            } else if (name.equals("EvadeLeft")) {
                dpc.actionEvadeLeft();
            } else if (name.equals("EvadeRight")) {
                dpc.actionEvadeRight();
            } else if (name.equals("Reset")) {
                dpc.clearForces();
                dpc.setPhysicsLocation(Vector3f.ZERO.add(0, 1f, 0));
            } else if (name.equals("toggleCam")) {
                if (toggleCam) {
                    spat.attachChild(cn);
                    cn.setLocalTranslation(0f, .4f, -4);
                    cn.lookAt(spat.getLocalTranslation(), Vector3f.UNIT_Y);
                } else {
                    root.attachChild(cn);
                    float y = !toggleScale ? 50f : 5f;
                    cn.setLocalTranslation(0, y, 0);
                    cn.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
                }
                toggleCam = !toggleCam;
            } else if (name.equals("toggleScale")) {
                if (toggleScale) {
                    spat.setLocalScale(5f);
                    if (dpcs[0] == null) {
                        dpcs[0] = newDPC();
                    }
                    if (dpcs[1] != null) {
                        dpcs[1].setEnabled(false);
                    }
                    dpc = dpcs[0];
                    dpcs[0].setEnabled(true);
                } else {
                    spat.setLocalScale(1f);
                    if (dpcs[1] == null) {
                        dpcs[1] = newDPC();
                    }
                    if (dpcs[0] != null) {
                        dpcs[0].setEnabled(false);
                    }
                    dpc = dpcs[1];
                    dpcs[1].setEnabled(true);
                }
                //
                toggleScale = !toggleScale;
                //adjustcam
                toggleCam = !toggleCam;//set so can be same but adjusted
                onAction("toggleCam", true, 0f);
            }
        }
    }

    public DynamicPhysicsControl newDPC() {
        DynamicPhysicsControl d = new DynamicPhysicsControl(space);
        d.setWalkDirection(Vector3f.UNIT_Z);
        spat.addControl(d);
        return d;
    }

    public void onAnalog(String name, float value, float tpf) {
        float i = 2 * 100f;
        tpf=4;//don't use tpf with physics has own framerate
        if (name.equals("Forward")) {
            dpc.actionForward(i * tpf);
        } else if (name.equals("Back")) {
            dpc.actionForward(-i * tpf);
        } else if (name.equals("Left")) {
            dpc.actionLeft(i * tpf);
        } else if (name.equals("Right")) {
            dpc.actionLeft(-i * tpf);
        }
    }

    @Override
    protected void appUpdate(float tpf) {
        super.appUpdate(tpf);
    }

    @Override
    protected void cleanUp() {
    }

    public static void main(String[] args) {
        Application app = new SimpleApplication(
                new TestDynamicPhysicsControl()) {
            @Override
            public void simpleInitApp() {
                //this.flyCam.setMoveSpeed(100f);
                /**
                 * A white ambient light source.
                 */
                AmbientLight ambient = new AmbientLight();
                ambient.setColor(ColorRGBA.White);
                getRootNode().addLight(ambient);
            }
        };
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
        settings.putBoolean("DebugMode", true);
        app.setSettings(settings);
        app.start();
    }
}
