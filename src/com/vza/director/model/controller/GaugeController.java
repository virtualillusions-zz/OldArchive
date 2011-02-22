/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vza.director.model.controller;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import java.io.IOException;

/**
 * This controller not only handles the Life Gauge but also the special gauges for the character
 * @author Kyle Williams
 */
public final class GaugeController implements com.jme3.scene.control.Control {
    public GaugeController(){reset();}

    private boolean enabled=true;
    private int meter=50;
    private float life=100;
    private int curLife=75;
    private boolean rage=false;
    private boolean penance=false;
    private boolean specials=false;
    private boolean retribution=false;

    public void reset(){
        meter=50;
        life=100;
        rage=false;
        curLife=100;
        penance=false;
        specials=false;
        retribution=false;
    }
    public float getHP(){return life;}
    /**This is the proper way to deplete life points*/
    public void getHitFor(float Damage){life-=Damage;}
    /**Get Current LifeBar Section*/
    public int getLifeSegment(){
        if(curLife>75)
            return 4;
        else if(curLife>50)
            return 3;
        else if(curLife>25)
            return 2;
        else return 1;
    }
    /**Get CUrrent LifeCondition*/
    public String getLifeCondition(){
        if(curLife>75)return "Great-Idle";
        else if(curLife<=25) return "Tired-Idle";
        return "Okay-Idle";
    }

    /**Get Current Specials Meter*/
    public int getMeter(){return meter;}
    /**resets the special meter to default*/
    public void resetMeter(){meter=50;}
    /**returns Rage Boolean*/
    public boolean isRage(){return rage;}
    /**returns Penance Boolean*/
    public boolean isPenance(){return penance;}
    /**returns Retribution Boolean*/
    public boolean isRetribution(){return retribution;}
    /*returns Specials Boolean**/
    public boolean isSpecial(){return specials;}

    @Override
    public void update(float tpf) {
        //This part controls life
        if(life<curLife-26)
            curLife-=25;
        else if((life<curLife))
            life+=.001f;
        //This part controls special meters
        if(meter<0) meter=0;
        else if(meter>100) meter=100;
        
        if(meter==25 || meter ==75) specials=true;
        
        if(meter<=1) penance=true;
            else penance=false;
        
        if(meter>=99) retribution=true;
            else retribution=false;

        if(life==10) rage=true;
            else rage=false;
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
        
    }
    
    @Override
    public void setEnabled(boolean enabled) {this.enabled=enabled; }

    @Override
    public boolean isEnabled() {return enabled; }

     @Override
    public com.jme3.scene.control.Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSpatial(Spatial spatial) {
          //  throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
