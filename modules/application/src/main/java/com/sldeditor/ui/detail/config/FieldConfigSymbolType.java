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

import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.symboltype.FieldState;
import com.sldeditor.ui.iface.MultiOptionSelectedInterface;
import com.sldeditor.ui.iface.ValueComboBoxDataSelectedInterface;
import com.sldeditor.ui.menucombobox.MenuComboBox;
import com.sldeditor.ui.widgets.FieldPanel;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import com.sldeditor.ui.widgets.ValueComboBoxDataGroup;

/**
 * The Class FieldConfigSymbolType allows a user to select a symbol from a list. Depending on the symbol
 * selected further field data can be configured in the {@link #containingPanel}.
 * <p>
 * The field wraps:
 * <ul>
 * <li>a drop down GUI component ({@link com.sldeditor.ui.menucombobox.MenuComboBox})</li>
 * <li>an optional value/attribute/expression drop down, ({@link com.sldeditor.ui.attribute.AttributeSelection})</li>
 * </ul>
 * <p>
 * The {@link #containingPanel} is a CardLayout.  Fields are added to the class using the {@link #addField} method.
 * A field provides symbol types to be added to the MenuComboBox and a panel to be displayed if the symbol type is selected.
 * <p>
 * The symbols present in the list depend on the symbolizer being edited and the vendor options allowed by the user.
 * <p>
 * Supports undo/redo functionality. 
 * <p> 
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig}
 * <p>
 * <img src="./doc-files/symboltypefield.png" />
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigSymbolType extends FieldConfigBase implements UndoActionInterface, ValueComboBoxDataSelectedInterface {

    /** The containing panel, contains the panels to be displayed when a symbol type is selected. */
    private JPanel containingPanel = null;

    /** The vendor option map. */
    private Map<Class<?>, VendorOptionVersion> vendorOptionMap = new HashMap<Class<?>, VendorOptionVersion>();

    /**
     * The field configuration map.
     * <p>
     * Key is panel id string the field appears on.
     */
    private Map<Class<?>, FieldConfigBase> fieldConfigMap = new HashMap<Class<?>, FieldConfigBase>();

    /** The symbol selected listener. */
    private MultiOptionSelectedInterface symbolSelectedListener = null;

    /** The logger. */
    private static Logger logger = Logger.getLogger(FieldConfigSymbolType.class);

    /** The menu combo box containing all the symbols that can be selected. */
    private MenuComboBox comboBox = null;

    /** The old value obj. */
    private Object oldValueObj = null;

    /**
     * Instantiates a new FieldConfigSymbolType.
     *
     * @param commonData the common data
     */
    public FieldConfigSymbolType(FieldConfigCommonData commonData) {
        super(commonData);
    }

    /**
     * Creates the ui.
     */
    @Override
    public void createUI() {

        int xPos = getXPos();
        FieldPanel fieldPanel = createFieldPanel(xPos, getLabel());

        comboBox = new MenuComboBox(this);
        comboBox.setBounds(xPos + BasePanel.WIDGET_X_START, 0, BasePanel.WIDGET_STANDARD_WIDTH, BasePanel.WIDGET_HEIGHT);

        // Register for changes in vendor option selections
        PrefManager.getInstance().addVendorOptionListener(comboBox);
        fieldPanel.add(comboBox);

        if(!isValueOnly())
        {
            setAttributeSelectionPanel(fieldPanel.internalCreateAttrButton(String.class, this, isRasterSymbol()));
        }

        // Create 
        containingPanel = new JPanel();
        containingPanel.setLayout(new CardLayout());
        containingPanel.setBounds(0, 0, BasePanel.WIDGET_STANDARD_WIDTH, BasePanel.WIDGET_HEIGHT * 3);

        addCustomPanel(containingPanel);
    }

    /**
     * Adds the field to the symbol type field.
     *
     * @param symbolType the symbol type
     */
    public void addField(FieldState symbolType)
    {
        if(symbolType != null)
        {
            FieldConfigBase fieldConfig = symbolType.getConfigField();
            Class<?> panelId = symbolType.getClass();
            VendorOptionVersion vendorOption = symbolType.getVendorOption();

            if(fieldConfig == null)
            {
                ConsoleManager.getInstance().error(this, "FieldConfigSymbolType.addPanel passed a field config as null");
            }
            else
            {
                // Add to card layout
                containingPanel.add(fieldConfig.getPanel(), panelId.getName());

                fieldConfigMap.put(panelId, fieldConfig);
                vendorOptionMap.put(panelId, vendorOption);

                fieldConfig.setExpressionUpdateListener(this);
                fieldConfig.setParent(this);
            }
        }
    }

    /**
     * Populate the field from the current SLD symbol.
     *
     * @param symbolSelectedListener the symbol selected listener
     * @param dataSelectionList the data selection list
     */
    public void populate(MultiOptionSelectedInterface symbolSelectedListener, List<ValueComboBoxDataGroup> dataSelectionList)
    {
        this.symbolSelectedListener = symbolSelectedListener;

        if(comboBox != null)
        {
            comboBox.initialiseMenu(dataSelectionList);
        }
    }

    /**
     * Gets the selected value.
     *
     * @return the selected value
     */
    public Class<?> getSelectedValue() {
        ValueComboBoxData selectedValueObj = getSelectedValueObj();
        if(selectedValueObj == null)
        {
            return null;
        }
        return selectedValueObj.getPanelId();
    }

    /**
     * Gets the selected value obj.
     *
     * @return the selected value obj
     */
    public ValueComboBoxData getSelectedValueObj()
    {
        if(comboBox == null)
        {
            return null;
        }
        return comboBox.getSelectedData();
    }

    /**
     * Sets the selected item.
     *
     * @param key the new selected item
     */
    public void setSelectedItem(String key) {
        populateField(key);
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
        // Do nothing
    }

    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled)
    {
        if(comboBox != null)
        {
            comboBox.setEnabled(enabled);
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

        if(comboBox != null)
        {
            ValueComboBoxData data = comboBox.getSelectedData();

            if(data != null)
            {
                FieldConfigBase fieldConfig = fieldConfigMap.get(data.getPanelId());

                if(fieldConfig == null)
                {
                    ConsoleManager.getInstance().error(this, "Failed to find field for :" + data.getPanelId().getName());
                }
                else
                {
                    expression = fieldConfig.getExpression();

                    if((expression == null) && fieldConfig.isASingleValue())
                    {
                        ValueComboBoxData value = getEnumValue();
                        if(value != null)
                        {
                            expression = getFilterFactory().literal(value.getKey());
                        }
                    }
                }
            }
        }
        return expression;
    }

    /**
     * Checks if field is enabled.
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
            if(comboBox != null)
            {
                return comboBox.isEnabled();
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
        // Empty
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
        if(objValue instanceof String)
        {
            populateField((String)objValue);
        }
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    @Override
    public ValueComboBoxData getEnumValue()
    {
        if(comboBox == null)
        {
            return null;
        }
        return comboBox.getSelectedData();
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#getStringValue()
     */
    @Override
    public String getStringValue()
    {
        ValueComboBoxData enumValue = getEnumValue();
        if(enumValue == null)
        {
            return null;
        }
        return enumValue.getKey();
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
    public void undoAction(UndoInterface undoRedoObject)
    {
        if(undoRedoObject != null)
        {
            if(undoRedoObject.getOldValue() instanceof String)
            {
                String oldValue = (String)undoRedoObject.getOldValue();

                populateField(oldValue);
            }
        }
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
    public void redoAction(UndoInterface undoRedoObject)
    {
        if(undoRedoObject != null)
        {
            if(undoRedoObject.getNewValue() instanceof String)
            {
                String newValue = (String)undoRedoObject.getNewValue();

                populateField(newValue);
            }
        }
    }

    /**
     * Symbol type menu option selected.
     *
     * @param selectedData the selected data
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.menucombobox.ValueComboBoxDataSelectedInterface#optionSelected(com.sldeditor.ui.ValueComboBoxData)
     */
    @Override
    public void optionSelected(ValueComboBoxData selectedData) {

        if(selectedData != null)
        {
            String newValueObj = selectedData.getKey();

            // Show the correct panel in the card layout for the selected symbol type
            CardLayout cl = (CardLayout)(containingPanel.getLayout());

            String name = selectedData.getPanelId().getName();
            cl.show(containingPanel, name);

            FieldConfigBase fieldConfig = fieldConfigMap.get(selectedData.getPanelId());

            if(fieldConfig == null)
            {
                ConsoleManager.getInstance().error(this, "Failed to find field config for panel id :" + selectedData.getPanelId());
            }
            else
            {
                fieldConfig.justSelected();

                JPanel p = fieldConfig.getPanel();

                Dimension preferredSize = null;
                if(p.isPreferredSizeSet())
                {
                    preferredSize = p.getPreferredSize();
                }
                else
                {
                    preferredSize = new Dimension(BasePanel.FIELD_PANEL_WIDTH, BasePanel.WIDGET_HEIGHT);
                }
                containingPanel.setPreferredSize(preferredSize);

                if(oldValueObj == null)
                {
                    oldValueObj = comboBox.getDefaultValue();
                }

                UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, newValueObj));

                oldValueObj = new String(newValueObj);

                valueUpdated();

                if(symbolSelectedListener != null)
                {
                    logger.debug(String.format("Field %s selected %s", getFieldId(), selectedData.getKey()));

                    symbolSelectedListener.optionSelected(selectedData.getPanelId(), selectedData.getKey());
                }
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
    public void setTestValue(FieldId fieldId, String testValue) {
        populateField(testValue);

        valueUpdated();
    }

    /**
     * Populate field.
     *
     * @param key the key
     */
    @Override
    public void populateField(String key) {
        if(comboBox != null)
        {
            comboBox.setSelectedDataKey(key);
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
        FieldConfigSymbolType copy = null;

        if(fieldConfigBase != null)
        {
            copy = new FieldConfigSymbolType(fieldConfigBase.getCommonData());
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
        if(comboBox != null)
        {
            comboBox.setVisible(visible);
        }
    }
}
