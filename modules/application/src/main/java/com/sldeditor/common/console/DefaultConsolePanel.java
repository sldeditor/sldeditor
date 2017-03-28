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

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * Default implementation of the console panel.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DefaultConsolePanel extends JPanel implements ConsolePanelInterface {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The text pane. */
    private JList<ConsoleData> textPane;

    /** The model. */
    private DefaultListModel<ConsoleData> model = new DefaultListModel<ConsoleData>();

    /**
     * Instantiates a new console panel.
     */
    public DefaultConsolePanel() {
        setLayout(new BorderLayout(0, 0));

        textPane = new JList<ConsoleData>();
        textPane.setCellRenderer(new ConsoleCellRenderer());
        textPane.setModel(model);

        JScrollPane jp = new JScrollPane(textPane);
        add(jp, BorderLayout.CENTER);
    }

    /**
     * Adds the message.
     *
     * @param message the message
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.console.ConsolePanelInterface#addMessage(java.lang.String)
     */
    @Override
    public void addMessage(String message) {
        model.addElement(new ConsoleData(message, ConsoleDataEnum.INFORMATION));
        showLastItem();
    }

    /**
     * Adds the error message.
     *
     * @param errorMessage the error message
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.console.ConsolePanelInterface#addErrorMessage(java.lang.String)
     */
    @Override
    public void addErrorMessage(String errorMessage) {
        model.addElement(new ConsoleData(errorMessage, ConsoleDataEnum.ERROR));
        showLastItem();
    }

    @Override
    public void clear() {
        model.clear();
    }

    /**
     * Show last item.
     */
    private void showLastItem() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                int lastIndex = model.getSize() - 1;

                if (lastIndex >= 0) {
                    textPane.ensureIndexIsVisible(lastIndex);
                }
            }
        });
    }
}
