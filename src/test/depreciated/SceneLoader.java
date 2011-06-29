///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package test.depreciated;
//
//import com.jme3.asset.AssetManager;
//import com.jme3.scene.Spatial;
//import com.spectre.director.Director;
//import com.spectre.util.SAX.spectreHandler;
//import java.io.FileInputStream;
//import java.util.HashMap;
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;
//
//
///**
// *
// * @author Kyle Williams
// */
//public class SceneLoader {
//    private com.jme3.asset.AssetManager assetManager = null;
//
//    public SceneLoader(AssetManager aM) {
//        assetManager = aM;
//        findAndLoadAllScenes();
//    }
//
//    private void findAndLoadAllScenes() {
//        HashMap<String,Spatial> sceneList = null;
//        try{
//            spectreHandler handler = new spectreHandler(),temp;
//            handler.setAssetManager(assetManager);
//            final SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
//            final String Directory = "assets/Scenes/";
//            //LOAD ALL Scenes
//            temp=handler.retrieveAllScenes();
//            saxParser.parse(new FileInputStream(Directory+"sceneList.xml"),temp);
//            sceneList = (HashMap<String, Spatial>) temp.getParameters();
//        }catch(Throwable err){
//             com.spectre.app.SpectreApplication.logger.log(
//                     java.util.logging.Level.SEVERE,
//                     "Finding All Scenes Have Failed",
//                     new java.io.IOException());
//
//            throw new java.io.IOError(err);
//        }
//        Director.setSceneList(sceneList);
//    }
//}
