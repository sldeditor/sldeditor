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
package com.sldeditor.datasource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.geotools.styling.StyledLayerDescriptor;

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.output.SLDOutputInterface;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.preferences.iface.PrefUpdateVendorOptionInterface;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.filter.v2.envvar.EnvVar;
import com.sldeditor.filter.v2.envvar.EnvVarUpdateInterface;
import com.sldeditor.filter.v2.envvar.EnvironmentVariableManager;
import com.sldeditor.ui.detail.config.inlinefeature.InlineFeatureUtils;
import com.sldeditor.ui.render.RuleRenderOptions;

/**
 * Central point that contains the following information about the current SLD:
 * <ul>
 * <li>SLD Editor project file</li>
 * <li>SLD file</li>
 * <li>Data source information used by the SLD</li>
 * </ul>
 * <p>Class implemented as a singleton.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDEditorFile implements RenderSymbolInterface, SLDEditorFileInterface, EnvVarUpdateInterface, PrefUpdateVendorOptionInterface {

    /** The Constant SLD_FILE_EXTENSION. */
    private static final String SLD_FILE_EXTENSION = "sld";

    /** The instance. */
    private static SLDEditorFile instance = null;

    /** The sld data. */
    private SLDDataInterface sldData = null;

    /** The listener list. */
    private List<SLDEditorDataUpdateInterface> sldEditorFileUpdateListenerList = new ArrayList<SLDEditorDataUpdateInterface>();

    /** The data edited flag. */
    private boolean dataEditedFlag = false;

    /**
     * Gets the single instance of SLDEditorFile.
     *
     * @return single instance of SLDEditorFile
     */
    public static SLDEditorFile getInstance()
    {
        if(instance == null)
        {
            instance = new SLDEditorFile();
        }

        return instance;
    }

    /**
     * Private default constructor.
     */
    private SLDEditorFile()
    {
        EnvironmentVariableManager.getInstance().addEnvVarUpdatedListener(this);
        PrefManager.getInstance().addVendorOptionListener(this);
    }

    /**
     * Gets the SLD data.
     *
     * @return the SLD data
     */
    @Override
    public SLDDataInterface getSLDData() {
        return sldData;
    }

    /**
     * File has been opened/saved.
     */
    public void fileOpenedSaved()
    {
        dataEditedFlag = false;
        notifySLDEditorFileHasUpdated();
    }

    /**
     * Gets the sld editor file.
     *
     * @return the sld editor file
     */
    public File getSldEditorFile() {
        if(sldData == null)
        {
            return null;
        }
        return sldData.getSldEditorFile();
    }

    /**
     * Notify sld editor file has updated.
     */
    private void notifySLDEditorFileHasUpdated()
    {
        for(SLDEditorDataUpdateInterface listener : sldEditorFileUpdateListenerList)
        {
            if(listener != null)
            {
                listener.sldDataUpdated(sldData, dataEditedFlag);
            }
        }
    }

    /**
     * Adds the sld editor file update listener.
     *
     * @param listener the listener
     */
    public void addSLDEditorFileUpdateListener(SLDEditorDataUpdateInterface listener)
    {
        sldEditorFileUpdateListenerList.add(listener);
    }

    /**
     * Data source loaded.
     *
     * @param geometryType the geometry type
     * @param isConnectedToDataSourceFlag the is connected to data source flag
     */
    /* (non-Javadoc)
     * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceLoaded(com.sldeditor.datasource.impl.GeometryTypeEnum, boolean)
     */
    @Override
    public void dataSourceLoaded(GeometryTypeEnum geometryType, boolean isConnectedToDataSourceFlag)
    {
        // Does nothing
    }

    /**
     * Adds the sld output listener.
     *
     * @param sldOutput the sld output
     */
    @Override
    public void addSLDOutputListener(SLDOutputInterface sldOutput)
    {
        // Does nothing

    }

    /**
     * Render symbol.
     */
    /* (non-Javadoc)
     * @see com.sldeditor.render.iface.RenderSymbolInterface#renderSymbol()
     */
    @Override
    public void renderSymbol()
    {
        dataEditedFlag = true;

        notifySLDEditorFileHasUpdated();
    }

    /**
     * Sets the SLD data.
     *
     * @param sldData the new SLD data
     */
    public void setSLDData(SLDDataInterface sldData)
    {
        this.sldData = sldData;

        DataSourcePropertiesInterface dataSourceProperties = null;

        if(sldData != null)
        {
            dataSourceProperties = sldData.getDataSourceProperties();
        }

        setDataSource(dataSourceProperties);
        dataEditedFlag = false;

        // Update the environment variables, will update the predefined flag
        if(this.sldData != null)
        {
            EnvironmentVariableManager.getInstance().update(this.sldData.getEnvVarList());
        }

        notifySLDEditorFileHasUpdated();
    }

    /**
     * Sets the sld file.
     *
     * @param sldFile the new sld file
     */
    public void setSldFile(File sldFile)
    {
        if(this.sldData != null)
        {
            this.sldData.setSLDFile(sldFile);
        }
    }

    /**
     * Gets the data source.
     *
     * @return the data source
     */
    @Override
    public DataSourcePropertiesInterface getDataSource()
    {
        if(this.sldData != null)
        {
            return this.sldData.getDataSourceProperties();
        }
        return null;
    }

    /**
     * Sets the data source.
     *
     * @param dataSourceProperties the new data source
     */
    public void setDataSource(DataSourcePropertiesInterface dataSourceProperties)
    {
        if(this.sldData != null)
        {
            if(dataSourceProperties == null)
            {
                StyledLayerDescriptor sld = SelectedSymbol.getInstance().getSld();

                if(InlineFeatureUtils.containsInLineFeatures(sld))
                {
                    this.sldData.setDataSourceProperties(DataSourceConnectorFactory.getInLineDataSource());
                }
                else
                {
                    this.sldData.setDataSourceProperties(DataSourceConnectorFactory.getNoDataSource());
                }
            }
            else
            {
                this.sldData.setDataSourceProperties(dataSourceProperties);
            }
        }
    }

    /**
     * Sets the sld editor filename.
     *
     * @param sldEditorFile the new sld editor filename
     */
    public void setSldEditorFile(File sldEditorFile)
    {
        if(sldData != null)
        {
            this.sldData.setSldEditorFile(sldEditorFile);
        }
    }

    /**
     * Gets the SLD file extension.
     *
     * @return the SLD file extension
     */
    public static String getSLDFileExtension()
    {
        return SLD_FILE_EXTENSION;
    }

    /**
     * Gets the StyledLayerDescriptor.
     *
     * @return the sld
     */
    @Override
    public StyledLayerDescriptor getSLD()
    {
        return SelectedSymbol.getInstance().getSld();
    }

    /**
     * Destroy singleton instance.
     */
    public static void destroyInstance() {
        instance = null;
    }

    /**
     * Gets the rule render options.
     *
     * @return the rule render options
     */
    @Override
    public RuleRenderOptions getRuleRenderOptions() {
        // Do nothing
        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.filter.v2.envvar.EnvVarUpdateInterface#envVarsUpdated(java.util.List)
     */
    @Override
    public void envVarsUpdated(List<EnvVar> envVarList) {
        dataEditedFlag = true;

        if(sldData != null)
        {
            sldData.setEnvVarList(envVarList);
        }
        notifySLDEditorFileHasUpdated();
    }

    @Override
    public void vendorOptionsUpdated(List<VersionData> vendorOptionList) {
        if(sldData != null)
        {
            dataEditedFlag = true;

            sldData.setVendorOptionList(vendorOptionList);

            notifySLDEditorFileHasUpdated();
        }
    }
}
