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

package com.sldeditor.tool.layerstyle;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sldeditor.common.Controller;
import com.sldeditor.common.data.GeoServerLayer;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.localisation.Localisation;

/**
 * Dialog allows a GeoServer layer style to be updated.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ConfigureLayerStyleDialog extends JDialog implements SelectedStyleInterface {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The geo server style tree. */
    private GeoServerStyleTree geoServerStyleTree = null;

    /** The ok button pressed. */
    private boolean okButtonPressed = false;

    /** The updated layer list. */
    private List<GeoServerLayer> updatedLayerList = null;

    /** The table. */
    private JTable table = null;

    /** The model. */
    private LayerStyleModel dataModel = new LayerStyleModel();

    /**
     * Instantiates a new update layer style dialog.
     */
    public ConfigureLayerStyleDialog() {
        setResizable(true);
        setTitle(Localisation.getString(ConfigureLayerStyleDialog.class,
                "ConfigureLayerStyleDialog.title"));
        setModalityType(ModalityType.APPLICATION_MODAL);
        setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        setModal(true);
        createUI();

        setSize(800, 400);

        Controller.getInstance().centreDialog(this);
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        table = new JTable(dataModel);

        dataModel.setColumnRenderer(table.getColumnModel());

        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                // Get selected rows and find the selected style.
                // If the selected layers all don't use the same style
                // then set to null
                int[] selectedRows = table.getSelectedRows();

                StyleWrapper selectedLayerStyle = null;
                boolean isUniqueStyle = true;

                for (int index = 0; index < selectedRows.length; index++) {
                    GeoServerLayer layer = dataModel.getLayer(selectedRows[index]);

                    if (selectedLayerStyle == null) {
                        selectedLayerStyle = layer.getStyle();
                    } else if (isUniqueStyle) {
                        if (selectedLayerStyle.compareTo(layer.getStyle()) != 0) {
                            isUniqueStyle = false;
                        }
                    }
                }

                geoServerStyleTree.select(isUniqueStyle ? selectedLayerStyle : null);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        //
        // Button panel
        //
        JPanel buttonPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.TRAILING);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton btnOk = new JButton(
                Localisation.getString(ConfigureLayerStyleDialog.class, "common.ok"));
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButtonPressed = false;

                updatedLayerList = dataModel.getUpdatedLayers();

                if (!updatedLayerList.isEmpty()) {
                    okButtonPressed = true;
                }
                setVisible(false);
            }
        });
        buttonPanel.add(btnOk);

        JButton btnCancel = new JButton(
                Localisation.getString(ConfigureLayerStyleDialog.class, "common.cancel"));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButtonPressed = false;
                setVisible(false);
            }
        });
        buttonPanel.add(btnCancel);

        geoServerStyleTree = new GeoServerStyleTree(this);
        getContentPane().add(geoServerStyleTree, BorderLayout.EAST);
    }

    /**
     * Populate the dialog.
     *
     * @param styleMap the style map
     * @param layerList the layer list
     * @return true, if successful
     */
    public boolean populate(Map<String, List<StyleWrapper>> styleMap,
            List<GeoServerLayer> layerList) {
        dataModel.populate(styleMap, layerList);
        dataModel.fireTableDataChanged();

        geoServerStyleTree.initialise();

        String geoserverName = "Unknown";

        if ((layerList != null) && !layerList.isEmpty()) {
            geoserverName = layerList.get(0).getConnection().getConnectionName();
        }
        geoServerStyleTree.populate(geoserverName, styleMap);

        setVisible(true);

        return okButtonPressed;
    }

    /**
     * Gets the updated layer styles.
     *
     * @return the updated layer styles
     */
    public List<GeoServerLayer> getUpdatedLayerStyles() {
        return updatedLayerList;
    }

    /**
     * Selected style.
     *
     * @param styleWrapper the style wrapper
     */
    @Override
    public void selectedStyle(StyleWrapper styleWrapper) {
        int[] selectedRows = table.getSelectedRows();

        dataModel.updateStyle(selectedRows, styleWrapper);
    }

}
