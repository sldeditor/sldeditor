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
package com.sldeditor.extension.filesystem.geoserver;

import java.util.List;
import java.util.Map;

import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.data.GeoServerLayer;
import com.sldeditor.common.data.StyleWrapper;

/**
 * The Interface GeoServerReadProgressInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface GeoServerReadProgressInterface
{

    /**
     * Start populating.
     *
     * @param connection the connection
     */
    void startPopulating(GeoServerConnection connection);
    
    /**
     * Read styles complete.
     *
     * @param connection the connection
     * @param styleMap the style map
     * @param partialRefresh the partial refresh
     */
    void readStylesComplete(GeoServerConnection connection, Map<String, List<StyleWrapper>> styleMap, boolean partialRefresh);

    /**
     * Read styles progress.
     *
     * @param connection the connection
     * @param count the count
     * @param total the total
     */
    void readStylesProgress(GeoServerConnection connection, int count, int total);

    /**
     * Read layers complete.
     *
     * @param connection the connection
     * @param layerMap the layer map
     */
    void readLayersComplete(GeoServerConnection connection, Map<String, List<GeoServerLayer>> layerMap);

    /**
     * Read layers progress.
     *
     * @param connection the connection
     * @param count the count
     * @param total the total
     */
    void readLayersProgress(GeoServerConnection connection, int count, int total);
}
