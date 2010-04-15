package FighterExample.etc;

import FighterExample.*;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.nodes.PhysicsCharacterNode;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.plugins.ogre.MeshLoader;
import java.util.HashMap;

/**
 *
 * @author Kyle Williams
 */
public class FighterFactory {
    private AssetManager manager;
    private Node root;
    //private PhysicsSpace phys;
    private HashMap<String, FighterEntity> fighters;
    /*The one and only instance of Fighter Factory*/
    private static FighterFactory instance = null;

        /**
         * Initializes the Fighter Factory
         * @param Assetmanager rootNode PhysicsSpace
         */
     public static void init(AssetManager manager, Node rootNode, PhysicsSpace getPhysicsSpace){
         if(instance==null)
                instance = new FighterFactory(manager, rootNode/*, getPhysicsSpace*/);
         else
            Fighting.getLogger.warning("Why Initalize Fighter Factory Twice");
     }
     /**
      * returns the one and only instance of gameSingleton
      * @return instance(gameSingleton)
      */
     public static FighterFactory get(){
         if (instance == null) {
                 // init has not been called yet.
         Fighting.getLogger.entering(FighterFactory.class.getName(), "get()");
         Fighting.getLogger.severe("ALERT, call init first!!");
         }
                return instance;
    }


    private FighterFactory(AssetManager manager, Node rootNode/*, PhysicsSpace getPhysicsSpace*/){
        this.manager = manager;
        root=rootNode;
       // phys = getPhysicsSpace;
        fighters = new HashMap<String,FighterEntity>();
    }

    public Node setFighter(String ID,String modelName){
        if(fighters.get(modelName)==null){
        Node fighterMesh = (Node)MeshLoader.loadModel(manager, modelName+".meshxml", null);
             fighterMesh.setMaterial(manager.loadMaterial(modelName+".j3m"));
             fighterMesh.setName(modelName+":Mesh");

        //Node intermediate = new Node();
          //  fighterMesh.center();
            //intermediate.attachChild(fighterMesh);

        PhysicsCharacterNode physfighter = new PhysicsCharacterNode(fighterMesh,
               new BoxCollisionShape(new Vector3f(1f,1f,1f)),0f);
        physfighter.setName(modelName);
        physfighter.updateGeometricState();
        physfighter.updateModelBound();
        physfighter.setMaxJumpHeight(100f);

        fighters.put(ID, new FighterEntity(ID,physfighter,null));

        root.attachChild(getFighter(ID));
       // phys.addQueued(getFighter(ID));
        }
        return getFighter(ID);
    }

    /**
     * Attaches Node To Root
     * @param ID
     */
    public void attachToRoot(String ID){
        root.attachChild(getFighter(ID));
       // phys.addQueued(getFighter(ID));
    }
    /**
     * Detaches Node From RooT
     * @param ID
     */
    public void detachFromRoot(String ID){
        //phys.removeQueued(getFighter(ID));
        root.detachChild(getFighter(ID));
    }

    /**
     * Returns the mesh of the Fighter
     * @param name
     * @return The name of the fighter
     */
    public Node getFighterMesh(String ID){
      return fighters.get(ID).getMesh();
    }


    
    /**
     * Returns the physics of the Fighter
     * @param name 
     * @return The name of the fighter
     */
    public PhysicsCharacterNode getFighter(String ID){
        return fighters.get(ID).getModel();
    }

    
}