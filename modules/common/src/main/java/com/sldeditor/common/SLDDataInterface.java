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
package com.sldeditor.common;

import java.io.File;
import java.util.List;

import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.filter.v2.envvar.EnvVar;

/**
 * The Interface SLDDataInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface SLDDataInterface {

    /**
     * Gets the layer name.
     *
     * @return the layer name
     */
    String getLayerName();

    /**
     * Gets the sld.
     *
     * @return the sld
     */
    String getSld();

    /**
     * Gets the field list.
     *
     * @return the field list
     */
    List<DataSourceFieldInterface> getFieldList();

    /**
     * Gets the data source properties.
     *
     * @return the data source properties
     */
    DataSourcePropertiesInterface getDataSourceProperties();

    /**
     * Sets the SLD file.
     *
     * @param file the new file
     */
    void setSLDFile(File file);

    /**
     * Gets the connection data.
     *
     * @return the connection data
     */
    GeoServerConnection getConnectionData();

    /**
     * Sets the connection data.
     *
     * @param connectionData the new connection data
     */
    void setConnectionData(GeoServerConnection connectionData);

    /**
     * Checks if is read only.
     *
     * @return true, if is read only
     */
    boolean isReadOnly();

    /**
     * Sets the read only.
     *
     * @param readOnly the new read only
     */
    void setReadOnly(boolean readOnly);

    /**
     * Gets the vendor option list.
     *
     * @return the vendor option list
     */
    List<VersionData> getVendorOptionList();

    /**
     * Sets the vendor option list.
     *
     * @param vendorOptionList the new vendor option list
     */
    void setVendorOptionList(List<VersionData> vendorOptionList);

    /**
     * Sets the field list.
     *
     * @param fieldList the new field list
     */
    void setFieldList(List<DataSourceFieldInterface> fieldList);

    /**
     * Sets the data source properties.
     *
     * @param dataSourceProperties the new data source properties
     */
    void setDataSourceProperties(DataSourcePropertiesInterface dataSourceProperties);

    /**
     * Gets the style.
     *
     * @return the style
     */
    StyleWrapper getStyle();

    /**
     * Gets the sld editor file.
     *
     * @return the sld editor file
     */
    File getSldEditorFile();

    /**
     * Sets the sld editor file.
     *
     * @param sldEditorFile the new sld editor file
     */
    void setSldEditorFile(File sldEditorFile);

    /**
     * Returns the SLD file
     *
     * @return the file
     */
    File getSLDFile();

    /**
     * Gets the layer name with out suffix.
     *
     * @return the layer name with out suffix
     */
    String getLayerNameWithOutSuffix();

    /**
     * Update sld contents.
     *
     * @param sldContents the sld contents
     */
    void updateSLDContents(String sldContents);

    /**
     * Sets the env var list.
     *
     * @param envVarList the new env var list
     */
    void setEnvVarList(List<EnvVar> envVarList);

    /**
     * Gets the env var list.
     *
     * @return the env var list
     */
    List<EnvVar> getEnvVarList();

}