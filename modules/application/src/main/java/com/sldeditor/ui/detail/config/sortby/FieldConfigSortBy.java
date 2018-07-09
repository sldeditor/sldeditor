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

package com.sldeditor.ui.detail.config.sortby;

import com.sldeditor.common.Controller;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.widgets.FieldPanel;
import org.opengis.filter.expression.Expression;

/**
 * The Class FieldConfigSortBy provides feature type style sort by functionality and an optional
 * value/attribute/expression drop down,
 *
 * <p>Supports undo/redo functionality.
 *
 * <p>Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigSortBy extends FieldConfigBase
        implements SortByUpdateInterface, UndoActionInterface {

    /** The sortby panel. */
    private SortByPanel sortbyPanel;

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The number of rows the sort by panel will have. */
    private int NO_OF_ROWS = 6;

    /**
     * Instantiates a new field config string.
     *
     * @param commonData the common data
     */
    public FieldConfigSortBy(FieldConfigCommonData commonData) {
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
        if (sortbyPanel == null) {
            sortbyPanel = new SortByPanel(this, NO_OF_ROWS);
            int xPos = getXPos();
            FieldPanel fieldPanel =
                    createFieldPanel(xPos, BasePanel.WIDGET_HEIGHT * NO_OF_ROWS, getLabel());
            fieldPanel.add(sortbyPanel);
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
     * @see
     * com.sldeditor.ui.iface.AttributeButtonSelectionInterface#attributeSelection(java.lang.String)
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
        if (sortbyPanel != null) {
            sortbyPanel.setEnabled(enabled);
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

        if (this.sortbyPanel != null) {
            expression = getFilterFactory().literal(sortbyPanel.getText());
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
            if (sortbyPanel != null) {
                return sortbyPanel.isEnabled();
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
        // Do nothing
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
        if (objValue instanceof String) {
            String sValue = (String) objValue;

            populateField(sValue);
        }
    }

    /**
     * Sets the default value.
     *
     * @param defaultValue the new default value
     */
    public void setDefaultValue(String defaultValue) {
        // Do nothing
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    @Override
    public String getStringValue() {
        if (sortbyPanel != null) {
            if (getPanel().isValueReadable()) {
                return sortbyPanel.getText();
            }
        }
        return null;
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        if (sortbyPanel != null) {
            if (undoRedoObject != null) {
                String oldValue = (String) undoRedoObject.getOldValue();

                Controller.getInstance().setPopulating(true);
                sortbyPanel.setText(oldValue);
                Controller.getInstance().setPopulating(false);
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
        if (sortbyPanel != null) {
            if (undoRedoObject != null) {
                String newValue = (String) undoRedoObject.getNewValue();

                Controller.getInstance().setPopulating(true);
                sortbyPanel.setText(newValue);
                Controller.getInstance().setPopulating(false);
            }
        }
    }

    /**
     * Sets the test string value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldIdEnum fieldId, String testValue) {
        populateField(testValue);
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    @Override
    public void populateField(String value) {
        if (sortbyPanel != null) {
            sortbyPanel.setText(value);

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
        FieldConfigSortBy copy = null;

        if (fieldConfigBase != null) {
            copy = new FieldConfigSortBy(fieldConfigBase.getCommonData());
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
        if (sortbyPanel != null) {
            sortbyPanel.setVisible(visible);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.ui.detail.config.sortby.SortByUpdateInterface#sortByUpdated(java.lang.String)
     */
    @Override
    public void sortByUpdated(String sortByString) {
        if (!Controller.getInstance().isPopulating()) {

            UndoManager.getInstance()
                    .addUndoEvent(
                            new UndoEvent(
                                    this, getFieldId(), oldValueObj, new String(sortByString)));

            oldValueObj = new String(sortByString);

            valueUpdated();
        }
    }
}
