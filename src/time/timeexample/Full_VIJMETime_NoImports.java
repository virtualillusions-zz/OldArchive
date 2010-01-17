package time.timeexample;
import java.util.Calendar; 
import java.util.TimeZone; 
import java.awt.Font; 
import java.util.concurrent.Callable; 

import com.jme.math.Vector3f; 
import com.jme.util.GameTaskQueueManager; 
import com.jmex.font3d.Font3D; 
import com.jmex.font3d.Text3D; 
import com.jmex.game.StandardGame; 
import com.jmex.game.state.DebugGameState;
import com.jmex.game.state.GameStateManager; 
  
public class Full_VIJMETime_NoImports { 
  
public static void main(String[] args) { 
  
TimeZone tz=TimeZone.getTimeZone("GMT+3:00"); 
  
Calendar now = Calendar.getInstance(tz); 
TimeZone Z=now.getTimeZone(); 
String zone=Z.getDisplayName(); 
  
  
  
int minute=now.get(Calendar.MINUTE); 
int hour=now.get(Calendar.HOUR); 
String x; 
  
if (minute <= 9){ 
      x = "" + hour + ":0" + minute +" "+zone;} 
else{  
      x = "" + hour + ":" + minute +" "+zone;} 
  
final String s = x;  
  
StandardGame game = new StandardGame("Time"); 
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