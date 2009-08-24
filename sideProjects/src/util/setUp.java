package util;

import com.jme.math.Vector3f;
import com.jme.scene.Node;

public class setUp {
	public enum size{
		Average{ public Vector3f getObjectSize() { return new Vector3f(0.040953137f, 0.040953137f, 0.040953137f);}},
		Skinny{ public Vector3f getObjectSize() { return new Vector3f(); }},
		Brolic{ public Vector3f getObjectSize() { return new Vector3f(); }},
		Small{ public Vector3f getObjectSize() { return new Vector3f(); }};
				
		 public abstract Vector3f getObjectSize();			
	}
	public enum Direction{
		FORWARD{ public Vector3f getDirection(Node theNode) { return theNode.getLocalRotation().getRotationColumn(0); }},
		BACKWARD{ public Vector3f getDirection(Node theNode) { return theNode.getLocalRotation().getRotationColumn(0).negate(); }},
		LEFT{ public Vector3f getDirection(Node theNode) { return theNode.getLocalRotation().getRotationColumn(2).negate(); }},
		RIGHT{ public Vector3f getDirection(Node theNode) { return theNode.getLocalRotation().getRotationColumn(2); }},
		UP{ public Vector3f getDirection(Node theNode) { return theNode.getLocalRotation().getRotationColumn(1); }};		
		 public abstract Vector3f getDirection(Node theNode);			
	}
}
