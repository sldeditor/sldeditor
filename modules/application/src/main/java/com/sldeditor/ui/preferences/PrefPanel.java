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
package com.sldeditor.ui.preferences;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.sldeditor.common.Controller;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.preferences.PrefData;
import com.sldeditor.common.preferences.VendorOptionTableModel;
import com.sldeditor.common.preferences.VersionCellEditor;
import com.sldeditor.common.preferences.VersionCellRenderer;
import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionTypeInterface;
import com.sldeditor.ui.layout.UILayoutFactory;

/**
 * Dialog that displays the user preferences to the user.
 * 
 * @author Robert Ward (SCISYS)
 */
public class PrefPanel extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The vendor option table. */
    private JTable vendorOptionTable;

    /** The model. */
    private VendorOptionTableModel model = null;

    /** The vendor options. */
    private Map<VendorOptionTypeInterface, String> options = new LinkedHashMap<VendorOptionTypeInterface, String>();

    /** The ok button pressed flag. */
    private boolean okPressed = false;

    /** The use anti alias checkbox. */
    private JCheckBox chckbxUseAntiAlias;

    /** The ui layout combo box. */
    private JComboBox<String> uiLayoutComboBox;

    /** The ui layout map. */
    private Map<String, String> uiLayoutMap;

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        PrefPanel panel = new PrefPanel();
        panel.pack();
        panel.setVisible(true);
    }

    /**
     * Default constructor
     */
    public PrefPanel()
    {
        setTitle(Localisation.getString(PrefPanel.class, "PrefPanel.title"));

        this.setModal(true);

        addVendorOption(VendorOptionManager.getInstance().getClass(GeoServerVendorOption.class));

        model = new VendorOptionTableModel(options);
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Anti-alias
        chckbxUseAntiAlias = new JCheckBox(Localisation.getString(PrefPanel.class, "PrefPanel.useAntiAlias"));
        panel.add(chckbxUseAntiAlias);

        // Ui layout class
        uiLayoutMap = UILayoutFactory.getAllLayouts();
        String[] uiLayoutNameList = new String[uiLayoutMap.size()];
        int index = 0;
        for(String key : uiLayoutMap.keySet())
        {
            uiLayoutNameList[index] = key;
            index ++;
        }
        uiLayoutComboBox = new JComboBox<String>(uiLayoutNameList);

        JPanel uiLayoutPanel = new JPanel();

        uiLayoutPanel.add(new JLabel(Localisation.getField(PrefPanel.class, "PrefPanel.uiLayout")));
        uiLayoutPanel.add(uiLayoutComboBox);
        panel.add(uiLayoutPanel);

        // Vendor options
        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Localisation.getString(PrefPanel.class, "PrefPanel.vendorOptions"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-2$
        panel.add(panel_1);

        JScrollPane scrollPane = new JScrollPane();
        panel_1.add(scrollPane);

        vendorOptionTable = new JTable();
        scrollPane.setViewportView(vendorOptionTable);

        vendorOptionTable.setModel(model);
        vendorOptionTable.getColumnModel().getColumn(1).setCellRenderer(new VersionCellRenderer());
        vendorOptionTable.getColumnModel().getColumn(1).setCellEditor(new VersionCellEditor(model));

        JPanel buttonPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
        flowLayout.setHgap(1);
        flowLayout.setAlignment(FlowLayout.TRAILING);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton btnOk = new JButton(Localisation.getString(PrefPanel.class, "common.ok"));
        btnOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okPressed = true;
                setVisible(false);
                dispose();
            }
        });
        buttonPanel.add(btnOk);

        JButton btnCancel = new JButton(Localisation.getString(PrefPanel.class, "common.cancel"));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okPressed = false;
                setVisible(false);
                dispose();
            }
        });
        buttonPanel.add(btnCancel);

        pack();

        Controller.getInstance().centreDialog(this);
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
     * Show dialog.
     *
     * @param prefData the pref data
     * @return true, if successful
     */
    public boolean showDialog(PrefData prefData) {

        chckbxUseAntiAlias.setSelected(prefData.isUseAntiAlias());
        model.setSelectedVendorOptionVersions(prefData.getVendorOptionVersionList());

        for(String displayName : uiLayoutMap.keySet())
        {
            String className = uiLayoutMap.get(displayName);

            if(prefData.getUiLayoutClass() != null)
            {
                if(className.compareTo(prefData.getUiLayoutClass()) == 0)
                {
                    uiLayoutComboBox.setSelectedItem(displayName);
                }
            }
        }

        setVisible(true);

        return okPressed;
    }

    /**
     * Get pref data.
     *
     * @return the pref data
     */
    public PrefData getPrefData() {

        PrefData prefData = new PrefData();

        prefData.setUseAntiAlias(chckbxUseAntiAlias.isSelected());
        prefData.setVendorOptionVersionList(model.getVendorOptionVersionList());
        String uiLayoutClass = uiLayoutMap.get(uiLayoutComboBox.getSelectedItem());
        prefData.setUiLayoutClass(uiLayoutClass);

        return prefData;
    }
}
