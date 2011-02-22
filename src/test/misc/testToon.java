/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.misc;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.CartoonEdgeFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.Caps;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.debug.WireBox;
import com.jme3.texture.Texture;

/**
 *
 * @author Kyle Williams
 */
public class testToon extends SimpleApplication{
    private DirectionalLight dl;

    @Override
    public void simpleInitApp() {
        getViewPort().setBackgroundColor(ColorRGBA.White);
        setup();
        setupFilters();
    }

    

    @Override
    public void simpleUpdate(float tpf){
        dl.setDirection(getCamera().getDirection().normalizeLocal());
        getCamera().getLocation().setY(0);
        getCamera().getLocation().setX(0);

        getCamera().lookAt(((Node)getRootNode().getChild(0)).getChild(1).getLocalTranslation().clone().addLocal(0, 1, 0), Vector3f.UNIT_Y);
    }

    public void setup(){
        dl = new DirectionalLight();
        getRootNode().addLight(dl);
 
        Node model = new Node();
        Spatial modelGeo = getAssetManager().loadModel("Models/Reino/Reino.j3o");
        Spatial modelGeo2 = getAssetManager().loadModel("Models/Reino/test/Reino.j3o");
        Spatial modelGeo3 = modelGeo.clone();
        makeToonish(modelGeo3);

        modelGeo.setLocalTranslation(1.1f,0,0);
        modelGeo3.setLocalTranslation(-1.1f, 0, 0);

        model.attachChild(modelGeo);
        model.attachChild(modelGeo2);
        model.attachChild(modelGeo3);

        getRootNode().attachChild(model);
        this.getCamera().setLocation(model.getLocalTranslation().add(0,3,5));
        this.getCamera().lookAt(model.getLocalTranslation(), Vector3f.UNIT_Y);

    }

public void setupFilters(){
        if (renderer.getCaps().contains(Caps.GLSL100)){
            FilterPostProcessor fpp=new FilterPostProcessor(assetManager);
            fpp.addFilter(new CartoonEdgeFilter());
            fpp.addFilter(new SSAOFilter(0.92f,2.2f,0.29000017f,0.21200025f));

            BloomFilter bloom=new BloomFilter();
        bloom.setDownSamplingFactor(2);
        bloom.setBlurScale(1.37f);
        bloom.setExposurePower(3.30f);
        bloom.setExposureCutOff(0.2f);
        bloom.setBloomIntensity(2.45f);

        fpp.addFilter(bloom);
            viewPort.addProcessor(fpp);
        }
    }

    public void makeToonish(Spatial spatial){
        if (spatial instanceof Node){
            Node n = (Node) spatial;
            for (Spatial child : n.getChildren())
                makeToonish(child);
        }else if (spatial instanceof Geometry){
            Geometry g = (Geometry) spatial;
            Material m = g.getMaterial();
            if (m.getMaterialDef().getName().equals("Phong Lighting")){
                Texture t = assetManager.loadTexture("Textures/toon.png");
                m.setTexture("m_ColorRamp", t);
                m.setBoolean("m_UseMaterialColors", true);
                m.setColor("m_Specular", ColorRGBA.Black);
                m.setColor("m_Diffuse", ColorRGBA.White);
                m.setBoolean("m_VertexLighting", true);
            }
        }
    }


    public static void main(String[] args){
        testToon app = new testToon();
        app.start();
    }
}
