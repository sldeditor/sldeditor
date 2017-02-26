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

import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.extension.filesystem.database.DatabaseReadProgressInterface;
import com.sldeditor.extension.filesystem.database.client.DatabaseClientInterface;

/**
 * The Interface GeoServerConnectionManagerInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface DatabaseConnectionManagerInterface {

    /**
     * Gets the connection list.
     *
     * @return the connection list
     */
    List<DatabaseConnection> getConnectionList();

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
    DatabaseConnection getConnection(String connectionDataName);

    /**
     * Read property file.
     *
     * @param progress the progress
     */
    void readPropertyFile(DatabaseReadProgressInterface progress);

    /**
     * Gets the connection map.
     *
     * @return the connection map
     */
    Map<DatabaseConnection, DatabaseClientInterface> getConnectionMap();

    /**
     * Removes the connection.
     *
     * @param connection the connection
     */
    void removeConnection(DatabaseConnection connection);

    /**
     * Adds the new connection.
     *
     * @param progress the progress
     * @param newConnectionDetails the new connection details
     */
    void addNewConnection(DatabaseReadProgressInterface progress, DatabaseConnection newConnectionDetails);

    /**
     * Gets the DB connection params.
     *
     * @param databaseConnection the database connection
     * @return the DB connection params
     */
    Map<String, Object> getDBConnectionParams(DatabaseConnection databaseConnection);

    /**
     * Gets the matching connection, if it already exists.
     *
     * @param connection the connection
     * @return the matching connection
     */
    DatabaseConnection getMatchingConnection(DatabaseConnection connection);
}
