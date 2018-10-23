/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.checks.CheckAttributeFactory;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.extension.ExtensionFactory;
import com.sldeditor.extension.ExtensionInterface;
import com.sldeditor.ui.legend.LegendManager;
import com.sldeditor.ui.panels.SLDEditorUIPanels;
import java.io.File;
import java.net.URL;
import java.util.List;
import org.geotools.styling.StyledLayerDescriptor;

/**
 * The main application class.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDEditorCommon implements SLDEditorCommonInterface {

    /** The batch import list. */
    private List<ExtensionInterface> extensionList = ExtensionFactory.getAvailableExtensions();

    /** The instance. */
    private static SLDEditorCommon instance = null;

    /**
     * Gets the single instance of SLDEditorCommon.
     *
     * @return single instance of SLDEditorCommon
     */
    public static SLDEditorCommon getInstance() {
        if (instance == null) {
            instance = new SLDEditorCommon();
        }

        return instance;
    }

    /** Instantiates a new SLD editor common. */
    private SLDEditorCommon() {
        // Default constructor
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.SLDEditorCommonInterface#openURL(java.net.URL)
     */
    @Override
    public List<SLDDataInterface> openURL(URL url) {
        List<SLDDataInterface> sldDataList = null;
        for (ExtensionInterface extension : extensionList) {
            if (sldDataList == null) {
                sldDataList = extension.open(url);
            }
        }

        return sldDataList;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.SLDEditorCommonInterface#populate(com.sldeditor.common.SLDDataInterface)
     */
    @Override
    public void populate(SLDDataInterface sldData) {
        String layerName = sldData.getLayerName();

        File sldEditorFile = sldData.getSldEditorFile();
        if (sldEditorFile != null) {
            ConsoleManager.getInstance()
                    .information(
                            this,
                            String.format(
                                    "%s : %s",
                                    Localisation.getString(
                                            SLDEditor.class, "SLDEditor.loadedSLDEditorFile"),
                                    sldEditorFile.getAbsolutePath()));
        }
        ConsoleManager.getInstance()
                .information(
                        this,
                        String.format(
                                "%s : %s",
                                Localisation.getString(SLDEditor.class, "SLDEditor.loadedSLDFile"),
                                layerName));

        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);

        SelectedSymbol selectedSymbolInstance = SelectedSymbol.getInstance();
        selectedSymbolInstance.setSld(sld);
        selectedSymbolInstance.setFilename(layerName);
        selectedSymbolInstance.setName(layerName);

        SLDEditorFile.getInstance().setSLDData(sldData);

        // Reload data source if sticky flag is set
        boolean isDataSourceSticky = SLDEditorFile.getInstance().isStickyDataSource();

        DataSourceInterface dataSource = DataSourceFactory.createDataSource(null);

        DataSourcePropertiesInterface previousDataSource = dataSource.getDataConnectorProperties();

        dataSource.reset();

        if (isDataSourceSticky) {
            SLDEditorFile.getInstance().setDataSource(previousDataSource);
        }

        dataSource.connect(
                ExternalFilenames.removeSuffix(layerName),
                SLDEditorFile.getInstance(),
                CheckAttributeFactory.getCheckList());

        VendorOptionManager.getInstance()
                .loadSLDFile(SLDEditorUIPanels.getInstance(), sld, sldData);

        LegendManager.getInstance().sldLoaded(sldData.getLegendOptions());
        SLDEditorFile.getInstance().fileOpenedSaved();
    }
}
