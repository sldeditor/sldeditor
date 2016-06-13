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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.junit.Test;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.xml.ParseXML;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.extension.filesystem.file.FileSystemInput;
import com.sldeditor.extension.filesystem.file.esri.MXDFileHandler;
import com.sldeditor.extension.filesystem.file.esri.MXDLayerNode;
import com.sldeditor.extension.filesystem.file.esri.MXDNode;

/**
 * Unit test for MXDFileHandler class.
 * <p>{@link com.sldeditor.extension.filesystem.file.esri.MXDFileHandler}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class MXDFileHandlerTest {

    private static final String MXD_JSON = "/mxd.json";

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDFileHandler#getFileExtensionList()}.
     */
    @Test
    public void testGetFileExtension() {
        MXDFileHandler handler = new MXDFileHandler();

        assertEquals(Arrays.asList("json"), handler.getFileExtensionList());
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDFileHandler#populate(com.sldeditor.common.filesystem.FileSystemInterface, javax.swing.tree.DefaultTreeModel, com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode)}.
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDFileHandler#getSLDContents(com.sldeditor.common.NodeInterface)}.
     */
    @Test
    public void testPopulate() {
        MXDFileHandler handler = new MXDFileHandler();

        assertFalse(handler.populate(null, null, null));

        FileSystemInput input = new FileSystemInput(null);

        File tmpFile = openTestFile();

        FileTreeNode fileTreeNode = null;
        try {
            fileTreeNode = new FileTreeNode(tmpFile.getParentFile(), tmpFile.getName());
        } catch (SecurityException | FileNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(new DefaultMutableTreeNode("Root"));

        assertTrue(handler.populate(input, treeModel, fileTreeNode));

        assertEquals(1, fileTreeNode.getChildCount());

        // Try the wrong node
        MXDNode mxdInfoNode = (MXDNode) fileTreeNode.getChildAt(0);
        List<SLDDataInterface> sldList = handler.getSLDContents(mxdInfoNode);
        assertEquals(1, sldList.size());

        // Try the correct node
        MXDLayerNode layerNode = (MXDLayerNode) mxdInfoNode.getChildAt(0);
        sldList = handler.getSLDContents(layerNode);
        assertEquals(1, sldList.size());
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

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDFileHandler#open(java.io.File)}.
     */
    @Test
    public void testOpen() {
        MXDFileHandler handler = new MXDFileHandler();

        assertNull(handler.open(null));
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDFileHandler#save(com.sldeditor.common.SLDDataInterface)}.
     */
    @Test
    public void testSave() {
        MXDFileHandler handler = new MXDFileHandler();

        assertFalse(handler.save(null));
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDFileHandler#getSLDName(com.sldeditor.common.SLDDataInterface)}.
     */
    @Test
    public void testGetSLDName() {
        MXDFileHandler handler = new MXDFileHandler();

        assertNull(handler.getSLDName(null));

        StyleWrapper styleWrapper = new StyleWrapper();
        String expectedStyleName = "teststyle";
        styleWrapper.setStyle(expectedStyleName);
        SLDData sldData = new SLDData(styleWrapper, "");
        String actualSLDName = handler.getSLDName(sldData);
        assertEquals(expectedStyleName + ".sld", actualSLDName);
    }

}
