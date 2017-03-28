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

package com.sldeditor.tool;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.xml.ParseXML;

/**
 * The Class ToggleToolButton, overloads a JToggleButton but allows the button text
 * to set as a tooltip and the resource string as the button icon.
 *
 * @author Robert Ward (SCISYS)
 */
public class ToggleToolButton extends JToggleButton {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new tool button.
     *
     * @param text the text
     */
    public ToggleToolButton(String text) {
        super(" ");

        setToolTipText(text);
        setSize(16, 16);
    }

    /**
     * Instantiates a new tool button.
     *
     * @param text the text
     */
    public ToggleToolButton(String text, String resourceString) {
        super("");

        setToolTipText(text);

        ImageIcon imageIcon = getResourceIcon(resourceString);
        this.setIcon(imageIcon);
        if (imageIcon != null) {
            this.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
        }
    }

    /**
     * Gets the resource icon.
     *
     * @param resourceString the resource string
     * @return the resource icon
     */
    private static ImageIcon getResourceIcon(String resourceString) {
        URL url = ToggleToolButton.class.getClassLoader().getResource(resourceString);

        if (url == null) {
            ConsoleManager.getInstance().error(ToggleToolButton.class,
                    Localisation.getField(ParseXML.class, "ParseXML.failedToFindResource")
                            + resourceString);
            return null;
        } else {
            return new ImageIcon(url);
        }
    }
}
