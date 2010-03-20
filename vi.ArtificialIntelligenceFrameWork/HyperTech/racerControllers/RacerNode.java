package racerControllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;

import utill.HyperNode;
import utill.gameSingleton;

import com.jme.bounding.BoundingBox;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.FogState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.ZBufferState;

import com.jmex.simplephysics.DynamicCollider;
import com.jmex.simplephysics.EllipseCollider;

public class RacerNode extends Node implements Serializable,HyperNode{

		private static final long serialVersionUID = 1L;
		private AIController ai = null;
		private playerController player = null;
		private DynamicCollider dynamicNode;
		private HashMap<String,Integer> properties;
		private MoveController movement;
		public RacerNode(Node rootNode){
			
		name = "Unaltered Racer";			
					
		setupLight();				
		//By Default AI is set
		ai = new AIController(this);
		this.addController(ai);
		this.setIsCollidable(true);
		
		rootNode.attachChild(this);
		rootNode.updateGeometricState(0, true);
		rootNode.updateRenderState();
		properties=new HashMap<String,Integer>();
		properties.put("Handle", 0);
		properties.put("Max Speed", 0);
		properties.put("Thrust", 0);
		properties.put("Boost", 0);
		properties.put("Shield", 0);
		properties.put("Mass", 0);
		properties.put("Breaks", 0);		
		
		}
		/**
		 * Attaches the Visual racer Node to RacerNode
		 * @param spat the Visual Representation to of the Racer 
		 */
		public void setLoadedSpatial(Spatial spat){
			try{
				if(this.getChildren().size()>1){System.out.println("Warning previous impl present");return;}
			}catch(Exception e){
				spat.setModelBound(new BoundingBox());
				spat.updateModelBound();
				this.attachChild(spat);
				this.updateGeometricState(0, true);
				this.updateRenderState();
				
				dynamicNode = new EllipseCollider(spat.getLocalTranslation(), spat.getLocalScale(), spat);
				dynamicNode.getPhysicState().setGravity(new Vector3f(0,-100f,0));
				gameSingleton.get().getPhysics().attachDynamic(dynamicNode).getPhysicMaterial().set(0.05f, 1, 0.7f, 0.05f, true);	
				movement = new MoveController(dynamicNode);
			}
		}	
		
		/**Sets this Node as a Human Player*/
		public void setAsHuman() {
			if(isHuman()){System.out.println("player controls already attached");return;}
				this.removeController(ai);
				ai=null;
				player = new playerController(this);
				this.addController(player);
		}
		/**Sets this Node as a Computer controlled Player*/
		public void setAsComp() {
			if(!isHuman()){System.out.println("ai controls already attached");return;}
				this.removeController(player);
				player=null;
				ai = new AIController(this);
				this.addController(ai);
		}
		/**Returns if this Node is player controlled or computer controlled*/
		public boolean isHuman(){
			return this.getControllers().contains(player);
		}
		public void update(float tpf){

		}
		@Override
		public void mainUpdate(float tpf){
	   		
		}
		
		private void setupLight(){
			ZBufferState buf = gameSingleton.get().getDisplay.getRenderer().createZBufferState();
	        buf.setEnabled(true);
	        buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
	        this.setRenderState(buf);   
	        //Sets up lighting
	        final PointLight light = new PointLight();
	        light.setDiffuse(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.75f));
	        light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
	        light.setLocation(new Vector3f(10, 180, 100));
	        light.setEnabled(true);

	        final LightState lightState = gameSingleton.get().getDisplay.getRenderer().createLightState();
	        lightState.setEnabled(true);
	        lightState.attach(light);
	        this.setRenderState(lightState); 
	        
	        FogState fs = gameSingleton.get().getDisplay.getRenderer().createFogState();
			fs.setDensity(.005f);	     
						
			fs.setColor(new ColorRGBA(.9411764706f,1.0f,.9450980392f,1.0f));

	        fs.setStart(200);
	        fs.setEnd(400);	       
	        fs.setDensityFunction(FogState.DensityFunction.Linear);
	        fs.setQuality(FogState.Quality.PerVertex);	   
	        fs.setEnabled(true);
	       this.setRenderState(fs);
		}

		/**
		 * Get a property of this entity.
		 * @param propertyName the property name to retrieve.
		 * @return The entity's property linked to propertyName.
		 */
		public Object getProperty(String propertyName) {
			return properties.get(propertyName);
		}

		/**
		 * Binds a property name of the entity with it's property object.
		 * @param propertyName the property name.
		 * @param property the property to bind with the name.
		 */
		public void setProperty(String propertyName, int property) {
		if(!properties.containsKey(propertyName)){System.out.println("property value does not exsist");return;}
			properties.put(propertyName,property);
		}
}

