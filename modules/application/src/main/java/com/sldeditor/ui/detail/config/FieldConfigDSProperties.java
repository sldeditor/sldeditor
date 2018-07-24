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

import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.DataSourceUpdatedInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.widgets.FieldPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import org.geotools.data.DataStore;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.opengis.filter.expression.Expression;

/**
 * The Class FieldConfigDSProperties wraps a drop down GUI component containing data source property
 * names and an optional value/attribute/expression drop down.
 *
 * <p>Supports undo/redo functionality.
 *
 * <p>Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigDSProperties extends FieldConfigBase
        implements UndoActionInterface, DataSourceUpdatedInterface {

    /** The default value. */
    private String defaultValue = "";

    /** The combo box. */
    private JComboBox<String> comboBox = new JComboBox<String>();

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The data model. */
    private DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();

    /** The populating attribute combo box. */
    private boolean populatingAttributeComboBox = false;

    /** The attribute name list. */
    private List<String> attributeNameList = null;

    /**
     * Instantiates a new field config enum.
     *
     * @param commonData the common data
     */
    public FieldConfigDSProperties(FieldConfigCommonData commonData) {
        super(commonData);

        DataSourceInterface dataSource = DataSourceFactory.getDataSource();
        dataSource.addListener(this);
    }

    /** Creates the ui. */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#createUI()
     */
    @Override
    public void createUI() {

        int xPos = getXPos();
        comboBox.setBounds(
                xPos + BasePanel.WIDGET_X_START,
                0,
                isValueOnly() ? BasePanel.WIDGET_EXTENDED_WIDTH : BasePanel.WIDGET_STANDARD_WIDTH,
                BasePanel.WIDGET_HEIGHT);

        FieldPanel fieldPanel = createFieldPanel(xPos, getLabel());
        fieldPanel.add(comboBox);

        comboBox.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        valueStored();
                    }
                });
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
        if (this.comboBox != null) {
            this.comboBox.setEnabled(field == null);
        }
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
        if (comboBox != null) {
            comboBox.setEnabled(enabled);
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
    protected Expression generateExpression() {
        Expression expression = null;

        if (comboBox != null) {
            String value = (String) comboBox.getSelectedItem();
            if (value != null) {
                expression = getFilterFactory().literal(value);
            }
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
            if (comboBox != null) {
                return comboBox.isEnabled();
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
        populateField(defaultValue);
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
        if (comboBox != null) {
            if (objValue instanceof String) {
                String sValue = (String) objValue;

                populateField(sValue);
            } else if (objValue instanceof LiteralExpressionImpl) {
                String sValue = objValue.toString();

                populateField(sValue);
            } else if (objValue instanceof AttributeExpressionImpl) {
                String sValue = objValue.toString();

                populateField(sValue);
            }
        }
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    @Override
    public String getStringValue() {
        return (String) comboBox.getSelectedItem();
    }

    /** Populate attribute combo box. */
    private void populateAttributeComboBox() {
        if (comboBox != null) {
            setPopulatingComboBox(true);
            // CHECKSTYLE:OFF
            Object selectedItem = model.getSelectedItem();
            // CHECKSTYLE:ON
            model.removeAllElements();
            model.addElement("");

            if (attributeNameList != null) {
                for (String attribute : attributeNameList) {
                    model.addElement(attribute);
                }
            }
            comboBox.setModel(model);
            model.setSelectedItem(selectedItem);

            setPopulatingComboBox(false);
        }
    }

    /**
     * Sets the populating combo box.
     *
     * @param populatingFlag the new populating combo box
     */
    private void setPopulatingComboBox(boolean populatingFlag) {
        populatingAttributeComboBox = populatingFlag;
    }

    /**
     * Checks if is attribute combo box populated.
     *
     * @return true, if is attribute combo box populated
     */
    private boolean isAttributeComboBoxPopulated() {
        return !populatingAttributeComboBox;
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo redo object
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.undo.UndoActionInterface#undoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        if ((comboBox != null) && (undoRedoObject != null)) {
            if (undoRedoObject.getOldValue() instanceof String) {
                String oldValue = (String) undoRedoObject.getOldValue();

                comboBox.setSelectedItem(oldValue);
            }
        }
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo redo object
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.undo.UndoActionInterface#redoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        if ((comboBox != null) && (undoRedoObject != null)) {
            if (undoRedoObject.getNewValue() instanceof String) {
                String newValue = (String) undoRedoObject.getNewValue();

                comboBox.setSelectedItem(newValue);
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
        populateField(testValue);

        valueUpdated();
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    @Override
    public void populateField(String value) {
        if (comboBox != null) {

            if (value != null) {
                comboBox.setSelectedItem(value);
            } else {
                comboBox.setSelectedIndex(-1);
            }
        }
    }

    /**
     * Sets the default value.
     *
     * @param defaultValue the new default value
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Creates a copy of the field.
     *
     * @param fieldConfigBase the field config base
     * @return the field config base
     */
    @Override
    protected FieldConfigBase createCopy(FieldConfigBase fieldConfigBase) {
        FieldConfigDSProperties copy = null;

        if (fieldConfigBase != null) {
            copy = new FieldConfigDSProperties(fieldConfigBase.getCommonData());
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
        if (comboBox != null) {
            comboBox.setVisible(visible);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceLoaded(com.sldeditor.datasource
     * .impl.GeometryTypeEnum, boolean)
     */
    @Override
    public void dataSourceLoaded(
            GeometryTypeEnum geometryType, boolean isConnectedToDataSourceFlag) {
        DataSourceInterface dataSource = DataSourceFactory.getDataSource();
        attributeNameList = dataSource.getAllAttributes(false);

        populateAttributeComboBox();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceAboutToUnloaded(org.geotools.
     * data.DataStore)
     */
    @Override
    public void dataSourceAboutToUnloaded(DataStore dataStore) {
        // Do nothing
    }

    /** Value stored. */
    protected void valueStored() {
        if (isAttributeComboBoxPopulated()) {
            if (comboBox.getSelectedItem() != null) {

                if (!FieldConfigDSProperties.this.isSuppressUndoEvents()) {
                    String newValueObj = new String((String) comboBox.getSelectedItem());

                    UndoManager.getInstance()
                            .addUndoEvent(
                                    new UndoEvent(this, getFieldId(), oldValueObj, newValueObj));

                    oldValueObj = new String(newValueObj);
                }
                valueUpdated();
            }
        }
    }
}
