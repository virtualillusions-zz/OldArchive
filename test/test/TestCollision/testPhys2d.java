/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TestCollision;

import com.jme3.bounding.BoundingBox;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.strategies.QuadSpaceStrategy;

/**
 *
 * @author Kyle Williams
 */
public class testPhys2d extends com.jme3.app.SimpleApplication{
    public static void main(String[] args){
        testPhys2d app = new testPhys2d();
        app.start();
    }

    protected World world;
    private Node collider, floor,collider2;
    private Body b,b1,b2;


    @Override
    public void simpleInitApp() {
        world = new World(new Vector2f(0.0f, -9.81f), 10, new QuadSpaceStrategy(20,5));

        //Creates the floor
        floor = new Node("floor");
        floor.attachChild(createBox(10,1,10));
        floor.move(0,-20,0);
        b=(StaticBody) createCollisionObject(floor,b,true);
        rootNode.attachChild(floor);


        //Creates a collider to interact with the floor
        collider = new Node("collider");
        collider.attachChild(createBox(2,2,1));
         //sets the collider a bit highter than the floor
        collider.move(0, 5, 0);
        b1=createCollisionObject(collider,b1,false);
        rootNode.attachChild(collider);

           //Creates a collider to interact with the floor
        collider2 = new Node("collider2");
        collider2.attachChild(createBox(5,5,1));
         //sets the collider a bit highter than the floor
        collider2.move(0, 20, 0);
        b2=createCollisionObject(collider2,b2,false);
        rootNode.attachChild(collider2);

        cam.setFrustumFar(10000);
        flyCam.setMoveSpeed(75);

        inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_L));
        inputManager.addListener(analogListener, "left","right");
    }

    //Creates a quick box dimenstions input by user
    public Geometry createBox(int x, int y, int z){
        Geometry geom = new Geometry("Box", new Box(Vector3f.ZERO, x, y, z));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
        mat.setTexture("m_ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        geom.setMaterial(mat);
        return geom;
    }

    public Body createCollisionObject(Spatial spat,Body body,boolean isStatic){
        //spat.center();
        spat.updateGeometricState();
        BoundingBox bounding = (BoundingBox) spat.getWorldBound();
        if(isStatic==true)
       body = new StaticBody(spat.getName(), new net.phys2d.raw.shapes.Box(bounding.getXExtent()*2,bounding.getYExtent()*2));
        else body = new Body(spat.getName(), new net.phys2d.raw.shapes.Box(bounding.getXExtent()*2,bounding.getYExtent()*2), 100.0f);
        body.setPosition(bounding.getCenter().getX(), bounding.getCenter().getY());
        world.add(body);
        return body;
    }

    @Override
    public void simpleUpdate(float tpf){
        //cam.lookAt(collider.getLocalTranslation(), Vector3f.UNIT_Y);
        world.step(tpf);        
         collider.setLocalTranslation(b1.getPosition().getX(),b1.getPosition().getY(),0);
        collider2.setLocalTranslation(b2.getPosition().getX(),b2.getPosition().getY(),0);
    }

      public AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            if(name.equals("left")){b2.adjustPosition(new Vector2f(-.01f,0),.1f);System.out.println("Left");}
            if(name.equals("right")){b2.adjustPosition(new Vector2f(.01f,0),.1f);System.out.println("Right");}
        }
    };
}
