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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * The Class GroupConfig represents the configuration for a group of fields.
 * 
 * <p>
 * An optional check box can be configured to enable/disable all the fields in the group.
 * 
 * @author Robert Ward (SCISYS)
 */
public class GroupConfig
        implements GroupConfigInterface, UndoActionInterface {

    /** The Constant FULL_WIDTH. */
    public static final int FULL_WIDTH = BasePanel.FIELD_PANEL_WIDTH;

    /** The id. */
    private GroupIdEnum id;

    /** The label. */
    private String label;

    /** The show title. */
    private boolean showLabel = true;

    /** The field list. */
    private List<FieldConfigBase> fieldList = new ArrayList<FieldConfigBase>();

    /** The is optional flag. */
    private boolean isOptional = false;

    /** The group check box. */
    private JCheckBox groupCheckbox;

    /** The sub group list. */
    private List<GroupConfigInterface> subGroupList = new ArrayList<GroupConfigInterface>();

    /** The function component list. */
    private List<Component> componentList = new ArrayList<Component>();

    /** The box containing the headings/fields. */
    private Box parentBox = null;

    /** The group enabled flag. */
    private boolean groupEnabled = true;

    /** The group title. */
    private JLabel groupTitle = null;

    /**
     * Instantiates a new group config.
     */
    public GroupConfig()
    {
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.base.GroupConfigInterface#getId()
     */
    @Override
    public GroupIdEnum getId() {
        return id;
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
     * Gets the label.
     *
     * @return the label
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.GroupConfigInterface#getLabel()
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setLabel(String name) {
        this.label = name;
    }

    /**
     * Checks if is show label.
     *
     * @return true, if is show label
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.GroupConfigInterface#isShowLabel()
     */
    @Override
    public boolean isShowLabel() {
        return showLabel;
    }

    /**
     * Sets the show label.
     *
     * @param showLabel the show label flag
     */
    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }

    /**
     * Gets the field config list.
     *
     * @return the field config list
     */
    public List<FieldConfigBase> getFieldConfigList() {
        return fieldList;
    }

    /**
     * Adds the field to the group.
     *
     * @param fieldConfig the field configuration
     */
    public void addField(FieldConfigBase fieldConfig) {
        this.fieldList.add(fieldConfig);
    }

    /**
     * Checks if is optional.
     *
     * @return true, if is optional
     */
    public boolean isOptional() {
        return isOptional;
    }

    /**
     * Sets the optional flag.
     *
     * @param isOptional the new optional flag
     */
    public void setOptional(boolean isOptional) {
        this.isOptional = isOptional;
    }

    /**
     * Creates the title components.
     *
     * @param box the box to add the components to
     * @param parent the parent object to be called when fields are changed
     */
    @Override
    public void createTitle(Box box, UpdateSymbolInterface parent) {

        this.parentBox = box;

        if (isShowLabel()) {
            Component separator = createSeparator();
            componentList.add(createSeparator());
            box.add(separator);

            Component component;

            if (isOptional()) {
                final UndoActionInterface parentObj = this;

                groupCheckbox = new JCheckBox(getLabel());
                component = groupCheckbox;
                groupCheckbox.setBounds(0, 0, FULL_WIDTH, BasePanel.WIDGET_HEIGHT);

                groupCheckbox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        enableSubGroups(groupCheckbox.isSelected());

                        boolean isSelected = groupCheckbox.isSelected();
                        Boolean oldValueObj = Boolean.valueOf(!isSelected);
                        Boolean newValueObj = Boolean.valueOf(isSelected);

                        UndoManager.getInstance().addUndoEvent(new UndoEvent(parentObj,
                                "Group : " + getId(), oldValueObj, newValueObj));

                        if (parent != null) {
                            parent.dataChanged(FieldIdEnum.UNKNOWN);
                        }
                    }
                });
            } else {
                groupTitle = new JLabel(getLabel());
                groupTitle.setBounds(0, 0, FULL_WIDTH, BasePanel.WIDGET_HEIGHT);
                groupTitle.setOpaque(true);
                component = groupTitle;
            }

            // Make sure label/check box appears left aligned within the group
            JPanel panel = new JPanel();
            panel.setLayout(null);

            Dimension size = new Dimension(FULL_WIDTH, BasePanel.WIDGET_HEIGHT);
            panel.setPreferredSize(size);
            panel.setMaximumSize(size);
            panel.setSize(size);
            panel.add(component);

            box.add(panel);
            componentList.add(panel);
        }
    }

    /**
     * Creates a horizontal separator.
     *
     * @return the j separator
     */
    public static Component createSeparator() {
        JPanel p = new JPanel();

        p.setLayout(new BorderLayout());

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);

        p.add(separator, BorderLayout.CENTER);
        Dimension size = new Dimension(FULL_WIDTH, 5);

        p.setPreferredSize(size);
        return p;
    }

    /**
     * Enable.
     *
     * @param enable the enable
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.GroupConfigInterface#enable(boolean)
     */
    @Override
    public void enable(boolean enable) {

        if (groupCheckbox != null) {
            groupCheckbox.setSelected(enable);
        }
        enableSubGroups(enable);

        setValueGroupState();
    }

    /**
     * Enable sub groups.
     *
     * @param enabled the enabled
     */
    private void enableSubGroups(boolean enabled) {
        for (FieldConfigBase field : getFieldConfigList()) {
            CurrentFieldState fieldState = field.getFieldState();
            fieldState.setGroupSelected(enabled);
            field.setFieldState(fieldState);
        }

        for (GroupConfigInterface subGroup : subGroupList) {
            subGroup.enable(enabled);
        }
    }

    /**
     * Checks if is panel enabled.
     *
     * @return true, if is panel enabled
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.GroupConfigInterface#isPanelEnabled()
     */
    @Override
    public boolean isPanelEnabled() {
        boolean checkBox = true;
        if (groupCheckbox != null) {
            checkBox = groupCheckbox.isSelected();
        }

        return checkBox && groupEnabled;
    }

    /**
     * Adds the sub group.
     *
     * @param subGroup the sub group
     */
    public void addGroup(GroupConfigInterface subGroup) {
        subGroupList.add(subGroup);
    }

    /**
     * Gets the sub group list.
     *
     * @return the sub group list
     */
    public List<GroupConfigInterface> getSubGroupList() {
        return subGroupList;
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        if (groupCheckbox != null) {
            Boolean oldValue = (Boolean) undoRedoObject.getOldValue();

            groupCheckbox.setSelected(oldValue.booleanValue());
            enableSubGroups(oldValue.booleanValue());
        }
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        if (groupCheckbox != null) {
            Boolean newValue = (Boolean) undoRedoObject.getNewValue();

            groupCheckbox.setSelected(newValue.booleanValue());
            enableSubGroups(newValue.booleanValue());
        }
    }

    /**
     * Removes the components from ui.
     */
    public void removeFromUI() {
        for (Component component : componentList) {
            this.parentBox.remove(component);
        }

        componentList.clear();
    }

    /**
     * Gets the component list.
     *
     * @return the componentList
     */
    public List<Component> getComponentList() {
        return componentList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s : (%s) %s", getClass().getName(), getId().toString(), getLabel());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.base.GroupConfigInterface#setGroupStateOverride(boolean)
     */
    @Override
    public void setGroupStateOverride(boolean enabled) {
        groupEnabled = enabled;

        for (FieldConfigBase field : getFieldConfigList()) {
            CurrentFieldState fieldState = field.getFieldState();
            fieldState.setGroupEnabled(groupEnabled);
            fieldState.setFieldEnabled(groupEnabled);
            field.setFieldState(fieldState);
        }

        setValueGroupState();
    }

    /**
     * Sets the value group state.
     */
    private void setValueGroupState() {
        boolean isSelected = true;

        if (isShowLabel()) {
            if (isOptional()) {
                groupCheckbox.setEnabled(groupEnabled);
                isSelected = groupCheckbox.isSelected();
            } else {
                groupTitle.setEnabled(groupEnabled);
            }
        }

        for (FieldConfigBase field : getFieldConfigList()) {
            CurrentFieldState fieldState = field.getFieldState();
            fieldState.setGroupSelected(isSelected);
            field.setFieldState(fieldState);
        }
    }
}
