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
package com.sldeditor.tool.batchupdatefont;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.geotools.styling.Font;

import com.sldeditor.common.Controller;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.localisation.Localisation;

/**
 * Dialog that displays one or more slds in table showing the fonts User is able to batch update the fonts.
 */
public class BatchUpdateFontPanel extends JDialog {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The table. */
    private JTable table;

    /** The table model. */
    private BatchUpdateFontModel dataModel = new BatchUpdateFontModel();

    /** The application. */
    private SLDEditorInterface application = null;

    /** The font details. */
    private FontDetails fontDetails = null;

    private JButton btnRevert;

    private JButton btnApply;

    private JButton btnSave;

    /**
     * Instantiates a new scale tool panel.
     *
     * @param application the application
     */
    public BatchUpdateFontPanel(SLDEditorInterface application) {

        this.application = application;

        setTitle(Localisation.getString(BatchUpdateFontPanel.class, "BatchUpdateFontPanel.title"));
        setModal(true);
        setSize(800, 500);

        createUI();

        Controller.getInstance().centreDialog(this);
    }

    /**
     * Creates the ui.
     */
    private void createUI() {

        JPanel buttonPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.TRAILING);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        btnApply = new JButton(Localisation.getString(BatchUpdateFontPanel.class, "common.apply"));
        btnApply.setEnabled(false);
        btnApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyData();
            }
        });
        buttonPanel.add(btnApply);

        btnRevert = new JButton(
                Localisation.getString(BatchUpdateFontPanel.class, "common.revert"));
        btnRevert.setEnabled(false);
        btnRevert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataModel.revertData();
                fontDetails.populate(null);
                btnSave.setEnabled(dataModel.anyChanges());
            }
        });
        buttonPanel.add(btnRevert);

        btnSave = new JButton(
                Localisation.getString(BatchUpdateFontPanel.class, "common.save"));
        btnSave.setEnabled(false);
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });
        buttonPanel.add(btnSave);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        table = new JTable();
        table.setModel(dataModel);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    List<Font> entries = dataModel.getFontEntries(table.getSelectedRows());
                    fontDetails.populate(entries);

                    btnRevert.setEnabled(true);
                    btnApply.setEnabled(true);
                }
            }
        });
        dataModel.setColumnRenderer(table.getColumnModel());

        JScrollPane scrollPanel = new JScrollPane();
        panel.add(scrollPanel);
        scrollPanel.setViewportView(table);
        table.setPreferredSize(new Dimension(800, 300));
        fontDetails = new FontDetails();
        fontDetails.setPreferredSize(new Dimension(800, 500));
        panel.add(fontDetails);
    }

    /**
     * Apply data.
     */
    private void applyData() {
        dataModel.applyData(table.getSelectedRows(), fontDetails.getFontData());
        fontDetails.populate(null);

        btnSave.setEnabled(dataModel.anyChanges());
    }

    /**
     * Save data.
     */
    private void saveData() {
        dataModel.saveData(application);
    }

    /**
     * Populate the dialog
     *
     * @param sldDataList the sld data list
     */
    public void populate(List<SLDDataInterface> sldDataList) {

        List<BatchUpdateFontData> fontDataList = new ArrayList<BatchUpdateFontData>();

        for (SLDDataInterface sldData : sldDataList) {
            List<BatchUpdateFontData> fontSLDDataList = BatchUpdateFontUtils.containsFonts(sldData);
            if ((fontSLDDataList != null) && !fontSLDDataList.isEmpty()) {
                fontDataList.addAll(fontSLDDataList);
            }
        }
        dataModel.loadData(fontDataList);
    }

}
