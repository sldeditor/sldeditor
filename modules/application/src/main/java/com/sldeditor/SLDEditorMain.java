/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2016, SCISYS UK Limited
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sldeditor;

import com.sldeditor.common.Controller;
import com.sldeditor.common.LoadSLDInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.watcher.ReloadManager;
import com.sldeditor.create.NewSLDPanel;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.extension.ExtensionFactory;
import com.sldeditor.extension.ExtensionInterface;
import com.sldeditor.generated.Version;
import com.sldeditor.ui.detail.config.symboltype.externalgraphic.RelativePath;
import com.sldeditor.ui.layout.UILayoutFactory;
import com.sldeditor.ui.panels.SLDEditorUIPanels;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The main application class.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDEditorMain implements SLDEditorInterface {

    /** The under test flag. */
    protected static boolean underTestFlag = false;

    /** The sld writer. */
    private SLDWriterInterface sldWriter = null;

    /** The app panel. */
    private JPanel appPanel = null;

    /**
     * Instantiates a new SLD editor main.
     *
     * @param appPanel the app panel
     */
    public SLDEditorMain(JPanel appPanel) {
        this.appPanel = appPanel;
    }

    /**
     * Generate application title string.
     *
     * @return the string
     */
    private static String generateApplicationTitleString() {
        return String.format(
                "%s %s \251%s %s",
                Version.getAppName(),
                Version.getVersionNumber(),
                Version.getAppCopyrightYear(),
                Version.getAppCompany());
    }

    /**
     * Update window title.
     *
     * @param dataEditedFlag the data edited flag
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.SLDEditorInterface#updateWindowTitle(boolean)
     */
    @Override
    public void updateWindowTitle(boolean dataEditedFlag) {
        SLDEditorOperations.getInstance().setDataEditedFlag(dataEditedFlag);

        String docName;
        File file = SLDEditorFile.getInstance().getSldEditorFile();

        String sldLayerName = SLDEditorFile.getInstance().getSLDData().getLayerName();
        if (file != null) {
            docName = file.getName() + " - " + sldLayerName;
        } else {
            docName = sldLayerName;
        }

        char docDirtyChar = dataEditedFlag ? '*' : ' ';
        JFrame frame = Controller.getInstance().getFrame();
        if (frame != null) {
            frame.setTitle(
                    String.format(
                            "%s - %s%c", generateApplicationTitleString(), docName, docDirtyChar));
        }
    }

    /** Exit application. */
    @Override
    public void exitApplication() {
        UILayoutFactory.writeLayout(null);

        System.exit(0);
    }

    /** Choose new sld. */
    @Override
    public void chooseNewSLD() {
        NewSLDPanel panel = new NewSLDPanel();

        List<SLDDataInterface> newSLDList = panel.showDialog();

        if (newSLDList != null) {
            SelectedFiles selectedFiles = new SelectedFiles();
            selectedFiles.setSldData(newSLDList);

            if (SLDEditorOperations.getInstance().loadSLDString(selectedFiles)) {
                SLDEditorUIPanels.getInstance().populateUI(1);
            }
        }
    }

    /**
     * Save file.
     *
     * @param urlToSave the url to save
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.SLDEditorInterface#saveFile(java.net.URL)
     */
    @Override
    public void saveFile(URL urlToSave) {

        String sldContents = getSLDString();

        SLDDataInterface sldData = SLDEditorFile.getInstance().getSLDData();
        sldData.updateSLDContents(sldContents);

        if (RelativePath.isLocalFile(urlToSave)) {
            StyleWrapper style = null;
            try {
                File f = new File(urlToSave.toURI());
                style = new StyleWrapper(f.getName());
            } catch (URISyntaxException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
            sldData.updateStyleWrapper(style);
        }

        ReloadManager.getInstance().setFileSaved();
        saveSLDData(sldData);

        SLDEditorFile.getInstance().fileOpenedSaved();
        UndoManager.getInstance().fileSaved();
    }

    /**
     * Gets the SLD string.
     *
     * @return the SLD string
     */
    public String getSLDString() {
        if (sldWriter == null) {
            sldWriter = SLDWriterFactory.createWriter(null);
        }

        return sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());
    }

    /**
     * Save sld data.
     *
     * @param sldData the sld data
     */
    @Override
    public void saveSLDData(SLDDataInterface sldData) {
        boolean saved = false;

        for (ExtensionInterface extension : ExtensionFactory.getAvailableExtensions()) {
            if (!saved) {
                saved = extension.save(sldData);
            }
        }
    }

    /**
     * Gets the app panel.
     *
     * @return the app panel
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.SLDEditorInterface#getAppPanel()
     */
    @Override
    public JPanel getAppPanel() {
        return appPanel;
    }

    /**
     * Gets the load sld interface.
     *
     * @return the load sld interface
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.SLDEditorInterface#getLoadSLDInterface()
     */
    @Override
    public LoadSLDInterface getLoadSLDInterface() {
        return SLDEditorOperations.getInstance();
    }

    /**
     * Open file.
     *
     * @param url the url
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.SLDEditorInterface#openFile(java.net.URL)
     */
    @Override
    public void openFile(URL url) {
        List<SLDDataInterface> sldDataList = SLDEditorCommon.getInstance().openURL(url);

        if (sldDataList != null) {
            SelectedFiles selectedFiles = new SelectedFiles();
            selectedFiles.setSldData(sldDataList);

            SLDEditorOperations.getInstance().loadSLDString(selectedFiles);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.common.SLDEditorInterface#refreshPanel(java.lang.Class, java.lang.Class)
     */
    @Override
    public void refreshPanel(Class<?> parent, Class<?> panelClass) {
        SLDEditorUIPanels.getInstance().refreshPanel(parent, panelClass);
    }
}
