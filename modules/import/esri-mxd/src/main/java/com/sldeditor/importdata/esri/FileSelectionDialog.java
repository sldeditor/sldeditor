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
package com.sldeditor.importdata.esri;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * The Class FileSelectionDialog.
 *
 * @author Robert Ward (SCISYS)
 */
public class FileSelectionDialog extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The input text field. */
    private JTextField inputTextField;

    /** The output text field. */
    private JTextField outputTextField;

    /** The ok button pressed. */
    private boolean okButtonPressed = false;

    /** The Constant APP_NAME. */
    private static final String APP_NAME = "SLD Editor";

    /** The Constant APP_COMPANY. */
    private static final String APP_COMPANY = "SCISYS";

    /** The Constant APP_COPYRIGHT_YEAR. */
    private static final String APP_COPYRIGHT_YEAR = "2015-2016";

    /**
     * Instantiates a new file selection dialog.
     */
    public FileSelectionDialog() {
        setModal(true);
        setResizable(false);
        setSize(700, 200);
        setLocationRelativeTo(null);
        createUI();
        setTitle(String.format("%s - Import Esri MXD", generateApplicationTitleString()));
    }

    /**
     * Generate application title string.
     *
     * @return the string
     */
    private static String generateApplicationTitleString() {
        return String.format("%s %1.1f \251%s %s", APP_NAME, 
                1.1, 
                APP_COPYRIGHT_YEAR, APP_COMPANY);
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //
        // Input panel
        //
        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(new TitledBorder(null, "Input MXD File", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.add(inputPanel);

        final JButton btnInput = new JButton("Input");
        btnInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();

                fc.addChoosableFileFilter(new InputFileFilter());

                int returnVal = fc.showOpenDialog(btnInput);

                if(returnVal == JFileChooser.APPROVE_OPTION)
                {
                    inputTextField.setText(fc.getSelectedFile().getAbsolutePath());
                }
            }
        });
        inputPanel.add(btnInput);

        inputTextField = new JTextField();
        inputPanel.add(inputTextField);
        inputTextField.setColumns(50);

        //
        // Output panel
        //
        JPanel outputPanel = new JPanel();
        outputPanel.setBorder(new TitledBorder(null, "Output JSON File", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.add(outputPanel);

        final JButton btnOutput = new JButton("Output");
        btnOutput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();

                fc.addChoosableFileFilter(new OutputFileFilter());

                int returnVal = fc.showSaveDialog(btnOutput);

                if(returnVal == JFileChooser.APPROVE_OPTION)
                {
                    outputTextField.setText(fc.getSelectedFile().getAbsolutePath());
                }
            }
        });
        outputPanel.add(btnOutput);

        outputTextField = new JTextField();
        outputTextField.setColumns(50);
        outputPanel.add(outputTextField);

        // Button panel
        JPanel buttonPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.TRAILING);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton btnOk = new JButton("Ok");
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                okButtonPressed = true;
                setVisible(false);
            }
        });
        buttonPanel.add(btnOk);

        // Cancel button
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButtonPressed = false;
                setVisible(false);
            }
        });
        buttonPanel.add(btnCancel);
    }

    /**
     * Show dialog.
     *
     * @param dataToPopulate the data to populate
     * @return the file data
     */
    public FileData showDialog(FileData dataToPopulate)
    {
        String inputString = "";
        String outputString = "";

        if(dataToPopulate != null)
        {
            if(dataToPopulate.getInputFile() != null)
            {
                inputString = dataToPopulate.getInputFile().getAbsolutePath();
            }

            if(dataToPopulate.getOutputFile() != null)
            {
                outputString = dataToPopulate.getOutputFile().getAbsolutePath();
            }
        }

        inputTextField.setText(inputString);
        outputTextField.setText(outputString);

        setVisible(true);

        if(okButtonPressed)
        {
            return getFileData();
        }
        return null;
    }

    /**
     * Gets the file data.
     *
     * @return the file data
     */
    private FileData getFileData() {
        FileData fileData = new FileData();
        String inputString = inputTextField.getText().trim();
        String outputString = outputTextField.getText().trim();
        
        fileData.setInputFile(inputString.isEmpty() ? null : inputString);
        fileData.setOutputFile(outputString.isEmpty() ? null : outputString);

        return fileData;
    }

}
