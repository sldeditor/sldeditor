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
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.detail.vendor.geoserver.featuretypestyle.VendorOptionFTSFactory;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import java.util.ArrayList;
import java.util.List;
import org.geotools.styling.FeatureTypeStyle;
import org.opengis.filter.expression.Expression;

/**
 * Panel that allows the setting and getting of FeatureTypeStyle data.
 *
 * @author Robert Ward (SCISYS)
 */
public class FeatureTypeStyleDetails extends StandardPanel
        implements PopulateDetailsInterface, UpdateSymbolInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The vendor option feature type style factory. */
    private VendorOptionFTSFactory vendorOptionFTSFactory = null;

    /** Constructor. */
    public FeatureTypeStyleDetails() {
        super(FeatureTypeStyleDetails.class);

        createUI();
    }

    /** Creates the ui. */
    private void createUI() {

        createVendorOptionPanel();

        readConfigFile(vendorOptionFTSFactory, getClass(), this, "FeatureTypeStyles.xml");
    }

    /**
     * Creates the vendor option panel.
     *
     * @return the detail panel
     */
    private void createVendorOptionPanel() {

        vendorOptionFTSFactory = new VendorOptionFTSFactory(getClass());

        List<VendorOptionInterface> veList = vendorOptionFTSFactory.getVendorOptionList();
        if (veList != null) {
            for (VendorOptionInterface extension : veList) {
                extension.setParentPanel(this);
            }
        }
    }

    /**
     * Populate.
     *
     * @param selectedSymbol the selected symbol
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#populate(com.sldeditor.ui.detail.
     * SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {
        if (selectedSymbol != null) {
            FeatureTypeStyle featureTypeStyle = selectedSymbol.getFeatureTypeStyle();

            populateStandardData(featureTypeStyle);

            if (featureTypeStyle != null) {
                fieldConfigVisitor.populateField(
                        FieldIdEnum.TRANSFORMATION, featureTypeStyle.getTransformation());
            }

            if (vendorOptionFTSFactory != null) {
                vendorOptionFTSFactory.populate(featureTypeStyle);
            }
        }
    }

    /**
     * Data changed.
     *
     * @param changedField the changed field
     */
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
            StandardData standardData = getStandardData();

            Expression transformation =
                    fieldConfigVisitor.getExpression(FieldIdEnum.TRANSFORMATION);

            FeatureTypeStyle existingFTS = SelectedSymbol.getInstance().getFeatureTypeStyle();
            if (existingFTS != null) {
                List<org.opengis.style.Rule> newRuleList = new ArrayList<org.opengis.style.Rule>();
                for (org.opengis.style.Rule rule : existingFTS.rules()) {
                    newRuleList.add(rule);
                }

                FeatureTypeStyle fts =
                        (FeatureTypeStyle)
                                getStyleFactory()
                                        .featureTypeStyle(
                                                standardData.name,
                                                (org.opengis.style.Description)
                                                        standardData.description,
                                                existingFTS.getFeatureInstanceIDs(),
                                                existingFTS.featureTypeNames(),
                                                existingFTS.semanticTypeIdentifiers(),
                                                newRuleList);

                if (transformation != null) {
                    fts.setTransformation(transformation);
                }

                if (vendorOptionFTSFactory != null) {
                    vendorOptionFTSFactory.updateSymbol(fts);
                }

                SelectedSymbol.getInstance().replaceFeatureTypeStyle(fts);

                this.fireUpdateSymbol();
            }
        }
    }

    /**
     * Gets the field data manager.
     *
     * @return the field data manager
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getFieldDataManager()
     */
    @Override
    public GraphicPanelFieldManager getFieldDataManager() {
        return fieldConfigManager;
    }

    /**
     * Checks if is data present.
     *
     * @return true, if is data present
     */
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
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getMinimumVersion(java.lang.Object,
     * java.util.List)
     */
    @Override
    public void getMinimumVersion(
            Object parentObj, Object sldObj, List<VendorOptionPresent> vendorOptionsPresentList) {
        vendorOptionFTSFactory.getMinimumVersion(parentObj, sldObj, vendorOptionsPresentList);
    }
}
