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

package com.sldeditor.common.connection;

import java.util.List;
import java.util.Map;

import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.extension.filesystem.geoserver.GeoServerReadProgress;
import com.sldeditor.extension.filesystem.geoserver.client.GeoServerClientInterface;

/**
 * The Interface GeoServerConnectionManagerInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface GeoServerConnectionManagerInterface {

    /**
     * Gets the connection list.
     *
     * @return the connection list
     */
    List<GeoServerConnection> getConnectionList();

    /**
     * Update connection list.
     */
    void updateList();

    /**
     * Gets the connection data for the given name.
     *
     * @param connectionDataName the connection data name
     * @return the connection
     */
    GeoServerConnection getConnection(String connectionDataName);

    /**
     * Read property file.
     *
     * @param progress the progress
     */
    void readPropertyFile(GeoServerReadProgress progress);

    /**
     * Gets the connection map.
     *
     * @return the connection map
     */
    Map<GeoServerConnection, GeoServerClientInterface> getConnectionMap();

    /**
     * Removes the connection.
     *
     * @param connection the connection
     */
    void removeConnection(GeoServerConnection connection);

    /**
     * Adds the new connection.
     *
     * @param progress the progress
     * @param newConnectionDetails the new connection details
     */
    void addNewConnection(GeoServerReadProgress progress, GeoServerConnection newConnectionDetails);
}
