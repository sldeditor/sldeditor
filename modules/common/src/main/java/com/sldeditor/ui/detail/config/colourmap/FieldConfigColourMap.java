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
package com.sldeditor.ui.detail.config.colourmap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapImpl;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.MultipleFieldInterface;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.widgets.FieldPanel;

/**
 * The Class FieldConfigColourMap wraps a table GUI component and allows a colour map to be configured
 * <p>
 * Supports undo/redo functionality. 
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig} 
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigColourMap extends FieldConfigBase implements UndoActionInterface, ColourMapModelUpdateInterface {

    /** The table. */
    private JTable table;

    /** The default value. */
    private ColorMap defaultValue = new ColorMapImpl();

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The colour map data model. */
    private ColourMapModel model = null;

    /** The add button. */
    private JButton addButton;

    /** The remove button. */
    private JButton removeButton;

    /**
     * Instantiates a new field config string.
     *
     * @param panelId the panel id
     * @param id the id
     * @param label the label
     * @param multipleFields the multiple fields
     */
    public FieldConfigColourMap(Class<?> panelId, FieldId id, String label, boolean multipleFields) {
        super(panelId, id, label, true, multipleFields);

        model = new ColourMapModel(this);
    }

    /**
     * Gets the row y.
     *
     * @param row the row
     * @return the row y
     */
    private static int getRowY(int row)
    {
        return BasePanel.WIDGET_HEIGHT * row;
    }

    /**
     * Creates the ui.
     *
     * @param parentPanel the parent panel
     * @param parentBox the parent box
     */
    @Override
    public void createUI(MultipleFieldInterface parentPanel, Box parentBox) {

        int xPos = getXPos();
        int maxNoOfRows = 12;
        FieldPanel fieldPanel = createFieldPanel(xPos, getRowY(maxNoOfRows), getLabel(), parentPanel, parentBox);

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        table.setBounds(xPos, 0, BasePanel.FIELD_PANEL_WIDTH, getRowY(maxNoOfRows - 2));
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                removeButton.setEnabled(true);
            }});
        model.setCellRenderer(table);

        JScrollPane scrollPanel = new JScrollPane(table);
        scrollPanel.setBounds(xPos, BasePanel.WIDGET_HEIGHT, BasePanel.FIELD_PANEL_WIDTH, BasePanel.WIDGET_HEIGHT * 10);
        fieldPanel.add(scrollPanel);

        int buttonY = getRowY(maxNoOfRows - 1);
        //
        // Add button
        //
        addButton = new JButton(Localisation.getString(FieldConfigBase.class, "FieldConfigColourMap.add"));
        addButton.setBounds(xPos + BasePanel.WIDGET_X_START, buttonY, BasePanel.WIDGET_BUTTON_WIDTH, BasePanel.WIDGET_HEIGHT);
        addButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                addEntry();
            }});
        fieldPanel.add(addButton);

        //
        // Remove button
        //
        removeButton = new JButton(Localisation.getString(FieldConfigBase.class, "FieldConfigColourMap.remove"));
        removeButton.setBounds(xPos + BasePanel.WIDGET_BUTTON_WIDTH + BasePanel.WIDGET_X_START + 10, buttonY, BasePanel.WIDGET_BUTTON_WIDTH, BasePanel.WIDGET_HEIGHT);
        removeButton.setEnabled(false);
        removeButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                removeEntry();
            }});
        fieldPanel.add(removeButton);
    }

    /**
     * Adds a new colour map entry.
     */
    private void addEntry() {
        model.addNewEntry();
        removeButton.setEnabled(false);
    }

    /**
     * Removes the selected colour map entries.
     */
    private void removeEntry() {
        model.removeEntries(table.getSelectionModel().getMinSelectionIndex(), table.getSelectionModel().getMaxSelectionIndex());
        removeButton.setEnabled(false);
    }

    /**
     * Attribute selection.
     *
     * @param field the field
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.AttributeButtonSelectionInterface#attributeSelection(java.lang.String)
     */
    @Override
    public void attributeSelection(String field)
    {
        // Not used
    }

    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    @Override
    public void setEnabled(boolean enabled)
    {
        if(table != null)
        {
            table.setEnabled(enabled);
        }
    }

    /**
     * Generate expression.
     *
     * @return the expression
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#generateExpression()
     */
    @Override
    protected Expression generateExpression()
    {
        Expression expression = null;

        //        if(this.textField != null)
        //        {
        //            expression = getFilterFactory().literal(textField.getText());
        //        }
        return expression;
    }

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        if((attributeSelectionPanel != null) && !isValueOnly())
        {
            return attributeSelectionPanel.isEnabled();
        }
        else
        {
            if(table != null)
            {
                return table.isEnabled();
            }
        }
        return false;
    }

    /**
     * Revert to default value.
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#revertToDefaultValue()
     */
    @Override
    public void revertToDefaultValue()
    {
        populateField(defaultValue);
    }

    /**
     * Populate expression.
     *
     * @param objValue the obj value
     * @param opacity the opacity
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)
     */
    @Override
    public void populateExpression(Object objValue, Expression opacity)
    {
        String sValue = (String) objValue;

        populateField(sValue);
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    @Override
    public ColorMap getColourMap()
    {
        if(table != null)
        {
            return model.getColourMap();
        }
        return null;
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject)
    {
        if(table != null)
        {
            ColorMap oldValue = (ColorMap)undoRedoObject.getOldValue();

            populateField(oldValue);
        }
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject)
    {
        if(table != null)
        {
            ColorMap newValue = (ColorMap)undoRedoObject.getNewValue();

            populateField(newValue);
        }
    }

    /**
     * Sets the test string value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldId fieldId, ColorMap testValue) {
        populateField(testValue);
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    @Override
    public void populateField(ColorMap value) {
        if(model != null)
        {
            model.populate(value);

            UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, value));

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
        FieldConfigColourMap copy = new FieldConfigColourMap(fieldConfigBase.getPanelId(),
                fieldConfigBase.getFieldId(),
                fieldConfigBase.getLabel(),
                fieldConfigBase.hasMultipleValues());
        return copy;
    }

    /**
     * Gets the class type supported.
     *
     * @return the class type
     */
    @Override
    public Class<?> getClassType() {
        return ColorMap.class;
    }

    /**
     * Sets the field visible.
     *
     * @param visible the new visible state
     */
    @Override
    public void setVisible(boolean visible) {
        if(table != null)
        {
            table.setVisible(visible);
        }
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    @Override
    public String getStringValue() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.colourmap.ColourMapModelUpdateInterface#colourMapUpdated()
     */
    @Override
    public void colourMapUpdated() {
        ColorMap colourMap = model.getColourMap();

        UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, colourMap));

        oldValueObj = colourMap;

        valueUpdated();
    }
}
