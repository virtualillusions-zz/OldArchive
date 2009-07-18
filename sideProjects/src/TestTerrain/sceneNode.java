package TestTerrain;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jmex.terrain.TerrainPage;

public abstract class sceneNode extends Node{

		public sceneNode(String name){super(name);this.setIsCollidable(true);}
		
		/**the beginning start point of the map for campaign*/
		public abstract Vector3f startpoint();
		
		/**the spawn point of the map for multiplayer*/
		public abstract Vector3f spawnpoints();
		/**the spawn point of the map for multiplayer*/
		public abstract TerrainPage getTerrain();
}
