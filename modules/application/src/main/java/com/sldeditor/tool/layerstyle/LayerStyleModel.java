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

import com.sldeditor.common.data.GeoServerLayer;
import com.sldeditor.common.data.StyleWrapper;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

/**
 * The Class LayerStyleModel.
 *
 * @author Robert Ward (SCISYS)
 */
public class LayerStyleModel extends AbstractTableModel {

    /** The workspace column index. */
    private static final int COL_WORKSPACE = 0;

    /** The layer column index. */
    private static final int COL_LAYER = 1;

    /** The style column index. */
    private static final int COL_STYLE = 2;

    /** The column headings. */
    private String[] columnHeadings = {"Workspace", "Layer", "Style"};

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The layer map. */
    private Map<GeoServerLayer, Boolean> layerMap = new LinkedHashMap<GeoServerLayer, Boolean>();

    /** The layer list. */
    private List<GeoServerLayer> layerList = null;

    /**
     * Populate model.
     *
     * @param styleMap the style map
     * @param layerList the layer list
     */
    public void populate(Map<String, List<StyleWrapper>> styleMap, List<GeoServerLayer> layerList) {
        this.layerMap.clear();
        this.layerList = layerList;

        if (layerList != null) {
            for (GeoServerLayer layer : layerList) {
                this.layerMap.put(layer, Boolean.FALSE);
            }
        }
    }

    /**
     * Gets the row count.
     *
     * @return the row count
     */
    @Override
    public int getRowCount() {
        return this.layerMap.size();
    }

    /**
     * Gets the column count.
     *
     * @return the column count
     */
    @Override
    public int getColumnCount() {
        return columnHeadings.length;
    }

    /**
     * Gets the value at.
     *
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @return the value at
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        GeoServerLayer layer = getLayer(rowIndex);
        if (layer != null) {
            StyleWrapper style = layer.getStyle();

            switch (columnIndex) {
                case COL_WORKSPACE:
                    return layer.getLayerWorkspace();
                case COL_LAYER:
                    return layer.getLayerName();
                case COL_STYLE:
                    return getStyleName(style);
                default:
                    break;
            }
        }
        return null;
    }

    /**
     * Gets the layer.
     *
     * @param rowIndex the row index
     * @return the layer
     */
    public GeoServerLayer getLayer(int rowIndex) {
        GeoServerLayer layer = layerList.get(rowIndex);

        return layer;
    }

    /**
     * Gets the style name.
     *
     * @param style the style
     * @return the style name
     */
    private String getStyleName(StyleWrapper style) {
        return style.getWorkspace() + "/" + style.getStyle();
    }

    /**
     * Gets the column name.
     *
     * @param column the column
     * @return the column name
     */
    @Override
    public String getColumnName(int column) {
        return columnHeadings[column];
    }

    /**
     * Update style.
     *
     * @param selectedRows the selected rows
     * @param styleWrapper the style wrapper
     */
    public void updateStyle(int[] selectedRows, StyleWrapper styleWrapper) {
        for (int index = 0; index < selectedRows.length; index++) {
            GeoServerLayer layer = getLayer(selectedRows[index]);

            layer.setStyle(styleWrapper);

            layerMap.put(layer, Boolean.TRUE);
        }
        this.fireTableDataChanged();
    }

    /**
     * Checks for value been updated.
     *
     * @param row the row
     * @param column the column
     * @return true, if successful
     */
    public boolean hasValueBeenUpdated(int row, int column) {
        if (column == COL_STYLE) {
            GeoServerLayer layer = getLayer(row);

            return layerMap.get(layer);
        }
        return false;
    }

    /**
     * Sets the column renderers.
     *
     * @param columnModel the new column renderer
     */
    public void setColumnRenderer(TableColumnModel columnModel) {
        columnModel.getColumn(COL_STYLE).setCellRenderer(new LayerStyleTableRenderer());
    }

    /**
     * Gets the updated layers.
     *
     * @return the updated layers
     */
    public List<GeoServerLayer> getUpdatedLayers() {
        List<GeoServerLayer> updatedList = new ArrayList<GeoServerLayer>();

        for (GeoServerLayer layer : layerList) {
            if (layerMap.get(layer)) {
                updatedList.add(layer);
            }
        }
        return updatedList;
    }
}
