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
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
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
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigString;
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

        public TestFieldConfigBase(FieldConfigCommonData commonData) {
            super(commonData);
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
        public void createUI() {
        }

        @Override
        protected FieldConfigBase createCopy(FieldConfigBase fieldConfigBase) {
            FieldConfigString copy = null;

            if(fieldConfigBase != null)
            {
                copy = new FieldConfigString(fieldConfigBase.getCommonData(),
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
        public void dataChanged(FieldIdEnum changedField) {
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
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        TestFieldConfigBase field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, "test label", valueOnly));

        assertEquals(expectedFieldId, field.getFieldId());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#getLabel()}.
     */
    @Test
    public void testGetLabel() {
        boolean valueOnly = true;
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, expectedLabel, valueOnly));

        assertTrue(expectedLabel.compareTo(field.getLabel()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#isValueOnly()}.
     */
    @Test
    public void testIsValueOnly() {
        boolean valueOnly = true;
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, expectedLabel, valueOnly));

        assertEquals(valueOnly, field.isValueOnly());

        valueOnly = false;
        field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, expectedLabel, valueOnly));

        assertEquals(valueOnly, field.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#getPanel()}.
     */
    @Test
    public void testGetPanel() {
        boolean valueOnly = true;
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, expectedLabel, valueOnly));

        assertNull(field.getPanel());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#addCustomPanel(javax.swing.JPanel)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#getCustomPanels()}.
     */
    @Test
    public void testGetCustomPanels() {
        boolean valueOnly = true;
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, expectedLabel, valueOnly));

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
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, expectedLabel, valueOnly));

        AttributeSelection attributeSelectionPanel = AttributeSelection.createAttributes(String.class, field, false);
        attributeSelectionPanel.setEnabled(true);
        field.testAttributeSelectionPanel(attributeSelectionPanel);
        field.testSetValueFieldState();
        assertFalse(field.isEnabled());
        assertTrue(field.getAttributeSelectionPanel().isEnabled());

        boolean fieldEnabledFlag = false;
        field.testSetValueFieldState();
        assertEquals(fieldEnabledFlag, field.isEnabled());

        field.attributeUpdated("");
        assertEquals(fieldEnabledFlag, field.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#fireDataChanged()}.
     */
    @Test
    public void testFireDataChanged() {
        boolean valueOnly = true;
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, expectedLabel, valueOnly));

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
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, expectedLabel, false));

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
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, expectedLabel, false));

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
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#expressionUpdated(org.opengis.filter.expression.Expression)}.
     */
    @Test
    public void testExpressionUpdated() {
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, expectedLabel, false));

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
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, expectedLabel, false));

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
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, expectedLabel, false));

        AttributeSelection attributeSelectionPanel = AttributeSelection.createAttributes(String.class, field, false);
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
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, expectedLabel, false));

        assertTrue(field.isASingleValue());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#setExpressionUpdateListener(com.sldeditor.ui.iface.ExpressionUpdateInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#fireExpressionUpdated(org.opengis.filter.expression.Expression)}.
     */
    @Test
    public void testFireExpressionUpdated() {
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, expectedLabel, false));

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
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, expectedLabel, false));

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
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, expectedLabel, false));

        TestUpdateSymbolInterface listener = new TestUpdateSymbolInterface();
        field.addDataChangedListener(listener);

        FieldConfigString copy = (FieldConfigString) field.duplicate();
        assertNotNull(copy);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBase#addUI(java.awt.Component,int, int, int)}.
     */
    @Test
    public void testAddUI() {
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        String expectedLabel = "test label";
        TestFieldConfigBase field = new TestFieldConfigBase(new FieldConfigCommonData(String.class, expectedFieldId, expectedLabel, false));

        field.addUI(null, 10, 10, 10);

        field.createUI();
        field.addUI(new JButton(), 10, 10, 10);
    }
}
