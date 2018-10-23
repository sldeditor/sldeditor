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

package com.sldeditor.test.unit.tool.raster;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.LoadSLDInterface;
import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.datasource.chooseraster.DetermineRasterFormat;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNodeTypeEnum;
import com.sldeditor.tool.raster.RasterImport;
import com.sldeditor.tool.savesld.SaveSLD;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

/**
 * Unit test for RasterImport.
 *
 * @author Robert Ward (SCISYS)
 */
class RasterImportTest {

    class TestLoadInterface implements LoadSLDInterface {
        public SelectedFiles loadSelectedFiles = null;

        /* (non-Javadoc)
         * @see com.sldeditor.common.LoadSLDInterface#emptySLD()
         */
        @Override
        public void emptySLD() {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see com.sldeditor.common.LoadSLDInterface#preLoad()
         */
        @Override
        public void preLoad() {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see com.sldeditor.common.LoadSLDInterface#loadSLDString(com.sldeditor.common.filesystem.SelectedFiles)
         */
        @Override
        public boolean loadSLDString(SelectedFiles selectedFiles) {
            loadSelectedFiles = selectedFiles;
            return false;
        }

        /* (non-Javadoc)
         * @see com.sldeditor.common.LoadSLDInterface#reloadSLDFile()
         */
        @Override
        public void reloadSLDFile() {
            // Do nothing
        }
    }

    class TestSLDEditorInterface implements SLDEditorInterface {
        private TestLoadInterface loadInterface = new TestLoadInterface();

        /* (non-Javadoc)
         * @see com.sldeditor.common.SLDEditorInterface#getAppPanel()
         */
        @Override
        public JPanel getAppPanel() {
            return null;
        }

        /* (non-Javadoc)
         * @see com.sldeditor.common.SLDEditorInterface#updateWindowTitle(boolean)
         */
        @Override
        public void updateWindowTitle(boolean dataEditedFlag) {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see com.sldeditor.common.SLDEditorInterface#chooseNewSLD()
         */
        @Override
        public void chooseNewSLD() {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see com.sldeditor.common.SLDEditorInterface#exitApplication()
         */
        @Override
        public void exitApplication() {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see com.sldeditor.common.SLDEditorInterface#saveFile(java.net.URL)
         */
        @Override
        public void saveFile(URL url) {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see com.sldeditor.common.SLDEditorInterface#saveSLDData(com.sldeditor.common.SLDDataInterface)
         */
        @Override
        public void saveSLDData(SLDDataInterface sldData) {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see com.sldeditor.common.SLDEditorInterface#getLoadSLDInterface()
         */
        @Override
        public LoadSLDInterface getLoadSLDInterface() {
            return loadInterface;
        }

        /* (non-Javadoc)
         * @see com.sldeditor.common.SLDEditorInterface#openFile(java.net.URL)
         */
        @Override
        public void openFile(URL selectedURL) {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see com.sldeditor.common.SLDEditorInterface#refreshPanel(java.lang.Class, java.lang.Class)
         */
        @Override
        public void refreshPanel(Class<?> parent, Class<?> panelClass) {
            // Do nothing
        }
    }

    /**
     * Test method for {@link com.sldeditor.tool.raster.RasterImport#importRaster(java.util.List,
     * com.sldeditor.common.SLDEditorInterface)}.
     */
    @Test
    void testImportRaster() {
        RasterImport testObj = new RasterImport();
        testObj.importRaster(null, null);

        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();

        try {
            // Copy raster file
            File rasterFile = null;
            InputStream inputStream =
                    SaveSLD.class.getResourceAsStream("/raster/sld/sld_cookbook_raster.tif");

            if (inputStream == null) {
                fail("Failed to find smileyface.png");
            } else {
                try {
                    rasterFile = stream2file(inputStream, ".tif");
                    rasterFile.deleteOnExit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileTreeNode vectorTreeNode =
                    new FileTreeNode(new File("/test"), "sld_cookbook_polygon.shp");
            vectorTreeNode.setFileCategory(FileTreeNodeTypeEnum.VECTOR);

            FileTreeNode rasterTreeNode =
                    new FileTreeNode(rasterFile.getParentFile(), rasterFile.getName());
            rasterTreeNode.setFileCategory(FileTreeNodeTypeEnum.RASTER);

            FileTreeNode sldTreeNode = new FileTreeNode(new File("/test"), "test.sld");
            sldTreeNode.setFileCategory(FileTreeNodeTypeEnum.SLD);

            nodeTypeList.add(vectorTreeNode);
            nodeTypeList.add(rasterTreeNode);
            nodeTypeList.add(sldTreeNode);

            // Too many tree nodes
            testObj.importRaster(nodeTypeList, null);

            // Try the correct number of tree nodes
            nodeTypeList.clear();
            nodeTypeList.add(rasterTreeNode);
            testObj.importRaster(nodeTypeList, null);

            // Supply an SLDEditorInterface
            TestSLDEditorInterface sldEditor = new TestSLDEditorInterface();

            // Make sure nothing has been loaded
            assertNull(((TestLoadInterface) sldEditor.getLoadSLDInterface()).loadSelectedFiles);
            testObj.importRaster(nodeTypeList, sldEditor);

            // Try with proper data
            assertNotNull(((TestLoadInterface) sldEditor.getLoadSLDInterface()).loadSelectedFiles);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.raster.RasterImport#importDataSource(java.util.List)}.
     */
    @Test
    void testImportDataSource() {
        RasterImport testObj = new RasterImport();

        testObj.importDataSource(null);

        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();

        try {
            // Copy raster file
            File rasterFile = null;
            InputStream inputStream =
                    SaveSLD.class.getResourceAsStream("/raster/sld/sld_cookbook_raster.tif");

            if (inputStream == null) {
                fail("Failed to find smileyface.png");
            } else {
                try {
                    rasterFile = stream2file(inputStream, ".tif");
                    rasterFile.deleteOnExit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileTreeNode vectorTreeNode =
                    new FileTreeNode(new File("/test"), "sld_cookbook_polygon.shp");
            vectorTreeNode.setFileCategory(FileTreeNodeTypeEnum.VECTOR);

            FileTreeNode rasterTreeNode =
                    new FileTreeNode(rasterFile.getParentFile(), rasterFile.getName());
            rasterTreeNode.setFileCategory(FileTreeNodeTypeEnum.RASTER);

            FileTreeNode sldTreeNode = new FileTreeNode(new File("/test"), "test.sld");
            sldTreeNode.setFileCategory(FileTreeNodeTypeEnum.SLD);

            nodeTypeList.add(vectorTreeNode);
            nodeTypeList.add(rasterTreeNode);
            nodeTypeList.add(sldTreeNode);

            // Too many tree nodes
            testObj.importDataSource(nodeTypeList);

            // Try the correct number of tree nodes
            nodeTypeList.clear();
            nodeTypeList.add(rasterTreeNode);

            // Prevent the choose raster dialog from appearing
            DetermineRasterFormat.setClassUndertest();

            testObj.importDataSource(nodeTypeList);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes an InputStream to a temporary file.
     *
     * @param in the in
     * @param suffix the suffix
     * @return the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static File stream2file(InputStream in, String suffix) throws IOException {
        final File tempFile = File.createTempFile("extracted", suffix);
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }
}
