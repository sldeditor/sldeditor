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

package com.sldeditor.test.unit.extension.filesystem.file.sldeditor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.extension.filesystem.file.sld.SLDFileHandler;
import com.sldeditor.extension.filesystem.file.sldeditor.SLDEditorFileHandler;
import com.sldeditor.filter.v2.envvar.EnvVar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Icon;
import org.junit.jupiter.api.Test;

/**
 * Unit test for SLDEditorFileHandler class.
 *
 * <p>{@link com.sldeditor.extension.filesystem.file.sldeditor.SLDEditorFileHandler}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDFileEditorHandlerTest {

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.sldeditor.SLDEditorFileHandler#getFileExtensionList()}.
     */
    @Test
    public void testGetFileExtension() {
        assertEquals(Arrays.asList("sldeditor"), new SLDEditorFileHandler().getFileExtensionList());
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.sldeditor.SLDEditorFileHandler#populate(com.sldeditor.common.filesystem.FileSystemInterface,
     * javax.swing.tree.DefaultTreeModel,
     * com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode)}.
     */
    @Test
    public void testPopulate() {
        assertFalse(new SLDEditorFileHandler().populate(null, null, null));
    }

    @Test
    public void testIsDataSource() {
        assertFalse(new SLDEditorFileHandler().isDataSource());
    }

    @Test
    public void testGetSLDName() {
        SLDEditorFileHandler sldEditorFileHandler = new SLDEditorFileHandler();

        // Try with null parameter
        assertEquals(sldEditorFileHandler.getSLDName(null), "");

        // Try with populated data
        StyleWrapper sldWrapper = new StyleWrapper("workspace", "style");
        SLDData sldData = new SLDData(sldWrapper, "");

        assertEquals(sldEditorFileHandler.getSLDName(sldData), "style.sld");
    }

    @Test
    public void testGetIcon() {
        SLDEditorFileHandler sldEditorFileHandler = new SLDEditorFileHandler();

        Icon icon1 = sldEditorFileHandler.getIcon(null, null);
        assertNotNull(icon1);
        Icon icon2 = sldEditorFileHandler.getIcon(null, null);
        assertNotNull(icon2);
        assertEquals(icon1, icon2);
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.sldeditor.SLDEditorFileHandler#getSLDContents(com.sldeditor.common.NodeInterface)}.
     */
    @Test
    public void testGetSLDContents() {
        assertNull(new SLDEditorFileHandler().getSLDContents(null));

        URL url = SLDFileEditorHandlerTest.class.getResource("/point/sld");

        File parent = null;
        try {
            parent = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        System.out.println(url.toString());
        try {
            FileTreeNode fileTreeNode = new FileTreeNode(parent, "point_attribute_sldeditor.sld");

            SLDFileHandler handler = new SLDFileHandler();

            List<SLDDataInterface> sldDataList = handler.getSLDContents(fileTreeNode);

            assertEquals(1, sldDataList.size());

            // Changes where the file is to be saved to
            File sldEditorFile = File.createTempFile(getClass().getSimpleName(), ".sldeditor");

            SLDData sldData = (SLDData) sldDataList.get(0);

            // Environment variables
            List<EnvVar> envVarList = new ArrayList<EnvVar>();
            envVarList.add(new EnvVar("notset", String.class, false));
            EnvVar envVar = new EnvVar("test", String.class, false);
            envVar.setValue("value");
            envVarList.add(envVar);

            EnvVar envVarBuiltIn = new EnvVar("wms_width", Integer.class, true);
            envVarBuiltIn.setValue(42);
            envVarList.add(envVarBuiltIn);

            sldData.setEnvVarList(envVarList);

            // Vendor options
            List<VersionData> vendorOptionList = new ArrayList<VersionData>();
            vendorOptionList.add(VersionData.decode(getClass(), "1.2.3"));

            sldData.setVendorOptionList(vendorOptionList);

            // Data sources
            DataSourcePropertiesInterface dataSourceProperties =
                    DataSourceConnectorFactory.getDataSourceProperties("test.shp");

            sldData.setDataSourceProperties(dataSourceProperties);

            sldData.setSldEditorFile(sldEditorFile);

            SLDEditorFileHandler editorFileHandler = new SLDEditorFileHandler();

            assertFalse(editorFileHandler.save(null));
            assertTrue(editorFileHandler.save(sldData));

            SLDEditorFileHandler editorFileHandler2 = new SLDEditorFileHandler();
            List<SLDDataInterface> actualSldDataList = editorFileHandler2.open(sldEditorFile);

            assertEquals(1, actualSldDataList.size());

            FileTreeNode editorFileTreeNode =
                    new FileTreeNode(sldEditorFile.getParentFile(), sldEditorFile.getName());
            List<SLDDataInterface> actualSldDataList2 =
                    editorFileHandler2.getSLDContents(editorFileTreeNode);
            assertEquals(1, actualSldDataList2.size());

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
}
