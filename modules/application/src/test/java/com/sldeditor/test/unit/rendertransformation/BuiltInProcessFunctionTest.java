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

package com.sldeditor.test.unit.rendertransformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.ExpressionDOMParser;
import org.geotools.process.function.ProcessFunction;
import org.geotools.process.function.ProcessFunctionFactory;
import org.junit.Test;
import org.opengis.filter.capability.FunctionName;
import org.opengis.parameter.Parameter;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sldeditor.rendertransformation.BuiltInProcessFunction;
import com.sldeditor.rendertransformation.ProcessFunctionParameterValue;

/**
 * Unit test for BuiltInProcessFunction class.
 * <p>{@link com.sldeditor.rendertransformation.BuiltInProcessFunction}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class BuiltInProcessFunctionTest {

    /**
     * Test method for {@link com.sldeditor.rendertransformation.BuiltInProcessFunction#extractParameters(org.opengis.filter.capability.FunctionName, org.geotools.process.function.ProcessFunction)}.
     */
    @Test
    public void testExtractParametersFunctionName() {
        BuiltInProcessFunction obj = new BuiltInProcessFunction();

        List<ProcessFunctionParameterValue> valueList = obj.extractParameters(null, null);

        assertTrue(valueList.isEmpty());

        ProcessFunctionFactory processFunctionFactory = new ProcessFunctionFactory();
        List<FunctionName> functionNameList = processFunctionFactory.getFunctionNames();

        for(FunctionName functionName : functionNameList)
        {
            valueList = obj.extractParameters(functionName, null);

            assertEquals(functionName.getArguments().size(), valueList.size());
            for(int index = 0; index < functionName.getArguments().size(); index ++)
            {
                Parameter<?> expectedParameter = functionName.getArguments().get(index);

                ProcessFunctionParameterValue actualParameter = valueList.get(index);

                assertTrue(functionName.getName(), expectedParameter.getName().compareTo(actualParameter.name) == 0);
                if(expectedParameter.getType().isEnum())
                {
                    assertEquals(functionName.getName(), StringBuilder.class, actualParameter.type);
                }
                else
                {
                    assertEquals(functionName.getName(), expectedParameter.getType(), actualParameter.type);
                    if(expectedParameter.getDefaultValue() != null)
                    {
                        assertNotNull(functionName.getName(), actualParameter.value);
                    }
                }
                assertTrue(functionName.getName(), expectedParameter.getType().getSimpleName().compareTo(actualParameter.dataType) == 0);
                assertEquals(functionName.getName(), !expectedParameter.isRequired(), actualParameter.optional);
                assertEquals(functionName.getName(), expectedParameter.getMinOccurs(), actualParameter.minOccurences);
                assertEquals(functionName.getName(), expectedParameter.getMaxOccurs(), actualParameter.maxOccurences);
            }
        }
    }

    /**
     * Test method for {@link com.sldeditor.rendertransformation.BuiltInProcessFunction#extractParameters(org.opengis.filter.capability.FunctionName, org.geotools.process.function.ProcessFunction)}.
     */
    @Test
    public void testExtractParametersProcessFunction() {
        BuiltInProcessFunction obj = new BuiltInProcessFunction();
        String testData = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
                "<StyledLayerDescriptor version=\"1.0.0\" xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" xmlns=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                "<ogc:Function name=\"vec:PointStacker\">" +
                "<ogc:Function name=\"parameter\">" +
                "  <ogc:Literal>data</ogc:Literal>" +
                "</ogc:Function>" +
                "<ogc:Function name=\"parameter\">" +
                "  <ogc:Literal>cellSize</ogc:Literal>" +
                "  <ogc:Literal>30</ogc:Literal>" +
                "</ogc:Function>" +
                "<ogc:Function name=\"parameter\">" +
                "  <ogc:Literal>outputBBOX</ogc:Literal>" +
                "  <ogc:Function name=\"env\">" +
                "        <ogc:Literal>wms_bbox</ogc:Literal>" +
                "  </ogc:Function>" +
                "</ogc:Function>" +
                "<ogc:Function name=\"parameter\">" +
                "  <ogc:Literal>outputWidth</ogc:Literal>" +
                "  <ogc:Function name=\"env\">" +
                "        <ogc:Literal>wms_width</ogc:Literal>" +
                "  </ogc:Function>" +
                "</ogc:Function>" +
                "<ogc:Function name=\"parameter\">" +
                " <ogc:Literal>outputHeight</ogc:Literal>" +
                "  <ogc:Function name=\"env\">" +
                "        <ogc:Literal>wms_height</ogc:Literal>" +
                "  </ogc:Function>" +
                " </ogc:Function>" +
                "</ogc:Function>" +
                "</StyledLayerDescriptor>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        ProcessFunction tx = null;
        try {
            builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(testData));
            Document doc = builder.parse(is);
            ExpressionDOMParser parser = new ExpressionDOMParser(
                    CommonFactoryFinder.getFilterFactory2(null));
            tx = (ProcessFunction) parser.expression(doc.getDocumentElement().getFirstChild());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<ProcessFunctionParameterValue> valueList = obj.extractParameters(null, tx);
        assertTrue(valueList.isEmpty());

        ProcessFunctionFactory processFunctionFactory = new ProcessFunctionFactory();
        for(FunctionName functionName : processFunctionFactory.getFunctionNames())
        {
            if(functionName.getName().compareTo("PointStacker") == 0)
            {
                valueList = obj.extractParameters(functionName, tx);
                assertEquals(functionName.getArguments().size(), valueList.size());
            }
        }
    }
}
