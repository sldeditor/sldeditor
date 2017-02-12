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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.property.PropertyManagerFactory;
import com.sldeditor.extension.filesystem.database.DatabaseInput;
import com.sldeditor.extension.filesystem.database.DatabaseReadProgressInterface;
import com.sldeditor.extension.filesystem.database.client.DatabaseClient;
import com.sldeditor.extension.filesystem.database.client.DatabaseClientInterface;

/**
 * The Class DatabaseConnectionManager.
 *
 * @author Robert Ward (SCISYS)
 */
public class DatabaseConnectionManager implements DatabaseConnectionManagerInterface {

    /** The Constant DATABASE_CONNECTION_FIELD. */
    private static final String DATABASE_CONNECTION_FIELD = "Database.connection";

    /**
     * Gets the singleton instance of DatabaseConnectionManager.
     *
     * @return singleton instance of DatabaseConnectionManager
     */
    private static DatabaseConnectionManagerInterface instance = null;

    /** The DatabaseClientInterface class to create. */
    public List<DatabaseClientInterface> databaseClientClassList = new ArrayList<DatabaseClientInterface>();

    /** The connection map. */
    private Map<DatabaseConnection, DatabaseClientInterface> connectionMap = new LinkedHashMap<DatabaseConnection, DatabaseClientInterface>();

    /**
     * Gets the singleton instance of DatabaseConnectionManager.
     *
     * @return singleton instance of DatabaseConnectionManager
     */
    public static DatabaseConnectionManagerInterface getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionManager();
        }

        return instance;
    }

    /**
     * Instantiates a new database connection manager.
     */
    public DatabaseConnectionManager() {
        databaseClientClassList.add(new DatabaseClient());
    }

    /**
     * Gets the connection list.
     *
     * @return the connection list
     */
    @Override
    public List<DatabaseConnection> getConnectionList() {
        List<DatabaseConnection> connectionList = new ArrayList<DatabaseConnection>();

        List<String> valueList = PropertyManagerFactory.getInstance()
                .getMultipleValues(DATABASE_CONNECTION_FIELD);

        for (String connectionString : valueList) {
            DatabaseConnection connection = DatabaseConnection.decodeString(connectionString);
            if (connection != null) {
                connectionList.add(connection);
            }
        }

        return connectionList;
    }

    /**
     * Update connection list.
     */
    @Override
    public void updateList() {
        Set<DatabaseConnection> keySet = connectionMap.keySet();
        int count = 0;
        PropertyManagerFactory.getInstance().clearValue(DATABASE_CONNECTION_FIELD, true);
        for (DatabaseConnection connection : keySet) {
            count++;
            PropertyManagerFactory.getInstance().updateValue(DATABASE_CONNECTION_FIELD, count,
                    connection.encodeAsString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.connection.DatabaseReadProgressInterface#getConnection(java.lang.String)
     */
    @Override
    public DatabaseConnection getConnection(String connectionDataName) {
        if (connectionDataName != null) {
            List<DatabaseConnection> connectionList = getConnectionList();
            for (DatabaseConnection existingConnectionData : connectionList) {
                if (existingConnectionData.getConnectionName().compareTo(connectionDataName) == 0) {
                    return existingConnectionData;
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.connection.DatabaseReadProgressInterface#readPropertyFile(com.sldeditor.extension.filesystem.geoserver.
     * GeoServerReadProgress)
     */
    public void readPropertyFile(DatabaseReadProgressInterface progress) {
        List<DatabaseConnection> connectionList = getConnectionList();

        for (DatabaseConnection connection : connectionList) {
            connectionMap.put(connection, createDatabaseClient(progress, connection));
        }
    }

    /**
     * Creates the database client.
     *
     * @param progress the progress
     * @param connection the connection
     * @return the database client
     */
    private DatabaseClientInterface createDatabaseClient(DatabaseReadProgressInterface progress,
            DatabaseConnection connection) {

        for (DatabaseClientInterface client : databaseClientClassList) {
            if (client.accept(connection)) {
                DatabaseClientInterface newClient = null;
                try {
                    newClient = (DatabaseClientInterface) Class.forName(client.getClass().getName())
                            .newInstance();
                    newClient.initialise(progress, connection);

                    return newClient;
                } catch (InstantiationException e) {
                    ConsoleManager.getInstance().exception(DatabaseInput.class, e);
                } catch (IllegalAccessException e) {
                    ConsoleManager.getInstance().exception(DatabaseInput.class, e);
                } catch (ClassNotFoundException e) {
                    ConsoleManager.getInstance().exception(DatabaseInput.class, e);
                }
            }
        }

        return null;
    }

    /**
     * Gets the connection map.
     *
     * @return the connectionMap
     */
    public Map<DatabaseConnection, DatabaseClientInterface> getConnectionMap() {
        return connectionMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.connection.DatabaseReadProgressInterface#removeConnection(com.sldeditor.common.data.DatabaseConnection)
     */
    @Override
    public void removeConnection(DatabaseConnection connection) {
        connectionMap.remove(connection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.connection.DatabaseReadProgressInterface#addNewConnection(com.sldeditor.extension.filesystem.geoserver.
     * GeoServerReadProgress, com.sldeditor.common.data.DatabaseConnection)
     */
    @Override
    public void addNewConnection(DatabaseReadProgressInterface progress,
            DatabaseConnection newConnectionDetails) {
        connectionMap.put(newConnectionDetails,
                createDatabaseClient(progress, newConnectionDetails));
    }

    /**
     * Destroy instance.
     */
    public static void destroyInstance() {
        instance = null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.connection.DatabaseConnectionManagerInterface#getDBConnectionParams(com.sldeditor.common.data.DatabaseConnection)
     */
    @Override
    public Map<String, Object> getDBConnectionParams(DatabaseConnection databaseConnection) {
        DatabaseClientInterface client = connectionMap.get(databaseConnection);
        
        if(client != null)
        {
            return client.getDBConnectionParams();
        }
        return null;
    }

}
