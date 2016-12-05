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

package com.sldeditor.minversion;

import java.awt.BorderLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.geotools.styling.StyledLayerDescriptor;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDOutputInterface;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.preferences.VendorOptionTableModel;
import com.sldeditor.common.preferences.VersionCellEditor;
import com.sldeditor.common.preferences.VersionCellRenderer;
import com.sldeditor.common.preferences.iface.PrefUpdateVendorOptionInterface;
import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionTypeInterface;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.vendoroption.info.VendorOptionInfoManager;
import com.sldeditor.common.vendoroption.info.VendorOptionInfoPanel;
import com.sldeditor.render.RenderPanelFactory;
import com.sldeditor.ui.panels.SLDEditorUIPanels;
import com.sldeditor.ui.preferences.PrefPanel;

/**
 * The Class VendorOptionUI.
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionUI extends JPanel implements SLDOutputInterface, PrefUpdateVendorOptionInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The vendor option table. */
    private JTable vendorOptionTable;

    /** The model. */
    private VendorOptionTableModel vendorOptionModel = null;

    /** The options. */
    private Map<VendorOptionTypeInterface, String> options = new LinkedHashMap<VendorOptionTypeInterface, String>();

    /** The table. */
    private JTable vendorOptionPresentTable = null;

    private VendorOptionPresentModel vendorOptionPresentModel = new VendorOptionPresentModel();

    // Find the minimum version supported by the SLD
    private MinimumVersion minimumVersion = null;

    /**
     * Instantiates a new vendor option UI.
     *
     * @param uiMgr the ui mgr
     */
    public VendorOptionUI(SLDEditorUIPanels uiMgr)
    {
        addVendorOption(VendorOptionManager.getInstance().getClass(GeoServerVendorOption.class));

        createUI();

        minimumVersion = new MinimumVersion(uiMgr);

        // Listen for changes in the SLD
        RenderPanelFactory.addSLDOutputListener(this);

        PrefManager.getInstance().addVendorOptionListener(this);
    }

    /**
     * Adds the vendor option.
     *
     * @param vendorOption the vendor option
     */
    private void addVendorOption(VendorOptionTypeInterface vendorOption) {
        options.put(vendorOption, vendorOption.getName());
    }

    /**
     * Creates the UI.
     */
    public void createUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // Vendor options
        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Localisation.getString(PrefPanel.class, "PrefPanel.vendorOptions"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_1.setLayout(new BorderLayout());
        add(panel_1);

        JScrollPane scrollPane = new JScrollPane();
        panel_1.add(scrollPane, BorderLayout.CENTER);

        vendorOptionTable = new JTable();
        scrollPane.setViewportView(vendorOptionTable);

        vendorOptionModel = new VendorOptionTableModel(options);

        vendorOptionTable.setModel(vendorOptionModel);
        vendorOptionTable.getColumnModel().getColumn(1).setCellRenderer(new VersionCellRenderer());
        vendorOptionTable.getColumnModel().getColumn(1).setCellEditor(new VersionCellEditor(vendorOptionModel));

        // Vendor option information
        VendorOptionInfoPanel vendorOptionInfoPanel = VendorOptionInfoManager.getInstance().getPanel();
        add(vendorOptionInfoPanel);

        // VendorOption present table
        JScrollPane scrollPaneTable = new JScrollPane();
        add(scrollPaneTable);

        vendorOptionPresentTable = new JTable();
        vendorOptionPresentTable.setModel(vendorOptionPresentModel);
        scrollPaneTable.setViewportView(vendorOptionPresentTable);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.output.SLDOutputInterface#updatedSLD(com.sldeditor.common.SLDDataInterface, org.geotools.styling.StyledLayerDescriptor)
     */
    @Override
    public void updatedSLD(SLDDataInterface sldData, StyledLayerDescriptor sld) {
        minimumVersion.findMinimumVersion(sld);

        vendorOptionPresentModel.populate(minimumVersion.getVendorOptionsPresentList());
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.preferences.iface.PrefUpdateVendorOptionInterface#vendorOptionsUpdated(java.util.List)
     */
    @Override
    public void vendorOptionsUpdated(List<VersionData> vendorOptionVersionsList) {
        vendorOptionModel.setSelectedVendorOptionVersions(vendorOptionVersionsList);
    }
}
