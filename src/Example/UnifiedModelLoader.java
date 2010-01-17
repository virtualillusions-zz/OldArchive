package Example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.jme.scene.Node;
import com.jme.util.export.Savable;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.AseToJme;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.MaxToJme;
import com.jmex.model.converters.Md2ToJme;
import com.jmex.model.converters.Md3ToJme;
import com.jmex.model.converters.MilkToJme;
import com.jmex.model.converters.ObjToJme;
import com.jmex.model.util.ModelLoader;

public class UnifiedModelLoader {
	/*
	 *  This method opens a model in various format evaluating the extension
	 *  In case in the same directory is already presents the same model in jbin format loads it
	 *  Otherways load the model and save a jbin copy for the next time.
	 *  
	 *  Attention : in case the original model is changed you'll have to delete the jbin one the reload it. 
	 */		
	public static Node loadModel (String modelFile){
		Node			loadedModel	= null;
		FormatConverter		formatConverter = null;		
		ByteArrayOutputStream 	BO 		= new ByteArrayOutputStream();
		String			modelFormat 	= modelFile.substring(modelFile.lastIndexOf(".") + 1, modelFile.length());
		String			modelBinary	= modelFile.substring(0, modelFile.lastIndexOf(".") + 1) + "jbin";
		URL			modelURL	= ModelLoader.class.getClassLoader().getResource(modelBinary);
		
		//verify the presence of the jbin model
		if (modelURL == null){
			
			modelURL		= ModelLoader.class.getClassLoader().getResource(modelFile);
			
			//evaluate the format
			if (modelFormat.equals("3ds")){
				formatConverter = new MaxToJme();
			} else if (modelFormat.equals("md2")){
				formatConverter = new Md2ToJme();
			} else if (modelFormat.equals("md3")){
				formatConverter = new Md3ToJme();
			} else if (modelFormat.equals("ms3d")){
				formatConverter = new MilkToJme();
			} else if (modelFormat.equals("ase")){
				formatConverter = new AseToJme();
			} else if (modelFormat.equals("obj")){
				formatConverter = new ObjToJme();
			}
			formatConverter.setProperty("mtllib", modelURL);
			
			try {
				formatConverter.convert(modelURL.openStream(), BO);
				loadedModel = (Node) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
				
				//save the jbin format
				BinaryExporter.getInstance().save((Savable)loadedModel, new File(modelBinary));
			} catch (IOException e) {				
				e.printStackTrace();
				return null;
			}
		}else{
			try {
				//load the jbin format
				loadedModel = (Node) BinaryImporter.getInstance().load(modelURL.openStream());
			} catch (IOException e) {
				return null;
			}
		}
		
		return loadedModel;
	}
}
