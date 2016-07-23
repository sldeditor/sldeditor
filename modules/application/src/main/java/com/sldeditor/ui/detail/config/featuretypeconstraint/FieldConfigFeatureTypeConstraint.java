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
package com.sldeditor.ui.detail.config.featuretypeconstraint;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.geotools.styling.FeatureTypeConstraint;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.widgets.FieldPanel;

/**
 * The Class FieldConfigFeatureTypeConstraint wraps a table GUI component and 
 * allows feature type constraints to be configured
 * <p>
 * Supports undo/redo functionality. 
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig} 
 * 
 * @author Robert Ward (SCISYS)
 */
/**
 * @author Robert Ward (SCISYS)
 *
 */
public class FieldConfigFeatureTypeConstraint extends FieldConfigBase implements UndoActionInterface, FeatureTypeConstraintModelUpdateInterface {

    /** The table. */
    private JTable table;

    /** The default value. */
    private Object defaultValue = null;

    /** The old value obj. */
    private List<FeatureTypeConstraint> oldValueObj = null;

    /** The add button. */
    private JButton addButton;

    /** The remove button. */
    private JButton removeButton;

    /** The feature type constraint map data model. */
    private FeatureTypeConstraintModel model = null;

    /**
     * Instantiates a new field config string.
     *
     * @param panelId the panel id
     * @param id the id
     * @param label the label
     */
    public FieldConfigFeatureTypeConstraint(Class<?> panelId, FieldId id, String label) {
        super(panelId, id, label, true);

        model = new FeatureTypeConstraintModel(this);
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
     * @param parentBox the parent box
     */
    @Override
    public void createUI(Box parentBox) {

        int xPos = getXPos();
        int maxNoOfRows = 12;
        FieldPanel fieldPanel = createFieldPanel(xPos, getRowY(maxNoOfRows), getLabel(), parentBox);

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        table.setBounds(xPos, 0, BasePanel.FIELD_PANEL_WIDTH, getRowY(maxNoOfRows - 2));
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                removeButton.setEnabled(true);
            }});

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
     * Adds a new feature type constraint entry.
     */
    private void addEntry() {
        model.addNewEntry();
        removeButton.setEnabled(false);
    }

    /**
     * Removes the selected feature type constraint entries.
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
        // Do nothing
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
     * Undo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject)
    {
        if((table != null) && (undoRedoObject != null))
        {
            List<FeatureTypeConstraint> oldValue = (List<FeatureTypeConstraint>)undoRedoObject.getOldValue();

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
        if((table != null) && (undoRedoObject != null))
        {
            List<FeatureTypeConstraint> newValue = (List<FeatureTypeConstraint>)undoRedoObject.getNewValue();

            populateField(newValue);
        }
    }

    /**
     * Gets the feature type constraint.
     *
     * @return the feature type constraint
     */
    @Override
    public List<FeatureTypeConstraint> getFeatureTypeConstraint()
    {
        return null;
    }

    /**
     * Sets the test string value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldId fieldId, List<FeatureTypeConstraint> testValue) {
        populateField(testValue);
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    @Override
    public void populateField(List<FeatureTypeConstraint> valueList) {
        if(model != null)
        {
            if(valueList != null)
            {
                model.populate(valueList);

                UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, valueList));

                oldValueObj = valueList;

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
        FieldConfigFeatureTypeConstraint copy = null;

        if(fieldConfigBase != null)
        {
            copy = new FieldConfigFeatureTypeConstraint(fieldConfigBase.getPanelId(),
                    fieldConfigBase.getFieldId(),
                    fieldConfigBase.getLabel());
        }
        return copy;
    }

    /**
     * Gets the class type supported.
     *
     * @return the class type
     */
    @Override
    public Class<?> getClassType() {
        return FeatureTypeConstraint.class;
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
     * @see com.sldeditor.ui.detail.config.featuretypeconstraint.FeatureTypeConstraintModelUpdateInterface#featureTypeConstraintUpdated()
     */
    @Override
    public void featureTypeConstraintUpdated() {
        List<FeatureTypeConstraint> ftc = model.getFeatureTypeConstraint();

        UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, ftc));

        oldValueObj = ftc;

        valueUpdated();
    }
}
