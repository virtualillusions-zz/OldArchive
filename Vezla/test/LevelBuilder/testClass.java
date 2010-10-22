
import com.jme3.app.SimpleApplication;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kyle Williams
 */
public class testClass extends SimpleApplication {

    public static void main(String args[]){
        testClass app = new testClass();
        app.start();
    }
       // public ArrayList<Geometry> platList = new ArrayList<Geometry>();

    @Override
    public void simpleInitApp() {
        this.flyCam.setMoveSpeed(1000);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/SolidColor.j3md");
        mat.setColor("m_Color", ColorRGBA.Blue);
 /**
        Geometry plat;
        findRectangles fRC = new findRectangles();
        fRC.starts("testStage");


        System.out.println("THIS IS THE PART"+fRC.getRectangles());

        Node node = new Node("Platforms");
       for(Rectangle r:fRC.getRectangles()){
           plat = new Geometry("Platform", new Box(new Vector3f((r.x + r.width / 2),-(r.y + r.height / 2),0),r.width/2,r.height/2,10));
           plat.setMaterial(mat);
           plat.updateModelBound();
           platList.add(plat);
           node.attachChild(plat);
        }
        fRC.stop();
        */
sceneNode sN = new sceneNode("test","testStage",mat);
        //rootNode.attachChild(sN);
        
         try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.writeTo(new FileOutputStream("test/LevelBuilder/scene.j3o"));
            BinaryExporter exp = new BinaryExporter();
            exp.save(sN, baos);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            BinaryImporter imp = new BinaryImporter();
            imp.setAssetManager(assetManager);
            Node ogreModelReloaded = (Node) imp.load(bais, null, baos);

            rootNode.attachChild(ogreModelReloaded);
        } catch (IOException ex){
            ex.printStackTrace();
        }
        
    }

    class sceneNode extends Node{
        public sceneNode(String name,String stageName, Material mat){
            super(name);
            setUp(stageName,mat);
        }

        public void setUp(String stageName,Material mat){
            Geometry plat;
            findRectangles fRC = new findRectangles();
            fRC.starts(stageName);

               for(Rectangle r:fRC.getRectangles()){
               System.out.println("THIS IS A RECTANGLE"+r.toString());

               plat = new Geometry("Platform", new Box(new Vector3f((r.x + r.width / 2),-(r.y + r.height / 2),0),r.width/2,r.height/2,10));
               plat.setMaterial(mat);
               plat.updateModelBound();
               //platList.add(plat);
               this.attachChild(plat);
            }
            fRC.stop();
        }

    }
}
