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
package com.sldeditor.common.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sldeditor.common.property.EncryptedProperties;

/**
 * The Class DatabaseConnection encapsulates database connection details, including connection name, url, user name and password.
 * <p>
 * The class is also capable of writing to and reading from a string all of its attribute data.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DatabaseConnection implements Comparable<DatabaseConnection>, Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7210287238346383636L;

    /** The Constant DUPLICATE_PREFIX. */
    private static final String DUPLICATE_PREFIX = "Copy of ";

    /** The Constant DELIMETER. */
    private static final String DELIMETER = ";";

    /** The Constant PROPERTY_DELIMETER. */
    private static final String PROPERTY_DELIMETER = "~";

    /** The connection name. */
    private String connectionName;

    /** The connection data map. */
    private Map<String, String> connectionDataMap = new HashMap<String, String>();

    /** The user name. */
    private String userName;

    /** The password. */
    private String password;

    /** The use encryption flag. */
    private static boolean useEncrpytion = true;

    /**
     * Default constructor.
     */
    public DatabaseConnection() {
    }

    /**
     * Decode an encoded GeoServerConnection string.
     *
     * @param connectionString the connection string
     * @return the geo server connection
     */
    public static DatabaseConnection decodeString(String connectionString) {
        DatabaseConnection connectionData = null;
        if (connectionString != null) {
            String[] components = connectionString.split(DELIMETER);
            if (components.length >= 3) {
                connectionData = new DatabaseConnection();

                connectionData.connectionName = components[0];
                connectionData.userName = components[1];
                connectionData.password = components[2];

                for (int index = 3; index < components.length; index++) {
                    String[] property = components[index].split(PROPERTY_DELIMETER);
                    if (property.length == 2) {
                        connectionData.connectionDataMap.put(property[0], property[1]);
                    }
                }
            }
        }
        return connectionData;
    }

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     *
     * @param userName the new user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        if (password == null) {
            return "";
        } else {
            if (useEncrpytion) {
                return EncryptedProperties.getInstance().decrypt(password);
            }
            return password;
        }
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        if (useEncrpytion) {
            this.password = EncryptedProperties.getInstance().encrypt(password);
        } else {
            this.password = password;
        }
    }

    /**
     * Gets the connection name.
     *
     * @return the connection name
     */
    public String getConnectionName() {
        return connectionName;
    }

    /**
     * Compare to another DatabaseConnection object.
     *
     * @param o the o
     * @return the int
     */
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(DatabaseConnection o) {
        return connectionName.compareTo(o.connectionName);
    }

    /**
     * Update the contents.
     *
     * @param existingItem the existing item
     */
    public void update(DatabaseConnection existingItem) {
        this.connectionName = existingItem.connectionName;
        this.connectionDataMap = existingItem.connectionDataMap;
        this.userName = existingItem.userName;
        this.password = existingItem.password;
    }

    /**
     * Encode as string.
     *
     * @return the string
     */
    public String encodeAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append(connectionName);
        sb.append(DELIMETER);
        sb.append(userName);
        sb.append(DELIMETER);
        sb.append(password);
        for (String key : connectionDataMap.keySet()) {
            sb.append(DELIMETER);
            sb.append(key);
            sb.append(PROPERTY_DELIMETER);
            sb.append(connectionDataMap.get(key));
        }
        return sb.toString();
    }

    /**
     * Gets the connection data map.
     *
     * @return the connectionDataMap
     */
    public Map<String, String> getConnectionDataMap() {
        return connectionDataMap;
    }

    /**
     * Sets the connection data map.
     *
     * @param connectionDataMap the connectionDataMap to set
     */
    public void setConnectionDataMap(Map<String, String> connectionDataMap) {
        this.connectionDataMap = connectionDataMap;
    }

    /**
     * Duplicate.
     *
     * @return the database connection
     */
    public DatabaseConnection duplicate() {
        DatabaseConnection newItem = new DatabaseConnection();
        newItem.connectionName = DUPLICATE_PREFIX + this.connectionName;
        newItem.connectionDataMap = this.connectionDataMap;
        newItem.userName = this.userName;
        newItem.password = this.password;

        return newItem;
    }

    /**
     * Sets the connection name.
     *
     * @param connectionName the connectionName to set
     */
    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }
}
