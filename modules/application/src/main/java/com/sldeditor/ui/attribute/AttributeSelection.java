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
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.geotools.data.DataStore;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.ConstantExpression;
import org.geotools.filter.LiteralExpressionImpl;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.NilExpression;

import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.DataSourceUpdatedInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.iface.AttributeButtonSelectionInterface;
import com.sldeditor.ui.iface.ExpressionUpdateInterface;
import com.sldeditor.ui.widgets.ExpressionTypeEnum;

/**
 * A panel to allow the user to specify whether field's contents is either:
 * <ul>
 * <li>value</li>
 * <li>attribute</li>
 * <li>expression</li>
 * </ul>.
 *
 * @author Robert Ward (SCISYS)
 */
public class AttributeSelection extends JPanel implements DataSourceUpdatedInterface, SubPanelUpdatedInterface, UndoActionInterface {

    /** The Constant WIDTH. */
    private static final int WIDTH = 270;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The selected listeners. */
    private List<AttributeButtonSelectionInterface> selectedListeners = new ArrayList<AttributeButtonSelectionInterface>();

    /** The outer panel. */
    private JPanel outerPanel;

    /** The attribute chooser combo box. */
    private JComboBox<String> attributeChooserComboBox;

    /** The listener list. */
    private List<ExpressionUpdateInterface> listenerList = new ArrayList<ExpressionUpdateInterface>();

    /** The value panel. */
    private ValueSubPanel valuePanel;

    /** The expression panel. */
    private ExpressionSubPanel expressionPanel;

    /** The data source attribute panel. */
    private DataSourceAttributePanel dataSourceAttributePanel;

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The field. */
    private FieldConfigBase field = null;

    /**
     * Gets the panel width.
     *
     * @return the panel width
     */
    public static int getPanelWidth() {
        return WIDTH;
    }

    /**
     * Instantiates a new attribute selection.
     *
     * @param expectedDataType the expected data type
     * @param field the field
     * @param allowedExpressionTypes the allowed expression types
     */
    private AttributeSelection(Class<?> expectedDataType,
            FieldConfigBase field, 
            List<ExpressionTypeEnum> allowedExpressionTypes) {

        this.field = field;
        setLayout(new BorderLayout(0, 0));
        setPreferredSize(new Dimension(100, BasePanel.WIDGET_HEIGHT));

        List<String> allowedList = new ArrayList<String>();
        if(allowedExpressionTypes.contains(ExpressionTypeEnum.E_VALUE))
        {
            allowedList.add(ValueSubPanel.getPanelName());
        }

        if(allowedExpressionTypes.contains(ExpressionTypeEnum.E_ATTRIBUTE))
        {
            allowedList.add(DataSourceAttributePanel.getPanelName());
        }

        if(allowedExpressionTypes.contains(ExpressionTypeEnum.E_EXPRESSION))
        {
            allowedList.add(ExpressionSubPanel.getPanelName());
        }

        createUI(expectedDataType, allowedList, field.isRasterSymbol());

        DataSourceInterface dataSource = DataSourceFactory.getDataSource();
        if(dataSource != null)
        {
            dataSource.addListener(this);
        }
    }

    /**
     * Adds the listener.
     *
     * @param listener the listener
     */
    public void addListener(AttributeButtonSelectionInterface listener)
    {
        if(!selectedListeners.contains(listener))
        {
            selectedListeners.add(listener);
        }
    }

    /**
     * Creates the ui.
     *
     * @param expectedDataType the expected data type
     * @param allowedList the allowed list
     * @param isRasterSymbol the is raster symbol
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void createUI(Class<?> expectedDataType, List<String> allowedList, boolean isRasterSymbol) {
        final UndoActionInterface thisObj = this;

        outerPanel = new JPanel();
        add(outerPanel, BorderLayout.CENTER);
        outerPanel.setLayout(new CardLayout(5, 0));

        valuePanel = createValuePanel(false);

        expressionPanel = createExpressionPanel(expectedDataType, isRasterSymbol);

        dataSourceAttributePanel = createDataSourceAttributePanel(expectedDataType);

        attributeChooserComboBox = new JComboBox<String>();
        attributeChooserComboBox.setModel(new DefaultComboBoxModel(allowedList.toArray()));
        attributeChooserComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JComboBox<String> cb = (JComboBox<String>)e.getSource();
                String selected = (String)cb.getSelectedItem();
                showPanel(selected);

                if((oldValueObj == null) && cb.getItemCount() > 0)
                {
                    oldValueObj = cb.getItemAt(0);
                }

                UndoManager.getInstance().addUndoEvent(new UndoEvent(thisObj, "DataSourceAttribute", oldValueObj, selected));

                updateSymbol();
            }
        });
        add(attributeChooserComboBox, BorderLayout.WEST);
    }

    /**
     * Creates the value panel.
     *
     * @param addValueField the add value field
     * @return the j panel
     */
    private ValueSubPanel createValuePanel(boolean addValueField) {
        ValueSubPanel valuePanel = new ValueSubPanel(addValueField);
        outerPanel.add(valuePanel, ValueSubPanel.getPanelName());
        return valuePanel;
    }

    /**
     * Creates the expression panel.
     *
     * @param expectedDataType the expected data type
     * @param isRasterSymbol the is raster symbol flag
     * @return the j panel
     */
    private ExpressionSubPanel createExpressionPanel(Class<?> expectedDataType, boolean isRasterSymbol) {
        ExpressionSubPanel panel = new ExpressionSubPanel(this, expectedDataType, isRasterSymbol);
        outerPanel.add(panel, ExpressionSubPanel.getPanelName());

        return panel;
    }

    /**
     * Creates the data source attribute panel.
     *
     * @param expectedDataType the expected data type
     * @return the j panel
     */
    private DataSourceAttributePanel createDataSourceAttributePanel(Class<?> expectedDataType) {
        DataSourceAttributePanel panel = new DataSourceAttributePanel(this);
        panel.setDataType(expectedDataType);

        outerPanel.add(panel, DataSourceAttributePanel.getPanelName());
        return panel;
    }

    /**
     * Update symbol.
     */
    @Override
    public void updateSymbol() {

        boolean enableField = false;

        String selectedString = (String) attributeChooserComboBox.getSelectedItem();
        if(selectedString.compareTo(ValueSubPanel.getPanelName()) == 0)
        {
            enableField = true;

            for(ExpressionUpdateInterface listener : listenerList)
            {
                listener.valueUpdated();
            }
        }
        else if(selectedString.compareTo(DataSourceAttributePanel.getPanelName()) == 0)
        {
            String attribute = dataSourceAttributePanel.getSelectedItem();

            for(ExpressionUpdateInterface listener : listenerList)
            {
                listener.attributeUpdated(attribute);
            }
        }
        else if(selectedString.compareTo(ExpressionSubPanel.getPanelName()) == 0)
        {
            Expression expression = expressionPanel.getExpression();

            for(ExpressionUpdateInterface listener : listenerList)
            {
                listener.expressionUpdated(expression);
            }
        }

        if(this.field != null)
        {
            this.field.setVisible(enableField);
        }
    }

    /**
     * Populate.
     *
     * @param expression the expression
     */
    public void populate(Expression expression)
    {
        if(expression != null)
        {
            populateAttributeComboxBox(expression);

            if((expression == null) ||
                    (expression instanceof NilExpression) ||
                    (expression instanceof ConstantExpression) ||
                    (expression instanceof LiteralExpressionImpl))
            {
                valuePanel.populateExpression(expression);
            }
            else if(expression instanceof AttributeExpressionImpl)
            {
                setAttribute(expression);
            }
            else
            {
                expressionPanel.populateExpression(expression);
            }
        }
    }

    /**
     * Populate.
     *
     * @param expression the expression
     */
    public void populateAttributeComboxBox(Expression expression) {

        String panelName;

        if(expression == null)
        {
            panelName = ValueSubPanel.getPanelName();
        }
        else if(expression instanceof NilExpression)
        {
            panelName = ValueSubPanel.getPanelName();
        }
        else if(expression instanceof ConstantExpression)
        {
            panelName = ValueSubPanel.getPanelName();
        }
        else if(expression instanceof LiteralExpressionImpl)
        {
            panelName = ValueSubPanel.getPanelName();
        }
        else if(expression instanceof AttributeExpressionImpl)
        {
            panelName = DataSourceAttributePanel.getPanelName();
        }
        else
        {
            panelName = ExpressionSubPanel.getPanelName();
        }

        oldValueObj = panelName;
        attributeChooserComboBox.setSelectedItem(panelName);
    }

    /**
     * Data source loaded.
     *
     * @param geometryType the geometry type
     * @param isConnectedToDataSourceFlag the is connected to data source flag
     */
    /* (non-Javadoc)
     * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceLoaded(com.sldeditor.datasource.impl.GeometryTypeEnum, boolean)
     */
    @Override
    public void dataSourceLoaded(GeometryTypeEnum geometryType, boolean isConnectedToDataSourceFlag) {
        DataSourceInterface dataSource = DataSourceFactory.getDataSource();

        dataSourceAttributePanel.dataSourceLoaded(dataSource);
    }

    /**
     * Show panel.
     *
     * @param selectedItem the selected item
     */
    private void showPanel(String selectedItem) {
        CardLayout cl = (CardLayout)(outerPanel.getLayout());
        cl.show(outerPanel, selectedItem);
    }

    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    /* (non-Javadoc)
     * @see javax.swing.JComponent#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        attributeChooserComboBox.setEnabled(enabled);
        dataSourceAttributePanel.setPanelEnabled(enabled);
        expressionPanel.setPanelEnabled(enabled);
        valuePanel.setPanelEnabled(enabled);
    }

    /**
     * Adds the expression listener.
     *
     * @param listener the listener
     */
    public void addExpressionListener(ExpressionUpdateInterface listener) {
        listenerList.add(listener);
    }

    /**
     * Sets the attribute property.
     *
     * @param expression the new attribute
     */
    public void setAttribute(Expression expression)
    {
        dataSourceAttributePanel.setAttribute(expression);
    }

    /**
     * Gets the expression.
     *
     * @return the expression
     */
    public Expression getExpression()
    {
        Expression expression = null;

        String selectedItem = (String)attributeChooserComboBox.getSelectedItem();

        if(selectedItem.compareToIgnoreCase(ValueSubPanel.getPanelName()) == 0)
        {
            expression = valuePanel.getExpression();
        }
        else if(selectedItem.compareToIgnoreCase(DataSourceAttributePanel.getPanelName()) == 0)
        {
            expression = dataSourceAttributePanel.getExpression();
        }
        else if(selectedItem.compareToIgnoreCase(ExpressionSubPanel.getPanelName()) == 0)
        {
            expression = expressionPanel.getExpression();
        }
        return expression;
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo redo object
     */
    /* (non-Javadoc)
     * @see com.sldeditor.undo.UndoActionInterface#undoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        String oldValueObj = (String)undoRedoObject.getOldValue();

        attributeChooserComboBox.setSelectedItem(oldValueObj);
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo redo object
     */
    /* (non-Javadoc)
     * @see com.sldeditor.undo.UndoActionInterface#redoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        String newValueObj = (String)undoRedoObject.getNewValue();

        attributeChooserComboBox.setSelectedItem(newValueObj);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceAboutToUnloaded(org.geotools.data.DataStore)
     */
    @Override
    public void dataSourceAboutToUnloaded(DataStore dataStore) {
        // Does nothing
    }

    /**
     * Creates the all attributes, value, attribute and expression.
     *
     * @param expectedDataType the expected data type
     * @param field the field
     * @param rasterSymbol the raster symbol
     * @return the attribute selection object
     */
    public static AttributeSelection createAttributes(Class<?> expectedDataType,
            FieldConfigBase field,
            boolean rasterSymbol)
    {
        List<ExpressionTypeEnum> allowedList = new ArrayList<ExpressionTypeEnum>();
        allowedList.add(ExpressionTypeEnum.E_VALUE);
        if(!rasterSymbol)
        {
            allowedList.add(ExpressionTypeEnum.E_ATTRIBUTE);
        }
        allowedList.add(ExpressionTypeEnum.E_EXPRESSION);

        AttributeSelection obj = new AttributeSelection(expectedDataType, field, allowedList);

        return obj;
    }
}
