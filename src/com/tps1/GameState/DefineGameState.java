package com.tps1.GameState;

import com.acarter.scenemonitor.SceneMonitor;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.renderer.Renderer;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.StatisticsGameState;
import com.jmex.physics.PhysicsDebugger;
import com.tps1.aMain.Game;

public class DefineGameState extends BasicGameState {
	
	/**Returns the DisplaySystem*/
	private static DisplaySystem display =DisplaySystem.getDisplaySystem(Game.getSettings().getRenderer());
	//////////
	protected InputHandler input;
	////////////
    protected boolean statisticsCreated = false;
    ////////////
	public DefineGameState() {
		super("PhysicsDebugger");
        KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
        
        ////////////////////SET UP PHYSICS DEBUGGER/////////////
        input = new InputHandler();
        input.addAction( new InputAction() {
            public void performAction( InputActionEvent evt ) {
                if ( evt.getTriggerPressed() ) {
                    showPhysics = !showPhysics;
                }
            }
        }, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_V, InputHandler.AXIS_NONE, false );
 	   /////////////////////SETUP SCENEMONITOR///////////////////
        showSceneMonitor=false;
        input.addAction( new InputAction() 
        {
            public void performAction( InputActionEvent evt ) 
            {
                if ( evt.getTriggerPressed() ) 
                {
                	showSceneMonitor = !showSceneMonitor;
                	if(showSceneMonitor==true)
                	{
                	registerSceneNodes();
                	SceneMonitor.getMonitor().showViewer(true);
                	}
                }
            }
        }, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_B, InputHandler.AXIS_NONE, false );
        
 	    ////////////////////STATISTICS
        KeyBindingManager.getKeyBindingManager().set("toggle_stats",
                KeyInput.KEY_C);
        
        GameStateManager.getInstance().attachChild(this);        
    }
	/** 
	 * Empty.
	 * 
	 * @see GameState#cleanup()
	 */
    protected boolean showPhysics, showSceneMonitor;

	public void cleanup()
	{
		 System.out.println("Good Bye");
         System.exit(0);
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
		
		//////////////////////////////////
		input.update( tpf );
		///////////////////////////////////
		
		 // check if ESC has been pressed, exit the application if needed 
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit", false))
        { cleanup();SceneMonitor.getMonitor().cleanup();} //OK, this is just a demo...
        if( showSceneMonitor)
        { 
        	SceneMonitor.getMonitor().updateViewer(tpf);
        }
        
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
        if ( showPhysics ) {
            PhysicsDebugger.drawPhysics( gameSingleton.get().getPhysicsSpace(), r );
        }
        
        if( showSceneMonitor) {
    		SceneMonitor.getMonitor().renderViewer(display.getRenderer());
        }        
    }
    
    private void registerSceneNodes(){
    	for(int i = 0 ; i<GameStateManager.getInstance().getQuantity(); i++)
    	{
        SceneMonitor.getMonitor().registerNode(((BasicGameState) GameStateManager.getInstance().getChild(i)).getRootNode());
       	}
    }
}
