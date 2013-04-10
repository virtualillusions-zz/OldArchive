/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.jme3.gde.core.assets;

import com.jme3.gde.core.particleemitter.ParticleEmitterProcessorNode;
import com.jme3.scene.Node;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiFileLoader;

/**
 *
 * @author Kyle Williams
 */
public class EmitterDataObject extends AssetDataObject {

    public EmitterDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        getLookupContents().add(new ParticleEmitterProcessorNode(this));
        saveExtension = "j3p";
    }

    @Override
    public Node loadAsset() {
        return (Node) super.loadAsset();
    }
}
