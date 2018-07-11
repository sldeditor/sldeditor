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

package com.sldeditor.test.unit.datasource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.datasource.SLDEditorDataUpdateInterface;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.StickyDataSourceInterface;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.datasource.connector.instance.DataSourceConnector;
import com.sldeditor.datasource.impl.DataSourceProperties;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit test for DataSourceField.
 *
 * <p>{@link com.sldeditor.datasource.SLDEditorFile}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDEditorFileTest {

    class DummyDataUpdate implements SLDEditorDataUpdateInterface {

        public SLDDataInterface sldData = null;

        public boolean dataEditedFlag = false;

        @Override
        public void sldDataUpdated(SLDDataInterface sldData, boolean dataEditedFlag) {
            this.sldData = sldData;
            this.dataEditedFlag = dataEditedFlag;
        }
    }

    /** The Class DummyStickyData. */
    class DummyStickyData implements StickyDataSourceInterface {
        public boolean sticky = false;

        public boolean stickyCalled = false;

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.datasource.StickyDataSourceInterface#stickyDataSourceUpdates(boolean)
         */
        @Override
        public void stickyDataSourceUpdates(boolean updated) {
            sticky = updated;
            stickyCalled = true;
        }
    }

    /**
     * Test method for {@link com.sldeditor.datasource.SLDEditorFile#getSLDData()}. Test method for
     * {@link com.sldeditor.datasource.SLDEditorFile#fileOpenedSaved()}. Test method for {@link
     * com.sldeditor.datasource.SLDEditorFile#getSldEditorFile()}. Test method for {@link
     * com.sldeditor.datasource.SLDEditorFile#addSLDEditorFileUpdateListener(com.sldeditor.datasource.SLDEditorDataUpdateInterface)}.
     * Test method for {@link com.sldeditor.datasource.SLDEditorFile#renderSymbol()}. Test method
     * for {@link
     * com.sldeditor.datasource.SLDEditorFile#setSLDData(com.sldeditor.common.SLDDataInterface)}.
     * Test method for {@link com.sldeditor.datasource.SLDEditorFile#setSldFile(java.io.File)}. Test
     * method for {@link com.sldeditor.datasource.SLDEditorFile#getDataSource()}. Test method for
     * {@link
     * com.sldeditor.datasource.SLDEditorFile#setDataSource(com.sldeditor.DataSourcePropertiesInterface)}.
     * Test method for {@link
     * com.sldeditor.datasource.SLDEditorFile#setSldEditorFile(java.io.File)}.
     */
    @Test
    public void testSLDData() {
        // Start with a blank canvas
        SLDEditorFile.destroyInstance();
        SelectedSymbol.destroyInstance();

        assertNull(SLDEditorFile.getInstance().getSLD());
        assertNull(SLDEditorFile.getInstance().getSLDData());
        assertNull(SLDEditorFile.getInstance().getSldEditorFile());
        assertNull(SLDEditorFile.getInstance().getDataSource());

        SLDEditorFile.getInstance().setDataSource(null);
        assertNull(SLDEditorFile.getInstance().getDataSource());

        SLDEditorFile.getInstance().setSldEditorFile(null);
        assertNull(SLDEditorFile.getInstance().getSldEditorFile());

        SLDEditorFile.getInstance().setSldFile(null);
        assertNull(SLDEditorFile.getInstance().getSLDData());

        SLDEditorFile.getInstance().setSLDData(null);
        assertNull(SLDEditorFile.getInstance().getSLDData());

        DummyDataUpdate dataUpdateListener = new DummyDataUpdate();

        SLDEditorFile.getInstance().addSLDEditorFileUpdateListener(dataUpdateListener);

        // Set SLDData
        StyleWrapper styleWrapper = new StyleWrapper("workspace", "style");
        SLDData sldData = new SLDData(styleWrapper, "contents");

        SLDEditorFile.getInstance().setSLDData(sldData);
        SLDEditorFile.getInstance().fileOpenedSaved();

        assertEquals(sldData, SLDEditorFile.getInstance().getSLDData());
        assertFalse(dataUpdateListener.dataEditedFlag);

        // Sld file
        File sldFile = new File("test.txt");
        SLDEditorFile.getInstance().setSldFile(sldFile);
        assertEquals(
                sldFile.getAbsolutePath(),
                SLDEditorFile.getInstance().getSLDData().getSLDFile().getAbsolutePath());
        assertFalse(dataUpdateListener.dataEditedFlag);

        // Sld editor file
        File sldEditorFile = new File("editor.file");
        SLDEditorFile.getInstance().setSldEditorFile(sldEditorFile);
        assertEquals(
                sldEditorFile.getAbsolutePath(),
                SLDEditorFile.getInstance().getSldEditorFile().getAbsolutePath());
        assertFalse(dataUpdateListener.dataEditedFlag);

        // Data source properties
        SLDEditorFile.getInstance().setDataSource(null);
        assertFalse(dataUpdateListener.dataEditedFlag);
        DataSourcePropertiesInterface noDataSource = DataSourceConnectorFactory.getNoDataSource();
        assertEquals(
                noDataSource.getDebugConnectionString(),
                SLDEditorFile.getInstance().getDataSource().getDebugConnectionString());
        assertFalse(dataUpdateListener.dataEditedFlag);

        DataSourcePropertiesInterface fileDSProperties =
                new DataSourceProperties(new DataSourceConnector());
        assertEquals(
                fileDSProperties.getDebugConnectionString(),
                SLDEditorFile.getInstance().getDataSource().getDebugConnectionString());
        assertFalse(dataUpdateListener.dataEditedFlag);

        // Render symbol
        SLDEditorFile.getInstance().renderSymbol();
        assertTrue(dataUpdateListener.dataEditedFlag);

        // File saved
        SLDEditorFile.getInstance().fileOpenedSaved();
        assertFalse(dataUpdateListener.dataEditedFlag);

        assertNull(SLDEditorFile.getInstance().getRuleRenderOptions());
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.SLDEditorFile#dataSourceLoaded(com.sldeditor.datasource.impl.GeometryTypeEnum,
     * boolean)}.
     */
    @Test
    public void testDataSourceLoaded() {
        SLDEditorFile.getInstance().dataSourceLoaded(null, false);
        // Does nothing
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.SLDEditorFile#addSLDOutputListener(com.sldeditor.common.output.SLDOutputInterface)}.
     */
    @Test
    public void testAddSLDOutputListener() {
        SLDEditorFile.getInstance().addSLDOutputListener(null);
        // Does nothing
    }

    /** Test method for {@link com.sldeditor.datasource.SLDEditorFile#getSLDFileExtension()}. */
    @Test
    public void testGetSLDFileExtension() {
        assertEquals(
                ".sld",
                ExternalFilenames.addFileExtensionSeparator(SLDEditorFile.getSLDFileExtension()));
    }

    /** Test method for {@link com.sldeditor.datasource.SLDEditorFile#vendorOptionsUpdated()}. */
    @Test
    public void testVendorOptionsUpdated() {
        SLDEditorFile.destroyInstance();
        DummyDataUpdate dataUpdateListener = new DummyDataUpdate();

        SLDEditorFile.getInstance().addSLDEditorFileUpdateListener(dataUpdateListener);

        assertFalse(dataUpdateListener.dataEditedFlag);

        SLDEditorFile.getInstance().vendorOptionsUpdated(null);
        assertFalse(dataUpdateListener.dataEditedFlag);

        List<VersionData> vendorOptionVersionsList = new ArrayList<VersionData>();
        SLDEditorFile.getInstance().vendorOptionsUpdated(vendorOptionVersionsList);
        assertFalse(dataUpdateListener.dataEditedFlag);

        StyleWrapper styleWrapper = new StyleWrapper("workspace", "style");
        SLDData sldData = new SLDData(styleWrapper, "contents");

        SLDEditorFile.getInstance().setSLDData(sldData);
        SLDEditorFile.getInstance().vendorOptionsUpdated(vendorOptionVersionsList);

        assertTrue(dataUpdateListener.dataEditedFlag);
    }

    /**
     * Test method for {@link com.sldeditor.datasource.SLDEditorFile#setStickyDataSource(boolean)}.
     * Test method for {@link com.sldeditor.datasource.SLDEditorFile#isStickyDataSource()}.
     */
    @Test
    public void testStickDataSource() {
        DummyStickyData obj1 = new DummyStickyData();
        DummyStickyData obj2 = new DummyStickyData();

        SLDEditorFile.getInstance().addStickyDataSourceListener(obj1);
        SLDEditorFile.getInstance().addStickyDataSourceListener(obj1);
        SLDEditorFile.getInstance().addStickyDataSourceListener(obj2);

        assertFalse(SLDEditorFile.getInstance().isStickyDataSource());
        assertFalse(obj1.stickyCalled);
        assertFalse(obj2.stickyCalled);
        SLDEditorFile.getInstance().setStickyDataSource(obj1, true);
        assertTrue(SLDEditorFile.getInstance().isStickyDataSource());
        assertFalse(obj1.stickyCalled);
        assertTrue(obj2.stickyCalled);
        assertTrue(obj2.sticky);
        obj2.stickyCalled = false;
        SLDEditorFile.getInstance().setStickyDataSource(obj2, false);
        assertFalse(SLDEditorFile.getInstance().isStickyDataSource());
        assertTrue(obj1.stickyCalled);
        assertFalse(obj2.stickyCalled);
        assertFalse(obj1.sticky);
    }
}
