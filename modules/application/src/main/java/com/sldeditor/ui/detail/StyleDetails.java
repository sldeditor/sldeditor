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
import java.util.ArrayList;
import java.util.List;
import org.geotools.styling.Style;
import org.geotools.styling.Symbolizer;

/**
 * The Class StyleDetails allows a user to configure style data in a panel.
 *
 * @author Robert Ward (SCISYS)
 */
public class StyleDetails extends StandardPanel
        implements PopulateDetailsInterface, UpdateSymbolInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Constructor. */
    public StyleDetails() {
        super(StyleDetails.class);
        createUI();
    }

    /** Creates the ui. */
    private void createUI() {
        readConfigFile(null, getClass(), this, "StyleDetails.xml");
    }

    /**
     * Populate.
     *
     * @param selectedSymbol the selected symbol
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#populate(com.sldeditor.ui.detail.selectedsymbol.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {

        if (selectedSymbol != null) {
            Style style = selectedSymbol.getStyle();

            populateStandardData(style);
            if (style != null) {
                fieldConfigVisitor.populateBooleanField(
                        FieldIdEnum.DEFAULT_STYLE, style.isDefault());
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
     * @see com.sldeditor.ui.iface.UpdateSymbolInterface#dataChanged(com.sldeditor.ui.detail.config.xml.FieldIdEnum)
     */
    @Override
    public void dataChanged(FieldIdEnum changedField) {
        updateSymbol();
    }

    /** Update symbol. */
    private void updateSymbol() {
        if (!Controller.getInstance().isPopulating()) {
            StandardData standardData = getStandardData();

            boolean isDefault = fieldConfigVisitor.getBoolean(FieldIdEnum.DEFAULT_STYLE);

            Style existingStyle = SelectedSymbol.getInstance().getStyle();
            if (existingStyle != null) {
                List<org.opengis.style.FeatureTypeStyle> newFTSList =
                        new ArrayList<org.opengis.style.FeatureTypeStyle>();
                for (org.opengis.style.FeatureTypeStyle fts : existingStyle.featureTypeStyles()) {
                    newFTSList.add(fts);
                }

                Symbolizer defaultSymbolizer = null;
                Style newStyle =
                        (Style)
                                getStyleFactory()
                                        .style(
                                                standardData.name,
                                                standardData.description,
                                                isDefault,
                                                newFTSList,
                                                defaultSymbolizer);

                SelectedSymbol.getInstance().replaceStyle(newStyle);

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
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getMinimumVersion(java.lang.Object, java.util.List)
     */
    @Override
    public void getMinimumVersion(
            Object parentObj, Object sldObj, List<VendorOptionPresent> vendorOptionsPresentList) {
        // No vendor options
    }
}
