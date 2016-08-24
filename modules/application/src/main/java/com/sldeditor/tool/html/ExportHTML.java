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
package com.sldeditor.tool.html;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.geotools.styling.StyledLayerDescriptor;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.tool.legendpanel.LegendManager;

/**
 * Exports a list of SLD object to html.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ExportHTML
{
    
    /** The Constant HTML_TEMPLATE. */
    private static final String HTML_TEMPLATE = "/html/exportedmxd.html";
    
    /** The Constant TEMPLATE_INSERT_CODE. */
    private static final String TEMPLATE_INSERT_CODE = "<!-- SLD Editor insert -->";

    private static final String PREFIX = "extracted";
    private static final String SUFFIX = ".html";

    /**
     * Save all html to folder.
     *
     * @param destinationFolder the destination folder
     * @param filename the filename
     * @param sldDataList the sld data list
     * @param backgroundColour the background colour
     */
    public static void save(File destinationFolder,
        String filename,
        List<SLDDataInterface> sldDataList, 
        Color backgroundColour)
    {
        if(!destinationFolder.exists())
        {
            destinationFolder.mkdirs();
        }

        InputStream inputStream = ExportHTML.class.getResourceAsStream(HTML_TEMPLATE);

        if(inputStream == null)
        {
            ConsoleManager.getInstance().error(ExportHTML.class, "Failed to find html template");
        }
        else
        {
            String htmlTemplate = null;
            BufferedReader reader = null;
            try
            {
                File file = stream2file(inputStream);

                reader = new BufferedReader(new FileReader(file));
                String         line = null;
                StringBuilder  stringBuilder = new StringBuilder();
                String         ls = System.getProperty("line.separator");

                while( ( line = reader.readLine() ) != null ) {
                    stringBuilder.append( line );
                    stringBuilder.append( ls );
                }
                htmlTemplate = stringBuilder.toString();
            }
            catch(Exception e)
            {
                ConsoleManager.getInstance().exception(ExportHTML.class, e);
            }
            finally
            {
                if(reader != null)
                {
                    try
                    {
                        reader.close();
                    }
                    catch (IOException e)
                    {
                        ConsoleManager.getInstance().exception(ExportHTML.class, e);
                    }
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append("  <tr>\n");
            sb.append("    <th>Layer Name</th>\n");
            sb.append("    <th>Legend</th>\n");
            sb.append("  </tr>\n");

            for(SLDDataInterface sldData : sldDataList)
            {
                StyleWrapper styleWrapper = sldData.getStyle();
                
                String layerName = styleWrapper.getStyle();
                sb.append("  <tr>\n");

                sb.append(String.format("    <td>%s</td>\n", layerName));

                StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);

                if(sld != null)
                {
                    String showHeading = null;
                    String showFilename = null;

                    List<String> legendFileNameList = new ArrayList<String>();
                    
                    boolean result = LegendManager.getInstance().saveLegendImage(sld, destinationFolder, layerName, showHeading, showFilename, legendFileNameList);

                    if(result)
                    {
                        String legendFilename = legendFileNameList.get(0);
                        sb.append(String.format("    <td><img src=\"%s\" alt=\"%s\" ></td>\n", legendFilename, layerName));
                    }
                }
                sb.append("  </tr>\n");
            }
            
            if(htmlTemplate != null)
            {
                htmlTemplate = htmlTemplate.replace(TEMPLATE_INSERT_CODE, sb.toString());
                
                PrintWriter out;
                try
                {
                    File f = new File(destinationFolder, filename);
                    out = new PrintWriter(f);
                    out.println(htmlTemplate);
                    out.close();
                }
                catch (FileNotFoundException e)
                {
                    ConsoleManager.getInstance().exception(ExportHTML.class, e);
                }
            }
        }
    }
    
    /**
     * Writes an InputStream to a temporary file.
     *
     * @param in the in
     * @return the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static File stream2file (InputStream in) throws IOException {
        final File tempFile = File.createTempFile(PREFIX, SUFFIX);
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }

}
