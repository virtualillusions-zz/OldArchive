package util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.scene.Node;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.model.ModelFormatException;
import com.jmex.model.ogrexml.MaterialLoader;
import com.jmex.model.ogrexml.MeshCloner;
import com.jmex.model.ogrexml.OgreLoader;
 
public class ogre {
    private Node model;
   
  //store character nodes
	private static HashMap<String, ogre> charList = new HashMap<String, ogre>();  
	public static HashMap<String, ogre> getcharList(){return charList;}
	/**
	 * returns character geometry generated from ogre class
	 * @return ogreNode
	 */
	public static Node getCharacter(String theName)
	{ 
		if(!getcharList().containsKey(theName))
			getcharList().put(theName, new ogre(theName));			
		return ((ogre)getcharList().get(theName)).newClone();
	}    
    
    
    private String modelpath = "data/models/";

    private String name;

    /**
     * @author Kyle Williams
     * @param modelName
     * @return Node Model
     * @Description loads a model from file**Remember to create a jbin converter.
     */
    private ogre(String modelName){
    	name=modelName;
    	loadModel(modelName);
    }
    
	private void loadModel(String modelName)
	{
		try {
            SimpleResourceLocator locator = new SimpleResourceLocator(ogre.class
                                                    .getClassLoader()
                                                    .getResource(modelpath+"/"+modelName+"/"));
            ResourceLocatorTool.addResourceLocator(
                    ResourceLocatorTool.TYPE_TEXTURE, locator);
            ResourceLocatorTool.addResourceLocator(
                    ResourceLocatorTool.TYPE_MODEL, locator);
        } catch (URISyntaxException e1) {
            Logger.getLogger(ogre.class.getName()).log(Level.SEVERE, "Unable to Locate Character data", e1);
        }
		
		
	  	OgreLoader loader = new OgreLoader();
        MaterialLoader matLoader = new MaterialLoader();
        try {
            URL matURL = ogre.class.getClassLoader().getResource(modelpath+modelName+"/Example.material");
            URL meshURL = ogre.class.getClassLoader().getResource(modelpath+modelName+"/"+modelName+".mesh.xml");
            
            if (meshURL == null)throw new IllegalStateException("Required runtime resource missing: "+ meshURL);
            if (matURL == null)throw new IllegalStateException("Required runtime resource missing: " + matURL);
            
            if (matURL != null){
                matLoader.load(matURL.openStream());
                if (matLoader.getMaterials().size() > 0)
//                	matLoader.getMaterials().put("Examples/Robot",matLoader.getMaterials().get("Examples/Ninja"));
                    loader.setMaterials(matLoader.getMaterials());
            }
                	model = (Node) loader.loadModel(meshURL);
                	
        } catch (IOException ex) {
            //    ErrorHandler.reportError("Unable to Locate "+modelName+" Character data", ex);
                Logger.getLogger(ogre.class.getName()).log(Level.SEVERE, "Unable to Locate Character data", ex);
        } catch (ModelFormatException e) {
        	// ErrorHandler.reportError("Unable to Locate "+modelName+" Character data", e);
		}finally {loader = null; matLoader = null; /* encourage GC*/ }
        
	}
	
	/**
	 * returns on clone of an original model
	 * @return the cloned model
	 */
	private Node newClone(){        
        Node clone = MeshCloner.cloneMesh(model);
        clone.setName(name);
		return clone;
	}
	
	public String getName() {
		return name;
	}
}
