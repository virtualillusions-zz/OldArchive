/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.renderer.visual.components;

import com.simsilica.es.EntityComponent;

/**
 * Component used to represent the entity in the Viewport and which helps
 * determine if the model should be attached to the scene or model node
 *
 * @author Kyle D. Williams
 */
public class VisualRepPiece implements EntityComponent {

    private String assetName;

    /**
     * Used to determine the visual element belonging to this entity
     *
     * @param assetName The name of the model being represented in the Scene
     */
    public VisualRepPiece(String assetName) {
        this.assetName = assetName;
    }

    /**
     * The name of the model being represented in the Scene
     *
     * @return assetName String
     */
    public String getAssetName() {
        return assetName;
    }

    @Override
    public String toString() {
        return "VisualRepPiece[AssetName=" + assetName + "]";
    }
}
