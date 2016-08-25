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
package com.sldeditor.common.data;

import java.io.File;
import java.util.List;

import com.sldeditor.common.DataSourceFieldInterface;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.output.SLDOutputFormatEnum;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.filter.v2.envvar.EnvVar;
import com.sldeditor.tool.legendpanel.option.LegendOptionData;

/**
 * Class the encapsulates data describing an SLD, includes:
 * <ul>
 * <li>Location of SLD file within a file system.</li>
 * <li>Location of SLD file within a GeoServer instance.</li>
 * <li>Name of the SLD.</li>
 * <li>The string contents of an SLD.</li>
 * <li>Data source details.</li>
 * <li>Vendor options supported</li>.
 * </ul>
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDData implements SLDDataInterface
{
    /** The name. */
    private String name;

    /** The sld string. */
    private String sldContents;

    /** The file containing the sld. */
    private File sldFile = null;

    /** The sld editor file. */
    private File sldEditorFile = null;

    /** The style. */
    private StyleWrapper style = null;

    /** The connection data. */
    private GeoServerConnection connectionData = null;

    /** The read only. */
    private boolean readOnly = true;

    /** The field list. */
    private List<DataSourceFieldInterface> fieldList;

    /** The data source properties. */
    private DataSourcePropertiesInterface dataSourceProperties;

    /** The vendor option list. */
    private List<VersionData> vendorOptionList = null;

    /** The env var list. */
    private List<EnvVar> envVarList = null;

    /** The original format. */
    private SLDOutputFormatEnum originalFormat = SLDOutputFormatEnum.SLD;

    /** The legend options. */
    private LegendOptionData legendOptions = new LegendOptionData();

    /**
     * Constructor.
     *
     * @param style the style
     * @param sldContents the sld contents
     */
    public SLDData(StyleWrapper style, String sldContents)
    {
        super();
        this.style = style;
        if(style != null)
        {
            this.name = removeFileExtension(style.getStyle());
        }
        this.sldContents = sldContents;
    }

    /**
     * Gets the layer name.
     *
     * @return the layer name
     */
    @Override
    public String getLayerName()
    {
        return name;
    }

    /**
     * Gets the sld.
     *
     * @return the sld
     */
    @Override
    public String getSld()
    {
        return sldContents;
    }

    /**
     * Gets the field list.
     *
     * @return the field list
     */
    @Override
    public List<DataSourceFieldInterface> getFieldList()
    {
        return fieldList;
    }

    /**
     * Gets the data source properties.
     *
     * @return the data source properties
     */
    @Override
    public DataSourcePropertiesInterface getDataSourceProperties()
    {
        return dataSourceProperties;
    }

    /**
     * Sets the SLD file.
     *
     * @param file the new file
     */
    @Override
    public void setSLDFile(File file) {
        this.sldFile = file;
        this.connectionData = null;
    }

    /**
     * Gets the connection data.
     *
     * @return the connection data
     */
    @Override
    public GeoServerConnection getConnectionData() {
        return connectionData;
    }

    /**
     * Sets the connection data.
     *
     * @param connectionData the new connection data
     */
    @Override
    public void setConnectionData(GeoServerConnection connectionData) {
        this.connectionData = connectionData;
    }

    /**
     * Checks if is read only.
     *
     * @return true, if is read only
     */
    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Sets the read only.
     *
     * @param readOnly the new read only
     */
    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * Gets the vendor option list.
     *
     * @return the vendor option list
     */
    @Override
    public List<VersionData> getVendorOptionList() {
        return vendorOptionList;
    }

    /**
     * Sets the vendor option list.
     *
     * @param vendorOptionList the new vendor option list
     */
    @Override
    public void setVendorOptionList(List<VersionData> vendorOptionList) {
        this.vendorOptionList = vendorOptionList;
    }

    /**
     * Sets the field list.
     *
     * @param fieldList the new field list
     */
    @Override
    public void setFieldList(List<DataSourceFieldInterface> fieldList) {
        this.fieldList = fieldList;
    }

    /**
     * Sets the data source properties.
     *
     * @param dataSourceProperties the new data source properties
     */
    @Override
    public void setDataSourceProperties(DataSourcePropertiesInterface dataSourceProperties) {
        if(this.dataSourceProperties != dataSourceProperties)
        {
            this.dataSourceProperties = dataSourceProperties;
        }
    }

    /**
     * Removes the file extension.
     *
     * @param s the s
     * @return the string
     */
    public static String removeFileExtension(String s) {

        String separator = System.getProperty("file.separator");
        String filename;

        // Remove the path upto the filename.
        int lastSeparatorIndex = s.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = s;
        } else {
            filename = s.substring(lastSeparatorIndex + 1);
        }
        return filename;
    }

    /**
     * Gets the style.
     *
     * @return the style
     */
    @Override
    public StyleWrapper getStyle()
    {
        return style;
    }

    /**
     * Gets the sld editor file.
     *
     * @return the sld editor file
     */
    @Override
    public File getSldEditorFile()
    {
        return sldEditorFile;
    }

    /**
     * Sets the sld editor file.
     *
     * @param sldEditorFile the new sld editor file
     */
    @Override
    public void setSldEditorFile(File sldEditorFile)
    {
        this.sldEditorFile = sldEditorFile;
    }

    /**
     * Returns the SLD file.
     *
     * @return the file
     */
    @Override
    public File getSLDFile()
    {
        return sldFile;
    }

    /**
     * Gets the layer name with out suffix.
     *
     * @return the layer name with out suffix
     */
    @Override
    public String getLayerNameWithOutSuffix()
    {
        String layerName = getLayerName();

        int index = layerName.lastIndexOf('.');

        if((index >= 0) && (index < layerName.length()))
        {
            layerName = layerName.substring(0, index);
        }
        return layerName;
    }

    /**
     * Update sld contents.
     *
     * @param sldContents the sld contents
     */
    @Override
    public void updateSLDContents(String sldContents) {
        this.sldContents = sldContents;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.SLDDataInterface#setEnvVarList(java.util.List)
     */
    @Override
    public void setEnvVarList(List<EnvVar> envVarList) {
        this.envVarList  = envVarList;
    }

    /**
     * Gets the env var list.
     *
     * @return the env var list
     */
    @Override
    public List<EnvVar> getEnvVarList() {
        return envVarList;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.SLDDataInterface#setOriginalFormat(com.sldeditor.common.output.SLDOutputFormatEnum)
     */
    @Override
    public void setOriginalFormat(SLDOutputFormatEnum format) {
        originalFormat = format;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.SLDDataInterface#getOriginalFormat()
     */
    @Override
    public SLDOutputFormatEnum getOriginalFormat() {
        return originalFormat;
    }

    /**
     * Gets the legend options.
     *
     * @return the legendOptions
     */
    @Override
    public LegendOptionData getLegendOptions() {
        return legendOptions;
    }

    /**
     * Sets the legend options.
     *
     * @param legendOptions the legendOptions to set
     */
    @Override
    public void setLegendOptions(LegendOptionData legendOptions) {
        this.legendOptions = legendOptions;
        
        if(this.legendOptions == null)
        {
            this.legendOptions = new LegendOptionData();
        }
    }
}
