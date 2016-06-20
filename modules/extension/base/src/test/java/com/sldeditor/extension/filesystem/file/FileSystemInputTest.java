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
package com.sldeditor.extension.filesystem.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.junit.Test;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.datasource.extension.filesystem.node.FSTree;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.extension.filesystem.file.sld.SLDFileHandlerTest;

/**
 * Unit test for FileSystemInput class.
 * <p>{@link com.sldeditor.extension.filesystem.file.FileSystemInput#FileSystemInput}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class FileSystemInputTest {

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.FileSystemInput#FileSystemInput(com.sldeditor.common.ToolSelectionInterface)}.
     */
    @Test
    public void testFileSystemInput() {
        FileSystemInput input = new FileSystemInput(null);

        FSTree tree = new FSTree();

        DefaultMutableTreeNode rootNode;
        try
        {
            rootNode = new DefaultMutableTreeNode("Root");

            DefaultTreeModel model = new DefaultTreeModel(rootNode);

            input.populate(tree, model, rootNode);

            URL url = SLDFileHandlerTest.class.getResource("/sld/point_attribute.sld");

            List<SLDDataInterface> sldDataList = input.open(url);

            assertEquals(1, sldDataList.size());

            File parent = null;
            try {
                parent = new File(url.toURI());
                parent = parent.getParentFile();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }

            FileTreeNode fileTreeNode = new FileTreeNode(parent, "point_attribute.sld");

            List<SLDDataInterface> sldDataContentsList = input.getSLDContents(fileTreeNode).getSldData();
            assertEquals(1, sldDataContentsList.size());

            // Changes where the file is to be saved to
            File saveFile = File.createTempFile("test", ".sld");
            saveFile.deleteOnExit();

            SLDData sldData = (SLDData) sldDataContentsList.get(0);

            sldData.setSLDFile(saveFile);

            assertFalse(input.save(null));
            assertTrue(input.save(sldData));
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.FileSystemInput#treeExpanded(java.lang.Object)}.
     */
    @Test
    public void testTreeExpanded() {
        // Hard to test
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.FileSystemInput#rightMouseButton(java.lang.Object, java.awt.event.MouseEvent)}.
     */
    @Test
    public void testRightMouseButton() {
        // Hard to test
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.FileSystemInput#getNodeTypes()}.
     */
    @Test
    public void testGetNodeTypes() {
        FileSystemInput input = new FileSystemInput(null);

        assertTrue(input.getNodeTypes().isEmpty());
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.FileSystemInput#copyNodes(com.sldeditor.common.NodeInterface, java.util.Map)}.
     */
    @Test
    public void testCopyNodes() {
        FileSystemInput input = new FileSystemInput(null);

        URL url = SLDFileHandlerTest.class.getResource("/sld/point_attribute.sld");

        List<SLDDataInterface> sldDataList = input.open(url);

        assertEquals(1, sldDataList.size());
        
        try {
            Path tempFolder = Files.createTempDirectory("test");
            
            File tempFolderFile = tempFolder.toFile();
            FileTreeNode destinationTreeNode = new FileTreeNode(tempFolderFile.getParentFile(), tempFolderFile.getName());

            Map<NodeInterface, List<SLDDataInterface>> copyDataMap = new HashMap<NodeInterface, List<SLDDataInterface>>();
            
            copyDataMap.put(destinationTreeNode, sldDataList);
            assertTrue(input.copyNodes(destinationTreeNode, copyDataMap));
            
            SLDData sldData = (SLDData) sldDataList.get(0);
            sldData.setSLDFile(new File(tempFolderFile, "point_attribute.sld"));
            
            input.deleteNodes(destinationTreeNode, sldDataList);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
