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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.rendertransformation.CheckBoxRenderer;
import com.sldeditor.rendertransformation.FunctionTableModel;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.geotools.data.Parameter;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.ExpressionDOMParser;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.process.function.ProcessFunction;
import org.junit.jupiter.api.Test;
import org.opengis.filter.capability.FunctionName;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Unit test for CheckBoxRenderer
 *
 * @author Robert Ward (SCISYS)
 */
class CheckBoxRendererTest {

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.CheckBoxRenderer#CheckBoxRenderer(com.sldeditor.rendertransformation.FunctionTableModel)}.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void testCheckBoxRenderer() {
        JTable table = new JTable();
        FunctionTableModel model = new FunctionTableModel();

        assertNull(
                new CheckBoxRenderer(null)
                        .getTableCellRendererComponent(null, null, false, false, 0, 0));

        assertNull(
                new CheckBoxRenderer(null)
                        .getTableCellRendererComponent(table, null, false, false, 0, 0));
        assertNull(
                new CheckBoxRenderer(model)
                        .getTableCellRendererComponent(null, null, false, false, 0, 0));

        model.addNewValue(0);

        ProcessFunction processFunction = createProcessFunction();

        FunctionName name =
                new FunctionNameImpl(
                        "Test",
                        parameter("cellSize", Double.class),
                        new Parameter(
                                "outputBBOX", Number.class, null, null, false, 0, 100, null, null),
                        parameter("outputWidth", Number.class),
                        parameter("outputHeight", Number.class));

        assertFalse(name.getArguments().get(0).isRequired());
        assertTrue(name.getArguments().get(1).isRequired());

        model.populate(name, processFunction);

        CheckBoxRenderer obj = new CheckBoxRenderer(model);

        // Row - optional and not selected
        assertEquals(obj, obj.getTableCellRendererComponent(table, null, false, false, 0, 0));
        assertEquals(obj, obj.getTableCellRendererComponent(table, true, false, false, 0, 0));
        assertTrue(obj.isSelected());
        assertEquals(obj, obj.getTableCellRendererComponent(table, false, false, false, 0, 0));
        assertFalse(obj.isSelected());

        // Row - optional and selected
        assertEquals(obj, obj.getTableCellRendererComponent(table, null, true, false, 0, 0));
        assertEquals(obj, obj.getTableCellRendererComponent(table, true, true, false, 0, 0));
        assertTrue(obj.isSelected());
        assertEquals(obj, obj.getTableCellRendererComponent(table, false, true, false, 0, 0));
        assertFalse(obj.isSelected());
        // Row - required and not selected
        assertEquals(
                JLabel.class,
                obj.getTableCellRendererComponent(table, null, false, false, 1, 0).getClass());
        // Row - required and selected
        assertEquals(
                JLabel.class,
                obj.getTableCellRendererComponent(table, null, true, false, 1, 0).getClass());
    }

    protected ProcessFunction createProcessFunction() {
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
