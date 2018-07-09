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

package com.sldeditor.extension.filesystem.geoserver.client;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.GeoServerConnection;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A factory for creating GeoServerRESTManager objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class GeoServerRESTManagerFactory {

    /** The map. */
    private Map<GeoServerConnection, GeoServerRESTManager> map =
            new HashMap<GeoServerConnection, GeoServerRESTManager>();

    /** The instance. */
    private static GeoServerRESTManagerFactory instance = null;

    /** Instantiates a new geo server rest manager factory. */
    private GeoServerRESTManagerFactory() {}

    /**
     * Gets the single instance of GeoServerRESTManagerFactory.
     *
     * @return single instance of GeoServerRESTManagerFactory
     */
    private static GeoServerRESTManagerFactory getInstance() {
        if (instance == null) {
            instance = new GeoServerRESTManagerFactory();
        }

        return instance;
    }

    /**
     * Gets the manager.
     *
     * @param connection the connection
     * @return the manager
     */
    public static GeoServerRESTManager getManager(GeoServerConnection connection) {
        return getInstance().internal_getManager(connection);
    }

    /**
     * Internal_get manager.
     *
     * @param connection the connection
     * @return the geo server rest manager
     */
    private GeoServerRESTManager internal_getManager(GeoServerConnection connection) {
        if (connection == null) {
            return null;
        }

        GeoServerRESTManager manager = map.get(connection);

        if (manager == null) {
            URL geoserverURL = connection.getUrl();

            if (geoserverURL != null) {
                try {
                    if (connection.getUserName() != null) {
                        manager =
                                new GeoServerRESTManager(
                                        geoserverURL,
                                        connection.getUserName(),
                                        connection.getPassword());

                        map.put(connection, manager);
                    }
                } catch (IllegalArgumentException e) {
                    ConsoleManager.getInstance().exception(this, e);
                }
            }
        }
        return manager;
    }

    /**
     * Delete connection.
     *
     * @param connection the connection
     */
    public static void deleteConnection(GeoServerConnection connection) {
        getInstance().internal_deleteConnection(connection);
    }

    /**
     * Internal_delete connection.
     *
     * @param connection the connection
     */
    private void internal_deleteConnection(GeoServerConnection connection) {
        map.remove(connection);
    }
}
