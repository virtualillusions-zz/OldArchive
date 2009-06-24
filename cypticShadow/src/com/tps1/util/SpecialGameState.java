package com.tps1.util;

import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.BasicGameStateNode;
import com.jmex.game.state.GameState;

public abstract class SpecialGameState extends GameState{

	 Spatial gameStateRoot;
     Node sceneRoot;
     BasicGameState sceneRootGameState;

     public SpecialGameState(BasicGameState sceneRootGameState, String name) {
    	 
         gameStateRoot = new Node(name);
      
         ((Node) gameStateRoot).attachChild(gameStateRoot);
         
     }
     public abstract void updater(float tpf);
     
     public void update(float tpf){updater(tpf);}

}
