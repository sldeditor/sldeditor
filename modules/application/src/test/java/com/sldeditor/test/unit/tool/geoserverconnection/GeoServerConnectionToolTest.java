/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor.test.unit.tool.geoserverconnection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerNode;
import com.sldeditor.test.unit.datasource.impl.DummyInternalSLDFile;
import com.sldeditor.test.unit.datasource.impl.DummyInternalSLDFile2;
import com.sldeditor.tool.geoserverconnection.GeoServerConnectStateInterface;
import com.sldeditor.tool.geoserverconnection.GeoServerConnectionTool;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * The Class GeoServerConnectionToolTest, unit test for GeoServerConnectionTool
 *
 * @author Robert Ward (SCISYS)
 */
class GeoServerConnectionToolTest {

    class TestGeoServerConnectStateInterface implements GeoServerConnectStateInterface {

        public Map<GeoServerConnection, Boolean> connectionMap =
                new HashMap<GeoServerConnection, Boolean>();

        private boolean failConnection = false;

        /*
         * (non-Javadoc)
         *
         * @see
         * com.sldeditor.tool.geoserverconnection.GeoServerConnectStateInterface#isConnected(com.
         * sldeditor.common.data.GeoServerConnection)
         */
        @Override
        public boolean isConnected(GeoServerConnection connection) {
            if (connectionMap.containsKey(connection)) {
                return connectionMap.get(connection);
            }
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * com.sldeditor.tool.geoserverconnection.GeoServerConnectStateInterface#connect(java.util.
         * List)
         */
        @Override
        public void connect(List<GeoServerConnection> connectionList) {
            connectionMap.clear();

            for (GeoServerConnection connection : connectionList) {

                boolean state = true;

                if (failConnection) {
                    state = false;
                    failConnection = false;
                }
                connectionMap.put(connection, state);
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * com.sldeditor.tool.geoserverconnection.GeoServerConnectStateInterface#disconnect(java.
         * util.List)
         */
        @Override
        public void disconnect(List<GeoServerConnection> connectionList) {
            for (GeoServerConnection connection : connectionList) {
                connectionMap.put(connection, false);
            }
        }

        /** Fail connection. */
        public void failConnection() {
            failConnection = true;
        }
    }

    class TestGeoServerConnectionTool extends GeoServerConnectionTool {

        /** @param geoServerConnectState */
        public TestGeoServerConnectionTool(GeoServerConnectStateInterface geoServerConnectState) {
            super(geoServerConnectState);
        }

        public boolean isConnectButtonEnabled() {
            return connectButton.isEnabled();
        }

        public boolean isDisconnectButtonEnabled() {
            return disconnectButton.isEnabled();
        }

        /* (non-Javadoc)
         * @see com.sldeditor.tool.geoserverconnection.GeoServerConnectionTool#connectButtonPressed()
         */
        @Override
        protected void connectButtonPressed() {
            super.connectButtonPressed();
        }

        /* (non-Javadoc)
         * @see com.sldeditor.tool.geoserverconnection.GeoServerConnectionTool#disconnectButtonPressed()
         */
        @Override
        protected void disconnectButtonPressed() {
            super.disconnectButtonPressed();
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.geoserverconnection.GeoServerConnectionTool#getPanel()}.
     */
    @Test
    void testGetPanel() {
        TestGeoServerConnectionTool testObj = new TestGeoServerConnectionTool(null);
        assertNotNull(testObj.getPanel());
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.geoserverconnection.GeoServerConnectionTool#setSelectedItems(java.util.List,
     * java.util.List)}.
     */
    @Test
    void testSetSelectedItems() {
        TestGeoServerConnectStateInterface receiver = new TestGeoServerConnectStateInterface();
        TestGeoServerConnectionTool testObj = new TestGeoServerConnectionTool(receiver);

        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        DummyInternalSLDFile sld1 = new DummyInternalSLDFile();
        sldDataList.add(sld1.getSLDData());

        DummyInternalSLDFile2 sld2 = new DummyInternalSLDFile2();
        sldDataList.add(sld2.getSLDData());

        GeoServerConnection expectedConnection = new GeoServerConnection();
        expectedConnection.setConnectionName("test");

        GeoServerNode geoserverNode = new GeoServerNode(null, expectedConnection);

        nodeTypeList.add(geoserverNode);

        GeoServerConnection expectedConnection2 = new GeoServerConnection();
        expectedConnection2.setConnectionName("test");

        GeoServerNode geoserverNode2 = new GeoServerNode(null, expectedConnection2);
        nodeTypeList.add(geoserverNode2);

        testObj.setSelectedItems(nodeTypeList, sldDataList);
        assertTrue(testObj.isConnectButtonEnabled());
        assertFalse(testObj.isDisconnectButtonEnabled());

        // Press Connect button
        testObj.connectButtonPressed();
        assertFalse(testObj.isConnectButtonEnabled());
        assertFalse(testObj.isDisconnectButtonEnabled());

        testObj.populateComplete(null);

        assertFalse(testObj.isConnectButtonEnabled());
        assertTrue(testObj.isDisconnectButtonEnabled());

        // Press Disconnect button
        testObj.disconnectButtonPressed();

        assertFalse(testObj.isConnectButtonEnabled());
        assertFalse(testObj.isDisconnectButtonEnabled());

        testObj.populateComplete(null);

        assertTrue(testObj.isConnectButtonEnabled());
        assertFalse(testObj.isDisconnectButtonEnabled());

        // Connect - fail connection
        receiver.failConnection();
        testObj.connectButtonPressed();
        assertTrue(testObj.isConnectButtonEnabled());
        assertFalse(testObj.isDisconnectButtonEnabled());

        testObj.populateComplete(null);

        assertTrue(testObj.isConnectButtonEnabled());
        assertTrue(testObj.isDisconnectButtonEnabled());
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.geoserverconnection.GeoServerConnectionTool#getToolName()}.
     */
    @Test
    void testGetToolName() {
        TestGeoServerConnectionTool testObj = new TestGeoServerConnectionTool(null);
        assertNotNull(testObj.getToolName());
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.geoserverconnection.GeoServerConnectionTool#supports(java.util.List,
     * java.util.List, java.util.List)}.
     */
    @Test
    void testSupports() {
        TestGeoServerConnectionTool testObj = new TestGeoServerConnectionTool(null);

        List<Class<?>> uniqueNodeTypeList = new ArrayList<Class<?>>();
        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        assertFalse(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

        uniqueNodeTypeList.add(String.class);

        assertTrue(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));
    }
}
