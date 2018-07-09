/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.tool.dbconnectionlist;

import com.sldeditor.common.data.DatabaseConnection;
import java.util.List;
import java.util.Map;

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
