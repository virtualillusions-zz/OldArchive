package com.vza.util.State;

public interface Director{
	/**
	 * Get the name of the State
	 */
	public String getName();
	
	/**
	 * used to perform all last minute actions before the applocation closes
	 */
	public abstract void cleanup();
	
}	
