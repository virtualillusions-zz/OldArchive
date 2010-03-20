
import java.util.logging.Level;

import utill.ObjectManager;
import utill.gameSingleton;

import com.jmex.game.StandardGame;
import com.jmex.game.state.DebugGameState;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;

public final class HyperTechGame extends GameState{
	 public static void main(String[] args) throws Exception{		  
	    	new HyperTechGame(new StandardGame("GameControl", StandardGame.GameType.GRAPHICAL, null));	    
	    }
	  
	 public HyperTechGame(final StandardGame openGL){
		 System.setProperty("jme.stats", "set");
		 openGL.start();
		 gameSingleton.init(openGL).setActive(true);
 	      
	      ObjectManager.init();      
	      ObjectManager.get().setActive(true);
	      
	     // setupDebugger();
	      
	     init();
	 }
	 
	 private void setupDebugger(){
		    DebugGameState bugger = new DebugGameState();
		    GameStateManager.getInstance().attachChild(bugger);
		  //bugger.getRootNode().attachChild(characterManager.get().getRootNode());
		    bugger.setActive(true);
	 }
	 
		/**
		 * The update method of the main Thread
		 * @Intented_Use for Physics and AI
		 */
		private void mainUpdate(float tpf){			
			gameSingleton.get().getPhysics().update(tpf);
			ObjectManager.get().mainUpdate(tpf);		
		}
	 /**
	  * Initialized the mainUpdate loop
	  * The loop is regulated by the OpenGL update
	  */
	 private void init(){
		 GameStateManager.getInstance().attachChild(this);
			this.setActive(true);
			
			gameSingleton.get().getPhysics().build(); //Build octrees (may take a while)
			System.out.println("Collision-Scene Built Sucesffully!!");
			//////////////////////////////
			try {
				 Status = false;
				 openGLTPF=0;
				while (this.isActive()){
					
					if(Status==true)
					{mainUpdate(openGLTPF);}
					Status=false;
					
					Thread.yield();
				}
			} catch (Throwable t) {
				gameSingleton.getLogger.logp(Level.SEVERE, this.getClass().toString(), "start()", "Exception in game loop", t);
		 	  }			
			//////////////////////
			System.out.println("main Thread Loop is ending");	
	 }

		@Override
		public void cleanup() {this.setActive(false);}

		@Override
		public void render(float tpf) {this.openGLTPF=tpf;}
	
		@Override
		public void update(float tpf) {Status=true;	}	 
		boolean Status;	float openGLTPF;	 
}
