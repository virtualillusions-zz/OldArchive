package TestTerrain;

import util.gameSingleton;

import com.jme.light.DirectionalLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jmex.terrain.TerrainPage;

public abstract class sceneNode extends Node{
		protected FogState fs;
		public sceneNode(String name){super(name);	}
		/**
		 * Loads a terrain
		 * @return terrain
		 */
		public sceneNode startup(){
			// Set basic render states
			CullState cs = gameSingleton.get().getRenderer.createCullState();
			cs.setCullFace(CullState.Face.Back);
			cs.setEnabled(true);
			this.setRenderState(cs);

			// Some light
			DirectionalLight dl = new DirectionalLight();
			dl.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
			dl.setDirection(new Vector3f(1, -0.5f, 1));
			dl.setEnabled(true);
			gameSingleton.get().getLightState.attach(dl);
			this.setIsCollidable(true);
			setupTerrain();
			fogState();
			this.updateGeometricState(0, true);
			this.updateRenderState();
			return this;
		}
		
		/**the beginning start point of the map for campaign*/
		public void fogState(){
			fs = gameSingleton.get().getRenderer.createFogState();
			fs.setDensity(.005f);	     
						
			fs.setColor(new ColorRGBA(.9411764706f,1.0f,.9450980392f,1.0f));

	        fs.setStart(0);
	        fs.setEnd(200);	       
	        fs.setDensityFunction(FogState.DensityFunction.Linear);
	        fs.setQuality(FogState.Quality.PerVertex);	   
	        fs.setEnabled(true);
	      // this.setRenderState(fs);
		}
		
		/**the beginning start point of the map for campaign*/
		public abstract void setupTerrain();
		
		/**the beginning start point of the map for campaign*/
		public abstract Vector3f startpoint();
		
		/**the spawn point of the map for multiplayer*/
		public abstract Vector3f spawnpoints();
		/**return the terrain used*/
		public abstract TerrainPage getTerrain();
		
		
}
