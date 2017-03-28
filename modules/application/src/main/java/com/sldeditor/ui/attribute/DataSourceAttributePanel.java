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

package com.sldeditor.ui.attribute;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.geotools.feature.NameImpl;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.function.PropertyExistsFunction;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.datasource.DataSourceInterface;

/**
 * Panel to be able to edit DataSourceAttribute objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceAttributePanel extends JPanel implements UndoActionInterface {

    /** The Constant ATTRIBUTE_PANEL. */
    private static final String ATTRIBUTE_PANEL = "Attribute";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The attribute combo box. */
    private JComboBox<String> attributeComboBox = new JComboBox<String>();

    /** The expected data type. */
    private Class<?> expectedDataType = Object.class;

    /** The attribute name list. */
    private List<String> attributeNameList = null;

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The data source. */
    private DataSourceInterface dataSource = null;

    /** The data model. */
    private DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();

    /** The populating attribute combo box. */
    private boolean populatingAttributeComboBox = false;

    /** The label : data type. */
    private JLabel lblDataType;

    /**
     * Gets the panel name.
     *
     * @return the panel name
     */
    public static String getPanelName() {
        return ATTRIBUTE_PANEL;
    }

    /**
     * Instantiates a new data source attribute panel.
     *
     * @param parentObj the parent obj
     */
    public DataSourceAttributePanel(SubPanelUpdatedInterface parentObj) {
        final UndoActionInterface thisObj = this;

        setLayout(new BorderLayout(5, 0));

        lblDataType = new JLabel();
        add(lblDataType, BorderLayout.WEST);

        add(attributeComboBox, BorderLayout.CENTER);
        populateAttributeComboBox();
        attributeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (isAttributeComboBoxPopulated()) {
                    String newValueObj = (String) attributeComboBox.getSelectedItem();
                    UndoManager.getInstance().addUndoEvent(new UndoEvent(thisObj,
                            "DataSourceAttribute", oldValueObj, newValueObj));

                    if (parentObj != null) {
                        parentObj.updateSymbol();
                    }
                }
            }
        });
    }

    /**
     * Sets the data type.
     *
     * @param expectedDataType the new data type
     */
    public void setDataType(Class<?> expectedDataType) {
        boolean different = (this.expectedDataType != expectedDataType);
        this.expectedDataType = expectedDataType;

        if (different) {
            if (dataSource != null) {
                attributeNameList = this.dataSource.getAttributes(expectedDataType);
                populateAttributeComboBox();
            }
        }

        if (expectedDataType != null) {
            lblDataType.setText(expectedDataType.getSimpleName().toLowerCase());
        }
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

    /**
     * Gets the selected item.
     *
     * @return the selected item
     */
    public String getSelectedItem() {
        return (String) attributeComboBox.getSelectedItem();
    }

    /**
     * Data source loaded.
     *
     * @param dataSource the data source
     */
    public void dataSourceLoaded(DataSourceInterface dataSource) {
        this.dataSource = dataSource;

        if (this.dataSource != null) {
            attributeNameList = this.dataSource.getAttributes(expectedDataType);
        }

        populateAttributeComboBox();
    }

    /**
     * Sets the panel enabled.
     *
     * @param enabled the new panel enabled
     */
    public void setPanelEnabled(boolean enabled) {
        attributeComboBox.setEnabled(enabled);
    }

    /**
     * Sets the attribute.
     *
     * @param expression the new attribute
     */
    public void setAttribute(Expression expression) {
        String propertyName = null;

        if (expression instanceof PropertyExistsFunction) {
            Expression e = ((PropertyExistsFunction) expression).getParameters().get(0);
            Object value = ((LiteralExpressionImpl) e).getValue();
            propertyName = ((AttributeExpressionImpl) value).getPropertyName();
        } else if (expression instanceof AttributeExpressionImpl) {
            propertyName = ((AttributeExpressionImpl) expression).getPropertyName();
        } else if (expression instanceof LiteralExpressionImpl) {
            propertyName = AttributeUtils
                    .extract((String) ((LiteralExpressionImpl) expression).getValue());
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
     * Gets the expression.
     *
     * @return the expression
     */
    public Expression getExpression() {
        Expression expression = null;
        String attributeName = (String) attributeComboBox.getSelectedItem();

        if (attributeName != null) {
            NameImpl name = new NameImpl(attributeName);
            expression = new AttributeExpressionImpl(name);
        }

        return expression;
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
        String oldValueObj = (String) undoRedoObject.getOldValue();

        attributeComboBox.setSelectedItem(oldValueObj);
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
        String newValueObj = (String) undoRedoObject.getNewValue();

        attributeComboBox.setSelectedItem(newValueObj);
    }
}
