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

package com.sldeditor.ui.detail.config.inlinefeature;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.sldeditor.ui.detail.BasePanel;

/**
 * The Class InlineGMLPreviewPanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class InlineGMLPreviewPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant FONT_SIZE. */
    private static final int FONT_SIZE = 14;

    /** The text field. */
    private JTextArea textField;

    /** The parent obj. */
    @SuppressWarnings("unused")
    private InlineFeatureUpdateInterface parentObj = null;

    /**
     * Instantiates a new inline GML preview panel.
     *
     * @param parentObj the parent obj
     * @param noOfRows the no of rows
     */
    public InlineGMLPreviewPanel(InlineFeatureUpdateInterface parentObj, int noOfRows) {
        this.parentObj = parentObj;
        createUI(noOfRows);
    }

    /**
     * Creates the UI.
     *
     * @param noOfRows the no of rows
     */
    private void createUI(int noOfRows) {
        setLayout(new BorderLayout());

        int xPos = 0;
        int width = BasePanel.FIELD_PANEL_WIDTH - xPos - 20;
        int height = BasePanel.WIDGET_HEIGHT * (noOfRows - 1);
        this.setBounds(0, 0, width, height);
        textField = new JTextArea();
        textField.setBounds(xPos, BasePanel.WIDGET_HEIGHT, width, height);
        Font font = textField.getFont();

        // Create a new, smaller font from the current font
        Font updatedFont = new Font(font.getFontName(), font.getStyle(), FONT_SIZE);

        // Set the new font in the editing area
        textField.setFont(updatedFont);
        textField.setEditable(true);

        // Wrap the text field with a scroll pane
        JScrollPane scroll = new JScrollPane(textField);
        scroll.setAutoscrolls(true);

        add(scroll, BorderLayout.CENTER);
    }

    /**
     * Gets the inl ine features.
     *
     * @return the inl ine features
     */
    public String getInlineFeatures() {
        return textField.getText();
    }

    /**
     * Sets the inline features.
     *
     * @param value the new inline features
     */
    public void setInlineFeatures(String value) {
        textField.setText(value);
    }
}
