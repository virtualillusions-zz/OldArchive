package time.timeexample;

import org.fenggui.util.Timer;


public class seconds_counter implements Runnable{
	//public class ThreadCountdownImpRunnable implements Runnable {

	    private int countDown = 5;
	    private static int threadCount = 0;
	    private int threadNumber = ++threadCount;

	    /**
	     * Constructs a ThreadCountdownImpRunnable object that will executed in a 
	     * separate thread.
	     */
	    public seconds_counter() {
	        System.out.println("Countdown starting");
	    }

	    /**
	     * A callback method that will be called by the start() method of the Thread 
	     * class. Keep in mind though, that this run() method WILL NOT override the
	     * run() method within the Thread class. This method will be called by the
	     * run() method of the Thread class as follows:
	     * 
	     * Thread.start() -> Thread.run() -> YourRunnableObject.run()
	     * 
	     * This method is very similar to the main() method of a typical 
	     * class. A new thread begins execution with the run() method in the same 
	     * way a program begins execution with the main() method.
	     * <p>
	     * While the main() method receives its arguments from the argv parameter
	     * (which is typically set from the command line), the newly created thread
	     * must receive its arguments programmatically from the originating thread.
	     * Hence, parameters can be passed in via the constructor, static instance
	     * variables, or any other technique designed by the developer.
	     */
	    public void run() {
	        while(true) {

	            System.out.println(countDown );

	           for (int j = 0; j < 300000000; j++) {
	                // This is a test...
	            }
	            
	            if (--countDown == 0) {
	                System.out.println(
	                    "GoodBye");
	                return;
	            }
	        }
	    }


	    /**This method will perform a sleep operation 
	     * before starting each thread to allow the output to reflect how each
	     * thread operates.
	     * @exception java.lang.InterruptedException Thrown from the Thread class.
	     */
	    public static void doThreadCountdown()
	        throws java.lang.InterruptedException {
	        
	            Thread.sleep(2000);
	           Runnable ot = new seconds_counter();
	            Thread th = new Thread(ot);
	            th.start();
	      	    }


	    /**
	     * Sole entry point to the class and application.
	     * @param args Array of String arguments.
	     * @exception java.lang.InterruptedException Thrown from the Thread class.
	     */
	    public static void main(String[] args)
	        throws java.lang.InterruptedException {
	        
	       doThreadCountdown();


	    }
	
		}

