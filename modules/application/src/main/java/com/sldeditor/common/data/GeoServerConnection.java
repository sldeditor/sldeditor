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
import java.net.MalformedURLException;
import java.net.URL;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.property.EncryptedPropertiesFactory;

/**
 * The Class GeoServerConnection encapsulates GeoServer connection details,
 * including connection name, url, user name and password.
 * 
 * <p>The class is also capable of writing to and reading from a string all
 * of its attribute data.
 * 
 * @author Robert Ward (SCISYS)
 */
public class GeoServerConnection implements Comparable<GeoServerConnection>, Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1230528722618014923L;

    /** The Constant DUPLICATE_PREFIX. */
    private static final String DUPLICATE_PREFIX = "Copy of ";

    /** The Constant DELIMETER. */
    private static final String DELIMETER = ";";

    /** The connection name. */
    private String connectionName;

    /** The url. */
    private URL url;

    /** The user name. */
    private String userName;

    /** The password. */
    private String password;

    /** The use encryption flag. */
    private static boolean useEncryption = true;

    /**
     * Default constructor.
     */
    public GeoServerConnection() {
    }

    /**
     * Decode an encoded GeoServerConnection string.
     *
     * @param connectionString the connection string
     * @return the geo server connection
     */
    public static GeoServerConnection decodeString(String connectionString) {
        GeoServerConnection connectionData = null;
        if (connectionString != null) {
            String[] components = connectionString.split(DELIMETER);
            if (components.length == 4) {
                connectionData = new GeoServerConnection();

                connectionData.connectionName = components[0];
                try {
                    connectionData.url = new URL(components[1]);
                } catch (MalformedURLException e) {
                    ConsoleManager.getInstance().exception(GeoServerConnection.class, e);
                    return null;
                }
                connectionData.userName = components[2];
                connectionData.password = components[3];
            }
        }
        return connectionData;
    }

    /**
     * Gets the url.
     *
     * @return the url
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Sets the url.
     *
     * @param url the new url
     */
    public void setUrl(URL url) {
        this.url = url;
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
            if (useEncryption) {
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
        if (useEncryption) {
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
     * Sets the connection name.
     *
     * @param connectionName the new connection name
     */
    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    /**
     * Compare to another GeoServerConnection object.
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
    public int compareTo(GeoServerConnection o) {
        return connectionName.compareTo(o.connectionName);
    }

    /**
     * Update the contents.
     *
     * @param existingItem the existing item
     */
    public void update(GeoServerConnection existingItem) {
        this.connectionName = existingItem.connectionName;
        this.url = existingItem.url;
        this.userName = existingItem.userName;
        this.password = existingItem.password;
    }

    /**
     * Duplicate item.
     *
     * @return the geo server connection
     */
    public GeoServerConnection duplicate() {

        GeoServerConnection newItem = new GeoServerConnection();
        newItem.connectionName = DUPLICATE_PREFIX + this.connectionName;
        newItem.url = this.url;
        newItem.userName = this.userName;
        newItem.password = this.password;

        return newItem;
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
        sb.append(url);
        sb.append(DELIMETER);
        sb.append(userName);
        sb.append(DELIMETER);
        sb.append(password);

        return sb.toString();
    }
}
