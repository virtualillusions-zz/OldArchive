/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.vza.director.model.controller;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import java.io.IOException;

/**
 * This Class assists the Fighter in looking in the correct direction
 * @author Kyle Williams
 */
public final class TargetController implements com.jme3.scene.control.Control{
    private boolean enabled=true;
    private boolean isLookingAt=false;
    private boolean isMovingLeft=false;
    private Spatial spat;
    private Spatial target;
    private boolean isX_Axis=true;
    private float xPos=90, xNeg=270;
    private float zPos=0, zNeg=180;

    /**
     * face an Object or another fighter
     * @param object
     */
    public void lookAt(Spatial object){
        target=object;
        isLookingAt=true;
    }

    /**
     * Look Left
     */
    public void lookLeft(){
        isLookingAt=false;
        float rad = (isX_Axis)?xNeg:zNeg;
        spat.setLocalRotation(
            new Quaternion().fromAngleNormalAxis(FastMath.DEG_TO_RAD*rad, Vector3f.UNIT_Y)
        );
    }

    /**
     * Look Right
     */
    public void lookRight(){
        isLookingAt=false;
        float rad = (isX_Axis)?xPos:zPos;
        spat.setLocalRotation(
            new Quaternion().fromAngleNormalAxis(FastMath.DEG_TO_RAD*rad, Vector3f.UNIT_Y)
        );
    }

    public boolean isLookingAt(){return isLookingAt;}
    public boolean isMovingLeft(){return isMovingLeft;}
    public boolean isX_Axis(){return isX_Axis;}
    public void setIs_X_Axis(boolean xAxis){isX_Axis=xAxis;}

    @Override
    public void update(float tpf) {
        if(isLookingAt){
            Vector3f trans = target.getWorldTranslation();
            Vector3f t = new Vector3f(trans.getX(),spat.getWorldTranslation().getY(),trans.getZ());
            spat.lookAt(t, Vector3f.UNIT_Y);
            isMovingLeft = spat.getLocalRotation().getY()<0?true:false;
        }
    }

    @Override
    public com.jme3.scene.control.Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSpatial(Spatial spatial) {  spat = spatial;    }

     @Override
    public void setEnabled(boolean enabled) {         this.enabled=enabled;     }

    @Override
    public boolean isEnabled() {return enabled; }

    @Override
    public void render(RenderManager rm, ViewPort vp) {    }

    @Override
    public void write(JmeExporter ex) throws IOException {    }

    @Override
    public void read(JmeImporter im) throws IOException {    }
}