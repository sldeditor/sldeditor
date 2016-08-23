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
package com.sldeditor.ui.detail.config.base;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.widgets.FieldPanel;
import com.sldeditor.ui.widgets.ValueComboBox;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * The Class MultiOptionGroup represents the configuration for a drop down list and a panel
 * displayed below it when a item is selected.  The panels are different for each drop down list
 * option.
 * <p>The group of fields to be displayed and the drop down label used to display it are configured
 * as an {@link OptionGroup}
 * 
 * @author Robert Ward (SCISYS)
 */
public class MultiOptionGroup implements GroupConfigInterface, UndoActionInterface {

    /** The id. */
    private GroupIdEnum id;

    /** The label. */
    private String label;

    /** The show title. */
    private boolean showLabel = true;

    /** The optional. */
    private boolean optional = false;

    /** The option map. */
    private Map<String, OptionGroup> optionMap = new HashMap<String, OptionGroup>();

    /** The option list. */
    private List<OptionGroup> optionList = new ArrayList<OptionGroup>();

    /** The option field list. */
    private List<FieldConfigBase> optionFieldList = new ArrayList<FieldConfigBase>();

    /** The combo box. */
    private ValueComboBox comboBox;

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The option panel. */
    private JPanel optionPanel = null;

    /** The field config manager. */
    private GraphicPanelFieldManager fieldConfigManager = null;

    private Box parentBox = null;

    private FieldPanel fieldPanel;

    /** The multi option group enabled flag. */
    private boolean multiOptionGroupEnabled = true;

    /**
     * Sets the label.
     *
     * @param label the new label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Sets the show label flag.
     *
     * @param showLabel the new show label flag value
     */
    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }

    /**
     * Checks if is optional.
     *
     * @return the optional
     */
    public boolean isOptional() {
        return optional;
    }

    /**
     * Sets the optional.
     *
     * @param optional the optional to set
     */
    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    /**
     * Gets the group list.
     *
     * @return the group list
     */
    public List<OptionGroup> getGroupList() {
        return optionList;
    }

    /**
     * Adds the group.
     *
     * @param group the group
     */
    public void addGroup(OptionGroup group) {
        this.optionMap.put(group.getId().toString(), group);
        this.optionList.add(group);
    }

    /**
     * Creates the ui.
     *
     * @param fieldConfigManager the field config manager
     * @param box the box
     * @param panelId the panel id
     * @return the field panel
     */
    public List<FieldConfigBase> createUI(GraphicPanelFieldManager fieldConfigManager, Box box, Class<?> panelId) {
        final UndoActionInterface parentObj = this;
        this.fieldConfigManager = fieldConfigManager;
        this.parentBox = box;

        List<FieldConfigBase> fdmList = new ArrayList<FieldConfigBase>();

        box.add(GroupConfig.createSeparator());

        fieldPanel = new FieldPanel(0, "", BasePanel.WIDGET_HEIGHT * 2);
        // Set up title
        if(isOptional())
        {
            JCheckBox groupTitle = new JCheckBox(getLabel());
            groupTitle.setBounds(0, 0, GroupConfig.FULL_WIDTH, BasePanel.WIDGET_HEIGHT);
            groupTitle.setOpaque(true);
            fieldPanel.add(groupTitle);
            multiOptionGroupEnabled = false;

            groupTitle.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    enable(groupTitle.isSelected());
                }});
        }
        else
        {
            JLabel groupTitle = new JLabel(getLabel());
            groupTitle.setBounds(0, 0, GroupConfig.FULL_WIDTH, BasePanel.WIDGET_HEIGHT);
            groupTitle.setOpaque(true);
            fieldPanel.add(groupTitle);
        }

        // Set up options in the drop down
        List<ValueComboBoxData> valueComboDataMap = new ArrayList<ValueComboBoxData>();

        for(OptionGroup optionGroup : optionList)
        {
            valueComboDataMap.add(new ValueComboBoxData(optionGroup.getId().toString(), optionGroup.getLabel(), panelId));
        }

        comboBox = new ValueComboBox();
        comboBox.initialiseSingle(valueComboDataMap);
        comboBox.setBounds(BasePanel.WIDGET_X_START, BasePanel.WIDGET_HEIGHT, BasePanel.WIDGET_STANDARD_WIDTH, BasePanel.WIDGET_HEIGHT);
        fieldPanel.add(comboBox);
        comboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                optionSelected(parentBox, fieldConfigManager, fieldPanel, parentObj);
            }
        });

        box.add(fieldPanel);

        return fdmList;
    }

    /**
     * Option selected.
     *
     * @param box the box
     * @param fieldConfigManager the field config manager
     * @param panel the panel
     * @param parentObj the parent obj
     */
    private void optionSelected(Box box, GraphicPanelFieldManager fieldConfigManager, FieldPanel panel, UndoActionInterface parentObj)
    {
        if (comboBox.getSelectedItem() != null) {

            ValueComboBoxData value = comboBox.getSelectedValue();

            if(value != null)
            {
                // Remove the fields from the previous selection
                removeOptionFields(box, fieldConfigManager);

                optionPanel = new JPanel();
                optionPanel.setLayout(new BorderLayout());

                Box optionBox = Box.createVerticalBox();
                optionPanel.add(optionBox, BorderLayout.CENTER);

                int index = findOptionPanel(box, panel);
                OptionGroup optionGroup = optionMap.get(value.getKey());
                for(GroupConfig groupConfig : optionGroup.getGroupList())
                {
                    groupConfig.createTitle(optionBox, null);

                    for(FieldConfigBase field : groupConfig.getFieldConfigList())
                    {
                        field.createUI();
                        FieldPanel component = field.getPanel();
                        optionBox.add(component);
                        fieldConfigManager.addField(field);
                        optionFieldList.add(field);
                    }

                    for(GroupConfig subGroup : groupConfig.getSubGroupList())
                    {
                        subGroup.createTitle(optionBox, null);

                        for(FieldConfigBase field : subGroup.getFieldConfigList())
                        {
                            field.createUI();
                            FieldPanel component = field.getPanel();
                            optionBox.add(component);
                            fieldConfigManager.addField(field);
                            optionFieldList.add(field);
                        }
                    }
                }

                box.add(optionPanel, index + 1);
                Object newValueObj = value.getKey();

                if((oldValueObj == null) && comboBox.getItemCount() > 0)
                {
                    oldValueObj = comboBox.getFirstItem().getKey();
                }

                UndoManager.getInstance().addUndoEvent(new UndoEvent(parentObj, "Multi option : " + getId(), oldValueObj, newValueObj));

                oldValueObj = newValueObj;
                box.revalidate();
            }
        }        
    }

    /**
     * Removes the option fields from the previous selection.
     *
     * @param box the box
     * @param fieldConfigManager the field config manager
     */
    private void removeOptionFields(Box box, GraphicPanelFieldManager fieldConfigManager)
    {
        if(optionPanel != null)
        {
            box.remove(optionPanel);

            for(FieldConfigBase field : optionFieldList)
            {
                fieldConfigManager.removeField(field);
            }
            optionFieldList.clear();

            optionPanel = null;

            box.revalidate();
        }
    }

    /**
     * Find option panel.
     *
     * @param box the box
     * @param panel the panel
     * @return the int
     */
    private int findOptionPanel(Box box, FieldPanel panel)
    {
        int index;
        for(index = 0; index < box.getComponentCount(); index ++)
        {
            if(box.getComponent(index) == panel)
            {
                return index;
            }
        }
        return -1;
    }

    /**
     * Creates the title.
     *
     * @param textPanel the text panel
     * @param parent the parent
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.GroupConfigInterface#createTitle(javax.swing.Box)
     */
    @Override
    public void createTitle(Box textPanel, UpdateSymbolInterface parent) {
    }

    /**
     * Enable.
     *
     * @param enable the enable
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.GroupConfigInterface#enable(boolean)
     */
    @Override
    public void enable(boolean enable) {
        multiOptionGroupEnabled = enable;
        comboBox.setEnabled(enable);

        if(enable)
        {
            optionSelected(parentBox, fieldConfigManager, fieldPanel, this);
        }
        else
        {
            removeOptionFields(parentBox, fieldConfigManager);
        }

        for(FieldConfigBase fieldConfig : optionFieldList)
        {
            fieldConfig.setEnabled(enable);
        }
    }

    /**
     * Sets the option.
     *
     * @param option the new option
     */
    public void setOption(GroupIdEnum option) {
        comboBox.setSelectValueKey(option.toString());
        oldValueObj = option.toString();
    }

    /**
     * Checks if is panel enabled.
     *
     * @return true, if is panel enabled
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.GroupConfigInterface#isPanelEnabled()
     */
    @Override
    public boolean isPanelEnabled() {
        return multiOptionGroupEnabled;
    }

    /**
     * Gets the selected option group.
     *
     * @return the selected option group
     */
    public OptionGroup getSelectedOptionGroup() {
        ValueComboBoxData selectedItem = comboBox.getSelectedValue();
        String key = selectedItem.getKey();

        return optionMap.get(key);
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
        if(comboBox != null)
        {
            String oldValue = (String)undoRedoObject.getOldValue();

            comboBox.setSelectValueKey(oldValue);
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
        if(comboBox != null)
        {
            String newValue = (String)undoRedoObject.getNewValue();

            comboBox.setSelectValueKey(newValue);
        }
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.base.GroupConfigInterface#getId()
     */
    @Override
    public GroupIdEnum getId() {
        return id;
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.base.GroupConfigInterface#getLabel()
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * Checks if is show label.
     *
     * @return true, if is show label
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.base.GroupConfigInterface#isShowLabel()
     */
    @Override
    public boolean isShowLabel() {
        return showLabel;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(GroupIdEnum id) {
        this.id = id;
    }

    /**
     * Removes the from ui.
     */
    @Override
    public void removeFromUI() {
        // Does nothing
    }
}
