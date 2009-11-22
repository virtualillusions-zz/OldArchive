package Blender2JME;

import java.util.List;
import java.util.ArrayList;
import java.net.URL;
import java.net.MalformedURLException;
 
import com.jme.input.MouseInput;
import com.jme.input.FirstPersonHandler;
import com.jme.util.export.xml.XMLImporter;
import com.jme.bounding.BoundingBox;
import com.jme.scene.Spatial;
import com.jme.app.SimpleGame;
 
public class blendertwojme extends SimpleGame {
    protected XMLImporter xmlImporter = XMLImporter.getInstance();
 
    static public void main(String[] args) throws MalformedURLException {
        blendertwojme app = new blendertwojme();
        app.start();
    } 
 
    protected void simpleInitGame() {
        try {
            MouseInput.get().setCursorVisible(true);
            ((FirstPersonHandler) input).setButtonPressRequired(true);
            // Windowed mode is extremely irritating without these two settings.
 
           loadModel(blendertwojme.class.getClassLoader().getResource("Blender2JME/TestBike-jme.xml"));
        } catch (Exception e) {
            // Programs should not just continue obvliviously when exceptions
            // are thrown.  Since we aren't handling them, we exit gracefully.
            e.printStackTrace();
            finish();
        }
    }
 
    protected void loadModel(URL modelUrl) {
        // May also be called during update() loop to add to scene.
        Spatial loadedSpatial = null;
        try {
            loadedSpatial = (Spatial) xmlImporter.load(modelUrl);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Failed to load URL: " + modelUrl, e);
        }
        loadedSpatial.setModelBound(new BoundingBox());
        rootNode.attachChild(loadedSpatial);
        loadedSpatial.updateModelBound();
        // The default update loop will update world bounding volumes.
        rootNode.updateRenderState();
    }
}