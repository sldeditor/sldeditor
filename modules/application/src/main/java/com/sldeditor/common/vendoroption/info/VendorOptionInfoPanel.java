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

package com.sldeditor.common.vendoroption.info;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * The Class VendorOptionPanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionInfoPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The vendor option table. */
    private JTable vendorOptionTable;

    /** The description area. */
    private JTextArea descriptionArea = null;

    /** The model. */
    private VendorOptionInfoModel model = null;

    /**
     * Instantiates a new vendor option panel.
     *
     * @param tableModel the table model
     */
    public VendorOptionInfoPanel(VendorOptionInfoModel tableModel) {
        this.model = tableModel;

        createUI();
    }

    /**
     * Creates the UI.
     */
    private void createUI() {
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        vendorOptionTable = new JTable();
        scrollPane.setViewportView(vendorOptionTable);
        vendorOptionTable.setModel(model);
        vendorOptionTable.getColumnModel().getColumn(0)
        .setCellRenderer(new VendorOptionInfoCellRenderer(model));
        vendorOptionTable.getColumnModel().getColumn(1)
        .setCellRenderer(new VendorOptionInfoCellRenderer(model));
        vendorOptionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                displayDescription(vendorOptionTable.getSelectedRow());
            }
        });

        descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setRows(5);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(vendorOptionTable.getFont());
        JScrollPane descriptionAreaScrollPane = new JScrollPane(descriptionArea);
        add(descriptionAreaScrollPane, BorderLayout.EAST);
        descriptionAreaScrollPane.setPreferredSize(new Dimension(200, 100));
    }

    /**
     * Display description.
     *
     * @param selectedRow the selected row
     */
    private void displayDescription(int selectedRow) {
        String description = model.getDescription(selectedRow);

        descriptionArea.setText(description);
    }
}
