/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.director;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.gde.core.scene.SceneApplication;

/**
 * This class is simply an automation of types making it easier to transition from editor to Game.
 * @author Kyle Williams
 */
public class Debug_Director {

    /**
     * Returns VezlaApplication in order to make system related calls
     * @return application
     */
    public static AssetManager getAssetManager() {
        AssetManager assets = SceneApplication.getApplication().getAssetManager();
        assets.registerLocator(path, FileLocator.class);
        return assets;
    }
    private static String path;

    public static void setAssetsPath(String dir) {
        path = dir;
    }

   // public static com.jme3.bullet.PhysicsSpace getPhysicsSpace() {
   //     return SceneDirector.getInstance().getPhysicsSpace();
   // }
}
