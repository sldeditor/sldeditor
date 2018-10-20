/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.tool.dbconnectionlist;

import java.io.Serializable;
import java.util.Map;

/**
 * The Interface DatabaseConnectionNameInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface DatabaseConnectionNameInterface extends Serializable {

    /**
     * Gets the connection name.
     *
     * @param duplicatePrefix the duplicate prefix
     * @param noOfDuplicates the no of duplicates
     * @param properties the properties
     * @return the connection name
     */
    String getConnectionName(
            String duplicatePrefix, int noOfDuplicates, Map<String, String> properties);
}
