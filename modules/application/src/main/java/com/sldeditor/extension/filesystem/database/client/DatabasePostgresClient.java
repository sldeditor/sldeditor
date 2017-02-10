/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.opengis.feature.type.Name;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.extension.filesystem.database.DatabaseReadProgressInterface;

/**
 * The Class DatabasePostgresClient.
 *
 * @author Robert Ward (SCISYS)
 */
public class DatabasePostgresClient implements DatabaseClientInterface {

    /** The parent object. */
    private transient DatabaseReadProgressInterface parentObj = null;

    /** The connection. */
    private DatabaseConnection connection = null;

    /** The connected flag. */
    private boolean connected = false;

    /** The feature class list. */
    private List<String> featureClassList = new ArrayList<String>();

    /** The expected keys. */
    private static List<String> expectedKeys = Arrays.asList("host", "port", "schema", "database");

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.filesystem.database.client.DatabaseClientInterface#initialise(com.sldeditor.extension.filesystem.database.
     * DatabaseReadProgressInterface, com.sldeditor.common.data.DatabaseConnection)
     */
    @Override
    public void initialise(DatabaseReadProgressInterface parent, DatabaseConnection connection) {
        this.parentObj = parent;
        this.connection = connection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.filesystem.database.client.DatabaseClientInterface#retrieveData()
     */
    @Override
    public void retrieveData() {
        if (parentObj != null) {
            parentObj.readFeatureClassesComplete(connection, featureClassList);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.filesystem.database.client.DatabaseClientInterface#connect()
     */
    @Override
    public boolean connect() {
        connected = false;

        Map<String, Object> params = getDBConnectionParams();

        try {
            DataStore dataStore = DataStoreFinder.getDataStore(params);

            if (dataStore != null) {
                try {
                    List<Name> nameList = dataStore.getNames();

                    if (nameList != null) {
                        for (Name name : nameList) {
                            featureClassList.add(name.getLocalPart());
                        }
                    }

                    dataStore.dispose();
                    dataStore = null;
                    connected = true;
                } catch (Exception e) {
                    ConsoleManager.getInstance().exception(this, e);
                }
            }
        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        return connected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.filesystem.database.client.DatabaseClientInterface#disconnect()
     */
    @Override
    public void disconnect() {
        connected = false;
        featureClassList.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.filesystem.database.client.DatabaseClientInterface#getFeatureClassList()
     */
    @Override
    public List<String> getFeatureClassList() {
        return featureClassList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.filesystem.database.client.DatabaseClientInterface#isConnected()
     */
    @Override
    public boolean isConnected() {
        return connected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.filesystem.database.client.DatabaseClientInterface#getConnection()
     */
    @Override
    public DatabaseConnection getConnection() {
        return connection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.filesystem.database.client.DatabaseClientInterface#accept(com.sldeditor.common.data.DatabaseConnection)
     */
    @Override
    public boolean accept(DatabaseConnection connection) {
        if (connection != null) {
            return connection.getConnectionDataMap().keySet().containsAll(expectedKeys);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.filesystem.database.client.DatabaseClientInterface#getDBConnectionParams()
     */
    @Override
    public Map<String, Object> getDBConnectionParams() {
        Map<String, String> connectionDataMap = connection.getConnectionDataMap();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dbtype", "postgis");
        params.put("host", connectionDataMap.get("host"));
        Integer port = Integer.valueOf(connectionDataMap.get("port"));
        params.put("port", port);
        params.put("schema", connectionDataMap.get("schema"));
        params.put("database", connectionDataMap.get("database"));
        params.put("user", connection.getUserName());
        params.put("passwd", connection.getPassword());

        return params;
    }

}
