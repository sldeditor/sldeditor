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

package com.sldeditor.test.unit.extension.filesystem.geoserver;

import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.data.GeoServerLayer;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.extension.filesystem.geoserver.GeoServerReadProgressInterface;
import com.sldeditor.extension.filesystem.geoserver.client.GeoServerClientInterface;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class DummyGeoServerClient.
 *
 * @author Robert Ward (SCISYS)
 */
public class DummyGeoServerClient implements GeoServerClientInterface {

    private GeoServerConnection connection = null;

    private boolean connected = true;

    private static final String DEFAULT_WORKSPACE_NAME = "<Test Default Workspace>";

    public List<String> workspaceList = new ArrayList<String>();

    @Override
    public void initialise(GeoServerReadProgressInterface parent, GeoServerConnection connection) {
        this.connection = connection;
        GeoServerLayer.setDefaultWorkspaceName(DEFAULT_WORKSPACE_NAME);
    }

    @Override
    public void retrieveData() {}

    @Override
    public String getStyle(StyleWrapper styleWrapper) {
        return "downloaded sld data";
    }

    @Override
    public boolean isDefaultWorkspace(String workspaceName) {
        if (workspaceName != null) {
            return (workspaceName.compareTo(DEFAULT_WORKSPACE_NAME) == 0);
        }
        return true;
    }

    @Override
    public boolean connect() {
        connected = true;
        return false;
    }

    @Override
    public void disconnect() {
        connected = false;
    }

    @Override
    public boolean uploadSLD(StyleWrapper styleWrapper, String sldBody) {
        return true;
    }

    @Override
    public boolean updateLayerStyles(GeoServerLayer originalLayer) {
        return true;
    }

    @Override
    public List<String> getWorkspaceList() {
        return workspaceList;
    }

    @Override
    public boolean deleteStyle(StyleWrapper styleToDelete) {
        return true;
    }

    @Override
    public String getDefaultWorkspaceName() {
        return DEFAULT_WORKSPACE_NAME;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public GeoServerConnection getConnection() {
        return connection;
    }

    @Override
    public void refreshWorkspace(String workspaceName) {}

    @Override
    public boolean deleteWorkspace(String workspaceName) {
        return false;
    }
}
