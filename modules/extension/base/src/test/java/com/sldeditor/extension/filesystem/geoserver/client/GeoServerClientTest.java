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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.data.GeoServerLayer;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.extension.filesystem.geoserver.GeoServerReadProgressInterface;

/**
 * Unit test for GeoServerClient class.
 * <p>{@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class GeoServerClientTest {

    private static final String TEST_GEOSERVER_INSTANCE = "http://localhost:8080/geoserver";
    private static final String TEST_GEOSERVER_USERNAME = "admin";
    private static final String TEST_GEOSERVER_PASSWORD = "geoserver";

    private static GeoServerConnection testConnection = null;

    class TestProgressClass implements GeoServerReadProgressInterface
    {
        private boolean stylesComplete = false;
        private boolean layersComplete = false;
        public int styleTotal = 0;

        @Override
        public void startPopulating(GeoServerConnection connection) {
            System.out.println("Started retrieving data from GeoServer");
            stylesComplete = false;
            layersComplete = false;
        }

        @Override
        public void readStylesComplete(GeoServerConnection connection,
                Map<String, List<StyleWrapper>> styleMap, boolean partialRefresh) {
            System.out.println("All layers read");
            stylesComplete = true;
        }

        @Override
        public void readStylesProgress(GeoServerConnection connection, int count, int total) {
            System.out.println(String.format("Styles : %d/%d", count, total));
            styleTotal = total;
        }

        @Override
        public void readLayersComplete(GeoServerConnection connection,
                Map<String, List<GeoServerLayer>> layerMap) {

            System.out.println("All styles read");
            layersComplete = true;
        }

        @Override
        public void readLayersProgress(GeoServerConnection connection, int count, int total) {
            System.out.println(String.format("Layers : %d/%d", count, total));
        }

        public boolean hasFinished()
        {
            return stylesComplete && layersComplete;
        }
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testConnection = new GeoServerConnection();
        testConnection.setConnectionName("Test Connection");

        URL url = new URL(TEST_GEOSERVER_INSTANCE);
        testConnection.setUrl(url);

        testConnection.setUserName(TEST_GEOSERVER_USERNAME);
        testConnection.setPassword(TEST_GEOSERVER_PASSWORD);
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#GeoServerClient()}.
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#isConnected()}.
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#getConnection()}.
     */
    @Test
    public void testGeoServerClient() {
        GeoServerClient client = new GeoServerClient();

        assertFalse(client.connect());

        assertNull(client.getConnection());
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#initialise(com.sldeditor.extension.filesystem.geoserver.GeoServerReadProgressInterface, com.sldeditor.common.data.GeoServerConnection)}.
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#connect()}.
     */
    @Test
    public void testInitialiseWithInvalidConnection() {
        GeoServerClient client = new GeoServerClient();

        GeoServerConnection invalidTestConnection = new GeoServerConnection();
        invalidTestConnection.setConnectionName("Invalid Test Connection");
        try {
            invalidTestConnection.setUrl(new URL("http://invalid.url.com"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        client.initialise(null, invalidTestConnection);
        assertFalse(client.connect());

        assertEquals(invalidTestConnection, client.getConnection());
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#initialise(com.sldeditor.extension.filesystem.geoserver.GeoServerReadProgressInterface, com.sldeditor.common.data.GeoServerConnection)}.
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#retrieveData()}.
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#connect()}.
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#getStyle(com.sldeditor.common.data.StyleWrapper)}.
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#getStyle(com.sldeditor.common.data.StyleWrapper)}.
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#uploadSLD(com.sldeditor.common.data.StyleWrapper, java.lang.String)}.
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#getWorkspaceList()}.
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#refreshWorkspace(java.lang.String)}.
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#deleteStyle(com.sldeditor.common.data.StyleWrapper)}.
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#disconnect()}.
     */
    @Test
    public void testInitialiseWithValidConnection() {
        GeoServerClient client = new GeoServerClient();
        TestProgressClass progress = new TestProgressClass();

        List<StyleWrapper> stylesToDeleteList = new ArrayList<StyleWrapper>();

        client.initialise(progress, testConnection);
        assertTrue(client.connect());

        assertEquals(testConnection, client.getConnection());

        List<String> actualWorkspaceList = client.getWorkspaceList();
        assertTrue(actualWorkspaceList.size() > 0);

        client.retrieveData();

        int count = 0;
        while(!progress.hasFinished() && count < 50)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count ++;
        }

        assertTrue(progress.hasFinished());

        int actualNoOfStyles = progress.styleTotal;

        // Retrieve style
        StyleWrapper styleWrapper = new StyleWrapper(client.getDefaultWorkspaceName(), "point");
        String sldContents = client.getStyle(styleWrapper);

        assertTrue(sldContents != null);

        SLDData sldData = new SLDData(styleWrapper, sldContents);

        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);
        assertEquals("default_point", sld.layers().get(0).getName());

        // Upload as new style
        String timeString = "" + System.currentTimeMillis();

        String styleName = "New_Test_style_" + timeString;
        styleWrapper.setStyle(styleName);
        assertTrue(client.uploadSLD(styleWrapper, sldContents));
        stylesToDeleteList.add(styleWrapper.clone());

        // Replace the existing style
        StyledLayer styledLayer = sld.layers().get(0);

        styledLayer.setName("test point sld");

        SLDWriterInterface sldWriter = SLDWriterFactory.createSLDWriter(null);
        String updatedSLDBody = sldWriter.encodeSLD(sld);
        assertTrue(client.uploadSLD(styleWrapper, updatedSLDBody));

        // Change the workspace name - invalid
        styleWrapper.setWorkspace("Test workspace");
        assertFalse(client.uploadSLD(styleWrapper, updatedSLDBody));

        // Change the workspace name - valid
        String expectedWorkspace = "Test_workspace_" + timeString;
        styleWrapper.setWorkspace(expectedWorkspace);
        assertTrue(client.uploadSLD(styleWrapper, updatedSLDBody));
        stylesToDeleteList.add(styleWrapper.clone());

        actualWorkspaceList = client.getWorkspaceList();
        assertTrue(actualWorkspaceList.contains(expectedWorkspace));

        // Update the contents
        styledLayer.setName("updated test point sld");

        updatedSLDBody = sldWriter.encodeSLD(sld);
        assertTrue(client.uploadSLD(styleWrapper, updatedSLDBody));

        // Check refresh workspace works
        client.refreshWorkspace(client.getDefaultWorkspaceName());

        count = 0;
        while(!progress.hasFinished() && count < 50)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count ++;
        }

        assertTrue(progress.hasFinished());

        assertEquals(actualNoOfStyles + 1, progress.styleTotal);

        //
        // Tidy up - delete all styles created
        //
        for(StyleWrapper styleToDelete : stylesToDeleteList)
        {
            System.out.println("Deleting style : " + styleToDelete.getWorkspace() + "/" + styleToDelete.getStyle());
            assertTrue(client.deleteStyle(styleToDelete));
        }
        assertFalse(client.deleteStyle(null));
        assertFalse(client.deleteStyle(new StyleWrapper()));

        StyleWrapper tmpStyle = styleWrapper.clone();
        tmpStyle.setStyle("invalid");
        assertFalse(client.deleteStyle(tmpStyle));

        tmpStyle = styleWrapper.clone();
        tmpStyle.setWorkspace("invalid");
        assertFalse(client.deleteStyle(tmpStyle));

        System.out.println("Deleting workspace : " + expectedWorkspace);

        assertTrue(client.deleteWorkspace(expectedWorkspace));

        client.disconnect();
        assertFalse(client.isConnected());
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#isDefaultWorkspace(java.lang.String)}.
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#getDefaultWorkspaceName()}.
     */
    @Test
    public void testIsDefaultWorkspace() {

        String workspaceName = "test workspace";

        GeoServerClient client = new GeoServerClient();

        assertFalse(client.isDefaultWorkspace(workspaceName));

        assertTrue(client.isDefaultWorkspace(client.getDefaultWorkspaceName()));
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient#updateLayerStyles(com.sldeditor.common.data.GeoServerLayer, com.sldeditor.common.data.StyleWrapper)}.
     */
    @Test
    public void testUpdateLayerStyles() {
        GeoServerClient client = new GeoServerClient();
        assertFalse(client.updateLayerStyles(null));
    }

}
