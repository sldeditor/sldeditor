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
package com.sldeditor.filter.v2.function;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.geotools.filter.FunctionExpression;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.filter.v2.expression.ExpressionPanelv2;
import com.sldeditor.ui.detail.FunctionDetails;

/**
 * The Class FunctionPanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class FunctionPanel extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The ok button pressed flag. */
    private boolean okButtonPressed = false;

    /** The field panel. */
    private FunctionDetails fieldPanel = null;

    /**
     * Instantiates a new function panel.
     *
     * @param parent the parent
     */
    public FunctionPanel(JFrame parent)
    {
        super(parent, "Function Panel", true);

        setPreferredSize(new Dimension(600, 415));

        createUI();
        
        this.pack();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        JPanel panel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setAlignment(FlowLayout.TRAILING);
        getContentPane().add(panel, BorderLayout.SOUTH);

        JButton btnOk = new JButton(Localisation.getString(ExpressionPanelv2.class, "common.ok"));
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButtonPressed = true;

                setVisible(false);
            }
        });
        panel.add(btnOk);

        JButton btnCancel = new JButton(Localisation.getString(ExpressionPanelv2.class, "common.cancel"));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButtonPressed = false;

                setVisible(false);
            }
        });
        panel.add(btnCancel);
    }

    /**
     * Show dialog.
     *
     * @param type the type
     * @param functionExpression the function expression
     * @return true, if Ok button pressed
     */
    public boolean showDialog(Class<?> type, FunctionExpression functionExpression) {

        FunctionNameInterface functionManager = FunctionManager.getInstance();

        fieldPanel = new FunctionDetails(functionManager, type);
        getContentPane().add(fieldPanel, BorderLayout.CENTER);

        setVisible(true);

        return okButtonPressed;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        FunctionPanel functionPanel = new FunctionPanel(null);

        FunctionExpression functionExpression = null;
        Class<?> type = String.class;
        functionPanel.showDialog(type, functionExpression);
    }

}
