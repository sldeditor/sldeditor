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
package com.sldeditor.ui.widgets;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.xml.ParseXML;
import com.sldeditor.ui.attribute.AttributeSelection;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.MultipleFieldInterface;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigFunction;

/**
 * Panel that contains a field.
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The add field button. */
    private JButton addFieldButton;

    /** The remove field button. */
    private JButton removeFieldButton;

    /** The x pos. */
    private int xPos = 0;

    /**
     * Instantiates a new field panel with only one row.
     *
     * @param xPos the x pos
     * @param labelString the label string
     * @param multipleValues the multiple values
     * @param parentPanel the parent panel
     * @param parentBox the parent box
     * @param field the field
     */
    public FieldPanel(int xPos, String labelString, boolean multipleValues, MultipleFieldInterface parentPanel, Box parentBox, FieldConfigBase field) {
        this(xPos, labelString, BasePanel.WIDGET_HEIGHT, multipleValues, parentPanel, parentBox, field);
    }

    /**
     * Instantiates a new field panel, allows multiple rows.
     *
     * @param xPos the x pos
     * @param labelString the label string
     * @param height the height
     * @param multipleValues the multiple values
     * @param parentPanel the parent panel
     * @param parentBox the parent box
     * @param field the field
     */
    public FieldPanel(int xPos, String labelString, int height, boolean multipleValues, MultipleFieldInterface parentPanel, Box parentBox, FieldConfigBase field) {
        this.xPos = xPos;
        setLayout(null);
        internalCreateLabel(xPos, labelString);

        createMultipleButtons(multipleValues, parentPanel, parentBox, field);

        Dimension size = new Dimension(BasePanel.FIELD_PANEL_WIDTH, height);

        this.setPreferredSize(size);
    }

    /**
     * Creates the multiple buttons.
     *
     * @param multipleValues the multiple values
     * @param parentPanel the parent panel
     * @param parentBox the parent box
     * @param field the field
     */
    private void createMultipleButtons(boolean multipleValues, MultipleFieldInterface parentPanel, Box parentBox, FieldConfigBase field) {
        if(multipleValues)
        {
            addFieldButton = new JButton("");
            addFieldButton.setIcon(getResourceIcon("button/addfield.png"));
            addFieldButton.setBorder(null);
            addFieldButton.setBounds(5, 0, 16, 16);
            addFieldButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(parentPanel != null)
                    {
                        int index = getFieldIndex(parentBox, field) + 1;
                        FieldConfigBase duplicateField = field.duplicate();
                        parentPanel.addField(parentBox, index, field.getParent(), duplicateField);
                        parentPanel.refreshPanel();
                    }
                }

            });
            add(addFieldButton);
            removeFieldButton = new JButton("");
            removeFieldButton.setIcon(getResourceIcon("button/removefield.png"));
            removeFieldButton.setBorder(null);
            removeFieldButton.setBounds(22, 0, 16, 16);
            removeFieldButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(parentPanel != null)
                    {
                        parentPanel.removeField(parentBox, field);
                        parentPanel.refreshPanel();
                    }
                }

            });
            add(removeFieldButton);
        }
    }

    /**
     * Gets the index of the field within the box.
     *
     * @param parentBox the parent box
     * @param fieldConfig the field config
     * @return the field index
     */
    private int getFieldIndex(Box parentBox, FieldConfigBase fieldConfig)
    {
        for(int index = 0; index < parentBox.getComponentCount(); index ++)
        {
            Component component = parentBox.getComponent(index);

            if(component == fieldConfig.getPanel())
            {
                return index;
            }
        }

        return -1;
    }

    /**
     * Internal create attribute button.
     *
     * @param classType the class type
     * @param field the field
     * @return the attribute selection
     */
    public AttributeSelection internalCreateAttrButton(Class<?> classType, FieldConfigBase field) {
        boolean isFunction = (field instanceof FieldConfigFunction);
        FieldConfigBase parentField = field.getParent();

        if(parentField != null)
        {
            isFunction = parentField.isFunction();
            field.setFunction(isFunction);
        }

        AttributeSelection buttonAttrLabel = new AttributeSelection(classType, isFunction, field);
        buttonAttrLabel.setBounds(xPos + BasePanel.ATTRIBUTE_BTN_X, 0, AttributeSelection.getPanelWidth(), BasePanel.WIDGET_HEIGHT);
        add(buttonAttrLabel);

        return buttonAttrLabel;
    }

    /**
     * Internal create label.
     *
     * @param xPos the x pos
     * @param label the label
     */
    private void internalCreateLabel(int xPos, String label) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setBounds(xPos + 5, 0, BasePanel.LABEL_WIDTH, BasePanel.WIDGET_HEIGHT);
        lblLabel.setHorizontalAlignment(SwingConstants.TRAILING);

        add(lblLabel);
    }

    /**
     * Enable panel.
     *
     * @param enabled the enabled
     */
    public void enablePanel(boolean enabled) {
        for(int index = 0; index < this.getComponentCount(); index ++)
        {
            Component c = this.getComponent(index);

            c.setEnabled(enabled);
        }
    }

    /**
     * Sets the multiple value button state.
     *
     * @param enableAddButton the enable add button
     * @param enableRemoveButton the enable remove button
     */
    public void setMultipleValueButtonState(boolean enableAddButton, boolean enableRemoveButton) {
        addFieldButton.setEnabled(enableAddButton);
        removeFieldButton.setEnabled(enableRemoveButton);
    }

    /**
     * Gets the resource icon.
     *
     * @param resourceString the resource string
     * @return the resource icon
     */
    private static ImageIcon getResourceIcon(String resourceString)
    {
        URL url = FieldPanel.class.getClassLoader().getResource(resourceString);

        if(url == null)
        {
            ConsoleManager.getInstance().error(FieldPanel.class, Localisation.getField(ParseXML.class, "ParseXML.failedToFindResource") + resourceString);
            return null;
        }
        else
        {
            return new ImageIcon(url);
        }
    }
}
