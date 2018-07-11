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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.rendertransformation.SelectedProcessFunction;
import java.math.BigInteger;
import java.util.List;
import net.opengis.ows11.CodeType;
import net.opengis.ows11.DomainMetadataType;
import net.opengis.ows11.LanguageStringType;
import net.opengis.ows11.impl.Ows11FactoryImpl;
import net.opengis.wps10.DataInputsType;
import net.opengis.wps10.InputDescriptionType;
import net.opengis.wps10.LiteralInputType;
import net.opengis.wps10.ProcessDescriptionType;
import net.opengis.wps10.impl.Wps10FactoryImpl;
import org.eclipse.emf.common.util.EList;
import org.geotools.process.function.ProcessFunctionFactory;
import org.junit.jupiter.api.Test;
import org.opengis.filter.capability.FunctionName;

/**
 * Unit test for SelectedProcessFunction class.
 *
 * <p>{@link com.sldeditor.rendertransformation.SelectedProcessFunction}
 *
 * @author Robert Ward (SCISYS)
 */
public class SelectedProcessFunctionTest {

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.SelectedProcessFunction#setBuiltInProcessFunction(org.opengis.filter.capability.FunctionName,
     * org.geotools.process.function.ProcessFunction)}.
     */
    @Test
    public void testSetBuiltInProcessFunction() {

        SelectedProcessFunction obj = new SelectedProcessFunction();
        assertTrue(obj.isBuiltInSelected());
        assertTrue(obj.extractParameters().isEmpty());
        assertNull(obj.getFunctionName());
        assertEquals(0, obj.getRowCount());

        ProcessFunctionFactory processFunctionFactory = new ProcessFunctionFactory();
        List<FunctionName> functionNameList = processFunctionFactory.getFunctionNames();
        FunctionName functionName = functionNameList.get(0);
        obj.setBuiltInProcessFunction(functionName, null);
        assertTrue(obj.isBuiltInSelected());
        assertTrue(obj.getFunctionName().getLocalPart().compareTo(functionName.getName()) == 0);
        assertEquals(obj.getRowCount(), obj.extractParameters().size());
        assertFalse(obj.extractParameters().isEmpty());
    }

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.SelectedProcessFunction#setSelectedCustomFunction(net.opengis.wps10.ProcessDescriptionType)}.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testSetSelectedCustomFunction() {
        ProcessDescriptionType process = Wps10FactoryImpl.init().createProcessDescriptionType();

        CodeType codeType = Ows11FactoryImpl.init().createCodeType();
        String expectedFunctionName = "JTS:area";
        codeType.setValue(expectedFunctionName);
        process.setIdentifier(codeType);

        InputDescriptionType inputDescription =
                Wps10FactoryImpl.init().createInputDescriptionType();

        CodeType codeType2 = Ows11FactoryImpl.init().createCodeType();
        codeType2.setValue("dummyParameter");
        inputDescription.setIdentifier(codeType2);
        inputDescription.setMinOccurs(BigInteger.valueOf(1));
        inputDescription.setMaxOccurs(BigInteger.valueOf(1));
        LiteralInputType literal = Wps10FactoryImpl.init().createLiteralInputType();
        DomainMetadataType domainType = Ows11FactoryImpl.init().createDomainMetadataType();
        domainType.setValue("xs:int");
        literal.setDefaultValue("1234");
        literal.setDataType(domainType);
        inputDescription.setLiteralData(literal);
        DataInputsType dataInputs = Wps10FactoryImpl.init().createDataInputsType();
        EList dataInputList = dataInputs.getInput();
        dataInputList.add(inputDescription);
        process.setDataInputs(dataInputs);
        LanguageStringType title = Ows11FactoryImpl.init().createLanguageStringType();
        title.setValue(expectedFunctionName);
        process.setTitle(title);
        SelectedProcessFunction obj = new SelectedProcessFunction();

        obj.setSelectedCustomFunction(process);
        assertFalse(obj.isBuiltInSelected());
        assertTrue(obj.getFunctionName().getLocalPart().compareTo(expectedFunctionName) == 0);
        assertEquals(1, obj.getRowCount());
        assertFalse(obj.extractParameters().isEmpty());

        obj.setSelectedCustomFunction(null);
        assertFalse(obj.isBuiltInSelected());
        assertNull(obj.getFunctionName());
        assertEquals(0, obj.getRowCount());
        assertTrue(obj.extractParameters().isEmpty());
    }

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.SelectedProcessFunction#extractLocalFunctionName(java.lang.String)}.
     */
    @Test
    public void testExtractLocalFunctionName() {
        assertNull(SelectedProcessFunction.extractLocalFunctionName(null));

        String expected1 = "abcdef";
        String result = SelectedProcessFunction.extractLocalFunctionName(expected1);
        assertTrue(expected1.compareTo(result) == 0);

        result = SelectedProcessFunction.extractLocalFunctionName("abc:def");
        assertTrue("def".compareTo(result) == 0);

        String expected3 = "ab:c:def";
        result = SelectedProcessFunction.extractLocalFunctionName(expected3);
        assertTrue(expected3.compareTo(result) == 0);
    }
}
