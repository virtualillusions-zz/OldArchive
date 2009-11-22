package Example;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingSphere;
import com.jme.input.KeyInput;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

public class HelloCelShader extends SimpleGame{
	 public static void main(String[] args) {
		 HelloCelShader app = new HelloCelShader();
	        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
	        app.start();
	    }  
	
	  private boolean outlineSmooth = true;                      // Flag To Anti-Alias The Lines ( NEW )
	  private boolean outlineDraw = true;                      // Flag To Draw The Outline ( NEW )
	  // User Defined Variables
	    private float[] outlineColor = {0.0f, 0.0f, 0.0f};   // Color Of The Lines ( NEW )
	    private float outlineWidth = 3f;                     // Width Of The Lines ( NEW )
	    private boolean increaseWidth=false;
	    private boolean decreaseWidth=false;
	    private KeyInput key;
	   
	@Override
	protected void simpleInitGame() {
		key = KeyInput.get();
		Box b=new Box("My Box",new Vector3f(0,0,0),new Vector3f(1,1,1));
        // Give the box a bounds object to allow it to be culled
        b.setModelBound(new BoundingSphere());
        // Calculate the best bounds for the object you gave it
        b.updateModelBound();
        // Move the box 2 in the y direction up
        b.setLocalTranslation(new Vector3f(0,2,0));
        // Give the box a solid color of blue.
        b.setDefaultColor(ColorRGBA.blue.clone());
        b.setLocalScale(5);
     // Remove  lighting for rootNode so that it will use our basic colors.
        rootNode.setLightCombineMode(Spatial.LightCombineMode.Off);
        rootNode.attachChild(b);
		
	}

	 public void toggleOutlineSmooth() {this.outlineSmooth = !outlineSmooth;   }
	 public void toggelOutlineDraw() {this.outlineDraw = !outlineDraw; }
	 public void increaseOutlineWidth(boolean increase) {increaseWidth = increase;}
	 public void decreaseOutlineWidth(boolean decrease) {decreaseWidth = decrease;}
	 
	 @Override
	 public void simpleUpdate() {
		 	if(key.isKeyDown(KeyInput.KEY_I)){outlineWidth += 1;}// Increase Line Width ( NEW )
            if(key.isKeyDown(KeyInput.KEY_K)){outlineWidth -= 1;} // Decrease Line Width ( NEW ) 
	        if(key.isKeyDown(KeyInput.KEY_J)){toggleOutlineSmooth();} 
	        if(key.isKeyDown(KeyInput.KEY_L)){toggelOutlineDraw();} 
	 }
	 
	 @Override
	 public void simpleRender() {
		// 	if (outlineSmooth) {                                          // Check To See If We Want Anti-Aliased Lines ( NEW )
	    //        gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);           // Use The Good Calculations ( NEW )
	    //        gl.glEnable(GL.GL_LINE_SMOOTH);                           // Enable Anti-Aliasing ( NEW )
	    //    } else                                                        // We Don't Want Smooth Lines ( NEW )
	    //        gl.glDisable(GL.GL_LINE_SMOOTH);                          // Disable Anti-Aliasing ( NEW )
  }

}
