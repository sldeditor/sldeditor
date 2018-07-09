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

import com.sldeditor.common.Controller;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.localisation.Localisation;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.geotools.styling.Font;
import org.geotools.styling.RuleImpl;
import org.geotools.styling.TextSymbolizerImpl;

/**
 * Dialog that displays one or more slds in table showing the fonts. User is able to batch update
 * the fonts.
 */
public class BatchUpdateFontPanel extends JDialog {

    /** The Constant PANEL_FULL_FONT. */
    private static final String PANEL_FULL_FONT =
            Localisation.getString(BatchUpdateFontPanel.class, "BatchUpdateFontPanel.full");

    /** The Constant PANEL_FONT_SIZE. */
    private static final String PANEL_FONT_SIZE =
            Localisation.getString(BatchUpdateFontPanel.class, "BatchUpdateFontPanel.fontSize");

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

    /** The btn revert. */
    private JButton btnRevert;

    /** The btn apply. */
    private JButton btnApply;

    /** The btn save. */
    private JButton btnSave;

    /** The edit panel. */
    private JPanel editPanel;

    /** The combo box. */
    private JComboBox<String> comboBox;

    /** The panel selector. */
    private JPanel panelSelector;

    /** The font size panel. */
    private FontSizePanel fontSizePanel;

    /**
     * Instantiates a new scale tool panel.
     *
     * @param application the application
     */
    public BatchUpdateFontPanel(SLDEditorInterface application) {

        this.application = application;

        setTitle(Localisation.getString(BatchUpdateFontPanel.class, "BatchUpdateFontPanel.title"));
        setModal(true);
        setSize(800, 550);

        createUI();

        Controller.getInstance().centreDialog(this);
    }

    /** Creates the ui. */
    private void createUI() {

        JPanel buttonPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.TRAILING);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        btnApply = new JButton(Localisation.getString(BatchUpdateFontPanel.class, "common.apply"));
        btnApply.setEnabled(false);
        btnApply.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        applyData();
                    }
                });
        buttonPanel.add(btnApply);

        btnRevert =
                new JButton(Localisation.getString(BatchUpdateFontPanel.class, "common.revert"));
        btnRevert.setEnabled(false);
        btnRevert.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dataModel.revertData();
                        fontDetails.populate(null);
                        fontSizePanel.populate(null);
                        btnSave.setEnabled(dataModel.anyChanges());
                    }
                });
        buttonPanel.add(btnRevert);

        btnSave = new JButton(Localisation.getString(BatchUpdateFontPanel.class, "common.save"));
        btnSave.setEnabled(false);
        btnSave.addActionListener(
                new ActionListener() {
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
        table.getSelectionModel()
                .addListSelectionListener(
                        new ListSelectionListener() {

                            @Override
                            public void valueChanged(ListSelectionEvent e) {
                                if (!e.getValueIsAdjusting()) {
                                    List<Font> entries =
                                            dataModel.getFontEntries(table.getSelectedRows());

                                    fontDetails.populate(entries);
                                    fontSizePanel.populate(entries);

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

        editPanel = new JPanel();
        panel.add(editPanel);

        comboBox = new JComboBox<String>();
        comboBox.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        CardLayout cl = (CardLayout) (panelSelector.getLayout());
                        cl.show(panelSelector, (String) comboBox.getSelectedItem());
                    }
                });
        comboBox.setModel(
                new DefaultComboBoxModel<String>(new String[] {PANEL_FULL_FONT, PANEL_FONT_SIZE}));
        editPanel.add(comboBox);

        panelSelector = new JPanel();
        panel.add(panelSelector);

        CardLayout cardlayout = new CardLayout();
        panelSelector.setLayout(cardlayout);

        fontDetails = new FontDetails();
        fontDetails.setPreferredSize(new Dimension(800, 500));
        panelSelector.add(PANEL_FULL_FONT, fontDetails);

        // Font size panel
        fontSizePanel = new FontSizePanel();
        panelSelector.add(PANEL_FONT_SIZE, fontSizePanel);
    }

    /** Apply data. */
    private void applyData() {
        if (comboBox.getSelectedItem().equals(PANEL_FULL_FONT)) {
            dataModel.applyData(table.getSelectedRows(), fontDetails.getFontData());
        } else if (comboBox.getSelectedItem().equals(PANEL_FONT_SIZE)) {
            dataModel.applyData(table.getSelectedRows(), fontSizePanel.getFontSize());
        }
        fontDetails.populate(null);
        fontSizePanel.populate(null);
        btnSave.setEnabled(dataModel.anyChanges());
    }

    /** Save data. */
    private void saveData() {
        if (dataModel.saveData(application)) {
            if (application != null) {
                application.refreshPanel(RuleImpl.class, TextSymbolizerImpl.class);
            }
        }
        btnSave.setEnabled(false);
    }

    /**
     * Populate the dialog.
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
