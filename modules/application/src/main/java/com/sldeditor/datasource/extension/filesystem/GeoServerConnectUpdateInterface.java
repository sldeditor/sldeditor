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

package com.sldeditor.datasource.extension.filesystem;

import com.sldeditor.common.data.GeoServerConnection;
import java.util.List;

/**
 * The Interface GeoServerConnectUpdateInterface allows information about GeoServer connections to
 * be transferred.
 *
 * @author Robert Ward (SCISYS)
 */
public interface GeoServerConnectUpdateInterface {

    /**
     * Gets the connection details.
     *
     * @return the connection details
     */
    List<GeoServerConnection> getConnectionDetails();

    /**
     * Adds the new connection.
     *
     * @param newConnectionDetails the new connection details
     */
    void addNewConnection(GeoServerConnection newConnectionDetails);

    /**
     * Update connection details.
     *
     * @param originalConnectionDetails the original connection details
     * @param newConnectionDetails the new connection details
     */
    void updateConnectionDetails(
            GeoServerConnection originalConnectionDetails,
            GeoServerConnection newConnectionDetails);

    /**
     * Delete connections.
     *
     * @param connectionList the connection list
     */
    void deleteConnections(List<GeoServerConnection> connectionList);
}
