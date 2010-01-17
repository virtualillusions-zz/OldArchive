package testFlyEffects;

import jmetest.effects.TestBatchParticles;

import util.gameSingleton;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;

import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleInfluence;
import com.jmex.effects.particles.ParticleMesh;
import com.jmex.effects.particles.SimpleParticleInfluenceFactory;
import com.jmex.game.state.GameState;

public class TestFly extends GameState{

	private Node rootNode;
	
	public TestFly(){
	//	this.rootNode=gameSingleton.get().getRootNode();
      // gameSingleton.get().getRootNode().attachChild( gameSingleton.get().getCamNode);
		init();
		rootNode.updateGeometricState(0f, true);
		rootNode.updateRenderState();
	}
	
	private void init() {		    
	        Sphere fly = new Sphere("sp", 16, 16, 1);		
	        fly.setLocalTranslation(new Vector3f(-1,0,40));
			fly.setModelBound(new BoundingBox());
			fly.updateModelBound();		
			gameSingleton.get().getCamNode.attachChild(fly);
			texturize(fly);
			particle(fly);
	}
	 private void texturize(Spatial spatial) {
		 TextureState ts = gameSingleton.get().getRenderer.createTextureState();
	        ts.setEnabled(true);
	        Texture tex = TextureManager.loadTexture(
	            TestBatchParticles.class.getClassLoader().getResource(
	            "jmetest/data/images/Monkey.jpg"),
	            Texture.MinificationFilter.Trilinear,
	            Texture.MagnificationFilter.Bilinear);
	        ts.setTexture(tex);	
	        spatial.setRenderState(ts);
	}
	
	private void particle(Spatial spatial) {
		ParticleMesh pMesh = ParticleFactory.buildMeshParticles("particles", (TriMesh) spatial);
	        pMesh.setEmissionDirection(new Vector3f(1, 1, 1));
	        pMesh.setOriginOffset(new Vector3f(1, 1, 1));
	        pMesh.setInitialVelocity(.002f);
	        pMesh.setStartSize(1);
	        pMesh.setEndSize(1f);
	        pMesh.setMinimumLifeTime(1000f);
	        pMesh.setMaximumLifeTime(3000f);
	        pMesh.setStartColor(new ColorRGBA(1, 1, 1, 1));
	        pMesh.setEndColor(new ColorRGBA(1, 1, 1, 0));
	        pMesh.setMaximumAngle(0f * FastMath.DEG_TO_RAD);
	        pMesh.setParticleSpinSpeed(180 * FastMath.DEG_TO_RAD);
	        ParticleInfluence wind = SimpleParticleInfluenceFactory.createBasicWind(.008f, new Vector3f(-1, 0, 0), true, true);
	        wind.setEnabled(true);
	       // pMesh.addInfluence(wind);
	        pMesh.forceRespawn();   
	       // pMesh.setModelBound(new BoundingBox());
	        //pMesh.updateModelBound();
	      	        
	        
	        pMesh.setRenderState( spatial.getRenderState(RenderState.StateType.Texture) );
			gameSingleton.get().getCamNode.attachChild(pMesh);
			
			

	}

	@Override
	public void cleanup() {
		
	}

	@Override
	public void render(float tpf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float tpf) {

	}

}
