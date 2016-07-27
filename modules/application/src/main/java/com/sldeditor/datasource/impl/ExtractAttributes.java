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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.geotools.styling.UserLayerImpl;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sldeditor.common.DataSourceFieldInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.datasource.DataSourceField;
import com.sldeditor.filter.v2.function.FunctionManager;

/**
 * Class that extracts all data source fields from an SLD file.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExtractAttributes {

    /** The Constant OGC_NAMESPACE. */
    private static final String OGC_NAMESPACE = "ogc";

    /** The Constant OGC_NAMESPACE_URL. */
    private static final String OGC_NAMESPACE_URL = "http://www.opengis.net/ogc";

    /** The Constant SLD_NAMESPACE. */
    private static final String SLD_NAMESPACE = "sld";

    /** The Constant SLD_NAMESPACE_URL. */
    private static final String SLD_NAMESPACE_URL = "http://www.opengis.net/sld";

    /** The Constant WELL_KNOWN_NAME. */
    private static final String WELL_KNOWN_NAME = "WellKnownName";

    /** The Constant GEOMETRY_FIELD. */
    private static final String GEOMETRY_FIELD = "Geometry";

    /** The Constant FUNCTION. */
    private static final String FUNCTION = "Function";

    /** The Constant FUNCTION_NAME. */
    private static final String FUNCTION_NAME = "name";

    /** The Constant PROPERTY_NAME. */
    private static final String PROPERTY_NAME = "PropertyName";

    /** The namespace map. */
    private static Map<String, String> namespaceMap = null;

    /** The processed field list. */
    private List<DataSourceFieldInterface> processedFieldList = new ArrayList<DataSourceFieldInterface>();

    /** The geometry field list. */
    private List<String> geometryFieldList = new ArrayList<String>();

    /**
     * Populate namespace map.
     */
    private static void populateNamespaceMap()
    {
        if(namespaceMap == null)
        {
            namespaceMap = new HashMap<String, String>();

            namespaceMap.put(OGC_NAMESPACE_URL, OGC_NAMESPACE);
            namespaceMap.put(SLD_NAMESPACE_URL, SLD_NAMESPACE);
        }
    }

    /**
     * Extract the default fields.
     *
     * @param b the feature type builder
     * @param sld the sld
     */
    public void extractDefaultFields(SimpleFeatureTypeBuilder b, StyledLayerDescriptor sld)
    {
        // Remove inline features
        String sldContents = preprocessSLD(sld);

        if((sldContents != null) && (b != null))
        {
            try
            {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(sldContents));
                Document doc = builder.parse(is);

                Map<String, List<String>> namespacePrefixes = getNamespacePrefixes(doc);

                extractSimpleAttributes(b, doc, namespacePrefixes, processedFieldList, geometryFieldList);
                extractWKTAttributes(b, doc, namespacePrefixes, processedFieldList);
            }
            catch(IOException e)
            {
                ConsoleManager.getInstance().exception(ExtractAttributes.class, e);
            } catch (SAXException e) {
                ConsoleManager.getInstance().exception(ExtractAttributes.class, e);
            } catch (ParserConfigurationException e) {
                ConsoleManager.getInstance().exception(ExtractAttributes.class, e);
            }
        }
    }

    /**
     * Take a copy of the StyledLayerDescriptor and null out the inline features
     *
     * @param sld the original sld
     * @return the string contents without the inline features
     */
    private String preprocessSLD(StyledLayerDescriptor sld) {
        if(sld == null)
        {
            return null;
        }
        SLDWriterInterface sldWriter = SLDWriterFactory.createWriter(null);

        DuplicatingStyleVisitor duplicator = new DuplicatingStyleVisitor();
        sld.accept(duplicator);
        StyledLayerDescriptor sldCopy = (StyledLayerDescriptor)duplicator.getCopy();

        for(StyledLayer styledLayer : sldCopy.layers())
        {
            if(styledLayer instanceof UserLayer)
            {
                UserLayerImpl userLayer = (UserLayerImpl) styledLayer;
                userLayer.setInlineFeatureDatastore(null);
                userLayer.setInlineFeatureType(null);
            }
        }

        String sldContents = sldWriter.encodeSLD(sldCopy);
        return sldContents;
    }

    /**
     * Gets the fields.
     *
     * @return the fields
     */
    public List<DataSourceFieldInterface> getFields()
    {
        return processedFieldList;
    }

    /**
     * Gets the geometry fields.
     *
     * @return the geometry fields
     */
    public List<String> getGeometryFields()
    {
        return geometryFieldList;
    }

    /**
     * Extract wkt attributes.
     *
     * @param b the b
     * @param doc the doc
     * @param namespacePrefixes the namespace prefixes
     * @param processedFieldList the processed field list
     */
    private static void extractWKTAttributes(SimpleFeatureTypeBuilder b, Document doc,
            Map<String, List<String>> namespacePrefixes,
            List<DataSourceFieldInterface> processedFieldList) {
        // Get node list for all possible namespace prefixes
        List<NodeList> completeNodeList = getNodeList(doc, namespacePrefixes, SLD_NAMESPACE, WELL_KNOWN_NAME);

        for(NodeList nodeList : completeNodeList)
        {
            for(int index = 0; index < nodeList.getLength(); index ++)
            {
                Node node = nodeList.item(index);

                String contents = node.getTextContent();

                System.out.println(contents);
            }
        }
    }

    /**
     * Extract simple attributes.
     *
     * @param b the feature type builder
     * @param doc the doc
     * @param namespacePrefixes the namespace prefixes
     * @param processedFieldList the processed field list
     * @param geometryList the geometry list
     */
    private static void extractSimpleAttributes(SimpleFeatureTypeBuilder b,
            Document doc,
            Map<String, List<String>> namespacePrefixes,
            List<DataSourceFieldInterface> processedFieldList,
            List<String> geometryList)
    {
        if(geometryList == null)
        {
            return;
        }

        if(processedFieldList == null)
        {
            return;
        }

        if(doc == null)
        {
            return;
        }

        // Get node list for all possible namespace prefixes
        List<NodeList> completeNodeList = getNodeList(doc, namespacePrefixes, OGC_NAMESPACE, PROPERTY_NAME);

        boolean addField = false;

        for(NodeList nodeList : completeNodeList)
        {
            for(int index = 0; index < nodeList.getLength(); index ++)
            {
                Node node = nodeList.item(index);

                String fieldName = node.getTextContent();

                if(!fieldExists(processedFieldList, fieldName))
                {
                    Class<?> fieldType = String.class; // We don't now any better at the moment

                    Node parent = node.getParentNode();

                    NamespaceHelper namespace = new NamespaceHelper(parent);
                    addField = true;

                    if(namespace.isElement(namespacePrefixes.get(OGC_NAMESPACE), FUNCTION))
                    {
                        Node parentAgain = parent.getParentNode();
                        if(parentAgain != null)
                        {
                            NamespaceHelper geometryNamespace = new NamespaceHelper(parentAgain);

                            if(geometryNamespace.isElement(namespacePrefixes.get(SLD_NAMESPACE), GEOMETRY_FIELD))
                            {
                                addField = false;
                            }
                        }

                        if(addField)
                        {
                            Element e = (Element) parent;
                            String functionName = e.getAttribute(FUNCTION_NAME);

                            Class<?> tmpFieldType = FunctionManager.getInstance().getFunctionType(functionName);
                            if(tmpFieldType != null)
                            {
                                fieldType = tmpFieldType;
                            }
                        }
                    }
                    else if(namespace.isElement(namespacePrefixes.get(SLD_NAMESPACE), GEOMETRY_FIELD))
                    {
                        addField = false;
                        geometryList.add(fieldName);
                    }

                    if(addField)
                    {
                        DataSourceFieldInterface field = new DataSourceField(fieldName, fieldType);
                        processedFieldList.add(field);

                        b.add(fieldName, fieldType);
                    }
                }
            }
        }
    }

    /**
     * Gets the node list.
     *
     * @param doc the doc
     * @param namespacePrefixes the namespace prefixes used in the document
     * @param namespacePrefix the namespace prefix to find
     * @param elementName the element name
     * @return the node list
     */
    private static List<NodeList> getNodeList(Document doc, 
            Map<String, List<String>> namespacePrefixes, 
            String namespacePrefix,
            String elementName) {
        List<NodeList> completeNodeList = new ArrayList<NodeList>();

        List<String> prefixList = namespacePrefixes.get(namespacePrefix);
        for(String prefix : prefixList)
        {
            String tagName = NamespaceHelper.encode(prefix, elementName);
            NodeList nodeList = doc.getElementsByTagName(tagName);
            completeNodeList.add(nodeList);
        }

        return completeNodeList;
    }

    /**
     * Gets the namespace prefixes.
     *
     * @param doc the doc
     * @return the namespace prefix map
     */
    private static Map<String, List<String>> getNamespacePrefixes(Document doc) {

        populateNamespaceMap();

        Map<String, List<String>> prefixMap = new HashMap<String, List<String>>();

        NamedNodeMap attributeMap = doc.getDocumentElement().getAttributes();

        for(int index = 0; index < attributeMap.getLength(); index ++)
        {
            Node node = attributeMap.item(index);

            String namespaceURI = node.getNodeValue();
            if(namespaceMap.containsKey(namespaceURI))
            {
                String prefixConstant = namespaceMap.get(namespaceURI);

                String[] components = node.getNodeName().split(":");
                String prefix = "";
                if(components.length == 2)
                {
                    prefix = components[1];
                }

                List<String> prefixList = prefixMap.get(prefixConstant);
                if(prefixList == null)
                {
                    prefixList = new ArrayList<String>();
                }
                prefixList.add(prefix);
                prefixMap.put(prefixConstant, prefixList);
            }
        }
        return prefixMap;
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
