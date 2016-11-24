package com.sldeditor.test.unit.common.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sldeditor.common.DataSourceConnectorInterface;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.output.SLDOutputFormatEnum;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import com.sldeditor.ui.legend.option.LegendOptionData;

/**
 * The unit test for SLDData.
 * <p>{@link com.sldeditor.common.data.SLDData}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDDataTest {

    class DummyDataSourceProperties implements DataSourcePropertiesInterface
    {

        @Override
        public Map<String, String> getConnectionProperties() {
            return null;
        }

        @Override
        public Map<String, String> getAllConnectionProperties() {
            return null;
        }

        @Override
        public void setPropertyMap(Map<String, String> propertyMap) {
        }

        @Override
        public void setFilename(String filename) {
        }

        @Override
        public DataSourceConnectorInterface getDataSourceConnector() {
            return null;
        }

        @Override
        public void populate() {
        }

        @Override
        public String getFilename() {
            return null;
        }

        @Override
        public void encodeXML(Document doc, Element root, String elementName) {
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean hasPassword() {
            return false;
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public void setPassword(String password) {
        }

        @Override
        public String getDebugConnectionString() {
            return null;
        }
    }

    /**
     * Test get layer name.
     */
    @Test
    public void testGetLayerName() {
        String styleName = "style";
        String styleFilename = styleName + ".sld";
        StyleWrapper styleWrapper = new StyleWrapper("workspace", styleFilename);
        SLDData data = new SLDData(styleWrapper, null);

        String actualLayerName = data.getLayerName();
        assertEquals(styleFilename, actualLayerName);
        String actualLayerNameWithOutSuffix = data.getLayerNameWithOutSuffix();
        assertEquals(styleName, actualLayerNameWithOutSuffix);
    }

    /**
     * Test sld file.
     */
    @Test
    public void testSLDFile() {
        SLDData data = new SLDData(null, null);

        File sldFile = null;
        try {
            sldFile = File.createTempFile(getClass().getSimpleName(), ".txt");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create test SLD file");
        }

        data.setSLDFile(sldFile);

        String expectedFile = sldFile.getAbsolutePath();
        String actualSldFile = data.getSLDFile().getAbsolutePath();
        assertEquals(expectedFile, actualSldFile);
        assertNull(data.getConnectionData());

        sldFile.delete();
    }

    /**
     * Test connection data.
     */
    @Test
    public void testConnectionData() {
        SLDData data = new SLDData(null, null);
        GeoServerConnection connectionData = new GeoServerConnection();
        data.setConnectionData(connectionData);

        assertEquals(connectionData, data.getConnectionData());
    }

    /**
     * Test read only.
     */
    @Test
    public void testReadOnly() {
        SLDData data = new SLDData(null, null);

        data.setReadOnly(true);
        assertTrue(data.isReadOnly());
        data.setReadOnly(false);
        assertFalse(data.isReadOnly());
    }

    /**
     * Test vendor option list.
     */
    @Test
    public void testVendorOptionList() {
        List<VersionData> vendorOptionList = new ArrayList<VersionData>();
        vendorOptionList.add(VersionData.getLatestVersion(this.getClass()));
        SLDData data = new SLDData(null, null);
        data.setVendorOptionList(vendorOptionList);
        assertEquals(vendorOptionList, data.getVendorOptionList());
    }

    /**
     * Test field list.
     */
    @Test
    public void testFieldList() {
        List<DataSourceAttributeData> fieldList = new ArrayList<DataSourceAttributeData>();
        fieldList.add(new DataSourceAttributeData("Field 1", String.class, null));
        fieldList.add(new DataSourceAttributeData("Field 2", Double.class, null));

        SLDData data = new SLDData(null, null);
        data.setFieldList(fieldList);
        assertEquals(fieldList, data.getFieldList());
    }

    /**
     * Test data source properties.
     */
    @Test
    public void testDataSourceProperties() {
        DataSourcePropertiesInterface dataSourceProperties = new DummyDataSourceProperties();
        SLDData data = new SLDData(null, null);
        data.setDataSourceProperties(dataSourceProperties);
        assertEquals(dataSourceProperties, data.getDataSourceProperties());

        // Increase code coverage
        data.setDataSourceProperties(dataSourceProperties);
        assertEquals(dataSourceProperties, data.getDataSourceProperties());
    }

    /**
     * Test style.
     */
    @Test
    public void testStyle() {
        StyleWrapper styleWrapper = new StyleWrapper("workspace","style");
        SLDData data = new SLDData(styleWrapper, null);
        assertEquals(styleWrapper, data.getStyle());
    }

    /**
     * Test sld editor file.
     */
    @Test
    public void testSldEditorFile() {
        SLDData data = new SLDData(null, null);

        File sldEditorFile = null;
        try {
            sldEditorFile = File.createTempFile(getClass().getSimpleName(), ".txt");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create test SLD Editor file");
        }
        data.setSldEditorFile(sldEditorFile);
        assertEquals(sldEditorFile, data.getSldEditorFile());

        sldEditorFile.delete();
    }

    /**
     * Test update sld contents.
     */
    @Test
    public void testUpdateSLDContents() {
        String sldContents = "Original sld contents";
        SLDData data = new SLDData(null, sldContents);
        assertEquals(sldContents, data.getSld());

        String updateSldContents = "Updated sld contents";
        data.updateSLDContents(updateSldContents);
        assertEquals(updateSldContents, data.getSld());
    }

    /**
     * Test original format
     */
    @Test
    public void testOriginalFormat() {
        SLDData data = new SLDData(null, null);
        assertEquals(SLDOutputFormatEnum.SLD, data.getOriginalFormat());

        SLDOutputFormatEnum expected = SLDOutputFormatEnum.YSLD;
        data.setOriginalFormat(expected);
        assertEquals(expected, data.getOriginalFormat());
    }

    /**
     * Test resource locator
     */
    @Test
    public void testResourceLocator() {
        SLDData data = new SLDData(null, null);

        assertNull(data.getResourceLocator());

        try {
            URL url = new URL("http://localhost:8080/geoserver");
            data.setResourceLocator(url);

            URL actualValue = data.getResourceLocator();

            assertTrue(url.toExternalForm().compareTo(actualValue.toExternalForm()) == 0);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test legend options
     */
    @Test
    public void testLegendOptions() {
        SLDData data = new SLDData(null, null);

        LegendOptionData legendOptions = null;
        assertNotNull(data.getLegendOptions());
        
        data.setLegendOptions(legendOptions);
        assertNotNull(data.getLegendOptions());

        legendOptions = new LegendOptionData();
        data.setLegendOptions(legendOptions);
        assertEquals(legendOptions, data.getLegendOptions());
    }
}
