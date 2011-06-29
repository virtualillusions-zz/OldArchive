///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package test.depreciated;
//
//import com.jme3.animation.AnimControl;
//import com.jme3.animation.BoneAnimation;
//import com.jme3.asset.AssetManager;
//import com.jme3.scene.Spatial;
//import com.jme3.scene.plugins.ogre.AnimData;
//import com.spectre.director.Director;
//import com.spectre.util.SAX.spectreHandler;
//import java.io.FileInputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;
//
///**
// * This class is in charge of fighter management use it to get all available fighters
// * @author Kyle Williams
// */
//public class ModelLoader{
//    private AssetManager assetManager = null;
//    
//    public ModelLoader(AssetManager aM) {
//        assetManager = aM;
//        findAndLoadAllCharacters();
//    }
//
//    /**
//     * This method searches for all characters and load them 
//     */
//    private void findAndLoadAllCharacters() {
//        ArrayList<String[]> basicMovement = null;
//        HashMap<String,Spatial> characterList = null;
//        ArrayList<AnimData> tempAnim = null;
//        try{
//            spectreHandler handler = new spectreHandler(),temp;
//            handler.setAssetManager(assetManager);
//            final SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
//            final String Directory = "assets/Models/";
//            //LOAD ALL ANIMATIONS
//            temp=handler.retrieveAllAnimations();
//            saxParser.parse(new FileInputStream(Directory+"Animations/Animations.xml"),temp);
//            tempAnim = (ArrayList<AnimData>) ((Object[])temp.getParameters())[0];
//            basicMovement = (ArrayList<String[]>) ((Object[])temp.getParameters())[1];
//            //LOAD ALL CHARACTERS
//            temp=handler.retrieveAllCharacters();
//            saxParser.parse(new FileInputStream(Directory+"characterList.xml"),temp);
//            characterList = (HashMap<String, Spatial>) temp.getParameters();
//
//
//        }catch(Throwable err){
//             com.spectre.app.SpectreApplication.logger.log(
//                     java.util.logging.Level.SEVERE,
//                     "Finding All Models Have Failed",
//                     new java.io.IOException());
//
//            throw new java.io.IOError(err);
//        }
//
//        //Give all characters the same moves
//        for(Spatial spat:characterList.values()){
//            AnimControl ctrl = spat.getControl(AnimControl.class);
//            for (AnimData data : tempAnim){
//                for(BoneAnimation anim :data.anims){
//                    ctrl.addAnim(anim);
//                }
//            }
//            //Attach The AnimationController to all spatials
//            int i = (Integer)spat.getUserData("BasicMovement");
//            String[] basics = basicMovement.get(i);
//            spat.addControl(new com.spectre.controller.AnimationController(basics));
//          }
//
//        //LOAD ALL IMPORTANT PARTS INTO THE DIRECTOR
//        Director.setCharacterList(characterList);
//    }
//}
