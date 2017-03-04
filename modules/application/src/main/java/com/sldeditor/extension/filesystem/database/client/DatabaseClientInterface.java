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
package com.sldeditor.extension.filesystem.database.client;

import java.util.List;
import java.util.Map;

import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.extension.filesystem.database.DatabaseReadProgressInterface;

/**
 * The Interface DatabaseClientInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface DatabaseClientInterface {

    /**
     * Accept.
     *
     * @param connection the connection
     * @return true, if successful
     */
    boolean accept(DatabaseConnection connection);
    
    /**
     * Initialise a new database client.
     *
     * @param parent the parent
     * @param connection the connection
     */
    void initialise(DatabaseReadProgressInterface parent, DatabaseConnection connection);

    /**
     * Retrieve data from GeoServer.
     */
    void retrieveData();

    /**
     * Connect.
     *
     * @return true, if successful
     */
    boolean connect();

    /**
     * Disconnect.
     */
    void disconnect();

    /**
     * Gets the feature class list.
     *
     * @return the feature class list
     */
    List<String> getFeatureClassList();

    /**
     * Returns the is connected flag.
     *
     * @return true, if is connected
     */
    boolean isConnected();

    /**
     * Gets the connection.
     *
     * @return the connection
     */
    DatabaseConnection getConnection();

    /**
     * Gets the DB connection params.
     *
     * @return the DB connection params
     */
    Map<String, Object> getDBConnectionParams();

}