package com.tps1.aMain;

import com.jme.math.Vector3f;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;
import com.tps1.GameState.DefineGameState;
import com.tps1.character.Charactertype;
import com.tps1.lvlLoader.CopyOfCopyOflevelTester;
import com.tps1.scene.SkyBoxManager.SkyBoxGameState;

public class LAUNCHER {
    public static void main(String[] args) throws Exception{
        System.setProperty("jme.stats", "set");
        StandardGame standardGame = new StandardGame("GameControl", StandardGame.GameType.GRAPHICAL, null);
    standardGame.getSettings().setVerticalSync(false);
        standardGame.start();     

        SkyBoxGameState.Manager().setActive(true);
        CopyOfCopyOflevelTester nex = new CopyOfCopyOflevelTester(0,0);
        GameStateManager.getInstance().attachChild(nex);
        nex.setActive(true);
             
                Charactertype PLAYER1 = Charactertype.build("robot",true);
                Charactertype PLAYER2 = Charactertype.build("ninja",true);
                Charactertype PLAYER3 = Charactertype.build("robot",true);
                Charactertype PLAYER4 = Charactertype.build("ninja",true);
                Charactertype PLAYER5 = Charactertype.build("robot",true);
                Charactertype PLAYER6 = Charactertype.build("ninja",true);
                Charactertype PLAYER7 = Charactertype.build("robot",true);
                Charactertype PLAYER8 = Charactertype.build("ninja",true);
                Charactertype PLAYER9 = Charactertype.build("robot",true);
                Charactertype PLAYER10 = Charactertype.build("ninja",true);
                Charactertype PLAYER11 = Charactertype.build("robot",true);
                Charactertype PLAYER12 = Charactertype.build("ninja",true);
                Charactertype PLAYER13 = Charactertype.build("robot",true);
                Charactertype PLAYER14 = Charactertype.build("ninja",true);
                Charactertype PLAYER15 = Charactertype.build("robot",true);
                Charactertype PLAYER16 = Charactertype.build("ninja",true);
                Charactertype PLAYER17 = Charactertype.build("robot",true);
                Charactertype PLAYER18 = Charactertype.build("ninja",true);
                Charactertype PLAYER19 = Charactertype.build("robot",true);
                Charactertype PLAYER20 = Charactertype.build("ninja",true);
                Charactertype PLAYER21 = Charactertype.build("robot",true);
                Charactertype PLAYER22 = Charactertype.build("ninja",true);
                Charactertype PLAYER23 = Charactertype.build("robot",true);
                Charactertype PLAYER24 = Charactertype.build("ninja",true);
                Charactertype PLAYER25 = Charactertype.build("robot",true);
                Charactertype PLAYER26 = Charactertype.build("ninja",true);
                Charactertype PLAYER27 = Charactertype.build("robot",true);
                Charactertype PLAYER28 = Charactertype.build("ninja",true);
                Charactertype PLAYER29 = Charactertype.build("robot",true);
                Charactertype PLAYER30 = Charactertype.build("ninja",true);
                
                PLAYER1.getRootNode().setLocalTranslation(new Vector3f(40,0, 50));
                PLAYER2.getRootNode().setLocalTranslation(new Vector3f(40,0, 55));
                PLAYER3.getRootNode().setLocalTranslation(new Vector3f(40,0, 60));
                PLAYER4.getRootNode().setLocalTranslation(new Vector3f(40,0, 65));
                PLAYER5.getRootNode().setLocalTranslation(new Vector3f(40,0, 70));
                PLAYER6.getRootNode().setLocalTranslation(new Vector3f(40,0, 75));
                PLAYER7.getRootNode().setLocalTranslation(new Vector3f(40,0, 80));
                PLAYER8.getRootNode().setLocalTranslation(new Vector3f(40,0, 85));
                PLAYER9.getRootNode().setLocalTranslation(new Vector3f(40,0, 90));
                PLAYER10.getRootNode().setLocalTranslation(new Vector3f(40,0, 95));
                PLAYER11.getRootNode().setLocalTranslation(new Vector3f(50,0, 50));
                PLAYER12.getRootNode().setLocalTranslation(new Vector3f(50,0, 55));
                PLAYER13.getRootNode().setLocalTranslation(new Vector3f(50,0, 60));
                PLAYER14.getRootNode().setLocalTranslation(new Vector3f(50,0, 65));
                PLAYER15.getRootNode().setLocalTranslation(new Vector3f(50,0, 70));
                PLAYER16.getRootNode().setLocalTranslation(new Vector3f(50,0, 75));
                PLAYER17.getRootNode().setLocalTranslation(new Vector3f(50,0, 80));
                PLAYER18.getRootNode().setLocalTranslation(new Vector3f(50,0, 85));
                PLAYER19.getRootNode().setLocalTranslation(new Vector3f(50,0, 90));
                PLAYER20.getRootNode().setLocalTranslation(new Vector3f(50,0, 95));
                PLAYER21.getRootNode().setLocalTranslation(new Vector3f(60,0, 50));
                PLAYER22.getRootNode().setLocalTranslation(new Vector3f(60,0, 55));
                PLAYER23.getRootNode().setLocalTranslation(new Vector3f(60,0, 60));
                PLAYER24.getRootNode().setLocalTranslation(new Vector3f(60,0, 65));
                PLAYER25.getRootNode().setLocalTranslation(new Vector3f(60,0, 70));
                PLAYER26.getRootNode().setLocalTranslation(new Vector3f(60,0, 75));
                PLAYER27.getRootNode().setLocalTranslation(new Vector3f(60,0, 80));
                PLAYER28.getRootNode().setLocalTranslation(new Vector3f(60,0, 85));
                PLAYER29.getRootNode().setLocalTranslation(new Vector3f(60,0, 90));
                PLAYER30.getRootNode().setLocalTranslation(new Vector3f(60,0, 95));
                                  
                PLAYER1.setActive(true);
                PLAYER2.setActive(true);
                PLAYER3.setActive(true);
                PLAYER4.setActive(true);
                PLAYER5.setActive(true);
                PLAYER6.setActive(true);
                PLAYER7.setActive(true);
                PLAYER8.setActive(true);
                PLAYER9.setActive(true);
                PLAYER10.setActive(true);
                PLAYER11.setActive(true);
                PLAYER12.setActive(true);
                PLAYER13.setActive(true);
                PLAYER14.setActive(true);
                PLAYER15.setActive(true);
                PLAYER16.setActive(true);
                PLAYER17.setActive(true);
                PLAYER18.setActive(true);
                PLAYER19.setActive(true);
                PLAYER20.setActive(true);
                PLAYER21.setActive(true);
                PLAYER22.setActive(true);
                PLAYER23.setActive(true);
                PLAYER24.setActive(true);
                PLAYER25.setActive(true);
                PLAYER26.setActive(true);
                PLAYER27.setActive(true);
                PLAYER28.setActive(true);
                PLAYER29.setActive(true);
                PLAYER30.setActive(true); 
                
            final DefineGameState base = new DefineGameState(standardGame); 
            base.setActive(true);   
          }
}
