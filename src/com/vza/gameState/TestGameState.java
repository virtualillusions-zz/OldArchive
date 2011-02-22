/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vza.gameState;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.vza.app.Player;
import com.vza.gameState.constant.GameState;


import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.nodes.PhysicsNode;
import com.jme3.input.KeyInput;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.shape.Box;
import com.vza.director.Director;
/**b
 *
 * @author Kyle Williams
 */
public class TestGameState implements GameState{

    public boolean isInitialized=false;
    public boolean active=true;
    @Override
    public Spatial[] getCharacters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public Spatial getScene() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
private DirectionalLight dl;
@Override
    public void initialize(AppStateManager stateManager, Application app) {
        isInitialized = true;
        Director.getGUI().fromXml("Interface/gameGUI.xml", "start");

        Director.createPlayer("testPlayer");
        Director.getPlayer("testPlayer").addIntoPlay();
        Director.buildCharacter("Reino", "testPlayer");
        Director.getFighterPhysics("testPlayer").setLocalTranslation(-2,5,0);
        //Director.getFighter("testPlayer").lookRight();
        Director.createPlayer("player2");
        Player player2 = Director.getPlayer("player2");
        player2.setUpButton(KeyInput.KEY_UP);
        player2.setDownButton(KeyInput.KEY_DOWN);
        player2.setLeftButton(KeyInput.KEY_LEFT);
        player2.setRightButton(KeyInput.KEY_RIGHT);
        player2.setAttack1Button(KeyInput.KEY_NUMPAD8);
        player2.setAttack2Button(KeyInput.KEY_NUMPAD6);
        player2.setAttack3Button(KeyInput.KEY_NUMPAD2);
        player2.setAttack4Button(KeyInput.KEY_NUMPAD4);
        player2.setComboModButton(KeyInput.KEY_NUMPAD1);
        player2.setSpecialModButton(KeyInput.KEY_NUMPAD3);
        Director.getPlayer("player2").addIntoPlay();
        Director.buildCharacter("Reino2", "player2");
        Director.getFighterPhysics("player2").setLocalTranslation(2,5,0);


        // sunset light
        dl = new DirectionalLight();   
        dl.setDirection(new Vector3f(-0.1f, -0.7f, 1).normalizeLocal());
        dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
        Director.getApp().getRootNode().addLight(dl);
        //SETS UP CAMERA
          Director.getApp().getCamera().lookAt(
                Director.getFighterPhysics("testPlayer").getLocalTranslation(), Vector3f.UNIT_Y);
          
         // the floor mesh, does not move (mass=0)
        PhysicsNode node3=new PhysicsNode(new MeshCollisionShape(new Box(Vector3f.ZERO,100f,.2f,100f)),0);
        node3.setLocalTranslation(new Vector3f(0f,-6,0f));
        node3.attachDebugShape(Director.getApp().getAssetManager());
        node3.attachChild(createBox(100,(int)0.2f,100));
        Director.getApp().getRootNode().attachChild(node3);
        Director.getPhysicsSpace().add(node3);
        Director.getApp().getCamera().setLocation(
                node3.getLocalTranslation().add(0, 3, 7));

        
        Director.getFighter("testPlayer").lookAt(Director.getFighter("player2").getModel());
        Director.getFighter("player2").lookAt(Director.getFighter("testPlayer").getModel());

    }

 //Creates a quick box dimenstions input by user
    public Geometry createBox(int x, int y, int z){
        Geometry geom = new Geometry("Box", new Box(Vector3f.ZERO, x, y, z));
        Material mat = new Material(Director.getApp().getAssetManager(), "Common/MatDefs/Misc/SimpleTextured.j3md");
        mat.setTexture("m_ColorMap", Director.getApp().getAssetManager().loadTexture("Interface/icons/arenaButton.dds"));
        geom.setMaterial(mat);
        return geom;
    }


    @Override
    public boolean isInitialized() {       return isInitialized;    }

    @Override
    public void stateAttached(AppStateManager stateManager) {    }
    @Override
    public void stateDetached(AppStateManager stateManager) {    }

    @Override
    public void update(float tpf) {
        dl.setDirection(Director.getApp().getCamera().getDirection().normalizeLocal());
        
        Director.getApp().getCamera().lookAt(
                Director.getFighter("testPlayer").getModel().getWorldTranslation(), Vector3f.UNIT_Y);
        //Director.getApp().getCamera().setLocation(
        //        Director.getFighter("player2").getModel().getWorldTranslation().add(0, 2, 5));

    }
    @Override
    public void render(RenderManager rm) {    }
    @Override
    public void cleanup() {
        isInitialized=false;
    }

    public void setActive(boolean active) {this.active = active;}

    public boolean isActive() {return active;}

    public void postRender() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
