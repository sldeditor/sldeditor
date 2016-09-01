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

import org.geotools.data.DataStore;

import com.sldeditor.datasource.impl.GeometryTypeEnum;

/**
 * The interface used to notify when the data source has updated,
 * either because a new data has been selected or the fields have
 * been updated.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface DataSourceUpdatedInterface {

    /**
     * Data source loaded.
     *
     * @param geometryType the geometry type
     * @param isConnectedToDataSourceFlag the is connected to data source flag
     */
    void dataSourceLoaded(GeometryTypeEnum geometryType, boolean isConnectedToDataSourceFlag);
    
    /**
     * Data source about to unloaded.
     *
     * @param dataStore the data store
     */
    void dataSourceAboutToUnloaded(DataStore dataStore);
}
