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
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.v2.function.temporal.DateUtils;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.widgets.FieldPanel;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import org.opengis.filter.expression.Expression;

/**
 * The Class FieldConfigDate wraps a date picker GUI component.
 *
 * <p>Supports undo/redo functionality.
 *
 * <p>Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigDate extends FieldConfigBase
        implements UndoActionInterface, DateTimePanelUpdateInterface {

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The date time panel. */
    private DateTimePanel dateTimePanel;

    /**
     * Flag indicating whether class is being populated, prevents multiple undo events being
     * triggered when setting a date value.
     */
    private boolean isPopulating = false;

    /**
     * Instantiates a new field config date.
     *
     * @param commonData the common data
     */
    public FieldConfigDate(FieldConfigCommonData commonData) {
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
        if (dateTimePanel == null) {
            dateTimePanel = new DateTimePanel(this);

            FieldPanel fieldPanel =
                    createFieldPanel(getXPos(), 2 * BasePanel.WIDGET_HEIGHT, getLabel());

            dateTimePanel.createUI(getXPos(), getIndent(), 0, fieldPanel);

            if (!isValueOnly()) {
                setAttributeSelectionPanel(
                        fieldPanel.internalCreateAttrButton(Double.class, this, isRasterSymbol()));
            }
        }
    }

    /** Value stored. */
    protected void valueStored() {
        if (!isSuppressUndoEvents() && !isPopulating) {
            ZonedDateTime newValueObj = dateTimePanel.getDate();

            UndoManager.getInstance()
                    .addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, newValueObj));

            oldValueObj = newValueObj;
        }
        valueUpdated();
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
        if (dateTimePanel != null) {
            dateTimePanel.setEnabled(enabled);
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
        Expression expression = null;

        if (dateTimePanel != null) {
            expression = getFilterFactory().literal(dateTimePanel.getStringValue());
        }
        return expression;
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
            if (dateTimePanel != null) {
                return dateTimePanel.isEnabled();
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
        ZonedDateTime currentDate = null;

        populateField(currentDate);
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
        ZonedDateTime value = null;

        if (objValue instanceof ZonedDateTime) {
            value = (ZonedDateTime) objValue;
        } else if (objValue instanceof String) {
            try {
                value = DateUtils.getZonedDateTime((String) objValue);
            } catch (DateTimeParseException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }

        populateField(value);
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    @Override
    public String getStringValue() {
        if (dateTimePanel == null) {
            return null;
        }
        ZonedDateTime date = dateTimePanel.getDate();
        if (date == null) {
            return null;
        }

        return DateUtils.getString(date);
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        if ((dateTimePanel != null) && (undoRedoObject != null)) {
            if (undoRedoObject.getOldValue() instanceof ZonedDateTime) {
                ZonedDateTime oldValue = (ZonedDateTime) undoRedoObject.getOldValue();

                dateTimePanel.populateUI(oldValue);
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
        if ((dateTimePanel != null) && (undoRedoObject != null)) {
            if (undoRedoObject.getNewValue() instanceof ZonedDateTime) {
                ZonedDateTime newValue = (ZonedDateTime) undoRedoObject.getNewValue();

                dateTimePanel.populateUI(newValue);
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
        ZonedDateTime date = DateUtils.getZonedDateTime(testValue);

        populateField(date);
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    @Override
    public void populateField(ZonedDateTime value) {
        if ((dateTimePanel != null) && (value != null)) {
            isPopulating = true;

            dateTimePanel.populateUI(value);

            if (!isSuppressUndoEvents()) {
                UndoManager.getInstance()
                        .addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, value));
                oldValueObj = value;
            }

            isPopulating = false;
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
        FieldConfigDate copy = null;
        if (fieldConfigBase != null) {
            copy = new FieldConfigDate(getCommonData());
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
        if (dateTimePanel != null) {
            dateTimePanel.setVisible(visible);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.DateTimePanelUpdateInterface#dateTimeValueUpdated()
     */
    @Override
    public void dateTimeValueUpdated() {
        valueStored();
    }
}
