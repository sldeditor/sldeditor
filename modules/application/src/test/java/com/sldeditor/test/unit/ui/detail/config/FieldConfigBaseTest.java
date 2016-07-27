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

package com.sldeditor.test.unit.ui.detail.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.Box;
import javax.swing.JPanel;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.function.DefaultFunctionFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.function.ProcessFunction;
import org.geotools.styling.ColorMap;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.Font;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.Controller;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.v2.function.temporal.TimePeriod;
import com.sldeditor.ui.attribute.AttributeSelection;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.base.GroupConfig;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.iface.ExpressionUpdateInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.widgets.ExpressionTypeEnum;

/**
 * The unit test for FieldConfigBase.
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigBase}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigBaseTest {

    class TestFieldConfigBase extends FieldConfigBase
    {

        public TestFieldConfigBase(Class<?> panelId, FieldId fieldId, String label,
                boolean valueOnly) {
            super(panelId, fieldId, label, valueOnly);
        }

        @Override
        public String getStringValue() {
            return null;
        }

        @Override
        public void attributeSelection(String field) {
        }

        @Override
        public void setEnabled(boolean enabled) {
        }

        @Override
        public void setVisible(boolean visible) {
        }

        @Override
        protected Expression generateExpression() {
            return null;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public void revertToDefaultValue() {
        }

        @Override
        public void populateExpression(Object objValue) {
        }

        @Override
        public Class<?> getClassType() {
            return null;
        }

        @Override
        public void createUI(Box parentBox) {
        }

        @Override
        protected FieldConfigBase createCopy(FieldConfigBase fieldConfigBase) {
            FieldConfigString copy = null;

            if(fieldConfigBase != null)
            {
                copy = new FieldConfigString(fieldConfigBase.getPanelId(),
                        fieldConfigBase.getFieldId(),
                        fieldConfigBase.getLabel(),
                        fieldConfigBase.isValueOnly(),
                        "");
            }
            return copy;
        }

        public void testAddCustomPanel(JPanel customPanel)
        {
            addCustomPanel(customPanel);
        }

        public void testSetValueFieldState()
        {
            setValueFieldState();
        }

        public void testFireDataChanged()
        {
            fireDataChanged();
        }

        public void testAttributeSelectionPanel(AttributeSelection attributeSelectionPanel)
        {
            setAttributeSelectionPanel(attributeSelectionPanel);
        }

        public AttributeSelection getAttributeSelectionPanel()
        {
            return attributeSelectionPanel;
        }

        public void testFireExpressionUpdated(Expression expression)
        {
            fireExpressionUpdated(expression);
        }
    }

    class TestUpdateSymbolInterface implements UpdateSymbolInterface
    {
        private boolean called = false;

        @Override
        public void dataChanged(FieldId changedField) {
            called = true;
        }

        public boolean hasBeenCalled()
        {
            boolean tmp = called;
            called = false;

            return tmp;
        }
    }

    class TestExpressionUpdateInterface implements ExpressionUpdateInterface 
    {
        private boolean valueUpdated = false;
        private boolean attributeUpdated = false;
        private boolean expressionUpdated = false;
        private boolean functionUpdated = false;

        @Override
        public void valueUpdated() {
            valueUpdated = true;
        }

        @Override
        public void attributeUpdated(String attributeName) {
            attributeUpdated = true;
        }

        @Override
        public void expressionUpdated(Expression updatedExpression) {
            expressionUpdated = true;
        }

        @Override
        public void functionUpdated(Expression updatedExpression) {
            functionUpdated = true;
        }

        public boolean hasValueBeenCalled()
        {
            boolean tmp = valueUpdated;
            valueUpdated = false;

            return tmp;
        }

        public boolean hasAttributeBeenCalled()
        {
            boolean tmp = attributeUpdated;
            attributeUpdated = false;

            return tmp;
        }

        public boolean hasExpressionBeenCalled()
        {
            boolean tmp = expressionUpdated;
            expressionUpdated = false;

            return tmp;
        }

        public boolean hasFunctionBeenCalled()
        {
            boolean tmp = functionUpdated;
            functionUpdated = false;

            return tmp;
        }
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#getFieldId()}.
     */
    @Test
    public void testGetFieldId() {
        boolean valueOnly = true;
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, "test label", valueOnly);

        assertEquals(expectedFieldId, field.getFieldId());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#getLabel()}.
     */
    @Test
    public void testGetLabel() {
        boolean valueOnly = true;
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, valueOnly);

        assertTrue(expectedLabel.compareTo(field.getLabel()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#isValueOnly()}.
     */
    @Test
    public void testIsValueOnly() {
        boolean valueOnly = true;
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, valueOnly);

        assertEquals(valueOnly, field.isValueOnly());

        valueOnly = false;
        field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, valueOnly);

        assertEquals(valueOnly, field.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#getPanel()}.
     */
    @Test
    public void testGetPanel() {
        boolean valueOnly = true;
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, valueOnly);

        assertNull(field.getPanel());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#addCustomPanel(javax.swing.JPanel)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#getCustomPanels()}.
     */
    @Test
    public void testGetCustomPanels() {
        boolean valueOnly = true;
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, valueOnly);

        assertNull(field.getCustomPanels());

        JPanel expectedPanel = new JPanel();
        field.testAddCustomPanel(expectedPanel);

        List<Component> customPanelList = field.getCustomPanels();
        assertEquals(1, customPanelList.size());
        assertEquals(expectedPanel, customPanelList.get(0));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#setValueFieldState()}.
     */
    @Test
    public void testSetValueFieldState() {
        boolean valueOnly = true;
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, valueOnly);

        AttributeSelection attributeSelectionPanel = new AttributeSelection(String.class, field);
        attributeSelectionPanel.setEnabled(true);
        field.testAttributeSelectionPanel(attributeSelectionPanel);
        field.testSetValueFieldState();
        assertFalse(field.isEnabled());
        assertTrue(field.getAttributeSelectionPanel().isEnabled());

        boolean disable = false;
        field.setFieldStateOverride(disable);
        field.testSetValueFieldState();
        assertEquals(disable, field.isEnabled());
        assertEquals(disable, !field.getAttributeSelectionPanel().isEnabled());

        disable = true;
        field.setFieldStateOverride(disable);
        field.testSetValueFieldState();
        assertEquals(disable, !field.isEnabled());
        assertEquals(disable, !field.getAttributeSelectionPanel().isEnabled());

        field.attributeUpdated("");
        assertEquals(disable, !field.isEnabled());
        assertEquals(disable, !field.getAttributeSelectionPanel().isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#fireDataChanged()}.
     */
    @Test
    public void testFireDataChanged() {
        boolean valueOnly = true;
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, valueOnly);

        TestUpdateSymbolInterface listener = new TestUpdateSymbolInterface();
        field.addDataChangedListener(listener);

        assertFalse(listener.hasBeenCalled());

        Controller.getInstance().setPopulating(true);
        field.testFireDataChanged();
        assertFalse(listener.hasBeenCalled());

        Controller.getInstance().setPopulating(false);
        field.testFireDataChanged();
        assertTrue(listener.hasBeenCalled());

        // Leave the is populating flag as false otherwise a load of tests will fail
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#valueUpdated()}.
     */
    @Test
    public void testValueUpdated() {
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, false);

        TestUpdateSymbolInterface listener = new TestUpdateSymbolInterface();
        field.addDataChangedListener(listener);

        assertFalse(listener.hasBeenCalled());
        field.valueUpdated();
        assertTrue(listener.hasBeenCalled());
        assertEquals(ExpressionTypeEnum.E_VALUE, field.getExpressionType());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#attributeUpdated(java.lang.String)}.
     */
    @Test
    public void testAttributeUpdated() {
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, false);

        TestUpdateSymbolInterface listener = new TestUpdateSymbolInterface();
        field.addDataChangedListener(listener);

        String attributeName = "test attribute";
        assertFalse(listener.hasBeenCalled());
        field.attributeUpdated(attributeName);
        assertTrue(listener.hasBeenCalled());

        assertEquals(ExpressionTypeEnum.E_ATTRIBUTE, field.getExpressionType());
        Expression expression = field.getExpression();
        assertTrue(expression instanceof AttributeExpressionImpl);
        assertTrue(attributeName.compareTo(expression.toString()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#setIndentColumn(int)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#getIndentColumn()}.
     */
    @Test
    public void testSetIndentColumn() {
        boolean valueOnly = true;
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, valueOnly);

        int expectedColumn = 42;

        field.setIndentColumn(expectedColumn);
        assertEquals(expectedColumn, field.getIndentColumn());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#expressionUpdated(org.opengis.filter.expression.Expression)}.
     */
    @Test
    public void testExpressionUpdated() {
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, false);

        TestUpdateSymbolInterface listener = new TestUpdateSymbolInterface();
        field.addDataChangedListener(listener);

        String expressionName = "test expression";
        assertFalse(listener.hasBeenCalled());
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        Expression testExpression = ff.literal(expressionName);
        field.expressionUpdated(testExpression);
        assertTrue(listener.hasBeenCalled());

        assertEquals(ExpressionTypeEnum.E_EXPRESSION, field.getExpressionType());
        Expression expression = field.getExpression();
        assertTrue(expressionName.compareTo(expression.toString()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#functionUpdated(org.opengis.filter.expression.Expression)}.
     */
    @Test
    public void testFunctionUpdated() {
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, false);

        TestUpdateSymbolInterface listener = new TestUpdateSymbolInterface();
        field.addDataChangedListener(listener);

        assertFalse(listener.hasBeenCalled());
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        DefaultFunctionFactory functionFactory = new DefaultFunctionFactory();
        FunctionName functionName = functionFactory.getFunctionNames().get(0);

        Expression testExpression = ff.function(functionName.getFunctionName(), (Expression)null);
        field.functionUpdated(testExpression);
        assertTrue(listener.hasBeenCalled());

        assertEquals(ExpressionTypeEnum.E_FUNCTION, field.getExpressionType());
        Expression expression = field.getExpression();
        assertTrue(expression.toString().startsWith(functionName.getName()));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#populate(org.opengis.filter.expression.Expression)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#populate(org.opengis.filter.expression.Expression, org.opengis.filter.expression.Expression)}.
     */
    @Test
    public void testPopulateExpressionExpression() {
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, false);

        AttributeSelection attributeSelectionPanel = new AttributeSelection(String.class, field);
        field.testAttributeSelectionPanel(attributeSelectionPanel);

        TestUpdateSymbolInterface listener = new TestUpdateSymbolInterface();
        field.addDataChangedListener(listener);

        assertFalse(listener.hasBeenCalled());
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        // Test function
        DefaultFunctionFactory functionFactory = new DefaultFunctionFactory();
        FunctionName functionName = functionFactory.getFunctionNames().get(0);

        Expression testExpression = ff.function(functionName.getFunctionName(), (Expression)null);
        field.populate(testExpression);
        // Updated because the attribute pulldown changed
        assertTrue(listener.hasBeenCalled());

        assertEquals(ExpressionTypeEnum.E_EXPRESSION, field.getExpressionType());
        Expression expression = field.getExpression();
        assertTrue(expression.toString().startsWith(functionName.getName()));

        // Attribute expression wrapped in a literal expression
        String testAttributeName = "test attribute";
        NameImpl name = new NameImpl(testAttributeName);
        AttributeExpressionImpl attributeExpression = new AttributeExpressionImpl(name);

        Expression literalExpression = ff.literal(attributeExpression);
        field.populate(literalExpression);
        assertEquals(ExpressionTypeEnum.E_ATTRIBUTE, field.getExpressionType());

        // Process Function
        //        ProcessFunctionFactory factory = new ProcessFunctionFactory();
        //        FunctionTableModel functionParameterTableModel = new FunctionTableModel();
        //        ProcessFunction processFunction = functionParameterTableModel.getExpression(factory);
        //        field.populate(processFunction);
        //        assertEquals(ExpressionTypeEnum.E_VALUE, field.getExpressionType());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#isASingleValue()}.
     */
    @Test
    public void testIsASingleValue() {
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, false);

        assertTrue(field.isASingleValue());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#setExpressionUpdateListener(com.sldeditor.ui.iface.ExpressionUpdateInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#fireExpressionUpdated(org.opengis.filter.expression.Expression)}.
     */
    @Test
    public void testFireExpressionUpdated() {
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, false);

        TestExpressionUpdateInterface testExpressionUpdate = new TestExpressionUpdateInterface();

        field.setExpressionUpdateListener(null);
        field.testFireExpressionUpdated(null);
        field.setExpressionUpdateListener(testExpressionUpdate);
        field.testFireExpressionUpdated(null);
        assertTrue(testExpressionUpdate.hasExpressionBeenCalled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#populateField(java.lang.String)}.
     */
    @Test
    public void testPopulateFieldString() {
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, false);

        field.populateField("");
        field.setTestValue(expectedFieldId, "");

        field.populateField(42);
        field.setTestValue(expectedFieldId, 42);
        assertEquals(0, field.getIntValue());

        field.populateField(3.142);
        field.setTestValue(expectedFieldId, 3.142);
        assertTrue(Math.abs(field.getDoubleValue()) < 0.0001);

        field.populateField(new Date());
        field.populateField((ReferencedEnvelope)null);
        field.setTestValue(expectedFieldId, (ReferencedEnvelope)null);
        field.populateField((Id)null);
        field.populateField((TimePeriod)null);
        field.populateField((ProcessFunction)null);
        assertNull(field.getProcessFunction());

        field.populateField(true);
        field.setTestValue(expectedFieldId, true);
        assertEquals(false, field.getBooleanValue());

        field.populateField((ColorMap)null);
        field.setTestValue(expectedFieldId, (ColorMap)null);
        assertNull(field.getColourMap());

        field.populateField((List<FeatureTypeConstraint>)null);
        field.setTestValue(expectedFieldId, (List<FeatureTypeConstraint>)null);
        assertNull(field.getFeatureTypeConstraint());

        field.populateField((Font)null);
        assertNull(field.getFont());

        field.setTestValue(expectedFieldId, (Expression)null);

        assertNull(field.getEnumValue());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#duplicate()}.
     */
    @Test
    public void testDuplicate() {
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, false);
        field.setIndentColumn(42);

        TestUpdateSymbolInterface listener = new TestUpdateSymbolInterface();
        field.addDataChangedListener(listener);

        FieldConfigString copy = (FieldConfigString) field.duplicate();
        assertNotNull(copy);
        assertEquals(field.getIndentColumn(), copy.getIndentColumn());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#addFunction(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#getNoOfFunctionFields()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#getFunctionFields()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#getAllFunctionFields()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#removeFunctionFields()}.
     */
    @Test
    public void testAddFunction() {
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, false);

        field.addFunction(null);
        assertEquals(0, field.getNoOfFunctionFields());
        assertEquals(0, field.getAllFunctionFields().size());

        FieldConfigString functionField = new FieldConfigString(String.class, new FieldId(FieldIdEnum.NAME), "test label", false, "button text");

        field.addFunction(functionField);
        assertEquals(1, field.getNoOfFunctionFields());
        assertEquals(functionField, field.getFunctionFields().get(0));
        assertEquals(1, field.getAllFunctionFields().size());

        field.removeFunctionFields();
        assertEquals(0, field.getNoOfFunctionFields());
        assertEquals(0, field.getAllFunctionFields().size());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#setGroupComponents(java.util.List)}.
     */
    @Test
    public void testSetGroupComponents() {
        List<GroupConfigInterface> groupConfigList = new ArrayList<GroupConfigInterface>();
        groupConfigList.add(new GroupConfig());

        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, false);

        field.setGroupComponents(groupConfigList);
        field.removeFunctionFields();
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#setFunctionParameterType(java.lang.Class)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#getFunctionParameterType()}.
     */
    @Test
    public void testSetFunctionParameterType() {
        FieldId expectedFieldId = new FieldId(FieldIdEnum.NAME);
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(String.class, expectedFieldId, expectedLabel, false);

        Class<Double> expectedClass = Double.class;
        field.setFunctionParameterType(expectedClass);
        assertEquals(expectedClass, field.getFunctionParameterType());
    }

}
