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
import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * The Class ProgressDialog.
 *
 * @author Robert Ward (SCISYS)
 */
public class ProgressDialog extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The text area. */
    private JTextArea textArea;

    /** The progress bar. */
    private JProgressBar progressBar;

    /** The continue progress. */
    private boolean continueProgress = true;
    
    /**
     * Should continue.
     *
     * @return the continueProgress
     */
    public boolean shouldContinue() {
        return continueProgress;
    }

    /** The finished. */
    private boolean finished = false;

    /** The cancel/ok button. */
    private JButton button;
    
    /**
     * Instantiates a new progress dialog.
     */
    public ProgressDialog() {
        setTitle("Import Esri MXD File");
        setModal(true);
        setSize(600, 400);
        setLocationRelativeTo(null);
        createUI();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        JPanel progressPanel = new JPanel();
        getContentPane().add(progressPanel, BorderLayout.NORTH);
        progressPanel.setLayout(new BorderLayout(0, 0));

        progressBar = new JProgressBar();
        progressPanel.add(progressBar);

        JPanel panelTextArea = new JPanel();
        getContentPane().add(panelTextArea, BorderLayout.CENTER);
        panelTextArea.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        panelTextArea.add(scrollPane);

        textArea = new JTextArea();
        textArea.setEditable(false);
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        scrollPane.setViewportView(textArea);
        
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.SOUTH);
        
        button = new JButton("Cancel");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(hasFinished())
                {
                    setVisible(false);
                    System.exit(0);
                }
                else if(!shouldContinue())
                {
                    setVisible(false);
                    System.exit(0);
                }
                else
                {
                    addErrorMessage("User requested cancel");
                    continueProgress = false;
                    button.setText("Close");
                }
            }
        });
        panel.add(button);
    }

    /**
     * Checks for finished.
     *
     * @return true, if successful
     */
    private boolean hasFinished() {
        return finished;
    }

    /**
     * Adds the info message.
     *
     * @param infoString the info string
     */
    public void addInfoMessage(String infoString) {
        textArea.setForeground(Color.BLACK);
        textArea.append(infoString);
        textArea.append("\n");
    }

    /**
     * Adds the error message.
     *
     * @param errorMessage the error message
     */
    public void addErrorMessage(String errorMessage)
    {
        textArea.setForeground(Color.RED);
        textArea.append(errorMessage);
        textArea.append("\n");
    }

    /**
     * Adds the warn message.
     *
     * @param warnString the warn string
     */
    public void addWarnMessage(String warnString) {
        textArea.setForeground(Color.BLUE);
        textArea.append(warnString);
        textArea.append("\n");
    }

    /**
     * Sets the progress.
     *
     * @param count the new progress
     */
    public void setProgress(int count) {
        progressBar.setValue(count);
    }

    /**
     * Sets the total.
     *
     * @param total the new total
     */
    public void setTotal(int total) {
        progressBar.setMinimum(0);
        progressBar.setMaximum(total);
    }

    public void finished()
    {
        finished = true;
        button.setText("OK");
    }

}
