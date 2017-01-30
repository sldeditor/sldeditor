/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

package com.sldeditor.ui.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 * The Class CheckBoxPanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class CheckBoxPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The label. */
    private TreeLabel label;

    /** The check box. */
    private JCheckBox checkBox;

    /** The selection foreground. */
    private Color selectionForeground;

    /** The text foreground. */
    private Color textForeground;

    /**
     * Instantiates a new check box panel.
     */
    public CheckBoxPanel() {
        setOpaque(false);
        checkBox = new JCheckBox();
        checkBox.setMargin(new Insets(0, 0, 0, 0));
        label = new TreeLabel();
        setLayout(new BorderLayout());
        add(checkBox, BorderLayout.WEST);

        Border border = label.getBorder();
        Border margin = new EmptyBorder(0, 3, 0, 0);
        label.setBorder(new CompoundBorder(border, margin));

        add(label, BorderLayout.CENTER);

        Boolean booleanValue = (Boolean) UIManager.get("Tree.drawsFocusBorderAroundIcon");
        checkBox.setFocusPainted((booleanValue != null) && (booleanValue.booleanValue()));

        selectionForeground = UIManager.getColor("Tree.selectionForeground");
        textForeground = UIManager.getColor("Tree.textForeground");
    }

    /**
     * Sets the selected.
     *
     * @param selected the selected
     * @param hasFocus the has focus
     */
    public void setSelected(boolean selected, boolean hasFocus) {
        label.setSelected(selected);
        label.setFocus(hasFocus);
        if (selected) {
            checkBox.setForeground(selectionForeground);
            label.setForeground(selectionForeground);
        } else {
            checkBox.setForeground(textForeground);
            label.setForeground(textForeground);
        }
    }

    /**
     * Sets the checkbox visible.
     *
     * @param showCheckbox the new checkbox visible
     */
    public void setCheckboxVisible(boolean showCheckbox) {
        checkBox.setVisible(showCheckbox);
    }

    /**
     * @param name
     */
    public void setLabelText(String text) {
        label.setText(text);
    }

    /**
     * Sets the checkbox selected.
     *
     * @param selected the new checkbox selected
     */
    public void setCheckboxSelected(boolean selected) {
        checkBox.setSelected(selected);
    }

    /**
     * Sets the checkbox action listener.
     *
     * @param listener the new checkbox action listener
     */
    public void setCheckboxActionListener(ActionListener listener) {
        checkBox.addActionListener(listener);
    }

    /**
     * Sets the checkbox mouse listener.
     *
     * @param mouseAdapter the new checkbox mouse listener
     */
    public void setCheckboxMouseListener(MouseAdapter mouseAdapter) {
        checkBox.addMouseListener(mouseAdapter);
    }

    /**
     * Checks if is check box selected.
     *
     * @return true, if is check box selected
     */
    public boolean isCheckBoxSelected() {
        return checkBox.isSelected();
    }
}
