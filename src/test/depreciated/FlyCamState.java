/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.depreciated;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.FlyByCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.spectre.app.SpectreState;

/**
 *
 * @author Kyle Williams
 */
public class FlyCamState extends SpectreState {

    protected FlyByCamera flyCam;
    private boolean active = true;
    protected Camera cam;
    private boolean initialized = false;
    @Override
    public void SpectreState(AppStateManager stateManager, Application app) {
        
        initialized=true;
        cam = app.getCamera();
        if (app.getInputManager()!= null){
            flyCam = new FlyByCamera(cam);
            flyCam.setMoveSpeed(75f);
            flyCam.registerWithInput(app.getInputManager());
             flyCam.setDragToRotate(true);
            app.getInputManager().addMapping("VezlaAPP_CameraPos", new KeyTrigger(KeyInput.KEY_C));
            app.getInputManager().addMapping("VisibleMouse", new KeyTrigger(KeyInput.KEY_M));
            app.getInputManager().addListener(new ActionListener() {
                @Override
                public void onAction(String name, boolean value, float tpf) {
                    if(value==true)
                        if (name.equals("VezlaAPP_CameraPos")){
                            if (cam != null){
                                Vector3f loc = cam.getLocation();
                                Quaternion rot = cam.getRotation();
                                System.out.println("Camera Position: ("+
                                        loc.x+", "+loc.y+", "+loc.z+")");
                                System.out.println("Camera Rotation: "+rot);
                                System.out.println("Camera Direction: "+cam.getDirection());
                            }
                       }else if(name.equals("VisibleMouse")){
                            flyCam.setDragToRotate(!flyCam.isDragToRotate());
                        }
                }
             },"VezlaAPP_CameraPos","VisibleMouse");
        }
    }

    public FlyByCamera getFlyByCamera() {
        return flyCam;
    }

    public Camera getCamera() {
        return cam;
    }
}
