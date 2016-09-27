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

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.ColorMapImpl;
import org.opengis.filter.expression.Expression;

import com.sldeditor.colourramp.ColourRampConfigPanel;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.widgets.FieldPanel;

/**
 * The Class FieldConfigColourMap wraps a table GUI component and allows a colour map to be configured.
 * <p>
 * Supports undo/redo functionality. 
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig} 
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigColourMap extends FieldConfigBase implements UndoActionInterface, ColourMapModelUpdateInterface, ColourMapEntryUpdateInterface {

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

    /** The colour ramp configuration panel. */
    private ColourRampConfigPanel colourRampConfig = null;

    /** The colour map entry panel. */
    private ColourMapEntryPanel colourMapEntryPanel;

    /**
     * Instantiates a new field config string.
     *
     * @param commonData the common data
     */
    public FieldConfigColourMap(FieldConfigCommonData commonData) {
        super(commonData);

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
     */
    @Override
    public void createUI() {

        int xPos = getXPos();
        int maxNoOfConfigRows = 7;
        int maxNoOfTableRows = 12;
        int totalRows = maxNoOfConfigRows + maxNoOfTableRows + ColourMapEntryPanel.getNoOfRows();
        FieldPanel fieldPanel = createFieldPanel(xPos, getRowY(totalRows), getLabel());

        colourRampConfig = new ColourRampConfigPanel(this, model);
        colourRampConfig.setBounds(xPos, 0, BasePanel.FIELD_PANEL_WIDTH, getRowY(maxNoOfConfigRows));
        fieldPanel.add(colourRampConfig);

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        table.setBounds(xPos, getRowY(maxNoOfConfigRows), BasePanel.FIELD_PANEL_WIDTH, getRowY(totalRows - 2));
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting())
                {
                    removeButton.setEnabled(true);

                    ColorMapEntry entry = model.getColourMapEntry(table.getSelectedRow());
                    colourMapEntryPanel.setSelectedEntry(entry);
                }
            }});
        model.setCellRenderer(table);

        JScrollPane scrollPanel = new JScrollPane(table);
        int endOfTableRow = maxNoOfConfigRows + maxNoOfTableRows - 2;
        scrollPanel.setBounds(xPos, getRowY(maxNoOfConfigRows), BasePanel.FIELD_PANEL_WIDTH, getRowY(endOfTableRow) - getRowY(maxNoOfConfigRows));
        fieldPanel.add(scrollPanel);

        int buttonY = getRowY(endOfTableRow);
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

        colourMapEntryPanel = new ColourMapEntryPanel(getPanelId(), this);
        colourMapEntryPanel.setBounds(xPos, getRowY(maxNoOfConfigRows + maxNoOfTableRows - 1), BasePanel.FIELD_PANEL_WIDTH, colourMapEntryPanel.getPanelHeight());
        fieldPanel.add(colourMapEntryPanel);
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
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#populateExpression(java.lang.Object)
     */
    @Override
    public void populateExpression(Object objValue)
    {
        // Do nothing
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
        if((table != null) && (undoRedoObject != null))
        {
            if(undoRedoObject.getOldValue() instanceof ColorMap)
            {
                ColorMap oldValue = (ColorMap)undoRedoObject.getOldValue();

                populateField(oldValue);
            }
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
        if((table != null) && (undoRedoObject != null))
        {
            if(undoRedoObject.getNewValue() instanceof ColorMap)
            {
                ColorMap newValue = (ColorMap)undoRedoObject.getNewValue();

                populateField(newValue);
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
            if(value != null)
            {
                if(colourRampConfig != null)
                {
                    colourRampConfig.populate(value);
                }
                model.populate(value);

                if(colourMapEntryPanel != null)
                {
                    colourMapEntryPanel.setSelectedEntry(null);
                }

                UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, value));

                oldValueObj = value;

                valueUpdated();
            }
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
        FieldConfigColourMap copy = null;

        if(fieldConfigBase != null)
        {
            copy = new FieldConfigColourMap(fieldConfigBase.getCommonData());
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

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.colourmap.ColourMapEntryUpdateInterface#colourMapEntryUpdated(com.sldeditor.ui.detail.config.colourmap.ColourMapData)
     */
    @Override
    public void colourMapEntryUpdated(ColourMapData data) {
        if(model != null)
        {
            model.updateColourMapEntry(table.getSelectedRow(), data);
        }
        removeButton.setEnabled(false);
    }
}
