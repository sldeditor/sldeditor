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

package com.sldeditor.test.unit.extension.filesystem.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.ToolSelectionInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.extension.filesystem.node.FSTree;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.extension.filesystem.file.FileSystemInput;
import com.sldeditor.test.unit.extension.filesystem.file.sld.SLDFileHandlerTest;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.junit.jupiter.api.Test;

/**
 * Unit test for FileSystemInput class.
 *
 * <p>{@link com.sldeditor.extension.filesystem.file.FileSystemInput#FileSystemInput}
 *
 * @author Robert Ward (SCISYS)
 */
public class FileSystemInputTest {

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.FileSystemInput#FileSystemInput(com.sldeditor.common.ToolSelectionInterface)}.
     */
    @Test
    public void testFileSystemInput() {
        FileSystemInput input = new FileSystemInput(null);

        FSTree tree = new FSTree();

        DefaultMutableTreeNode rootNode;
        try {
            rootNode = new DefaultMutableTreeNode("Root");

            DefaultTreeModel model = new DefaultTreeModel(rootNode);

            input.populate(tree, model, rootNode);

            URL url = SLDFileHandlerTest.class.getResource("/point/sld/point_attribute.sld");

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

            List<SLDDataInterface> sldDataContentsList =
                    input.getSLDContents(fileTreeNode).getSldData();
            assertEquals(1, sldDataContentsList.size());

            // Changes where the file is to be saved to
            File saveFile = File.createTempFile(getClass().getSimpleName(), ".sld");
            File sldEditorFile = File.createTempFile(getClass().getSimpleName(), ".sldeditor");

            SLDData sldData = (SLDData) sldDataContentsList.get(0);

            sldData.setSLDFile(saveFile);
            sldData.setSldEditorFile(sldEditorFile);
            assertFalse(input.save(null));
            assertTrue(input.save(sldData));

            saveFile.delete();
            sldEditorFile.delete();
        } catch (SecurityException e) {
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
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.FileSystemInput#treeExpanded(java.lang.Object)}.
     */
    @Test
    public void testTreeExpanded() {
        // Hard to test
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.FileSystemInput#getDestinationText(NodeInterface)}.
     */
    @Test
    public void testGetDestinationText() {
        FileSystemInput input = new FileSystemInput(null);

        assertEquals(
                input.getDestinationText(null),
                Localisation.getString(FileSystemInput.class, "FileSystemInput.unknown"));

        FileTreeNode fileTreeNode;
        try {
            File file = new File(".", "test.shp");
            fileTreeNode = new FileTreeNode(file.getParentFile(), file.getName());
            assertEquals(input.getDestinationText(fileTreeNode), file.getAbsolutePath());
        } catch (SecurityException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.FileSystemInput#rightMouseButton(java.lang.Object,
     * java.awt.event.MouseEvent)}.
     */
    @Test
    public void testRightMouseButton() {
        class TestFileSystemInput extends FileSystemInput {

            /** The Constant serialVersionUID. */
            private static final long serialVersionUID = 1L;

            /**
             * Instantiates a new test file system input.
             *
             * @param toolMgr the tool mgr
             */
            public TestFileSystemInput(ToolSelectionInterface toolMgr) {
                super(toolMgr);
            }

            /*
             * (non-Javadoc)
             *
             * @see
             * com.sldeditor.extension.filesystem.file.FileSystemInput#copyPathToClipboard(java.io.
             * File)
             */
            @Override
            protected void copyPathToClipboard(File file) {
                super.copyPathToClipboard(file);
            }
        }
        TestFileSystemInput input = new TestFileSystemInput(null);

        FSTree tree = new FSTree();

        DefaultMutableTreeNode rootNode;

        rootNode = new DefaultMutableTreeNode("Root");

        DefaultTreeModel model = new DefaultTreeModel(rootNode);

        input.populate(tree, model, rootNode);

        URL url = SLDFileHandlerTest.class.getResource("/point/sld/point_attribute.sld");

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

        FileTreeNode fileTreeNode = null;
        try {
            fileTreeNode = new FileTreeNode(parent, "point_attribute.sld");
        } catch (SecurityException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        JPopupMenu popupMenu = new JPopupMenu();

        input.rightMouseButton(popupMenu, fileTreeNode, null);

        input.copyPathToClipboard(null);
        input.copyPathToClipboard(fileTreeNode.getFile());

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        String result = null;
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText =
                (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException ex) {
                ex.printStackTrace();
                fail(ex.getMessage());
            }
        }
        assertEquals(fileTreeNode.getFile().getAbsolutePath(), result);
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.FileSystemInput#getNodeTypes()}.
     */
    @Test
    public void testGetNodeTypes() {
        FileSystemInput input = new FileSystemInput(null);

        assertTrue(input.getNodeTypes().isEmpty());
    }

    /**
     * Copy file to new folder
     *
     * <p>Test method for {@link
     * com.sldeditor.extension.filesystem.file.FileSystemInput#copyNodes(com.sldeditor.common.NodeInterface,
     * java.util.Map)}.
     */
    @Test
    public void testCopyNodes() {
        FileSystemInput input = new FileSystemInput(null);

        URL url = SLDFileHandlerTest.class.getResource("/point/sld/point_attribute.sld");

        List<SLDDataInterface> sldDataList = input.open(url);

        assertEquals(1, sldDataList.size());

        try {
            Path tempFolder = null;
            try {
                tempFolder = Files.createTempDirectory(getClass().getSimpleName());
            } catch (IOException e) {
                e.printStackTrace();
                fail("Failed to create temp folder in temp folder!");
            }

            File tempFolderFile = tempFolder.toFile();
            FileTreeNode destinationTreeNode =
                    new FileTreeNode(tempFolderFile.getParentFile(), tempFolderFile.getName());

            Map<NodeInterface, List<SLDDataInterface>> copyDataMap =
                    new HashMap<NodeInterface, List<SLDDataInterface>>();

            copyDataMap.put(destinationTreeNode, sldDataList);
            assertTrue(input.copyNodes(destinationTreeNode, copyDataMap));
            assertFalse(input.copyNodes(null, copyDataMap));
            assertFalse(input.copyNodes(destinationTreeNode, null));
            assertFalse(input.copyNodes(null, null));

            SLDData sldData = (SLDData) sldDataList.get(0);
            sldData.setSLDFile(new File(tempFolderFile, "point_attribute.sld"));

            input.deleteNodes(destinationTreeNode, null);
            input.deleteNodes(destinationTreeNode, sldDataList);

            tempFolderFile.deleteOnExit();

            try {
                Files.walk(tempFolder)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
