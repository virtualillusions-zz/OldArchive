package time.timeexample;

//import java.util.Calendar; 
//import java.util.TimeZone; 
import java.awt.Font; 
import java.util.concurrent.Callable; 


import com.jme.math.Vector3f; 
import com.jme.util.GameTaskQueueManager; 
import com.jmex.font3d.Font3D; 
import com.jmex.font3d.Text3D; 
import com.jmex.game.StandardGame; 
import com.jmex.game.state.DebugGameState;
import com.jmex.game.state.GameStateManager; 

public class VIJMETimeTest { 
  
public static void main(String[] args) { 
 
	
VIJMETime myTime=new VIJMETime();

final String s = myTime.getTime();  

  
StandardGame game = new StandardGame("JMETime"); 
     game.start(); 
      
     final DebugGameState debug = new DebugGameState(); 
     GameStateManager.getInstance().attachChild(debug); 
     debug.setActive(true); 
      
     GameTaskQueueManager.getManager().update(new Callable<Object>() { 
          public Object call() throws Exception { 
               Font3D font = new Font3D(new Font("Arial", Font.PLAIN, 24), 0.001f, true, true, true); 
               Text3D text = font.createText(s, 50.0f, 0); 
               text.setLocalScale(new Vector3f(5.0f, 5.0f, 0.01f)); 
               debug.getRootNode().attachChild(text); 
               return null; 
          } 
     }); 
      
} 
}