/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.vza.director.model.controller;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import java.io.IOException;

/**
 *
 * @author Kyle Williams
 */
public final class TargetController implements com.jme3.scene.control.Control{
    private boolean enabled=true;
    private Spatial spat;
    private Spatial target;
    private boolean lookLeft = false;
    private boolean lookRight = false;
    private boolean lookAt=false;


     private void target(Vector3f target){
        Vector3f direction  = target;
        Vector3f up = Vector3f.UNIT_Y;
        com.jme3.math.Quaternion quat = spat.getLocalRotation();
        direction.setY(0).normalizeLocal();
        Vector3f left = up.normalizeLocal().cross(direction);
        quat.fromAxes(left,up,direction);
        spat.setLocalRotation(quat);
    }

    public void lookAt(Spatial object){
        lookLeft=false;lookRight=false;lookAt=true;
        if(target==null||!target.equals(object))target=object;
        target(object.getWorldTranslation().subtract(spat.getWorldTranslation()));
    }

    public void lookLeft(){
        lookLeft=true;lookRight=false;lookAt=false;
        target(new Vector3f(-1,0,0));
    }
    public void lookRight(){
        lookLeft=false;lookRight=true;lookAt=false;
        target(Vector3f.UNIT_X);
    }

    @Override
    public void update(float tpf) {
        if(lookAt)lookAt(this.target);
        else if(lookLeft)lookLeft();
        else if(lookRight)lookRight();
    }

    @Override
    public com.jme3.scene.control.Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSpatial(Spatial spatial) {        spat = spatial;    }

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