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
package com.sldeditor.datasource.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sldeditor.common.DataSourceFieldInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.datasource.DataSourceField;
import com.sldeditor.filter.v2.function.FunctionManager;

/**
 * Class that extracts all data source fields from an SLD file
 * 
 * @author Robert Ward (SCISYS)
 */
public class ExtractAttributes {

    private static final String OGC_FUNCTION_NAME = "name";

    private static final String WELL_KNOWN_NAME = "sld:WellKnownName";

    /** The Constant OGC_FUNCTION. */
    private static final String OGC_FUNCTION = "ogc:Function";

    /** The Constant OGC_PROPERTY_NAME. */
    private static final String OGC_PROPERTY_NAME = "ogc:PropertyName";

    /**
     * Adds the default fields.
     *
     * @param b the feature type builder
     * @param encodedSLD the encoded sld
     */
    public static List<DataSourceFieldInterface> addDefaultFields(SimpleFeatureTypeBuilder b, String encodedSLD) {

        List<DataSourceFieldInterface> processedFieldList = new ArrayList<DataSourceFieldInterface>();

        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(encodedSLD));
            Document doc = builder.parse(is);

            extractSimpleAttributes(b, doc, processedFieldList);
            extractWKTAttributes(b, doc, processedFieldList);
        }
        catch(IOException e)
        {
            ConsoleManager.getInstance().exception(ExtractAttributes.class, e);
        } catch (SAXException e) {
            ConsoleManager.getInstance().exception(ExtractAttributes.class, e);
        } catch (ParserConfigurationException e) {
            ConsoleManager.getInstance().exception(ExtractAttributes.class, e);
        }

        return processedFieldList;
    }

    /**
     * Extract wkt attributes.
     *
     * @param b the b
     * @param doc the doc
     * @param processedFieldList the processed field list
     */
    private static void extractWKTAttributes(SimpleFeatureTypeBuilder b, Document doc,
            List<DataSourceFieldInterface> processedFieldList) {
        NodeList nodeList = doc.getElementsByTagName(WELL_KNOWN_NAME);

        for(int index = 0; index < nodeList.getLength(); index ++)
        {
            Node node = nodeList.item(index);

            String contents = node.getTextContent();

            System.out.println(contents);
        }
    }

    /**
     * Extract simple attributes.
     *
     * @param b the feature type builder
     * @param doc the doc
     * @param processedFieldList the processed field list
     */
    private static void extractSimpleAttributes(SimpleFeatureTypeBuilder b, Document doc,
            List<DataSourceFieldInterface> processedFieldList) {
        NodeList nodeList = doc.getElementsByTagName(OGC_PROPERTY_NAME);

        for(int index = 0; index < nodeList.getLength(); index ++)
        {
            Node node = nodeList.item(index);

            String fieldName = node.getTextContent();

            if(!fieldExists(processedFieldList, fieldName))
            {
                Class<?> fieldType = String.class; // We don't now any better at the moment

                Node parent = node.getParentNode();

                String propName = parent.getNodeName();

                if(propName.compareTo(OGC_FUNCTION) == 0)
                {
                    propName = parent.getParentNode().getNodeName();

                    Element e = (Element) parent;
                    String functionName = e.getAttribute(OGC_FUNCTION_NAME);

                    Class<?> tmpFieldType = FunctionManager.getInstance().getFunctionType(functionName);
                    if(tmpFieldType != null)
                    {
                        fieldType = tmpFieldType;
                    }
                }

                DataSourceFieldInterface field = new DataSourceField(fieldName, fieldType);
                processedFieldList.add(field);

                b.add(fieldName, fieldType);
            }
        }
    }

    /**
     * Check to see if the field name already exists.
     *
     * @param processedFieldList the processed field list
     * @param fieldName the field name
     * @return true, if successful
     */
    private static boolean fieldExists(List<DataSourceFieldInterface> processedFieldList,
            String fieldName) {
        for(DataSourceFieldInterface field: processedFieldList)
        {
            if(field.getName().compareTo(fieldName) == 0)
            {
                return true;
            }
        }

        return false;
    }
}
