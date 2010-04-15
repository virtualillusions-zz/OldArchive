
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.plugins.ogre.OgreMeshKey;

public class TestModelLoading extends SimpleApplication{
	public static void main(String[] args){
		TestModelLoading app = new TestModelLoading();
		app.start();
	}

	@Override
	public void simpleInitApp() {
	manager.registerLocator("/com/vza/characters/CharacterData/",ClasspathLocator.class.getName(),"material","meshxml","skeletonxml","j3m","jpg","tga");

        PointLight dl = new PointLight();
        dl.setPosition(new Vector3f(-0.1f, -0.7f, 1).normalizeLocal());
        dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
        rootNode.addLight(dl);
	        
	Node model = (Node) manager.loadContent(new OgreMeshKey("roboto.meshxml", null));
	model.setMaterial(manager.loadMaterial("roboto.j3m"));
	model.addLight(dl);

	rootNode.attachChild(model);
	     
	     //model.setAnimation("jump");
	  }
}
