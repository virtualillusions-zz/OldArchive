/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TestCollision;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.awt.Rectangle;

/**
 *
 * @author Kyle Williams
 */
public class testCollision extends com.jme3.app.SimpleApplication{
    public static void main(String[] args){
        testCollision app = new testCollision();
        app.start();
    }

    private Node collider, collider2, floor;
    private Rectangle cR,cR2, fR;

    @Override
    public void simpleInitApp() {

        //Creates the floor
        floor = new Node("floor");
        floor.attachChild(createBox(10,1,10));
        floor.move(0,-20,0);
        fR = createCollisionObject(floor);
        rootNode.attachChild(floor);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/WireColor.j3md");
        mat.setColor("m_Color", ColorRGBA.Red);

        //Creates a collider to interact with the floor
        collider = new Node("collider");
        collider.attachChild(createBox(2,3,1));
         //sets the collider a bit highter than the floor
        collider.move(0, 10, 0);
        rootNode.attachChild(collider);
        cR = createCollisionObject(collider);


         //Creates a collider to interact with the floor
        collider2 = new Node("collider");
        collider2.attachChild(createBox(5,2,1));
         //sets the collider a bit highter than the floor
        collider2.move(0, 20, 0);
        rootNode.attachChild(collider2);
        cR2 = createCollisionObject(collider2);

        cam.setLocation(collider.getLocalTranslation().subtract(new Vector3f(5,0,0)));
        cam.setFrustumFar(10000);
        flyCam.setMoveSpeed(100);

    }

    public Rectangle createCollisionObject(Spatial object){
        object.updateGeometricState();
        BoundingBox bV = (BoundingBox) object.getWorldBound();
        int x = (int)(bV.getCenter().getX()-bV.getXExtent());
        int y = (int) (bV.getCenter().getY()-bV.getYExtent());
        int width = (int)(bV.getXExtent()*2);
        int height = (int)(bV.getYExtent()*2);
      
        return new Rectangle(x,y,width,height);
    }

    public void updateCollision(Rectangle rec, Spatial object){
        object.updateGeometricState();
        BoundingBox bV = (BoundingBox) object.getWorldBound();
        int x = (int)(bV.getCenter().getX()-bV.getXExtent());
        int y = (int) (bV.getCenter().getY()-bV.getYExtent());
        rec.setLocation(x, y);
        rec.grow(1, 1);
    }


    //Creates a quick box dimenstions input by user
    public Geometry createBox(int x, int y, int z){
        Geometry geom = new Geometry("Box", new Box(Vector3f.ZERO, x, y, z));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
        mat.setTexture("m_ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        geom.setMaterial(mat);
        return geom;
    }

    @Override
    public void simpleUpdate(float tpf){
        cam.lookAt(collider.getLocalTranslation(), Vector3f.UNIT_Y);
       // collider.setLocation(x, y);
        
        for(int x = 0; x<5; x++){
         updateCollision(cR,collider);
         if(!cR.intersects(fR)){
             collider.move(0, -1f*tpf, 0);
         }

         updateCollision(cR2,collider2);
         if(!cR2.intersects(cR)){
             collider2.move(0, -1f*tpf, 0);
         }
        }

    }
}
