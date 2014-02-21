package com;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Kyle D. Williams
 */
public class CollisionShapeBug extends AbstractAppState implements ActionListener {

    public RigidBodyControl rbc;
    public CollisionShape shape1;
    public CollisionShape shape2;
    public boolean switchShape;
    public Camera cam;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        SimpleApplication sApp = (SimpleApplication) app;
        InputManager input = app.getInputManager();
        AssetManager assetManager = sApp.getAssetManager();
        Node rootNode = sApp.getRootNode();
        BulletAppState bullet = new BulletAppState();
        stateManager.attach(bullet);
        bullet.setDebugEnabled(true);
        PhysicsSpace space = bullet.getPhysicsSpace();


        shape1 = new BoxCollisionShape(new Vector3f(1.0f, 1.0f, 1.0f));
        shape2 = new BoxCollisionShape(new Vector3f(0.5f, 0.5f, 0.5f));

        Box box = new Box(Vector3f.ZERO, 1f, 1f, 1f);
        Geometry cube = new Geometry("Bleed-through color cube", box);
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Green);
        cube.setMaterial(material);
        rbc = new RigidBodyControl(shape1);//let it drop so can easier show change

        Box floorBox = new Box(20, 0.25f, 20);
        Geometry floorGeometry = new Geometry("Floor", floorBox);
        Material material2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material2.setColor("Color", ColorRGBA.Pink);
        floorGeometry.setMaterial(material2);
        floorGeometry.setLocalTranslation(0, -0.25f, 0);


        floorGeometry.addControl(new RigidBodyControl(0));
        cube.addControl(rbc);

        rootNode.attachChild(floorGeometry);
        rootNode.attachChild(cube);

        space.add(floorGeometry);
        space.add(rbc);


        cam = app.getCamera();
        cam.setLocation(Vector3f.UNIT_XYZ.mult(7f));

        input.addMapping("BUGGED_SWITCH",
                new KeyTrigger(KeyInput.KEY_SPACE));
        input.addMapping("Switch",
                new KeyTrigger(KeyInput.KEY_RETURN));
        input.addListener(this, "BUGGED_SWITCH", "Switch");
        switchShape = false;
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed == true) {
            CollisionShape cs = null;
            if (name.equals("BUGGED_SWITCH")) {
                cs = switchShape ? shape1 : shape2;
            } else if (name.equals("Switch")) {
                float f = switchShape ? 1.0f : 0.5f;
                //uncomment below and comment the line after
                //cs = new BoxCollisionShape(new Vector3f(f, f, f));
                cs = new BoxCollisionShape(new Vector3f(1, f, 1));
            }
            rbc.setCollisionShape(cs);
            switchShape = !switchShape;
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        cam.lookAt(rbc.getPhysicsLocation(), Vector3f.UNIT_Y);
    }

    public static void main(String[] args) {
        Application app = new SimpleApplication(
                new FlyCamAppState(),
                new CollisionShapeBug()) {
            @Override
            public void simpleInitApp() {
                this.flyCam.setMoveSpeed(30f);
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
