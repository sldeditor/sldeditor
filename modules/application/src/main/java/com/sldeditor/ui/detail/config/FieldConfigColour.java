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

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.utils.ColourUtils;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.iface.ColourNotifyInterface;
import com.sldeditor.ui.widgets.ColourButton;
import com.sldeditor.ui.widgets.ExpressionTypeEnum;
import com.sldeditor.ui.widgets.FieldPanel;
import java.awt.Color;
import org.geotools.filter.LiteralExpressionImpl;
import org.opengis.filter.expression.Expression;

/**
 * The Class FieldConfigColour wraps a button GUI component displaying the current colour and an
 * optional value/attribute/expression drop down, ({@link
 * com.sldeditor.ui.attribute.AttributeSelection})
 *
 * <p>Supports undo/redo functionality.
 *
 * <p>Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigColour extends FieldConfigBase implements UndoActionInterface {

    /** The button fill colour. */
    private ColourButton colourButton;

    /** The default colour. */
    private Color defaultValue = Color.black;

    /** The old value obj. */
    private Color oldValueObj = null;

    /**
     * Instantiates a new field config colour.
     *
     * @param commonData the common data
     */
    public FieldConfigColour(FieldConfigCommonData commonData) {
        super(commonData);
    }

    /** Creates the ui. */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#createUI()
     */
    @Override
    public void createUI() {
        if (colourButton == null) {
            final UndoActionInterface parentObj = this;

            int xPos = getXPos();
            FieldPanel fieldPanel = createFieldPanel(xPos, getLabel());

            colourButton = new ColourButton();
            colourButton.setBounds(
                    xPos + BasePanel.WIDGET_X_START,
                    0,
                    BasePanel.WIDGET_STANDARD_WIDTH,
                    BasePanel.WIDGET_HEIGHT);
            fieldPanel.add(colourButton);

            if (!isValueOnly()) {
                setAttributeSelectionPanel(
                        fieldPanel.internalCreateAttrButton(String.class, this, isRasterSymbol()));
            }

            colourButton.registerObserver(
                    new ColourNotifyInterface() {
                        @Override
                        public void notify(String colourString, double opacity) {

                            Color newValueObj = colourButton.getColour();

                            UndoManager.getInstance()
                                    .addUndoEvent(
                                            new UndoEvent(
                                                    parentObj,
                                                    getFieldId(),
                                                    oldValueObj,
                                                    newValueObj));

                            oldValueObj = newValueObj;

                            valueUpdated();
                        }
                    });
        }
    }

    /**
     * Attribute selection.
     *
     * @param field the field
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.iface.AttributeButtonSelectionInterface#attributeSelection(java.lang.String)
     */
    @Override
    public void attributeSelection(String field) {
        // Not used
    }

    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#setEnabled(boolean)
     */
    @Override
    public void internal_setEnabled(boolean enabled) {
        if (colourButton != null) {
            colourButton.setEnabled(enabled);
        }
    }

    /**
     * Generate expression.
     *
     * @return the expression
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#generateExpression()
     */
    @Override
    protected Expression generateExpression() {
        // Handled by getColourExpression() and getColourOpacityExpression()
        return null;
    }

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        if ((attributeSelectionPanel != null) && !isValueOnly()) {
            return attributeSelectionPanel.isEnabled();
        } else {
            if (colourButton != null) {
                return colourButton.isEnabled();
            }
        }
        return false;
    }

    /** Revert to default value. */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#revertToDefaultValue()
     */
    @Override
    public void revertToDefaultValue() {
        if (colourButton != null) {
            oldValueObj = this.defaultValue;
            colourButton.populate(this.defaultValue);
        }
    }

    /**
     * Populate expression.
     *
     * @param objValue the obj value
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#populateExpression(java.lang.Object)
     */
    @Override
    public void populateExpression(Object objValue) {
        String sValue = null;
        boolean validColour = true;

        if (objValue instanceof LiteralExpressionImpl) {
            sValue = ((LiteralExpressionImpl) objValue).toString();
        } else if (objValue instanceof String) {
            sValue = (String) objValue;
        } else {
            validColour = false;
            if (objValue != null) {
                ConsoleManager.getInstance()
                        .error(
                                this,
                                "Colour expression is of unknown type : "
                                        + objValue.getClass().getName());
            } else {
                // Null is allowed, just will not be shown
            }
        }

        if ((colourButton != null) && validColour) {
            if (!sValue.startsWith("#")) {
                ConsoleManager.getInstance()
                        .error(this, "Colour string does not start with #" + sValue);
            } else {
                Expression opacity =
                        getFilterFactory().literal(DefaultSymbols.defaultColourOpacity());
                colourButton.populate(sValue, opacity);
                Color newValue = colourButton.getColour();

                UndoManager.getInstance()
                        .addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, newValue));

                oldValueObj = newValue;
            }
        }
    }

    /**
     * Gets the colour expression.
     *
     * @return the colour expression
     */
    public Expression getColourExpression() {
        if (colourButton == null) {
            return null;
        }
        if (getPanel().isValueReadable()) {
            if ((getExpressionType() == ExpressionTypeEnum.E_ATTRIBUTE)
                    || (getExpressionType() == ExpressionTypeEnum.E_EXPRESSION)) {
                return getExpression();
            }
            return getFilterFactory().literal(colourButton.getColourString());
        }
        return null;
    }

    /**
     * Gets the colour opacity expression.
     *
     * @return the colour opacity expression
     */
    public Expression getColourOpacityExpression() {
        if ((getExpressionType() == ExpressionTypeEnum.E_ATTRIBUTE)
                || (getExpressionType() == ExpressionTypeEnum.E_EXPRESSION)) {
            return getExpression();
        }

        if (colourButton != null) {
            return getFilterFactory().literal(colourButton.getColourOpacity());
        } else {
            return null;
        }
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    @Override
    public String getStringValue() {
        if (colourButton == null) {
            return "";
        }
        return colourButton.getColourString();
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        if ((colourButton != null) && (undoRedoObject != null)) {
            if (undoRedoObject.getOldValue() instanceof Color) {
                Color oldValue = (Color) undoRedoObject.getOldValue();

                colourButton.setColour(oldValue);

                valueUpdated();
            }
        }
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        if ((colourButton != null) && (undoRedoObject != null)) {
            if (undoRedoObject.getNewValue() instanceof Color) {
                Color newValue = (Color) undoRedoObject.getNewValue();

                colourButton.setColour(newValue);

                valueUpdated();
            }
        }
    }

    /**
     * Sets the test value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldIdEnum fieldId, String testValue) {
        if (colourButton != null) {
            colourButton.populate(testValue, null);

            Color value = ColourUtils.toColour(testValue);
            UndoManager.getInstance()
                    .addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, value));

            oldValueObj = value;

            valueUpdated();
        }
    }

    /**
     * Creates a copy of the field.
     *
     * @param fieldConfigBase the field config base
     * @return the field config base
     */
    @Override
    protected FieldConfigBase createCopy(FieldConfigBase fieldConfigBase) {
        FieldConfigColour copy = null;
        if (fieldConfigBase != null) {
            copy = new FieldConfigColour(fieldConfigBase.getCommonData());
        }
        return copy;
    }

    /**
     * Sets the field visible.
     *
     * @param visible the new visible state
     */
    @Override
    public void setVisible(boolean visible) {
        if (colourButton != null) {
            colourButton.setVisible(visible);
        }
    }
}
