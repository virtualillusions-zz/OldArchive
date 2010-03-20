import com.jme3.animation.Model;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.collision.SweepSphere;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.plugins.ogre.OgreMaterialList;
import com.jme3.scene.plugins.ogre.OgreMeshKey;

public class TestModelLoading extends SimpleApplication{
	public static void main(String[] args){
		TestModelLoading app = new TestModelLoading();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		manager.registerLocator("/Characters/CharacterData/",ClasspathLocator.class.getName(),"material","meshxml","skeletonxml","j3m","jpg","tga");
       // manager.registerLoader(, );
		DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -0.7f, 1).normalizeLocal());
        dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
        rootNode.addLight(dl);
	        
	    Model model = (Model) manager.loadContent(new OgreMeshKey("roboto.meshxml", null));	         
	    model.setMaterial(manager.loadMaterial("roboto.j3m"));
	     
	     rootNode.attachChild(model);		
	     
	     model.setAnimation("jump");
	  }
}
