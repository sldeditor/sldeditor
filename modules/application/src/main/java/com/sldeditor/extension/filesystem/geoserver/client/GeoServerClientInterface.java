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

package com.sldeditor.extension.filesystem.geoserver.client;

import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.data.GeoServerLayer;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.extension.filesystem.geoserver.GeoServerReadProgressInterface;
import java.util.List;

/**
 * The Interface GeoServerClientInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface GeoServerClientInterface {

    /**
     * Initialise a new geo server client.
     *
     * @param parent the parent
     * @param connection the connection
     */
    void initialise(GeoServerReadProgressInterface parent, GeoServerConnection connection);

    /** Retrieve data from GeoServer. */
    void retrieveData();

    /**
     * Gets the style.
     *
     * @param styleWrapper the style wrapper
     * @return the style
     */
    String getStyle(StyleWrapper styleWrapper);

    /**
     * Checks if supplied workspace name is the default workspace.
     *
     * @param workspaceName the workspace name to check
     * @return true, if is default workspace
     */
    boolean isDefaultWorkspace(String workspaceName);

    /**
     * Connect.
     *
     * @return true, if successful
     */
    boolean connect();

    /** Disconnect. */
    void disconnect();

    /**
     * Upload sld.
     *
     * @param styleWrapper the style wrapper
     * @param sldBody the sld body
     * @return true, if successful
     */
    boolean uploadSLD(StyleWrapper styleWrapper, String sldBody);

    /**
     * Update layer styles.
     *
     * @param originalLayer the original layer
     * @return true, if successful
     */
    boolean updateLayerStyles(GeoServerLayer originalLayer);

    /**
     * Gets the workspace list.
     *
     * @return the workspace list
     */
    List<String> getWorkspaceList();

    /**
     * Delete style.
     *
     * @param styleToDelete the style to delete
     * @return true, if successful
     */
    boolean deleteStyle(StyleWrapper styleToDelete);

    /**
     * Gets the default workspace name.
     *
     * @return the default workspace name
     */
    String getDefaultWorkspaceName();

    /**
     * Returns the is connected flag.
     *
     * @return true, if is connected
     */
    boolean isConnected();

    /**
     * Gets the connection.
     *
     * @return the connection
     */
    GeoServerConnection getConnection();

    /**
     * Refresh workspace.
     *
     * @param workspaceName the workspace name
     */
    void refreshWorkspace(String workspaceName);

    /**
     * Delete workspace.
     *
     * @param workspaceName the workspace name
     * @return true, if successful
     */
    boolean deleteWorkspace(String workspaceName);
}
