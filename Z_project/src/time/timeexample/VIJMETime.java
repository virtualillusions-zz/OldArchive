package time.timeexample;

import java.util.Calendar;
import java.util.TimeZone;

public class VIJMETime{	  
	   public String x;
		public String getTime(){
	    TimeZone tz=TimeZone.getTimeZone("PST");
	  
	    Calendar now = Calendar.getInstance(tz);
	    TimeZone Z=now.getTimeZone();
	    String zone=Z.getDisplayName();
	    	    
	    int minute=now.get(Calendar.MINUTE);
	    int hour=now.get(Calendar.HOUR);
	    
	    if (minute <= 9){ 
	          x = "" + hour + ":0" + minute +" "+zone;} 
	    else{  
	          x = "" + hour + ":" + minute +" "+zone;} 
	    
	    return x;
	    }

}
	    
