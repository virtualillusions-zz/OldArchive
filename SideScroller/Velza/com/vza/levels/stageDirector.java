package com.vza.levels;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;


import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.nodes.PhysicsNode;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.vza.util.State.Director;

/**
 * Class to manage, update and render multiple stages
 * @author Kyle Williams
 */
public class stageDirector implements Director{
	private AssetManager manager;
	//The root node of the scene graph
	private Node root;
	//This Array Contains all of the stage data stored within the Levels Directory
	private ArrayList<String> stageList = new ArrayList<String>();
	//The Directory of the 
	private URI searchDirectory;
	//Returns the name of the stage
	private String name;	
	/**<pre> returns the platform coordinates as
	 * 0)x	1)y	2)z
	 * 3)width 4)height	
	 * 5)depth		
	 * 6)angle				
	 * Axis					
	 * 7)x	8)y	9)z</pre>
	 * */
	private ArrayList<int[]> platformList;
	/*The Spawn Points of the Player and Enemies seperated respectfully in thier
	  Section of the HashMap Enemy And Player
	  Each ArrayList is a SpawnPoint Location containing X Y Z coordinates
	*/
	private HashMap<String, ArrayList<int[]>> spawnPointList;
	/**
	 * Finds all XML Files in the Levels Directory
	 * @param physicsSpace 
	 */
	public stageDirector(Node rootNode,AssetManager manager){
		//Registers the Levels folder as a source to find png files 
		manager.registerLocator("/com/vza/levels/", ClasspathLocator.class.getName(), "png","dds");
		this.manager=manager;
		root = new Node("sceneNode");		
	
		rootNode.attachChild(root);
		resetValues();
		try {
			searchDirectory = stageDirector.class.getClassLoader().getResource("com/vza/levels/").toURI();
			File xmlList = new File(searchDirectory);		
			for (File file : xmlList.listFiles()) {	    	 
				if (file.isFile()&&file.getName().contains("xml")) {
					stageList.add(file.getName());
				}
			}
		} catch (URISyntaxException e) {			
			e.printStackTrace();
		}
	}
		
	/**
	 * Loads all of the Values for the Stage into the Director Class to be used and loaded
	 * @param stage Name of Stage without .xml
	 */
	public void setActive(String stage) {
		resetValues();
		try {
		File file = new File(searchDirectory.resolve(stage+".xml"));
		
			DocumentBuilder builder =
			DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(file);
			//Get The Name of The Stage
			Element element = (Element) doc.getElementsByTagName("stage").item(0);			
			name = element.getAttribute("name");
			
			
			//Helper Element
			Element line;
			
			//Get Platform Locations
			Element platforms = (Element) element.getElementsByTagName("platforms").item(0);
			NodeList platform =  platforms.getElementsByTagName("platform");
			for(int i=0;i<platform.getLength();i++){
				line = (Element) platform.item(i);
				    Element position = (Element) line.getElementsByTagName("position").item(0);
						int x = Integer.parseInt(position.getAttribute("x"));
						int y = Integer.parseInt(position.getAttribute("y"));
						int z = Integer.parseInt(position.getAttribute("z"));						
					Element scale = (Element) line.getElementsByTagName("scale").item(0);
						int height = Integer.parseInt(scale.getAttribute("height"));
						int width = Integer.parseInt(scale.getAttribute("width"));
						int depth = Integer.parseInt(scale.getAttribute("depth"));
					Element rotation = (Element)line.getElementsByTagName("rotation").item(0);
						int angle = Integer.parseInt(rotation.getAttribute("angle"));
						Element axis = (Element)line.getElementsByTagName("axis").item(0);
						int u = Integer.parseInt(axis.getAttribute("x"));
						int v = Integer.parseInt(axis.getAttribute("y"));
						int w = Integer.parseInt(axis.getAttribute("z"));
				int[] pL = {x,y,z,width,height,depth,angle,u,v,w};
				platformList.add(pL);
			}
			
			//Get SpawnPoints
			Element spawnPoints = (Element) element.getElementsByTagName("spawnPoint").item(0);
			//Set Up Hashmap for Player and enemy locations
			spawnPointList.put("player", new ArrayList<int[]>());
			spawnPointList.put("enemies", new ArrayList<int[]>());
			//Get Player Respawn/Checkpoints
			Element player =  (Element) spawnPoints.getElementsByTagName("player").item(0);
			NodeList Spawn = player.getElementsByTagName("location");
			for(int i =0; i<Spawn.getLength();i++){
				line = (Element) Spawn.item(i);
				int x = Integer.parseInt(line.getAttribute("x"));
				int y = Integer.parseInt(line.getAttribute("y"));
				int z = Integer.parseInt(line.getAttribute("z"));
				int[] p = {x,y,z};
				spawnPointList.get("player").add(p);
			}
			//Get Enemy Spawn Points
			Element enemies =  (Element) spawnPoints.getElementsByTagName("enemies").item(0);
					Spawn = enemies.getElementsByTagName("location");
			for(int i =0; i<Spawn.getLength();i++){
				line = (Element) Spawn.item(i);
				int x = Integer.parseInt(line.getAttribute("x"));
				int y = Integer.parseInt(line.getAttribute("y"));
				int z = Integer.parseInt(line.getAttribute("z"));
				int[] p = {x,y,z};
				spawnPointList.get("enemies").add(p);
			}			
		}
		catch (Exception e) {
		e.printStackTrace();
		throw new NullPointerException("Either one of the values of an attribute is empty or the xml file is formatted incorrectly");
		}
	}
	/**
	 * Loads the stage by generating the platforms and loading the dual background images
	 * @see loadPlatforms()
	 * @see loadBackground()
	 */
	public void loadStage(){
		loadPlatforms(name);
		loadBackground(name);
	}
	/**
	 * Loads two sets of pictures and places them on either sides of the platforms
	 * @param bg The name of the background image (without extensions/must be png)
	 * SHOULD NOT BE CALLED UNLESS FOR EVENT HANDELING 
	 * Call setActive() and then loadStage() instead
	 */	
	public void loadBackground(String bg){       
        Texture tex = manager.loadTexture(bg+".png");
        Material mat = new Material(manager, "plain_texture.j3md");
        mat.setTexture("m_ColorMap", tex);
        Vector3f coordinates = new Vector3f(tex.getImage().getWidth(), tex.getImage().getHeight(), 1);
        //The very first platform
        int[] pL = platformList.get(0);

        // create a simple plane/quad
        Quad quadMesh = new Quad(1, 1);
        Geometry farBG = new Geometry("farBG", quadMesh);
        farBG.setMaterial(mat);
        //scale the coordinates equally
        farBG.setLocalScale(coordinates);
      //Translates the Geom to the far side of the paltform
        farBG.center();        
        farBG.setLocalTranslation(0,-tex.getImage().getHeight(),-pL[5]); 
       //recreate Quad and apply to new geom because otherwise flipping the Y will flip both prev and this one
        Quad quadMesh2 = new Quad(1, 1);
        quadMesh2.updateGeometry(1, 1, true);
        Geometry nearBG = new Geometry("nearBG", quadMesh2);
        nearBG.setMaterial(mat);
      //scale the coordinates equally
        nearBG.setLocalScale(coordinates);
        //rotate the quad so that it is seen in platform perspective but not cam sideview perspective
        Quaternion rot = new Quaternion();
        rot.fromAngleNormalAxis(FastMath.DEG_TO_RAD*180, Vector3f.UNIT_X);
        nearBG.setLocalRotation(nearBG.getLocalRotation().mult(rot));  
       
      //Translates the Geom to the side of the platform closest to the camera because the geom 
        //is rotated around the x axis instead of flipped it is already in the the bottom quadrants
        nearBG.center();
        nearBG.setLocalTranslation(0, 0, pL[5]);      
        
        root.attachChild(farBG);
        root.attachChild(nearBG);
	}
	
	
	/**
	 * Attaches all platforms to the sceneNode to be rendered
	 * @param bg the name of the platform texture (without -platform and extension/must by dds)
	 * SHOULD NOT BE CALLED UNLESS FOR EVENT HANDELING
	 * Call setActive() and then loadStage() instead
	 */ 
	public void loadPlatforms(String bg){
		if(platformList.equals(null)||platformList.isEmpty())
			throw new NullPointerException("Thier are no active Stages set please call setActive(String stage)");
		Box b;
		Geometry geom;
		Material mat = new Material(manager, "plain_texture.j3md");
        Texture tex = manager.loadTexture(bg+"-platform.dds");

        tex.setMinFilter(Texture.MinFilter.Trilinear);
        mat.setTexture("m_ColorMap", tex);
		for(int[] p:platformList){
			//Box b = new Box(new Vector3f((x + width / 2),-(y + height / 2),0),width/2,height/2,10);
			
			b = new Box(new Vector3f((p[0] + p[3] / 2),-(p[1] + p[4] / 2),p[2]),p[3]/2,p[4]/2,p[5]);
			
			geom = new Geometry("Platform", b);
			
	        geom.updateModelBound();	        

	        geom.setMaterial(mat);	        
			
	        root.attachChild(geom);	       

		}		
	}
	/**
	 * Returns All The Points enemies should spawn within a stage
	 * @return Spawn points for enemies
	 */
	public ArrayList<int[]> getEnemySpawn(){
		if(platformList.equals(null)||platformList.isEmpty())
			throw new NullPointerException("EnemeySpawn/Thier are no active Stages set please call setActive(String stage)");

		return spawnPointList.get("enemies");
	}
	/**
	 * Returns All The Checkpoints players can respawn at in a stage with the first being the original Spawn location
	 * @return Spawn points for enemies
	 */
	public ArrayList<int[]> getPlayerCheckpoints(){
		if(platformList.equals(null)||platformList.isEmpty())
			throw new NullPointerException("PlayerCheckpoints/Thier are no active Stages set please call setActive(String stage)");
			
		return spawnPointList.get("player");
	}

	
	
	private void resetValues(){
		name = "";
		if(platformList!=null)
			platformList.clear();
		else	
			platformList = new ArrayList<int[]>();
		
		if(spawnPointList!=null)
			spawnPointList.clear();
		else	
			spawnPointList = new HashMap<String, ArrayList<int[]>>();
	}
	
	
	@Override
	public void cleanup() {
		stageList.clear();
		platformList.clear();
		spawnPointList.clear();
	}

	@Override
	public String getName() {return name;}
}
