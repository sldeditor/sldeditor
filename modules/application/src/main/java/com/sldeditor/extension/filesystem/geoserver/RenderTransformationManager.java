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
package com.sldeditor.extension.filesystem.geoserver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.sldeditor.common.DataTypeEnum;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.extension.filesystem.geoserver.client.GeoServerWPSClient;
import com.sldeditor.extension.filesystem.geoserver.client.GeoServerWPSClientInterface;

import net.opengis.wps10.ProcessBriefType;

/**
 * The Class RenderTransformationManager.
 *
 * @author Robert Ward (SCISYS)
 */
public class RenderTransformationManager {

    /** The singleton instance. */
    private static RenderTransformationManager instance = null;

    /**
     * Gets the singleton instance of RenderTransformationManager.
     *
     * @return single instance of RenderTransformationManager
     */
    public static RenderTransformationManager getInstance()
    {
        if(instance == null)
        {
            instance = new RenderTransformationManager();
        }

        return instance;
    }

    /**
     * Gets the render transform.
     *
     * @param connection the connection
     * @return the render transform
     */
    public List<ProcessBriefType> getRenderTransform(GeoServerConnection connection)
    {
        GeoServerWPSClientInterface client = new GeoServerWPSClient(connection);

        client.getCapabilities();

        List<ProcessBriefType> functionList = client.getRenderTransformations(DataTypeEnum.E_VECTOR);

        functionList = client.getRenderTransformations(DataTypeEnum.E_RASTER);

        return functionList;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        GeoServerConnection connection = new GeoServerConnection();
        connection.setConnectionName("Test");
        try {
            connection.setUrl(new URL("http://localhost/geoserver"));
            connection.setUserName("admin");
            connection.setPassword("geoserver");

            RenderTransformationManager.getInstance().getRenderTransform(connection);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
