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
package com.sldeditor.test.unit.datasource.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sldeditor.common.DataSourceConnectorInterface;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.datasource.connector.instance.DataSourceConnector;
import com.sldeditor.datasource.impl.DataSourceProperties;

/**
 * Unit test for DataSourceProperties.
 * <p>{@link com.sldeditor.datasource.impl.DataSourceProperties}
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourcePropertiesTest {

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceProperties#DataSourceProperties(com.sldeditor.DataSourceConnectorInterface)}.
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceProperties#setPropertyMap(java.util.Map)}.
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceProperties#setFilename(java.lang.String)}.
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceProperties#getFilename()}.
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceProperties#getDataSourceConnector()}.
     */
    @Test
    public void testDataSourcePropertiesNoConnector() {
        Map<String, Object> propertyMap = new HashMap<String, Object>();

        propertyMap.put("field1", "value1");
        propertyMap.put("field2", "value2");
        propertyMap.put("field3", "value3");

        DataSourceProperties dsp = new DataSourceProperties(null);

        dsp.setPropertyMap(propertyMap);

        Map<String, Object> actualPropertyMap = dsp.getConnectionProperties();
        assertEquals(propertyMap, actualPropertyMap);
        assertEquals(propertyMap, dsp.getAllConnectionProperties());

        // Make sure filename key does not exist
        assertFalse(actualPropertyMap.containsKey("url"));

        String expectedFilename = "this is a filename";
        dsp.setFilename(expectedFilename);
        actualPropertyMap = dsp.getConnectionProperties();
        assertTrue(actualPropertyMap.containsKey("url"));

        assertEquals(expectedFilename, dsp.getFilename());

        // Is empty
        assertTrue(dsp.isEmpty());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceProperties#DataSourceProperties(com.sldeditor.DataSourceConnectorInterface)}.
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceProperties#setPropertyMap(java.util.Map)}.
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceProperties#getDataSourceConnector()}.
     */
    @Test
    public void testDataSourcePropertiesWithConnector() {
        Map<String, Object> propertyMap = new HashMap<String, Object>();

        propertyMap.put("field1", "value1");
        propertyMap.put("field2", "value2");
        propertyMap.put("field3", "value3");

        DataSourceConnectorInterface dsc = new DataSourceConnector();
        DataSourceProperties dsp = new DataSourceProperties(dsc);

        dsp.setPropertyMap(propertyMap);

        Map<String, Object> actualPropertyMap = dsp.getConnectionProperties();
        assertEquals(propertyMap, actualPropertyMap);
        assertEquals(propertyMap, dsp.getAllConnectionProperties());

        // Is empty
        assertFalse(dsp.isEmpty());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceProperties#populate()}.
     */
    @Test
    public void testPopulate() {
        DataSourceProperties dsp = new DataSourceProperties(null);
        dsp.populate();
        
        // Does nothing
        DataSourceConnectorInterface dsc = new DataSourceConnector();
        dsp = new DataSourceProperties(dsc);
        dsp.populate();
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceProperties#encodeXML(org.w3c.dom.Document, org.w3c.dom.Element, java.lang.String)}.
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceProperties#decodeXML(org.w3c.dom.Document, java.lang.String)}.
     */
    @Test
    public void testEncodeDecodeXML() {
        Map<String, Object> propertyMap = new HashMap<String, Object>();

        propertyMap.put("field1", "value1");
        propertyMap.put("field2", "value2");
        propertyMap.put("field3", "value3");

        DataSourceConnectorInterface dsc = new DataSourceConnector();
        DataSourceProperties dsp = new DataSourceProperties(dsc);

        dsp.setPropertyMap(propertyMap);
        dsp.setFilename("testfilename.shp");
        dsp.setPassword("top secret password");

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        Document doc = documentBuilder.newDocument();
        String elementName = "test_data_source_properties";
        Element root = doc.createElement("test");
        doc.appendChild(root);
        dsp.encodeXML(doc, root, elementName);

        // Try null parameters
        dsp.encodeXML(null, null, null);
        assertNull(DataSourceProperties.decodeXML(null, null));

        // Now decode XML
        DataSourcePropertiesInterface decodeDSP = DataSourceProperties.decodeXML(doc, elementName);

        assertEquals(dsp.getConnectionProperties(), decodeDSP.getConnectionProperties());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceProperties#encodeFilename(java.lang.String)}.
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceProperties#decodeFilename(java.util.Map)}.
     */
    @Test
    public void testEncodeFilename() {
        String expectedFilename = "A file";

        Map<String, Object> propertyMap = DataSourceProperties.encodeFilename(expectedFilename);
        assertEquals(1, propertyMap.size());

        String actualFilename = DataSourceProperties.decodeFilename(propertyMap);
        assertEquals(expectedFilename, actualFilename);
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceProperties#getPassword()}.
     */
    @Test
    public void testPassword() {
        DataSourceProperties dsp = new DataSourceProperties(null);

        assertFalse(dsp.hasPassword());
        String expectedPassword = "top secret password";
        dsp.setPassword(expectedPassword);
        assertTrue(dsp.hasPassword());
        assertEquals(expectedPassword, dsp.getPassword());
    }
}
