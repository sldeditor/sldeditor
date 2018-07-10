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

package com.sldeditor.test.unit.extension.filesystem.geoserver.client;

import static org.junit.Assert.assertTrue;

import com.sldeditor.common.DataTypeEnum;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.extension.filesystem.geoserver.client.GeoServerWPSClient;
import java.net.URL;
import java.util.List;
import net.opengis.wps10.ProcessBriefType;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for GeoServerWPSClient class.
 *
 * <p>{@link com.sldeditor.extension.filesystem.geoserver.client.GeoServerWPSClient}
 *
 * @author Robert Ward (SCISYS)
 */
public class GeoServerWPSClientTest {

    private static final String TEST_GEOSERVER_INSTANCE = "http://localhost:8081/geoserver";

    private static final String TEST_GEOSERVER_USERNAME = "admin";

    private static final String TEST_GEOSERVER_PASSWORD = "geoserver";

    private static GeoServerConnection testConnection = null;

    /**
     * Sets the up before class.
     *
     * @throws Exception the exception
     */
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
     * Test method for {@link
     * com.sldeditor.extension.filesystem.geoserver.client.GeoServerWPSClient#GeoServerWPSClient(com.sldeditor.common.data.GeoServerConnection)}.
     */
    @Test
    public void testGeoServerWPSClient() {
        GeoServerWPSClient wpsClient = new GeoServerWPSClient(testConnection);

        boolean result = wpsClient.getCapabilities();

        if (!result) {
            System.out.println("No GeoServer running");
        } else {
            DataTypeEnum typeOfData = DataTypeEnum.E_VECTOR;
            List<ProcessBriefType> vectorTransformList =
                    wpsClient.getRenderTransformations(typeOfData);

            assertTrue(!vectorTransformList.isEmpty());

            typeOfData = DataTypeEnum.E_RASTER;
            List<ProcessBriefType> rasterTransformList =
                    wpsClient.getRenderTransformations(typeOfData);

            assertTrue(!rasterTransformList.isEmpty());
        }
    }
}
