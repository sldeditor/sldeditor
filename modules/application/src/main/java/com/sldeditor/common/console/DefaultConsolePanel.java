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

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.preferences.PrefData;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.preferences.iface.PrefUpdateInterface;
import com.sldeditor.ui.preferences.FileEncodingComboBox;
import com.sldeditor.ui.reportissue.ReportIssue;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Default implementation of the console panel.
 *
 * @author Robert Ward (SCISYS)
 */
public class DefaultConsolePanel extends JPanel
        implements ConsolePanelInterface, PrefUpdateInterface {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The text pane. */
    private JList<ConsoleData> textPane;

    /** The model. */
    private DefaultListModel<ConsoleData> model = new DefaultListModel<>();

    /** The file encoding label. */
    private String fileEncodingLabelString = Localisation.getString(DefaultConsolePanel.class,
            "DefaultConsolePanel.fileEncoding");

    /** The file encoding label. */
    private JLabel fileEncodingLabel;

    private JComboBox<String> fileEncodingComboBox;

    /** Instantiates a new console panel. */
    public DefaultConsolePanel() {
        setLayout(new BorderLayout(0, 0));

        textPane = new JList<>();
        textPane.setCellRenderer(new ConsoleCellRenderer());
        textPane.setModel(model);

        JScrollPane jp = new JScrollPane(textPane);
        add(jp, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        add(panel, BorderLayout.SOUTH);
        panel.setLayout(new BorderLayout(0, 0));

        // Feedback button
        JPanel feedbackPanel = new JPanel();
        panel.add(feedbackPanel, BorderLayout.EAST);

        JButton btnFeedback = new JButton(
                Localisation.getString(DefaultConsolePanel.class, "DefaultConsolePanel.feedback"));
        feedbackPanel.add(btnFeedback);
        btnFeedback.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ReportIssue.getInstance().display();
            }
        });

        // File encoding
        JPanel fileEncodingPanel = new JPanel();
        panel.add(fileEncodingPanel, BorderLayout.WEST);

        fileEncodingLabel = new JLabel(fileEncodingLabelString);
        fileEncodingLabel.setHorizontalAlignment(SwingConstants.LEFT);
        fileEncodingPanel.add(fileEncodingLabel);
        fileEncodingComboBox = FileEncodingComboBox.create();
        fileEncodingComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) fileEncodingComboBox.getSelectedItem();
                Charset fileEncoding = Charset.forName(selectedItem);

                PrefData prefData = PrefManager.getInstance().getPrefData();
                if (!fileEncoding.equals(prefData.getFileEncoding())) {
                    prefData.setFileEncoding(fileEncoding);
                    PrefManager.getInstance().setPrefData(prefData);
                }
            }
        });
        fileEncodingPanel.add(fileEncodingComboBox);

        // Register for changes in file encoding
        PrefManager.getInstance().addListener(this);
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

    /** Show last item. */
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

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.common.preferences.iface.PrefUpdateInterface#useAntiAliasUpdated(boolean)
     */
    @Override
    public void useAntiAliasUpdated(boolean value) {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.common.preferences.iface.PrefUpdateInterface#backgroundColourUpdate(java.awt.
     * Color)
     */
    @Override
    public void backgroundColourUpdate(Color backgroundColour) {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.common.preferences.iface.PrefUpdateInterface#fileEncodingUpdate(java.nio.
     * charset.Charset)
     */
    @Override
    public void fileEncodingUpdate(Charset fileEncoding) {
        fileEncodingComboBox.setSelectedItem(fileEncoding.name());

        String displayString = String.format("%s : %s", fileEncodingLabelString,
                fileEncoding.name());
        ConsoleManager.getInstance().information(this, displayString);
    }
}
