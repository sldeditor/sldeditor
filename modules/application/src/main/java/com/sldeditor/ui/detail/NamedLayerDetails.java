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
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import java.util.List;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;

/**
 * The Class NamedLayerDetails allows a user to configure named layer data in a panel.
 *
 * @author Robert Ward (SCISYS)
 */
public class NamedLayerDetails extends StandardPanel
        implements PopulateDetailsInterface, UpdateSymbolInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Constructor. */
    public NamedLayerDetails() {
        super(NamedLayerDetails.class);

        createUI();
    }

    /** Creates the ui. */
    private void createUI() {
        readConfigFile(null, getClass(), this, "NamedLayer.xml");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#populate(com.sldeditor.ui.detail.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {

        if (selectedSymbol != null) {
            StyledLayer styledLayer = selectedSymbol.getStyledLayer();
            if (styledLayer instanceof NamedLayerImpl) {
                NamedLayerImpl namedLayer = (NamedLayerImpl) styledLayer;

                fieldConfigVisitor.populateTextField(FieldIdEnum.NAME, namedLayer.getName());

                // Feature layer constraint
                List<FeatureTypeConstraint> ftcList = namedLayer.layerFeatureConstraints();

                fieldConfigVisitor.populateFieldTypeConstraint(
                        FieldIdEnum.LAYER_FEATURE_CONSTRAINTS, ftcList);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.iface.UpdateSymbolInterface#dataChanged()
     */
    @Override
    public void dataChanged(FieldIdEnum changedField) {
        updateSymbol();
    }

    /** Update symbol. */
    private void updateSymbol() {
        if (!Controller.getInstance().isPopulating()) {
            String name = fieldConfigVisitor.getText(FieldIdEnum.NAME);
            NamedLayer namedLayer = getStyleFactory().createNamedLayer();
            namedLayer.setName(name);

            // Feature type constraints
            List<FeatureTypeConstraint> ftcList =
                    fieldConfigVisitor.getFeatureTypeConstraint(
                            FieldIdEnum.LAYER_FEATURE_CONSTRAINTS);
            if ((ftcList != null) && !ftcList.isEmpty()) {
                FeatureTypeConstraint[] ftcArray = new FeatureTypeConstraint[ftcList.size()];
                namedLayer.setLayerFeatureConstraints(ftcList.toArray(ftcArray));
            }

            StyledLayer existingStyledLayer = SelectedSymbol.getInstance().getStyledLayer();
            if (existingStyledLayer instanceof NamedLayerImpl) {
                NamedLayerImpl existingNamedLayer = (NamedLayerImpl) existingStyledLayer;

                for (Style style : existingNamedLayer.styles()) {
                    namedLayer.addStyle(style);
                }
            }
            SelectedSymbol.getInstance().replaceStyledLayer(namedLayer);

            this.fireUpdateSymbol();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getFieldDataManager()
     */
    @Override
    public GraphicPanelFieldManager getFieldDataManager() {
        return fieldConfigManager;
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
