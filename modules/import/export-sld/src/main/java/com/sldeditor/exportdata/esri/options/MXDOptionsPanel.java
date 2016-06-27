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
package com.sldeditor.exportdata.esri.options;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import com.sldeditor.common.console.ConsoleManager;

/**
 * Dialog that allows the user to configure MXD conversion options. 
 * 
 * @author Robert Ward (SCISYS)
 */
public class MXDOptionsPanel extends JDialog
{
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The ok. */
    private boolean ok = false;

    /** The font size factor spinner. */
    private JSpinner fontSizeFactorSpinner = null;
    
    /**
     * Instantiates a new MXD options panel.
     */
    public MXDOptionsPanel() {
        setResizable(false);
        setModal(true);
        setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle("MXD Import Options");
        
        JPanel buttonPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        JButton btnOk = new JButton("Ok");
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                Object obj = fontSizeFactorSpinner.getValue();
                
                double value = 0.0;
                
                if(obj instanceof Integer)
                {
                    Integer tmpInt = (Integer)fontSizeFactorSpinner.getValue();
                    
                    value = tmpInt.doubleValue();
                }
                else if(obj instanceof Double)
                {
                    Double tmpDouble = (Double)fontSizeFactorSpinner.getValue();
                    
                    value = tmpDouble.doubleValue();
                }
                else
                {
                    ConsoleManager.getInstance().error(this, "Unexpected class : " + obj.getClass().getName());
                }
                
                MXDOptions.getInstance().setFontSizeFactor(value);

                ok = true;
                setVisible(false);
            }
        });
        buttonPanel.add(btnOk);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ok = false;
                setVisible(false);
            }
        });
        buttonPanel.add(btnCancel);
        
        JPanel mainPanel = new JPanel();
        getContentPane().add(mainPanel, BorderLayout.NORTH);
        mainPanel.setLayout(new BorderLayout(0, 0));
        
        JPanel panel = new JPanel();
        mainPanel.add(panel, BorderLayout.NORTH);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        
        JPanel fieldPanel = new JPanel();
        panel.add(fieldPanel);
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        
        JPanel fontSizePanel = new JPanel();
        FlowLayout flowLayout_1 = (FlowLayout) fontSizePanel.getLayout();
        flowLayout_1.setAlignment(FlowLayout.LEFT);
        fieldPanel.add(fontSizePanel);
        
        JLabel labelFontSizeOffsetLabel = new JLabel("Font Size Offset :");
        fontSizePanel.add(labelFontSizeOffsetLabel);
        
        JPanel valuePanel = new JPanel();
        panel.add(valuePanel);
        valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.Y_AXIS));
        
        JPanel panel_1 = new JPanel();
        valuePanel.add(panel_1);
        panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
        
        fontSizeFactorSpinner = new JSpinner();
        fontSizeFactorSpinner.setValue(Double.valueOf(MXDOptions.getInstance().getFontSizeFactor()));
        
        panel_1.add(fontSizeFactorSpinner);
        
        setSize(300, 100);
    }

    /**
     * Show dialog.
     *
     * @return true, if successful
     */
    public static boolean showDialog()
    {
        MXDOptionsPanel panel = new MXDOptionsPanel();
        
        panel.setVisible(true);
        
        return panel.ok;
    }
}
