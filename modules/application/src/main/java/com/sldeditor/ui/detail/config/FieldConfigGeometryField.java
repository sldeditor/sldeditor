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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.geotools.data.DataStore;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.function.PropertyExistsFunction;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.DataSourceUpdatedInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.ui.attribute.AttributeUtils;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.widgets.FieldPanel;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The Class FieldConfigGeometryField wraps a drop down GUI component the contains the data source attributes
 * <p>
 * Supports undo/redo functionality.
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig}
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigGeometryField extends FieldConfigBase
        implements UndoActionInterface, DataSourceUpdatedInterface {

    /** The attribute combo box. */
    private JComboBox<String> attributeComboBox = new JComboBox<String>();

    /** The attribute name list. */
    private List<String> attributeNameList = null;

    /** The default value. */
    private String defaultValue = "";

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The data model. */
    private DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();

    /** The populating attribute combo box. */
    private boolean populatingAttributeComboBox = false;

    /** The field enabled flag. */
    private boolean fieldEnabled = true;

    /**
     * Default constructor.
     *
     * @param commonData the common data
     */
    public FieldConfigGeometryField(FieldConfigCommonData commonData) {
        super(commonData);
    }

    /**
     * Creates the ui.
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#createUI()
     */
    @Override
    public void createUI() {
        final UndoActionInterface parentObj = this;

        int xPos = getXPos();
        FieldPanel fieldPanel = createFieldPanel(xPos, getLabel());

        attributeComboBox.setBounds(xPos + BasePanel.WIDGET_X_START, 0,
                BasePanel.WIDGET_STANDARD_WIDTH, BasePanel.WIDGET_HEIGHT);

        fieldPanel.add(attributeComboBox);
        populateAttributeComboBox();

        if (!isValueOnly()) {
            setAttributeSelectionPanel(
                    fieldPanel.internalCreateAttrButton(Geometry.class, this, true));
        }

        attributeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isAttributeComboBoxPopulated()) {
                    String newValueObj = (String) attributeComboBox.getSelectedItem();
                    UndoManager.getInstance().addUndoEvent(new UndoEvent(parentObj,
                            "DataSourceAttribute", oldValueObj, newValueObj));

                    valueUpdated();
                }
            }
        });

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
    public void setEnabled(boolean enabled) {
        // Do nothing
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

        if (attributeComboBox != null) {
            String value = getStringValue();
            if (value != null) {
                expression = getFilterFactory().property(value);
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
        return fieldEnabled;
    }

    /**
     * Revert to default value.
     */
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
        String propertyName = null;

        if (objValue instanceof PropertyExistsFunction) {
            Expression e = ((PropertyExistsFunction) objValue).getParameters().get(0);
            Object value = ((LiteralExpressionImpl) e).getValue();
            propertyName = ((AttributeExpressionImpl) value).getPropertyName();
        } else if (objValue instanceof AttributeExpressionImpl) {
            propertyName = ((AttributeExpressionImpl) objValue).getPropertyName();
        } else if (objValue instanceof LiteralExpressionImpl) {
            propertyName = AttributeUtils
                    .extract((String) ((LiteralExpressionImpl) objValue).getValue());
        }

        if (propertyName != null) {
            oldValueObj = propertyName;

            attributeComboBox.setSelectedItem(propertyName);
        } else {
            oldValueObj = propertyName;
            attributeComboBox.setSelectedIndex(-1);
        }
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    @Override
    public String getStringValue() {
        return (String) attributeComboBox.getSelectedItem();
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
        if ((attributeComboBox != null) && (undoRedoObject != null)) {
            if (undoRedoObject.getOldValue() instanceof String) {
                String oldValue = (String) undoRedoObject.getOldValue();

                attributeComboBox.setSelectedItem(oldValue);
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
        if ((attributeComboBox != null) && (undoRedoObject != null)) {
            if (undoRedoObject.getNewValue() instanceof String) {
                String newValue = (String) undoRedoObject.getNewValue();

                attributeComboBox.setSelectedItem(newValue);
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
        if (value != null) {
            oldValueObj = value;

            attributeComboBox.setSelectedItem(value);
        } else {
            oldValueObj = value;
            attributeComboBox.setSelectedIndex(-1);
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
        FieldConfigGeometryField copy = null;

        if (fieldConfigBase != null) {
            copy = new FieldConfigGeometryField(fieldConfigBase.getCommonData());
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
        if (attributeComboBox != null) {
            attributeComboBox.setVisible(visible);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceLoaded(com.sldeditor.datasource.impl.GeometryTypeEnum, boolean)
     */
    @Override
    public void dataSourceLoaded(GeometryTypeEnum geometryType,
            boolean isConnectedToDataSourceFlag) {

        DataSourceInterface dataSource = DataSourceFactory.getDataSource();

        if (dataSource != null) {
            attributeNameList = dataSource.getAttributes(Geometry.class);
        }

        populateAttributeComboBox();
    }

    /**
     * Populate attribute combo box.
     */
    private void populateAttributeComboBox() {
        if (attributeComboBox != null) {
            setPopulatingComboBox(true);
            Object selectedItem = model.getSelectedItem();
            model.removeAllElements();
            model.addElement("");

            if (attributeNameList != null) {
                for (String attribute : attributeNameList) {
                    model.addElement(attribute);
                }
            }
            attributeComboBox.setModel(model);
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

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.AttributeButtonSelectionInterface#attributeSelection(java.lang.String)
     */
    @Override
    public void attributeSelection(String field) {
        // Does nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceAboutToUnloaded(org.geotools.data.DataStore)
     */
    @Override
    public void dataSourceAboutToUnloaded(DataStore dataStore) {
        // Does nothing
    }
}
