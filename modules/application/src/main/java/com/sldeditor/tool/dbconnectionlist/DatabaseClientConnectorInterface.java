/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.tool.dbconnectionlist;

import java.util.List;
import java.util.Map;

import com.sldeditor.common.data.DatabaseConnection;

/**
 * The Interface DatabaseClientConnectorInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface DatabaseClientConnectorInterface {

    /**
     * Gets the expected keys.
     *
     * @return the expected keys
     */
    public List<String> getExpectedKeys();

    /**
     * Gets the DB connection params.
     *
     * @param connection the connection
     * @return the DB connection params
     */
    public Map<String, Object> getDBConnectionParams(DatabaseConnection connection);
}
