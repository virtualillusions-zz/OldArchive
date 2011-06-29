/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.particles;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.shapes.EmitterBoxShape;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

/**
 *
 * @author Kyle Williams
 */
public class PlayingWithParticles extends SimpleApplication{

    private ParticleEmitter emit;
    private float angle = 0;

    public static void main(String[] args){
        PlayingWithParticles app = new PlayingWithParticles();
        app.start();
    }

      @Override
    public void simpleInitApp() {
          

        emit = new ParticleEmitter("Emitter", Type.Triangle, 10000);
        emit.setShape(new EmitterBoxShape(new Vector3f(-1.8f, -1.8f, -1.8f),
                                          new Vector3f(1.8f, 1.8f, 1.8f)));
        emit.setMesh(null);
        emit.setGravity(0);
        emit.setLowLife(60);
        emit.setHighLife(60);
        //emit.setStartVel(new Vector3f(0, 0, 0));
        emit.setImagesX(15);
        emit.setStartSize(0.05f);
        emit.setEndSize(0.05f);
        emit.setStartColor(ColorRGBA.White);
        emit.setEndColor(ColorRGBA.White);
        emit.setSelectRandomImage(true);
        emit.emitAllParticles();
        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setBoolean("PointSprite", true);
        mat.setTexture("Texture", assetManager.loadTexture("Interface/EssenceInterface/Background.png"));
        emit.setMaterial(mat);

        rootNode.attachChild(emit);

     
    }

    @Override
    public void simpleUpdate(float tpf){
        angle += tpf;
        angle %= FastMath.TWO_PI;
        float x = FastMath.cos(angle) * 2;
        float y = FastMath.sin(angle) * 2;
        emit.setLocalTranslation(x, 0, y);
    }

    public void test(){
        
       
    }

}
