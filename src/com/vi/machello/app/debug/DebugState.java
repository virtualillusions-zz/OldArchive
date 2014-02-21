/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.app.debug;

import com.jme3.app.Application;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.font.BitmapFont;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.system.JmeContext;
import com.jme3.util.BufferUtils;
import com.vi.machello.app.state.BaseAppState;

/**
 *
 * @author Kyle D. Williams
 */
public class DebugState extends BaseAppState {

    private final String PHYS = "Physics";
    private final String STATS = "Stats";
    private final String MEM = "Memory";
    private final String EXIT = "Exit";
    private final String NODE = "Node";
    private final String GUI_NODE = "GUI_Node";
    private static PhysicsDebugAppState dps;
    private ScreenshotAppState sap;
    private static Application app;
    private AppStateManager sm;
    private InputManager input;
    private StatsAppState sAs;

    @Override
    public void AppState(final Application sAppState) {
        app = sAppState;
        sm = app.getStateManager();
        input = sAppState.getInputManager();
        final Node guiNode = getCoreState().getGuiNode();
        final Node rootNode = getCoreState().getRootNode();

        input.addMapping(MEM, new KeyTrigger(KeyInput.KEY_F1));
        if (app.getContext().getType() == JmeContext.Type.Display) {
            input.addMapping(EXIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
            input.addMapping(NODE, new KeyTrigger(KeyInput.KEY_F4));
            input.addMapping(GUI_NODE, new KeyTrigger(KeyInput.KEY_F5));
        }
        statsView(guiNode);
        screenShot();
        physicsDebug();
        //This Is the Primary Way to set Up registered KeyBindings
        input.addListener(new ActionListener() {
            @Override
            public void onAction(String binding, boolean value, float tpf) {
                if (value) {
                    if (binding.equals(EXIT)) {
                        app.stop();
                    } else if (binding.equals(MEM)) {
                        BufferUtils.printCurrentDirectMemory(null);
                    } else if (binding.equals(STATS)) {
                        sAs.toggleStats();
                    } else if (binding.equals(NODE)) {
                        PrintNodeUtil.printNode(rootNode, true);
                    } else if (binding.equals(GUI_NODE)) {
                        PrintNodeUtil.printNode(guiNode);
                    } else if (binding.equals(PHYS)) {
                        if (dps == null) {
                            physicsDebug();
                        }
                        if (dps != null) {
                            dps.setEnabled(!dps.isEnabled());
                        }
                    }
                }
            }
        }, EXIT, MEM, STATS, PHYS, NODE, GUI_NODE);
    }

    private void statsView(Node guiNode) {
        //ATTACH STATS VIEW
        final BitmapFont guiFont = app.getAssetManager().loadFont("Interface/Fonts/TimesNewRoman12Bold.fnt");
        sAs = new StatsAppState(guiNode, guiFont);
        sm.attach(sAs);
        input.addMapping(STATS, new KeyTrigger(KeyInput.KEY_F2));
    }

    private void screenShot() {
        //AttachScreenShotAppState
        sap = new ScreenshotAppState("screenshots/");
        sm.attach(sap);
    }

    private void physicsDebug() {
        try {
            PhysicsSpace pSpace = sm.getState(BulletAppState.class).getPhysicsSpace();
            //ATTACH PHYSICS DEBUG
            if (pSpace != null) {
                dps = new PhysicsDebugAppState(pSpace);
                sm.attach(dps);
                dps.setEnabled(false);
                input.addMapping(PHYS, new KeyTrigger(KeyInput.KEY_F3));
            }
        } catch (NullPointerException ex) {
            //WE DON'T CARE
        }
    }

    @Override
    public void cleanUp() {
        sm.detach(sAs);
        sm.detach(dps);
        sm.detach(sap);
    }

    public synchronized static void VisualVectorReference(Node root, AssetManager manager, Vector3f origin, Vector3f direction) {
        if (debugArrowGeom == null
                || debugArrow == null) {
            debugArrow = new Arrow(new Vector3f(0, 0, 0));
            debugArrow.setLineWidth(4);
            debugArrowGeom = new Geometry("GEOM", debugArrow);
            Material mat = new Material(manager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat.getAdditionalRenderState().setWireframe(true);
            mat.setColor("Color", ColorRGBA.Red);
            debugArrowGeom.setMaterial(mat);
        }
        debugArrowGeom.setLocalTranslation(origin);
        debugArrow.setArrowExtent(direction);
        root.attachChild(debugArrowGeom);
    }
    private static Geometry debugArrowGeom;
    private static Arrow debugArrow;
}
