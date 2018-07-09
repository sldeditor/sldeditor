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

package com.sldeditor.ui.detail;

import com.sldeditor.common.Controller;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import java.util.List;
import org.geotools.styling.Displacement;
import org.geotools.styling.PolygonSymbolizer;
import org.opengis.filter.expression.Expression;

/**
 * The Class PolygonSymbolizerDetails allows a user to configure polygon symbolizer data in a panel.
 *
 * @author Robert Ward (SCISYS)
 */
public class PolygonSymbolizerDetails extends StandardPanel
        implements PopulateDetailsInterface, UpdateSymbolInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Constructor. */
    public PolygonSymbolizerDetails() {
        super(PolygonSymbolizerDetails.class);

        createUI();
    }

    /** Creates the ui. */
    private void createUI() {

        readConfigFile(null, getClass(), this, "Polygon.xml");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#populate(com.sldeditor.ui.detail.selectedsymbol.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {

        if (selectedSymbol != null) {
            PolygonSymbolizer polygonSymbolizer =
                    (PolygonSymbolizer) selectedSymbol.getSymbolizer();
            if (polygonSymbolizer != null) {
                populateStandardData(polygonSymbolizer);

                fieldConfigVisitor.populateField(
                        FieldIdEnum.PERPENDICULAR_OFFSET,
                        polygonSymbolizer.getPerpendicularOffset());

                fieldConfigVisitor.populateField(
                        FieldIdEnum.GEOMETRY, polygonSymbolizer.getGeometry());
            }
        }
    }

    /** Update symbol. */
    private void updateSymbol() {
        if (!Controller.getInstance().isPopulating()) {
            Expression geometryField = ExtractGeometryField.getGeometryField(fieldConfigVisitor);

            Expression perpendicularOffset =
                    fieldConfigVisitor.getExpression(FieldIdEnum.PERPENDICULAR_OFFSET);

            //
            // Displacement
            //
            Displacement displacement = null;

            StandardData standardData = getStandardData();

            PolygonSymbolizer polygonSymbolizer =
                    (PolygonSymbolizer) SelectedSymbol.getInstance().getSymbolizer();

            if (polygonSymbolizer != null) {
                polygonSymbolizer.setName(standardData.name);
                polygonSymbolizer.setDescription(standardData.description);
                polygonSymbolizer.setUnitOfMeasure(
                        (standardData.unit != null) ? standardData.unit.getUnit() : null);

                polygonSymbolizer.setDisplacement(displacement);
                polygonSymbolizer.setGeometry(geometryField);
                polygonSymbolizer.setPerpendicularOffset(perpendicularOffset);

                this.fireUpdateSymbol();
            }
        }
    }

    /**
     * Data changed.
     *
     * @param changedField the changed field
     */
    @Override
    public void dataChanged(FieldIdEnum changedField) {
        updateSymbol();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.BasePanel#addRenderer(com.sldeditor.render.iface.RenderSymbolInterface)
     */
    @Override
    public void addRenderer(RenderSymbolInterface renderer) {
        super.addRenderer(renderer);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getFieldDataManager()
     */
    @Override
    public GraphicPanelFieldManager getFieldDataManager() {
        return this.fieldConfigManager;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#isDataPresent()
     */
    @Override
    public boolean isDataPresent() {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#initialseFields()
     */
    @Override
    public void preLoadSymbol() {
        setAllDefaultValues();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getMinimumVersion(java.lang.Object, java.util.List)
     */
    @Override
    public void getMinimumVersion(
            Object parentObj, Object sldObj, List<VendorOptionPresent> vendorOptionsPresentList) {
        // No vendor options
    }
}
