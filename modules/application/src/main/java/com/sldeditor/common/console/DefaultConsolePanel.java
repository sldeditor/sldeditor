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
package com.sldeditor.common.console;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Default implementation of the console panel.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DefaultConsolePanel extends JPanel implements ConsolePanelInterface
{
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The text pane. */
    private JTextArea textPane;

    /**
     * Instantiates a new console panel.
     */
    public DefaultConsolePanel()
    {
        setLayout(new BorderLayout(0, 0));

        textPane = new JTextArea();
        textPane.setRows(5);
        textPane.setEditable(false);

        JScrollPane jp = new JScrollPane(textPane);
        add(jp, BorderLayout.CENTER);
    }

    /**
     * Adds the message.
     *
     * @param message the message
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.console.ConsolePanelInterface#addMessage(java.lang.String)
     */
    @Override
    public void addMessage(String message)
    {
        textPane.setForeground(Color.BLACK);
        textPane.append(message);
        textPane.append("\n");
    }

    /**
     * Adds the error message.
     *
     * @param errorMessage the error message
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.console.ConsolePanelInterface#addErrorMessage(java.lang.String)
     */
    @Override
    public void addErrorMessage(String errorMessage)
    {
        textPane.setForeground(Color.RED);
        textPane.append(errorMessage);
        textPane.append("\n");
    }

    @Override
    public void clear() {
        textPane.setText("");
    }
}
