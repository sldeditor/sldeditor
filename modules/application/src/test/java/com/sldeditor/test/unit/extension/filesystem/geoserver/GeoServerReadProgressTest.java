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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.data.GeoServerLayer;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerOverallNode;
import com.sldeditor.extension.filesystem.geoserver.GeoServerParseCompleteInterface;
import com.sldeditor.extension.filesystem.geoserver.GeoServerReadProgress;

/**
 * Unit test for GeoServerReadProgress class.
 * 
 * <p>{@link com.sldeditor.extension.filesystem.geoserver.GeoServerReadProgress}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class GeoServerReadProgressTest {

    class DummyProgressComplete implements GeoServerParseCompleteInterface
    {
        public boolean completed = false;

        @Override
        public void populateComplete(GeoServerConnection connection,
                Map<String, List<StyleWrapper>> styleMap,
                Map<String, List<GeoServerLayer>> layerMap) {
            completed = true;
        }

    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.GeoServerReadProgress#GeoServerReadProgress(com.sldeditor.common.filesystem.FileSystemInterface, com.sldeditor.extension.filesystem.geoserver.GeoServerParseCompleteInterface)}.
     */
    @Test
    public void testGeoServerReadProgress() {
        DummyProgressComplete complete = new DummyProgressComplete();

        GeoServerReadProgress progress = new GeoServerReadProgress(null, complete);

        GeoServerOverallNode geoServerRootNode = new GeoServerOverallNode(null);
        GeoServerConnection connection = new GeoServerConnection();
        connection.setConnectionName("test connection 1");

        GeoServerNode node = new GeoServerNode(null, connection);
        geoServerRootNode.add(node);

        progress.addNewConnectionNode(connection, node);

        progress.startPopulating(null);

        progress.startPopulating(connection);
        progress.readLayersProgress(null, 0, 0);
        assertFalse(complete.completed);
        progress.readLayersProgress(connection, 0, 5);
        progress.readLayersProgress(connection, 1, 5);
        progress.readLayersProgress(connection, 2, 5);
        progress.readLayersProgress(connection, 3, 5);
        progress.readLayersProgress(connection, 4, 5);
        progress.readLayersProgress(connection, 5, 5);
        assertFalse(complete.completed);
        progress.readLayersProgress(connection, 6, 5);
        assertFalse(complete.completed);

        progress.readStylesProgress(null, 0, 0);
        progress.readStylesProgress(connection, 0, 3);
        progress.readStylesProgress(connection, 1, 3);
        progress.readStylesProgress(connection, 2, 3);
        progress.readStylesProgress(connection, 3, 3);
        progress.readStylesProgress(connection, 4, 3);
        assertFalse(complete.completed);

        Map<String, List<GeoServerLayer>> completedLayersMap =
                new HashMap<String, List<GeoServerLayer>>();
        Map<String, List<StyleWrapper>> completedStyleMap =
                new HashMap<String, List<StyleWrapper>>();

        progress.readLayersComplete(connection, completedLayersMap);
        progress.readStylesComplete(connection, completedStyleMap, false);

        assertTrue(complete.completed);
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.GeoServerReadProgress#removeNode(com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerNode, java.lang.String)}.
     */
    @Test
    public void testRemoveNode() {
        GeoServerOverallNode geoServerRootNode = new GeoServerOverallNode(null);
        GeoServerConnection connection = new GeoServerConnection();
        connection.setConnectionName("test connection 1");

        GeoServerNode node = new GeoServerNode(null, connection);
        geoServerRootNode.add(node);

        GeoServerReadProgress.removeNode(null, null);
        GeoServerReadProgress.removeNode(node, "test title");
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.GeoServerReadProgress#disconnect(com.sldeditor.common.data.GeoServerConnection)}.
     */
    @Test
    public void testDisconnect() {
        GeoServerReadProgress progress = new GeoServerReadProgress(null, null);
        GeoServerOverallNode geoServerRootNode = new GeoServerOverallNode(null);
        GeoServerConnection connection = new GeoServerConnection();
        connection.setConnectionName("test connection 1");

        GeoServerNode node = new GeoServerNode(null, connection);
        geoServerRootNode.add(node);

        progress.addNewConnectionNode(connection, node);

        progress.disconnect(null);
        progress.disconnect(connection);
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.GeoServerReadProgress#deleteConnection(com.sldeditor.common.data.GeoServerConnection)}.
     */
    @Test
    public void testDeleteConnection() {
        GeoServerReadProgress progress = new GeoServerReadProgress(null, null);
        GeoServerOverallNode geoServerRootNode = new GeoServerOverallNode(null);
        GeoServerConnection connection = new GeoServerConnection();
        connection.setConnectionName("test connection 1");

        GeoServerNode node = new GeoServerNode(null, connection);
        geoServerRootNode.add(node);

        progress.addNewConnectionNode(connection, node);

        progress.deleteConnection(null);
        progress.deleteConnection(connection);
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.GeoServerReadProgress#updateConnection(com.sldeditor.common.data.GeoServerConnection, com.sldeditor.common.data.GeoServerConnection)}.
     */
    @Test
    public void testUpdateConnection() {

        GeoServerReadProgress progress = new GeoServerReadProgress(null, null);

        GeoServerOverallNode geoServerRootNode = new GeoServerOverallNode(null);
        GeoServerConnection connection = new GeoServerConnection();
        connection.setConnectionName("test connection 1");

        GeoServerNode node = new GeoServerNode(null, connection);
        geoServerRootNode.add(node);

        progress.addNewConnectionNode(connection, node);

        progress.updateConnection(null, null);

        GeoServerConnection newConnectionDetails = new GeoServerConnection();
        newConnectionDetails.setConnectionName("updated test connection 1");
        progress.updateConnection(connection, newConnectionDetails);
    }

}
