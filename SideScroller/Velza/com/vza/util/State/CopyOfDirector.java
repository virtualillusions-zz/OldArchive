package com.vza.util.State;

import com.jme3.renderer.RenderManager;

public interface CopyOfDirector{
	/**
	 * Get the name of the State
	 */
	public String getName();
	
	/**
	 * used to check if the state is active or inactive 
	 * @return is Active
	 */
	public boolean isActive();
	
	/**
	 * Used to set if the GameState is active or inactive
	 * @param active
	 */
	public void setActive(boolean active);
	
	/**
	 * Gets called every successive frame
	 * @param tpf The Elapsed Time since the Last Frame
	 */
	public abstract void update(float tpf);
	
	/**
	 * Used to Directly Access the Render Manager
	 * @param rm
	 */
	public abstract void render(RenderManager rm);		
	/**
	 * used to perform all last minute actions before the applocation closes
	 */
	public abstract void cleanup();
	
}	
