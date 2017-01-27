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
package com.sldeditor.tool.scale;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.geotools.styling.FeatureTypeStyleImpl;
import org.geotools.styling.RuleImpl;

import com.sldeditor.common.Controller;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.localisation.Localisation;

/**
 * Dialog that displays one or more slds in table showing the scales at which rules are displayed. User is able to update and save the scales.
 */
public class ScaleToolPanel extends JDialog implements ScaleToolUpdate {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The table. */
    private JTable table;

    /** The table model. */
    private ScaleSLDModel dataModel = null;

    /** The application. */
    private SLDEditorInterface application = null;

    /** The btn save. */
    private JButton btnSave = null;

    /** The btn revert. */
    private JButton btnRevert = null;

    /**
     * Instantiates a new scale tool panel.
     *
     * @param application the application
     */
    public ScaleToolPanel(SLDEditorInterface application) {

        this.application = application;

        setTitle(Localisation.getString(ScaleToolPanel.class, "ScaleToolPanel.title"));
        setModal(true);
        setSize(800, 300);

        createUI();

        Controller.getInstance().centreDialog(this);
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        dataModel = new ScaleSLDModel(this);

        table = new JTable();
        table.setModel(dataModel);

        dataModel.setColumnRenderer(table.getColumnModel());

        JScrollPane scrollPanel = new JScrollPane();
        scrollPanel.setViewportView(table);

        getContentPane().add(scrollPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.TRAILING);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        btnRevert = new JButton(
                Localisation.getString(ScaleToolPanel.class, "common.revert"));
        btnRevert.setEnabled(false);
        btnRevert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataModel.revertData();
            }
        });

        buttonPanel.add(btnRevert);

        btnSave = new JButton(Localisation.getString(ScaleToolPanel.class, "common.save"));
        btnSave.setEnabled(false);
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });
        buttonPanel.add(btnSave);
    }

    /**
     * Save data.
     */
    private void saveData() {
        if (dataModel.applyData(application)) {
            if (application != null) {
                application.refreshPanel(FeatureTypeStyleImpl.class, RuleImpl.class);
            }
        }
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        ScaleToolPanel dlg = new ScaleToolPanel(null);

        dlg.setVisible(true);
    }

    /**
     * Populate the dialog.
     *
     * @param sldDataList the sld data list
     */
    public void populate(List<SLDDataInterface> sldDataList) {

        List<ScaleSLDData> scaleDataList = new ArrayList<ScaleSLDData>();

        for (SLDDataInterface sldData : sldDataList) {
            List<ScaleSLDData> scaleSLDDataList = ScalePanelUtils.containsScales(sldData);
            if ((scaleSLDDataList != null) && !scaleSLDDataList.isEmpty()) {
                scaleDataList.addAll(scaleSLDDataList);
            }
        }
        dataModel.loadData(scaleDataList);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.tool.scale.ScaleToolUpdate#dataUpdated()
     */
    @Override
    public void dataUpdated() {
        btnRevert.setEnabled(true);
        btnSave.setEnabled(true);
    }

}
