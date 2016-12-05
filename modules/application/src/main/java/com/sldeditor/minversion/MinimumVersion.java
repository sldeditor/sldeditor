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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.UserLayerImpl;

import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.ui.panels.SLDEditorUIPanels;

/**
 * The Class MinimumVersion.
 *
 * @author Robert Ward (SCISYS)
 */
public class MinimumVersion {

    /** The ui manager. */
    private SLDEditorUIPanels uiMgr = null;

    /** The vendor options present list. */
    private List<VendorOptionPresent> vendorOptionsPresentList = new ArrayList<VendorOptionPresent>();

    /** The default vendor option version. */
    private VendorOptionVersion defaultVendorOptionVersion = VendorOptionManager.getInstance().getDefaultVendorOptionVersion();

    /**
     * Instantiates a new minimum version.
     *
     * @param uiMgr the ui mgr
     */
    public MinimumVersion(SLDEditorUIPanels uiMgr) {
        this.uiMgr = uiMgr;
    }

    /**
     * Find minimum version.
     *
     * @param sld the sld
     */
    public void findMinimumVersion(StyledLayerDescriptor sld) {
        vendorOptionsPresentList.clear();
        Object parentObj = null;

        if ((sld != null) && (uiMgr != null)) {
            uiMgr.getMinimumVersion(parentObj, sld, vendorOptionsPresentList);
            List<StyledLayer> styledLayerList = sld.layers();

            if (styledLayerList != null) {
                parentObj = sld;
                for (StyledLayer styledLayer : styledLayerList) {
                    uiMgr.getMinimumVersion(parentObj, styledLayer, vendorOptionsPresentList);
                    List<Style> styleList = null;

                    if (styledLayer instanceof NamedLayerImpl) {
                        NamedLayerImpl namedLayerImpl = (NamedLayerImpl) styledLayer;
                        styleList = namedLayerImpl.styles();
                    } else if (styledLayer instanceof UserLayerImpl) {
                        UserLayerImpl userLayerImpl = (UserLayerImpl) styledLayer;
                        styleList = userLayerImpl.userStyles();
                    }

                    if (styleList != null) {
                        parentObj = styledLayer;
                        for (Style style : styleList) {
                            uiMgr.getMinimumVersion(parentObj, style, vendorOptionsPresentList);
                            parentObj = style;

                            for (FeatureTypeStyle fts : style.featureTypeStyles()) {
                                uiMgr.getMinimumVersion(parentObj, fts, vendorOptionsPresentList);
                                parentObj = fts;

                                for (Rule rule : fts.rules()) {
                                    uiMgr.getMinimumVersion(parentObj, rule, vendorOptionsPresentList);
                                    parentObj = rule;

                                    for (Symbolizer symbolizer : rule.symbolizers()) {
                                        uiMgr.getMinimumVersion(parentObj, symbolizer, vendorOptionsPresentList);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        removeStrictSLD();
    }

    /**
     * Removes the strict SLD.
     */
    private void removeStrictSLD() {
        List<VendorOptionPresent> newList = new ArrayList<VendorOptionPresent>();

        for(VendorOptionPresent obj : vendorOptionsPresentList)
        {
            if(obj.getVendorOptionInfo().getVersionData() != defaultVendorOptionVersion)
            {
                newList.add(obj);
            }
        }

        vendorOptionsPresentList = newList;

        Collections.sort(vendorOptionsPresentList);
    }

    /**
     * Gets the vendor options present list.
     *
     * @return the vendorOptionsPresentList
     */
    public List<VendorOptionPresent> getVendorOptionsPresentList() {
        return vendorOptionsPresentList;
    }
}
