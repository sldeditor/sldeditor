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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.sldeditor.ui.attribute.AttributeSelection;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;

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

    /**
     * Instantiates a new field panel with no row.
     */
    public FieldPanel() {
        this(0, null, 1);
    }

    /**
     * Instantiates a new field panel with only one row.
     *
     * @param xPos the x pos
     * @param labelString the label string
     */
    public FieldPanel(int xPos, String labelString) {
        this(xPos, labelString, BasePanel.WIDGET_HEIGHT);
    }

    /**
     * Instantiates a new field panel, allows multiple rows.
     *
     * @param xPos the x pos
     * @param labelString the label string
     * @param height the height
     */
    public FieldPanel(int xPos, String labelString, int height) {
        this.xPos = xPos;
        
        setLayout(null);
        
        if(labelString != null)
        {
            internalCreateLabel(xPos, labelString);
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
    public AttributeSelection internalCreateAttrButton(Class<?> classType, 
            FieldConfigBase field,
            boolean rasterSymbol) {

        AttributeSelection buttonAttrLabel = AttributeSelection.createAttributes(classType, field, rasterSymbol);
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
}
