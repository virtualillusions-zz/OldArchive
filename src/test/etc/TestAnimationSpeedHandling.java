/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.etc;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.BoneAnimation;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.AnimData;

/**
 *
 * @author Kyle Williams
 */
public class TestAnimationSpeedHandling extends SimpleApplication implements AnimEventListener{
    public static void main(String[] args){
        TestAnimationSpeedHandling app = new TestAnimationSpeedHandling();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        this.getFlyByCamera().setMoveSpeed(50);
         // sunset light
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(getCamera().getDirection().normalizeLocal());
        dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
        getRootNode().addLight(dl);


        Spatial Alex = getAssetManager().loadModel("Models/Alex/Alex.j3o");
        getRootNode().attachChild(Alex);

        AnimControl ctrl = Alex.getControl(AnimControl.class);

        AnimData tempAnim = (AnimData) assetManager.loadAsset("Models/Animations/Skeleton.skeleton.xml");

        for(BoneAnimation anim :tempAnim.anims){
            ctrl.addAnim(anim);
        }

        channel = ctrl.createChannel();
        channel.setAnim("jump");
        maxTime=channel.getAnimMaxTime()/2;

        ctrl.addListener(this);
        setUp();
    }

    private AnimChannel channel;
    private float maxTime;

    @Override
    public void simpleUpdate(float tpf){
        if(channel.getTime()<=maxTime/2){
            float newTime = channel.getTime()-.0001f<=0?channel.getTime()-.0001f:channel.getTime() ;
            channel.setTime(newTime);
        }else if(channel.getTime()>maxTime/2){
            float newTime = channel.getTime()+.0001f>=1?channel.getTime()-.0001f:channel.getTime();
            channel.setTime(channel.getTime());
        }
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        channel.setAnim("idle");
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        maxTime=channel.getAnimMaxTime()/2;
    }

    public void setUp(){
        getInputManager().addMapping("jump", new KeyTrigger(KeyInput.KEY_SPACE));
        getInputManager().addMapping("Run", new KeyTrigger(KeyInput.KEY_V));
        getInputManager().addListener(new ActionListener(){

            public void onAction(String name, boolean isPressed, float tpf) {
                if(name.equals("jump")&&!channel.getAnimationName().equals("jump")){
                    channel.setAnim("jump",0);
                }else if(name.equals("Run") && !channel.getAnimationName().equals("run")){
                    channel.setAnim("run",0);
                }
            }


        }, "jump","Run");
    }

    
}
