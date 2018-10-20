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

import com.sldeditor.common.property.EncryptedPropertiesFactory;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnectionFactory;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnectionNameInterface;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.jdbc.JDBCDataStoreFactory;

/**
 * The Class DatabaseConnection encapsulates database connection details, including connection name,
 * url, user name and password.
 *
 * <p>The class is also capable of writing to and reading from a string all of its attribute data.
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
    private Map<String, String> connectionDataMap = new HashMap<>();

    /** The user name. */
    private String userName;

    /** The password. */
    private String password;

    /** The database type label. */
    private String databaseTypeLabel;

    /** The use encryption flag. */
    private static boolean useEncrpytion = true;

    /** The detail list. */
    private transient List<DatabaseConnectionField> detailList = null;

    /** The database type. */
    private String databaseType;

    /** The initial values. */
    private Map<String, String> initialValues = new HashMap<>();

    /** The database connection name. */
    private DatabaseConnectionNameInterface databaseConnectionName = null;

    /** The no of times duplicated. */
    private int noOfTimesDuplicated = 0;

    /** The supports duplication flag. */
    private boolean supportsDuplication = false;

    /** The expected keys. */
    private List<String> expectedKeys = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param databaseType the database type
     * @param databaseTypeLabel the database type label
     * @param supportsDuplication the supports duplication
     * @param detailList the detail list
     * @param databaseConnectionName the database connection name
     */
    public DatabaseConnection(
            Param databaseType,
            String databaseTypeLabel,
            boolean supportsDuplication,
            List<DatabaseConnectionField> detailList,
            DatabaseConnectionNameInterface databaseConnectionName) {
        this.databaseType = (databaseType == null) ? "" : (String) databaseType.sample;
        this.databaseTypeLabel = databaseTypeLabel;
        this.detailList = detailList;
        this.databaseConnectionName = databaseConnectionName;
        this.supportsDuplication = supportsDuplication;

        createInitialValues();

        this.connectionDataMap = this.initialValues;

        if (detailList != null) {
            for (DatabaseConnectionField param : detailList) {
                if (!param.isOptional() && !(param.isPassword() || param.isUsername())) {
                    expectedKeys.add(param.getKey());
                }
            }
        }
    }

    /** Creates the initial values. */
    private void createInitialValues() {
        initialValues.put(DatabaseConnectionFactory.DATABASE_TYPE_KEY, databaseType);
        if (detailList != null) {
            for (DatabaseConnectionField detail : detailList) {
                String defaultValue = "";

                if (detail.getDefaultValue() != null) {
                    defaultValue = detail.getDefaultValue().toString();
                }
                initialValues.put(detail.getKey(), defaultValue);
            }
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

                Map<String, String> localConnectionDataMap = new HashMap<>();

                for (int index = 3; index < components.length; index++) {
                    String[] property = components[index].split(PROPERTY_DELIMETER);
                    if (property.length == 2) {
                        localConnectionDataMap.put(property[0], getString(property[1]));
                    }
                }

                connectionData = DatabaseConnectionFactory.decodeString(localConnectionDataMap);

                if (connectionData != null) {
                    connectionData.connectionName = components[0];
                    connectionData.userName = getString(components[1]);
                    connectionData.password = getString(components[2]);

                    connectionData.setConnectionDataMap(localConnectionDataMap);
                }
            }
        }
        return connectionData;
    }

    /**
     * Gets the string.
     *
     * @param string the string
     * @return the string
     */
    private static String getString(String string) {
        return (string.equals("null")) ? null : string;
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
                return EncryptedPropertiesFactory.getInstance().decrypt(password);
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
            this.password = EncryptedPropertiesFactory.getInstance().encrypt(password);
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
        if (connectionName == null) {
            return (o.connectionName == null) ? 0 : 1;
        }
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
        for (Entry<String, String> entry : connectionDataMap.entrySet()) {
            sb.append(DELIMETER);
            sb.append(entry.getKey());
            sb.append(PROPERTY_DELIMETER);
            sb.append(connectionDataMap.get(entry.getKey()));
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
        for (Entry<String, String> entry : connectionDataMap.entrySet()) {
            this.connectionDataMap.put(entry.getKey(), entry.getValue());
        }

        if (this.databaseConnectionName != null) {
            this.connectionName =
                    this.databaseConnectionName.getConnectionName(
                            DUPLICATE_PREFIX, noOfTimesDuplicated, connectionDataMap);
        }
    }

    /**
     * Duplicate.
     *
     * @return the database connection
     */
    public DatabaseConnection duplicate() {
        return new DatabaseConnection(this);
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
    public DatabaseConnectionNameInterface getDatabaseConnectionName() {
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

    /**
     * Gets the DB connection params.
     *
     * @return the DB connection params
     */
    public Map<String, Object> getDBConnectionParams() {
        Map<String, String> localConnectionDataMap = getConnectionDataMap();

        Map<String, Object> params = new LinkedHashMap<>();
        params.put(JDBCDataStoreFactory.DBTYPE.key, getDatabaseType());
        for (DatabaseConnectionField field : detailList) {
            String key = field.getKey();

            if (field.isUsername()) {
                if (isDataPopulated(getUserName())) {
                    params.put(key, getUserName());
                }
            } else if (field.isPassword()) {
                if (isDataPopulated(getPassword())) {
                    params.put(key, getPassword());
                }
            } else if (field.isOptional()) {
                String value = localConnectionDataMap.get(key);
                if (isDataPopulated(value)) {
                    params.put(key, getValue(value, field.getType()));
                }
            } else {
                String value = localConnectionDataMap.get(key);
                params.put(key, getValue(value, field.getType()));
            }
        }

        return params;
    }

    /**
     * Checks if is data populated.
     *
     * @param string the string
     * @return true, if is data populated
     */
    private boolean isDataPopulated(String string) {
        return ((string != null) && !string.trim().isEmpty());
    }

    /**
     * Gets the value.
     *
     * @param value the value
     * @param type the type
     * @return the value
     */
    private Object getValue(String value, Class<?> type) {
        if (type == String.class) {
            return value;
        } else if (type == Integer.class) {
            return Integer.valueOf(value);
        } else if (type == Long.class) {
            return Long.valueOf(value);
        } else if (type == File.class) {
            return value;
        }

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((connectionDataMap == null) ? 0 : connectionDataMap.hashCode());
        result = prime * result + ((connectionName == null) ? 0 : connectionName.hashCode());
        result = prime * result + ((databaseType == null) ? 0 : databaseType.hashCode());
        result = prime * result + ((databaseTypeLabel == null) ? 0 : databaseTypeLabel.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DatabaseConnection other = (DatabaseConnection) obj;
        if (connectionDataMap == null) {
            if (other.connectionDataMap != null) {
                return false;
            }
        } else if (!connectionDataMap.equals(other.connectionDataMap)) {
            return false;
        }
        if (connectionName == null) {
            if (other.connectionName != null) {
                return false;
            }
        } else if (!connectionName.equals(other.connectionName)) {
            return false;
        }
        if (databaseType == null) {
            if (other.databaseType != null) {
                return false;
            }
        } else if (!databaseType.equals(other.databaseType)) {
            return false;
        }
        if (databaseTypeLabel == null) {
            if (other.databaseTypeLabel != null) {
                return false;
            }
        } else if (!databaseTypeLabel.equals(other.databaseTypeLabel)) {
            return false;
        }
        if (userName == null) {
            if (other.userName != null) {
                return false;
            }
        } else if (!userName.equals(other.userName)) {
            return false;
        }

        if (this.supportsDuplication != other.supportsDuplication) {
            return false;
        }
        return true;
    }
}
