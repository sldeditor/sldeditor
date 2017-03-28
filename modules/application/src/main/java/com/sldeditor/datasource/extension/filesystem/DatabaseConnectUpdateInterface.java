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

import java.util.List;

import com.sldeditor.common.data.DatabaseConnection;

/**
 * The Interface DatabaseConnectUpdateInterface allows information about database connections to be transferred.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface DatabaseConnectUpdateInterface {

    /**
     * Gets the connection details.
     *
     * @return the connection details
     */
    List<DatabaseConnection> getConnectionDetails();

    /**
     * Adds the new connection.
     *
     * @param newConnectionDetails the new connection details
     */
    void addNewConnection(DatabaseConnection newConnectionDetails);

    /**
     * Update connection details.
     *
     * @param originalConnectionDetails the original connection details
     * @param newConnectionDetails the new connection details
     */
    void updateConnectionDetails(DatabaseConnection originalConnectionDetails,
            DatabaseConnection newConnectionDetails);

    /**
     * Delete connections.
     *
     * @param connectionList the connection list
     */
    void deleteConnections(List<DatabaseConnection> connectionList);
}
