package FighterExample.data;


import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.OgreMaterialList;
import com.jme3.scene.plugins.ogre.OgreMeshKey;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kyle Williams
 */
public class testClass extends SimpleApplication {

    public static void main(String[] args){
        testClass app = new testClass();
        app.start();
    }

    @Override
    public void simpleInitApp() {
         DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -0.7f, -1).normalizeLocal());
        dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
        rootNode.addLight(dl);

  OgreMaterialList matList = (OgreMaterialList) manager.loadContent("OTO.material");
        OgreMeshKey key = new OgreMeshKey("OTO.meshxml", matList);
        Spatial model = (Spatial) manager.loadContent(key);
        //model.center();
        rootNode.attachChild(model);
        model.center();

        // create a simple plane/quad
        Quad quadMesh = new Quad(1, 1);

        Geometry quad = new Geometry("Textured Quad", quadMesh);
        quad.updateModelBound();

        Texture tex = manager.loadTexture("Monkey.jpg");

        Material mat = new Material(manager, "plain_texture.j3md");
        mat.setTexture("m_ColorMap", tex);
        quad.setMaterial(mat);

        float aspect = tex.getImage().getWidth() / (float) tex.getImage().getHeight();
        quad.setLocalScale(new Vector3f(aspect * 5, 5, 1));
        quad.center();

        rootNode.attachChild(quad);
    }
}
