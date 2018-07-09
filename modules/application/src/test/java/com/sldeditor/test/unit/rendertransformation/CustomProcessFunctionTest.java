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
import static org.junit.Assert.assertTrue;

import com.sldeditor.rendertransformation.CustomProcessFunction;
import com.sldeditor.rendertransformation.ProcessFunctionParameterValue;
import java.math.BigInteger;
import java.util.List;
import net.opengis.ows11.AllowedValuesType;
import net.opengis.ows11.CodeType;
import net.opengis.ows11.DomainMetadataType;
import net.opengis.ows11.ValueType;
import net.opengis.ows11.impl.Ows11FactoryImpl;
import net.opengis.wps10.DataInputsType;
import net.opengis.wps10.InputDescriptionType;
import net.opengis.wps10.LiteralInputType;
import net.opengis.wps10.ProcessDescriptionType;
import net.opengis.wps10.SupportedCRSsType;
import net.opengis.wps10.SupportedComplexDataInputType;
import net.opengis.wps10.impl.Wps10FactoryImpl;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;

/**
 * Unit test for CustomProcessFunction class.
 *
 * <p>{@link com.sldeditor.rendertransformation.CustomProcessFunction}
 *
 * @author Robert Ward (SCISYS)
 */
public class CustomProcessFunctionTest {

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.CustomProcessFunction#extractParameters(net.opengis.wps10.ProcessDescriptionType)}.
     */
    @Test
    public void testExtractParameters() {
        CustomProcessFunction obj = new CustomProcessFunction();

        obj.extractParameters(null);

        ProcessFunctionParameterValue value = createProcessDescription("xs:int", "1234", 0, 1);
        assertEquals("1234", value.objectValue.getExpression().toString());
        value = createProcessDescription("xs:boolean", "true", 1, 1);
        assertEquals("true", value.objectValue.getExpression().toString());
        value = createProcessDescription("xs:double", "3.141", 1, 2);
        assertTrue(
                Math.abs(
                                Double.valueOf("3.141")
                                        - Double.valueOf(
                                                value.objectValue.getExpression().toString()))
                        < 0.001);
        value = createProcessDescription("xs:float", "1.234", 0, 1);
        assertTrue(
                Math.abs(
                                Float.valueOf("1.234")
                                        - Float.valueOf(
                                                value.objectValue.getExpression().toString()))
                        < 0.001);
        value = createProcessDescription("xs:long", "9876", 1, 1);
        assertEquals("9876", value.objectValue.getExpression().toString());
        value = createProcessDescription("xs:xxx", "string", 1, 1);
        assertEquals("string", value.objectValue.getExpression().toString());
    }

    /**
     * Creates the process description.
     *
     * @param type the type
     * @param defaultValue the default value
     * @param minOccurs the min occurs
     * @param maxOccurs the max occurs
     * @return the process function parameter value
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private ProcessFunctionParameterValue createProcessDescription(
            String type, String defaultValue, int minOccurs, int maxOccurs) {
        ProcessDescriptionType process = Wps10FactoryImpl.init().createProcessDescriptionType();

        CodeType codeType = Ows11FactoryImpl.init().createCodeType();
        codeType.setValue("JTS:area");
        process.setIdentifier(codeType);

        CodeType codeType2 = Ows11FactoryImpl.init().createCodeType();
        codeType2.setValue("dummyParameter");
        InputDescriptionType inputDescription =
                Wps10FactoryImpl.init().createInputDescriptionType();
        inputDescription.setIdentifier(codeType2);
        inputDescription.setMinOccurs(BigInteger.valueOf(minOccurs));
        inputDescription.setMaxOccurs(BigInteger.valueOf(maxOccurs));
        LiteralInputType literal = Wps10FactoryImpl.init().createLiteralInputType();
        DomainMetadataType domainType = Ows11FactoryImpl.init().createDomainMetadataType();
        domainType.setValue(type);
        literal.setDefaultValue(defaultValue);
        literal.setDataType(domainType);
        inputDescription.setLiteralData(literal);

        DataInputsType dataInputs = Wps10FactoryImpl.init().createDataInputsType();
        EList dataInputList = dataInputs.getInput();
        dataInputList.add(inputDescription);
        process.setDataInputs(dataInputs);

        CustomProcessFunction obj = new CustomProcessFunction();

        List<ProcessFunctionParameterValue> valueList = obj.extractParameters(process);

        assertEquals(type, 1, valueList.size());
        ProcessFunctionParameterValue value = valueList.get(0);
        assertEquals(type, minOccurs, value.minOccurences);
        assertEquals(type, maxOccurs, value.maxOccurences);
        return value;
    }

    /** Test the process description enumeration values. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void createProcessDescriptionEnum() {
        ProcessDescriptionType process = Wps10FactoryImpl.init().createProcessDescriptionType();

        CodeType codeType = Ows11FactoryImpl.init().createCodeType();
        codeType.setValue("JTS:area");
        process.setIdentifier(codeType);

        InputDescriptionType inputDescription =
                Wps10FactoryImpl.init().createInputDescriptionType();

        CodeType codeType2 = Ows11FactoryImpl.init().createCodeType();
        codeType2.setValue("dummyParameter");
        inputDescription.setIdentifier(codeType2);
        inputDescription.setMinOccurs(BigInteger.valueOf(1));
        inputDescription.setMaxOccurs(BigInteger.valueOf(1));
        AllowedValuesType allowedValues = Ows11FactoryImpl.init().createAllowedValuesType();
        EList allowedValueList = allowedValues.getValue();
        ValueType item1 = Ows11FactoryImpl.init().createValueType();
        item1.setValue("item 1");
        allowedValueList.add(item1);
        ValueType item2 = Ows11FactoryImpl.init().createValueType();
        item2.setValue("item 2");
        allowedValueList.add(item2);
        ValueType item3 = Ows11FactoryImpl.init().createValueType();
        item1.setValue("item 3");
        allowedValueList.add(item3);
        LiteralInputType literal = Wps10FactoryImpl.init().createLiteralInputType();
        literal.setAllowedValues(allowedValues);
        inputDescription.setLiteralData(literal);

        DataInputsType dataInputs = Wps10FactoryImpl.init().createDataInputsType();

        EList dataInputList = dataInputs.getInput();
        dataInputList.add(inputDescription);
        process.setDataInputs(dataInputs);

        CustomProcessFunction obj = new CustomProcessFunction();

        List<ProcessFunctionParameterValue> valueList = obj.extractParameters(process);

        assertEquals(1, valueList.size());
        ProcessFunctionParameterValue value = valueList.get(0);
        assertEquals(1, value.minOccurences);
        assertEquals(1, value.maxOccurences);
    }

    /** Test the process description boundaing box values. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void createProcessDescriptionBBox() {
        ProcessDescriptionType process = Wps10FactoryImpl.init().createProcessDescriptionType();

        CodeType codeType = Ows11FactoryImpl.init().createCodeType();
        codeType.setValue("JTS:area");
        process.setIdentifier(codeType);

        InputDescriptionType inputDescription =
                Wps10FactoryImpl.init().createInputDescriptionType();

        CodeType codeType2 = Ows11FactoryImpl.init().createCodeType();
        codeType2.setValue("dummyParameter");
        inputDescription.setIdentifier(codeType2);
        inputDescription.setMinOccurs(BigInteger.valueOf(1));
        inputDescription.setMaxOccurs(BigInteger.valueOf(1));

        DataInputsType dataInputs = Wps10FactoryImpl.init().createDataInputsType();
        EList dataInputList = dataInputs.getInput();
        dataInputList.add(inputDescription);
        process.setDataInputs(dataInputs);

        SupportedCRSsType crs = Wps10FactoryImpl.init().createSupportedCRSsType();
        inputDescription.setBoundingBoxData(crs);

        CustomProcessFunction obj = new CustomProcessFunction();

        List<ProcessFunctionParameterValue> valueList = obj.extractParameters(process);

        assertEquals(1, valueList.size());
        ProcessFunctionParameterValue value = valueList.get(0);
        assertEquals(1, value.minOccurences);
        assertEquals(1, value.maxOccurences);
        assertTrue(value.dataType.compareTo("BBOX") == 0);

        SupportedComplexDataInputType complex =
                Wps10FactoryImpl.init().createSupportedComplexDataInputType();
        inputDescription.setComplexData(complex);
        inputDescription.setBoundingBoxData(null);

        valueList = obj.extractParameters(process);

        assertEquals(1, valueList.size());
        value = valueList.get(0);
        assertEquals(1, value.minOccurences);
        assertEquals(1, value.maxOccurences);
        assertTrue(value.dataType.compareTo("Geometry") == 0);
    }
}
