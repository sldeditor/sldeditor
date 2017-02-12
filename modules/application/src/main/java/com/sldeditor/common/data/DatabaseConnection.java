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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataAccessFactory.Param;
import org.geotools.jdbc.JDBCDataStoreFactory;

import com.sldeditor.common.property.EncryptedProperties;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnectionFactory;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnectionName;

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

    /** The database type label. */
    private String databaseTypeLabel;

    /** The use encryption flag. */
    private static boolean useEncrpytion = true;

    /** The detail list. */
    private List<DatabaseConnectionField> detailList = null;

    /** The database type. */
    private String databaseType;

    /** The initial values. */
    private Map<String, String> initialValues = new HashMap<String, String>();

    /** The database connection name. */
    private DatabaseConnectionName databaseConnectionName = null;

    /** The no of times duplicated. */
    private int noOfTimesDuplicated = 0;

    /** The supports duplication flag. */
    private boolean supportsDuplication = false;

    /** The expected keys. */
    private List<String> expectedKeys = new ArrayList<String>();

    /**
     * Constructor.
     *
     * @param databaseType the database type
     * @param databaseTypeLabel the database type label
     * @param supportsDuplication the supports duplication
     * @param detailList the detail list
     * @param databaseConnectionName the database connection name
     * @param databaseClientConnector the database client connector
     */
    public DatabaseConnection(Param databaseType, String databaseTypeLabel,
            boolean supportsDuplication, List<DatabaseConnectionField> detailList,
            DatabaseConnectionName databaseConnectionName) {
        this.databaseType = (String) databaseType.sample;
        this.databaseTypeLabel = databaseTypeLabel;
        this.detailList = detailList;
        this.databaseConnectionName = databaseConnectionName;
        this.supportsDuplication = supportsDuplication;

        createInitialValues();

        this.connectionDataMap = this.initialValues;

        for (DatabaseConnectionField param : detailList) {
            if (!param.isOptional() && (param.isPassword() || param.isUsername())) {
                expectedKeys.add(param.getKey());
            }
        }
    }

    /**
     * Creates the initial values.
     */
    private void createInitialValues() {
        initialValues.put(DatabaseConnectionFactory.DATABASE_TYPE_KEY, databaseType);
        for (DatabaseConnectionField detail : detailList) {
            initialValues.put(detail.getKey(), detail.getFieldName());
        }
    }

    /**
     * Instantiates a duplicate database connection.
     *
     * @param databaseConnection the database connection to duplicate
     */
    public DatabaseConnection(DatabaseConnection databaseConnection) {
        this.databaseType = databaseConnection.databaseType;
        this.databaseTypeLabel = databaseConnection.databaseTypeLabel;
        this.detailList = databaseConnection.detailList;
        this.noOfTimesDuplicated = databaseConnection.noOfTimesDuplicated + 1;
        this.userName = databaseConnection.userName;
        this.password = databaseConnection.password;
        this.databaseConnectionName = databaseConnection.databaseConnectionName;
        this.supportsDuplication = databaseConnection.supportsDuplication;
        this.setConnectionDataMap(databaseConnection.getConnectionDataMap());

        createInitialValues();
    }

    /**
     * Decode an encoded DatabaseConnection string.
     *
     * @param connectionString the connection string
     * @return the database connection
     */
    public static DatabaseConnection decodeString(String connectionString) {
        DatabaseConnection connectionData = null;
        if (connectionString != null) {
            String[] components = connectionString.split(DELIMETER);
            if (components.length >= 3) {

                Map<String, String> localConnectionDataMap = new HashMap<String, String>();

                for (int index = 3; index < components.length; index++) {
                    String[] property = components[index].split(PROPERTY_DELIMETER);
                    if (property.length == 2) {
                        localConnectionDataMap.put(property[0],
                                (property[1].equals("null")) ? null : property[1]);
                    }
                }

                connectionData = DatabaseConnectionFactory.decodeString(localConnectionDataMap);

                if (connectionData != null) {
                    connectionData.connectionName = components[0];
                    connectionData.userName = (components[1].equals("null")) ? null : components[1];
                    connectionData.password = (components[2].equals("null")) ? null : components[2];

                    connectionData.setConnectionDataMap(localConnectionDataMap);
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
        for (String key : connectionDataMap.keySet()) {
            this.connectionDataMap.put(key, connectionDataMap.get(key));
        }

        if (this.databaseConnectionName != null) {
            this.connectionName = this.databaseConnectionName.getConnectionName(DUPLICATE_PREFIX,
                    noOfTimesDuplicated, connectionDataMap);
        }
    }

    /**
     * Duplicate.
     *
     * @return the database connection
     */
    public DatabaseConnection duplicate() {
        DatabaseConnection newItem = new DatabaseConnection(this);

        return newItem;
    }

    /**
     * Gets the detail list.
     *
     * @return the detailList
     */
    public List<DatabaseConnectionField> getDetailList() {
        return detailList;
    }

    /**
     * Gets the database connection name.
     *
     * @return the databaseConnectionName
     */
    public DatabaseConnectionName getDatabaseConnectionName() {
        return databaseConnectionName;
    }

    /**
     * Gets the database type.
     *
     * @return the databaseType
     */
    public String getDatabaseType() {
        return databaseType;
    }

    /**
     * Gets the database type label.
     *
     * @return the databaseTypeLabel
     */
    public String getDatabaseTypeLabel() {
        return databaseTypeLabel;
    }

    /**
     * Checks if is supports duplication.
     *
     * @return the supportsDuplication
     */
    public boolean isSupportsDuplication() {
        return supportsDuplication;
    }

    /**
     * Gets the expected keys.
     *
     * @return the expected keys
     */
    public List<String> getExpectedKeys() {
        return expectedKeys;
    }

    public Map<String, Object> getDBConnectionParams() {
        Map<String, String> connectionDataMap = getConnectionDataMap();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(JDBCDataStoreFactory.DBTYPE.key, getDatabaseType());
        for (DatabaseConnectionField field : detailList) {
            if (field.isUsername()) {
                if ((getUserName() != null) && !getUserName().trim().isEmpty()) {
                    params.put(field.getKey(), getUserName());
                }
            } else if (field.isPassword()) {
                if ((getPassword() != null) && !getPassword().trim().isEmpty()) {
                    params.put(field.getKey(), getPassword());
                }
            } else if (field.isOptional()) {
                String value = connectionDataMap.get(field.getKey());
                if ((value != null) && !value.trim().isEmpty()) {
                    params.put(field.getKey(), getValue(value, field.getType()));
                }
            }
            else
            {
                String value = connectionDataMap.get(field.getKey());
                params.put(field.getKey(), getValue(value, field.getType()));
            }
        }

        return params;
    }

    private Object getValue(String value, Class<?> type) {
        if(type == String.class)
        {
            return value;
        }
        else if(type == Integer.class)
        {
            return Integer.valueOf(value);
        }
        else if(type == Long.class)
        {
            return Long.valueOf(value);
        }
        else if(type == File.class)
        {
            return value;
        }

        return null;
    }
}
