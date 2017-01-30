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

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.geotools.styling.FeatureTypeConstraint;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.ExpressionPanelFactory;
import com.sldeditor.filter.FilterPanelInterface;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
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
public class FieldConfigFeatureTypeConstraint extends FieldConfigBase implements UndoActionInterface, FeatureTypeConstraintModelUpdateInterface {

    /** The filter table. */
    private JTable filterTable;

    /** The extent table. */
    private JTable extentTable;

    /** The old value obj. */
    private List<FeatureTypeConstraint> oldValueObj = null;

    /** The add feature type button. */
    private JButton addFTCButton;

    /** The remove feature type button. */
    private JButton removeFTCButton;

    /** The add extent button. */
    private JButton addExtentButton;

    /** The remove extent button. */
    private JButton removeExtentButton;

    /** The feature type constraint map data model. */
    private FeatureTypeConstraintModel filterModel = null;

    /** The extent model. */
    private ExtentModel extentModel = null;

    /**
     * Instantiates a new field config string.
     *
     * @param commonData the common data
     */
    public FieldConfigFeatureTypeConstraint(FieldConfigCommonData commonData) {
        super(commonData);

        filterModel = new FeatureTypeConstraintModel(this);
        extentModel = new ExtentModel(this);
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
        int maxNoOfFilterRows = 6;
        int maxNoOfExtentRows = 6;
        int maxY = getRowY(maxNoOfFilterRows + maxNoOfExtentRows);
        FieldPanel fieldPanel = createFieldPanel(xPos, maxY, getLabel());

        createFilterTable(xPos, maxNoOfFilterRows, fieldPanel);
        createExtentTable(xPos, maxNoOfFilterRows, maxNoOfExtentRows, fieldPanel);
    }

    /**
     * Creates the extent table.
     *
     * @param xPos the x pos
     * @param maxNoOfRows the max no of rows
     * @param fieldPanel the field panel
     */
    private void createExtentTable(int xPos, int startRows, int noOfRows, FieldPanel fieldPanel) {
        int maxNoOfRows = startRows + noOfRows;
        extentTable = new JTable(extentModel);
        extentTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        extentTable.setBounds(xPos, getRowY(startRows + 1), BasePanel.FIELD_PANEL_WIDTH, getRowY(noOfRows - 2));
        extentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                removeExtentButton.setEnabled(true);
            }});

        JScrollPane scrollPanel = new JScrollPane(extentTable);
        scrollPanel.setBounds(xPos, getRowY(startRows), BasePanel.FIELD_PANEL_WIDTH, BasePanel.WIDGET_HEIGHT * (noOfRows - 2));
        fieldPanel.add(scrollPanel);

        int buttonY = getRowY(maxNoOfRows - 2);
        //
        // Add button
        //
        addExtentButton = new JButton(Localisation.getString(FieldConfigBase.class, "FieldConfigFeatureTypeConstraint.add"));
        addExtentButton.setBounds(xPos + BasePanel.WIDGET_X_START, buttonY, BasePanel.WIDGET_BUTTON_WIDTH, BasePanel.WIDGET_HEIGHT);
        addExtentButton.setEnabled(false);
        addExtentButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                addExtentEntry();
            }});
        fieldPanel.add(addExtentButton);

        //
        // Remove button
        //
        removeExtentButton = new JButton(Localisation.getString(FieldConfigBase.class, "FieldConfigFeatureTypeConstraint.remove"));
        removeExtentButton.setBounds(xPos + BasePanel.WIDGET_BUTTON_WIDTH + BasePanel.WIDGET_X_START + 10, buttonY, BasePanel.WIDGET_BUTTON_WIDTH, BasePanel.WIDGET_HEIGHT);
        removeExtentButton.setEnabled(false);
        removeExtentButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                removeExtentEntry();
            }});
        fieldPanel.add(removeExtentButton);
    }

    /**
     * Creates the filter table.
     *
     * @param xPos the x pos
     * @param maxNoOfRows the max no of rows
     * @param fieldPanel the field panel
     */
    private void createFilterTable(int xPos, int maxNoOfRows, FieldPanel fieldPanel) {
        filterTable = new JTable(filterModel);
        filterTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        filterTable.setBounds(xPos, 0, BasePanel.FIELD_PANEL_WIDTH, getRowY(maxNoOfRows - 2));
        filterTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting())
                {
                    FeatureTypeConstraint ftc = filterModel.getFeatureTypeConstraint(filterTable.getSelectedRow());
                    if(ftc != null)
                    {
                        extentModel.populate(ftc.getExtents());
                        addExtentButton.setEnabled(true);
                        removeFTCButton.setEnabled(true);

                        int[] selectedColumns = filterTable.getSelectedColumns();

                        if(filterModel.isFilterColumn(selectedColumns))
                        {
                            FilterPanelInterface filterPanel = ExpressionPanelFactory.getFilterPanel(null);

                            String panelTitle = Localisation.getString(FieldConfigBase.class, "FieldConfigFeatureTypeConstraint.filterPanel");
                            filterPanel.configure(panelTitle,
                                    Object.class,
                                    SelectedSymbol.getInstance().isRasterSymbol());

                            filterPanel.populate(ftc.getFilter());

                            if(filterPanel.showDialog())
                            {
                                ftc.setFilter(filterPanel.getFilter());

                                filterModel.fireTableDataChanged();

                                featureTypeConstraintUpdated();
                            }
                        }
                    }
                }
            }});

        JScrollPane scrollPanel = new JScrollPane(filterTable);
        scrollPanel.setBounds(xPos, BasePanel.WIDGET_HEIGHT, BasePanel.FIELD_PANEL_WIDTH, BasePanel.WIDGET_HEIGHT * (maxNoOfRows - 2));
        fieldPanel.add(scrollPanel);

        int buttonY = getRowY(maxNoOfRows - 1);
        //
        // Add button
        //
        addFTCButton = new JButton(Localisation.getString(FieldConfigBase.class, "FieldConfigFeatureTypeConstraint.add"));
        addFTCButton.setBounds(xPos + BasePanel.WIDGET_X_START, buttonY, BasePanel.WIDGET_BUTTON_WIDTH, BasePanel.WIDGET_HEIGHT);
        addFTCButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                addEntry();
            }});
        fieldPanel.add(addFTCButton);

        //
        // Remove button
        //
        removeFTCButton = new JButton(Localisation.getString(FieldConfigBase.class, "FieldConfigFeatureTypeConstraint.remove"));
        removeFTCButton.setBounds(xPos + BasePanel.WIDGET_BUTTON_WIDTH + BasePanel.WIDGET_X_START + 10, buttonY, BasePanel.WIDGET_BUTTON_WIDTH, BasePanel.WIDGET_HEIGHT);
        removeFTCButton.setEnabled(false);
        removeFTCButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                removeEntry();
            }});
        fieldPanel.add(removeFTCButton);
    }

    /**
     * Adds a new feature type constraint entry.
     */
    private void addEntry() {
        filterModel.addNewEntry();
        extentModel.populate(null);
        removeFTCButton.setEnabled(false);
        addExtentButton.setEnabled(false);
        removeExtentButton.setEnabled(false);
    }

    /**
     * Removes the selected feature type constraint entries.
     */
    private void removeEntry() {
        filterModel.removeEntries(filterTable.getSelectionModel().getMinSelectionIndex(), filterTable.getSelectionModel().getMaxSelectionIndex());
        extentModel.populate(null);
        removeFTCButton.setEnabled(false);
        addExtentButton.setEnabled(false);
        removeExtentButton.setEnabled(false);
    }

    /**
     * Adds a new extent entry.
     */
    private void addExtentEntry() {
        extentModel.addNewEntry();
        removeExtentButton.setEnabled(false);
    }

    /**
     * Removes the selected extent entries.
     */
    private void removeExtentEntry() {
        extentModel.removeEntries(extentTable.getSelectionModel().getMinSelectionIndex(), extentTable.getSelectionModel().getMaxSelectionIndex());
        removeExtentButton.setEnabled(false);
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
    public void internal_setEnabled(boolean enabled)
    {
        if(filterTable != null)
        {
            filterTable.setEnabled(enabled);
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
        if(filterTable != null)
        {
            return filterTable.isEnabled();
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
        if((filterTable != null) && (undoRedoObject != null))
        {
            try
            {
                @SuppressWarnings("unchecked")
                List<FeatureTypeConstraint> oldValue = (List<FeatureTypeConstraint>)undoRedoObject.getOldValue();

                populateField(oldValue);
            }
            catch(ClassCastException e)
            {
                // Ignore
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
        if((filterTable != null) && (undoRedoObject != null))
        {
            try
            {
                @SuppressWarnings("unchecked")
                List<FeatureTypeConstraint> newValue = (List<FeatureTypeConstraint>)undoRedoObject.getNewValue();

                populateField(newValue);
            }
            catch(ClassCastException e)
            {
                // Ignore
            }
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
        return filterModel.getFeatureTypeConstraint();
    }

    /**
     * Sets the test string value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldIdEnum fieldId, List<FeatureTypeConstraint> testValue) {
        populateField(testValue);
    }

    /**
     * Populate field.
     *
     * @param valueList the value list
     */
    @Override
    public void populateField(List<FeatureTypeConstraint> valueList) {
        if(filterModel != null)
        {
            if(valueList != null)
            {
                filterModel.populate(valueList);

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
            copy = new FieldConfigFeatureTypeConstraint(fieldConfigBase.getCommonData());
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
        if(filterTable != null)
        {
            filterTable.setVisible(visible);
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
        List<FeatureTypeConstraint> ftc = filterModel.getFeatureTypeConstraint();

        UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, ftc));

        oldValueObj = ftc;

        valueUpdated();
    }

    @Override
    public void extentUpdated() {
        if(filterTable != null)
        {
            FeatureTypeConstraint ftc = filterModel.getFeatureTypeConstraint(filterTable.getSelectedRow());
            if(ftc != null)
            {
                extentModel.updateExtent(ftc);
            }

            featureTypeConstraintUpdated();
        }
    }
}
