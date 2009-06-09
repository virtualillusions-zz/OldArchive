package time.timeexample;
import java.util.Calendar;
import java.util.TimeZone;

public class ConsoleLineTimeexample {

  public static void main(String[] args) {
   
    TimeZone tz=TimeZone.getTimeZone("PST");
    
    Calendar now = Calendar.getInstance(tz);
    TimeZone Z=now.getTimeZone();
    String zone=Z.getDisplayName();
    
    
    
    int minute=now.get(Calendar.MINUTE);
    int hour=now.get(Calendar.HOUR);


    System.out.println(hour+":"+minute + " "+zone);
    
  }
}
