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

import org.geotools.styling.StyledLayerDescriptor;

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.SLDDataInterface;

/**
 * The Interface SLDEditorFileInterface.
 * 
 * @author Robert Ward (SCISYS)
 * 
 */
public interface SLDEditorFileInterface {

    /**
     * Gets the SLD data.
     *
     * @return the SLD data
     */
    SLDDataInterface getSLDData();

    /**
     * Gets the data source.
     *
     * @return the data source
     */
    DataSourcePropertiesInterface getDataSource();

    /**
     * Gets the StyledLayerDescriptor.
     *
     * @return the sld
     */
    StyledLayerDescriptor getSLD();
}