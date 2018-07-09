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

package com.sldeditor.tool.databaseconnection;

import com.sldeditor.common.data.DatabaseConnection;
import java.util.List;

/**
 * The Interface DatabaseConnectStateInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface DatabaseConnectStateInterface {

    /**
     * Checks if is connected.
     *
     * @param connection the connection
     * @return true, if is connected
     */
    boolean isConnected(DatabaseConnection connection);

    /**
     * Connect to GeoServer.
     *
     * @param connectionList the connection list
     */
    void connect(List<DatabaseConnection> connectionList);

    /**
     * Disconnect from GeoServer.
     *
     * @param connectionList the connection list
     */
    void disconnect(List<DatabaseConnection> connectionList);
}
