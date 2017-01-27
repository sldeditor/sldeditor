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
package com.sldeditor.test.unit.extension.filesystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.junit.Test;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.RecursiveUpdateInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.ToolSelectionInterface;
import com.sldeditor.common.connection.GeoServerConnectionManager;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.common.preferences.PrefData;
import com.sldeditor.common.preferences.PrefDataLastViewedEnum;
import com.sldeditor.common.property.PropertyManagerFactory;
import com.sldeditor.datasource.extension.filesystem.node.FSTree;
import com.sldeditor.extension.ExtensionFactory;
import com.sldeditor.extension.filesystem.FileSystemExtension;
import com.sldeditor.extension.filesystem.FileSystemExtensionFactory;
import com.sldeditor.tool.ToolInterface;

/**
 * Unit test for FileSystemExtension class.
 * <p>{@link com.sldeditor.extension.filesystem.FileSystemExtension}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class FileSystemExtensionTest {

    /**
     * The Class DummyToolMgr.
     */
    class DummyToolMgr implements ToolSelectionInterface
    {
        @Override
        public void setSelectedItems(List<NodeInterface> nodeTypeList,
                List<SLDDataInterface> sldDataList) {
        }

        @Override
        public JPanel getPanel() {
            return null;
        }

        @Override
        public void refreshSelection() {
        }

        @Override
        public SLDEditorInterface getApplication() {
            return null;
        }

        @Override
        public void registerTool(Class<?> nodeType, ToolInterface toolToRegister) {
        }

        /* (non-Javadoc)
         * @see com.sldeditor.common.ToolSelectionInterface#isRecursiveFlag()
         */
        @Override
        public boolean isRecursiveFlag() {
            return false;
        }

        /* (non-Javadoc)
         * @see com.sldeditor.common.ToolSelectionInterface#setRecursiveFlag(boolean)
         */
        @Override
        public void setRecursiveFlag(boolean recursiveFlag) {
        }

        /* (non-Javadoc)
         * @see com.sldeditor.common.ToolSelectionInterface#addRecursiveListener(com.sldeditor.common.RecursiveUpdateInterface)
         */
        @Override
        public void addRecursiveListener(RecursiveUpdateInterface recursiveUpdate) {
        }
    }

    /**
     * The Class DummyExtension.
     */
    class DummyExtension implements FileSystemInterface
    {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Populate.
         *
         * @param tree the tree
         * @param model the model
         * @param rootNode the root node
         */
        @Override
        public void populate(FSTree tree, DefaultTreeModel model, DefaultMutableTreeNode rootNode) {
        }

        /**
         * Tree expanded.
         *
         * @param selectedItem the selected item
         * @return true, if successful
         */
        @Override
        public boolean treeExpanded(Object selectedItem) {
            return false;
        }

        /**
         * Right mouse button.
         *
         * @param popupMenu the popup menu
         * @param selectedItem the selected item
         * @param e the e
         */
        @Override
        public void rightMouseButton(JPopupMenu popupMenu, Object selectedItem, MouseEvent e) {
        }

        /**
         * Gets the SLD contents.
         *
         * @param node the node
         * @return the SLD contents
         */
        @Override
        public SelectedFiles getSLDContents(NodeInterface node) {
            return null;
        }

        /**
         * Open.
         *
         * @param url the url
         * @return the list
         */
        @Override
        public List<SLDDataInterface> open(URL url) {
            if(url.toExternalForm().endsWith(".tst"))
            {
                List<SLDDataInterface> list = new ArrayList<SLDDataInterface>();
                return list;
            }
            return null;
        }

        /**
         * Save.
         *
         * @param sldData the sld data
         * @return true, if successful
         */
        @Override
        public boolean save(SLDDataInterface sldData) {
            return (sldData != null);
        }

        /**
         * Gets the node types.
         *
         * @return the node types
         */
        @Override
        public List<NodeInterface> getNodeTypes() {
            return null;
        }

        /**
         * Copy nodes.
         *
         * @param destinationTreeNode the destination tree node
         * @param droppedDataMap the dropped data map
         * @return true, if successful
         */
        @Override
        public boolean copyNodes(NodeInterface destinationTreeNode,
                Map<NodeInterface, List<SLDDataInterface>> droppedDataMap) {
            return false;
        }

        /**
         * Delete nodes.
         *
         * @param nodeToTransfer the node to transfer
         * @param sldDataList the sld data list
         */
        @Override
        public void deleteNodes(NodeInterface nodeToTransfer, List<SLDDataInterface> sldDataList) {
        }

        /**
         * Gets the destination text.
         *
         * @param destinationTreeNode the destination tree node
         * @return the destination text
         */
        @Override
        public String getDestinationText(NodeInterface destinationTreeNode) {
            return null;
        }
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.FileSystemExtension#initialise(com.sldeditor.common.LoadSLDInterface, com.sldeditor.common.ToolSelectionInterface)}.
     */
    @Test
    public void testInitialise() {
        FileSystemExtension fsExt = new FileSystemExtension();
        DummyExtension dummyExtension = new DummyExtension();

        List<FileSystemInterface> overrideExtensionList = new ArrayList<FileSystemInterface>();
        overrideExtensionList.add(dummyExtension);
        FileSystemExtensionFactory.override(overrideExtensionList);
        fsExt.initialise(null, null);

        fsExt.initialise(null, new DummyToolMgr());
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.FileSystemExtension#setArguments(java.util.List)}.
     */
    @Test
    public void testSetArguments() {
        File configPropertiesFile = new File("./FileSystemExtensionTest.properties");
        PropertyManagerFactory.getInstance().setPropertyFile(configPropertiesFile);

        FileSystemExtension fsExt = new FileSystemExtension();
        FileSystemExtensionFactory.override(null);
        fsExt.initialise(null, new DummyToolMgr());

        // Handle null argument
        fsExt.setArguments(null);

        List<String> extensionArgList = new ArrayList<String>();
        extensionArgList.add("invalid1");
        extensionArgList.add("in.val.id2");
        extensionArgList.add("in.va.li.d3.");
        extensionArgList.add("invalid4=abc");
        String actual = fsExt.setArguments(extensionArgList);
        assertNull(actual);

        // File does not exist
        extensionArgList.clear();
        String expectedFile = "folder=X:\\asdef";
        extensionArgList.add(expectedFile);
        actual = fsExt.setArguments(extensionArgList);
        assertNull(actual);

        // Valid file
        extensionArgList.clear();
        File f = null;
        try {
            f = File.createTempFile(getClass().getSimpleName(), ".tmp");
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        expectedFile = "folder=" + f.getParent();
        extensionArgList.add(expectedFile);
        actual = fsExt.setArguments(extensionArgList);
        assertEquals(expectedFile, actual);
        f.delete();

        // Invalid GeoServer
        extensionArgList.clear();
        String expectedGeoServer = "geoserver=unknown";
        extensionArgList.add(expectedGeoServer);
        actual = fsExt.setArguments(extensionArgList);
        assertNull(actual);

        // Valid GeoServer
        GeoServerConnection geoServerConnection = new GeoServerConnection();
        geoServerConnection.setConnectionName("GeoServer connection");
        try {
            geoServerConnection.setUrl(new URL("http://localhost/geoserver"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail();
        }
        geoServerConnection.setUserName("username");
        geoServerConnection.setPassword("password");
        GeoServerConnectionManager.getInstance().addNewConnection(null, geoServerConnection);
        GeoServerConnectionManager.getInstance().updateList();

        extensionArgList.clear();
        expectedGeoServer = "geoserver=" + geoServerConnection.getConnectionName();
        extensionArgList.add(expectedGeoServer);
        actual = fsExt.setArguments(extensionArgList);

        assertEquals(expectedGeoServer, actual);
        configPropertiesFile.delete();
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.FileSystemExtension#getExtensionArgPrefix()}.
     */
    @Test
    public void testGetExtensionArgPrefix() {
        FileSystemExtension fsExt = new FileSystemExtension();
        DummyExtension dummyExtension = new DummyExtension();

        List<FileSystemInterface> overrideExtensionList = new ArrayList<FileSystemInterface>();
        overrideExtensionList.add(dummyExtension);
        FileSystemExtensionFactory.override(overrideExtensionList);
        fsExt.initialise(null, null);

        assertEquals("file", fsExt.getExtensionArgPrefix());
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.FileSystemExtension#getPanel()}.
     */
    @Test
    public void testGetPanel() {
        FileSystemExtension fsExt = new FileSystemExtension();
        DummyExtension dummyExtension = new DummyExtension();

        List<FileSystemInterface> overrideExtensionList = new ArrayList<FileSystemInterface>();
        overrideExtensionList.add(dummyExtension);
        FileSystemExtensionFactory.override(overrideExtensionList);
        fsExt.initialise(null, null);

        assertTrue(fsExt.getPanel() != null);
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.FileSystemExtension#createMenus(javax.swing.JMenu)}.
     */
    @Test
    public void testCreateMenus() {
        FileSystemExtension fsExt = new FileSystemExtension();
        DummyExtension dummyExtension = new DummyExtension();

        List<FileSystemInterface> overrideExtensionList = new ArrayList<FileSystemInterface>();
        overrideExtensionList.add(dummyExtension);
        FileSystemExtensionFactory.override(overrideExtensionList);
        fsExt.initialise(null, null);

        fsExt.createMenus(null);
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.FileSystemExtension#getTooltip()}.
     */
    @Test
    public void testGetTooltip() {
        FileSystemExtension fsExt = new FileSystemExtension();
        DummyExtension dummyExtension = new DummyExtension();

        List<FileSystemInterface> overrideExtensionList = new ArrayList<FileSystemInterface>();
        overrideExtensionList.add(dummyExtension);
        FileSystemExtensionFactory.override(overrideExtensionList);
        fsExt.initialise(null, null);

        assertTrue(fsExt.getTooltip() != null);
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.FileSystemExtension#getName()}.
     */
    @Test
    public void testGetName() {
        FileSystemExtension fsExt = new FileSystemExtension();
        DummyExtension dummyExtension = new DummyExtension();

        List<FileSystemInterface> overrideExtensionList = new ArrayList<FileSystemInterface>();
        overrideExtensionList.add(dummyExtension);
        FileSystemExtensionFactory.override(overrideExtensionList);
        fsExt.initialise(null, null);

        assertTrue(fsExt.getName() != null);
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.FileSystemExtension#open(java.net.URL)}.
     */
    @Test
    public void testOpen() {
        FileSystemExtension fsExt = new FileSystemExtension();

        fsExt.initialise(null, null);
        FileSystemExtensionFactory.override(null);

        // Try null parameter
        assertNull(fsExt.open(null));

        FileSystemExtensionFactory.getFileExtensionList(null);
        assertNull(fsExt.open(null));

        // Try invalid url
        try {
            URL url = new URL("http://test.com/abc/xyz.notrecognised");

            assertNull(fsExt.open(url));

        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        // Try valid url
        try {
            URL url = new URL("http://test.com/abc/xyz.tst");

            assertNull(fsExt.open(url));

        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.FileSystemExtension#save(com.sldeditor.common.SLDDataInterface)}.
     */
    @Test
    public void testSave() {
        FileSystemExtension fsExt = new FileSystemExtension();
        DummyExtension dummyExtension = new DummyExtension();

        List<FileSystemInterface> overrideExtensionList = new ArrayList<FileSystemInterface>();
        overrideExtensionList.add(dummyExtension);
        FileSystemExtensionFactory.override(overrideExtensionList);
        fsExt.initialise(null, null);

        // Try null parameter
        assertFalse(fsExt.save(null));

        assertTrue(fsExt.save(new SLDData(null, null)));
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.FileSystemExtension#updateForPreferences(PrefData, List<String>)}.
     */
    @Test
    public void testUpdateForPreferences() {
        FileSystemExtension fsExt = new FileSystemExtension();
        FileSystemExtensionFactory.override(null);
        fsExt.initialise(null, null);

        fsExt.updateForPreferences(null, null);

        // Set up with 'save last folder view' set to false
        List<String> actualArgList = new ArrayList<String>();
        PrefData prefData = new PrefData();
        String lastFolderViewed = null;
        prefData.setLastFolderViewed(lastFolderViewed);
        PrefDataLastViewedEnum lastViewedKey = PrefDataLastViewedEnum.FOLDER;
        prefData.setLastViewedKey(lastViewedKey);
        prefData.setSaveLastFolderView(false);

        fsExt.updateForPreferences(prefData, actualArgList);

        assertTrue(actualArgList.isEmpty());

        // Set up with 'save last folder view' set to true but no folder
        prefData.setSaveLastFolderView(true);
        fsExt.updateForPreferences(prefData, actualArgList);
        assertTrue(actualArgList.isEmpty());

        // Set up with 'save last folder view' set to true but with folder
        lastFolderViewed = "last viewed";
        prefData.setLastFolderViewed(lastFolderViewed);
        fsExt.updateForPreferences(prefData, actualArgList);
        assertEquals(1, actualArgList.size());
        String expected = String.format("%s.%s.%s=%s",
                ExtensionFactory.EXTENSION_PREFIX,
                fsExt.getExtensionArgPrefix(),
                "folder",
                lastFolderViewed);
        assertEquals(expected, actualArgList.get(0));
        
        // Set up with 'save last folder view' set to true but with GeoServer
        actualArgList.clear();
        lastViewedKey = PrefDataLastViewedEnum.GEOSERVER;
        prefData.setLastViewedKey(lastViewedKey);
        fsExt.updateForPreferences(prefData, actualArgList);
        assertEquals(1, actualArgList.size());
        expected = String.format("%s.%s.%s=%s",
                ExtensionFactory.EXTENSION_PREFIX,
                fsExt.getExtensionArgPrefix(),
                "geoserver",
                lastFolderViewed);
        assertEquals(expected, actualArgList.get(0));

        // Try and replace existing argument
        String previous = lastFolderViewed;
        lastFolderViewed = "new folder";
        prefData.setLastFolderViewed(lastFolderViewed);
        fsExt.updateForPreferences(prefData, actualArgList);
        assertEquals(1, actualArgList.size());
        expected = String.format("%s.%s.%s=%s",
                ExtensionFactory.EXTENSION_PREFIX,
                fsExt.getExtensionArgPrefix(),
                "geoserver",
                previous);
        assertEquals(expected, actualArgList.get(0));
    }
}
