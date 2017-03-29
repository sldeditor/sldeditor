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

package com.sldeditor.test;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sldeditor.TreeSelectionData;
import com.sldeditor.common.xml.ParseXML;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.SLDXPath;
import com.sldeditor.common.xml.ui.SelectedTreeItemEnum;
import com.sldeditor.common.xml.ui.XMLFieldBase;
import com.sldeditor.common.xml.ui.XMLSetFieldAttribute;
import com.sldeditor.common.xml.ui.XMLSetFieldLiteralBoolean;
import com.sldeditor.common.xml.ui.XMLSetFieldLiteralDouble;
import com.sldeditor.common.xml.ui.XMLSetFieldLiteralInt;
import com.sldeditor.common.xml.ui.XMLSetFieldLiteralString;
import com.sldeditor.common.xml.ui.XMLTestSection;
import com.sldeditor.common.xml.ui.XMLTestSectionField;

/**
 * The Class SLDOutputTest.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDOutputTest {

    private static final String XPATH_CHILD_ELEMENT_VALUE = "/child::*";

    /** The XML document factory. */
    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    /** The XPathFactory instance. */
    private XPathFactory xPathfactory = XPathFactory.newInstance();

    /** The Constant OUTPUT_SCHEMA_RESOURCE. */
    private static final String OUTPUT_SCHEMA_RESOURCE = "/xsd/testoutput.xsd";

    /** The x path map. */
    private Map<SelectedTreeItemEnum, Map<FieldIdEnum, String>> xPathMap =
            new HashMap<SelectedTreeItemEnum, Map<FieldIdEnum, String>>();

    /** The prefix map. */
    private Map<SelectedTreeItemEnum, String> prefixMap = 
            new HashMap<SelectedTreeItemEnum, String>();

    /**
     * Instantiates a new SLD output test.
     */
    public SLDOutputTest() {
        initialise();
    }

    /**
     * Initialise.
     */
    private void initialise() {

        String fullPath = "/test/SLDXPath.xml";

        SLDXPath xPath = (SLDXPath) ParseXML.parseFile("", fullPath, OUTPUT_SCHEMA_RESOURCE,
                SLDXPath.class);

        Map<SelectedTreeItemEnum, String> initialPrefixMap = 
                new HashMap<SelectedTreeItemEnum, String>();

        for (XMLTestSection section : xPath.getSection()) {
            initialPrefixMap.put(section.getSldtype(), "/" + section.getPrefix());
            Map<FieldIdEnum, String> fieldMap = new HashMap<FieldIdEnum, String>();

            for (XMLTestSectionField xmlField : section.getField()) {
                fieldMap.put(xmlField.getId(), xmlField.getXpath());
            }

            xPathMap.put(section.getSldtype(), fieldMap);
        }

        prefixMap.put(SelectedTreeItemEnum.SLD, initialPrefixMap.get(SelectedTreeItemEnum.SLD));
        populatePrefixMap(SelectedTreeItemEnum.LAYER, SelectedTreeItemEnum.SLD, initialPrefixMap);
        populatePrefixMap(SelectedTreeItemEnum.STYLE, SelectedTreeItemEnum.LAYER, initialPrefixMap);
        populatePrefixMap(SelectedTreeItemEnum.FEATURETYPESTYLE, SelectedTreeItemEnum.STYLE,
                initialPrefixMap);
        populatePrefixMap(SelectedTreeItemEnum.RULE, SelectedTreeItemEnum.FEATURETYPESTYLE,
                initialPrefixMap);
        populatePrefixMap(SelectedTreeItemEnum.POINT_SYMBOLIZER, SelectedTreeItemEnum.RULE,
                initialPrefixMap);
        populatePrefixMap(SelectedTreeItemEnum.LINE_SYMBOLIZER, SelectedTreeItemEnum.RULE,
                initialPrefixMap);
        populatePrefixMap(SelectedTreeItemEnum.POLYGON_SYMBOLIZER, SelectedTreeItemEnum.RULE,
                initialPrefixMap);
        populatePrefixMap(SelectedTreeItemEnum.TEXT_SYMBOLIZER, SelectedTreeItemEnum.RULE,
                initialPrefixMap);
        populatePrefixMap(SelectedTreeItemEnum.RASTER_SYMBOLIZER, SelectedTreeItemEnum.RULE,
                initialPrefixMap);
        populatePrefixMap(SelectedTreeItemEnum.POINT_FILL, SelectedTreeItemEnum.RULE,
                initialPrefixMap);
        populatePrefixMap(SelectedTreeItemEnum.POLYGON_FILL, SelectedTreeItemEnum.RULE,
                initialPrefixMap);
        populatePrefixMap(SelectedTreeItemEnum.STROKE, SelectedTreeItemEnum.RULE, initialPrefixMap);
    }

    /**
     * Populate prefix map.
     *
     * @param item the item
     * @param parentItem the parent item
     * @param initialPrefixMap the initial prefix map
     */
    private void populatePrefixMap(SelectedTreeItemEnum item, SelectedTreeItemEnum parentItem,
            Map<SelectedTreeItemEnum, String> initialPrefixMap) {
        prefixMap.put(item, prefixMap.get(parentItem) + initialPrefixMap.get(item));
    }

    /**
     * Gets the string.
     *
     * @param sldContentString the sld content string
     * @param selectionData the selection data
     * @param field the field
     * @param suffix the suffix
     * @return the string
     */
    public String getString(String sldContentString, TreeSelectionData selectionData,
            FieldIdEnum field, String suffix) {
        Document doc = getXMLDocument(sldContentString);

        String extractedString = null;

        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr;
        try {
            String xPathString = null;

            SelectedTreeItemEnum selection = selectionData.getSelection();
            String prefix = prefixMap.get(selection);
            Map<FieldIdEnum, String> fieldMap = xPathMap.get(selection);
            if (fieldMap == null) {
                System.err.println("Unknown selected tree item : " + selection);
            } else {
                String configXPathString = fieldMap.get(field);
                if (configXPathString == null) {
                    System.err.println("Unknown XPath : " + field);
                }

                StringBuilder sb = new StringBuilder();
                sb.append(prefix);
                sb.append("/");
                sb.append(configXPathString);

                if (suffix != null) {
                    sb.append(suffix);
                }

                String fieldString = sb.toString();

                switch (selection) {
                case LAYER:
                    xPathString = getLayerString(selectionData, fieldString);
                    break;
                case STYLE:
                    xPathString = getStyleString(selectionData, fieldString);
                    break;
                case POINT_SYMBOLIZER:
                case LINE_SYMBOLIZER:
                case POLYGON_SYMBOLIZER:
                case TEXT_SYMBOLIZER:
                case RASTER_SYMBOLIZER:
                    xPathString = getSymbolizerString(selectionData, fieldString);
                    break;
                case RULE:
                    xPathString = getRuleString(selectionData, fieldString);
                    break;
                case POINT_FILL:
                case POLYGON_FILL:
                    xPathString = getFillString(selectionData, fieldString);
                    break;
                case STROKE:
                    xPathString = getStrokeString(selectionData, fieldString);
                    break;
                default:
                    break;
                }

                if (xPathString != null) {
                    expr = xpath.compile(xPathString);
                    extractedString = expr.evaluate(doc);
                    if ((extractedString == null) || extractedString.isEmpty()) {
                        System.out.println("SLD : " + sldContentString);
                        System.out.println("XPath : " + xPathString);
                    }
                } else {
                    System.out.println("No XPath string");
                }
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return extractedString;
    }

    /**
     * Gets the style string.
     *
     * @param selectionData the selection data
     * @param fieldString the field string
     * @return the style string
     */
    private String getStyleString(TreeSelectionData selectionData, String fieldString) {

        String completedString = String.format(fieldString, selectionData.getLayerIndex() + 1,
                selectionData.getStyleIndex() + 1);
        return completedString;
    }

    /**
     * Gets the rule string.
     *
     * @param selectionData the selection data
     * @param fieldString the field string
     * @return the rule string
     */
    private String getRuleString(TreeSelectionData selectionData, String fieldString) {

        String completedString = String.format(fieldString, selectionData.getLayerIndex() + 1,
                selectionData.getStyleIndex() + 1, selectionData.getFeatureTypeStyleIndex() + 1,
                selectionData.getRuleIndex() + 1);
        return completedString;
    }

    /**
     * Gets the symbolizer string.
     *
     * @param selectionData the selection data
     * @param fieldString the field string
     * @return the symbolizer string
     */
    private String getSymbolizerString(TreeSelectionData selectionData, String fieldString) {

        String completedString = String.format(fieldString, selectionData.getLayerIndex() + 1,
                selectionData.getStyleIndex() + 1, selectionData.getFeatureTypeStyleIndex() + 1,
                selectionData.getRuleIndex() + 1, selectionData.getSymbolizerIndex() + 1);
        return completedString;
    }

    /**
     * Gets the layer string.
     *
     * @param selectionData the selection data
     * @param fieldString the field string
     * @return the layer string
     */
    private String getLayerString(TreeSelectionData selectionData, String fieldString) {

        String completedString = String.format(fieldString, selectionData.getLayerIndex() + 1);
        return completedString;
    }

    /**
     * Gets the fill string.
     *
     * @param selectionData the selection data
     * @param fieldString the field string
     * @return the fill string
     */
    private String getFillString(TreeSelectionData selectionData, String fieldString) {

        String completedString = null;

        if (fieldString == null) {
            System.out.println("Failed");
        } else {
            completedString = String.format(fieldString, selectionData.getLayerIndex() + 1,
                    selectionData.getStyleIndex() + 1, selectionData.getFeatureTypeStyleIndex() + 1,
                    selectionData.getRuleIndex() + 1, selectionData.getSymbolizerIndex() + 1);
        }
        return completedString;
    }

    /**
     * Gets the stroke string.
     *
     * @param selectionData the selection data
     * @param fieldString the field string
     * @return the stroke string
     */
    private String getStrokeString(TreeSelectionData selectionData, String fieldString) {

        String completedString = null;

        if (fieldString == null) {
            System.out.println("Failed");
        } else {
            completedString = String.format(fieldString, selectionData.getLayerIndex() + 1,
                    selectionData.getStyleIndex() + 1, selectionData.getFeatureTypeStyleIndex() + 1,
                    selectionData.getRuleIndex() + 1, selectionData.getSymbolizerIndex() + 1);
        }
        return completedString;
    }

    /**
     * Test value.
     *
     * @param sldContentString the sld content string
     * @param selectionData the selection data
     * @param field the field
     * @param testValue the test value
     * @return true, if successful
     */
    public boolean testValue(String sldContentString, TreeSelectionData selectionData,
            FieldIdEnum field, XMLFieldBase testValue) {
        boolean passed = false;

        String extractedString = getString(sldContentString, selectionData, field, null);

        if (testValue instanceof XMLSetFieldLiteralString) {
            XMLSetFieldLiteralString stringLiteral = (XMLSetFieldLiteralString) testValue;
            String valueToTest = stringLiteral.getValue();

            passed = (valueToTest.compareTo(extractedString) == 0);
            if (!passed) {
                System.out.println(String.format("Value to test : '%s' Extracted : '%s'",
                        valueToTest, extractedString));
            }
        } else if (testValue instanceof XMLSetFieldLiteralDouble) {
            XMLSetFieldLiteralDouble doubleLiteral = (XMLSetFieldLiteralDouble) testValue;
            if ((extractedString != null) && !extractedString.isEmpty()) {
                Double convertedValue = Double.valueOf(extractedString);
                passed = (Math.abs(convertedValue - doubleLiteral.getValue()) < 0.001);
            }

            if (!passed) {
                System.out.println(String.format("Value to test : '%f' Extracted : '%s'",
                        doubleLiteral.getValue(), extractedString));
            }
        } else if (testValue instanceof XMLSetFieldLiteralInt) {
            XMLSetFieldLiteralInt intLiteral = (XMLSetFieldLiteralInt) testValue;
            if ((extractedString != null) && !extractedString.isEmpty()) {
                passed = (Integer.valueOf(extractedString).intValue() == intLiteral.getValue()
                        .intValue());
            }
        } else if (testValue instanceof XMLSetFieldLiteralBoolean) {
            XMLSetFieldLiteralBoolean booleanLiteral = (XMLSetFieldLiteralBoolean) testValue;
            if ((extractedString != null) && !extractedString.isEmpty()) {
                passed = (Boolean.valueOf(extractedString) == booleanLiteral.isValue());
            }
        }

        System.out.println("Checking value : " + field + " " + extractedString + " "
                + (passed ? "PASS" : "FAIL"));

        return passed;
    }

    /**
     * Gets the XML document.
     *
     * @param sldContentString the sld content string
     * @return the XML document
     */
    Document getXMLDocument(String sldContentString) {
        DocumentBuilder builder = null;
        Document doc = null;

        try {
            builder = factory.newDocumentBuilder();

            InputSource is = new InputSource(new StringReader(sldContentString));
            doc = builder.parse(is);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * Test attribute.
     *
     * @param sldContentString the sld content string
     * @param selectionData the selection data
     * @param field the field
     * @param testValue the test value
     * @return true, if successful
     */
    public boolean testAttribute(String sldContentString, TreeSelectionData selectionData,
            FieldIdEnum field, XMLSetFieldAttribute testValue) {
        boolean passed = false;

        String extractedString = getString(sldContentString, selectionData, field,
                XPATH_CHILD_ELEMENT_VALUE);

        String valueToTest = testValue.getAttribute();

        passed = (valueToTest.compareTo(extractedString) == 0);

        System.out.println("Checking attribute : " + field + " " + extractedString + " "
                + (passed ? "PASS" : "FAIL"));

        return passed;
    }
}
