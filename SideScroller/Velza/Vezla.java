

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.vza.characters.modelDirector;
import com.vza.levels.stageDirector;

public class Vezla extends SimpleApplication{
	public static void main(String[] args){		
		final Vezla app = new Vezla();
		app.start();
	}	
	/**@see stageDirector*/
	private stageDirector sd;
	/**@see modelDirector*/
	private modelDirector md;  
	
	@Override
	public void simpleInitApp() {
		cam.setFrustumFar(10000);
		flyCam.setMoveSpeed(100);
		//Remember to export stages as jMElevels and models as jme files for speed
		//jme model files can encorporate material files
		sd = new stageDirector(rootNode,manager);
		sd.setActive("TestStage");
		sd.loadStage();
		md = new modelDirector(rootNode,manager,inputManager); 
	  //System.exit(0);		
	}
	@Override
	public void simpleUpdate(float tpf){
		md.update(tpf);
    }
	@Override
    public void simpleRender(RenderManager rm){
		md.render(rm);
    }
    
    @Override
    public void destroy(){
    	sd.cleanup();
    	md.cleanup();
    	super.destroy();
    }
}
