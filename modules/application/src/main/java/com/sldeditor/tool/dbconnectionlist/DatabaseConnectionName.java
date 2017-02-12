/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.tool.dbconnectionlist;

import java.util.Map;

/**
 * The Interface DatabaseConnectionName.
 *
 * @author Robert Ward (SCISYS)
 */
public interface DatabaseConnectionName {

    /**
     * Gets the connection name.
     *
     * @param duplicatePrefix the duplicate prefix
     * @param noOfDuplicates the no of duplicates
     * @param properties the properties
     * @return the connection name
     */
    String getConnectionName(String duplicatePrefix, int noOfDuplicates, Map<String, String> properties);
}
