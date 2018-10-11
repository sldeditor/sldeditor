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

package com.sldeditor.test.unit.rendertransformation;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.rendertransformation.FunctionTableModel;
import com.sldeditor.rendertransformation.ProcessFunctionParameterValue;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.opengis.wps10.ProcessBriefType;
import net.opengis.wps10.Wps10Factory;
import net.opengis.wps10.impl.ProcessDescriptionTypeImpl;
import org.geotools.data.Parameter;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.filter.ExpressionDOMParser;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.process.function.ProcessFunction;
import org.geotools.process.function.ProcessFunctionFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Unit tests for FunctionTableModel
 *
 * @author Robert Ward (SCISYS)
 */
class FunctionTableModelTest {

    /** Test all the methods using a ProcessFunction */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void testProcessFunction() {
        FunctionTableModel model = new FunctionTableModel();

        assertEquals(0, model.getRowCount());

        model.addNewValue(0);
        ProcessFunction processFunction = createProcessFunction();

        FunctionName name =
                new FunctionNameImpl(
                        new NameImpl("vec", "PointStacker"),
                        parameter("cellSize", Double.class),
                        new Parameter(
                                "outputBBOX", Number.class, null, null, false, 0, 100, null, null),
                        parameter("outputWidth", Number.class),
                        parameter("outputHeight", Number.class));

        assertFalse(name.getArguments().get(0).isRequired());
        assertTrue(name.getArguments().get(1).isRequired());

        model.populate(name, processFunction);

        assertEquals(3, model.getRowCount());
        assertEquals(4, model.getColumnCount());

        // Get value
        assertEquals("outputBBOX", model.getValueAt(0, 0));
        assertEquals(Number.class.getSimpleName(), model.getValueAt(0, 1));
        assertEquals(true, model.getValueAt(0, 2));
        assertEquals("env([wms_bbox])", model.getValueAt(0, 3));
        assertNull(model.getValueAt(0, 4));

        // Is editable
        assertFalse(model.isCellEditable(0, 0));
        assertFalse(model.isCellEditable(0, 1));
        assertTrue(model.isCellEditable(0, FunctionTableModel.getOptionalColumn()));
        assertFalse(model.isCellEditable(0, 3));
        assertFalse(model.isCellEditable(0, 4));

        // Set value
        model.setValueAt(true, 0, 2);
        assertTrue((Boolean) model.getValueAt(0, FunctionTableModel.getOptionalColumn()));
        model.setValueAt(false, 0, 2);
        assertFalse((Boolean) model.getValueAt(0, FunctionTableModel.getOptionalColumn()));

        // Get row
        assertNull(model.getValue(-1));
        assertNull(model.getValue(10));

        // Add a new value
        assertEquals(0, model.getNoOfOccurences(null));
        ProcessFunctionParameterValue value = model.getValue(0);
        assertEquals(1, model.getNoOfOccurences(value));
        model.addNewValue(0);
        assertEquals(4, model.getRowCount());

        assertEquals(2, model.getNoOfOccurences(value));

        // Remove value
        model.removeValue(3);
        assertEquals(3, model.getRowCount());

        // Get expression
        ProcessFunction actualFunction = model.getExpression(null);
        assertNull(actualFunction);

        model.setValueAt(true, 0, FunctionTableModel.getOptionalColumn());

        ProcessFunctionFactory factory = new ProcessFunctionFactory();
        actualFunction = model.getExpression(factory);
        assertNotNull(actualFunction);

        // Update expression
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        Expression expression = ff.literal(4.2);
        model.update(expression, 0);
        model.update(null, 1);
    }

    /**
     * Test all the methods using a ProcessBriefType.
     *
     * <p>Not tested because it needs to interact with GeoServer to create a receive a remote custom
     * WPS function.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Disabled
    @Test
    void testProcessBriefType() {
        FunctionTableModel model = new FunctionTableModel();

        assertEquals(0, model.getRowCount());

        model.addNewValue(0);
        ProcessBriefType customFunction = createCustomFunction();

        FunctionName name =
                new FunctionNameImpl(
                        new NameImpl("vec", "PointStacker"),
                        parameter("cellSize", Double.class),
                        new Parameter(
                                "outputBBOX", Number.class, null, null, false, 0, 100, null, null),
                        parameter("outputWidth", Number.class),
                        parameter("outputHeight", Number.class));

        assertFalse(name.getArguments().get(0).isRequired());
        assertTrue(name.getArguments().get(1).isRequired());

        model.populate(customFunction);

        assertEquals(3, model.getRowCount());
        assertEquals(4, model.getColumnCount());

        // Get value
        assertEquals("outputBBOX", model.getValueAt(0, 0));
        assertEquals(Number.class.getSimpleName(), model.getValueAt(0, 1));
        assertEquals(true, model.getValueAt(0, 2));
        assertEquals("env([wms_bbox])", model.getValueAt(0, 3));
        assertNull(model.getValueAt(0, 4));

        // Is editable
        assertFalse(model.isCellEditable(0, 0));
        assertFalse(model.isCellEditable(0, 1));
        assertTrue(model.isCellEditable(0, FunctionTableModel.getOptionalColumn()));
        assertFalse(model.isCellEditable(0, 3));
        assertFalse(model.isCellEditable(0, 4));

        // Set value
        model.setValueAt(true, 0, 2);
        assertTrue((Boolean) model.getValueAt(0, FunctionTableModel.getOptionalColumn()));
        model.setValueAt(false, 0, 2);
        assertFalse((Boolean) model.getValueAt(0, FunctionTableModel.getOptionalColumn()));

        // Get row
        assertNull(model.getValue(-1));
        assertNull(model.getValue(10));

        // Add a new value
        assertEquals(0, model.getNoOfOccurences(null));
        ProcessFunctionParameterValue value = model.getValue(0);
        assertEquals(1, model.getNoOfOccurences(value));
        model.addNewValue(0);
        assertEquals(4, model.getRowCount());

        assertEquals(2, model.getNoOfOccurences(value));

        // Remove value
        model.removeValue(3);
        assertEquals(3, model.getRowCount());

        // Get expression
        ProcessFunction actualFunction = model.getExpression(null);
        assertNull(actualFunction);

        model.setValueAt(true, 0, FunctionTableModel.getOptionalColumn());

        ProcessFunctionFactory factory = new ProcessFunctionFactory();
        actualFunction = model.getExpression(factory);
        assertNotNull(actualFunction);

        // Update expression
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        Expression expression = ff.literal(4.2);
        model.update(expression, 0);
        model.update(null, 1);
    }

    /**
     * Creates the custom function.
     *
     * @return the process brief type
     */
    private ProcessBriefType createCustomFunction() {
        ProcessDescriptionTypeImpl processDescription =
                (ProcessDescriptionTypeImpl) Wps10Factory.eINSTANCE.createProcessDescriptionType();

        return processDescription;
    }

    /**
     * Creates the process function.
     *
     * @return the process function
     */
    private ProcessFunction createProcessFunction() {
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
        return processFunction;
    }
}
