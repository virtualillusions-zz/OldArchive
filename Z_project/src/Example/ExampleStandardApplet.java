package Example;

	import java.util.concurrent.Callable;

	import com.jme.bounding.BoundingSphere;
	import com.jme.math.Vector3f;
	import com.jme.renderer.ColorRGBA;
	import com.jme.scene.shape.Box;
	import com.jme.util.GameTaskQueueManager;
	import com.jmex.awt.applet.StandardApplet;
	import com.jmex.game.state.DebugGameState;
	import com.jmex.game.state.GameStateManager;

	public class ExampleStandardApplet extends StandardApplet {

		private static final long serialVersionUID = 1L;

		public void init() {
			setBackgroundColor(ColorRGBA.blue.clone());
			super.init();
			GameTaskQueueManager.getManager().update(new Callable<Void>() {

				public Void call() throws Exception {
					// Create a DebugGameState - has all the built-in features that SimpleGame provides
					// NOTE: for a distributable game implementation you'll want to use something like
					// BasicGameState instead and provide control features yourself.
					DebugGameState state = new DebugGameState();
					// Put our box in it
					Box box = new Box("my box", new Vector3f(0, 0, 0), 2, 2, 2);
				    box.setModelBound(new BoundingSphere());
				    box.updateModelBound();
				    // We had to add the following line because the render thread is already running
				    // Anytime we add content we need to updateRenderState or we get funky effects
				    state.getRootNode().attachChild(box);
				    box.updateRenderState();
					// Add it to the manager
					GameStateManager.getInstance().attachChild(state);
					// Activate the game state
					state.setActive(true);
					
					return null;
				}
			});
		}
	}

