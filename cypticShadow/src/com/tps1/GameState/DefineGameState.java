package com.tps1.GameState;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.renderer.Renderer;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.Debugger;
import com.jmex.game.StandardGame;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.StatisticsGameState;
import com.tps1.aMain.Game;

public class DefineGameState extends BasicGameState {
	
	/**Returns the DisplaySystem*/
	private static DisplaySystem display =DisplaySystem.getDisplaySystem(Game.getSettings().getRenderer());
	//////////
	protected InputHandler input;
	////////////
    protected boolean statisticsCreated = false;
    ////////////
    /**
     * True if the renderer should display bounds.
     */
    protected boolean showBounds = false;
    StandardGame standardGame;
	public DefineGameState(StandardGame standardGame) {
		super("Debugger");
		this.standardGame = standardGame;
        KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
        
        input = new InputHandler();
       

        
        
 	    ////////////////////STATISTICS
        KeyBindingManager.getKeyBindingManager().set("toggle_stats", KeyInput.KEY_C);
        /////model bounds        
        /** Assign key v to action "toggle_bounds". */
        KeyBindingManager.getKeyBindingManager().set( "toggle_bounds", KeyInput.KEY_V );
        
        GameStateManager.getInstance().attachChild(this);        
    }
	/** 
	 * Empty.
	 * 
	 * @see GameState#cleanup()
	 */
    protected boolean showPhysics;

	public void cleanup()
	{	
		 System.out.println("Closing Application");
		
         standardGame.finish();
	}

	/**
	 * Gets called every frame before render(float) by the parent 
	 * <code>GameStateNode</code>.
	 * 
	 * @param tpf The elapsed time since last frame.
	 */
	public void update(float tpf)
	{
		super.update(tpf);
		 //////////////////////////////////////////////////////////////////
       
    	input.update( tpf );
		///////////////////////////////////        
		
		/** If toggle_bounds is a valid command (via key v), change bounds. */
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "toggle_bounds", false ) ) {
            showBounds = !showBounds;
        }
		//////////////////////////////////	
		
		 // check if ESC has been pressed, exit the application if needed 
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit", false)){ cleanup();}
      ////////////////////////////////////////////////////////////////////////////////
        /** If toggle_stats is a valid command (via key F4), change depth. */
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_stats", false))
        {
        	if (statisticsCreated == false) {
                // create a statistics game state
                GameStateManager.getInstance().attachChild(
                		new StatisticsGameState("stats", 1f, 0.25f, 0.75f, true));
                statisticsCreated = true;
        	}
            GameStateManager.getInstance().getChild("stats").setActive(
                    !GameStateManager.getInstance().getChild("stats").isActive());
        }

	}
	
	public DisplaySystem getDisplay() {
		return display; 
	}
	
	
	 @Override
	public final void render( float interpolation ) {
	        super.render( interpolation );
	       
//////////////////////////
	          doDebug(display.getRenderer());
//////////////////////////
	    }
	 
    /////////////////////////////////////
    protected void doDebug(Renderer r) {
               
    	 /**
         * If showing bounds, draw rootNode's bounds, and the bounds of all its
         * children.
         */
        if ( showBounds ) {
        	registerBounds(r);
        }
       
    }
       
    private void registerBounds(Renderer r){
    	for(int i = 0 ; i<GameStateManager.getInstance().getQuantity(); i++)
    	{
    		Debugger.drawBounds(((BasicGameState) GameStateManager.getInstance().getChild(i)).getRootNode(),r,true);
       	}
    }
}
