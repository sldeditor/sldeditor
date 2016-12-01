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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * The Class VendorOptionPanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The vendor option table. */
    private JTable vendorOptionTable;

    /** The model. */
    private VendorOptionInfoModel model = null;

    /**
     * Instantiates a new vendor option panel.
     *
     * @param tableModel the table model
     */
    public VendorOptionPanel(VendorOptionInfoModel tableModel) {
        this.model = tableModel;

        createUI();
    }

    /**
     * Creates the UI.
     */
    private void createUI() {
        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane);

        vendorOptionTable = new JTable();
        scrollPane.setViewportView(vendorOptionTable);
        vendorOptionTable.setModel(model);
    }
}
