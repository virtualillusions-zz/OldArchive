package utill;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.export.xml.XMLImporter;
import com.jmex.game.state.BasicGameState;

import racerControllers.RacerNode;
import scenes.SceneNode;

public class ObjectManager extends BasicGameState implements HyperNode{

	private static ArrayList[] gameObjects = new ArrayList[2];
	private static ObjectManager instance;
	
	/**Initializes the ObjectManager...Will take a while*/
	public static void init(){ if(instance==null)instance = new ObjectManager();		} 
	/**returns the one and only isntance @return instance*/
	public static ObjectManager get(){
		 if (instance == null) {
    		 // init has not been called yet.
    	 gameSingleton.getLogger.entering(ObjectManager.class.getName(), "get()");
    	 gameSingleton.getLogger.severe("SEVERE ALERT, ArrayModels has not been impl properly, call init first!!");
		 return null;
		 } 
		 return instance; 
	}

	private ObjectManager(){
		super("ObjectManager");
   
		gameObjects[1]=loadTerrains();
		gameObjects[0]=loadCharacters();	
		////////////////////////////////////////////////////
		getRootNode().attachChild(getCharacter(Racers.TestBike));
		
		getRootNode().updateGeometricState(0, true);
		getRootNode().updateRenderState();
		gameSingleton.get().attachChild(this);			
		}
	/**
	 * <ul>
	 * <li>01  Victor		02 Ikki					03  Soul
	 * <li>04  Brock		05 Idol	 				06  Naganuma
	 * <li>07  Anthony		08 Benten   			09  Akita
	 * <li>10 Jacky 		11 Tagun				12 Kazuma
	 * <li>13 Jose			14 Fuu(Wind)			15 Katonai(Fire)
	 * <li>16 Retsui(Water) 17 Raishon(Electricity) 18 Doton(Earth)
	 * </ul>
	 
	 */
	private final ArrayList loadCharacters(){
		ArrayList<RacerNode> characters = new ArrayList<RacerNode>();
		RacerFactory.init(rootNode);

		for(Racers r: Racers.values()){
			RacerNode racer = RacerFactory.get().createRacerNode();
			RacerFactory.get().setModel(r, racer);
			racer.updateGeometricState(0, false);
			racer.updateRenderState();
			characters.add(r.ordinal(),racer );

		}

		rootNode.updateGeometricState(0, true);
		rootNode.updateRenderState();
	
		return characters;
	}


	@Override
	public void update(float tpf){
		super.update(tpf);
		for(int i=0; i<gameObjects.length;i++){
			if(gameObjects[i]!=null)
			for(int k=0; k<gameObjects[i].size();k++){
				if(gameObjects[i].get(k)!=null)
				 ((RacerNode) gameObjects[i].get(k)).update(tpf);
			}
		}
	}

	@Override
	public void mainUpdate(float tpf) {
		for(int i=0; i<gameObjects.length;i++){
			if(gameObjects[i]!=null)
			for(int k=0; k<gameObjects[i].size();k++){
				if(gameObjects[i].get(k)!=null)
				{((HyperNode) gameObjects[i].get(k)).mainUpdate(tpf);}
			}
		}
	}
	/**
	 * </ul>
	 * <li>01  Level01		02 Level02	
	 * <li>03  Level03		04 Level04	 
	 * <li>05  Level05		06 Level06
	 * <li>07  Level07 		08 Level08	
	 * </ul>
	 */
	private final ArrayList loadTerrains(){
		ArrayList<SceneNode> terrains = new ArrayList<SceneNode>();
		
	
		
		
		return terrains;
	}
	/**{@link ObjectManager#loadCharacters}
	 * @param racer choose a racer from enum Racers
	 *  @return Character*/
	public final RacerNode getCharacter(Racers racer){return (RacerNode) gameObjects[0].get(racer.ordinal());}
	/**{@link ObjectManager#loadTerrains}
	 * @param scene choose a Level from enum Levels
	 * @return Level*/
	public final SceneNode getTerrain(Levels scene){return (SceneNode) gameObjects[1].get(scene.ordinal());}
///////////////////////////////////////
	/**Quick Racer Selection*/
	public enum Racers{
		TestBike/**,Victor,		Ikki,		Soul,
		Brock,		Idol,		Naganuma,
		Anthony,	Benten,		Akita,
		Jacky,		Tagun,		Kazuma,
		Jose,		Fuu,		Katonai,
		Retsui,		Raishon,	Doton*/;		 		
	}
	/**Quick Level Selection*/
	public enum Levels{
		Level01,	Level02,
		Level03,	Level04,
		Level05,	Level06,
		Level07,	Level08;		 		
	}
	
	/////////////////////////////////////////////////////////////
static class RacerFactory{
		private static RacerFactory instance = null;
		private XMLImporter loader = null;
		private static Node baseNode;
		private RacerNode racer;
		public static RacerFactory get(){
			if(instance==null){
				 // init has not been called yet.
		    	 gameSingleton.getLogger.entering(RacerFactory.class.getName(), "get()");
		    	 gameSingleton.getLogger.severe("ALERT, call init first!!");
			}
			return instance;
		}
		/**
		 * initialized RacerFactory
		 * @param rootNode the rootNode of the gameState
		 */
		public static void init(Node rootNode){
			baseNode = rootNode;
			instance = new RacerFactory();
		}
		/*initializes xml parser and default spatial*/
		private RacerFactory(){ 
		loader = XMLImporter.getInstance();
		racer = new RacerNode(baseNode);
		racer.setName("Default");
		}
		
		/**returns a shared RacerNode*/
		public RacerNode createRacerNode(){return racer;}
		
		public void setModel(Racers identity, RacerNode theRacer){
			try {			
				theRacer.setName(identity.name());
		Spatial RacerSpatial = (Spatial) loader.load(RacerFactory.class.getClassLoader().getResource("models/"+identity.toString()+"-jme.xml"));
	    		RacerSpatial.setName(identity+":mesh");	    		
	    		theRacer.setLoadedSpatial(RacerSpatial);		
		} catch (Exception e) {
        	Logger.getLogger(RacerNode.class.getName()).log(Level.SEVERE, "Unable to Locate Character data", e);
        	   				}		
		}
}

}
