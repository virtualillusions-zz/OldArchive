/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.seriesfiletype;

import com.spectre.deck.SupplyDeck;
import com.spectre.flairapi.Loadable;
import com.spectre.flairapi.SeriesNode.SeriesChildren;
import com.spectre.util.JSON.MasterDeckJSONImporterExporter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Children;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

public final class SeriesDataObject extends MultiDataObject implements Loadable {

    private final ChangeSupport cs;
    protected SupplyDeck savable = new SupplyDeck();
    protected SeriesSaveCookie saveCookie = new SeriesSaveCookie() {
        private FileObject fo = null;

        @Override
        public void setSaveFileObject(FileObject fi) {
            fo = fi;
        }

        @Override
        public FileObject getSaveFileObject() {
            return fo;
        }

        @Override
        public void save() throws IOException {
            saveAsset();
        }
    };

    public SeriesDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        saveCookie.setSaveFileObject(pf);
        CookieSet cookies = getCookieSet();
        //cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));
        setSaveCookie(saveCookie);
        cs = new ChangeSupport(SeriesDataObject.class);
    }

    @Override
    protected Node createNodeDelegate() {
        cs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ev) {
                setModified(true);
            }
        });
        return new DataNode(this, Children.create(new SeriesChildren(savable, cs, saveCookie, this), true), getLookup());
    }

    @Override
    public Lookup getLookup() {
        return getCookieSet().getLookup();
    }

    @Override
    public void setModified(boolean modif) {
        super.setModified(modif);
        if (modif && saveCookie != null) {
            getCookieSet().assign(SaveCookie.class, saveCookie);
        } else {
            getCookieSet().assign(SaveCookie.class);
        }
    }

    public void setSaveCookie(SeriesSaveCookie cookie) {
        this.saveCookie = cookie;
        getCookieSet().assign(SaveCookie.class, saveCookie);
        setModified(false);
    }

    @Override
    public SupplyDeck load() {
        FileLock lock = null;
        SupplyDeck deck = null;
        try {
            lock = getPrimaryFile().lock();
            deck = MasterDeckJSONImporterExporter.Import(FileUtil.toFile(getPrimaryFile()));
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            if (lock != null) {
                lock.releaseLock();
            }
        }
        if (deck == null) {
            JOptionPane.showMessageDialog(null, "Failed To Load Master Supply Deck");
        }
        return deck;
    }

    public void saveAsset() throws IOException {
        if (savable == null) {
            Logger.getLogger(SeriesDataObject.class.getName()).log(Level.WARNING, "Trying to save Master Supply Deck that has not been loaded before or does not support saving!");
            return;
        }

        FileLock lock = null;
        try {
            lock = this.getPrimaryFile().lock();
            MasterDeckJSONImporterExporter.Export(savable, FileUtil.toFile(getPrimaryFile()));
        } finally {
            if (lock != null) {
                lock.releaseLock();
            }
        }
        setModified(false);
    }

    public SupplyDeck getMasterDeck() {
        return savable;
    }

    public SaveCookie getSaveCookie() {
        return saveCookie;
    }

    public static interface SeriesSaveCookie extends org.openide.cookies.SaveCookie {

        public void setSaveFileObject(FileObject fi);

        public FileObject getSaveFileObject() throws NullPointerException;
    }
}
