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

package com.sldeditor.test.unit.datasource.extension.filesystem.node;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.datasource.extension.filesystem.node.FSTree;
import com.sldeditor.datasource.extension.filesystem.node.FileSystemNodeManager;
import com.sldeditor.datasource.extension.filesystem.node.file.FileHandlerInterface;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.extension.filesystem.file.FileSystemInput;
import com.sldeditor.extension.filesystem.file.raster.RasterFileHandler;
import com.sldeditor.extension.filesystem.file.sld.SLDFileHandler;
import com.sldeditor.extension.filesystem.file.sldeditor.SLDEditorFileHandler;
import com.sldeditor.extension.filesystem.file.vector.VectorFileHandler;
import com.sldeditor.extension.filesystem.file.ysld.YSLDFileHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.junit.jupiter.api.Test;

/**
 * Unit test for FileSystemNodeManager.
 *
 * <p>{@link com.sldeditor.datasource.extension.filesystem.node.FileSystemNodeManager}
 *
 * @author Robert Ward (SCISYS)
 */
public class FileSystemNodeManagerTest {

    /**
     * Test method for {@link
     * com.sldeditor.datasource.extension.filesystem.node.FileSystemNodeManager#create(com.sldeditor.datasource.extension.filesystem.node.FSTree,
     * javax.swing.tree.DefaultTreeModel)}.
     */
    @Test
    public void testCreate() {}

    /**
     * Test method for {@link
     * com.sldeditor.datasource.extension.filesystem.node.FileSystemNodeManager#showNodeInTree(java.net.URL,
     * boolean)}.
     */
    @Test
    public void testShowNodeInTree() {}

    /**
     * Test method for {@link
     * com.sldeditor.datasource.extension.filesystem.node.FileSystemNodeManager#getNode(java.io.File)}.
     */
    @Test
    public void testGetNode() {
        FSTree tree = new FSTree();

        DefaultMutableTreeNode rootNode;

        rootNode = new DefaultMutableTreeNode("Root");

        DefaultTreeModel model = new DefaultTreeModel(rootNode);

        FileSystemNodeManager.create(tree, model);

        // Set up file handlers
        Map<String, FileHandlerInterface> fileHandlerMap =
                new LinkedHashMap<String, FileHandlerInterface>();

        List<String> fileHandlerClassList = new ArrayList<String>();
        fileHandlerClassList.add(SLDFileHandler.class.getName());
        fileHandlerClassList.add(SLDEditorFileHandler.class.getName());
        fileHandlerClassList.add(RasterFileHandler.class.getName());
        fileHandlerClassList.add(VectorFileHandler.class.getName());
        fileHandlerClassList.add(YSLDFileHandler.class.getName());

        for (String fileHandlerClass : fileHandlerClassList) {
            try {
                FileHandlerInterface fileHandler =
                        (FileHandlerInterface) Class.forName(fileHandlerClass).newInstance();
                for (String fileExtension : fileHandler.getFileExtensionList()) {
                    fileHandlerMap.put(fileExtension, fileHandler);
                }
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                ConsoleManager.getInstance().exception(FileSystemInput.class, e);
            }
        }

        FileTreeNode.setFileHandlerMap(fileHandlerMap);

        // Populate file system node tree
        try {
            File tempFile = File.createTempFile("aaatest", ".shp");
            File tempDir = tempFile.getParentFile();

            List<Path> folderList = new ArrayList<Path>();

            while (tempDir != null) {
                folderList.add(0, tempDir.toPath());

                tempDir = tempDir.getParentFile();
            }

            FileTreeNode parentNode = null;
            try {

                for (Path folder : folderList) {
                    FileTreeNode node = new FileTreeNode(folder);
                    if (parentNode == null) {
                        rootNode.add(node);
                    } else {
                        parentNode.add(node);
                    }
                    parentNode = node;
                }

                FileTreeNode node = new FileTreeNode(tempFile.toPath());
                parentNode.add(node);

            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            DefaultMutableTreeNode node = FileSystemNodeManager.getNode(null);
            assertNull(node);

            FileSystemNodeManager.nodeAdded(null, 0);
            node = FileSystemNodeManager.getNode(tempFile);
            assertNotNull(node);
            assertEquals(node.toString(), tempFile.getName());

            tempFile.delete();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.extension.filesystem.node.FileSystemNodeManager#refreshNode(javax.swing.tree.DefaultMutableTreeNode)}.
     */
    @Test
    public void testRefreshNode() {}
}
