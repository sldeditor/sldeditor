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

import com.sldeditor.common.Controller;
import com.sldeditor.ui.attribute.AttributeSelection;
import com.sldeditor.ui.detail.config.base.CurrentFieldState;
import com.sldeditor.ui.iface.AttributeButtonSelectionInterface;
import com.sldeditor.ui.iface.ExpressionUpdateInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.widgets.ExpressionTypeEnum;
import com.sldeditor.ui.widgets.FieldPanel;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.ConstantExpression;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.process.function.ProcessFunction;
import org.geotools.styling.StyleFactoryImpl;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.BinaryExpression;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.NilExpression;

/**
 * The Class FieldConfigBase is the base class for all derived FieldConfigxxx classes.
 *
 * <p>Fields are identified by the id field.
 *
 * <p>Handles the following common tasks:
 *
 * <ul>
 *   <li>value/attribute/expression drop down list, ({@link
 *       com.sldeditor.ui.attribute.AttributeSelection})
 *   <li>setting/getting of values via Expression
 *   <li>field enable/disable state
 *   <li>firing data changed notifications
 * </ul>
 *
 * <img src="./doc-files/field.png" />
 *
 * @author Robert Ward (SCISYS)
 */
public abstract class FieldConfigBase extends FieldConfigPopulate
        implements AttributeButtonSelectionInterface, ExpressionUpdateInterface {

    /** The Constant X_POS. */
    private static final int X_POS = 0;

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
    private List<UpdateSymbolInterface> updateSymbolListenerList = new ArrayList<>();

    /** The style factory. */
    private StyleFactoryImpl styleFactory =
            (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    /** The field state flag. */
    private CurrentFieldState fieldState = new CurrentFieldState();

    /** The cached expression. */
    private Expression cachedExpression = null;

    /** The expression update listener. */
    private ExpressionUpdateInterface expressionUpdateListener = null;

    /** The parent field config. */
    private FieldConfigBase parentFieldConfig = null;

    /**
     * Instantiates a new field config base.
     *
     * @param commonData the common data
     */
    public FieldConfigBase(FieldConfigCommonData commonData) {
        super(commonData);
    }

    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    public void setEnabled(boolean enabled) {
        CurrentFieldState localFieldState = getFieldState();
        localFieldState.setFieldEnabled(enabled);
        setFieldState(localFieldState);
    }

    /**
     * Sets the field enabled state.
     *
     * @param enabled the new enabled
     */
    protected abstract void internalSetEnabled(boolean enabled);

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

    /** Reverts to the default value. */
    public abstract void revertToDefaultValue();

    /**
     * Populate expression.
     *
     * @param objValue the obj value
     */
    public abstract void populateExpression(Object objValue);

    /** Creates the ui. */
    public abstract void createUI();

    /**
     * Gets the common data.
     *
     * @return the common data
     */
    public FieldConfigCommonData getCommonData() {
        return this;
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
    public List<Component> getCustomPanels() {
        return customPanelList;
    }

    /**
     * Adds the custom panel to the list of custom panels.
     *
     * @param customPanel the custom panel
     */
    protected void addCustomPanel(JPanel customPanel) {
        // Create the list if it hasn't been created before
        if (customPanelList == null) {
            customPanelList = new ArrayList<>();
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

        if (this.attributeSelectionPanel != null) {
            this.attributeSelectionPanel.addListener(this);
            this.attributeSelectionPanel.addExpressionListener(this);
        }
    }

    /** Sets the value field state. */
    protected void setValueFieldState() {

        boolean fieldEnabled = fieldState.getFieldEnabledState();

        if (fieldPanel != null) {
            fieldEnabled &= fieldPanel.isValueReadable();
        }
        internalSetEnabled(fieldEnabled && (getExpressionType() == ExpressionTypeEnum.E_VALUE));

        if (attributeSelectionPanel != null) {
            attributeSelectionPanel.setEnabled(fieldEnabled);
        }
    }

    /** Fire data changed. */
    protected void fireDataChanged() {
        if (!Controller.getInstance().isPopulating()) {
            for (UpdateSymbolInterface listener : updateSymbolListenerList) {
                listener.dataChanged(getFieldId());
            }
        }
    }

    /** Value updated. */
    @Override
    public void valueUpdated() {
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
    public void attributeUpdated(String attributeName) {
        expressionType = ExpressionTypeEnum.E_ATTRIBUTE;

        NameImpl name = new NameImpl(attributeName);
        setCachedExpression(new AttributeExpressionImpl(name));

        setValueFieldState();
        fireDataChanged();
    }

    /**
     * Expression updated.
     *
     * @param updatedExpression the updated expression
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.iface.ExpressionUpdateInterface#expressionUpdated(org.opengis.filter.expression.Expression)
     */
    @Override
    public void expressionUpdated(Expression updatedExpression) {
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
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.iface.ExpressionUpdateInterface#expressionUpdated(org.opengis.filter.expression.Expression)
     */
    @Override
    public void functionUpdated(Expression updatedExpression) {
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
    public void populate(Expression expression) {
        if (attributeSelectionPanel != null) {
            attributeSelectionPanel.populateAttributeComboxBox(expression);
        }

        if (expression == null) {
            expressionType = ExpressionTypeEnum.E_VALUE;

            revertToDefaultValue();

            valueUpdated();
        } else {
            if (expression instanceof LiteralExpressionImpl) {
                populateLiteralExpression(expression);
            } else if (expression instanceof ConstantExpression) {
                populateConstantExpression(expression);
            } else if (expression instanceof NilExpression) {
                populateNilExpression();
            } else if (expression instanceof ProcessFunction) {
                populateProcessFunction(expression);
            } else if (expression instanceof AttributeExpressionImpl) {
                populateAttributeExpression(expression);
            } else if ((expression instanceof FunctionExpressionImpl)
                    || (expression instanceof BinaryExpression)) {
                expressionType = ExpressionTypeEnum.E_EXPRESSION;

                if (attributeSelectionPanel != null) {
                    attributeSelectionPanel.populate(expression);
                }

                setCachedExpression(expression);
            } else {
                expressionType = ExpressionTypeEnum.E_EXPRESSION;
            }
        }

        setValueFieldState();
    }

    /**
     * Populate literal expression.
     *
     * @param expression the expression
     */
    private void populateLiteralExpression(Expression expression) {
        LiteralExpressionImpl lExpression = (LiteralExpressionImpl) expression;

        Object objValue = lExpression.getValue();

        if (objValue instanceof AttributeExpressionImpl) {
            expressionType = ExpressionTypeEnum.E_ATTRIBUTE;

            if (attributeSelectionPanel != null) {
                attributeSelectionPanel.setAttribute((AttributeExpressionImpl) objValue);
            }

            setCachedExpression((AttributeExpressionImpl) objValue);
        } else {
            expressionType = ExpressionTypeEnum.E_VALUE;

            populateExpression(objValue);

            valueUpdated();
        }
    }

    /**
     * Populate attribute expression.
     *
     * @param expression the expression
     */
    private void populateAttributeExpression(Expression expression) {
        expressionType = ExpressionTypeEnum.E_ATTRIBUTE;

        if (attributeSelectionPanel != null) {
            attributeSelectionPanel.setAttribute(expression);
        } else {
            populateExpression(expression);
        }

        setCachedExpression(expression);
    }

    /**
     * Populate process function.
     *
     * @param expression the expression
     */
    private void populateProcessFunction(Expression expression) {
        ProcessFunction processExpression = (ProcessFunction) expression;

        Object objValue = processExpression;

        populateExpression(objValue);
    }

    /** Populate nil expression. */
    private void populateNilExpression() {
        Object objValue = null;

        populateExpression(objValue);
    }

    /**
     * Populate constant expression.
     *
     * @param expression the expression
     */
    private void populateConstantExpression(Expression expression) {
        ConstantExpression cExpression = (ConstantExpression) expression;

        Object objValue = cExpression.getValue();

        populateExpression(objValue);
    }

    /**
     * Adds the data changed listener.
     *
     * @param listener the listener
     */
    public void addDataChangedListener(UpdateSymbolInterface listener) {
        if (!updateSymbolListenerList.contains(listener)) {
            updateSymbolListenerList.add(listener);
        }
    }

    /**
     * Gets the expression.
     *
     * @return the expression
     */
    public Expression getExpression() {
        if ((getPanel() != null) && !getPanel().isValueReadable()) {
            return null;
        }
        if (getExpressionType() == ExpressionTypeEnum.E_VALUE) {
            cachedExpression = generateExpression();
        } else {
            if (attributeSelectionPanel != null) {
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
    protected StyleFactoryImpl getStyleFactory() {
        return styleFactory;
    }

    /**
     * Checks if is a single value is legal To be overridden if necessary.
     *
     * @return true, if is a single value
     */
    public boolean isASingleValue() {
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
        if (expressionUpdateListener != null) {
            expressionUpdateListener.expressionUpdated(expression);
        }
    }

    /**
     * Method called when the field has been selected from a combo box and may need to be
     * initialised
     *
     * <p>Will be be overridden if necessary.
     */
    public void justSelected() {
        // Overridden if necessary
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
    protected FieldPanel createFieldPanel(int xPos, String fieldLabel) {
        return createFieldPanel(xPos, 0, fieldLabel);
    }

    /**
     * Creates the field panel with the supplied label.
     *
     * @param xPos the x pos
     * @param yOffset the y offset
     * @param fieldLabel the field label
     * @return the field panel
     */
    protected FieldPanel createFieldPanel(int xPos, int yOffset, String fieldLabel) {
        if (fieldPanel == null) {
            fieldPanel =
                    new FieldPanel(
                            xPos, yOffset, fieldLabel, getCommonData().isOptionalField(), this);
        }

        return fieldPanel;
    }

    /**
     * Creates the field panel with no label.
     *
     * @return the field panel
     */
    protected FieldPanel createFieldPanel() {
        if (fieldPanel == null) {
            fieldPanel = new FieldPanel(this);
        }

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
    public FieldConfigBase getParent() {
        return parentFieldConfig;
    }

    /**
     * Duplicate.
     *
     * @return the field config base
     */
    public FieldConfigBase duplicate() {
        FieldConfigBase copy = createCopy(this);

        if (copy != null) {
            copy.setParent(getParent());
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
    protected int getXPos() {
        return X_POS;
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
        if (fieldPanel != null) {
            int lastX = -1;

            for (Component c : fieldPanel.getComponents()) {
                int x = c.getX() + c.getWidth();

                if (x > lastX) {
                    lastX = x;
                }
            }
            component.setBounds(lastX + buffer, 0, width, height);
            fieldPanel.add(component);
        }
    }

    /**
     * Update attribute selection.
     *
     * @param isRasterSymbol the is raster symbol flag
     */
    public void updateAttributeSelection(boolean isRasterSymbol) {
        if (attributeSelectionPanel != null) {
            attributeSelectionPanel.updateAttributeSelection(isRasterSymbol);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(
                "%s : (%s) %s", getClass().getName(), getFieldId().toString(), getLabel());
    }

    /**
     * Gets the field state.
     *
     * @return the fieldState
     */
    public CurrentFieldState getFieldState() {
        return fieldState;
    }

    /**
     * Sets the field state.
     *
     * @param fieldState the fieldState to set
     */
    public void setFieldState(CurrentFieldState fieldState) {
        if (fieldState != null) {
            this.fieldState = fieldState;

            setValueFieldState();
        }
    }

    /**
     * Show option field.
     *
     * @param displayOptionalFields the display optional fields
     */
    public void showOptionField(boolean displayOptionalFields) {
        if (fieldPanel != null) {
            fieldPanel.showOptionField(displayOptionalFields);
        }
    }

    /**
     * Sets the option field value.
     *
     * @param isSelected the new option field value
     */
    public void setOptionFieldValue(boolean isSelected) {
        if (fieldPanel != null) {
            fieldPanel.setOptionFieldValue(isSelected);
        }
    }
}
