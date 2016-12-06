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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.geotools.styling.StyledLayerDescriptor;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDOutputInterface;
import com.sldeditor.common.preferences.VendorOptionTableModel;
import com.sldeditor.common.preferences.VersionCellEditor;
import com.sldeditor.common.preferences.VersionCellRenderer;
import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionTypeInterface;
import com.sldeditor.common.vendoroption.VendorOptionUpdateInterface;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.vendoroption.info.VendorOptionInfoManager;
import com.sldeditor.common.vendoroption.info.VendorOptionInfoPanel;
import com.sldeditor.ui.panels.GetMinimumVersionInterface;

/**
 * The Class VendorOptionUI.
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionUI extends JPanel
        implements SLDOutputInterface, VendorOptionUpdateInterface {

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

    /** The vendor option present model. */
    private VendorOptionPresentModel vendorOptionPresentModel = new VendorOptionPresentModel();

    /** Find the minimum version supported by the SLD */
    private MinimumVersion minimumVersion = null;

    /** The latest button. */
    private JButton btnLatestVO;

    /** The minimum vendor option button. */
    private JButton btnMinimumVendorOption;

    /**
     * Instantiates a new vendor option UI.
     *
     * @param uiMgr the ui mgr
     */
    public VendorOptionUI(GetMinimumVersionInterface uiMgr) {
        addVendorOption(VendorOptionManager.getInstance().getClass(GeoServerVendorOption.class));

        createUI();

        minimumVersion = new MinimumVersion(uiMgr);

        VendorOptionManager.getInstance().addVendorOptionListener(this);
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
        JPanel vendorOptionSelectionPanel = new JPanel();
        vendorOptionSelectionPanel
                .setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
                        Localisation.getString(VendorOptionUI.class,
                                "VendorOptionUI.vendorOptions"),
                        TitledBorder.LEADING, TitledBorder.TOP, null, null));
        vendorOptionSelectionPanel.setLayout(new BorderLayout());
        add(vendorOptionSelectionPanel);

        vendorOptionTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(vendorOptionTable);
        vendorOptionSelectionPanel.add(scrollPane, BorderLayout.CENTER);

        vendorOptionSelectionPanel.setPreferredSize(new Dimension(400, 100));
        vendorOptionModel = new VendorOptionTableModel(options);

        vendorOptionTable.setModel(vendorOptionModel);
        vendorOptionTable.getColumnModel().getColumn(1).setCellRenderer(new VersionCellRenderer());
        vendorOptionTable.getColumnModel().getColumn(1)
                .setCellEditor(new VersionCellEditor(vendorOptionModel));

        // Vendor option information
        VendorOptionInfoPanel vendorOptionInfoPanel = VendorOptionInfoManager.getInstance()
                .getPanel();
        vendorOptionInfoPanel.setPreferredSize(new Dimension(400, 200));
        add(vendorOptionInfoPanel);

        // VendorOption present table
        JPanel voPresentPanel = new JPanel();
        voPresentPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
                Localisation.getString(VendorOptionUI.class, "VendorOptionUI.loadedSymbol"),
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        voPresentPanel.setLayout(new BorderLayout());
        voPresentPanel.setPreferredSize(new Dimension(400, 300));

        vendorOptionPresentTable = new JTable();
        vendorOptionPresentTable.setModel(vendorOptionPresentModel);

        JScrollPane scrollPaneTable = new JScrollPane(vendorOptionPresentTable);
        voPresentPanel.add(scrollPaneTable, BorderLayout.CENTER);

        add(voPresentPanel);

        JPanel panel = new JPanel();
        voPresentPanel.add(panel, BorderLayout.SOUTH);

        btnLatestVO = new JButton(
                Localisation.getString(VendorOptionUI.class, "VendorOptionUI.latest"));
        btnLatestVO.setEnabled(false);
        btnLatestVO.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                vendorOptionsUpdated(VendorOptionManager.getInstance().getLatest());
            }
        });
        panel.add(btnLatestVO);

        btnMinimumVendorOption = new JButton(
                Localisation.getString(VendorOptionUI.class, "VendorOptionUI.minimumVO"));
        btnMinimumVendorOption.setEnabled(false);
        btnMinimumVendorOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                vendorOptionsUpdated(vendorOptionPresentModel.getMinimum());
            }
        });
        panel.add(btnMinimumVendorOption);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.output.SLDOutputInterface#updatedSLD(com.sldeditor.common.SLDDataInterface,
     * org.geotools.styling.StyledLayerDescriptor)
     */
    @Override
    public void updatedSLD(SLDDataInterface sldData, StyledLayerDescriptor sld) {
        updatePanel(sld);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.preferences.iface.PrefUpdateVendorOptionInterface#vendorOptionsUpdated(java.util.List)
     */
    @Override
    public void vendorOptionsUpdated(List<VersionData> vendorOptionVersionsList) {
        vendorOptionModel.setSelectedVendorOptionVersions(vendorOptionVersionsList);
    }

    /**
     * @param instance
     */
    public void populate(SelectedSymbol selectedSymbol) {
        if (selectedSymbol != null) {
            StyledLayerDescriptor sld = selectedSymbol.getSld();
            updatePanel(sld);
        }
    }

    /**
     * Update panel.
     *
     * @param sld the sld
     */
    private void updatePanel(StyledLayerDescriptor sld) {
        minimumVersion.findMinimumVersion(sld);

        vendorOptionPresentModel.populate(minimumVersion.getVendorOptionsPresentList());

        boolean hasVendorOptionData = (vendorOptionPresentModel.getRowCount() > 0);
        btnLatestVO.setEnabled(hasVendorOptionData);
        btnMinimumVendorOption.setEnabled(hasVendorOptionData);
    }
}
