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

package com.sldeditor.create;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.geotools.styling.StyledLayerDescriptor;

import com.sldeditor.common.Controller;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.create.sld.NewSLDInterface;

/**
 * Dialog to allow the user to create a new SLD symbol.
 * 
 * @author Robert Ward (SCISYS)
 */
public class NewSLDPanel extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The class to create a new SLD symbol. */
    private NewSLDInterface selected = null;

    /** The new sld object map. */
    private Map<String, NewSLDInterface> newSLDObjectMap = null;

    /** The combo box new sld. */
    protected JComboBox<String> comboBoxNewSLD;

    /** The sld writer. */
    private SLDWriterInterface sldWriter = null;

    /**
     * Default constructor.
     */
    public NewSLDPanel() {
        setModal(true);
        setResizable(false);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setSize(300, 110);
        createUI();
        Controller.getInstance().centreDialog(this);
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        setTitle(Localisation.getString(NewSLDPanel.class, "NewSLDPanel.title"));

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);

        JLabel lblSldType = new JLabel(
                Localisation.getField(NewSLDPanel.class, "NewSLDPanel.symbolField"));
        panel.add(lblSldType);

        comboBoxNewSLD = new JComboBox<String>();
        newSLDObjectMap = NewSLDFactory.getAvailable();
        String[] itemArray = new String[newSLDObjectMap.size()];
        int index = 0;
        for (Object obj : newSLDObjectMap.keySet()) {
            itemArray[index] = (String) obj;

            index++;
        }
        comboBoxNewSLD.setModel(new DefaultComboBoxModel<String>(itemArray));

        panel.add(comboBoxNewSLD);

        JPanel buttonPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.TRAILING);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton btnOk = new JButton(Localisation.getString(NewSLDPanel.class, "common.ok"));
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButtonPressed();
            }
        });
        buttonPanel.add(btnOk);

        JButton btnCancel = new JButton(Localisation.getString(NewSLDPanel.class, "common.cancel"));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelButtonPressed();
            }
        });
        buttonPanel.add(btnCancel);
    }

    /**
     * Show dialog.
     *
     * @param parent the parent
     * @return the created SLD if selected
     */
    public List<SLDDataInterface> showDialog(JFrame parent) {

        List<SLDDataInterface> newSLDList = null;
        selected = null;

        if (parent != null) {
            this.setLocationRelativeTo(parent);
            int x = ((parent.getWidth() - getWidth()) / 2);
            int y = ((parent.getHeight() - getHeight()) / 2);
            this.setLocation(x, y);
        }

        setVisible(true);

        if (selected != null) {
            newSLDList = new ArrayList<SLDDataInterface>();

            StyledLayerDescriptor sld = selected.create();

            if (sldWriter == null) {
                sldWriter = SLDWriterFactory.createWriter(null);
            }

            newSLDList.add(new SLDData(new StyleWrapper(selected.getName()),
                    sldWriter.encodeSLD(null, sld)));
            return newSLDList;
        }

        return null;
    }

    /**
     * Ok button pressed.
     */
    protected void okButtonPressed() {
        String selectedItem = (String) comboBoxNewSLD.getSelectedItem();

        if (selectedItem != null) {
            if (newSLDObjectMap != null) {
                selected = newSLDObjectMap.get(selectedItem);
            }
        }
        setVisible(false);
    }

    /**
     * Cancel button pressed.
     */
    protected void cancelButtonPressed() {
        setVisible(false);
    }
}
