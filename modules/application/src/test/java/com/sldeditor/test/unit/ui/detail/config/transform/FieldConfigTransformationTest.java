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

package com.sldeditor.test.unit.ui.detail.config.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.detail.config.transform.FieldConfigTransformation;
import com.sldeditor.ui.detail.config.transform.ParameterFunctionUtils;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.ExpressionDOMParser;
import org.geotools.process.function.ProcessFunction;
import org.junit.jupiter.api.Test;
import org.opengis.filter.expression.Expression;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The unit test for FieldConfigTransformation.
 *
 * <p>{@link com.sldeditor.ui.detail.config.transform.FieldConfigTransformation}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigTransformationTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.transform.FieldConfigTransformation#internal_setEnabled(boolean)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.transform.FieldConfigTransformation#isEnabled()}. Test method
     * for {@link com.sldeditor.ui.detail.config.transform.FieldConfigTransformation#createUI()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigTransformation field =
                new FieldConfigTransformation(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "edit",
                        "clear");

        // Text field will not have been created
        boolean expectedValue = true;
        field.internal_setEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Create text field
        field.createUI();
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.internal_setEnabled(expectedValue);

        assertEquals(expectedValue, field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigTransformation field2 =
                new FieldConfigTransformation(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "edit",
                        "clear");

        // Text field will not have been created
        expectedValue = true;
        field2.internal_setEnabled(expectedValue);
        assertFalse(field2.isEnabled());

        // Create text field
        field2.createUI();
        field2.createUI();

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = false;
        field2.internal_setEnabled(expectedValue);

        // Actual value is coming from the attribute panel, not the text field
        assertEquals(expectedValue, field2.isEnabled());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.transform.FieldConfigTransformation#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigTransformation field =
                new FieldConfigTransformation(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "edit",
                        "clear");

        boolean expectedValue = true;
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.transform.FieldConfigTransformation#populateExpression(java.lang.Object)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.transform.FieldConfigTransformation#generateExpression()}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.transform.FieldConfigTransformation#getProcessFunction()}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;

        class TestFieldConfigTransformation extends FieldConfigTransformation {
            public TestFieldConfigTransformation(
                    FieldConfigCommonData commonData,
                    String editButtonText,
                    String clearButtonText) {
                super(commonData, editButtonText, clearButtonText);
            }

            public Expression callGenerateExpression() {
                return generateExpression();
            }
        }

        TestFieldConfigTransformation field =
                new TestFieldConfigTransformation(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "edit",
                        "clear");
        Expression actualExpression = field.callGenerateExpression();
        assertNull(actualExpression);

        field.createUI();
        String expectedValue1 = "test string value";
        field.setTestValue(FieldIdEnum.UNKNOWN, expectedValue1);
        actualExpression = field.callGenerateExpression();
        assertNull(actualExpression);

        // Strings are ignored when calling populateExpression
        String expectedValue2 = "test string value as expression";
        field.populateExpression(expectedValue2);
        actualExpression = field.callGenerateExpression();
        assertNull(actualExpression);

        // Create process function
        String testData =
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
                        + "<StyledLayerDescriptor version=\"1.0.0\" xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" xmlns=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                        + "<ogc:Function name=\"vec:PointStacker\">"
                        + "<ogc:Function name=\"parameter\">"
                        + "  <ogc:Literal>data</ogc:Literal>"
                        + "</ogc:Function>"
                        + "<ogc:Function name=\"parameter\">"
                        + "  <ogc:Literal>cellSize</ogc:Literal>"
                        + "  <ogc:Literal>30</ogc:Literal>"
                        + "</ogc:Function>"
                        + "<ogc:Function name=\"parameter\">"
                        + "  <ogc:Literal>outputBBOX</ogc:Literal>"
                        + "  <ogc:Function name=\"env\">"
                        + "        <ogc:Literal>wms_bbox</ogc:Literal>"
                        + "  </ogc:Function>"
                        + "</ogc:Function>"
                        + "<ogc:Function name=\"parameter\">"
                        + "  <ogc:Literal>outputWidth</ogc:Literal>"
                        + "  <ogc:Function name=\"env\">"
                        + "        <ogc:Literal>wms_width</ogc:Literal>"
                        + "  </ogc:Function>"
                        + "</ogc:Function>"
                        + "<ogc:Function name=\"parameter\">"
                        + " <ogc:Literal>outputHeight</ogc:Literal>"
                        + "  <ogc:Function name=\"env\">"
                        + "        <ogc:Literal>wms_height</ogc:Literal>"
                        + "  </ogc:Function>"
                        + " </ogc:Function>"
                        + "</ogc:Function>"
                        + "</StyledLayerDescriptor>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        ProcessFunction processFunction = null;
        try {
            builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(testData));
            Document doc = builder.parse(is);
            ExpressionDOMParser parser =
                    new ExpressionDOMParser(CommonFactoryFinder.getFilterFactory2(null));
            processFunction =
                    (ProcessFunction) parser.expression(doc.getDocumentElement().getFirstChild());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        field.populateExpression((ProcessFunction) null);
        field.populateExpression(processFunction);

        actualExpression = field.callGenerateExpression();
        String expectedValue3 = ParameterFunctionUtils.getString(processFunction);
        String string = actualExpression.toString();
        assertTrue(expectedValue3.compareTo(string) != 0);
        assertEquals(processFunction, field.getProcessFunction());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.transform.FieldConfigTransformation#revertToDefaultValue()}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.transform.FieldConfigTransformation#setDefaultValue(java.lang.String)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.transform.FieldConfigTransformation#getStringValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigTransformation field =
                new FieldConfigTransformation(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "edit",
                        "clear");

        String expectedDefaultValue = "default value";
        field.setDefaultValue(expectedDefaultValue);
        field.revertToDefaultValue();
        assertNull(field.getStringValue());

        field.createUI();
        field.revertToDefaultValue();
        assertTrue(expectedDefaultValue.compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.transform.FieldConfigTransformation#populateField(java.lang.String)}.
     */
    @Test
    public void testPopulateFieldString() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.transform.FieldConfigTransformation#populateField(org.geotools.process.function.ProcessFunction)}.
     */
    @Test
    public void testPopulateFieldProcessFunction() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.transform.FieldConfigTransformation#setTestValue(com.sldeditor.ui.detail.config.FieldId,
     * java.lang.String)}.
     */
    @Test
    public void testSetTestValueFieldIdString() {
        boolean valueOnly = true;
        FieldConfigTransformation field =
                new FieldConfigTransformation(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "edit",
                        "clear");

        String expectedTestValue = "test value";
        field.setTestValue(FieldIdEnum.ANCHOR_POINT_V, expectedTestValue);
        assertNull(field.getStringValue());

        field.createUI();
        field.setTestValue(FieldIdEnum.ANCHOR_POINT_V, expectedTestValue);
        assertTrue(expectedTestValue.compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.transform.FieldConfigTransformation#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigTransformation extends FieldConfigTransformation {

            public TestFieldConfigTransformation(
                    FieldConfigCommonData commonData,
                    String editButtonText,
                    String clearButtonText) {
                super(commonData, editButtonText, clearButtonText);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase) {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigTransformation field =
                new TestFieldConfigTransformation(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "edit",
                        "clear");
        FieldConfigTransformation copy = (FieldConfigTransformation) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigTransformation) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.transform.FieldConfigTransformation#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigTransformation field =
                new FieldConfigTransformation(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "edit",
                        "clear");

        field.attributeSelection("field");
        // Does nothing
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.transform.FieldConfigTransformation#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.transform.FieldConfigTransformation#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        boolean valueOnly = true;
        FieldConfigTransformation field =
                new FieldConfigTransformation(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "edit",
                        "clear");

        field.undoAction(null);
        field.redoAction(null);

        field.createUI();
        field.undoAction(null);
        field.redoAction(null);

        String expectedTestValue = "test value";
        field.setTestValue(FieldIdEnum.UNKNOWN, expectedTestValue);
        assertTrue(expectedTestValue.compareTo(field.getStringValue()) == 0);

        String expectedUndoTestValue = "undo value";
        String expectedRedoTestValue = "redo value";

        UndoEvent undoEvent =
                new UndoEvent(
                        null, FieldIdEnum.UNKNOWN, expectedUndoTestValue, expectedRedoTestValue);
        field.undoAction(undoEvent);
        assertTrue(expectedUndoTestValue.compareTo(field.getStringValue()) == 0);

        field.redoAction(undoEvent);
        assertTrue(expectedRedoTestValue.compareTo(field.getStringValue()) == 0);

        // Increase code coverage status
        undoEvent =
                new UndoEvent(null, FieldIdEnum.UNKNOWN, Integer.valueOf(0), Double.valueOf(10.0));
        field.undoAction(undoEvent);
        field.redoAction(undoEvent);
    }
}
