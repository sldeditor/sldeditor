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

package com.sldeditor.ui.detail.config.inlinefeature;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTConversion;
import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTDialog;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The Class InlineCellEditor.
 *
 * @author Robert Ward (SCISYS)
 */
public class InlineCellEditor extends AbstractCellEditor implements TableCellEditor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The text field. */
    private JTextField textField = new JTextField("");

    /** The model. */
    private InLineFeatureModel model = null;

    /**
     * Instantiates a new inline cell editor.
     *
     * @param model the model
     */
    public InlineCellEditor(InLineFeatureModel model) {
        this.model = model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    @Override
    public Object getCellEditorValue() {
        return this.textField.getText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable,
     * java.lang.Object, boolean, int, int)
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
            int row, int column) {

        if ((model != null) && column == model.getGeometryFieldIndex()) {
            WKTDialog wktDialog = new WKTDialog();

            String geometryString = null;
            Object obj = model.getValueAt(row, column);
            if (obj != null) {
                geometryString = (String) obj.toString();
            }

            if (wktDialog.showDialog(geometryString)) {
                String crsCode = model.getSelectedCRSCode();
                Geometry geometry = WKTConversion.convertToGeometry(wktDialog.getWKTString(),
                        crsCode);

                model.updateGeometry(row, geometry);
            }
        } else {
            this.textField.setFont(table.getFont());
            this.textField.setText(value.toString());
            return this.textField;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.AbstractCellEditor#isCellEditable(java.util.EventObject)
     */
    @Override
    public boolean isCellEditable(EventObject e) {
        if (super.isCellEditable(e)) {
            if (e instanceof MouseEvent) {
                MouseEvent me = (MouseEvent) e;
                return me.getClickCount() >= 2;
            }
        }
        return false;
    }

}
