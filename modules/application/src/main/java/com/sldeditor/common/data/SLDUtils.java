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
package com.sldeditor.common.data;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.DefaultResourceLocator;
import org.geotools.styling.SLDParser;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;

/**
 * The Class SLDUtils, contains utility methods to populate StyledLayerDescriptor
 * objects from a string or a file.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDUtils {

    /** The Constant STYLES_PATH. */
    private static final String STYLES_PATH = "styles/";

    /**
     * Creates a StyledLayerDescriptor object containing a SLD from a string.
     *
     * @param sldData the sld data
     * @return the styled layer descriptor
     */
    public static StyledLayerDescriptor createSLDFromString(SLDDataInterface sldData)
    {
        if((sldData == null) || (sldData.getSld() == null))
        {
            return null;
        }

        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

        InputStream stream = new ByteArrayInputStream(sldData.getSld().getBytes());

        SLDParser styleReader = new SLDParser(styleFactory, stream);

        URL resourceLocator = getResourceLocator(sldData);

        sldData.setResourceLocator(resourceLocator);
        setResourcelocator(styleReader, resourceLocator);
        StyledLayerDescriptor sld = null;

        try
        {
            sld = styleReader.parseSLD();
        }
        catch(RuntimeException e)
        {
            ConsoleManager.getInstance().error(SLDUtils.class, e.getMessage());
        }

        return sld;
    }

    /**
     * Gets the resource locator.
     *
     * @param sldData the sld data
     * @return the resource locator
     */
    public static URL getResourceLocator(SLDDataInterface sldData) {
        GeoServerConnection geoServer = sldData.getConnectionData();

        URL resourceLocator = null;

        if(geoServer != null)
        {
            try {
                resourceLocator = DataUtilities.extendURL(geoServer.getUrl(), STYLES_PATH);
            } catch (MalformedURLException e) {
                ConsoleManager.getInstance().exception(SLDUtils.class, e);
            }
        }
        else
        {
            try {
                File sldFile = sldData.getSLDFile();
                if(sldFile != null)
                {
                    resourceLocator = sldFile.getParentFile().toURI().toURL();
                }
            } catch (MalformedURLException e) {
                ConsoleManager.getInstance().exception(SLDUtils.class, e);
            }
        }
        return resourceLocator;
    }

    /**
     * Sets the resource locator so that relative external graphic files can be found.
     *
     * @param styleReader the new resource locator
     * @param url the url
     */
    public static void setResourcelocator(SLDParser styleReader, URL url)
    {
        DefaultResourceLocator resourceLocator = new DefaultResourceLocator();

        resourceLocator.setSourceUrl(url);
        styleReader.setOnLineResourceLocator(resourceLocator);
    }

    /**
     * Creates a StyledLayerDescriptor object containing a SLD by 
     * reading the contents of a file
     *
     * @param file the file
     * @return the styled layer descriptor
     */
    public static StyledLayerDescriptor readSLDFile(File file) {
        StyledLayerDescriptor sld = null;

        if(file != null)
        {
            StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
            try
            {
                // By using URL here allows external graphics to loaded properly
                URL url = file.toURI().toURL();
                SLDParser styleReader = new SLDParser(styleFactory, url);
                setResourcelocator(styleReader, file.toURI().toURL());
                sld = styleReader.parseSLD();
            }
            catch (MalformedURLException e)
            {
                ConsoleManager.getInstance().exception(SLDUtils.class, e);
            }
            catch (IOException e)
            {
                ConsoleManager.getInstance().exception(SLDUtils.class, e);
            }
        }
        return sld;
    }
}
