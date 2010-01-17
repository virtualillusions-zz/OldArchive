package Virtualproject2D;

import java.awt.DisplayMode;


public class DisplayWrapper {

    /**The wrapped <code>DisplayMode</code>.*/
    DisplayMode mode;

    /**Constructor - Constructs a wrapper for a <code>DisplayMode</code>.
     * 
     * @param mode The <code>DisplayMode</code> to wrap.
     */
    public DisplayWrapper(DisplayMode mode) {
        this.mode = mode;
    }

    public boolean equals(Object o) {
        if (o instanceof DisplayWrapper) {
            return mode.equals(((DisplayWrapper) o).mode);
        }
        return mode.equals(o);
    }

    public String toString() {
        return (mode.getWidth() + "x" + mode.getHeight() +
                "x" + mode.getBitDepth() + " @" + mode.getRefreshRate());
    }
}
