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
package com.sldeditor.test.unit.extension.filesystem.file.esri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.xml.ParseXML;
import com.sldeditor.extension.filesystem.file.esri.MXDInfo;
import com.sldeditor.extension.filesystem.file.esri.MXDParser;

/**
 * Unit test for MXDParser class.
 * <p>{@link com.sldeditor.extension.filesystem.file.esri.MXDParser}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class MXDParserTest {

    private static final String EXPECTED_LAYER = "TEST_LAYER";
    private static final String MXD_JSON = "/mxd.json";

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDParser#readLayers(java.lang.String)}.
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDParser#readLayer(com.sldeditor.extension.filesystem.file.esri.MXDInfo, java.lang.String)}.
     */
    @Test
    public void testReadLayers() {
        assertNull(MXDParser.readLayers(null));

        String filename = "filename.json";
        assertNull(MXDParser.readLayers(filename));

        File f = openTestFile();

        MXDInfo mxdInfo = MXDParser.readLayers(f.getAbsolutePath());

        assertTrue(mxdInfo != null);

        assertEquals(f.getAbsolutePath(), mxdInfo.getIntermediateFile().getAbsolutePath());

        assertNull(MXDParser.readLayer(null, null));
        assertNull(MXDParser.readLayer(filename, "Invalid layer"));

        SLDDataInterface sldData = MXDParser.readLayer("different filename", EXPECTED_LAYER);
        assertNull(sldData);

        sldData = MXDParser.readLayer(f.getAbsolutePath(), EXPECTED_LAYER);

        assertEquals(EXPECTED_LAYER, sldData.getLayerName());
    }

    /**
     * Open test file.
     *
     * @return the file
     */
    private File openTestFile() {
        InputStream inputStream = ParseXML.class.getResourceAsStream(MXD_JSON); 
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String read;

        try {
            while((read = br.readLine()) != null) {
                sb.append(read);
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        File tmpFile = null;
        try {
            tmpFile = File.createTempFile("test", ".json");
            tmpFile.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter(new FileWriter(tmpFile));
            writer.write(sb.toString());
        }
        catch ( IOException e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
        finally
        {
            try
            {
                if (writer != null)
                {
                    writer.close( );
                }
            }
            catch ( IOException e)
            {
                e.printStackTrace();
                fail(e.getMessage());
            }
        }
        return tmpFile;
    }
}
