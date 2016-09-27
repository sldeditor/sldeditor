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
package com.sldeditor.ui.detail.config;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.ConstantExpression;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.function.ProcessFunction;
import org.geotools.styling.ColorMap;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.Font;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.UserLayer;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.Controller;
import com.sldeditor.common.xml.TestValueVisitor;
import com.sldeditor.filter.v2.function.temporal.TimePeriod;
import com.sldeditor.ui.attribute.AttributeSelection;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.iface.AttributeButtonSelectionInterface;
import com.sldeditor.ui.iface.ExpressionUpdateInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.widgets.ExpressionTypeEnum;
import com.sldeditor.ui.widgets.FieldPanel;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * The Class FieldConfigBase is the base class for all derived FieldConfigxxx classes.
 * <p>
 * Fields are identified by the id field.
 * <p>
 * Handles the following common tasks:
 * <ul>
 * <li>value/attribute/expression drop down list, ({@link com.sldeditor.ui.attribute.AttributeSelection}) </li>
 * <li>setting/getting of values via Expression</li>
 * <li>field enable/disable state</li>
 * <li>firing data changed notifications</li>
 * </ul>
 * 
 * <img src="./doc-files/field.png" />
 * 
 * @author Robert Ward (SCISYS)
 */
public abstract class FieldConfigBase extends FieldConfigCommonData implements FieldConfigValuePopulateInterface, TestValueVisitor, AttributeButtonSelectionInterface, ExpressionUpdateInterface {

    /** The panel. */
    private FieldPanel fieldPanel = null;

    /** The custom panel list. */
    private List<Component> customPanelList = null;

    /** The expression type. */
    private ExpressionTypeEnum expressionType = ExpressionTypeEnum.E_VALUE;

    /** The attribute selection panel, handles the value/attribute/expression drop down list. */
    protected AttributeSelection attributeSelectionPanel = null;

    /** The filter factory. */
    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /** The update symbol listener list, called when a value changes. */
    private List<UpdateSymbolInterface> updateSymbolListenerList = new ArrayList<UpdateSymbolInterface>();

    /** The style factory. */
    private StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    /** The field state override disable. */
    private boolean fieldStateOverrideDisable = false;

    /** The field enabled. */
    private boolean fieldEnabled = true;

    /** The cached expression. */
    private Expression cachedExpression = null;

    /** The expression update listener. */
    private ExpressionUpdateInterface expressionUpdateListener = null;

    /** The parent field config. */
    private FieldConfigBase parentFieldConfig = null;

    /** The indent column. */
    private int indentColumn = 0;

    /** The function list. */
    private List<FieldConfigBase> functionList = new ArrayList<FieldConfigBase>();

    /** The function group list. */
    private List<GroupConfigInterface> functionGroupList = new ArrayList<GroupConfigInterface>();

    /** The function parameter type. */
    private Class<?> functionParameterType = null;

    /**
     * Instantiates a new field config base.
     *
     * @param commonData the common data
     */
    public FieldConfigBase(FieldConfigCommonData commonData) {
        super(commonData);
    }

    /**
     * Sets the field enabled state.
     *
     * @param enabled the new enabled
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Sets the field visibility state.
     *
     * @param visible the new visible state
     */
    public abstract void setVisible(boolean visible);

    /**
     * Generate expression.
     *
     * @return the expression
     */
    protected abstract Expression generateExpression();

    /**
     * Checks if field is enabled.
     *
     * @return true, if is enabled
     */
    public abstract boolean isEnabled();

    /**
     * Reverts to the default value.
     */
    public abstract void revertToDefaultValue();

    /**
     * Populate expression.
     *
     * @param objValue the obj value
     */
    public abstract void populateExpression(Object objValue);

    /**
     * Creates the ui.
     */
    public abstract void createUI();

    /**
     * Gets the common data.
     *
     * @return the common data
     */
    public FieldConfigCommonData getCommonData()
    {
        return (FieldConfigCommonData)this;
    }

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    public FieldPanel getPanel() {
        return fieldPanel;
    }

    /**
     * Gets the custom panels.
     *
     * @return the custom panels
     */
    public List<Component> getCustomPanels()
    {
        return customPanelList;
    }

    /**
     * Adds the custom panel to the list of custom panels.
     *
     * @param customPanel the custom panel
     */
    protected void addCustomPanel(JPanel customPanel)
    {
        // Create the list if it hasn't been created before
        if(customPanelList == null)
        {
            customPanelList = new ArrayList<Component>();
        }

        customPanelList.add(customPanel);
    }

    /**
     * Sets the attribute selection panel.
     *
     * @param attributeSelectionPanel the new attribute selection panel
     */
    protected void setAttributeSelectionPanel(AttributeSelection attributeSelectionPanel) {

        this.attributeSelectionPanel = attributeSelectionPanel;

        if(this.attributeSelectionPanel != null)
        {
            this.attributeSelectionPanel.addListener(this);
            this.attributeSelectionPanel.addExpressionListener(this);
        }
    }

    /**
     * Sets the value field state.
     */
    protected void setValueFieldState() {
        boolean enabled = false;

        if(!fieldStateOverrideDisable)
        {
            enabled = fieldEnabled;
        }

        setEnabled(enabled && (getExpressionType() == ExpressionTypeEnum.E_VALUE));

        if(attributeSelectionPanel != null)
        {
            attributeSelectionPanel.setEnabled(enabled);
        }
    }

    /**
     * Fire data changed.
     */
    protected void fireDataChanged() {
        if(!Controller.getInstance().isPopulating())
        {
            for(UpdateSymbolInterface listener : updateSymbolListenerList)
            {
                listener.dataChanged(getFieldId());
            }
        }
    }

    /**
     * Value updated.
     */
    @Override
    public void valueUpdated()
    {
        expressionType = ExpressionTypeEnum.E_VALUE;

        setCachedExpression(generateExpression());

        setValueFieldState();

        fireDataChanged();
    }

    /**
     * Attribute updated.
     *
     * @param attributeName the attribute name
     */
    @Override
    public void attributeUpdated(String attributeName)
    {
        expressionType = ExpressionTypeEnum.E_ATTRIBUTE;

        NameImpl name = new NameImpl(attributeName);
        setCachedExpression(new AttributeExpressionImpl(name));

        setValueFieldState();
        fireDataChanged();
    }

    /**
     * Sets the indent column.
     *
     * @param indentColumn the new indent column
     */
    public void setIndentColumn(int indentColumn) {
        this.indentColumn = indentColumn;
    }

    /**
     * Gets the indent column.
     *
     * @return the indent column
     */
    public int getIndentColumn() {
        return indentColumn;
    }

    /**
     * Expression updated.
     *
     * @param updatedExpression the updated expression
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.ExpressionUpdateInterface#expressionUpdated(org.opengis.filter.expression.Expression)
     */
    @Override
    public void expressionUpdated(Expression updatedExpression)
    {
        expressionType = ExpressionTypeEnum.E_EXPRESSION;

        setCachedExpression(updatedExpression);

        setValueFieldState();
        fireDataChanged();
    }

    /**
     * Function updated.
     *
     * @param updatedExpression the updated expression
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.ExpressionUpdateInterface#expressionUpdated(org.opengis.filter.expression.Expression)
     */
    @Override
    public void functionUpdated(Expression updatedExpression)
    {
        expressionType = ExpressionTypeEnum.E_FUNCTION;

        setCachedExpression(updatedExpression);

        setValueFieldState();
        fireDataChanged();
    }

    /**
     * Populate.
     *
     * @param expression the expression
     */
    public void populate(Expression expression)
    {
        if(attributeSelectionPanel != null)
        {
            attributeSelectionPanel.populateAttributeComboxBox(expression);
        }

        if(expression == null)
        {
            expressionType = ExpressionTypeEnum.E_VALUE;

            revertToDefaultValue();

            valueUpdated();
        }
        else
        {
            if(expression instanceof LiteralExpressionImpl)
            {
                LiteralExpressionImpl lExpression = (LiteralExpressionImpl) expression;

                Object objValue = lExpression.getValue();

                if(objValue instanceof AttributeExpressionImpl)
                {
                    expressionType = ExpressionTypeEnum.E_ATTRIBUTE;

                    if(attributeSelectionPanel != null)
                    {
                        attributeSelectionPanel.setAttribute((AttributeExpressionImpl)objValue);
                    }

                    setCachedExpression((AttributeExpressionImpl)objValue);
                }
                else
                {
                    expressionType = ExpressionTypeEnum.E_VALUE;

                    populateExpression(objValue);

                    valueUpdated();
                }
            }
            else if(expression instanceof ConstantExpression)
            {
                ConstantExpression cExpression = (ConstantExpression) expression;

                Object objValue = cExpression.getValue();

                expressionType = ExpressionTypeEnum.E_VALUE;

                populateExpression(objValue);

                valueUpdated();
            }
            else if(expression instanceof ProcessFunction)
            {
                ProcessFunction processExpression = (ProcessFunction) expression;

                Object objValue = processExpression;

                expressionType = ExpressionTypeEnum.E_VALUE;

                populateExpression(objValue);

                valueUpdated();
            }
            else if(expression instanceof AttributeExpressionImpl)
            {
                expressionType = ExpressionTypeEnum.E_ATTRIBUTE;

                if(attributeSelectionPanel != null)
                {
                    attributeSelectionPanel.setAttribute(expression);
                }
                else
                {
                    populateExpression(expression);
                }

                setCachedExpression(expression);
            }
            else if(expression instanceof FunctionExpressionImpl)
            {
                expressionType = ExpressionTypeEnum.E_EXPRESSION;

                if(attributeSelectionPanel != null)
                {
                    attributeSelectionPanel.populate(expression);
                }

                setCachedExpression(expression);
            }
            else
            {
                expressionType = ExpressionTypeEnum.E_EXPRESSION;
            }
        }

        setValueFieldState();
    }

    /**
     * Adds the data changed listener.
     *
     * @param listener the listener
     */
    public void addDataChangedListener(UpdateSymbolInterface listener)
    {
        if(!updateSymbolListenerList.contains(listener))
        {
            updateSymbolListenerList.add(listener);
        }
    }

    /**
     * Sets the field state override.
     *
     * @param disable the new field state override
     */
    public void setFieldStateOverride(boolean disable)
    {
        fieldEnabled = !disable;

        setValueFieldState();
    }

    /**
     * Gets the expression.
     *
     * @return the expression
     */
    public Expression getExpression()
    {
        if(getExpressionType() == ExpressionTypeEnum.E_VALUE)
        {
            cachedExpression = generateExpression();
        }
        else
        {
            if(attributeSelectionPanel != null)
            {
                cachedExpression = attributeSelectionPanel.getExpression();
            }
        }
        return cachedExpression;
    }

    /**
     * Gets the style factory.
     *
     * @return the style factory
     */
    protected StyleFactoryImpl getStyleFactory()
    {
        return styleFactory;
    }

    /**
     * Checks if is a single value is legal
     * To be overridden if necessary.
     *
     * @return true, if is a single value
     */
    public boolean isASingleValue()
    {
        return true;
    }

    /**
     * Sets the expression update listener.
     *
     * @param parent the new expression update listener
     */
    public void setExpressionUpdateListener(ExpressionUpdateInterface parent) {
        expressionUpdateListener = parent;
    }

    /**
     * Fire expression updated.
     *
     * @param expression the expression
     */
    protected void fireExpressionUpdated(Expression expression) {
        if(expressionUpdateListener != null)
        {
            expressionUpdateListener.expressionUpdated(expression);
        }
    }

    /**
     * Method called when the field has been selected from a combo box
     * and may need to be initialised
     * 
     * Will be be overridden if necessary.
     */
    public void justSelected() {
    }

    /**
     * Gets the expression type.
     *
     * @return the expression type
     */
    public ExpressionTypeEnum getExpressionType() {
        return expressionType;
    }

    /**
     * Sets the cached expression.
     *
     * @param cachedExpression the new cached expression
     */
    protected void setCachedExpression(Expression cachedExpression) {
        this.cachedExpression = cachedExpression;
    }

    /**
     * Creates the field panel with the supplied label.
     *
     * @param xPos the x pos
     * @param fieldLabel the field label
     * @return the field panel
     */
    protected FieldPanel createFieldPanel(int xPos, String fieldLabel)
    {
        fieldPanel = new FieldPanel(xPos, fieldLabel);

        return fieldPanel;
    }

    /**
     * Creates the field panel.
     *
     * @param xPos the x pos
     * @param height the height
     * @param fieldLabel the field label
     * @return the field panel
     */
    protected FieldPanel createFieldPanel(int xPos, int height, String fieldLabel)
    {
        fieldPanel = new FieldPanel(xPos, fieldLabel, height);

        return fieldPanel;
    }

    /**
     * Gets the filter factory.
     *
     * @return the filter factory
     */
    protected FilterFactory getFilterFactory() {
        return ff;
    }

    /**
     * Sets the parent.
     *
     * @param parentFieldConfig the new parent
     */
    public void setParent(FieldConfigBase parentFieldConfig) {
        this.parentFieldConfig = parentFieldConfig;
    }

    /**
     * Gets the parent field.
     *
     * @return the parent
     */
    public FieldConfigBase getParent()
    {
        return parentFieldConfig;
    }

    /**
     * Populate string field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(String value) {
        // Do nothing
    }

    /**
     * Populate integer field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(Integer value) {
        // Do nothing
    }

    /**
     * Populate double field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(Double value) {
        // Do nothing
    }

    /**
     * Populate date field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(Date value) {
        // Do nothing
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    public void populateField(ReferencedEnvelope value)
    {
        // Do nothing
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    public void populateField(UserLayer value)
    {
        // Do nothing
    }

    /**
     * Populate string field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(Id value) {
        // Do nothing
    }

    /**
     * Populate time period field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(TimePeriod value) {
        // Do nothing
    }

    /**
     * Populate process function field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(ProcessFunction value) {
        // Do nothing
    }

    /**
     * Populate boolean field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(Boolean value) {
        // Do nothing
    }

    /**
     * Populate colourmap field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(ColorMap value) {
        // Do nothing
    }

    /**
     * Populate font field, overridden if necessary.
     *
     * @param value the value
     */
    public void populateField(Font value)
    {
        // Do nothing
    }

    /**
     * Populate feature type constraint map field, overridden if necessary.
     *
     * @param value the value
     */
    public void populateField(List<FeatureTypeConstraint> value)
    {
        // Do nothing
    }

    /**
     * Gets the feature type constraint.
     *
     * @return the feature type constraint
     */
    public List<FeatureTypeConstraint> getFeatureTypeConstraint()
    {
        return null;
    }

    /**
     * Sets the test value, overridden if necessary.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldId fieldId, String testValue) {
        // Do nothing
    }

    /**
     * Sets the test value, overridden if necessary.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldId fieldId, int testValue) {
        // Do nothing
    }

    /**
     * Sets the test value, overridden if necessary.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldId fieldId, double testValue) {
        // Do nothing
    }

    /**
     * Sets the test value, overridden if necessary.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldId fieldId, boolean testValue) {
        // Do nothing
    }

    /**
     * Sets the test value, overridden if necessary.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldId fieldId, ColorMap testValue) {
        // Do nothing
    }

    /**
     * Sets the test value, overridden if necessary.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldId fieldId, List<FeatureTypeConstraint> testValue) {
        // Do nothing
    }

    /**
     * Sets the test expression value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldId fieldId, Expression testValue) {
        populate(testValue);
    }

    /**
     * Sets the test value, overridden if necessary.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldId fieldId, ReferencedEnvelope testValue)
    {
        // Do nothing
    }

    /**
     * Gets the double value, overridden if necessary.
     *
     * @return the double value
     */
    @Override
    public double getDoubleValue() {
        // Do nothing
        return 0.0;
    }

    /**
     * Gets the integer value, overridden if necessary.
     *
     * @return the int value
     */
    @Override
    public int getIntValue() {
        // Do nothing
        return 0;
    }

    /**
     * Gets the boolean value, overridden if necessary.
     *
     * @return the boolean value
     */
    @Override
    public boolean getBooleanValue() {
        // Do nothing
        return false;
    }

    /**
     * Gets the font value, overridden if necessary.
     *
     * @return the font
     */
    @Override
    public Font getFont() {
        // Do nothing
        return null;
    }

    /**
     * Gets the enum value, overridden if necessary.
     *
     * @return the enum value
     */
    @Override
    public ValueComboBoxData getEnumValue() {
        // Do nothing
        return null;
    }

    /**
     * Gets the process function, overridden if necessary.
     *
     * @return the process function
     */
    @Override
    public ProcessFunction getProcessFunction()
    {
        // Do nothing
        return null;
    }

    /**
     * Gets the colour map.
     *
     * @return the colour map
     */
    @Override
    public ColorMap getColourMap() {
        // Do nothing
        return null;
    }

    /**
     * Duplicate.
     *
     * @return the field config base
     */
    public FieldConfigBase duplicate() {
        FieldConfigBase copy = createCopy(this);

        if(copy != null)
        {
            copy.setParent(getParent());
            copy.setIndentColumn(getIndentColumn());
            copy.updateSymbolListenerList = this.updateSymbolListenerList;
        }
        return copy;
    }

    /**
     * Creates the copy.
     *
     * @param fieldConfigBase the field config base
     * @return the field config base
     */
    protected abstract FieldConfigBase createCopy(FieldConfigBase fieldConfigBase);

    /**
     * Gets the x pos.
     *
     * @return the x pos
     */
    protected int getXPos()
    {
        int x = getIndentColumn() * 20;

        return x;
    }

    /**
     * Gets the no of function fields.
     *
     * @return the no of function fields
     */
    public int getNoOfFunctionFields() {
        return functionList.size();
    }

    /**
     * Gets the function fields.
     *
     * @return the function fields
     */
    public List<FieldConfigBase> getFunctionFields() {
        return functionList;
    }

    /**
     * Gets the all function fields.
     *
     * @return the all function fields
     */
    public List<FieldConfigBase> getAllFunctionFields() {
        List<FieldConfigBase> existingFunctionList = new ArrayList<FieldConfigBase>();

        findChildFunctionFields(functionList, existingFunctionList);

        return existingFunctionList;
    }

    /**
     * Find child function fields.
     *
     * @param subFunctionList the sub function list
     * @param existingFunctionList the existing function list
     */
    private void findChildFunctionFields(List<FieldConfigBase> subFunctionList, List<FieldConfigBase> existingFunctionList)
    {
        if(subFunctionList != null)
        {
            for(FieldConfigBase functionField : subFunctionList)
            {
                if(functionField != null)
                {
                    existingFunctionList.add(functionField);
                    findChildFunctionFields(functionField.functionList, existingFunctionList);
                }
            }
        }
    }

    /**
     * Removes the function fields.
     */
    public void removeFunctionFields() {
        functionList.clear();

        if(functionGroupList != null)
        {
            for(GroupConfigInterface group : functionGroupList)
            {
                group.removeFromUI();
            }

            functionGroupList.clear();
        }
    }

    /**
     * Sets the group components.
     *
     * @param groupConfigList the new group components
     */
    public void setGroupComponents(List<GroupConfigInterface> groupConfigList) {
        functionGroupList = groupConfigList;
    }

    /**
     * Sets the function parameter type.
     *
     * @param functionParameterType the new function parameter type
     */
    public void setFunctionParameterType(Class<?> functionParameterType) {
        this.functionParameterType = functionParameterType;
    }

    /**
     * Gets the function parameter type.
     *
     * @return the functionParemeterType
     */
    public Class<?> getFunctionParameterType() {
        return functionParameterType;
    }

    /**
     * Adds the UI component to the field panel.
     *
     * @param component the component
     * @param buffer the buffer
     * @param width the width
     * @param height the height
     */
    public void addUI(Component component, int buffer, int width, int height) {
        if(fieldPanel != null)
        {
            int lastX = -1;

            for(Component c : fieldPanel.getComponents())
            {
                int x = c.getX() + c.getWidth();

                if(x > lastX)
                {
                    lastX = x;
                }
            }
            component.setBounds(lastX + buffer, 0, width, height);
            fieldPanel.add(component);
        }
    }
}
