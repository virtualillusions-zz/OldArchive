/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.director;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.gde.core.assets.BinaryModelDataObject;
import com.jme3.gde.core.scene.PreviewRequest;
import com.jme3.gde.core.scene.SceneApplication;
import com.jme3.gde.core.scene.SceneListener;
import com.jme3.gde.core.scene.SceneRequest;
import com.jme3.gde.core.sceneexplorer.nodes.JmeNode;
import com.jme3.gde.core.sceneexplorer.nodes.NodeUtility;
import com.jme3.gde.scenecomposer.ComposerCameraController;
import com.jme3.gde.scenecomposer.SceneComposerToolController;
import com.jme3.gde.scenecomposer.SceneComposerTopComponent;
import com.jme3.gde.scenecomposer.SceneEditorController;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.AnimData;
import com.spectre.controller.character.SpectreAnimationController;
import com.spectre.controller.character.SpectreAnimationController.AnimPriority;
import com.spectre.controller.character.SpectrePhysicsController;
import com.spectre.deck.controller.CardManifestController;
import com.spectre.deck.controller.DebugCharacterEditorController;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.swing.JOptionPane;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;

/**
 *
 * @author Kyle Williams
 */
public class SceneDirector implements SceneListener {

    private static SceneDirector instance;
    private AssetManager manager;
    private Node root;
    private Spatial player;
    private Spatial target;
    private SceneRequest currentRequest;
    private ComposerCameraController camController;
    private SceneComposerToolController toolController;
    private SceneEditorController editorController;
    private String assetPath;
    private Collection<String> animationNames;

    public static synchronized SceneDirector getInstance() {
        if (instance == null) {
            instance = new SceneDirector();
        }
        return instance;
    }

    public SceneDirector() {
        //Some things seems to be changing this is to compensate for changes
        assetPath = new File("").getAbsolutePath().contains("FlairAPI") ? "/src/" : "/FlairAPI/src/";
        assetPath = new File("").getAbsolutePath() + assetPath;
        animationNames = new ArrayList< String>();
    
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public AssetManager getManager() {
        if (manager == null) {
            try {
                manager = SceneApplication.getApplication().getAssetManager();
                Class<FileLocator> cl = (Class<FileLocator>) Class.forName(FileLocator.class.getName());
                manager.registerLocator(assetPath, cl);
            } catch (ClassNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return manager;
    }

    public void displayModel() {
        if (currentRequest == null) {
            loadSceneRequest();
        } else if (!currentRequest.equals(SceneApplication.getApplication().getCurrentSceneRequest())) {
            resetModel();
            SceneApplication.getApplication().addSceneListener(this);
            SceneApplication.getApplication().openScene(currentRequest);
            SceneApplication.getApplication().enableCamLight(true);
        } else {
            resetModel();
        }
    }

    private void resetModel() {
        getDebugCharacterEditorController().clear();
        getCardManifestController().clearQueue();
        //this.getPlayer();
        //this.getTarget();
        //getModel().getControl(CharacterControl.class).setPhysicsLocation(new Vector3f(0, 2.4f, 0));
    }

    /**
     * returns the root node of the visual representation of the card editor
     *
     * @return root
     */
    private Spatial getRoot() {
        if (root == null) {
            if(SceneApplication.getApplication().getStateManager().getState(Director.class)==null){
                SceneApplication.getApplication().getStateManager().attach(new Director());
            }
            //Remember this is valid since assetPath registered in constructor
            root = (Node) getManager().loadModel("Models/TestScene.j3o");
            HashMap<String, SpectrePhysicsController> cL = new HashMap<String, SpectrePhysicsController>();
            cL.put("player", setUpSpectreCharacter("player", player));
            cL.put("target", setUpSpectreCharacter("target", target));
            Director.setCharacterList(cL);
            loadAllAnimations();
            Director.getPlayer("player").setModel("player").setSpectreControllers(true);
            Director.getPlayer("target").setModel("target").setSpectreControllers(true);
        }
        return root;
    }

    public Spatial getPlayer() {
        if (player == null) {
            getRoot();
        }
        return player;
    }

    public Spatial getTarget() {
        if (target == null) {
            getRoot();
        }
        return target;
    }

    /**
     * Sets up each node in the world to be interactive
     *
     * @param name
     * @param spat
     * @return
     */
    private SpectrePhysicsController setUpSpectreCharacter(String name, Spatial spat) {
        spat = root.getChild("target");
        //initializing needed controllers
        //CharacterControl & jme3 AnimationControl should already be added to character mesh
        spat.addControl(new CardManifestController());
        spat.addControl(new SpectreAnimationController());
        //special controllers created for debugging
        spat.addControl(new DebugCharacterEditorController());//Requirement is to make sure cardManifestController added prior
        //spat.addControl(new SpectreCameraController(cam));//gamestate
        //character.addControl(new SpectreEssenceController());//gamestate
        SpectrePhysicsController sPc = new SpectrePhysicsController();//character
        spat.addControl(sPc);

        return sPc;
    }

    private void loadAllAnimations() {
        HashMap<String, AnimData> aL = new HashMap<String, AnimData>();
        animationNames.clear();
        //Leave it like this so users can add new animations and the list would be automatically updated
        FileObject file = FileUtil.toFileObject(new File(assetPath + "Models/Skeleton/"));
        for (FileObject f : file.getChildren()) {
            if (f.getPath().contains(".skeleton.xml")) {
                String skelPath = f.getPath().split("Models/Skeleton/")[1];
                AnimData animation = (AnimData) manager.loadAsset("Models/Skeleton/" + skelPath);
                aL.put(f.getName(), animation);

                //ADD ALL ANIMATIONS TO AN EASILY ACESSIBLE LIST
                for (Animation a : animation.anims) {
                    animationNames.add(a.getName());
                }
            }
        }
        //ADD ALL ANIMATIONS TO DIRECTOR
        Director.setAnimationsList(aL);
    }

    public Collection<String> getAllAnimations() {
        if (root == null) {
            getRoot();
        }
        return animationNames;
    }

    public CardManifestController getCardManifestController() {
        return getPlayer().getControl(CardManifestController.class);
    }

    public DebugCharacterEditorController getDebugCharacterEditorController() {
        return getPlayer().getControl(DebugCharacterEditorController.class);
    }

    public SpectreAnimationController getAnimationController() {
        return getPlayer().getControl(SpectreAnimationController.class);
    }

    public AnimControl getAnimControl() {
        return getPlayer().getControl(AnimControl.class);
    }

    public AnimChannel getAnimChannel() {
        return getPlayer().getControl(AnimControl.class).getChannel(0);
    }

    public ArrayList<String> getBoneList() {
        return getPlayer().getControl(SpectreAnimationController.class).getBoneList();
    }

    public boolean animExists(String animName) {//not needed excessive due to the way animations are selected
        return Director.getAnimation(animName) == null ? false : true;
    }

    public void setAnim(String animName) {
        getPlayer().getControl(SpectreAnimationController.class).changeAnimation(animName, AnimPriority.DEBUG);
    }

    public synchronized boolean isCurrentRequest() {
        return SceneApplication.getApplication().getCurrentSceneRequest().equals(currentRequest);
    }

    private void loadSceneRequest() {

        cleanupControllers();
        SceneApplication.getApplication().addSceneListener(this);

        JmeNode jmeNode = NodeUtility.createNode(root, false);
        currentRequest = new SceneRequest(this, jmeNode, null);

        FileObject fileObject = FileUtil.toFileObject(new File("FlairAPI/src/Models/TestScene.j3o").getAbsoluteFile());
        BinaryModelDataObject data = null;
        try {
            data = (BinaryModelDataObject) DataObject.find(fileObject);
        } catch (DataObjectNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        currentRequest.setDataObject(data);
        currentRequest.setHelpCtx(SceneComposerTopComponent.findInstance().getHelpCtx());
        currentRequest.setWindowTitle("Flair Editor");
        currentRequest.setToolNode(new Node("SceneComposerToolNode"));

        SceneApplication.getApplication().openScene(currentRequest);
    }

    @Override
    public void sceneOpened(SceneRequest sr) {
        if (currentRequest.equals(sr)) {
            SceneApplication.getApplication().getStateManager().attach(new Director());

            SceneApplication.getApplication().enableCamLight(true);

            if (editorController != null) {
                editorController.cleanup();
            }
            if (camController != null) {
                camController.disable();
            }
            if (toolController != null) {
                toolController.cleanup();
            }

            editorController = new SceneEditorController(currentRequest.getJmeNode(), currentRequest.getDataObject());

            toolController = new SceneComposerToolController(currentRequest.getToolNode(), getManager(), currentRequest.getJmeNode());

            camController = new ComposerCameraController(
                    SceneApplication.getApplication().getCamera(),
                    currentRequest.getJmeNode());

            camController.getCamera().setLocation(new Vector3f(3f, 4f, 8f));
            camController.getCamera().lookAt(new Vector3f(0f, 2f, 0f), Vector3f.UNIT_Y);
            //camController.setCamFocus(Vector3f.ZERO);//toolController.getCursorLocation());//root.getLocalTranslation());

            toolController.setEditorController(editorController);

            camController.setToolController(toolController);
            camController.setMaster(this);

            toolController.setCameraController(camController);
            editorController.setToolController(toolController);

            SceneApplication.getApplication().setPhysicsEnabled(true);

            //open editors
            if (!EditorDirector.getFlairVisualEditorTopComponent().isOpened()) {
                EditorDirector.getFlairVisualEditorTopComponent().open();//Open Everything
            }
            if (!EditorDirector.getFliarDataEditorTopComponent().isOpened()) {
                EditorDirector.getFliarDataEditorTopComponent().open();//Open Everything
            }
        }
    }

    @Override
    public void sceneClosed(SceneRequest sr) {
        if (currentRequest.equals(sr)) {
            SceneApplication.getApplication().getStateManager().detach(new Director());

            SceneApplication.getApplication().enableCamLight(false);

            SceneApplication.getApplication().removeSceneListener(this);

            cleanupControllers();

            SceneApplication.getApplication().setPhysicsEnabled(false);

            if (EditorDirector.getFlairVisualEditorTopComponent().isOpened()) {
                EditorDirector.getFlairVisualEditorTopComponent().close();//Open Everything
            }
            if (EditorDirector.getFliarDataEditorTopComponent().isOpened()) {
                EditorDirector.getFliarDataEditorTopComponent().close();//Open Everything
            }
        }
    }

    @Override
    public void previewCreated(PreviewRequest pr) {
    }

    private void cleanupControllers() {
        if (camController != null) {
            camController.disable();
            camController = null;
        }
        if (toolController != null) {
            toolController.cleanup();
            toolController = null;
        }
        if (editorController != null) {
            editorController.cleanup();
            editorController = null;
        }
    }
}
