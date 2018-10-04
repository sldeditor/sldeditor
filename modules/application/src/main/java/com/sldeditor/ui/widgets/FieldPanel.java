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

import com.sldeditor.ui.attribute.AttributeSelection;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Panel that contains a field.
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The x pos. */
    private int xPos = 0;

    /** The optional checkbox. */
    private JCheckBox optionalCheckbox = null;

    /** The this obj. */
    private FieldConfigBase thisObj = null;

    /**
     * Instantiates a new field panel with no row.
     *
     * @param obj the obj
     */
    public FieldPanel(FieldConfigBase obj) {
        this(0, 0, null, 1, false, obj);
    }

    /**
     * Instantiates a new field panel with only one row.
     *
     * @param xPos the x pos
     * @param yOffset the y offset
     * @param labelString the label string
     * @param optionalField the optional field
     * @param obj the obj
     */
    public FieldPanel(
            int xPos, int yOffset, String labelString, boolean optionalField, FieldConfigBase obj) {
        this(xPos, yOffset, labelString, BasePanel.WIDGET_HEIGHT, optionalField, obj);
    }

    /**
     * Instantiates a new field panel, allows multiple rows.
     *
     * @param xPos the x pos
     * @param yOffset the y offset
     * @param labelString the label string
     * @param height the height
     * @param optionalField the optional field
     * @param obj the obj
     */
    public FieldPanel(
            int xPos,
            int yOffset,
            String labelString,
            int height,
            boolean optionalField,
            FieldConfigBase obj) {
        thisObj = obj;

        this.xPos = xPos;

        setLayout(null);

        if (labelString != null) {
            internalCreateLabel(xPos, yOffset, labelString);
        }

        if (optionalField) {
            internalCreateOptionalCheckbox(xPos);
        }
        Dimension size = new Dimension(BasePanel.FIELD_PANEL_WIDTH, height);

        this.setPreferredSize(size);
    }

    /**
     * Internal create attribute button.
     *
     * @param classType the class type
     * @param field the field
     * @param rasterSymbol the raster symbol
     * @return the attribute selection
     */
    public AttributeSelection internalCreateAttrButton(
            Class<?> classType, FieldConfigBase field, boolean rasterSymbol) {

        AttributeSelection buttonAttrLabel =
                AttributeSelection.createAttributes(classType, field, rasterSymbol);
        buttonAttrLabel.setBounds(
                xPos + BasePanel.ATTRIBUTE_BTN_X,
                0,
                AttributeSelection.getPanelWidth(),
                BasePanel.WIDGET_HEIGHT);
        add(buttonAttrLabel);

        return buttonAttrLabel;
    }

    /**
     * Internal create label.
     *
     * @param xPos the x pos
     * @param yOffset the y offset
     * @param label the label
     */
    private void internalCreateLabel(int xPos, int yOffset, String label) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setBounds(xPos + 5, yOffset, BasePanel.LABEL_WIDTH, BasePanel.WIDGET_HEIGHT);
        lblLabel.setHorizontalAlignment(SwingConstants.TRAILING);

        add(lblLabel);
    }

    /**
     * Internal create optional checkbox.
     *
     * @param xPos the x pos
     */
    private void internalCreateOptionalCheckbox(int xPos) {
        optionalCheckbox = new JCheckBox();
        optionalCheckbox.setBounds(
                xPos + 5 + BasePanel.LABEL_WIDTH,
                0,
                BasePanel.CHECKBOX_WIDTH,
                BasePanel.WIDGET_HEIGHT);
        optionalCheckbox.setVisible(false);
        optionalCheckbox.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleOptionalValue();
                    }
                });
        add(optionalCheckbox);
    }

    /** Handle optional value. */
    private void handleOptionalValue() {
        boolean enabled = true;

        if (optionalCheckbox != null) {
            if (optionalCheckbox.isVisible()) {
                enabled = optionalCheckbox.isSelected();
            }
        }

        if (thisObj != null) {
            thisObj.setEnabled(enabled);
        }
    }

    /**
     * Show option field.
     *
     * @param displayOptionalFields the display optional fields
     */
    public void showOptionField(boolean displayOptionalFields) {
        if (optionalCheckbox != null) {
            optionalCheckbox.setVisible(displayOptionalFields);
            handleOptionalValue();
        }
    }

    /**
     * Sets the option field value.
     *
     * @param isSelected the new option field value
     */
    public void setOptionFieldValue(boolean isSelected) {
        if (optionalCheckbox != null) {
            optionalCheckbox.setSelected(isSelected);
            handleOptionalValue();
        }
    }

    /**
     * Checks if is value readable.
     *
     * @return true, if is value readable
     */
    public boolean isValueReadable() {
        if (optionalCheckbox == null) {
            return true;
        }

        if (optionalCheckbox.isVisible()) {
            return optionalCheckbox.isSelected();
        }
        return true;
    }
}
