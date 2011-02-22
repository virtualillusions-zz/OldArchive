/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.bugs;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.nodes.PhysicsCharacterNode;

/**
 * 
 * @author Kyle Williams
 */
public class TestFallingCameraBug extends SimpleApplication{
    private PhysicsCharacterNode character;

    public static void main(String[] jme3){
        TestFallingCameraBug app = new TestFallingCameraBug();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        CapsuleCollisionShape capsule = new CapsuleCollisionShape(2, 3);
        character = new PhysicsCharacterNode(capsule, 1f);

        character.attachDebugShape(getAssetManager());

        bulletAppState.getPhysicsSpace().add(character);
        rootNode.attachChild(character);
    }

    @Override
    public void simpleUpdate(float tpf){
        getCamera().setLocation(character.getLocalTranslation().add(0,3,15));
        getCamera().lookAt(character.getWorldTranslation(), com.jme3.math.Vector3f.UNIT_Y);
    }
}
