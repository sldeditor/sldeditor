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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.rendertransformation.FunctionTableModel;
import com.sldeditor.rendertransformation.OptionalValueEditor;
import java.io.IOException;
import java.io.StringReader;
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
 * The unit tests for OptionalValueEditor.
 *
 * @author Robert Ward (SCISYS)
 */
class OptionalValueEditorTest {

    class TestOptionalValueEditor extends OptionalValueEditor {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new test optional value editor.
         *
         * @param tableModel the table model
         */
        public TestOptionalValueEditor(FunctionTableModel tableModel) {
            super(tableModel);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.rendertransformation.OptionalValueEditor#setValue()
         */
        @Override
        protected void setValue() {
            super.setValue();
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.OptionalValueEditor#OptionalValueEditor(com.sldeditor.rendertransformation.FunctionTableModel)}.
     */
    @Test
    void testOptionalValueEditor() {
        TestOptionalValueEditor obj = new TestOptionalValueEditor(null);

        obj.setValue();

        assertTrue(obj.isCellEditable(null));
        assertNull(obj.getCellEditorValue());
    }

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.OptionalValueEditor#getCellEditorValue()}.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void testGetCellEditorValue() {
        FunctionTableModel model = new FunctionTableModel();
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

        TestOptionalValueEditor obj = new TestOptionalValueEditor(model);

        assertNotNull(obj.getTableCellEditorComponent(null, null, false, 0, 0));
        assertNotNull(obj.getCellEditorValue());
        obj.setValue();

        assertNull(obj.getTableCellEditorComponent(null, null, false, 1, 0));
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
