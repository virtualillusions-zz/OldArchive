/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.app.control;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.vi.machello.app.AppLogger;
import java.io.IOException;
import org.slf4j.Logger;

/**
 * An abstract implementation of the Control interface.
 *
 * @author Kyle D. Williams
 */
public abstract class BaseControl implements Control, AppLogger {

    protected boolean isEnabled = true;
    protected Spatial spatial;
    /**
     * Allows on the fly spatial switching
     */
    private boolean replaceSpatial = false;

    /**
     * Called Directly after setSpatial if spatial is not null
     */
    protected abstract void BaseControl();

    /**
     * called when removing control from spatial
     */
    public void cleanup() {
    }

    @Override
    public final void setSpatial(Spatial spatial) {
        getLogger().trace("Initializing {}...", this.getClass().getName());
        if (this.spatial != null && spatial != null && spatial != this.spatial) {
            IllegalStateException ise = new IllegalStateException("This control has already been added to a Spatial");
            getLogger().error("Initialization Error", ise);
            throw ise;
        }
        this.spatial = spatial;
        if (spatial != null) {
            isEnabled = true;
            BaseControl();
            enable();
        } else if (!replaceSpatial) {
            isEnabled = false;
            disable();
            cleanup();
        }
        /**
         * replace Spatial always gets set to false at the end regardless of
         * condition to prevent any issues
         */
        replaceSpatial = false;
    }

    public final boolean isInitialized() {
        return spatial != null;
    }

    public final Spatial getSpatial() {
        return spatial;
    }

    public final boolean isEnabled() {
        return isEnabled;
    }

    public final void setEnabled(boolean active) {
        if (this.isEnabled == active) {
            return;
        }
        this.isEnabled = active;
        if (!isInitialized()) {
            return;
        }
        if (active) {
            log.trace("Enabling {}...", this.getClass().getName());
            enable();
        } else {
            log.trace("Disabling {}...", this.getClass().getName());
            disable();
        }
    }

    /**
     * <code>enable</code> is initially called when the control is added to a
     * spatial and the any time the system is re-isEnabled afterwards after
     * first being disabled
     *
     * @see BaseControl#setEnabled(boolean)
     */
    protected void enable() {
    }

    /**
     * <code>disable</code> is called whenever the isEnabled is set to false
     * through setEnabled
     *
     * @see BaseControl#setEnabled(boolean)
     */
    protected void disable() {
    }

    /**
     * Default implementation of cloneForSpatial() that simply clones the
     * control and sets the spatial.
     * <pre>
     *  AbstractControl c = clone();
     *  c.spatial = null;
     *  c.setSpatial(spatial);
     * </pre>
     *
     * Controls that wish to be persisted must be Cloneable.
     */
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        try {
            BaseControl c = (BaseControl) clone();
            c.spatial = null; // to keep setSpatial() from throwing an exception
            c.setSpatial(spatial);
            return c;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Can't clone control for spatial", e);
        }
    }

    @Override
    public final void update(float tpf) {
        if (!isEnabled) {
            return;
        }
        controlUpdate(tpf);
    }

    @Override
    public final void render(RenderManager rm, ViewPort vp) {
        if (!isEnabled) {
            return;
        }
        controlRender(rm, vp);
    }

    /**
     * To be implemented in subclass.
     */
    protected void controlUpdate(float tpf) {
    }

    /**
     * To be implemented in subclass.
     */
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    /**
     * Replaces this controls target spatial with a new one
     *
     * @param spat Spatial
     */
    public final void replaceSpatial(Spatial spat) {
        /**
         * TODO: Sensitive this is a hack hazard way of performing this to
         * prevent null pointers should reconsider at a later time
         */
        if (spatial != spat) {
            replaceSpatial = true;
            destroy();
            spat.addControl(this);
        }
    }

    /**
     * <b>Convenience method</b> Remove this control from its parent Spatial
     */
    public final void destroy() {
        if (spatial != null) {
            isEnabled = false;
            spatial.removeControl(this);
        }
    }

    public static Logger getLogger() {
        return log;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(isEnabled, "enabled", true);
        oc.write(spatial, "spatial", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        isEnabled = ic.readBoolean("enabled", true);
        spatial = (Spatial) ic.readSavable("spatial", null);
    }
}