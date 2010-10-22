package FighterExample;

import FighterExample.etc.Fighting;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.plugins.ogre.MeshLoader;

import java.util.HashMap;
import java.io.File;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

/**
 *
 * @author Kyle Williams
 */
public class FighterFactory {
    private AssetManager manager;
    private Node root;
    private HashMap<String, FighterEntity> fighters;
    /**
     * This Hashmap holds all of the information for animation of the differnt characters handled in a uniform list.
     * Each charter has a hashmap full of stances which contin arrays for each stance.
     * The numbering inside the arrays according to any given stance is as follows:
     * <ol>
     * <li>name</li>
     * <li>archetype</li>
     * <li>idle</li>
     * <li>walk</li>
     * <li>run</li>
     * <li>jump</li>
     * <li>crouch</li>
     * <li>Attack1</li>
     * <li>Attack2</li>
     * <li>Attack3</li>
     * <li>Attack4</li>
     * <li>penance</li>
     * <li>vengence</li>
     * <li>rage</li>
     * </ol>
     */
    HashMap<String,LinkedHashMap> characterInfoList;
    /*The one and only instance of Fighter Factory*/
    private static FighterFactory instance = null;

        /**
         * Initializes the Fighter Factory
         * @param Assetmanager rootNode PhysicsSpace
         */
     public static void init(AssetManager manager, Node rootNode){
         if(instance==null)
                instance = new FighterFactory(manager, rootNode);
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


    private FighterFactory(AssetManager manager, Node rootNode){
        this.manager = manager;
        root=rootNode;
        fighterData();
        fighters = new HashMap<String,FighterEntity>();
    }

    public void fighterData(){
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            URI searchDirectory = FighterFactory.class.getClassLoader().getResource("FighterExample/").toURI();
            Document doc = builder.parse(new File(searchDirectory.resolve("characterData.xml")));
            Element characterData = (Element) doc.getElementsByTagName("characters").item(0);
                NodeList stances,character = characterData.getElementsByTagName("fighter");
                
                characterInfoList = new HashMap<String,LinkedHashMap>();
                LinkedHashMap<String,String[]> stanceList = new LinkedHashMap<String,String[]>();
                //loops through all characters and thier stances
                for(int i=0;i<character.getLength();i++){
                    stances = ((Element)character.item(i)).getElementsByTagName("stance");
                    stanceList.clear();
                    for(int j=0;j<stances.getLength();j++){
                        String[] info = new String[14];
                        Element stance = (Element)stances.item(i);
                            info[0] = stance.getAttribute("name");
                            info[1] = stance.getAttribute("archetype");
                        Element move = (Element)stance.getElementsByTagName("move").item(0);
                            info[2] = move.getAttribute("idle");
                            info[3] = move.getAttribute("walk");
                            info[4] = move.getAttribute("run");
                            info[5] = move.getAttribute("jump");
                            info[6] = move.getAttribute("crouch");
                        Element basic = (Element)stance.getElementsByTagName("basic").item(0);
                            info[7] = basic.getAttribute("Attack1");
                            info[8] = basic.getAttribute("Attack2");
                            info[9] = basic.getAttribute("Attack3");
                            info[10] = basic.getAttribute("Attack4");
                        Element retribution = (Element)stance.getElementsByTagName("retribution").item(0);
                            info[11] = retribution.getAttribute("penance");
                            info[12] = retribution.getAttribute("vengence");
                            info[13] = retribution.getAttribute("rage");
                        stanceList.put(info[0], info);
                    }
                  characterInfoList.put(((Element)character.item(i)).getAttribute("name"), stanceList);
                  System.out.println(characterInfoList.keySet());
                }
        } catch (Exception ex) {
            Logger.getLogger(FighterFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates and deploys a character and addes them to an accesable Hashmap
     * remember to set isHuman if character is player controlled
     * @param ID
     * @param modelName
     * @return fighterMesh
     */
    public Node setFighter(String ID,String modelName){
        if(fighters.get(modelName)==null){
            Node fighterMesh = (Node)MeshLoader.loadModel(manager, modelName+".meshxml", null);
                 //fighterMesh.setMaterial(manager.loadMaterial(modelName+".j3m"));
                 fighterMesh.setName(modelName+":Mesh");

                 fighters.put(ID, new FighterEntity(ID,fighterMesh,characterInfoList.get(modelName)));

            root.attachChild(getFighter(ID));
         }
        return getFighter(ID);
    }

    /**
     * Attaches Node To Root
     * @param ID
     */
    public void attachToRoot(String ID){
        root.attachChild(getFighter(ID));
    }
    /**
     * Detaches Node From RooT
     * @param ID
     */
    public void detachFromRoot(String ID){
        root.detachChild(getFighter(ID));
    }

    
    /**
     * Returns the physics of the Fighter
     * @param name 
     * @return The name of the fighter
     */
    public Node getFighter(String ID){
        return fighters.get(ID).getModel();
    }

    /**
     *  returns a hashmap of all fighters and thier stances
     * @return characterInfoList
     */
    public HashMap<String,LinkedHashMap> getCharacterList(){return characterInfoList;}
}