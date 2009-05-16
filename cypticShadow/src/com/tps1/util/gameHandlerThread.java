package com.tps1.util;

import com.jmex.physics.PhysicsSpace;

public class gameHandlerThread {
	 //the one and only instance of the gameSingleton*/
	private static gameHandlerThread instance = null;	
	
	private gameHandlerThread() {
		
	}

	 /**
	 * @return the one and only isntance of this game
	 */
	 public static gameHandlerThread get() {
	        if (instance == null) {
	        	instance = new gameHandlerThread();
	        }
	        return instance;
	    }
	 
	 
	 public static class threadWorker implements Runnable {

		    public void run() {
		        threadWorker handler = new threadWorker();
		        PhysicsSpace p = PhysicsSpace.create();
		        
		        
		    }

		    /**
			 * @return the physics space for this game
			 */
			// public PhysicsSpace getPhysicsSpace() {
				//return physics;
			//}
			 
		    private static void main(String args[]) {
		        (new Thread(new threadWorker())).start();
		    }
		}
}
