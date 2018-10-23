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
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.watcher.ReloadManager;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.ui.panels.SLDEditorUIPanels;
import java.net.URL;
import java.util.List;

/**
 * The main application class.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDEditorOperations implements LoadSLDInterface {

    /** The data edited flag. */
    private boolean dataEditedFlag = false;

    /** The under test flag. */
    protected static boolean underTestFlag = false;

    /** The sld editor dlg. */
    private SLDEditorDlgInterface sldEditorDlg = null;

    /** The instance. */
    private static SLDEditorOperations instance = null;

    /**
     * Gets the single instance of SLDEditorOperations.
     *
     * @return single instance of SLDEditorOperations
     */
    public static SLDEditorOperations getInstance() {
        if (instance == null) {
            instance = new SLDEditorOperations();
        }
        return instance;
    }

    /** Instantiates a new SLD editor operations. */
    private SLDEditorOperations() {
        // Private default constructor
    }

    /** Empty sld. */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.iface.LoadSLDInterface#emptySLD()
     */
    @Override
    public void emptySLD() {
        String emptyFilename = "";
        SelectedSymbol.getInstance().setFilename(emptyFilename);

        SLDEditorFile.getInstance().setSLDData(null);

        DataSourceInterface dataSource = DataSourceFactory.createDataSource(null);

        dataSource.reset();

        SelectedSymbol.getInstance().setSld(null);
        SLDEditorUIPanels.getInstance().populateUI(0);
    }

    /**
     * Load sld string.
     *
     * @param selectedFiles the selected files
     * @return true, if successful
     */
    @Override
    public boolean loadSLDString(SelectedFiles selectedFiles) {
        boolean loadNewSymbol = true;

        PrefManager.getInstance().setLastFolderViewed(selectedFiles);

        List<SLDDataInterface> sldFilesToLoad = selectedFiles.getSldData();

        if (selectedFiles.isFolder()) {
            return loadNewSymbol;
        }

        // Application can only support editing one SLD file at a time
        if (sldFilesToLoad.size() == 1) {
            SLDDataInterface firstObject = sldFilesToLoad.get(0);

            if (firstObject != null) {
                if (dataEditedFlag && !underTestFlag && (sldEditorDlg != null)) {
                    loadNewSymbol = sldEditorDlg.load(Controller.getInstance().getFrame());
                }

                if (loadNewSymbol) {
                    SLDEditorCommon.getInstance().populate(firstObject);
                }

                ReloadManager.getInstance().reset();
            }
        }

        if (!selectedFiles.isDataSource()) {
            // Inform UndoManager that a new SLD file has been
            // loaded and to clear undo history
            UndoManager.getInstance().fileLoaded();

            Controller.getInstance().setPopulating(true);
            SLDEditorUIPanels.getInstance().populateUI(sldFilesToLoad.size());
            Controller.getInstance().setPopulating(false);
        }

        return loadNewSymbol;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.common.LoadSLDInterface#preLoad()
     */
    @Override
    public void preLoad() {
        ConsoleManager.getInstance().clear();
        SLDEditorUIPanels.getInstance().getDataSourceConfig().reset();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.common.LoadSLDInterface#reloadSLDFile()
     */
    @Override
    public void reloadSLDFile() {
        boolean reloadFile = true;
        if (!underTestFlag && (sldEditorDlg != null)) {
            reloadFile = sldEditorDlg.reload(Controller.getInstance().getFrame());
        }

        if (reloadFile) {
            SLDDataInterface sldData = SLDEditorFile.getInstance().getSLDData();

            if (sldData != null) {
                URL url = sldData.getSLDURL();
                if (url != null) {
                    List<SLDDataInterface> sldDataList = SLDEditorCommon.getInstance().openURL(url);

                    if ((sldDataList != null) && !sldDataList.isEmpty()) {
                        SLDDataInterface firstObject = sldDataList.get(0);
                        SLDEditorCommon.getInstance().populate(firstObject);
                    }
                }
            }

            // Inform UndoManager that a new SLD file has been
            // loaded and to clear undo history
            UndoManager.getInstance().fileLoaded();

            Controller.getInstance().setPopulating(true);
            SLDEditorUIPanels.getInstance().populateUI(1);
            Controller.getInstance().setPopulating(false);
        }
        ReloadManager.getInstance().reset();
    }

    /**
     * Sets the under test flag.
     *
     * @param underTestFlag the underTestFlag to set
     */
    public static void setUnderTest(boolean underTestFlag) {
        SLDEditorOperations.underTestFlag = underTestFlag;
    }

    /**
     * Sets the data edited flag.
     *
     * @param dataEditedFlag the dataEditedFlag to set
     */
    public void setDataEditedFlag(boolean dataEditedFlag) {
        this.dataEditedFlag = dataEditedFlag;
    }

    /**
     * Sets the sld editor dlg.
     *
     * @param sldEditorDlg the sldEditorDlg to set
     */
    public void setSldEditorDlg(SLDEditorDlgInterface sldEditorDlg) {
        this.sldEditorDlg = sldEditorDlg;
    }

    /** Destroy instance. */
    public static void destroyInstance() {
        instance = null;
    }
}
